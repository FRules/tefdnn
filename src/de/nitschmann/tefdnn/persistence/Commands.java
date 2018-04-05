package de.nitschmann.tefdnn.persistence;

import de.nitschmann.tefdnn.application.Layer;
import de.nitschmann.tefdnn.application.NeuralNetwork;
import de.nitschmann.tefdnn.application.NeuralNetworkType;
import de.nitschmann.tefdnn.application.TrainingEnvironment;
import de.nitschmann.tefdnn.application.training.ActivationFunctionType;
import de.nitschmann.tefdnn.application.training.TrainingType;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Commands {

    /**
     * Initializes all needed tables in the database if they do not exist
     * @param con
     */
    public static void initializeTables(Connection con) {
        try {
            Statement stmt = con.createStatement();

            stmt.executeUpdate("" +
                    "CREATE TABLE IF NOT EXISTS TrainingEnvironment (" +
                    "id INT IDENTITY, " +
                    "name VARCHAR(50) UNIQUE, " +
                    "feedForwardId INT NOT NULL, " +
                    "autoEncoderId INT, " +
                    "PRIMARY KEY(id)); "
            );

            stmt.executeUpdate("" +
                    "CREATE TABLE IF NOT EXISTS NeuralNetwork (" +
                    "id INT IDENTITY, " +
                    "name VARCHAR(50) UNIQUE, " +
                    "learningRate DOUBLE, " +
                    "activationFunction INT, " +
                    "momentum DOUBLE, " +
                    "targetLoss DOUBLE, " +
                    "trainingType INT, " +
                    "maxEpoch INT, " +
                    "PRIMARY KEY(id))"
            );

            stmt.executeUpdate("" +
                    "CREATE TABLE IF NOT EXISTS Layer (" +
                    "id INT IDENTITY NOT NULL, " +
                    "neuralNetworkId INT NOT NULL, " +
                    "position INT NOT NULL, " +
                    "PRIMARY KEY(id))"
            );

            stmt.executeUpdate("" +
                    "CREATE TABLE IF NOT EXISTS Neuron (" +
                    "id INT IDENTITY, " +
                    "layerId INT NOT NULL, " +
                    "position INT NOT NULL, " +
                    "PRIMARY KEY(id))"
            );

            stmt.executeUpdate("" +
                    "CREATE TABLE IF NOT EXISTS Weight (" +
                    "neuronId INT NOT NULL, " +
                    "position INT NOT NULL, " +
                    "value DOUBLE NOT NULL, " +
                    "PRIMARY KEY(neuronId, position))"
            );

            stmt.executeUpdate("" +
                "CREATE TABLE iF NOT EXISTS Test (" +
                    "id INT IDENTITY NOT NULL, " +
                    "trainingEnvironmentId INT, " +
                    "feedForwardId INT, " +
                    "autoEncoderId INT, " +
                    "testPath VARCHAR(255), " +
                    "result VARCHAR(255), " +
                    "PRIMARY KEY (id))"
            );

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * saves the results of a trained network to the database
     * @param con
     * @param trainingEnvironment
     * @param pathToImage
     * @param result
     * @return
     */
    public static boolean saveResults(Connection con, TrainingEnvironment trainingEnvironment, String pathToImage, String result) {
        try {
            int ffId, aeId = -1, teId;
            ffId = getNeuralNetworkId(con, trainingEnvironment.getFeedForwardNetwork().getName());
            if (trainingEnvironment.getAutoEncoderNetwork() != null) {
                aeId = getNeuralNetworkId(con, trainingEnvironment.getAutoEncoderNetwork().getName());
            }
            teId = getTrainingEnvironmentId(con, trainingEnvironment.getName());

            if (ffId == -1 && teId == -1) {
                System.out.println("Couldnt save results because training environment isn't saved in the database.");
                return false;
            }

            PreparedStatement pStmt = con.prepareStatement("INSERT INTO Test (trainingEnvironmentId, feedForwardId, autoEncoderId, testPath, result) VALUES (?, ?, ?, ?, ?)");
            pStmt.setInt(1, teId);
            pStmt.setInt(2, ffId);
            pStmt.setInt(3, aeId);
            pStmt.setString(4, pathToImage);
            pStmt.setString(5, result);

            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * saves just the feedfoward network to the database
     * @param con
     * @param trainingEnvironment
     * @return
     */
    public static boolean saveTrainingEnvironmentJustFeedforward(Connection con, TrainingEnvironment trainingEnvironment) {
        try {
            int ffId = saveNeuralNetwork(con, trainingEnvironment.getFeedForwardNetwork());
            if (ffId == -1) {
                System.out.println("Feedforward network couldn't be saved");
                return false;
            }

            saveInputLayer(con, ffId);
            int lastHiddenLayerIndexFF = saveHiddenLayers(con, trainingEnvironment.getFeedForwardNetwork(), ffId);
            saveOutputLayer(con, ffId, lastHiddenLayerIndexFF);
            saveNeurons(con, ffId, trainingEnvironment.getFeedForwardNetwork());

            PreparedStatement pStmt = con.prepareStatement("INSERT INTO TrainingEnvironment (name, feedForwardId) VALUES (?, ?)");
            pStmt.setString(1, trainingEnvironment.getName());
            pStmt.setInt(2, ffId);
            pStmt.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException x) {
            System.out.println("There's already a training environment with the specified name in the database. Please choose another one.");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * saves the training environment to the database
     * @param con
     * @param trainingEnvironment
     * @return
     */
    public static boolean saveTrainingEnvironment(Connection con, TrainingEnvironment trainingEnvironment) {
        try {
            int ffId = saveNeuralNetwork(con, trainingEnvironment.getFeedForwardNetwork());
            if (ffId == -1) {
                System.out.println("Feedforward network couldn't be saved");
                return false;
            }

            saveInputLayer(con, ffId);
            int lastHiddenLayerIndexFF = saveHiddenLayers(con, trainingEnvironment.getFeedForwardNetwork(), ffId);
            saveOutputLayer(con, ffId, lastHiddenLayerIndexFF);
            saveNeurons(con, ffId, trainingEnvironment.getFeedForwardNetwork());

            int aeId = saveNeuralNetwork(con, trainingEnvironment.getAutoEncoderNetwork());
            if (aeId == -1) {
                System.out.println("Autoencoder couldn't be saved");
                return false;
            }

            saveInputLayer(con, aeId);
            int lastHiddenLayerIndexAE = saveHiddenLayers(con, trainingEnvironment.getAutoEncoderNetwork(), aeId);
            saveOutputLayer(con, aeId, lastHiddenLayerIndexAE);
            saveNeurons(con, aeId, trainingEnvironment.getAutoEncoderNetwork());

            PreparedStatement pStmt = con.prepareStatement("INSERT INTO TrainingEnvironment (name, feedForwardId, autoEncoderId) VALUES (?, ?, ?)");
            pStmt.setString(1, trainingEnvironment.getName());
            pStmt.setInt(2, ffId);
            pStmt.setInt(3, aeId);
            pStmt.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException x) {
            System.out.println("There's already a training environment with the specified name in the database. Please choose another one.");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * saves the neural network and returns the id which was given by the database
     * @param con           database connection
     * @param neuralNetwork specified network (feedforward or autoencoder)
     * @return id of the network in the neuralnetwork table
     */
    private static int saveNeuralNetwork(Connection con, NeuralNetwork neuralNetwork) {
        try {
            PreparedStatement pStmt;
            ResultSet rs;
            pStmt = con.prepareStatement("INSERT INTO NeuralNetwork (name, learningRate, activationFunction, momentum, targetLoss, maxEpoch, trainingType) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pStmt.setString(1, neuralNetwork.getName());
            pStmt.setDouble(2, neuralNetwork.getLearningRate());
            if (neuralNetwork.getActivationFunctionType() == null) {
                pStmt.setObject(3, null);
            } else {
                switch (neuralNetwork.getActivationFunctionType()) {
                    case RELU:
                        pStmt.setInt(3, 1);
                        break;
                    case SIGMOID:
                        pStmt.setInt(3, 2);
                        break;
                }
            }

            pStmt.setDouble(4, neuralNetwork.getMomentum());
            pStmt.setDouble(5, neuralNetwork.getTargetLoss());
            pStmt.setInt(6, neuralNetwork.getMaxEpoch());
            if (neuralNetwork.getTrainingType() == null) {
                pStmt.setObject(7, null);
            } else {
                switch (neuralNetwork.getTrainingType()) {
                    case BACKPROPAGATION:
                        pStmt.setInt(7, 1);
                        break;
                        /* right now, only backpropagation is supported */
                    default:
                        pStmt.setInt(7, 1);
                }
            }

            pStmt.executeUpdate();

            rs = pStmt.getGeneratedKeys();
            int ffId = -1;
            if (rs.next()) {
                ffId = rs.getInt(1);
            }
            return ffId;
        } catch (SQLIntegrityConstraintViolationException x) {
            System.out.println("There's already a neural network with the specified name in the database. Please choose another one.");
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Saves the input layer of the specified neural network id
     * @return
     */
    private static void saveInputLayer(Connection con, int neuralNetworkId) {
        try {
            PreparedStatement pStmt = con.prepareStatement("INSERT INTO Layer (neuralNetworkId, position) VALUES (?, ?)");
            pStmt.setInt(1, neuralNetworkId);
            pStmt.setInt(2, 0);
            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the hidden layers of the specified neural network.
     * @param con
     * @param neuralNetwork
     * @param neuralNetworkId
     * @return Returns the last index
     */
    private static int saveHiddenLayers(Connection con, NeuralNetwork neuralNetwork, int neuralNetworkId) {
        try {
            PreparedStatement pStmt;
            int i = 1;
            for (; i <= neuralNetwork.getHiddenLayers().size(); i++) {
                pStmt = con.prepareStatement("INSERT INTO Layer (neuralNetworkId, position) VALUES (?, ?)");
                pStmt.setInt(1, neuralNetworkId);
                pStmt.setInt(2, i);
                pStmt.executeUpdate();
            }
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Saves the output layer of the specified network
     * @param con
     * @param neuralNetworkId
     * @param lastIndexHiddenLayer
     */
    private static void saveOutputLayer(Connection con, int neuralNetworkId, int lastIndexHiddenLayer) {
        try {
            PreparedStatement pStmt = con.prepareStatement("INSERT INTO Layer (neuralNetworkId, position) VALUES (?, ?)");
            pStmt.setInt(1, neuralNetworkId);
            pStmt.setInt(2, lastIndexHiddenLayer);
            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * saves the neurons to the database by specifying the layer and layer type
     * @param con
     * @param layer
     * @param layerId
     * @param layerType
     */
    private static void saveNeurons(Connection con, Layer layer, int layerId, String layerType) {
        try {
            for (int j = 0; j < layer.getNeurons().size(); j++) {
                if (j == 0 && layerType != "output") {
                    continue;
                } // Skipping BIAS
                PreparedStatement pStmt = con.prepareStatement("INSERT INTO Neuron (layerId, position) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                pStmt.setInt(1, layerId);
                pStmt.setInt(2, j);
                pStmt.executeUpdate();
                ResultSet rs = pStmt.getGeneratedKeys();
                int neuronId = -1;
                if (rs.next()) {
                    neuronId = rs.getInt(1);
                }

                if (layerType == "hidden" || layerType == "output") {
                    if (j == 0 && layerType == "hidden") {
                        continue;
                    }
                    for (int p = 0; p < layer.getNeurons().get(j).getWeightIn().size(); p++) {
                        pStmt = con.prepareStatement("INSERT INTO Weight (neuronId, position, value) VALUES (?, ?, ?)");
                        pStmt.setInt(1, neuronId);
                        pStmt.setInt(2, p);
                        pStmt.setDouble(3, layer.getNeurons().get(j).getWeightIn().get(p));
                        pStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the neurons of the specified network by specifying the neural network id
     * @param con
     * @param neuralNetwork
     */
    private static void saveNeurons(Connection con, int neuralNetworkId, NeuralNetwork neuralNetwork) {
        try {
            int numberOfLayers = getNumberOfLayers(con, neuralNetworkId);

            PreparedStatement pStmt = con.prepareStatement("SELECT id FROM Layer WHERE neuralNetworkId = ? ORDER BY position ASC");
            pStmt.setInt(1, neuralNetworkId);
            ResultSet rs = pStmt.executeQuery();

            int layerIteration = -1;
            int hiddenLayerIteration = 0;
            while (rs.next()) {

                layerIteration++;
                if (layerIteration == 0) {
                    saveNeurons(con, neuralNetwork.getInputLayer(), rs.getInt(1), "input");
                    continue;
                }

                if (layerIteration == numberOfLayers - 1) {
                    saveNeurons(con, neuralNetwork.getOutputLayer(), rs.getInt(1), "output");
                    continue;
                }

                saveNeurons(con, neuralNetwork.getHiddenLayers().get(hiddenLayerIteration), rs.getInt(1), "hidden");
                hiddenLayerIteration++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * loads just a feedforward network from the database
     * @param con
     * @param feedforwardName
     * @return
     */
    public static TrainingEnvironment initTrainingEnvironmentJustFeedforward(Connection con, String feedforwardName) {
        int feedForwardId = getNeuralNetworkId(con, feedforwardName);

        if (feedForwardId == -1) {
            System.out.println("Couldn't find neural network in database");
            return null;
        }

        NeuralNetwork feedForwardNetwork = initNeuralNetwork(con, feedForwardId);
        feedForwardNetwork.setNeuralNetworkType(NeuralNetworkType.FEEDFORWARD);
        return new TrainingEnvironment(feedForwardNetwork);
    }

    /**
     * loads a training environment by the environment name from the database
     * @param con
     * @param systemName
     * @return
     */
    public static TrainingEnvironment initTrainingEnvironment(Connection con, String systemName) {
        try {
            PreparedStatement pStmt = con.prepareStatement("SELECT feedForwardId, autoEncoderId FROM TrainingEnvironment WHERE name = ?");
            pStmt.setString(1, systemName);
            ResultSet rs = pStmt.executeQuery();
            int feedForwardId = -1;
            int autoEncoderId = -1;
            while (rs.next()) {
                feedForwardId = rs.getInt(1);
                autoEncoderId = rs.getInt(2);
            }

            if (feedForwardId == -1 || autoEncoderId == -1) {
                System.out.println("Couldn't find neural network in database");
                return null;
            }

            NeuralNetwork feedForwardNetwork = initNeuralNetwork(con, feedForwardId);
            feedForwardNetwork.setNeuralNetworkType(NeuralNetworkType.FEEDFORWARD);
            NeuralNetwork autoEncoderNetwork = initNeuralNetwork(con, autoEncoderId);
            autoEncoderNetwork.setNeuralNetworkType(NeuralNetworkType.AUTOENCODER);
            return new TrainingEnvironment(systemName, feedForwardNetwork, autoEncoderNetwork);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * loads a training environment by the feedforward and autoencoder name from the database
     * @param con
     * @param feedForwardName
     * @param autoEncoderName
     * @return
     */
    public static TrainingEnvironment initTrainingEnvironment(Connection con, String feedForwardName, String autoEncoderName) {
        int feedForwardId = getNeuralNetworkId(con, feedForwardName);
        int autoEncoderId = getNeuralNetworkId(con, autoEncoderName);

        if (feedForwardId == -1 || autoEncoderId == -1) {
            System.out.println("Couldn't find neural network in database");
            return null;
        }

        NeuralNetwork feedForwardNetwork = initNeuralNetwork(con, feedForwardId);
        feedForwardNetwork.setNeuralNetworkType(NeuralNetworkType.FEEDFORWARD);
        NeuralNetwork autoEncoderNetwork = initNeuralNetwork(con, autoEncoderId);
        autoEncoderNetwork.setNeuralNetworkType(NeuralNetworkType.AUTOENCODER);
        return new TrainingEnvironment(feedForwardNetwork, autoEncoderNetwork);
    }

    /**
     * returns the neural network id by specifying the string from the database
     * @param con
     * @param name
     * @return
     */
    private static int getNeuralNetworkId(Connection con, String name) {
        try {
            PreparedStatement pStmt = con.prepareStatement("SELECT id FROM NeuralNetwork WHERE name = ?");
            pStmt.setString(1, name);
            ResultSet rs = pStmt.executeQuery();
            int id = -1;
            while (rs.next()) {
                id = rs.getInt(1);
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * returns the training environment id by specifying the string from the database
     * @param con
     * @param name
     * @return
     */
    private static int getTrainingEnvironmentId(Connection con, String name) {
        try {
            PreparedStatement pStmt = con.prepareStatement("SELECT id FROM TrainingEnvironment WHERE name = ?");
            pStmt.setString(1, name);
            ResultSet rs = pStmt.executeQuery();
            int id = -1;
            while (rs.next()) {
                id = rs.getInt(1);
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * loads a neural network by specifying the neural network id
     * @param con
     * @param neuralNetworkId
     * @return
     */
    private static NeuralNetwork initNeuralNetwork(Connection con, int neuralNetworkId) {
        try {
            PreparedStatement pStmt = con.prepareStatement("SELECT count(neuralNetworkId) FROM Layer WHERE neuralNetworkId = ?");
            pStmt.setInt(1, neuralNetworkId);
            ResultSet rs = pStmt.executeQuery();

            int numberOfLayers = 0;
            while (rs.next()) {
                numberOfLayers = rs.getInt(1);
            }

            pStmt = con.prepareStatement("SELECT id, position FROM Layer WHERE neuralNetworkId = ? ORDER BY position ASC");
            pStmt.setInt(1, neuralNetworkId);
            rs = pStmt.executeQuery();

            List<Double> outputsInputLayer = new ArrayList<>();
            List<Map<Integer, List<Double>>> weightsHiddenLayer = new ArrayList<>();
            Map<Integer, List<Double>> weightsOutputLayer = new HashMap<>();

            int i = -1;
            while (rs.next()) {
                i++;
                if (i == 0) {
                    /* First iteration -> Input layer
                    * We only need to know the count of neurons on the input layer. The value doesn't matter */
                    for (int j = 0; j < getNumberOfNeuronsOnLayer(con, rs.getInt(1)); j++) {
                        outputsInputLayer.add(0.0);
                    }
                    continue;
                }

                if (i == numberOfLayers - 1) {
                    /* Last iteration -> Output layer */
                    weightsOutputLayer = initNeurons(con, rs.getInt(1));
                    continue;
                }

                weightsHiddenLayer.add(initNeurons(con, rs.getInt(1)));
            }

            pStmt = con.prepareStatement("SELECT name, learningRate, maxEpoch, momentum, targetLoss, trainingType, activationFunction FROM NeuralNetwork WHERE id = ?");
            pStmt.setInt(1, neuralNetworkId);
            rs = pStmt.executeQuery();
            String name = "";
            int maxEpoch = 0, trainingType = 0, activationFunction = 0;
            double targetLoss = 0, momentum = 0, learningRate = 0;
            while (rs.next()) {
                name = rs.getString(1);
                learningRate = rs.getDouble(2);
                maxEpoch = rs.getInt(3);
                momentum = rs.getDouble(4);
                targetLoss = rs.getDouble(5);
                trainingType = rs.getInt(6);
                activationFunction = rs.getInt(7);
            }

            NeuralNetwork nn = new NeuralNetwork();
            nn.setName(name);
            nn.setLearningRate(learningRate);
            nn.setMaxEpoch(maxEpoch);
            nn.setMomentum(momentum);
            nn.setTargetLoss(targetLoss);
            if (trainingType == 1) {
                nn.setTrainingType(TrainingType.BACKPROPAGATION);
            }
            if (activationFunction == 1) {
                nn.setActivationFunction(ActivationFunctionType.RELU);
            } else if (activationFunction == 2) {
                nn.setActivationFunction(ActivationFunctionType.SIGMOID);
            }
            nn.initNetwork(outputsInputLayer, weightsHiddenLayer, weightsOutputLayer);
            return nn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * returns the number of neurons on a layer
     * @param con
     * @param layerId
     * @return
     */
    private static int getNumberOfNeuronsOnLayer(Connection con, int layerId) {
        try {
            PreparedStatement pStmt = con.prepareStatement("SELECT count(*) FROM Neuron WHERE layerId = ?");
            pStmt.setInt(1, layerId);
            ResultSet rs = pStmt.executeQuery();
            int id = -1;
            while (rs.next()) {
                id = rs.getInt(1);
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * returns the number of layers of a neural network
     * @param con
     * @param neuralNetworkId
     * @return
     */
    private static int getNumberOfLayers(Connection con, int neuralNetworkId) {
        try {
            PreparedStatement pStmt = con.prepareStatement("SELECT count(*) FROM Layer WHERE neuralNetworkId = ?");
            pStmt.setInt(1, neuralNetworkId);
            ResultSet rs = pStmt.executeQuery();
            int id = -1;
            while (rs.next()) {
                id = rs.getInt(1);
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * initializes the neurons of a layer
     * @param con
     * @param layerId
     * @return
     */
    private static Map<Integer, List<Double>> initNeurons(Connection con, int layerId) {
        try {
            PreparedStatement pStmt = con.prepareStatement("SELECT id FROM Neuron WHERE layerId = ? ORDER BY position ASC");
            pStmt.setInt(1, layerId);
            ResultSet rs = pStmt.executeQuery();
            Map<Integer, List<Double>> map = new HashMap<>();
            int iteration = 0;
            while (rs.next()) {
                int id = rs.getInt(1);
                map.put(iteration, initWeights(con, id));
                iteration++;
            }
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * initializes the weights of a neuron
     * @param con
     * @param neuronId
     * @return
     */
    private static ArrayList<Double> initWeights(Connection con, int neuronId) {
        try {
            PreparedStatement pStmt = con.prepareStatement("SELECT value FROM Weight WHERE neuronId = ? ORDER BY position ASC");
            pStmt.setInt(1, neuronId);
            ResultSet rs = pStmt.executeQuery();
            ArrayList<Double> values = new ArrayList<>();
            while (rs.next()) {
                double value = rs.getDouble(1);
                values.add(value);
            }
            return values;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
