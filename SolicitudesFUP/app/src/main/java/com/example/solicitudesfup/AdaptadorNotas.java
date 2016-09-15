package com.example.solicitudesfup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Luis Hurtado on 08/06/2016.
 */
public class AdaptadorNotas extends ArrayAdapter {

    private Activity context;
    Notas[] datos;
    ViewHolderNotas holderNotas;

    public AdaptadorNotas(Activity context, Notas[] datos) {
        super(context, R.layout.activity_main2_notas, datos);
        this.context = context;
        this.datos = datos;
    }

    public View getView(int posicion, View convertView, ViewGroup parent) {
        View item = convertView;


        if (item == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            item = inflater.inflate(R.layout.activity_main2_notas, null);
            holderNotas = new ViewHolderNotas();

            holderNotas.Txt_Cod_Asig = (TextView) item.findViewById(R.id.Text_Codigo);
            holderNotas.Txt_Asignatura = (TextView) item.findViewById(R.id.Txt_Asignatura);
            holderNotas.Text_N1 = (TextView) item.findViewById(R.id.Text_N1);
            holderNotas.Text_N2 = (TextView) item.findViewById(R.id.Text_N2);
            holderNotas.Text_N3 = (TextView) item.findViewById(R.id.Text_N3);
            holderNotas.Text_Def = (TextView) item.findViewById(R.id.Text_Def);

            item.setTag(holderNotas);

        } else {
            holderNotas = (ViewHolderNotas) item.getTag();
        }

        holderNotas.Txt_Cod_Asig.setText(datos[posicion].getCod());
        holderNotas.Txt_Asignatura.setText(datos[posicion].getAsignatura());
        holderNotas.Text_N1.setText(datos[posicion].getNota1());
        holderNotas.Text_N2.setText(datos[posicion].getNota2());
        holderNotas.Text_N3.setText(datos[posicion].getNota3());
        holderNotas.Text_Def.setText(datos[posicion].getDefinitiva());

        return item;
    }

}
