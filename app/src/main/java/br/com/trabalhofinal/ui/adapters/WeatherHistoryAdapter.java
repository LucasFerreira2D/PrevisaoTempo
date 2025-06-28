package br.com.trabalhofinal.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.trabalhofinal.R;
import br.com.trabalhofinal.data.local.model.WeatherHistory;

public class WeatherHistoryAdapter extends RecyclerView.Adapter<WeatherHistoryAdapter.ViewHolder> {

    private List<WeatherHistory> historyList;

    public WeatherHistoryAdapter(List<WeatherHistory> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_historico, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherHistory history = historyList.get(position);
        holder.textViewCidade.setText(history.cityName);
        holder.textViewDataHora.setText(history.dateTime);
        holder.textViewTemperatura.setText(history.temperature);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCidade, textViewDataHora, textViewTemperatura;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCidade = itemView.findViewById(R.id.textViewCidadeValue);
            textViewDataHora = itemView.findViewById(R.id.textViewDataHoraValue);
            textViewTemperatura = itemView.findViewById(R.id.textViewTemperaturaValue);
        }
    }
}
