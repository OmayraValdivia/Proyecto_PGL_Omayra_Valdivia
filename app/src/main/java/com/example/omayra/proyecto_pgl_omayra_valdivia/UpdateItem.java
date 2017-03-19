package com.example.omayra.proyecto_pgl_omayra_valdivia;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class UpdateItem extends AppCompatActivity {
    //Instanciamos variables
    TextView tvTitle, dateView, timeView;
    String status = "", priority = "", mes = "", dia = "", hora = "", minuto = "";
    String tituloConsultado="", estadoConsultado="", prioridadConsultado="", fechaConsultado="",horaConsultado="";
    RadioGroup opcionesStatus, opcionesPrioriry;
    RadioButton rbDone, rbNotDone, rbLow, rbMedium, rbHigh;
    DatePickerDialog datePickerDialog;
    TimePickerDialog mTimePicker;
    Button btDate, btTime, btCancel, btReset, btUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);
        //Enlazamos variables con los objetos del layout
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        opcionesStatus = (RadioGroup) findViewById(R.id.groupStatus);
        rbDone = (RadioButton) findViewById(R.id.rbDone);
        rbNotDone = (RadioButton) findViewById(R.id.rbNotDone);
        opcionesPrioriry = (RadioGroup) findViewById(R.id.groupPriority);
        rbLow = (RadioButton) findViewById(R.id.rbLow);
        rbMedium = (RadioButton) findViewById(R.id.rbMedium);
        rbHigh = (RadioButton) findViewById(R.id.rbHigh);
        dateView = (TextView) findViewById(R.id.tvDate);
        timeView = (TextView) findViewById(R.id.tvTime);
        btDate = (Button) findViewById(R.id.btDate);
        btTime = (Button) findViewById(R.id.btTime);
        btCancel = (Button) findViewById(R.id.btCancel);
        btReset = (Button) findViewById(R.id.btReset);
        btUpdate = (Button) findViewById(R.id.btUpdate);
        //Capturamos los datos que se envian de la otra activity
        Bundle bundle = getIntent().getExtras();
        tituloConsultado = bundle.getString("titulo");
        estadoConsultado = bundle.getString("estado");
        prioridadConsultado = bundle.getString("prioridad");
        fechaConsultado = bundle.getString("fecha");
        horaConsultado = bundle.getString("hora");
        //Rellenamos los campos con los datos dados:
        camposConsultados();
        //RadioButton de Status
        opcionesStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbDone:
                        status = "Hecho";
                        break;
                    case R.id.rbNotDone:
                        status = "No hecho";
                        break;
                }
            }
        });
        //RadioButton de Priority
        opcionesPrioriry.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbLow:
                        priority = "Bajo";
                        break;
                    case R.id.rbMedium:
                        priority = "Medio";
                        break;
                    case R.id.rbHigh:
                        priority = "Alto";
                        break;
                }
            }
        });
        // Cuando clickamos el boton para cambiar fecha se ejecuta lo siguiente;
        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Instanciamos las clases para obtener la fecha, año, mes y dia del calendario.
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // año actual
                final int mMonth = c.get(Calendar.MONTH); // mes actual
                int mDay = c.get(Calendar.DAY_OF_MONTH); // dia actual
                // creamos la ventana del datePicker
                datePickerDialog = new DatePickerDialog(UpdateItem.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear++;
                        //Si el mes es menor a 10, se añade un 0 al mes para que sea dos digitos.
                        if (monthOfYear < 10)
                            mes = "0" + monthOfYear;
                        else {
                            mes = String.valueOf(monthOfYear);
                        }
                        //Si el dia es menor a 10, se añade un 0 al día para que sea dos digitos.
                        if (dayOfMonth < 10)
                            dia = "0" + dayOfMonth;
                        else {
                            dia = String.valueOf(dayOfMonth);
                        }
                        // modificamos el campo dateView con el dia, mes y año seleccionado.
                        dateView.setText(dia + "/" + mes + "/" + year);
                    }
                }, mYear, mMonth, mDay);
                //Mostramos el datePicker
                datePickerDialog.show();
            }
        });
        // Cuando clickamos el boton para cambiar hora se ejecuta lo siguiente;
        btTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Instanciamos las clases para obtener la hora y minuto del calendario
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                // creamos la ventana del timePicker
                mTimePicker = new TimePickerDialog(UpdateItem.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //Si la hora es menor a 10 añadimos un 0 para que muestre dos digitos en la hora.
                        if (selectedHour < 10)
                            hora = "0" + selectedHour;
                        else {
                            hora = String.valueOf(selectedHour);
                        }
                        //Si el minuto es menor a 10 añadimos un 0 para que muestre dos digitos en los minutos.
                        if (selectedMinute < 10)
                            minuto = "0" + selectedMinute;
                        else {
                            minuto = String.valueOf(selectedMinute);
                        }
                        //Modificamos el textView para mostrar la hora y minuto obtenido.
                        timeView.setText(hora + ":" + minuto);
                    }
                }, hour, minute, true);//Si es 24 horas
                //Mostramos la mentana del timePicker.
                mTimePicker.show();
            }
        });
        //Cuando clickamos sobre el boton cancel
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cerramos la actividad
                finish();
            }
        });
        //Cuando clickamos sobre el boton reset
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dejamos los campos con los datos consultados
                camposConsultados();
            }
        });
        //Cuando clickamos sobre el boton submit
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hacemos la modificacion en la bd
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(UpdateItem.this, "administracion", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();
                String titulo = tituloConsultado;
                String estado = status;
                String prioridad = priority;
                String fecha = dateView.getText().toString();
                String hora = timeView.getText().toString();
                ContentValues registro = new ContentValues();
                registro.put("estado", estado);
                registro.put("prioridad", prioridad);
                registro.put("fecha", fecha);
                registro.put("hora", hora);
                int cant = bd.update("tareas", registro, "titulo='" + titulo+"'", null);
                bd.close();
                if (cant == 1)
                    Toast.makeText(UpdateItem.this, "Se modificó la tarea", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(UpdateItem.this, "No se pudo modificar la tarea", Toast.LENGTH_SHORT).show();

            }
        });
    }
    //Metodo para mostrar los datos consultados
    private void camposConsultados(){
        tvTitle.setText(tituloConsultado);
        status=estadoConsultado;
        priority=prioridadConsultado;
        if(estadoConsultado.equals("Hecho")){
            rbDone.setChecked(true);
        }else{
            rbNotDone.setChecked(true);
        }
        if(prioridadConsultado.equals("Bajo")){
            rbLow.setChecked(true);
        }else{
            if(prioridadConsultado.equals("Medio")){
                rbMedium.setChecked(true);
            }else{
                rbHigh.setChecked(true);
            }
        }
        dateView.setText(fechaConsultado);
        timeView.setText(horaConsultado);
    }
}