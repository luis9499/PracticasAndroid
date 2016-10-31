package com.example.luishurtado.registroestudiantev2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
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
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;

import modelo.Estudiante;

import static android.app.Activity.RESULT_OK;
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
    Bitmap bmp;
    Button btnGuardar, btnFoto, btnFirma, btnDir;
    String nombre, apellido, genero, documento, datoBdFirma, firma, datoBdFoto, foto, img_strFoto = "", img_strFirma = "";
    EditText txtNombre, txtApellido, txtGenero, txtDocumento;
    ImageView img_camera, img_firma;
    final String NAMESPACE = Constantes.NAMESPACE_S;
    final String URL = "http://" + Constantes.IP + "/WebServiceLUG_ANDROID.asmx";

    private OnFragmentInteractionListener mListener;

    public FragmentRegistro() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_fragment_registro, container, false);

        txtNombre = (EditText) view.findViewById(R.id.txtNombre);
        txtApellido = (EditText) view.findViewById(R.id.txtApellido);
        txtDocumento = (EditText) view.findViewById(R.id.txtDocumento);
        txtGenero = (EditText) view.findViewById(R.id.txtGenero);

        img_camera = (ImageView) view.findViewById(R.id.img_Foto);
        img_firma = (ImageView) view.findViewById(R.id.camera_preview);

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

        btnDir = (Button) view.findViewById(R.id.btnDireccion);
        btnDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Mapas.class);
                startActivity(intent);
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
                if (resultCode == RESULT_OK) {
                    {
                        Bundle extras = data.getExtras();
                        bmp = (Bitmap) extras.get("data");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();
                        img_strFoto = Base64.encodeToString(b, Base64.DEFAULT);
                        img_camera.setImageBitmap(bmp);
                    }
                }
            }
        } catch (Exception e) {

        }

        try {
            if (requestCode == SIGNATURE_ACTIVITY && resultCode == CaptureSignature.RESULT_OK) {
                Bitmap imagenFirma;
                Bundle bundle = data.getExtras();
                String status = bundle.getString("status");
                if (status.equalsIgnoreCase("done")) {
                    img_strFirma = bundle.getString("str_Firma");
                    datoBdFirma = bundle.getString("firma");
                    try {
                        //imagenFirma = BitmapFactory.decodeStream(getActivity().openFileInput(datoBdFirma));
                        //datoBdFirma = BitMapToString(imagenFirma);
                        //img_firma.setImageBitmap(imagenFirma);
                        byte[] decodedString = Base64.decode(img_strFirma, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        img_firma.setImageBitmap(decodedByte);
                        //FileNotFoundException
                    } catch (Exception ex) {
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

    /**
     * private String BitMapToStringFoto(ImageView bitmap) {
     * Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.id.img_Foto);
     * ByteArrayOutputStream stream = new ByteArrayOutputStream();
     * bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
     * byte[] imageByteArray = stream.toByteArray();
     * System.out.println("byte array:" + imageByteArray);
     * String img_strFoto = Base64.encodeToString(imageByteArray, 0);
     * return img_strFoto;
     * }
     */

    private String BitMapToStringFirma(ImageView bitmap) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.id.camera_preview);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageByteArray = stream.toByteArray();
        System.out.println("byte array:" + imageByteArray);
        String img_strFirma = Base64.encodeToString(imageByteArray, 0);
        return img_strFirma;
    }

    //Tarea As�ncrona para llamar al WS de consulta en segundo plano
    private class RegistrarAlumno extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            final String METHOD_NAME = "registrarAlumno";
            final String SOAP_ACTION = "http://sgoliver.net/registrarAlumno";
            //final String SOAP_ACTION = "http://tempuri.org/registrarAlumno";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("nombre", nombre);
            request.addProperty("apellido", apellido);
            request.addProperty("genero", genero);
            request.addProperty("documento", documento);
            request.addProperty("foto", img_strFoto);
            request.addProperty("firma", img_strFirma);

            System.out.println(img_strFoto + " espacio " + img_strFirma);

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
            foto = img_strFoto;
            firma = img_strFirma;
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