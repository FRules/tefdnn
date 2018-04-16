package de.nitschmann.tefdnn.application.io;

import de.nitschmann.tefdnn.application.NeuralNetwork;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ImageLoader {

    private NeuralNetwork nn;
    private Map<String, Integer> pathNeuronTrainingMap = new HashMap<>();
    private Set<String> testPaths = new HashSet<>();
    private String testImage;

    public ImageLoader(NeuralNetwork nn) {
        this.nn = nn;
    }

    public boolean addTestPath(String path) {
        if (!new File(path).exists()) {
            System.out.println("Directory doesn't exist.");
            return false;
        }

        if (!testPaths.contains(path))
            testPaths.add(path);

        return true;
    }

    public boolean removeTestPath(String path) {
        if (!new File(path).exists()) {
            System.out.println("Directory doesn't exist.");
            return false;
        }

        if (testPaths.contains(path)) {
            testPaths.remove(path);
        }

        return true;
    }

    public boolean setTestImage(String path) {
        if (!new File(path).exists()) {
            System.out.println("Directory doesn't exist.");
            return false;
        }

        this.testImage = path;
        return true;
    }

    public void addTrainingSet(String path, int neuronWhichShouldFire) {
        if (!new File(path).exists()) {
            System.out.println("Directory doesn't exist.");
            return;
        }

        if (pathNeuronTrainingMap.containsKey(path)) {
            System.out.println("Path already exists. Choose another path.");
            return;
        }

        if (pathNeuronTrainingMap.containsValue(neuronWhichShouldFire)) {
            System.out.println("Output Neuron is already responsible to recognize another class. Choose another neuron.");
            return;
        }
        pathNeuronTrainingMap.put(path, neuronWhichShouldFire);
    }

    public TrainingData getTrainingData() {
        int elements = getElementsInTrainingFolders();
        double[][] images = new double[elements][nn.getInputLayer().getNeurons().size() - 1];
        double[][] estimatedResults = new double[elements][nn.getOutputLayer().getNeurons().size()];

        int totalNumberOfImages = 0;
        double mean = 0;

        for (Map.Entry<String, Integer> entry : pathNeuronTrainingMap.entrySet()) {
            File folder = new File(entry.getKey());
            for (final File fileEntry : folder.listFiles()) {
                try {
                    BufferedImage image = convertToGray(ImageIO.read(fileEntry));
                    image = createResizedCopy(image, 28, 28, false);
                    Image img = getImage(image);

                    mean += img.getMean();
                    images[totalNumberOfImages] = img.getImageData();

                    estimatedResults[totalNumberOfImages][entry.getValue()] = 1;
                    totalNumberOfImages++;
                } catch (IOException e) {

                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping file: " + fileEntry.getAbsolutePath());
                }
            }
        }

        mean = mean / 1.0 / totalNumberOfImages;
        return new TrainingData(images, estimatedResults, mean);
    }

    public Map<String, double[]> getTestImages(double meanImage) {
        Map<String, double[]> map = new HashMap<>();
        double[][] testImages = new double[getElementsInTestFolders()][784];
        int i = 0;

        for (String s : testPaths) {
            File folder = new File(s);

            for (final File fileEntry : folder.listFiles()) {
                try {
                    BufferedImage image = convertToGray(ImageIO.read(fileEntry));
                    image = createResizedCopy(image, 28, 28, false);
                    Image img = getImage(image, meanImage);
                    testImages[i] = img.getImageData();
                    map.put(fileEntry.getAbsolutePath(), testImages[i]);
                    i++;
                } catch (IOException e) {

                }
            }
        }

        return map;
    }

    public double[] getTestImage(double meanImage) {
        File file = new File(this.testImage);
        try {
            BufferedImage image = convertToGray(ImageIO.read(file));
            image = createResizedCopy(image, 28, 28, false);
            Image img = getImage(image, meanImage);
            return img.getImageData();
        } catch (IOException e) {

        }
        return null;
    }

    private Image getImage(BufferedImage image) {
        double mean = 0;
        int imageIterations = 0;
        double[] imgData = new double[784];
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 28; j++) {
                int rgb = image.getRGB(i, j);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);
                double gray = (r + g + b) / 3.0 / 255.0;
                mean += gray;
                imgData[imageIterations] = gray;
                imageIterations++;
            }
        }
        mean = mean / 1.0 / imageIterations;
        return new Image(imgData, mean);
    }

    private Image getImage(BufferedImage image, double mean) {
        int imageIterations = 0;
        double[] imgData = new double[784];
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 28; j++) {
                int rgb = image.getRGB(i, j);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);
                double gray = (r + g + b) / 3.0 / 255.0;
                imgData[imageIterations] = gray - mean;
                imageIterations++;
            }
        }
        return new Image(imgData);
    }

    private BufferedImage convertToGray(BufferedImage image) {
        BufferedImage gray;
        try {
            // create a grayscale image the same size
            gray = new BufferedImage(image.getWidth(), image.getHeight(),
                    BufferedImage.TYPE_BYTE_GRAY);

            // convert the original colored image to grayscale
            ColorConvertOp op = new ColorConvertOp(
                    image.getColorModel().getColorSpace(),
                    gray.getColorModel().getColorSpace(), null);
            op.filter(image, gray);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return gray;
    }

    private BufferedImage createResizedCopy(java.awt.Image originalImage,
                                    int scaledWidth, int scaledHeight,
                                    boolean preserveAlpha)
    {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }

    private int getElementsInTestFolders() {
        int sum = 0;
        for (String s : testPaths) {
            sum += new File(s).listFiles().length;
        }
        return sum;
    }

    private int getElementsInTrainingFolders() {
        int sum = 0;
        for (Map.Entry<String, Integer> entry : pathNeuronTrainingMap.entrySet()) {
            sum += new File(entry.getKey()).listFiles().length;
        }
        return sum;
    }
}
