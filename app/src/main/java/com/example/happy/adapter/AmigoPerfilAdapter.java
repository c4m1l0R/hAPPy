package com.example.happy.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happy.R;
import com.example.happy.modelos.Amigo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


public class AmigoPerfilAdapter extends FirestoreRecyclerAdapter<Amigo, AmigoPerfilAdapter.ViewHolder> {

    public AmigoPerfilAdapter(@NonNull FirestoreRecyclerOptions<Amigo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AmigoPerfilAdapter.ViewHolder viewHolder, int i, @NonNull Amigo amigo) {

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAbsoluteAdapterPosition());
        final String id = documentSnapshot.getId();

        viewHolder.nombreAmigo.setText(amigo.getNombre());
        viewHolder.idAmigo.setText(amigo.getIdAmigo());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("idAmigo", viewHolder.idAmigo.getText().toString());
                bundle.putString("idAmigoColeccion", id);
                Navigation.findNavController(v).navigate(R.id.fragmentPerfilAmigos, bundle);
            }
        });

    }

    @NonNull
    @Override
    public AmigoPerfilAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_amigo_single, parent, false);
        return new AmigoPerfilAdapter.ViewHolder(view);

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nombreAmigo,idAmigo;
        private CardView cardView;

        public ViewHolder(View view){
            super(view);

            this.nombreAmigo = (TextView) view.findViewById(R.id.nombreAmigo);
            this.idAmigo = (TextView) view.findViewById(R.id.idAmigo);
            this.cardView = (CardView) view.findViewById(R.id.cardViewAmigo);

        }

    }
}

