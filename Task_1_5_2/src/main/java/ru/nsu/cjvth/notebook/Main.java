package ru.nsu.cjvth.notebook;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.function.BiFunction;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * CLI program to work with a notebook stored in notebook.json.
 */
public class Main {


    private static final String dateTimeFormatString = "dd.MM.yyyy HH:mm:ss";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
        dateTimeFormatString);
    private static String help;

    private static String help() {
        if (help == null) {
            StringBuilder helpBuilder = new StringBuilder();
            helpBuilder.append("Usage: notebook <action> [...]\nActions:\n");
            for (var entry : possibleCommands.entrySet()) {
                helpBuilder.append(entry.getValue().help);
            }
            help = helpBuilder.toString();
        }
        return help;
    }

    /**
     * Parse arguments and manipulate with already existing notebook.
     *
     * @param args     cli arguments from main
     * @param notebook notebook to be manipulated
     * @return Message that indicates error of empty string
     */
    public static String parseArgs(String[] args, Notebook notebook) {
        if (args.length == 0) {
            return help();
        }
        for (var entry : possibleCommands.entrySet()) {
            if (args[0].equals(entry.getKey())) {
                return entry.getValue().function.apply(args, notebook);
            }
        }
        return help();
    }

    /**
     * CLI program to work with a notebook stored in notebook.json.
     *
     * @param args arguments, given at program startup
     */
    public static void main(String[] args) {
        File file = new File("notebook.json");
        Notebook notebook;
        if (file.exists()) {
            try {
                Reader reader = new FileReader(file);
                notebook = readNotebook(reader);
            } catch (IOException e) {
                System.out.println(
                    "Error parsing json from file " + file.getName() + "\n" + e.getMessage());
                return;
            }
        } else {
            notebook = new Notebook();
        }
        if (notebook == null) {
            return;
        }
        String result = parseArgs(args, notebook);
        if (!result.isEmpty()) {
            System.out.println(result);
            return;
        }
        for (var entry : possibleCommands.entrySet()) {
            if (args[0].equals(entry.getKey()) && entry.getValue().changes) {
                try {
                    Writer writer = new FileWriter(file);
                    writeNotebook(writer, notebook);
                    writer.close();
                } catch (IOException e) {
                    System.out.println(
                        "Error writing json to file " + file.getName() + "\n" + e.getMessage());
                }
                return;
            }
        }
    }

    /**
     * Parse json of a notebook.
     *
     * @param reader reader from where the notebook is read
     * @return the notebook
     */
    public static Notebook readNotebook(Reader reader) throws IOException {
        Gson gson = new Gson();
        return gson.getAdapter(Notebook.class).fromJson(reader);
    }

    /**
     * Write notebook as json.
     *
     * @param writer   writer to where json is written
     * @param notebook notebook to write
     */
    public static void writeNotebook(Writer writer, Notebook notebook) throws IOException {
        Gson gson = new Gson();
        gson.getAdapter(Notebook.class).toJson(writer, notebook);
    }

    private static String add(String[] args, Notebook notebook) {
        if (args.length != 3) {
            return help();
        }
        boolean res = notebook.add(args[1], args[2]);
        if (!res) {
            return "Note with this title already exists";
        } else {
            return "";
        }
    }

    private static String remove(String[] args, Notebook notebook) {
        if (args.length != 2) {
            return help();
        }
        boolean res = notebook.remove(args[1]);
        if (!res) {
            return "Note with this title doesn't exist";
        } else {
            return "";
        }
    }

    private static String show(String[] args, Notebook notebook) {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("after").hasArg().build());
        options.addOption(Option.builder("b").longOpt("before").hasArg().build());
        options.addOption(Option.builder("f").longOpt("find").hasArg().build());
        CommandLine cmd;
        try {
            cmd = new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            return "Error parsing cli arguments" + e.getMessage();
        }

        LocalDateTime after;
        LocalDateTime before;
        try {
            String[] a = cmd.getOptionValues("a");
            if (a == null) {
                after = null;
            } else if (a.length > 1) {
                return help();
            } else {
                after = LocalDateTime.parse(a[0], dateTimeFormatter);
            }
            String[] b = cmd.getOptionValues("b");
            if (b == null) {
                before = null;
            } else if (b.length > 1) {
                return help();
            } else {
                before = LocalDateTime.parse(b[0], dateTimeFormatter);
            }
        } catch (DateTimeParseException e) {
            return "Datetime must be in format " + dateTimeFormatString;
        }

        String[] find = cmd.getOptionValues("f");
        StringBuilder sb = new StringBuilder();
        boolean notFirst = false;
        for (Map.Entry<String, Notebook.Note> entry : notebook.show(find, after, before)) {
            if (notFirst) {
                sb.append("\n\n--------------------\n\n");
            }
            notFirst = true;
            sb.append(entry.getKey()).append("\n");
            String lastEdit = dateTimeFormatter.format(
                ZonedDateTime.ofInstant(Instant.ofEpochMilli(entry.getValue().created()),
                    ZoneId.systemDefault()));
            sb.append("<").append(lastEdit).append(">\n");
            sb.append(entry.getValue().text());
        }
        return sb.toString();
    }

    private static class Command {

        public BiFunction<String[], Notebook, String> function;
        public String help;
        public boolean changes;

        public Command(BiFunction<String[], Notebook, String> f, String h, boolean c) {
            function = f;
            help = h;
            changes = c;
        }
    }

    private static final Map<String, Command> possibleCommands = Map.of(
        "add", new Command(Main::add, "title body", true),
        "remove", new Command(Main::remove, "title", true),
        "show", new Command(Main::show,
            "[-a | --after <datetime>] [-b | -before <datetime>] [-f | --find <query>]*", false)
    );
}
