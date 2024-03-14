package edu.northeastern.groupproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WeatherInfoAdapter extends RecyclerView.Adapter<WeatherInfoAdapter.ViewHolder> {

    private List<String> weatherDetails;

    public WeatherInfoAdapter(List<String> weatherDetails) {
        this.weatherDetails = weatherDetails;
    }
    ImageView clothingIconImageView;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.weatherTextView.setText(weatherDetails.get(position));
    }

    @Override
    public int getItemCount() {
        return weatherDetails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView weatherTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            weatherTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}
