package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

import POJO.contactos;
import adaptadores.adaptadorcontacto;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class vercontactos extends AppCompatActivity implements adaptadorcontacto.OnContactoClickListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private adaptadorcontacto adaptador;
    private ArrayList<contactos> listaContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vercontactos);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerContactos);

        listaContactos = new ArrayList<>();

        adaptador = new adaptadorcontacto(this, listaContactos, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarListaContactos();
    }

    private void actualizarListaContactos() {
        new Thread(() -> {
            // Realizamos la solicitud para obtener los contactos desde el servidor
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2/ver.php") // Cambia esto si tu servidor está en una IP diferente
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> Toast.makeText(vercontactos.this, "Error al obtener los contactos", Toast.LENGTH_SHORT).show());
                    Log.e("API_ERROR", "Error en la solicitud: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        runOnUiThread(() -> Toast.makeText(vercontactos.this, "Error en la respuesta del servidor: " + response.message(), Toast.LENGTH_SHORT).show());
                        Log.e("API_ERROR", "Error en la respuesta del servidor: " + response.message());
                        return;
                    }

                    // Obtener la respuesta completa
                    String jsonResponse = response.body().string();
                    Log.d("API_RESPONSE", "Respuesta del servidor: " + jsonResponse); // Imprime la respuesta

                    try {
                        // Procesar la respuesta JSON
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        boolean success = jsonObject.getBoolean("success");

                        if (success) {
                            JSONArray contactosArray = jsonObject.getJSONArray("contactos");
                            listaContactos.clear();

                            // Recorrer el array de contactos y agregar cada uno a la lista
                            for (int i = 0; i < contactosArray.length(); i++) {
                                JSONObject contactoJson = contactosArray.getJSONObject(i);

                                String nombre = contactoJson.getString("nombre");
                                String telefono = contactoJson.getString("telefono");
                                String direccion = contactoJson.getString("direccion");
                                String correo = contactoJson.getString("correo");
                                int edad = contactoJson.getInt("edad");
                                String genero = contactoJson.getString("genero");
                                String tipo = contactoJson.getString("tipo");
                                String descripcion = contactoJson.getString("descripcion");

                                // Crear un objeto de contacto y agregarlo a la lista
                                contactos contacto = new contactos(nombre, telefono, direccion, correo, edad, genero, tipo, descripcion);
                                listaContactos.add(contacto);
                            }

                            runOnUiThread(() -> adaptador.notifyDataSetChanged());
                        } else {
                            runOnUiThread(() -> Toast.makeText(vercontactos.this, "No se encontraron contactos", Toast.LENGTH_SHORT).show());
                        }
                    } catch (Exception e) {
                        runOnUiThread(() -> Toast.makeText(vercontactos.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show());
                        Log.e("API_ERROR", "Error al procesar la respuesta", e);
                    }
                }
            });
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.opc1) {
            Intent intent = new Intent(vercontactos.this, vercontactos.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.opc2) {
            Intent intent = new Intent(vercontactos.this, Registro.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.opc3) {
            Intent intent = new Intent(vercontactos.this, Editarcontactos.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.opc4) {
            Intent intent = new Intent(vercontactos.this, Eliminarcontactos.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.opc5) {
            finish(); // Cierra la actividad actual
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onContactoClick(contactos contacto) {
        Intent intent = new Intent(this, Editarcontactos.class);
        intent.putExtra("nombre", contacto.getNombre());
        intent.putExtra("telefono", contacto.getTelefono());
        intent.putExtra("direccion", contacto.getDireccion());
        intent.putExtra("correo", contacto.getCorreo());
        intent.putExtra("edad", contacto.getEdad());
        intent.putExtra("genero", contacto.getGenero());
        intent.putExtra("tipo", contacto.getTipo());
        intent.putExtra("descripcion", contacto.getDescripcion());
        startActivity(intent);
    }
}
