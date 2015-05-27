package common;

import java.util.stream.Stream;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class CommandLineParser {
    protected final String[] args;

    public CommandLineParser(String[] args) {
        this.args = args;
    }

    public boolean hasKey(String name) {
        return Stream.of(args).anyMatch(s -> s.equalsIgnoreCase("-" + name));
    }

    public String getString(String name) {
        String result = getString(name, null);
        if (result == null)
            throw new IllegalArgumentException("Required parameter '" + name + "' not found");
        else
            return result;
    }


    public String getString(String name, String def) {
        name = "-" + name;
        for (int i = 0; i < args.length - 1; i++)
            if (args[i].equals(name))
                return args[i + 1];

        return def;
    }

    public int getInt(String name, int def) {
        String s = getString(name, null);
        return s == null ? def : Integer.parseInt(s);
    }
}
