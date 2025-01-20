package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import POJO.contactos;
import adaptadores.EliminarcontactosAdaptador;

public class Eliminarcontactos extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView rvContactos;
    private EliminarcontactosAdaptador adaptador;
    private ArrayList<contactos> listaContactos;
    private Button btnConfirmarEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminarcontactos);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Eliminar Contactos");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rvContactos = findViewById(R.id.rvContactos);
        btnConfirmarEliminar = findViewById(R.id.btnConfirmarEliminar);

        rvContactos.setLayoutManager(new LinearLayoutManager(this));

        listaContactos = new ArrayList<>();
        adaptador = new EliminarcontactosAdaptador(this, listaContactos);
        rvContactos.setLayoutManager(new LinearLayoutManager(this));
        rvContactos.setAdapter(adaptador);

        btnConfirmarEliminar.setOnClickListener(v -> eliminarContactosSeleccionados());
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarListaContactos();
    }

    private void actualizarListaContactos() {
        listaContactos.clear();
        listaContactos.addAll(ContactosListaGlobal.getInstancia().obtenerContactos());
        adaptador.notifyDataSetChanged();
    }

    private void eliminarContactosSeleccionados() {
        ArrayList<contactos> contactosSeleccionados = adaptador.obtenerContactosSeleccionados();

        if (contactosSeleccionados.size() > 0) {
            for (contactos contacto : contactosSeleccionados) {
                ContactosListaGlobal.getInstancia().eliminarContacto(contacto);
            }
            actualizarListaContactos();
            Toast.makeText(this, "Contactos eliminados", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se seleccionaron contactos", Toast.LENGTH_SHORT).show();
        }
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
            Intent intent = new Intent(Eliminarcontactos.this, vercontactos.class);
            startActivity(intent);
            return true;
        }

        // Opción para agregar un contacto
        else if (id == R.id.opc2) {
            Intent intent = new Intent(Eliminarcontactos.this, Registro.class);
            startActivity(intent);
            return true;
        }

        // Opción para editar un contacto
        else if (id == R.id.opc3) {
            Intent intent = new Intent(Eliminarcontactos.this, Editarcontactos.class);
            startActivity(intent);
            return true;
        }

        // Opción para eliminar contactos
        else if (id == R.id.opc4) {
            Intent intent = new Intent(Eliminarcontactos.this, Eliminarcontactos.class);
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


}