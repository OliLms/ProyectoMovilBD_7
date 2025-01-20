package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import POJO.contactos;

public class Editarcontactos extends AppCompatActivity {
    private EditText etNameEdit, etPhoneEdit, etAddressEdit, etEmailEdit, etAgeEdit, etDescriptionEdit;
    private Spinner spinnerGenderEdit, spinnerTypeEdit;
    private Button btnGuardarCambios, btnAnterior, btnSiguiente;
    private Toolbar toolbar;
    private int currentIndex;
    private ArrayList<contactos> listaContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editarcontactos);

        etNameEdit = findViewById(R.id.etNameEdit);
        etPhoneEdit = findViewById(R.id.etPhoneEdit);
        etAddressEdit = findViewById(R.id.etAddressEdit);
        etEmailEdit = findViewById(R.id.etEmailEdit);
        etAgeEdit = findViewById(R.id.etAgeEdit);
        spinnerGenderEdit = findViewById(R.id.spinnerGenderEdit);
        spinnerTypeEdit = findViewById(R.id.spinnerTypeEdit);
        etDescriptionEdit = findViewById(R.id.etDescriptionEdit);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        btnAnterior = findViewById(R.id.btnAnterior);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaContactos = ContactosListaGlobal.getInstancia().getListaContactos();

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.generos,
                android.R.layout.simple_spinner_item
        );
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenderEdit.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.tipos,
                android.R.layout.simple_spinner_item
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeEdit.setAdapter(typeAdapter);

        Intent intent = getIntent();
        etNameEdit.setText(intent.getStringExtra("nombre"));
        etPhoneEdit.setText(intent.getStringExtra("telefono"));
        etAddressEdit.setText(intent.getStringExtra("direccion"));
        etEmailEdit.setText(intent.getStringExtra("correo"));
        etAgeEdit.setText(String.valueOf(intent.getIntExtra("edad", 0)));
        etDescriptionEdit.setText(intent.getStringExtra("descripcion"));

        String genero = intent.getStringExtra("genero");
        if (genero != null) {
            int spinnerPosition = genderAdapter.getPosition(genero);
            spinnerGenderEdit.setSelection(spinnerPosition);
        }

        String tipo = intent.getStringExtra("tipo");
        if (tipo != null) {
            int spinnerPosition = typeAdapter.getPosition(tipo);
            spinnerTypeEdit.setSelection(spinnerPosition);
        }

        btnGuardarCambios.setOnClickListener(v -> guardarCambios());
        btnAnterior.setOnClickListener(v -> mostrarContactoAnterior());
        btnSiguiente.setOnClickListener(v -> mostrarContactoSiguiente());

        currentIndex = listaContactos.indexOf(new contactos(
                intent.getStringExtra("nombre"),
                intent.getStringExtra("telefono"),
                intent.getStringExtra("direccion"),
                intent.getStringExtra("correo"),
                intent.getIntExtra("edad", 0),
                intent.getStringExtra("genero"),
                intent.getStringExtra("tipo"),
                intent.getStringExtra("descripcion")
        ));
    }

    private void guardarCambios() {
        contactos contactoEditado = listaContactos.get(currentIndex);
        contactoEditado.setNombre(etNameEdit.getText().toString());
        contactoEditado.setTelefono(etPhoneEdit.getText().toString());
        contactoEditado.setDireccion(etAddressEdit.getText().toString());
        contactoEditado.setCorreo(etEmailEdit.getText().toString());
        contactoEditado.setEdad(Integer.parseInt(etAgeEdit.getText().toString()));
        contactoEditado.setGenero(spinnerGenderEdit.getSelectedItem().toString());
        contactoEditado.setTipo(spinnerTypeEdit.getSelectedItem().toString());
        contactoEditado.setDescripcion(etDescriptionEdit.getText().toString());

        Toast.makeText(Editarcontactos.this, "Cambios guardados", Toast.LENGTH_SHORT).show();
    }
    private void mostrarContactoAnterior() {
        if (currentIndex > 0) {
            currentIndex--;
            mostrarContacto(); }
    }

    private void mostrarContactoSiguiente() {
        if (currentIndex < listaContactos.size() - 1) {
            currentIndex++;
            mostrarContacto(); }
    }
  private void mostrarContacto(){
      contactos contacto = ContactosListaGlobal.getInstancia().getListaContactos().get(currentIndex);
      etNameEdit.setText(contacto.getNombre());
      etPhoneEdit.setText(contacto.getTelefono());
      etAddressEdit.setText(contacto.getDireccion());
      etEmailEdit.setText(contacto.getCorreo());
      etAgeEdit.setText(String.valueOf(contacto.getEdad()));
      etDescriptionEdit.setText(contacto.getDescripcion());

      int genderPosition = ((ArrayAdapter) spinnerGenderEdit.getAdapter()).getPosition(contacto.getGenero());
      spinnerGenderEdit.setSelection(genderPosition);

      int typePosition = ((ArrayAdapter) spinnerTypeEdit.getAdapter()).getPosition(contacto.getTipo());
      spinnerTypeEdit.setSelection(typePosition);
    }
}
