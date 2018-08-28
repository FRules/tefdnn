package de.nitschmann.tefdnn.presentation;

import de.nitschmann.tefdnn.persistence.Database;

import static de.nitschmann.tefdnn.presentation.Parser.parseString;

public class Cleaner {

    public boolean deleteNeuralNetwork(Database database, String input) {
        String name = parseString(input, "-nff:");
        return database.deleteNeuralNetwork(name);
    }

}
