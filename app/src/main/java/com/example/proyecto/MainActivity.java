package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText userEditText, passwordEditText;
    private Button loginButton, CrearCuentaButton, CambiarContrasenaButton;
    private final String URL_API = "http://10.0.2.2/login.php"; // Cambia a la URL de tu servidor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias a los elementos del layout
        userEditText = findViewById(R.id.user);
        passwordEditText = findViewById(R.id.contrasena);
        loginButton = findViewById(R.id.btnLogin);
        CambiarContrasenaButton = findViewById(R.id.btnCambiarContrasena);
        CrearCuentaButton = findViewById(R.id.btnCrearCuenta);

        // Conectar los botones a sus actividades respectivas
        CrearCuentaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a la actividad de CrearCuenta
                Intent intent = new Intent(MainActivity.this, CrearCuenta.class);
                startActivity(intent);
            }
        });

        CambiarContrasenaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a la actividad de CambiarContraseña
                Intent intent = new Intent(MainActivity.this, CambiarContrasena.class);
                startActivity(intent);
            }
        });

        // Acción cuando se hace clic en el botón de login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores ingresados por el usuario
                String username = userEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (!username.isEmpty() && !password.isEmpty()) {
                    realizarLogin(username, password);
                } else {
                    Toast.makeText(MainActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void realizarLogin(String username, String password) {
        // Crear cliente OkHttp
        OkHttpClient client = new OkHttpClient();

        // Crear cuerpo JSON
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("usuario", username);
            jsonObject.put("contraseña", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear RequestBody
        RequestBody body = RequestBody.create(
                jsonObject.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        // Crear Request
        Request request = new Request.Builder()
                .url(URL_API)
                .post(body)
                .build();

        // Realizar la solicitud
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseString = response.body().string();
                    try {
                        JSONObject responseJson = new JSONObject(responseString);
                        boolean success = responseJson.getBoolean("success");

                        if (success) {
                            runOnUiThread(() -> {
                                Toast.makeText(MainActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();
                                // Navegar a la siguiente actividad
                                Intent intent = new Intent(MainActivity.this, vercontactos.class);
                                startActivity(intent);
                                finish(); // Cerrar la actividad actual
                            });
                        } else {
                            String message = responseJson.getString("message");
                            runOnUiThread(() ->
                                    Toast.makeText(MainActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(MainActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
