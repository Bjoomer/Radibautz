package DisplayCards;

import javax.swing.*;

/**
 * The main class for starting the Radibautz game.
 */
public class RadibautzMain {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        // Launch the game on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            RadibautzModel model = new RadibautzModel(); // Create the game model
            RadibautzView view = new RadibautzView();   // Create the game view
            RadibautzController controller = new RadibautzController(model, view); // Connect model and view with the controller
        });
    }
}