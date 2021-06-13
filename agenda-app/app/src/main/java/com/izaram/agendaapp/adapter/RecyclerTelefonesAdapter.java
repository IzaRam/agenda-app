package com.izaram.agendaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.izaram.agendaapp.R;
import com.izaram.agendaapp.model.Telefone;

import java.util.ArrayList;

public class RecyclerTelefonesAdapter extends RecyclerView.Adapter<RecyclerTelefonesAdapter.MyViewHolder> {

    private ArrayList<Telefone> telefonesList;
    private Context context;

    public RecyclerTelefonesAdapter(Context context, ArrayList<Telefone> telefonesList) {
        this.telefonesList = telefonesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_telefone_lista, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvTelefoneNumero.setText(this.telefonesList.get(position).getNumero());
        holder.tvTelefoneTipo.setText(this.telefonesList.get(position).getTipo());
    }

    @Override
    public int getItemCount() {
        return telefonesList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTelefoneNumero;
        TextView tvTelefoneTipo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTelefoneNumero = itemView.findViewById(R.id.tv_numero);
            tvTelefoneTipo = itemView.findViewById(R.id.tv_tipo);
        }
    }

}
