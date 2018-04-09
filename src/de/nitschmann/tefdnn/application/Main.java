package de.nitschmann.tefdnn.application;

import de.nitschmann.tefdnn.persistence.Database;
import de.nitschmann.tefdnn.presentation.Console;
import de.nitschmann.tefdnn.presentation.gui.TestingView;

public class Main {

    public static void main(String[] args) {

        SensitivityAnalysis analysis = new SensitivityAnalysis();
        analysis.startAllScenarios();
        return;
/*
        Database db = new Database("jdbc:hsqldb:file:db/database;shutdown=true;", "SA", "", false);
        Console console = new Console(db);

        System.out.println("Console initialized.");

        while (true) {
            if (console.read() == false) {
                System.out.println("Application quit.");
                return;
            }
        }
        */
    }
}
