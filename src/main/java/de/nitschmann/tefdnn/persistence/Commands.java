package de.nitschmann.tefdnn.persistence;

import de.nitschmann.tefdnn.application.Layer;
import de.nitschmann.tefdnn.application.NeuralNetwork;
import de.nitschmann.tefdnn.application.training.ActivationFunctionType;
import de.nitschmann.tefdnn.application.training.TrainingType;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Commands {

    public static void dropTables(Connection con) {
        String dropString = "DROP TABLE IF EXISTS NeuralNetwork; DROP TABLE IF EXISTS Layer; DROP TABLE IF EXISTS Neuron; " +
                "DROP TABLE IF EXISTS Weight; DROP TABLE IF EXISTS TEST";
        try {
            Statement stmt = con.createStatement();

            stmt.executeUpdate(dropString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes all needed tables in the database if they do not exist
     * @param con connection
     */
    public static void initializeTables(Connection con) {
        try {
            Statement stmt = con.createStatement();

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
                    "meanImage DOUBLE, " +
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
                    "name VARCHAR(100), " +
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
                    "neuralNetworkId INT, " +
                    "testPath VARCHAR(255), " +
                    "result VARCHAR(255), " +
                    "PRIMARY KEY (id))"
            );

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * Gets all neural networks
     */
    public static List<NeuralNetworkInformation> getNeuralNetworks(Connection con) {
        try {
            ArrayList<NeuralNetworkInformation> list = new ArrayList<>();
            PreparedStatement pStmt = con.prepareStatement("SELECT id, name, learningRate, activationFunction, momentum, targetLoss, trainingType, maxEpoch FROM NeuralNetwork");
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double learningRate = rs.getDouble("learningRate");
                int activationFunction = rs.getInt("activationFunction");
                double momentum = rs.getDouble("momentum");
                double targetLoss = rs.getDouble("targetLoss");
                int trainingType = rs.getInt("trainingType");
                int maxEpoch = rs.getInt("maxEpoch");

                list.add(new NeuralNetworkInformation(id, name, learningRate, momentum, targetLoss, maxEpoch, activationFunction, trainingType));
            }

            for(NeuralNetworkInformation nn : list) {
                pStmt = con.prepareStatement("SELECT count(id) as countHiddenLayers FROM Layer WHERE neuralNetworkId = ?");
                pStmt.setInt(1, nn.getNeuralNetworkId());
                rs = pStmt.executeQuery();
                while (rs.next()) {
                    nn.setNumberOfHiddenLayers(rs.getInt("countHiddenLayers") - 2);
                }

                pStmt = con.prepareStatement("SELECT id FROM Layer WHERE neuralNetworkId = ? ORDER BY position ASC");
                pStmt.setInt(1, nn.getNeuralNetworkId());
                rs = pStmt.executeQuery();
                int i = 0;
                while (rs.next()) {
                    if (i == 0) {
                        nn.setInputLayerId(rs.getInt("id"));
                    } else if (i == nn.getNumberOfHiddenLayers() + 1) {
                        nn.setOutputLayerId(rs.getInt("id"));
                    } else {
                        nn.setIdOfOneHiddenLayer(rs.getInt("id"));
                    }
                    i++;
                }

                pStmt = con.prepareStatement("SELECT count(id) as countInputNeurons FROM Neuron WHERE layerId = ?");
                pStmt.setInt(1, nn.getInputLayerId());
                rs = pStmt.executeQuery();
                while (rs.next()) {
                    nn.setNumberOfInputNeurons(rs.getInt("countInputNeurons"));
                }

                pStmt = con.prepareStatement("SELECT count(id) as countHiddenNeurons FROM Neuron WHERE layerId = ?");
                pStmt.setInt(1, nn.getIdOfOneHiddenLayer());
                rs = pStmt.executeQuery();
                while (rs.next()) {
                    nn.setNumberOfHiddenNeurons(rs.getInt("countHiddenNeurons"));
                }

                pStmt = con.prepareStatement("SELECT count(id) as countOutputNeurons FROM Neuron WHERE layerId = ?");
                pStmt.setInt(1, nn.getOutputLayerId());
                rs = pStmt.executeQuery();
                while (rs.next()) {
                    nn.setNumberOfOutputNeurons(rs.getInt("countOutputNeurons"));
                }
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * saves the results of a trained network to the database
     * @param con
     * @param neuralNetwork
     * @param pathToImage
     * @param result
     * @return
     */
    public static boolean saveResults(Connection con, NeuralNetwork neuralNetwork, String pathToImage, String result) {
        try {
            int neuralNetworkId;
            neuralNetworkId = getNeuralNetworkId(con, neuralNetwork.getName());

            if (neuralNetworkId == -1) {
                System.out.println("Couldnt save results because neural network isn't saved in the database.");
                return false;
            }

            PreparedStatement pStmt = con.prepareStatement("INSERT INTO Test (neuralNetworkId, testPath, result) VALUES (?, ?, ?, ?, ?)");
            pStmt.setInt(1, neuralNetworkId);
            pStmt.setString(2, pathToImage);
            pStmt.setString(3, result);

            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * saves the neural network and returns the id which was given by the database
     * @param con           database connection
     * @param neuralNetwork specified network
     * @return id of the network in the neuralnetwork table
     */
    public static boolean saveNeuralNetwork(Connection con, NeuralNetwork neuralNetwork) {
        try {
            PreparedStatement pStmt;
            ResultSet rs;
            pStmt = con.prepareStatement("INSERT INTO NeuralNetwork (name, learningRate, activationFunction, momentum, targetLoss, maxEpoch, trainingType, meanImage) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
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
            pStmt.setDouble(8, neuralNetwork.getMeanImage());

            pStmt.executeUpdate();

            rs = pStmt.getGeneratedKeys();
            int ffId = -1;
            if (rs.next()) {
                ffId = rs.getInt(1);
            }
            if (ffId == -1) {
                System.out.println("Network could be saved but not the corresponding layers... The identity of the network could not be determined.");
                return false;
            }

            saveInputLayer(con, ffId);
            int lastHiddenLayerIndexFF = saveHiddenLayers(con, neuralNetwork, ffId);
            saveOutputLayer(con, ffId, lastHiddenLayerIndexFF);
            saveNeurons(con, ffId, neuralNetwork);

            return true;
        } catch (SQLIntegrityConstraintViolationException x) {
            System.out.println("There's already a neural network with the specified name in the database. Please choose another one.");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
                PreparedStatement pStmt = con.prepareStatement("INSERT INTO Neuron (layerId, position, name) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                pStmt.setInt(1, layerId);
                pStmt.setInt(2, j);
                if (layer.getNeurons().get(j).getName() == null) {
                    pStmt.setString(3, "no name specified");
                } else {
                    pStmt.setString(3, layer.getNeurons().get(j).getName());
                }
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
     * @param neuralNetworkName
     * @return
     */
    public static NeuralNetwork initNeuralNetwork(Connection con, String neuralNetworkName) {
        int neuralNetworkId = getNeuralNetworkId(con, neuralNetworkName);

        if (neuralNetworkId == -1) {
            System.out.println("Couldn't find neural network in database");
            return null;
        }

        NeuralNetwork neuralNetwork = initNeuralNetwork(con, neuralNetworkId);
        return neuralNetwork;
    }

    /**
     * Removes a neural network from the database
     * @param con
     * @param name
     * @return
     */
    public static boolean deleteNeuralNetwork(Connection con, String name) {
        try {
            PreparedStatement pStmt = con.prepareStatement("DELETE FROM NeuralNetwork WHERE name = ?");
            pStmt.setString(1, name);
            pStmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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

            pStmt = con.prepareStatement("SELECT name, learningRate, maxEpoch, momentum, targetLoss, trainingType, activationFunction, meanImage FROM NeuralNetwork WHERE id = ?");
            pStmt.setInt(1, neuralNetworkId);
            rs = pStmt.executeQuery();
            String name = "";
            int maxEpoch = 0, trainingType = 0, activationFunction = 0;
            double targetLoss = 0, momentum = 0, learningRate = 0, meanImage = 0;
            while (rs.next()) {
                name = rs.getString(1);
                learningRate = rs.getDouble(2);
                maxEpoch = rs.getInt(3);
                momentum = rs.getDouble(4);
                targetLoss = rs.getDouble(5);
                trainingType = rs.getInt(6);
                activationFunction = rs.getInt(7);
                meanImage = rs.getDouble(8);
            }

            NeuralNetwork nn = new NeuralNetwork();
            nn.setName(name);
            nn.setLearningRate(learningRate);
            nn.setMaxEpoch(maxEpoch);
            nn.setMomentum(momentum);
            nn.setTargetLoss(targetLoss);
            nn.setMeanImage(meanImage);
            if (trainingType == 1) {
                nn.setTrainingType(TrainingType.BACKPROPAGATION);
            }
            if (activationFunction == 1) {
                nn.setActivationFunction(ActivationFunctionType.RELU);
            } else if (activationFunction == 2) {
                nn.setActivationFunction(ActivationFunctionType.SIGMOID);
            }
            nn.initNetwork(outputsInputLayer, weightsHiddenLayer, weightsOutputLayer);

            List<String> classes = getClasses(con, neuralNetworkId);
            for(int j = 0; j < classes.size(); j++) {
                nn.getOutputLayer().getNeurons().get(j).setName(classes.get(j));
            }
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
            PreparedStatement pStmt = con.prepareStatement("SELECT id, name FROM Neuron WHERE layerId = ? ORDER BY position ASC");
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

    private static ArrayList<String> getClasses(Connection con, int neuralNetworkId) {
        // Fetch the names of the classes
        try {
            PreparedStatement pStmt = con.prepareStatement("SELECT id FROM Layer WHERE neuralNetworkId = ? ORDER BY POSITION DESC");
            pStmt.setInt(1, neuralNetworkId);
            ResultSet rs = pStmt.executeQuery();
            int outputLayerId = -1;
            while (rs.next()) {
                outputLayerId = rs.getInt("id");
                break;
            }
            // Names can not be fetched
            if (outputLayerId == -1) {
                return null;
            }

            pStmt = con.prepareStatement("SELECT name FROM Neuron WHERE layerId = ? ORDER BY POSITION ASC");
            pStmt.setInt(1, outputLayerId);
            rs = pStmt.executeQuery();

            ArrayList<String> list = new ArrayList<>();
            while(rs.next()) {
                list.add(rs.getString("name"));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
