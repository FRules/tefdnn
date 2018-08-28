package de.nitschmann.tefdnn.persistence;

import de.nitschmann.tefdnn.application.NeuralNetwork;

import java.sql.*;
import java.util.List;

public class Database {

    private Connection con = null;

    public Database(String connectionString, String user, String password, boolean overwrite) {
        try
        {
            Class.forName( "org.hsqldb.jdbcDriver" );
        }
        catch ( ClassNotFoundException e )
        {
            System.err.println( "Couldn't find driver class" );
            return;
        }

        try
        {
            con = DriverManager.getConnection(
                    connectionString, user, password);
            if (overwrite) {
                dropTables();
            }
            // Since we have "CREATE TABLE IF NOT EXISTS" commands here, it
            // is good to init them here if they do not exist.
            initDatabase();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
    }

    /**
     *  initializes a new database connection
     */
    public Database(String connectionString, String user, String password) {
        this(connectionString, user, password, false);
    }

    public void close() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void initDatabase() {
        Commands.initializeTables(con);
    }

    public void dropTables() {
        Commands.dropTables(con);
    }

    public List<NeuralNetworkInformation> getNeuralNetworks() {
        return Commands.getNeuralNetworks(con);
    }

    public boolean saveResult(NeuralNetwork neuralNetwork, String pathToImage, String result) {
        return Commands.saveResults(con, neuralNetwork, pathToImage, result);
    }

    public boolean save(NeuralNetwork neuralNetwork) {
        return Commands.saveNeuralNetwork(con, neuralNetwork);
    }

    public NeuralNetwork initNeuralNetwork(String neuralNetworkName) {
        return Commands.initNeuralNetwork(con, neuralNetworkName);
    }

    public boolean deleteNeuralNetwork(String neuralNetworkName) {
        return Commands.deleteNeuralNetwork(con, neuralNetworkName);
    }
}
