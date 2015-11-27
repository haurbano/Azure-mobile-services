package com.example.hamiltonurbano.testconnectionazure;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hamiltonurbano.testconnectionazure.models.Estudiante;
import com.microsoft.windowsazure.mobileservices.*;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables Conexion Azure
    private MobileServiceClient mClient;
    List<Estudiante> estudiantes = new ArrayList<>();

    //GUI
    EditText editNombre,editPassword;
    Button btnagregar;
    ListView listEstudiantes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conectar();

        //region GUI
        editNombre = (EditText) findViewById(R.id.edit_nombre);
        editPassword = (EditText) findViewById(R.id.edit_password);
        btnagregar = (Button) findViewById(R.id.btn_agregar);
        listEstudiantes = (ListView) findViewById(R.id.list_estudiantes);

        btnagregar.setOnClickListener(this);
        //endregion


    }

    @Override
    protected void onResume() {
        super.onResume();
        ConsultarDB();
    }

    //region Conectar
    private void conectar()
    {
        try {
            mClient =  new MobileServiceClient( "https://awesomekids.azure-mobile.net/", "CSzfWKijcjdVjJIvTjqXHPHItoPGuN99",this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i("haur","Error al crear el clinte MobileServiceClient"+ e.getMessage());
        }
    }
    //endregion

    //region Consultar DB
    private void ConsultarDB(){
        new AsyncTask<Void,Void,Void>(){


            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final MobileServiceList<Estudiante> listaEstudiantes = mClient.getTable(Estudiante.class).execute().get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            estudiantes.clear();
                            for (Estudiante e : listaEstudiantes)
                            {
                                estudiantes.add(e);
                            }
                            mostrarEstudiantes();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("haur","error al cargar estudiante: "+e.getMessage());
                }
                return null;
            }

        }.execute();
    }
    //endregion

    @Override
    public void onClick(View v) {
        agregarEstudiante();
    }

    //region Agregar estudiante
    private void agregarEstudiante() {
        Estudiante e = new Estudiante();
        e.setNombre(editNombre.getText().toString());
        e.setPassword(editPassword.getText().toString());
        Log.i("haur","name: "+editNombre.getText().toString()+" --pass: "+editPassword.getText().toString());
        try {
            mClient.getTable(Estudiante.class).insert(e, new TableOperationCallback<Estudiante>() {
                @Override
                public void onCompleted(Estudiante entity, Exception exception, ServiceFilterResponse response) {
                    if (exception == null){
                        Toast.makeText(getApplicationContext(),"Estudiante Agregado",Toast.LENGTH_SHORT).show();
                        ConsultarDB();
                    }else{
                        Toast.makeText(getApplicationContext(),"Error al agregar Estudiante",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (Exception exe){
            Log.i("haur","Error en agregar cliente"+ exe.getMessage());
        }

    }
    //endregion

    //region MOstrar estudiante
    public void mostrarEstudiantes()
    {
        List<String> data = new ArrayList<>();
        if (estudiantes != null){
            for (Estudiante e: estudiantes){
                String est = "Nombre: "+e.getNombre()+"-- Password: "+e.getPassword();
                data.add(est);
            }
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,data);
            Log.i("haur","tamaño de data: "+data);
            listEstudiantes.setAdapter(adapter);
        }

    }
    //endregion


}
