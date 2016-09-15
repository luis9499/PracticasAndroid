package com.example.solicitudesfup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
    Button boton2, boton3, boton4, boton5, boton6;
    String cod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        cod = getIntent().getExtras().getString("codigo");
        Toast.makeText(Main2Activity.this, " "+cod, Toast.LENGTH_SHORT).show();
        boton2 = (Button) findViewById(R.id.btn_Notas);
        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notas = new Intent(Main2Activity.this, Main2ActivityNotas.class);
                startActivity(notas);
            }
        });
        boton3 = (Button) findViewById(R.id.btn_Sol_Cert);
        boton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent solCert = new Intent(Main2Activity.this, Main2Activity_Sol_Cert.class);
                startActivity(solCert);
            }
        });
        boton4 = (Button) findViewById(R.id.btn_Pet);
        boton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent solPet = new Intent(Main2Activity.this, Main2Activity_Peticiones.class);
                startActivity(solPet);
            }
        });
        boton5 = (Button) findViewById(R.id.btn_Consul);
        boton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent consul = new Intent(Main2Activity.this, Main2Activity_Cons_Sol.class);
                startActivity(consul);
            }
        });
        boton6 = (Button) findViewById(R.id.btn_Cerrar);
        boton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
