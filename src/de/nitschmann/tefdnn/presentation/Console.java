package de.nitschmann.tefdnn.presentation;


import de.nitschmann.tefdnn.application.NeuralNetwork;
import de.nitschmann.tefdnn.application.TrainingEnvironment;
import de.nitschmann.tefdnn.application.io.ImageLoader;
import de.nitschmann.tefdnn.application.io.TrainingData;
import de.nitschmann.tefdnn.persistence.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Console {

    private TrainingEnvironment trainingEnvironment;
    private TrainingEnvironment trainedEnvironment;
    private Database database;
    private Saver saver;
    private Loader loader;
    private Configurator configurator;
    private ImageLoader imageLoader;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Initializes a new Console - therefore, a database connection is required as well as
     * saver, loader and configurator objects.
     * @param database
     */
    public Console(Database database) {
        this.database = database;
        this.saver = new Saver();
        this.loader = new Loader();
        this.configurator = new Configurator();
    }

    public boolean read() {
        try {
            String s = br.readLine();
            if (s.equals(":q")) {
                return false;
            }
            parse(s);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * returns a training environment by the input string
     * @param input
     * @return
     */
    public TrainingEnvironment init(String input) {
        trainingEnvironment = loader.initEnvironment(database, input);
        if (trainingEnvironment != null) {
            imageLoader = new ImageLoader(trainingEnvironment.getFeedForwardNetwork());
            System.out.println("Training environment initialized.");
        }
        return trainingEnvironment;
    }

    /**
     * saves the training environment by the input string
     * @param input
     * @return
     */
    public boolean save(String input, TrainingEnvironment trainingEnvironment) {
        if (saver.saveEnvironment(database, input, trainingEnvironment)) {
            System.out.println("Training environment saved.");
            return true;
        }
        return false;
    }

    /**
     * trains the training environment and returns the trained one
     * @param input
     * @param trainingEnvironment
     * @return
     */
    public TrainingEnvironment train(String input, TrainingEnvironment trainingEnvironment) {
        if (input.contains("-ptd:") && input.contains("-tn:")) {
            String path = Parser.parseString(input, "-ptd:");
            path = path.replace("\"", "");
            int neuron = Parser.parseInt(input, "-tn:");

            if (neuron == -1 || neuron >= trainingEnvironment.getFeedForwardNetwork().getOutputLayer().getNeurons().size()) {
                System.out.println("stop");
            }

            imageLoader.addTrainingSet(path, neuron);

            TrainingData trainingData = imageLoader.getTrainingData();

            if (trainingEnvironment.getAutoEncoderNetwork() != null) {
                trainingEnvironment.getAutoEncoderNetwork().setTrainSet(trainingData.getImages());
                trainingEnvironment.getAutoEncoderNetwork().setEstimatedResults(trainingData.getImages());
            }

            trainingEnvironment.getFeedForwardNetwork().setTrainSet(trainingData.getImages());
            trainingEnvironment.getFeedForwardNetwork().setEstimatedResults(trainingData.getEstimatedResults());
            return null;
        } else if (input.contains("-s")) {
            if (!configurator.isProperlyConfigured(trainingEnvironment)) {
                System.out.println("Training environment is not configured properly.");
                return null;
            }
            System.out.println("Training...");
            NeuralNetwork trainedAE = null;
            if (trainingEnvironment.getAutoEncoderNetwork() != null) {
                trainedAE = trainingEnvironment.getAutoEncoderNetwork().train(trainingEnvironment.getAutoEncoderNetwork());
            }
            NeuralNetwork trainedFF = trainingEnvironment.getFeedForwardNetwork().train(trainingEnvironment.getFeedForwardNetwork());
            TrainingEnvironment trainedEnvironment = new TrainingEnvironment(trainedFF, trainedAE);
            this.trainedEnvironment = trainedEnvironment;
            System.out.println("Training done");
            return trainedEnvironment;
        } else {
            System.out.println("parameters not specified correctly. Train takes following arguments:");
            System.out.println("-pTD: path to directory which contains training data, required");
            System.out.println("-tN: target output neuron which represents the trained class, required");
            return null;
        }
    }

    /**
     * saves the results of the specified training environment to the database
     * @param env
     * @param pathToImage
     * @param result
     */
    public void saveResult(TrainingEnvironment env, String pathToImage, String result) {
        if (saver.saveResult(database, env, pathToImage, result)) {
            System.out.println("Result saved in database.");
        }
    }

    /**
     * tests a training environment by the input string and saves the results to the database
     * @param input
     * @param trainedEnvironment
     */
    public boolean test(String input, TrainingEnvironment trainedEnvironment) {
        if (input.contains("-ptd:")) {
            String path = Parser.parseString(input, "-ptd:");
            path = path.replace("\"", "");

            if (!imageLoader.addTestPath(path)) {
                return false;
            }
            Map<String, double[]> testData = imageLoader.getTestImages(imageLoader.getTrainingData().getMeanImage());

            Map<String, double[]> testDataAfterAutoencoder = new HashMap<>();
            if (trainedEnvironment.getAutoEncoderNetwork() != null) {
                Iterator it = testData.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    trainedEnvironment.getAutoEncoderNetwork().setInput(trainedEnvironment.getAutoEncoderNetwork(), (double[]) pair.getValue());
                    List<Double> result = trainedEnvironment.getAutoEncoderNetwork().test(trainedEnvironment.getAutoEncoderNetwork());
                    double[] res = new double[result.size()];
                    for (int i = 0; i < result.size(); i++) {
                        res[i] = result.get(i);
                    }
                    testDataAfterAutoencoder.put((String) pair.getKey(), res);
                }
            }
            if (testDataAfterAutoencoder.size() != 0) {
                testData = testDataAfterAutoencoder;
            }

            Iterator it = testData.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                trainedEnvironment.getFeedForwardNetwork().setInput(trainedEnvironment.getFeedForwardNetwork(), (double[]) pair.getValue());
                List<Double> result = trainedEnvironment.getFeedForwardNetwork().test(trainedEnvironment.getFeedForwardNetwork());
                System.out.println(pair.getKey());
                System.out.println(result);
                saveResult(trainedEnvironment, (String) pair.getKey(), result.toString());
            }
            imageLoader.removeTestPath(path);
            return true;
        } else if (input.contains("-ps:")) {
            // test single picture
            String path = Parser.parseString(input, "-ps:");
            path = path.replace("\"", "");

            if (!imageLoader.setTestImage(path)) {
                return false;
            }
            double[] testData = imageLoader.getTestImage(imageLoader.getTrainingData().getMeanImage());

            trainedEnvironment.getFeedForwardNetwork().setInput(trainedEnvironment.getFeedForwardNetwork(), testData);
            List<Double> result = trainedEnvironment.getFeedForwardNetwork().test(trainedEnvironment.getFeedForwardNetwork());
            System.out.println(path);
            System.out.println(result);
            return true;
        } else {
            System.out.println("parameters not specified correctly. Test takes following arguments:");
            System.out.println("-pTD: path to image directory which contains testing data");
            System.out.println("-pS: path to single image");
            System.out.println("Either -pTD or -pS needs to be specified");
            return false;
        }
    }

    /**
     * configues the training environment by the input string
     * @param input
     * @param trainingEnvironment
     */
    public boolean conf(String input, TrainingEnvironment trainingEnvironment) {
        if (configurator.configureEnvironment(input, trainingEnvironment)) {
            System.out.println("Training environment configured.");
            return true;
        }
        return false;
    }

    /**
     * parses the user input and calls the required methods
     * @param input
     */
    public void parse(String input) {
        input = input.trim().toLowerCase();
        if (input.startsWith("init")) {
            init(input);
        } else if (input.startsWith("save")) {
            if (trainingEnvironment == null) {
                System.out.println("Training environment is null. You gotta initialize it first.");
                return;
            }
            save(input, trainingEnvironment);
        } else if (input.startsWith("train")) {
            if (trainingEnvironment == null) {
                System.out.println("Training environment is null. You gotta initialize it first.");
                return;
            }
            train(input, trainingEnvironment);
        } else if (input.startsWith("test")) {
            if (trainingEnvironment == null) {
                System.out.println("Training environment is null. You gotta initialize it first.");
                return;
            }
            test(input, trainedEnvironment);
        } else if (input.startsWith("conf")) {
            if (trainingEnvironment == null) {
                System.out.println("Training environment is null. You gotta initialize it first.");
                return;
            }
            conf(input, trainingEnvironment);
        } else {
            System.out.println("Command was not found");
        }
    }

}
