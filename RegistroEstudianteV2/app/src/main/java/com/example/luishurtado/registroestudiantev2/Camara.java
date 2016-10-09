package com.example.luishurtado.registroestudiantev2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Camara extends Activity {

    private Camera mCamara;
    private CamaraPreview mPreview;
    int degrees = 0;
    static int idCamara = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fragment_registro);

        mCamara = getCameraInstance();
        setCameraDisplayOrientation(0, mCamara);
        mPreview = new CamaraPreview(this, mCamara);
        FrameLayout preview = (FrameLayout) findViewById(R.id.img_Foto);
        preview.addView(mPreview);

        View capa2 = View.inflate(this, R.layout.fragment_camara, null);
        preview.addView(capa2);
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(idCamara); //idcamara = 0 camara trasera, idCamara = 1 camara delantera
        } catch (Exception e) {

        }
        return c;
    }

    public class CamaraPreview extends SurfaceView implements SurfaceHolder.Callback {


        private SurfaceHolder mHolder;
        private Camera mCamera;

        public CamaraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();

            } catch (IOException e) {

            }

        }


        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

        public void CambiarCamara(int idCam) {
            //1. paramos la previsialiacion
            try {
                mCamera.stopPreview();
                mCamera.release();
                idCamara = idCam;
                mCamera = getCameraInstance();
                setCameraDisplayOrientation(idCam, mCamera);
                mCamera.setPreviewDisplay(mHolder);

            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();
        }
    }

    public void setCameraDisplayOrientation(int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();

        Camera.getCameraInfo(cameraId, info);

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;

            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;

        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    public void cambiarCamara(View v) {
        ToggleButton toggleCamara = (ToggleButton) v;

        boolean estadoPresionado = toggleCamara.isChecked();

        if (estadoPresionado) {
            //camara delantera
            mPreview.CambiarCamara(1);

        } else {
            //camara trasera
            mPreview.CambiarCamara(0);
        }
    }

    public void tomarFoto(View view) {
        mPreview.mCamera.takePicture(null, null, mPicture);

    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {


            Intent irPreview = new Intent(Camara.this, FragmentRegistro.class);
            irPreview.putExtra("data", data);
            irPreview.putExtra("idCamara", idCamara);
            Camara.this.startActivity(irPreview);
            Camara.this.finish();

        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if (mPreview.mCamera != null) {
            mPreview.mCamera.stopPreview();
            mPreview.mCamera.release();
        }
    }


}
