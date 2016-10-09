package com.example.luishurtado.registroestudiantev2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

public class MainActivityCamara extends AppCompatActivity {
    final static int CONS = 1;
    String datoBdFoto;
    Button btnFoto;
    Bitmap bmpFoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CONS ){
            Bundle ext = data.getExtras();
            bmpFoto = (Bitmap)ext.get("data");
            datoBdFoto = createImgeFromBitmap(bmpFoto);
        }
    }

    public String createImgeFromBitmap(Bitmap bitmap) {

        String fileName = "myImage";
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName,Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            fo.close();

        }catch(Exception e){
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }
}