package DisplayCards;

import javax.swing.*;

/**
 * The type Radibautz main.
 */
public class RadibautzMain {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RadibautzModel model = new RadibautzModel();
            RadibautzView view = new RadibautzView();
            RadibautzController controller = new RadibautzController(model, view);
        });
    }
}
