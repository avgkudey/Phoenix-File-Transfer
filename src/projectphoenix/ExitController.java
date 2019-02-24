/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectphoenix;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;

/**
 * FXML Controller class
 *
 * @author KASUN
 */
public class ExitController implements Initializable {

    @FXML
    private AnchorPane pane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pane.setBackground(Background.EMPTY);
        Thread exitThread = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                    Platform.runLater(() -> {
                        System.exit(0);
                        Platform.exit();
                    });
                } catch (Exception e) {
                }
            }
        };
        exitThread.start();
    }
}
