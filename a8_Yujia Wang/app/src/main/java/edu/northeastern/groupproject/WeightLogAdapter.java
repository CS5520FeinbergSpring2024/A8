package edu.northeastern.groupproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WeightLogAdapter extends RecyclerView.Adapter<WeightLogAdapter.ViewHolder> {

    private List<WeightLog> weightLogs = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate, tvWeight, tvBodyFat;

        public ViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvDate);
            tvWeight = view.findViewById(R.id.tvWeight);
            tvBodyFat = view.findViewById(R.id.tvBodyFat);
        }

        public void bind(WeightLog log) {
            tvDate.setText(log.getDate());
            tvWeight.setText(String.format("%.2f kg", log.getWeight()));
            tvBodyFat.setText(String.format("%.2f%%", log.getBodyFat()));
        }
    }

    public WeightLogAdapter(List<WeightLog> dataSet) {
        weightLogs = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_log_weight, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.bind(weightLogs.get(position));
    }

    @Override
    public int getItemCount() {
        return weightLogs == null ? 0 : weightLogs.size();
    }
    public void setWeightLogs(List<WeightLog> weightLogs) {
        this.weightLogs = weightLogs != null ? weightLogs : new ArrayList<>();
        notifyDataSetChanged();
    }

}
