package com.example.happy.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happy.R;
import com.example.happy.modelos.Amigo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AmigoAdapter extends RecyclerView.Adapter<AmigoAdapter.ViewHolder> {

    private int resource;
    private ArrayList<Amigo> amigosList;
    private View.OnClickListener listener;

    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public AmigoAdapter(ArrayList<Amigo> amigosList, int resource){

        this.amigosList = amigosList;
        this.resource = resource;

    }

    @NonNull
    @Override
    public AmigoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Eliminar Amigo");
                builder.setMessage("¿Deseas eliminar este amigo?");

                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        firebaseAuth = FirebaseAuth.getInstance();
                        firebaseUser = firebaseAuth.getCurrentUser();
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("amigos");

                        databaseReference.child(holder.idAmigo.getText().toString()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(v.getContext(), "Se ha eliminado tu regalo", Toast.LENGTH_SHORT).show();
                                amigosList.clear();

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
        return amigosList.size();
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
