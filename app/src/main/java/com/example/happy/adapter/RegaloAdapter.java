package com.example.happy.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happy.R;
import com.example.happy.modelos.Regalo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegaloAdapter extends RecyclerView.Adapter<RegaloAdapter.ViewHolder> {

    private int resource;
    private ArrayList<Regalo> regalosList;
    private View.OnClickListener listener;


    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    public RegaloAdapter(ArrayList<Regalo> regalosList, int resource){
        this.regalosList = regalosList;
        this.resource = resource;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Regalo regalo = regalosList.get(position);
        holder.nombre.setText(regalo.getNombre());
        holder.link.setText(regalo.getLink());
        holder.idRegalo.setText(regalo.getIdRegalo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Borrar Regalo");
                builder.setMessage("¿Deseas eliminar este regalo de tu lista?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        firebaseAuth = FirebaseAuth.getInstance();
                        firebaseUser = firebaseAuth.getCurrentUser();
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("regalos");

                        databaseReference.child(holder.idRegalo.getText().toString()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(v.getContext(), "Se ha eliminado tu regalo", Toast.LENGTH_SHORT).show();
                                regalosList.clear();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "No se ha podido eliminar tu regalo", Toast.LENGTH_SHORT).show();
                            }
                        });
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
        public View view;

        public ViewHolder(View view){
            super(view);

            this.view = view;
            this.nombre = (TextView) view.findViewById(R.id.nombre);
            this.link = (TextView) view.findViewById(R.id.link);
            this.idRegalo = (TextView) view.findViewById(R.id.idRegalo);
        }

    }

}
