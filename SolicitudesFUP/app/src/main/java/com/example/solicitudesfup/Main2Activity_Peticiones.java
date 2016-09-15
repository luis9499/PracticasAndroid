package com.example.solicitudesfup;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Main2Activity_Peticiones extends AppCompatActivity {
    Button btn_Insertar;
    EditText Text_Codigo, Text_Correo, Text_Pet;
    final String NAMESPACE = "http://sgoliver.net/";
    final String URL = "http://192.168.43.189/ServicioClientes.asmx";
    String cod, correo, peticion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_peticiones);
        Text_Codigo = (EditText) findViewById(R.id.Text_Codigo);
        Text_Correo = (EditText) findViewById(R.id.Text_Email);
        Text_Pet = (EditText) findViewById(R.id.Text_Pet);

        btn_Insertar = (Button) findViewById(R.id.btn_registrarPet);
        btn_Insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaresWSInsertarPet insertar = new TaresWSInsertarPet();
                insertar.execute();
            }

        });
    }

    /**
     * Tarea Asincrona para insertar Peticiones
     */
    private class TaresWSInsertarPet extends AsyncTask<String, Integer, Boolean> {
        protected Boolean doInBackground(String... params) {
            boolean result = true;
            final String METHOD_NAME = "insertPeticiones";
            final String SOAP_ACTION = "http://sgoliver.net/insertPeticiones";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("cod", cod);
            request.addProperty("correo", correo);
            request.addProperty("peticion", peticion);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL);
            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
                String res = resultado_xml.toString();
                if (!res.equals("1"))
                    result = false;
            } catch (Exception e) {
                Log.i("Errores", e.toString());
                result = false;
            }
            return result;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cod = Text_Codigo.getText().toString();
            correo = Text_Correo.getText().toString();
            peticion = Text_Pet.getText().toString();
        }
        protected void onPostExecute(Boolean result) {
            if (result)
                Toast.makeText(getApplicationContext(), "Petición registrada", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "No se registro Petición", Toast.LENGTH_SHORT).show();
        }
    }
}
