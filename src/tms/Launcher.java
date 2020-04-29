package tms;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import tms.display.MainViewModel;
import tms.display.MainView;
import tms.network.Network;
import tms.network.NetworkInitialiser;
import tms.util.InvalidNetworkException;

import java.io.IOException;

/**
 * Main entry point for the CSSE2002/7023 Traffic Management Simulation.
 *
 * @ass2_given View code for A2.
 */
public class Launcher extends Application {

    /**
     * CSSE2002/7023 Traffic Management Simulation Control Portal
     * <p>
     * Arguments: [FILENAME]
     *
     * @param args from the command line.
     * @ass2_given View code for A2.
     */
    public static void main(final String... args) {
        if (args.length != 1) {
            System.out.println("Usage: [NETWORK_LOAD_FILENAME]");
            System.out.println("To add a command line argument to your " +
                    "program in IntelliJ, go to \"Run > Edit Configurations " +
                    "> Program Arguments\" and add your file name to the " +
                    "text box.");
            System.exit(1);
        }
        Application.launch(Launcher.class, args);
    }

    /**
     * Runs the main GUI with the parameters passed via the command line.
     *
     * @param theStage stage to render to
     * @ass2_given View code for A2.
     */
    @Override
    public void start(Stage theStage) {
        theStage.setResizable(true);
        var params = getParameters().getRaw();

        Network network = null;  // Safe due to System.exit in catch block below.
        try {
            network = NetworkInitialiser.loadNetwork(params.get(0));
        } catch (IOException | InvalidNetworkException e) {
            System.err.println("Error loading from file \"" + params.get(0)
                    + "\": " + e.toString());
            Platform.exit();
            System.exit(1);
        }

        var view = new MainView(theStage, new MainViewModel(network));
        view.run();
    }
}
