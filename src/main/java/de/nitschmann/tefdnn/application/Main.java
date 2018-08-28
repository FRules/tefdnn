package de.nitschmann.tefdnn.application;

import de.nitschmann.tefdnn.persistence.Database;
import de.nitschmann.tefdnn.presentation.Console;
import de.nitschmann.tefdnn.presentation.Parser;
import de.nitschmann.tefdnn.presentation.gui.StartView;

public class Main {

    public static void main(String[] args) {
        String str = argsToString(args).toLowerCase();
        Parser parser = new Parser();

        boolean initializeDatabase = false;
        if (str.contains("-initdb:")) {
            String initDbString = parser.parseString(str, "-initdb:");
            try {
                initializeDatabase = Boolean.parseBoolean(initDbString.replace(":", ""));
            } catch(Exception e) {
                System.out.println("Couldn't parse '-initDb:' parameter to boolean. Using previous database.");
            }
        } else {
            System.out.println("Parameter '-initDb:' not existing. Using previous database.");
        }

        Database db = new Database("jdbc:hsqldb:file:db/database;shutdown=true;", "SA", "", initializeDatabase);

        if (!str.contains("-cli")) {
            /* Gui Mode */
            System.out.println("Starting application in GUI mode");
            StartView v = new StartView(db);
            v.setVisible(true);
            return;
        }
        System.out.println("Starting application in CLI mode");

        /* Console mode */
        Console console = new Console(db);
        System.out.println("Console initialized.");

        while (true) {
            if (console.read() == false) {
                System.out.println("Application quit.");
                return;
            }
        }

    }

    private static String argsToString(String[] args) {
        StringBuilder builder = new StringBuilder();
        for(String s : args) {
            builder.append(s);
        }
        return builder.toString();
    }
}
