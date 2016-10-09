package com.example.luishurtado.registroestudiantev2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import modelo.Estudiante;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentRegistro.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class FragmentRegistro extends Fragment {
    final static int CONS = 1;
    Estudiante estu;
    Button btnGuardar, btnFoto, btnFirma, btnConsulta;
    String nombre, apellido, genero, documento, firma, foto;
    EditText txtNombre, txtApellido, txtGenero, txtDocumento;
    final String NAMESPACE = "http://tempuri.org/";
    final String URL = "http://" + Constantes.IP + "/WebServiceLUG_ANDROID.asmx";

    private OnFragmentInteractionListener mListener;

    public FragmentRegistro() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_fragment_registro, container, false);

        txtNombre = (EditText) view.findViewById(R.id.txtNombre);
        txtApellido = (EditText) view.findViewById(R.id.txtApellido);
        txtGenero = (EditText) view.findViewById(R.id.txtGenero);
        txtDocumento = (EditText) view.findViewById(R.id.txtDocumento);

        btnFoto = (Button) view.findViewById(R.id.btnFoto);
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goCamara = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
                startActivityForResult(goCamara, CONS);
            }
        });

        btnFirma = (Button) view.findViewById(R.id.btnFirma);
        btnFirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goFirma = new Intent(getActivity(), CaptureSignature.class);
                startActivity(goFirma);
            }
        });

        btnGuardar = (Button) view.findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              RegistrarAlumno registro = new RegistrarAlumno();
                                              registro.execute();

                                          }
                                      }
        );

        /**btnConsulta = (Button) view.findViewById(R.id.btnConsultar);
        btnConsulta.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Bundle bundle = new Bundle();
                                               Intent intent = new Intent(getActivity(), FragmentConsulta.class);
                                               intent.putExtra("documento", txtDocumento.getText());
                                               startActivity(intent);
                                           }
                                       }
        );*/


        //return inflater.inflate(R.layout.fragment_fragment_registro, container, false);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    protected  void OnRestart()
    {
        Bundle bundle = getActivity().getIntent().getExtras();
        firma = bundle.getString("firma");
    }

    public void onResume()
    {
        super.onResume();
        Bundle bundle = getActivity().getIntent().getExtras();
        firma = bundle.getString("firma");

    }
*/


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    //Tarea As�ncrona para llamar al WS de consulta en segundo plano
    private class RegistrarAlumno extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            final String METHOD_NAME = "registrarAlumno";
            final String SOAP_ACTION = "http://tempuri.org/registrarAlumno";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


            // request.addProperty("id", codigo);
            request.addProperty("nombre", nombre);
            request.addProperty("apellido", apellido);
            request.addProperty("genero", genero);
            request.addProperty("documento", documento);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);

                SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
                int res = Integer.parseInt(resultado_xml.toString());

                if (res < 1)
                    resul = false;
            } catch (Exception e) {
                Log.i("Errores", e.toString());
                resul = false;
            }

            return resul;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nombre = txtNombre.getText().toString();
            apellido = txtApellido.getText().toString();
            genero = txtGenero.getText().toString();
            documento = txtDocumento.getText().toString();
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(getActivity());
                alerta.setTitle("PROCESO: ");
                alerta.setMessage("El Alumno " + nombre + " " + apellido + " fue registrado");
                alerta.setPositiveButton("Atras", null);
                alerta.show();
                txtNombre.setText("");
                txtApellido.setText("");
                txtGenero.setText("");
                txtDocumento.setText("");
            } else {
                AlertDialog.Builder alerta = new AlertDialog.Builder(getActivity());
                alerta.setTitle("Error: ");
                alerta.setMessage("Los datos proporcionados no son validos");
                alerta.setPositiveButton("Atras", null);
                alerta.show();
            }

        }
    }


}
