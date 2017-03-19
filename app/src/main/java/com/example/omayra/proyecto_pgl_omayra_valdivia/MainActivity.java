package com.example.omayra.proyecto_pgl_omayra_valdivia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    public ArrayList<String> tareas =new ArrayList<String>();
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Enlazamos el listView del layout con la variable listView.
        listView = (ListView) findViewById(R.id.listView);
        //Anadimos al layout el footer.xml para luego añadirlo al listview como un footer.
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.footer, listView, false);
        listView.addFooterView(footer, null, true);
        //llamamos al metodo mostrarLista para que realice la consulta y cree el adaptador
        mostrarLista();
        //Modificar elemento
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l){
                //obtenemos el contenido del item seleccionado
                String itemSeleccionado = String.valueOf(adapterView.getAdapter().getItem(i));
                //dividimos el array en string para tener por separado cada valor
                String[] array = itemSeleccionado.split(" - ");
                final String tituloSeleccionado = array[0];
                final String prioridadSeleccionado = array[1];
                final String resto = array[2];
                String[] array2 = resto.split("\n");
                final String estadoSeleccionado =array2[0];
                final String resto2 = array2[1];
                String[] array3 = resto2.split("       ");
                final String fechaSeleccionado = array3[0];
                final String horaSeleccionado = array3[1];
                //Creamos una nueva actividad y pasamos parametros a esa actividad.
                Intent intent = new Intent(MainActivity.this, UpdateItem.class);
                intent.putExtra("titulo", tituloSeleccionado);
                intent.putExtra("estado", estadoSeleccionado);
                intent.putExtra("prioridad", prioridadSeleccionado);
                intent.putExtra("fecha", fechaSeleccionado);
                intent.putExtra("hora", horaSeleccionado);
                startActivity(intent);
            }
        });
        //Borrar elementos
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l) {
                final int posicion=i;
                String itemSeleccionado = String.valueOf(adapterView.getAdapter().getItem(posicion));
                String[] arrayTitulo = itemSeleccionado.split(" - ");
                final String tituloSeleccionado = arrayTitulo[0];
                //Mensaje indicando de si esta seguro de eliminar la tarea
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MainActivity.this);
                dialogo1.setTitle(tituloSeleccionado);
                dialogo1.setMessage("¿Elimina esta tarea?");
                //si cancela no hace nada
                dialogo1.setCancelable(false);
                //se clickea sobre Confirmar se ejecuta la eliminacion a la base de datos
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(MainActivity.this, "administracion", null, 1);
                        SQLiteDatabase bd = admin.getWritableDatabase();
                        int cant = bd.delete("tareas", "titulo='" + tituloSeleccionado+"'", null);
                        bd.close();
                        if (cant == 1)
                            //muestra un mensaje de que se ha borrado la tarea
                            Toast.makeText(MainActivity.this, "Se borró la tarea " + tituloSeleccionado, Toast.LENGTH_SHORT).show();
                        //limpiamos adaptador y llamamos al metodo mostrarLista para que actualice
                        adapter.clear();
                        mostrarLista();
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();
                return true;
            }
        });
    }
    //recargar la pagina
    @Override
    protected void onResume() {
        super.onResume();
        //limpiamos adaptador
        adapter.clear();
        //llamamos al metodo mostrarLista para que se ejecute la consulta y se cree el adaptador
        mostrarLista();
    }
    //metodo para consultar a la base de datos y crear el adaptador.
    public void mostrarLista(){
        //Leemos la base de datos
        AdminSQLiteOpenHelper admin=new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd=admin.getWritableDatabase();
        final Cursor c = bd.rawQuery("select titulo,estado,prioridad,fecha,hora from tareas", null);
        if (c.moveToFirst())
        {
            do {
                String titulo = c.getString(0);
                String estado = c.getString(1);
                String prioridad = c.getString(2);
                String fecha = c.getString(3);
                String hora = c.getString(4);
                tareas.add(titulo+" - "+prioridad+" - "+estado+"\n"+fecha+"       "+hora);
            } while (c.moveToNext());
        }
        bd.close();
        //Creamos el adaptador con el contenido tareas.
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tareas);
        //Mostramos el adaptador creado.
        listView.setAdapter(adapter);
    }
    //Metodo en el footer que al seleccionarlo añadimos un nuevo activity.
    public void add(View v) {
        Intent i = new Intent(this, AddItem.class);
        startActivity(i);
    }
}
