package com.example.luishurtado.registroestudiantev2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import static com.example.luishurtado.registroestudiantev2.MainActivityFirma.SIGNATURE_ACTIVITY;


public class FragmentRegistro extends Fragment {
    private static final String NOMBRE_CARPETA_APP = "com.example.luishurtado.reportes";
    private static final String GENERADOS = "reportesGenerados";
    Button bt_generar;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;

    Button btnGuardar, btnFoto, btnFirma;
    String nombre, apellido, genero, documento, datoBdFirma;
    EditText txtNombre, txtApellido, txtGenero, txtDocumento;
    ImageView img_camera, img_firma;
    final String NAMESPACE = "http://tempuri.org/";
    final String URL = "http://" + Constantes.IP + "/WebServiceLUG_ANDROID.asmx";

    private OnFragmentInteractionListener mListener;

    public FragmentRegistro() {
        // Required empty public constructor
    }

    public void generarPDFOnClic() {
        Document document = new Document(PageSize.LETTER);
        String NOMBRE_ARCHIVO = "Estudiante.pdf";
        String tarjetaSD = Environment.getExternalStorageDirectory().toString();
        File pdfDir = new File(tarjetaSD + File.separator + NOMBRE_CARPETA_APP);

        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }
        File pdfSubDir = new File(pdfDir.getPath() + File.separator + GENERADOS);

        if (!pdfSubDir.exists()) {
            pdfSubDir.mkdir();
        }
        String nombre_completo = Environment.getExternalStorageDirectory()
                + File.separator + NOMBRE_CARPETA_APP +
                File.separator + GENERADOS + File.separator + NOMBRE_ARCHIVO;
        File outputfile = new File(nombre_completo);
        if (!outputfile.exists()) {
            outputfile.delete();
        }
        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document,
                    new FileOutputStream(nombre_completo));
            document.open();
            document.addAuthor("REPORTE DE EJEMPLO");
            document.addCreator("PDF");
            document.addSubject("GENERACION DE REPORTES EN PDF");
            document.addCreationDate();
            document.addTitle("ESTUDIANTES POR CURSO");

            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            String htmlPDF = "<html><head></head><boody><h1>ESTUDIANTES " +
                    "inscritos</h1><p>Estos son los estudiantes inscritos " +
                    "en PRIMERO</p>";

            worker.parseXHtml(pdfWriter, document, new StringReader(htmlPDF));
            document.close();
            Toast.makeText(getActivity(), "REPORTE GENERADO", Toast.LENGTH_SHORT).show();
            muestraPDF(nombre_completo, getActivity());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void muestraPDF(String archivo, Context context) {
        Toast.makeText(context, "Leyendo el archivo", Toast.LENGTH_SHORT).show();
        File file = new File(archivo);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No tiene una app para abrir este tipo de archivo", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_fragment_registro, container, false);
        bt_generar = (Button) view.findViewById(R.id.btnConsultar);
        bt_generar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPDFOnClic();
            }
        });
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
                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    img_camera.setImageBitmap(bitmap);
                }
            }
        } catch (Exception e) {
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
