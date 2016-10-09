package com.example.luishurtado.registroestudiantev2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class MainActivityFirma extends AppCompatActivity {
    public static final int SIGNATURE_ACTIVITY = 1;
    final static int CONS = 1;
    String datoBdFirma;
    ImageView firma;
    Bitmap bmpFirma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == SIGNATURE_ACTIVITY && resultCode == CaptureSignature.RESULT_OK) {

                Bitmap imagenFirma = null;
                Bundle bundle = data.getExtras();
                String status = bundle.getString("status");
                if (status.equalsIgnoreCase("done")) {
                    datoBdFirma = bundle.getString("imagen");

                    try {
                        imagenFirma = BitmapFactory.decodeStream(MainActivityFirma.this.openFileInput(datoBdFirma));
                        firma.setImageBitmap(imagenFirma);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();

                    }

                    Toast toast = Toast.makeText(this, "Signature capture successful!", Toast.LENGTH_SHORT);

                    toast.setGravity(Gravity.TOP, 105, 50);
                    toast.show();

                }
            }
        } catch (Exception e) {

        }
    }
}
