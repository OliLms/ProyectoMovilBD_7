package com.example.proyecto;

import android.os.Bundle;
import android.text.TextUtils;
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

public class CrearCuenta extends AppCompatActivity {

    private EditText userEditText, passwordEditText;
    private Button crearCuentaButton;
    private final String URL_API = "http://10.0.2.2/Crear.php"; // Cambia esta URL según tu configuración

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta); // Cambia si el archivo XML tiene un nombre distinto

        // Inicializar vistas
        userEditText = findViewById(R.id.user);
        passwordEditText = findViewById(R.id.contrasena);
        crearCuentaButton = findViewById(R.id.btnCrearCuenta);

        // Configurar botón
        crearCuentaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(CrearCuenta.this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    registrarUsuario(username, password);
                }
            }
        });
    }

    private void registrarUsuario(String username, String password) {
        OkHttpClient client = new OkHttpClient();

        // Crear JSON
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("usuario", username);
            jsonObject.put("contraseña", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear cuerpo de solicitud
        RequestBody body = RequestBody.create(
                jsonObject.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        // Crear solicitud
        Request request = new Request.Builder()
                .url(URL_API)
                .post(body)
                .build();

        // Enviar solicitud
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(CrearCuenta.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseString = response.body().string();
                    try {
                        JSONObject responseJson = new JSONObject(responseString);
                        boolean success = responseJson.getBoolean("success");

                        runOnUiThread(() -> {
                            if (success) {
                                Toast.makeText(CrearCuenta.this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show();
                                finish(); // Cerrar la actividad actual
                            } else {
                                String message = null;
                                try {
                                    message = responseJson.getString("message");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                Toast.makeText(CrearCuenta.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(CrearCuenta.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}
