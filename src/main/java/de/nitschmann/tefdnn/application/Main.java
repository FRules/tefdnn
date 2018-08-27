package de.nitschmann.tefdnn.application;

import de.nitschmann.tefdnn.persistence.Database;
import de.nitschmann.tefdnn.presentation.Console;
import de.nitschmann.tefdnn.presentation.Parser;
import de.nitschmann.tefdnn.presentation.gui.StartView;
import de.nitschmann.tefdnn.presentation.gui.TestingView;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        String str = argsToString(args).toLowerCase();
        if (!str.contains("-initdb:")) {
            printHelp();
            return;
        }

        Parser parser = new Parser();
        String initDbString = parser.parseString(str, "-initdb:");
        boolean initializeDatabase;
        try {
            initializeDatabase = Boolean.parseBoolean(initDbString.replace(":", ""));
        } catch(Exception e) {
            System.out.println("Couldn't parse initDb parameter to boolean. Make sure to specify true or false");
            printHelp();
            return;
        }

        Database db = new Database("jdbc:hsqldb:file:db/database;shutdown=true;", "SA", "", initializeDatabase);

        if (str.contains("-gui")) {
            /* Gui Mode */
            StartView v = new StartView(db);
            v.setVisible(true);
            return;
        }

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
    private static void printHelp() {
        System.out.println("You have to provide arguments to the application. Following switches are possible:");
        System.out.println("  -gui\t\t\t\tStarts the application in GUI mode, optional");
        System.out.println("  -initDb: true | false\t\tTrue, if database should be initialized new, required");
    }

    private static String argsToString(String[] args) {
        StringBuilder builder = new StringBuilder();
        for(String s : args) {
            builder.append(s);
        }
        return builder.toString();
    }
}
