package com.example.happy.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happy.R;
import com.example.happy.modelos.Regalo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class RegaloAdapterAmigo extends RecyclerView.Adapter<RegaloAdapterAmigo.ViewHolder>{

    private int resource;
    private ArrayList<Regalo> regalosList;


    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public RegaloAdapterAmigo(ArrayList<Regalo> regalosList, int resource){
        this.regalosList = regalosList;
        this.resource = resource;

    }

    @NonNull
    @Override
    public RegaloAdapterAmigo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new RegaloAdapterAmigo.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Regalo regalo = regalosList.get(position);
        holder.nombre.setText(regalo.getNombre());
        holder.link.setText(regalo.getLink());
       // holder.idRegalo.setText(regalo.getIdRegalo());
        holder.regaloReservado.setText(regalo.getRegaloReservado());

        if(regalo.getRegaloReservado().equalsIgnoreCase("true")){

            holder.nombre.setText(holder.nombre.getText()+"   RESERVADO ");
            holder.link.setTextColor(Color.RED);
            holder.nombre.setTextColor(Color.RED);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Agregar Regalo");
                builder.setMessage("¿Deseas adjudicarte este regalo?");

                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return regalosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nombre;
        private TextView link;
        private TextView idRegalo;
        private TextView regaloReservado;
        public View view;

        public ViewHolder(View view){
            super(view);

            this.view = view;
            this.nombre = (TextView) view.findViewById(R.id.nombre);
            this.link = (TextView) view.findViewById(R.id.link);
            //this.idRegalo = (TextView) view.findViewById(R.id.idRegalo);
            this.regaloReservado = (TextView) view.findViewById(R.id.regaloReservado);
        }

    }
}
