package se.kth.iv1350.pos.startup;

import se.kth.iv1350.pos.integration.CreateExternalSys;
import se.kth.iv1350.pos.integration.Printer;
import se.kth.iv1350.pos.controller.Controller;
import se.kth.iv1350.pos.view.View;

import java.io.IOException;

/**
 * <code>Main</code>> starts the application and sets up the necessary components needed.
 */
public class Main {

    /**
     * <code>main</code> starts the application and sets up the necessary components needed to run it.
     * @param args This application does not use command line arguments.
     */
    public static void main(String[] args) {
        CreateExternalSys creator = new CreateExternalSys();
        Printer printer = new Printer();
        try {
            Controller controller = new Controller(creator, printer);
            View view = new View(controller);
            view.simulateApplication();
        }catch (IOException ioe){
            System.out.println("Could not start log file.");
        }
    }
}