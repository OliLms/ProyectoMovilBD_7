package com.example.proyecto;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Registro extends AppCompatActivity {

    EditText etName, etPhone, etAddress, etEmail, etAge, etDescription, etFecha, etHora;
    Spinner spinnerTipo;
    Button btnRegister;
    RadioGroup radioGroupGenero;
    RadioButton radioButtonSeleccionado;

    private static final String BASE_URL = "http://10.0.2.2/registrar.php"; // Cambiar la URL si es necesario
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
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
        spinnerTipo = findViewById(R.id.spinnerType);
        radioGroupGenero = findViewById(R.id.botongenero);
        btnRegister = findViewById(R.id.btnRegistrar);

        client = new OkHttpClient();

        etFecha.setOnClickListener(view -> showDatePicker());
        etHora.setOnClickListener(view -> showTimePicker());

        btnRegister.setOnClickListener(view -> registerUser());

        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(
                this,
                R.array.tipos,
                android.R.layout.simple_spinner_item
        );
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adaptador);
    }

    private void registerUser() {
        // Validar campos
        if (etName.getText().toString().isEmpty() || etPhone.getText().toString().isEmpty() ||
                etAddress.getText().toString().isEmpty() || etEmail.getText().toString().isEmpty() ||
                etAge.getText().toString().isEmpty() || etDescription.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = radioGroupGenero.getCheckedRadioButtonId();
        if (selectedId != -1) {
            radioButtonSeleccionado = findViewById(selectedId);
            String generoSeleccionado = radioButtonSeleccionado.getText().toString();

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("nombre", etName.getText().toString());
                jsonObject.put("telefono", etPhone.getText().toString());
                jsonObject.put("direccion", etAddress.getText().toString());
                jsonObject.put("correo", etEmail.getText().toString());
                jsonObject.put("edad", Integer.parseInt(etAge.getText().toString()));
                jsonObject.put("genero", generoSeleccionado);
                jsonObject.put("tipo", spinnerTipo.getSelectedItem().toString());
                jsonObject.put("descripcion", etDescription.getText().toString());
                jsonObject.put("fecha", etFecha.getText().toString());
                jsonObject.put("hora", etHora.getText().toString());

                RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.get("application/json"));

                Request request = new Request.Builder()
                        .url(BASE_URL)
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(() -> Toast.makeText(Registro.this, "Error en la conexión", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            runOnUiThread(() -> {
                                Toast.makeText(Registro.this, "Contacto registrado exitosamente", Toast.LENGTH_SHORT).show();
                                limpiarCampos();
                            });
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(Registro.this, "Error al registrar contacto: " + response.message(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(Registro.this, "Error al procesar datos", Toast.LENGTH_SHORT).show());
            }
        } else {
            Toast.makeText(this, "Por favor selecciona un género", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            etFecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        }, ano, mes, dia);

        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            etHora.setText(hourOfDay + ":" + minute);
        }, hora, minuto, true);

        timePickerDialog.show();
    }

    private void limpiarCampos() {
        etName.setText("");
        etPhone.setText("");
        etAddress.setText("");
        etEmail.setText("");
        etAge.setText("");
        etDescription.setText("");
        etFecha.setText("");
        etHora.setText("");
        spinnerTipo.setSelection(0);
        radioGroupGenero.clearCheck();
    }
}
