package com.example.happy.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happy.R;
import com.example.happy.modelos.Amigo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AmigoAdapter extends RecyclerView.Adapter<AmigoAdapter.ViewHolder> implements View.OnClickListener {

    private int resource;
    private ArrayList<Amigo> amigosList;
    private View.OnClickListener listener;

    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //referencias para comunicar fragments


    public AmigoAdapter(ArrayList<Amigo> amigosList, int resource){

        this.amigosList = amigosList;
        this.resource = resource;

    }

    @NonNull
    @Override
    public AmigoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        view.setOnClickListener(this);
        return new AmigoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Amigo amigo = amigosList.get(position);
        holder.nombreAmigo.setText(amigo.getNombre()+ " " +amigo.getApellido1()+" "+amigo.getApellido2());
        holder.idAmigo.setText(amigo.getIdAmigo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("idAmigo", holder.idAmigo.getText().toString());
                Navigation.findNavController(v).navigate(R.id.fragmentPerfilAmigos, bundle);


            }
        });

    }


    @Override
    public int getItemCount() {
        return amigosList.size();
    }


    @Override
    public void onClick(View v) {

        if(listener !=null){

            listener.onClick(v);
        }

    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nombreAmigo;
        private TextView idAmigo;
        public View view;

        public ViewHolder(View view){
            super(view);

            this.view = view;
            this.nombreAmigo = (TextView) view.findViewById(R.id.nombreAmigo);
            this.idAmigo = (TextView) view.findViewById(R.id.idAmigo);
        }

    }



}
