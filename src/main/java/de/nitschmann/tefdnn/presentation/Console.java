package de.nitschmann.tefdnn.presentation;

import de.nitschmann.tefdnn.application.NeuralNetwork;
import de.nitschmann.tefdnn.application.Neuron;
import de.nitschmann.tefdnn.application.io.ImageLoader;
import de.nitschmann.tefdnn.application.io.TrainingData;
import de.nitschmann.tefdnn.persistence.Database;
import de.nitschmann.tefdnn.presentation.gui.TestingView;
import de.nitschmann.tefdnn.presentation.json.JsonParser;
import de.nitschmann.tefdnn.presentation.json.JsonValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class Console {

    private NeuralNetwork neuralNetwork;
    private NeuralNetwork trainedNeuralNetwork;
    private Database database;
    private Saver saver;
    private Loader loader;
    private Cleaner cleaner;
    private Configurator configurator;
    private ImageLoader imageLoader;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Initializes a new Console - therefore, a database connection is required as well as
     * saver, loader and configurator objects.
     * @param database database
     */
    public Console(Database database) {
        this.database = database;
        this.saver = new Saver();
        this.loader = new Loader();
        this.configurator = new Configurator();
        this.cleaner = new Cleaner();
    }

    /**
     * This method gets called in a loop and reads the current line
     * @return false, if the user types :q to exit the cli, true otherwise
     */
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
     * returns a neural network by the input string
     * @param input input
     * @return initialized neural network
     */
    public NeuralNetwork init(String input) {
        if (input.contains("-json:")) {
            return initJson(input);
        }
        neuralNetwork = loader.initNeuralNetwork(database, input);
        trainedNeuralNetwork = neuralNetwork;
        if (neuralNetwork != null) {
            imageLoader = new ImageLoader(neuralNetwork);
            System.out.println("Neural network initialized.");
        }
        return neuralNetwork;
    }

    /**
     * Deletes a neural network
     * @param input input string where user specifies name (-nff: name)
     * @return true if removal was successful
     */
    public boolean delete(String input) {
        return input.contains("-nff:") && this.cleaner.deleteNeuralNetwork(database, input);
    }

    private NeuralNetwork initJson(String input) {
        String filename = Parser.parseString(input, "-json:");
        filename = filename.replace("\"", "");
        JsonParser jsonParser = new JsonParser();
        if (!JsonValidator.isValid(filename)) {
            System.out.println("JSON is not valid!");
            return null;
        }
        try {
            jsonParser.parse(filename);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }

        neuralNetwork = loader.initNeuralNetwork(database, jsonParser.getLoadString());

        if (neuralNetwork != null) {
            imageLoader = new ImageLoader(neuralNetwork);
            System.out.println("Neural network initialized.");
        }

        conf(jsonParser.getConfigString(), neuralNetwork);
        for(String trainString : jsonParser.getTrainStrings()) {
            train(trainString, neuralNetwork);
        }
        trainedNeuralNetwork = neuralNetwork;

        if (jsonParser.getStartTrainingImmediately()) {
            if (jsonParser.getOpenGuiAfterTrainingIsCompleted()) {
                trainedNeuralNetwork = train("train -s", neuralNetwork);
                test("-gui", trainedNeuralNetwork);
                return trainedNeuralNetwork;
            } else {
                return train("train -s", trainedNeuralNetwork);
            }
        }

        return trainedNeuralNetwork;
    }

    /**
     * saves the neural network by the input string
     * @param input input
     * @return true if the storing was successful
     */
    public boolean save(String input, NeuralNetwork neuralNetwork) {
        if (saver.saveNeuralNetwork(database, input, neuralNetwork)) {
            System.out.println("Neural network saved.");
            return true;
        }
        return false;
    }

    /**
     * trains the neural network and returns the trained one
     * @param input input
     * @param neuralNetwork neuralNetwork
     * @return The trained neural network
     */
    public NeuralNetwork train(String input, NeuralNetwork neuralNetwork) {
        if (input.contains("-ptd:") && input.contains("-tn:") && input.contains("-n:")) {
            String path = Parser.parseString(input, "-ptd:");
            path = path.replace("\"", "");
            int neuron = Parser.parseInt(input, "-tn:");

            String name = Parser.parseString(input, "-n:");
            name = name.substring(0, 1).toUpperCase() + name.substring(1);

            if (neuron == -1 || neuron >= neuralNetwork.getOutputLayer().getNeurons().size()) {
                System.out.println("stop");
            }

            neuralNetwork.getOutputLayer().getNeurons().get(neuron).setName(name);

            imageLoader.addTrainingSet(path, neuron);

            TrainingData trainingData = imageLoader.getTrainingData();

            neuralNetwork.setTrainSet(trainingData.getImages());
            neuralNetwork.setEstimatedResults(trainingData.getEstimatedResults());
            neuralNetwork.setMeanImage(trainingData.getMeanImage());

            return null;
        } else if (input.contains("-s")) {
            if (!configurator.isProperlyConfigured(neuralNetwork)) {
                System.out.println("Neural network is not configured properly.");
                return null;
            }
            System.out.println("Training...");
            NeuralNetwork trainedNeuralNetwork = neuralNetwork.train(neuralNetwork);
            this.trainedNeuralNetwork = trainedNeuralNetwork;
            // We might want to show directly the gui. Unfortunately, since this application gets tested with travis-ci which is not
            // able to show guis, we comment it out.
            /*
            System.out.println("Training done");
            TestingView view = new TestingView(trainedEnvironment);
            view.setVisible(true);
            */
            return trainedNeuralNetwork;
        } else {
            System.out.println("parameters not specified correctly. Train takes following arguments:");
            System.out.println("-pTD: path to directory which contains training data, required");
            System.out.println("-tN: target output neuron which represents the trained class, required");
            System.out.println("-n: Name of the class, required");
            System.out.println("-s: start training");
            return null;
        }
    }

    /**
     * saves the results of the specified neural network to the database
     * @param neuralNetwork neuralNetwork
     * @param pathToImage path to the image
     * @param result result as a string
     */
    public void saveResult(NeuralNetwork neuralNetwork, String pathToImage, String result) {
        if (saver.saveResult(database, neuralNetwork, pathToImage, result)) {
            System.out.println("Result saved in database.");
        }
    }

    /**
     * tests a neural network by the input string and saves the results to the database
     * @param input input
     * @param trainedNeuralNetwork the neural network that was trained via train method
     */
    public boolean test(String input, NeuralNetwork trainedNeuralNetwork) {
        if (input.contains("-ptd:")) {
            String path = Parser.parseString(input, "-ptd:");
            path = path.replace("\"", "");

            if (!imageLoader.addTestPath(path)) {
                return false;
            }
            Map<String, double[]> testData = imageLoader.getTestImages(imageLoader.getTrainingData().getMeanImage());

            for (Object o : testData.entrySet()) {
                Map.Entry pair = (Map.Entry) o;
                trainedNeuralNetwork.setInput(trainedNeuralNetwork, (double[]) pair.getValue());
                List<Double> result = trainedNeuralNetwork.test(trainedNeuralNetwork);
                System.out.println(pair.getKey());
                for (int i = 0; i < trainedNeuralNetwork.getOutputLayer().getNeurons().size(); i++) {
                    Neuron n = trainedNeuralNetwork.getOutputLayer().getNeurons().get(i);
                    System.out.println(n.getName() + ":\t" + result.get(i));
                }
                //System.out.println(result);
                saveResult(trainedNeuralNetwork, (String) pair.getKey(), result.toString());
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
            double[] testData = imageLoader.getTestImage(trainedNeuralNetwork.getMeanImage());

            trainedNeuralNetwork.setInput(trainedNeuralNetwork, testData);
            List<Double> result = trainedNeuralNetwork.test(trainedNeuralNetwork);
            System.out.println(path);
            for (int i = 0; i < trainedNeuralNetwork.getOutputLayer().getNeurons().size(); i++) {
                Neuron n = trainedNeuralNetwork.getOutputLayer().getNeurons().get(i);
                System.out.println(n.getName() + ":\t" + result.get(i));
            }
            return true;
        } else if (input.contains("-gui")) {
            TestingView view = new TestingView(trainedNeuralNetwork);
            view.setVisible(true);
            view.toFront();
            view.repaint();
            return true;
        } else {
            System.out.println("parameters not specified correctly. Test takes following arguments:");
            System.out.println("-pTD: path to image directory which contains testing data");
            System.out.println("-pS: path to single image");
            System.out.println("-gui (for opening a small graphical user interface for testing)");
            System.out.println("Either -pTD, -pS or -gui needs to be specified");
            return false;
        }
    }

    /**
     * configures the neural network by the input string
     * @param input input
     * @param neuralNetwork neural network
     */
    public void conf(String input, NeuralNetwork neuralNetwork) {
        if (configurator.configureNeuralNetwork(input, neuralNetwork)) {
            System.out.println("Neural network configured.");
        }
    }

    /**
     * parses the user input and calls the required methods
     * @param input input
     */
    public void parse(String input) {
        input = input.trim().toLowerCase();
        if (input.startsWith("init")) {
            init(input);
        } else if (input.startsWith("save")) {
            if (neuralNetwork == null) {
                System.out.println("Neural network is null. You gotta initialize it first.");
                return;
            }
            save(input, neuralNetwork);
        } else if (input.startsWith("train")) {
            if (neuralNetwork == null) {
                System.out.println("Neural network is null. You gotta initialize it first.");
                return;
            }
            train(input, neuralNetwork);
        } else if (input.startsWith("test")) {
            if (neuralNetwork == null) {
                System.out.println("Neural network is null. You gotta initialize it first.");
                return;
            }
            test(input, neuralNetwork);
        } else if (input.startsWith("conf")) {
            if (neuralNetwork == null) {
                System.out.println("Neural network is null. You gotta initialize it first.");
                return;
            }
            conf(input, neuralNetwork);
        } else {
            System.out.println("Command was not found");
        }
    }

}
