package com.example.luishurtado.registroestudiantev2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import modelo.Estudiante;

import static com.example.luishurtado.registroestudiantev2.MainActivityFirma.SIGNATURE_ACTIVITY;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentRegistro.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class FragmentRegistro extends Fragment {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    final static int CONS = 1;
    Estudiante estu;
    Button btnGuardar, btnFoto, btnFirma, btnConsulta;
    String nombre, apellido, genero, documento, firma, foto, datoBdFirma;
    EditText txtNombre, txtApellido, txtGenero, txtDocumento;
    ImageView img_camera, img_firma;
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
        txtDocumento = (EditText) view.findViewById(R.id.txtDocumento);
        img_camera = (ImageView) view.findViewById(R.id.img_Foto);
        img_firma = (ImageView) view.findViewById(R.id.camera_preview);
        Spinner spinner = (Spinner) view.findViewById(R.id.txtGenero);
        String[] valores = {"Masculino","Femenino"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, valores);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // vacio

            }
        });

        btnFoto = (Button) view.findViewById(R.id.btnFoto);
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,
                        CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        btnFirma = (Button) view.findViewById(R.id.btnFirma);
        btnFirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CaptureSignature.class);
                startActivityForResult(intent, SIGNATURE_ACTIVITY);
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


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                if (resultCode == Activity.RESULT_OK) {

                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    // convert byte array to Bitmap

                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                    //stringFoto = BitMapToString(bitmap);

                    img_camera.setImageBitmap(bitmap);

                }
            }
        }catch (Exception e){

        }

        try {
            if (requestCode == SIGNATURE_ACTIVITY && resultCode == CaptureSignature.RESULT_OK) {
                Bitmap imagenFirma = null;
                Bundle bundle = data.getExtras();
                String status = bundle.getString("status");
                if (status.equalsIgnoreCase("done")) {
                    datoBdFirma = bundle.getString("imagen");
                    try {
                        imagenFirma = BitmapFactory.decodeStream(getActivity().openFileInput(datoBdFirma));
                        //datoBdFirma = BitMapToString(imagenFirma);
                        img_firma.setImageBitmap(imagenFirma);
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    Toast toast = Toast.makeText(getActivity(), "Signature capture succesfull", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 105, 50);
                    toast.show();
                }
            }
        } catch (Exception exc) {

        }

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
