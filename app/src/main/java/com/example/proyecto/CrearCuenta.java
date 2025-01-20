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

public class CrearCuenta extends AppCompatActivity {

    private EditText userEditText, passwordEditText;
    private Button crearCuentaButton;
    private final String URL_API = "http://10.0.2.2/crear.php"; // Cambia a la URL de tu servidor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        // Referencias a los elementos del layout
        userEditText = findViewById(R.id.user);
        passwordEditText = findViewById(R.id.contrasena);
        crearCuentaButton = findViewById(R.id.btnCrearCuenta);

        // Acción del botón "Crear Cuenta"
        crearCuentaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores ingresados
                String username = userEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (!username.isEmpty() && !password.isEmpty()) {
                    crearUsuario(username, password);
                } else {
                    Toast.makeText(CrearCuenta.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void crearUsuario(String username, String password) {
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
                        Toast.makeText(CrearCuenta.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseString = response.body().string();
                    try {
                        // Verifica si responseString es un JSON válido
                        JSONObject responseJson = new JSONObject(responseString);
                        boolean success = responseJson.getBoolean("success");

                        if (success) {
                            runOnUiThread(() -> {
                                Toast.makeText(CrearCuenta.this, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CrearCuenta.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            });
                        } else {
                            String message = responseJson.getString("message");
                            runOnUiThread(() ->
                                    Toast.makeText(CrearCuenta.this, "Error: " + message, Toast.LENGTH_SHORT).show());
                        }
                    } catch (JSONException e) {
                        // Aquí manejamos el error JSON
                        runOnUiThread(() ->
                                Toast.makeText(CrearCuenta.this, "Respuesta no es un JSON válido: " + responseString, Toast.LENGTH_SHORT).show());
                    }
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(CrearCuenta.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show());
                }
            }


        });
    }
}
