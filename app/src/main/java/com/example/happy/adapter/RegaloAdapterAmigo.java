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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happy.R;
import com.example.happy.modelos.Regalo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegaloAdapterAmigo extends FirestoreRecyclerAdapter<Regalo,RegaloAdapterAmigo.ViewHolder> {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore;

    private String idAmigo = "";

    public RegaloAdapterAmigo(@NonNull FirestoreRecyclerOptions<Regalo> options) {
        super(options);
    }

    @NonNull
    @Override
    public RegaloAdapterAmigo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_regalo_single, parent, false);
        return new RegaloAdapterAmigo.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegaloAdapterAmigo.ViewHolder viewHolder, int i, @NonNull Regalo regalo) {

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAbsoluteAdapterPosition());
        final String idRegalo = documentSnapshot.getId();

        firebaseFirestore = FirebaseFirestore.getInstance();

        viewHolder.nombre.setText(regalo.getNombre());
        viewHolder.link.setText(regalo.getLink());
        viewHolder.regaloReservado.setText(regalo.getRegaloReservado());

        if(viewHolder.regaloReservado.getText().toString().equals("true")){

            viewHolder.linearLayout.setBackgroundColor(Color.BLUE);
        }

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!viewHolder.regaloReservado.getText().toString().equals("true")){

                    reservarRegalo(v, viewHolder.nombre.getText().toString(),viewHolder.link.getText().toString(), idRegalo);

                }

            }
        });
    }

    private void reservarRegalo(View v, String nombre, String link, String idRegalo){

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
        builder.setTitle("Reservar regalo");
        builder.setMessage("Seguro que quieres reservar este regalo");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                firebaseFirestore.collection("users").document(getIdAmigo()).collection("regalos").
                        document(idRegalo).update("regaloReservado", "true").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                cargarRegaloAMiLista(v, nombre, link, idRegalo);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(v.getRootView().getContext(), "Error al reservar este regalo",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    private void cargarRegaloAMiLista(View v,String nombre, String link, String idRegalo){

        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("nombre", nombre);
        hashMap.put("link", link);
        hashMap.put("idRegalo", idRegalo);

        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).
                collection("regalosReservados").add(hashMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(v.getRootView().getContext(), "Tu regalo se ha reservado correctamente",
                                Toast.LENGTH_SHORT).show();

                        Navigation.findNavController(v).navigate(R.id.fragmentBandeja);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(v.getRootView().getContext(), "Error al reservar este regalo",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void setIdAmigo(String idAmigo){

        this.idAmigo = idAmigo;
    }

    public String getIdAmigo(){

        return this.idAmigo;
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
