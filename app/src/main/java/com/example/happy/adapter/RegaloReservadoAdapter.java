package com.example.happy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happy.R;
import com.example.happy.modelos.Regalo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RegaloReservadoAdapter extends FirestoreRecyclerAdapter<Regalo, RegaloReservadoAdapter.ViewHolder> {

    public RegaloReservadoAdapter(@NonNull FirestoreRecyclerOptions<Regalo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RegaloReservadoAdapter.ViewHolder viewHolder, int i, @NonNull Regalo regalo) {

        viewHolder.nombre.setText(regalo.getNombre());
        viewHolder.link.setText(regalo.getLink());
        viewHolder.regaloReservado.setText(regalo.getRegaloReservado());

    }

    @NonNull
    @Override
    public RegaloReservadoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_regalo_single, parent, false);
        return new RegaloReservadoAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nombre, link, regaloReservado;

        public ViewHolder(View view){
            super(view);

            this.nombre = (TextView) view.findViewById(R.id.nombre);
            this.link = (TextView) view.findViewById(R.id.link);
            this.regaloReservado = (TextView) view.findViewById(R.id.regaloReservado);

        }

    }
}
