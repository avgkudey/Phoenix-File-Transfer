package projectphoenix;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProjectPhoenix extends Application {

    private static Stage primaryStageObj;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStageObj = stage;
//        Parent root = FXMLLoader.load(getClass().getResource("SendToPc.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("Splash.fxml"));
//Parent root = FXMLLoader.load(getClass().getResource("ReceiveFromPc.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(null);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.getScene().getStylesheets().add(getClass().getResource("tbl.css").toExternalForm());
        stage.show();
//        stage.setResizable(false);
        stage.setOnCloseRequest(e -> Platform.exit());
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStageObj;
    }
}
