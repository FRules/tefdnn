package de.nitschmann.tefdnn.application;

import de.nitschmann.tefdnn.persistence.Database;
import de.nitschmann.tefdnn.presentation.Console;

public class Main {

    public static void main(String[] args) {
        boolean initializeDatabase;
        if (args.length != 1) {
            System.out.println("You have to provide if the database should be re-initialized. True = Initialize database new, anything else use existing database if it exists");
            return;
        }

        initializeDatabase = args[0].equals("true");

        Database db = new Database("jdbc:hsqldb:file:db/database;shutdown=true;", "SA", "", initializeDatabase);
        Console console = new Console(db);

        System.out.println("Console initialized.");

        while (true) {
            if (console.read() == false) {
                System.out.println("Application quit.");
                return;
            }
        }
    }
}
