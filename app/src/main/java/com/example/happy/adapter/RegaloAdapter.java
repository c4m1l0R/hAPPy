package com.example.happy.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happy.R;
import com.example.happy.modelos.Regalo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class RegaloAdapter extends FirestoreRecyclerAdapter<Regalo,RegaloAdapter.ViewHolder> {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public RegaloAdapter(@NonNull FirestoreRecyclerOptions<Regalo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RegaloAdapter.ViewHolder viewHolder, int i, @NonNull Regalo regalo) {

        viewHolder.nombre.setText(regalo.getNombre());
        viewHolder.link.setText(regalo.getLink());
        viewHolder.regaloReservado.setText(regalo.getRegaloReservado());

        if(viewHolder.regaloReservado.getText().toString().equals("true")){

            viewHolder.linearLayout.setBackgroundColor(Color.BLUE);
        }

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAbsoluteAdapterPosition());
        final String id = documentSnapshot.getId();

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                borrarRegalo (id, v, viewHolder.regaloReservado.getText().toString());
            }
        });
    }

    private void borrarRegalo(String id, View v, String regaloReservado){

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
        builder.setTitle("Eliminar regalo");
        builder.setMessage("Seguro que quieres eliminar el regalo");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(regaloReservado.equals("true")){

                    Toast.makeText(v.getRootView().getContext(), "Este regalo ya esta reservado por uno de tus amigos",
                            Toast.LENGTH_SHORT).show();

                }else{

                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                    firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).
                            collection("regalos").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(v.getRootView().getContext(), "Regalo eliminado con éxito",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(v.getRootView().getContext(), "No se ha podido eliminar el regalo",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                }

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }


    @NonNull
    @Override
    public RegaloAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_regalo_single, parent, false);
        return new ViewHolder(view);

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nombre, link, regaloReservado;
        private CardView cardView;
        private LinearLayout linearLayout;

        public ViewHolder(View view){
            super(view);

            this.nombre = (TextView) view.findViewById(R.id.nombre);
            this.link = (TextView) view.findViewById(R.id.link);
            this.regaloReservado = (TextView) view.findViewById(R.id.regaloReservado);
            this.cardView = (CardView) view.findViewById(R.id.cardViewRegalo);
            this.linearLayout = (LinearLayout) view.findViewById(R.id.linearRegalo);

        }

    }

}
