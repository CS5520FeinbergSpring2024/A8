package edu.northeastern.groupproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {
    private final List<Forecast> forecastList;

    public ForecastAdapter(List<Forecast> forecastList) {
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Forecast forecast = forecastList.get(position);
        holder.dateTextView.setText(forecast.getDate());
        holder.tempTextView.setText(String.format("%.2f°F", forecast.getTemp()));
        holder.descriptionTextView.setText(forecast.getDescription());
        String iconUrl = "https://openweathermap.org/img/wn/" + forecast.getIconCode() + ".png";
        Glide.with(holder.itemView.getContext()).load(iconUrl).into(holder.weatherIconImageView);
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, tempTextView, descriptionTextView;
        ImageView weatherIconImageView;

        ViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            tempTextView = itemView.findViewById(R.id.tempTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            weatherIconImageView = itemView.findViewById(R.id.weatherIconImageView);
        }
    }
}
