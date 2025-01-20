package com.example.proyecto;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import POJO.contactos;
import adaptadores.adaptadorcontacto;
public class vercontactos extends AppCompatActivity implements adaptadorcontacto.OnContactoClickListener{

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
            listaContactos.clear();
            listaContactos.addAll(ContactosListaGlobal.getInstancia().obtenerContactos());
            runOnUiThread(() -> adaptador.notifyDataSetChanged());
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

        // Opción para ver contactos
        if (id == R.id.opc1) {
            Intent intent = new Intent(vercontactos.this, vercontactos.class);
            startActivity(intent);
            return true;
        }

        // Opción para agregar un contacto
        else if (id == R.id.opc2) {
            Intent intent = new Intent(vercontactos.this, Registro.class);
            startActivity(intent);
            return true;
        }
        // Opción para editar un contacto
        else if (id == R.id.opc3) {
            Intent intent = new Intent(vercontactos.this, Editarcontactos.class);
            startActivity(intent);
            return true;
        }

        // Opción para eliminar contactos
        else if (id == R.id.opc4) {
            Intent intent = new Intent(vercontactos.this, Eliminarcontactos.class);
            startActivity(intent);
            return true;
        }
        // Opción para salir
        else if (id == R.id.opc5) {
            finish(); // Cierra la actividad actual
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onContactoClick(contactos contacto) {
        Intent intent = new Intent(this, Editarcontactos.class);
        intent.putExtra("nombre", contacto.getNombre());
        intent.putExtra("telefono", contacto.getTelefono());
        intent.putExtra("direccion", contacto.getDireccion());
        intent.putExtra("correo", contacto.getCorreo());
        intent.putExtra("edad", contacto.getEdad());
        intent.putExtra("genero", contacto.getGenero());
        intent.putExtra("tipo", contacto.getTipo());
        intent.putExtra("descripcion", contacto.getDescripcion()); // Añadir otros campos según sea necesario startActivity(intent); }
        startActivity(intent);
    }
}