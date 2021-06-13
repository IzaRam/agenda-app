package com.izaram.agendaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.izaram.agendaapp.R;
import com.izaram.agendaapp.model.Endereco;

import java.util.ArrayList;

public class RecyclerEnderecosAdapter extends RecyclerView.Adapter<RecyclerEnderecosAdapter.MyViewHolder> {

    private ArrayList<Endereco> enderecosList;
    private Context context;

    public RecyclerEnderecosAdapter(Context context, ArrayList<Endereco> enderecosList) {
        this.enderecosList = enderecosList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_endereco_lista, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvEnderecoCidade.setText(this.enderecosList.get(position).getCidade());
        holder.tvEnderecoEstado.setText(this.enderecosList.get(position).getUf());
    }

    @Override
    public int getItemCount() {
        return enderecosList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvEnderecoCidade;
        TextView tvEnderecoEstado;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEnderecoCidade = itemView.findViewById(R.id.tv_cidade);
            tvEnderecoEstado = itemView.findViewById(R.id.tv_estado);
        }
    }

}
