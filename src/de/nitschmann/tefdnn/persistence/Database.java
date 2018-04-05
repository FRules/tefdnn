package de.nitschmann.tefdnn.persistence;

import de.nitschmann.tefdnn.application.TrainingEnvironment;

import java.sql.*;

public class Database {

    private Connection con = null;

    /**
     *  initializes a new database connection
     */
    public Database(String connectionString, String user, String password) {
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
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
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
        String dropString = "DROP TABLE IF EXISTS TrainingEnvironment; DROP TABLE IF EXISTS NeuralNetwork; DROP TABLE IF EXISTS Layer; DROP TABLE IF EXISTS Neuron; " +
                "DROP TABLE IF EXISTS Weight; DROP TABLE IF EXISTS TEST";
        try {
            Statement stmt = con.createStatement();

            stmt.executeUpdate(dropString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearDatabase() {
        String deleteString = "DELETE FROM TrainingEnvironment; DELETE FROM NeuralNetwork; DELETE FROM Layer; DELETE FROM Neuron; DELETE FROM Weight;";
        try {
            Statement stmt = con.createStatement();

            stmt.executeUpdate(deleteString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean saveResult(TrainingEnvironment trainingEnvironment, String pathToImage, String result) {
        return Commands.saveResults(con, trainingEnvironment, pathToImage, result);
    }

    public boolean save(TrainingEnvironment trainingEnvironment) {
        return Commands.saveTrainingEnvironment(con, trainingEnvironment);
    }

    public boolean saveJustFeedforward(TrainingEnvironment trainingEnvironment) {
        return Commands.saveTrainingEnvironmentJustFeedforward(con, trainingEnvironment);
    }

    public TrainingEnvironment initTrainingEnvironment(String systemName) {
        return Commands.initTrainingEnvironment(con, systemName);
    }

    public TrainingEnvironment initTrainingEnvironmentJustFeedforward(String feedforwardName) {
        return Commands.initTrainingEnvironmentJustFeedforward(con, feedforwardName);
    }

    public TrainingEnvironment initTrainingEnvironment(String feedForwardName, String autoEncoderName) {
        return Commands.initTrainingEnvironment(con, feedForwardName, autoEncoderName);
    }
}
