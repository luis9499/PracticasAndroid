package com.example.solicitudesfup;

import android.content.Intent;
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
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {
    EditText Text_Codigo;
    EditText Text_Identif;

    Button btn_Iniciar, boton;
    String cod, ident;

    final String NAMESPACE = "http://sgoliver.net/";
    final String URL = "http://192.168.43.189/ServicioClientes.asmx";

    //String dato = Text_Codigo.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Text_Codigo = (EditText) findViewById(R.id.Text_Codigo);
        Text_Identif = (EditText) findViewById(R.id.Text_Identif);
        boton = (Button) findViewById(R.id.btn_Iniciar);


        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = ((EditText) findViewById(R.id.Text_Codigo)).getText().toString();
                String identificacion = ((EditText) findViewById(R.id.Text_Identif)).getText().toString();
                if (codigo.length() == 0 || identificacion.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Código o Identificación incorrecto", Toast.LENGTH_SHORT).show();
                }
                if (codigo.length() != 0 || codigo.toString() != "") {
                    if (identificacion.length() != 0 || identificacion.toString() != "") {
                        TareaWSBuscarEstudiante buscar = new TareaWSBuscarEstudiante();
                        buscar.execute();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Código o Identificación incorrecto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Tarea Asincrona que valida el ingreso
     */
    private class TareaWSBuscarEstudiante extends AsyncTask<String, Integer, Boolean> {

        private Estudiante[] listaEstudiante;

        protected Boolean doInBackground(String... params) {
            boolean result = true;
            final String METHOD_NAME = "buscarEstudiante";
            final String SOAP_ACTION = "http://sgoliver.net/buscarEstudiante";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("cod", cod);
            request.addProperty("cedula", ident);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();
                listaEstudiante = new Estudiante[resultado_xml.getPropertyCount()];
                for (int i = 0; i < listaEstudiante.length; i++) {
                    SoapObject ic = (SoapObject) resultado_xml.getProperty(i);
                    Estudiante est = new Estudiante();
                    est.setCod(Integer.parseInt(ic.getProperty(0).toString()));
                    est.setCedula(Integer.parseInt(ic.getProperty(1).toString()));
                    est.setNombres(ic.getProperty(2).toString());

                    listaEstudiante[i] = est;
                }

            } catch (Exception e) {
                Log.i("Errores", toString());
                result = false;
            }
            return result;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            cod = Text_Codigo.getText().toString();
            ident = Text_Identif.getText().toString();

        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                if (listaEstudiante.length > 0) {

                    Intent segAct = new Intent(MainActivity.this, Main2Activity.class);
                    segAct.putExtra("codigo",cod);
                    startActivity(segAct);
                    Toast.makeText(MainActivity.this, "Bienvenido(a) Estudiante", Toast.LENGTH_SHORT).show();
                } else {
                    Text_Codigo.setText("");
                    Text_Identif.setText("");
                    Toast.makeText(MainActivity.this, "Código o Identificación incorrecto", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }
}
