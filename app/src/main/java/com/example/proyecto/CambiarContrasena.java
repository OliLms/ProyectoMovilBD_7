package com.example.proyecto;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CambiarContrasena extends AppCompatActivity {
    private EditText userEditText;
    private EditText contrasenaEditText;
    private Button btnCrearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);

        userEditText = findViewById(R.id.user);
        contrasenaEditText = findViewById(R.id.contrasena);
        btnCrearCuenta = findViewById(R.id.btnCambiarContrasena);

        btnCrearCuenta.setText("Cambiar Contraseña");
        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = userEditText.getText().toString().trim();
                String nuevaContrasena = contrasenaEditText.getText().toString().trim();

                if (usuario.isEmpty() || nuevaContrasena.isEmpty()) {
                    Toast.makeText(CambiarContrasena.this, "Llena todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    cambiarContraseña(usuario, nuevaContrasena);
                }
            }
        });
    }

    private void cambiarContraseña(String usuario, String nuevaContrasena) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/cambiar.php"); // Cambia por tu URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setDoOutput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("usuario", usuario);
                jsonParam.put("nueva_contrasena", nuevaContrasena);

                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(jsonParam.toString());
                writer.flush();
                writer.close();

                int responseCode = connection.getResponseCode();
                InputStream inputStream;
                if (responseCode >= 200 && responseCode < 400) {
                    // Si la respuesta es exitosa
                    inputStream = connection.getInputStream();
                } else {
                    // Si la respuesta tiene un error
                    inputStream = connection.getErrorStream();
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                bufferedReader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());

                runOnUiThread(() -> {
                    try {
                        if (jsonResponse.getBoolean("success")) {
                            Toast.makeText(CambiarContrasena.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CambiarContrasena.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(CambiarContrasena.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(CambiarContrasena.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();

    }
}
