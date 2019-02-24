package projectphoenix;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import sun.plugin2.os.windows.Windows;

public class SplashController implements Initializable {

    @FXML
    private Pane pane;
    @FXML
    private Button btnMain;
    @FXML
    private ImageView imgviewtext;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FadeTransition rt = new FadeTransition(Duration.millis(1000), imgviewtext);
        rt.setFromValue(0.2);
        rt.setToValue(0.8);
        rt.setCycleCount((int) Windows.INFINITE);
        rt.setAutoReverse(true);
        rt.play();

        SearchLocalIP searchLocalIp = new SearchLocalIP();
        searchLocalIp.setName("SearchLocalIp");
        searchLocalIp.start();
        btnMain.setVisible(false);
        pane.setBackground(Background.EMPTY);
        Thread loadThread = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                    Platform.runLater(() -> btnMain.fire());
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        };
        loadThread.start();
    }

    public void main(ActionEvent e) {
        try {
            ((Node) e.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("PcUI.fxml").openStream());
            Scene scene = new Scene(root);
            scene.setFill(null);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
//            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e2) {
        }
    }
}
