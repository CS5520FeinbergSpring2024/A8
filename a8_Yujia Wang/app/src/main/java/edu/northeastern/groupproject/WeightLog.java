package edu.northeastern.groupproject;


public class WeightLog {
    private String date;
    private double weight;
    private double bodyFat; // Consider storing as a percentage

    // Constructor
    public WeightLog(String date, double weight, double bodyFat) {
        this.date = date;
        this.weight = weight;
        this.bodyFat = bodyFat;
    }

    // Getters
    public String getDate() { return date; }
    public double getWeight() { return weight; }
    public double getBodyFat() { return bodyFat; }
}

