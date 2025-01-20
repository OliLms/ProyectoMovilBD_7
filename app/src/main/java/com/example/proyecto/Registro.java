package com.example.proyecto;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import java.util.Calendar;

import POJO.contactos;
import POJO.datos;

public class Registro extends AppCompatActivity {

    EditText etName, etPhone, etAddress, etEmail, etAge, etDescription, etFecha, etHora;
    Spinner spinnerTipo;
    Button btnRegister;
    datos datos = new datos(); // POJO para almacenar los datos
    SharedPreferences archivo;
    Toolbar toolbar;
    RadioGroup radioGroupGenero;
    RadioButton radioButtonSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro); // Asegúrate de usar el nombre correcto del XML
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etFecha = findViewById(R.id.fecha);
        etHora = findViewById(R.id.hora);
        etAddress = findViewById(R.id.etAddress);
        etEmail = findViewById(R.id.etEmail);
        etAge = findViewById(R.id.etAge);
        etDescription = findViewById(R.id.etDescription);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        radioGroupGenero = findViewById(R.id.botongenero);
        btnRegister = findViewById(R.id.btnRegister);

        // Configurar selección de fecha
        etFecha.setOnClickListener(view -> showDatePicker());

        // Configurar selección de hora
        etHora.setOnClickListener(view -> showTimePicker());

        // Configurar botón de registro
        btnRegister.setOnClickListener(view -> registerUser());

        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(
                this,
                R.array.tipos,
                android.R.layout.simple_spinner_item
        );
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adaptador);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.opc1) { // Ver
            Intent intent = new Intent(this, vercontactos.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.opc2) { // Agregar
            Intent intent = new Intent(this, Registro.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.opc3) { // Editar
            Intent intent = new Intent(this, Editarcontactos.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.opc4) { // Eliminar
            Intent intent = new Intent(this, Eliminarcontactos.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.opc5) { //cerrar sesión
            if (archivo.contains("id_usuario")) {
                SharedPreferences.Editor editor = archivo.edit();
                editor.remove("id_usuario"); // Elimina la sesión
                editor.apply();

                // Redirige al inicio de sesión
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            datos.setFecha(dayOfMonth + "/" + (month + 1) + "/" + year);
            etFecha.setText(datos.getFecha());
        }, ano, mes, dia);

        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            datos.setHora(hourOfDay + ":" + minute);
            etHora.setText(datos.getHora());
        }, hora, minuto, true);

        timePickerDialog.show();
    }

    private void registerUser() {
        // Validar campos
        if (etName.getText().toString().isEmpty() || etPhone.getText().toString().isEmpty() ||
                etAddress.getText().toString().isEmpty() || etEmail.getText().toString().isEmpty() ||
                etAge.getText().toString().isEmpty() || etDescription.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        // Obtener el ID del botón seleccionado
        int selectedId = radioGroupGenero.getCheckedRadioButtonId();
        if (selectedId != -1) {
            radioButtonSeleccionado = findViewById(selectedId);
            String generoSeleccionado = radioButtonSeleccionado.getText().toString();


            // Asignar valores al POJO
            datos.setNombre(etName.getText().toString());
            datos.setTelefono(etPhone.getText().toString());
            datos.setDireccion(etAddress.getText().toString());
            datos.setCorreo(etEmail.getText().toString());
            datos.setTipo(spinnerTipo.getSelectedItem().toString());
            datos.setDescripcion(etDescription.getText().toString());
            datos.setEdad(Integer.parseInt(etAge.getText().toString()));
            datos.setTipo(spinnerTipo.getSelectedItem().toString()); // Valor del Spinner
            datos.setGenero(generoSeleccionado);

            // Crear un nuevo contacto a partir de los datos ingresados
            contactos nuevoContacto = new contactos(
                    etName.getText().toString(),
                    etPhone.getText().toString(),
                    etAddress.getText().toString(),
                    etEmail.getText().toString(),
                    Integer.parseInt(etAge.getText().toString()), generoSeleccionado,  // Variable del RadioGroup
                    spinnerTipo.getSelectedItem().toString(),
                    etDescription.getText().toString()
            );
            ContactosListaGlobal.getInstancia().agregarContacto(nuevoContacto);

// Agregar el contacto a la lista global
            ContactosListaGlobal.getInstancia().agregarContacto(nuevoContacto);

// Mostrar mensaje de éxito
            Toast.makeText(this, "Contacto registrado exitosamente", Toast.LENGTH_SHORT).show();
            limpiarCampos();

        } else {
            Toast.makeText(this, "Por favor selecciona un género", Toast.LENGTH_SHORT).show();
        }
    }
    private void limpiarCampos() {
        etName.setText("");
        etPhone.setText("");
        etAddress.setText("");
        etEmail.setText("");
        etAge.setText("");
        etDescription.setText("");
        spinnerTipo.setSelection(0);
        radioGroupGenero.clearCheck();
    }
}
