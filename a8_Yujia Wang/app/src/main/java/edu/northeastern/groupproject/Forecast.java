package edu.northeastern.groupproject;

public class Forecast {
    private String date;
    private double temp;
    private String description;
    private String iconCode;

    public Forecast(String date, double temp, String description, String iconCode) {
        this.date = date;
        this.temp = temp;
        this.description = description;
        this.iconCode = iconCode;
    }

    // Getters
    public String getDate() { return date; }
    public double getTemp() { return temp; }
    public String getDescription() { return description; }
    public String getIconCode() { return iconCode; }
}
