package ru.nsu.cjvth.notebook;

import java.util.List;

public class Main {

    private static String help() {
        String s = "";
        s += "Usage: notebook <action> [...]\n";
        s += "Actions:\n";
        s += "    notebook add title body\n";
        s += "    notebook remove title\n";
        s += "    notebook show [-p | --period = <date/datetime>] [-f | --filter = <query>]\n";
        return s;
    }

    public static String parseArgs(String[] args) {
        List<String> possibleCommands = List.of("add", "remove", "show");
        if (args.length == 0 || args[0].equals("help")) {
            return help();
        } else if (possibleCommands.contains(args[0])) {
            // do something
            return "";
        } else {
            String s = String.format("Unknown action: %s\n", args[0]);
            s += help();
            return s;
        }
    }

    public static void main(String[] args) {
        String result = parseArgs(args);
        if (!result.isEmpty()) {
            System.out.print(result);
        }
    }
}
