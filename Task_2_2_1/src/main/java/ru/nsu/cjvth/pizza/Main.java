package ru.nsu.cjvth.pizza;

import java.util.ArrayList;

/**
 * Class to run program as executable. Initializes actors and proceeds them to Manager.
 */
public class Main {


    /**
     * Main function.
     *
     * @param args the only argument should be the path to configuration files
     */
    public static void main(String[] args) throws InterruptedException {
        Manager m = new Manager(10, 4, 3, 3, new ArrayList<>(80));
        m.run();
    }
}
