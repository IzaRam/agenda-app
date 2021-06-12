package com.izaram.agendaapp.adapter;

import com.izaram.agendaapp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerContatosAdapter extends RecyclerView.Adapter<RecyclerContatosAdapter.MyViewHolder> {

    private ArrayList<String> contatosList;
    private ArrayList<String> contatosListFull;
    private Context context;

    public RecyclerContatosAdapter(Context context, ArrayList<String> contatosList) {
        this.contatosList = contatosList;
        this.contatosListFull = new ArrayList<>(contatosList);
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contato_lista, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvContatonome.setText(this.contatosList.get(position));
    }

    @Override
    public int getItemCount() {
        return contatosList.size();
    }

    //Filter Results on RecyclerView
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<String> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(contatosListFull);
            } else {
                contatosList.clear();
                contatosList.addAll(contatosListFull);
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (String item : contatosList) {
                    if (item.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contatosList.clear();
            contatosList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvContatonome;
        CardView cvContato;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvContatonome = itemView.findViewById(R.id.tv_nome);
            cvContato = itemView.findViewById(R.id.cv_contato);
        }
    }

}
