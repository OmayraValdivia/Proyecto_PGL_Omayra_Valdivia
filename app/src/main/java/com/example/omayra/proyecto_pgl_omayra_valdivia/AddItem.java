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

public class AddItem extends AppCompatActivity {
    // 7 dias en milisegundos -> 7x24x60x60x1000 = 604800000
    private static final int SEVEN_DAYS = 604800000;
    //Instanciamos variables
    EditText title;
    String status ="No hecho", priority="Medio", mes ="", dia="", hora ="", minuto ="";
    RadioGroup opcionesStatus, opcionesPrioriry;
    RadioButton rbDone, rbNotDone, rbLow, rbMedium, rbHigh;
    TextView dateView, timeView;
    String dateString, timeString;
    DatePickerDialog datePickerDialog;
    TimePickerDialog mTimePicker;
    Button btDate, btTime, btCancel, btReset, btSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        //Enlazamos las variables con los ids del xml.
        title = (EditText) findViewById(R.id.etTitle);
        opcionesStatus = (RadioGroup)findViewById(R.id.groupStatus);
        rbDone = (RadioButton)findViewById(R.id.rbDone);
        rbNotDone = (RadioButton)findViewById(R.id.rbNotDone);
        opcionesPrioriry = (RadioGroup)findViewById(R.id.groupPriority);
        rbLow = (RadioButton)findViewById(R.id.rbLow);
        rbMedium = (RadioButton)findViewById(R.id.rbMedium);
        rbHigh = (RadioButton) findViewById(R.id.rbHigh);
        dateView = (TextView) findViewById(R.id.tvDate);
        timeView = (TextView) findViewById(R.id.tvTime);
        btDate = (Button) findViewById(R.id.btDate);
        btTime = (Button) findViewById(R.id.btTime);
        btCancel = (Button) findViewById(R.id.btCancel);
        btReset = (Button) findViewById(R.id.btReset);
        btSubmit = (Button) findViewById(R.id.btSubmit);
        //obtenemos la fecha por defecto con + 7 dias.
        setDefaultDateTime();
        //RadioButton de Status
        opcionesStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
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
                switch (checkedId){
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
                datePickerDialog = new DatePickerDialog(AddItem.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear++;
                        //Si el mes es menor a 10, se añade un 0 al mes para que sea dos digitos.
                        if(monthOfYear<10)
                            mes = "0"+monthOfYear;
                        else{
                            mes = String.valueOf(monthOfYear);
                        }
                        //Si el dia es menor a 10, se añade un 0 al día para que sea dos digitos.
                        if(dayOfMonth<10)
                            dia = "0"+dayOfMonth;
                        else{
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
                mTimePicker = new TimePickerDialog(AddItem.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //Si la hora es menor a 10 añadimos un 0 para que muestre dos digitos en la hora.
                        if (selectedHour<10)
                            hora = "0" + selectedHour;
                        else{
                            hora = String.valueOf(selectedHour);
                        }
                        //Si el minuto es menor a 10 añadimos un 0 para que muestre dos digitos en los minutos.
                        if (selectedMinute<10)
                            minuto = "0" + selectedMinute;
                        else{
                            minuto = String.valueOf(selectedMinute);
                        }
                        //Modificamos el textView para mostrar la hora y minuto obtenido.
                        timeView.setText( hora + ":" + minuto);
                    }
                }, hour, minute, true);//Si es 24 horas
                //Mostramos la mentana del timePicker.
                mTimePicker.show();
            }
        });
        //Cuando clickamos sobre el boton cancel
        btCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Cerramos la actividad
                finish();
            }
        });
        //Cuando clickamos sobre el boton reset
        btReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Limpiamos todos los campos o los dejamos por defecto
                title.setText("");
                rbDone.setChecked(false);
                rbNotDone.setChecked(true);
                rbLow.setChecked(false);
                rbMedium.setChecked(true);
                rbHigh.setChecked(false);
                //Fecha y hora por defecto con los 7 dias.
                setDefaultDateTime();
            }
        });
        //Cuando clickamos sobre el boton submit
        btSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String titulo = title.getText().toString();
                //Compara primero que titulo no este vacio.
                if (!titulo.equals("")){
                    //sino lo esta, hace una consulta a la base de datos, si ese titulo existe muestra un mensaje de que el titulo existe.
                    AdminSQLiteOpenHelper admin=new AdminSQLiteOpenHelper(AddItem.this, "administracion", null, 1);
                    SQLiteDatabase bd=admin.getWritableDatabase();
                    final Cursor c = bd.rawQuery("select * from tareas where titulo='"+titulo+"'", null);
                    if (c.moveToFirst()){
                        Toast.makeText(getApplicationContext(), "Titulo repetido, debe poner otro titulo", Toast.LENGTH_SHORT).show();
                    }
                    //sino existe ese titulo, se obtienen los datos para insertarlo a la base de datos.
                    else{
                        String estado = status;
                        String prioridad = priority;
                        String fecha = dateView.getText().toString();
                        String hora = timeView.getText().toString();
                        ContentValues registro = new ContentValues();
                        registro.put("titulo", titulo);
                        registro.put("estado", estado);
                        registro.put("prioridad", prioridad);
                        registro.put("fecha", fecha);
                        registro.put("hora", hora);
                        bd.insert("tareas", null, registro);
                        //Mostramos por pantalla un toast indicando que se ha guardado
                        Toast guardado = Toast.makeText(getApplicationContext(), "Datos guardados", Toast.LENGTH_SHORT);
                        guardado.show();
                        //Limpiamos todos los campos
                        title.setText("");
                        rbDone.setChecked(false);
                        rbNotDone.setChecked(true);
                        rbLow.setChecked(false);
                        rbMedium.setChecked(true);
                        rbHigh.setChecked(false);
                        setDefaultDateTime();
                    }
                    bd.close();
                }else{
                    Toast.makeText(getApplicationContext(), "Titulo esta vacio, ponga un titulo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setDefaultDateTime() {
        // Fecha por defecto mas 7 dias.
        Date mDate = new Date();
        mDate = new Date(mDate.getTime() + SEVEN_DAYS);
        Calendar c = Calendar.getInstance();
        c.setTime(mDate);
        //Obtenemos el año, mes y dia del calendario.
        dateString=setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        //Modificamos el textView para que se muestre la fecha obtenida por defecto con los 7 dias de mas
        dateView.setText(dateString);
        //Obtenemos la hora y minuto del calendario
        timeString=setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
        //Modificamos el TextView para que se muestre la hora actual
        timeView.setText(timeString);
    }
    //Metodo para extraer la fecha por defecto.
    private static String setDateString(int year, int monthOfYear, int dayOfMonth) {
        // Incrementamos el mes del año en uno.
        monthOfYear++;
        String mon = "" + monthOfYear;
        String day = "" + dayOfMonth;
        //Si el mes es menor a 10, se añade un 0 al mes para que sea dos digitos.
        if (monthOfYear < 10)
            mon = "0" + monthOfYear;
        //Si el dia es menor a 10, se añade un 0 al día para que sea dos digitos.
        if (dayOfMonth < 10)
            day = "0" + dayOfMonth;
        String dateS = day+ "/" + mon+ "/" + year;
        return dateS;
    }
    private static String setTimeString(int hourOfDay, int minute){
        String hour = "" + hourOfDay;
        String min = "" + minute;
        //Si la hora es menor a 10, se añade un 0 a la hora para que sea de dos digitos.
        if (hourOfDay < 10)
            hour = "0" + hourOfDay;
        //Si la hora es menor a 10, se añade un 0 al minuto para que sea de dos digitos
        if (minute < 10)
            min = "0" + minute;
        String timeS = hour + ":" + min;
        return timeS;
    }
}
