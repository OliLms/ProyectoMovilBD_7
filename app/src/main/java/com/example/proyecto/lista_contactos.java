package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import POJO.contactos;
import adaptadores.adaptadorcontacto;

public class lista_contactos extends AppCompatActivity implements adaptadorcontacto.OnContactoClickListener {
    private RecyclerView recyclerView;
    private adaptadorcontacto adapter;
    private ArrayList<contactos> listaContactos;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos); // Usa activity_lista_contactos.xml

        // Inicializar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewContactos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar lista de contactos
        listaContactos = new ArrayList<>();
        cargarContactos();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Configurar adaptador
        adapter = new adaptadorcontacto(this, listaContactos, this);
        recyclerView.setAdapter(adapter);
    }

    private void cargarContactos() {
        // Crear contactos precargados
        listaContactos.add(new contactos("Victoria Zarazúa", "1234567890", "Calle Falsa 123", "juan@example.com", 25, "Masculino", "Estudiante", "Amigo de la universidad"));
        listaContactos.add(new contactos("Natalia Camacho", "0987654321", "Av. Siempre Viva 742", "ana@example.com", 30, "Femenino","Maestro", "Compañera de trabajo"));
        listaContactos.add(new contactos("Paulo Santoyo", "5555555555", "Calle Luna 456", "carlos@example.com", 28, "Masculino", "Administrativo", "Vecino"));
    }

    public void onContactoClick(contactos contacto){
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

