package de.nitschmann.tefdnn.application.io;

public class Image {

    private double[] imageData;
    private double mean;

    public Image(double[] imageData, double mean) {
        this.imageData = imageData;
        this.mean = mean;
    }

    public Image(double[] imageData) {
        this.imageData = imageData;
    }

    public double[] getImageData() {
        return this.imageData;
    }

    public double getMean() {
        return this.mean;
    }
}
