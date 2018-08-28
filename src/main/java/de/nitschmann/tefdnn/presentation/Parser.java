package de.nitschmann.tefdnn.presentation;

public class Parser {

    /**
     * parses the input string and returns the searched parameter as a string
     * @param input input
     * @param parameter parameter
     * @return integer which was parsed or -1 if it couldn't be parsed
     */
    public static int parseInt(String input, String parameter) {
        int pos = input.indexOf(parameter) + parameter.length();
        int pos2 = input.indexOf('-', pos);
        String value;
        if (pos2 != -1) {
            value = input.substring(pos, pos2).trim();
        } else {
            value = input.substring(pos).trim();
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException n) {
            System.out.printf("Couldn't convert the value of parameter %s. Be sure to specify only integers. Given value: %s\n", parameter, value);
        }
        return -1;
    }

    /**
     * parses the input string and returns the searched parameter as a double
     * @param input  input
     * @param parameter parameter
     * @return double which was parsed or -1 if it couldn't be parsed
     */
    public static double parseDouble(String input, String parameter) {
        int pos = input.indexOf(parameter) + parameter.length();
        int pos2 = input.indexOf('-', pos);
        String value;
        if (pos2 != -1) {
            value = input.substring(pos, pos2).trim();
        } else {
            value = input.substring(pos).trim();
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException n) {
            System.out.printf("Couldn't convert the value of parameter %s. Be sure to specify only doubles. Given value: %s\n", parameter, value);
        }
        return -1;
    }

    /**
     * parses the input string and returns the searched parameter as a string
     * @param input input
     * @param parameter parameter
     * @return string which was parsed
     */
    public static String parseString(String input, String parameter) {
        int pos = input.indexOf(parameter) + parameter.length();
        int pos2 = input.indexOf('-', pos);
        String value;
        if (pos2 != -1) {
            value = input.substring(pos, pos2).trim();
        } else {
            value = input.substring(pos).trim();
        }
        return value;
    }
}
