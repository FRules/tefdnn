package de.nitschmann.tefdnn.application.io;

public class TrainingData {
    private double[][] images;
    private double[][] estimatedResults;
    private double meanImage;

    public TrainingData(double[][] images, double[][] estimatedResults, double meanImage) {
        this.images = new double[images.length][];
        for (int i = 0; i < images.length; i++) {
            this.images[i] = new double[images[i].length];
            for (int j = 0; j < images[i].length; j++) {
                this.images[i][j] = images[i][j] - meanImage;
            }
        }
        this.estimatedResults = estimatedResults;
        this.meanImage = meanImage;
    }

    public double[][] getImages() {
        return this.images;
    }

    public double[][] getEstimatedResults() {
        return this.estimatedResults;
    }

    public double getMeanImage() {
        return this.meanImage;
    }
}
