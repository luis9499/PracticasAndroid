package com.example.solicitudesfup;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Main2ActivityNotas extends AppCompatActivity {
    ListView listaNot;
    EditText Text_Codigo;
    AdaptadorNotas adaptadorNotas;
    String cod;

    final String NAMESPACE = "http://sgoliver.net/";
    final String URL = "http://192.168.43.189/ServicioClientes.asmx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_notas);
        Text_Codigo = (EditText) findViewById(R.id.Text_Codigo);
        listaNot = (ListView) findViewById(R.id.listaNot);
        cod = getIntent().getExtras().getString("codigo");
        Toast.makeText(this, "Esto tiene el cod: "+cod, Toast.LENGTH_SHORT).show();
        TareaWSBuscar notasAsig = new TareaWSBuscar();
        notasAsig.execute();
    }


    private class TareaWSBuscar extends AsyncTask<String, Integer, Boolean> {

        private Notas[] listaNotasEst;
        protected Boolean doInBackground(String... params) {
            boolean resul = true;
            final String METHOD_NAME = "listaNotas";
            final String SOAP_ACTION = "http://sgoliver.net/listaNotas";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("cod", cod);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapObject resSoap = (SoapObject) envelope.getResponse();
                listaNotasEst = new Notas[resSoap.getPropertyCount()];

                for (int i = 0; i < listaNotasEst.length; i++) {
                    SoapObject ic = (SoapObject) resSoap.getProperty(i);
                    Notas not = new Notas();
                    not.setCod_asig(Integer.parseInt(ic.getProperty(0).toString()));
                    not.setCod(Integer.parseInt(ic.getProperty(1).toString()));
                    not.setAsignatura(ic.getProperty(2).toString());
                    not.getNota1(Double.parseDouble(ic.getProperty(3).toString()));
                    not.getNota2(Double.parseDouble(ic.getProperty(4).toString()));
                    not.getNota3(Double.parseDouble(ic.getProperty(5).toString()));
                    not.getDefinitiva(Double.parseDouble(ic.getProperty(6).toString()));

                    listaNotasEst[i] = not;
                }
            } catch (Exception e) {
                Log.i("Errores", e.toString());
                resul = false;
            }
            return resul;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cod = Text_Codigo.getText().toString();
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                if (listaNotasEst.length > 0) {

                    adaptadorNotas = new AdaptadorNotas(Main2ActivityNotas.this, listaNotasEst);
                    listaNot.setAdapter(adaptadorNotas);

                }
            }
        }


    }
}
