package projectphoenix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HistoryController implements Initializable {

    public ObservableList<String> hislist = FXCollections.observableArrayList();
   
    @FXML
    private AnchorPane pane;

    
    @FXML
    private ListView<String> lv;
    @FXML
    private ListView<String> lv1;
    private int animX = 500;
    private int animY = 500;

    @FXML
    private Button btnClear;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lv.setBackground(Background.EMPTY);
        lv.getStylesheets().add(this.getClass().getResource("ListViewStyle.css").toExternalForm());

        lv1.setBackground(Background.EMPTY);
        lv1.getStylesheets().add(this.getClass().getResource("ListViewStyle.css").toExternalForm());
        pane.setBackground(Background.EMPTY);
        int numberOfSquares = 30;
        while (numberOfSquares > 0) {
            generateAnimation();
            numberOfSquares--;
        }

        try {
            BufferedReader br2 = new BufferedReader(new FileReader("C:\\Users\\Public\\historyreceive.txt"));
            StringBuilder sb2 = new StringBuilder();
            String line2 = br2.readLine();
            while (line2 != null) {
                line2 = br2.readLine();
                if (line2 != null) {
                    hislist.add(line2);
                }
            }
            String everything = sb2.toString();
            System.err.println(hislist);
            lv1.setItems(hislist);

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void exit(ActionEvent e) {
        ((Node) e.getSource()).getScene().getWindow().hide();
    }

    public void clear(ActionEvent e) {
        try {
            PrintWriter writer = new PrintWriter("C:\\Users\\Public\\historyreceive.txt");
            writer.print("");
            writer.close();
            hislist.clear();
            lv.setItems(hislist);
            lv.refresh();
        } catch (Exception e2) {
        }

    }

    public void back(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) btnClear.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("PcUI.fxml").openStream());
            Scene scene = new Scene(root);
            scene.setFill(null);
//            primaryStage.initStyle(StageSt5yle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void generateAnimation() {
        Random rand = new Random();
        int sizeOfSqaure = rand.nextInt(40) + 1;
        int speedOfSqaure = rand.nextInt(10) + 5;
        int startXPoint = rand.nextInt(animY);
        int startYPoint = rand.nextInt(animX);
        int direction = rand.nextInt(5) + 1;

        KeyValue moveXAxis = null;
        KeyValue moveYAxis = null;
        Rectangle r1 = null;
//        r1.toBack();

        switch (direction) {
            case 1:
                // MOVE LEFT TO RIGHT
                r1 = new Rectangle(0, startYPoint, sizeOfSqaure, sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), animX - sizeOfSqaure);
                break;
            case 2:
                // MOVE TOP TO BOTTOM
                r1 = new Rectangle(startXPoint, 0, sizeOfSqaure, sizeOfSqaure);
                moveYAxis = new KeyValue(r1.yProperty(), animY - sizeOfSqaure);
                break;
            case 3:
                // MOVE LEFT TO RIGHT, TOP TO BOTTOM
                r1 = new Rectangle(startXPoint, 0, sizeOfSqaure, sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), animX - sizeOfSqaure);
                moveYAxis = new KeyValue(r1.yProperty(), animY - sizeOfSqaure);
                break;
            case 4:
                // MOVE BOTTOM TO TOP
                r1 = new Rectangle(startXPoint, animY - sizeOfSqaure, sizeOfSqaure, sizeOfSqaure);
                moveYAxis = new KeyValue(r1.xProperty(), 0);
                break;
            case 5:
                // MOVE RIGHT TO LEFT
                r1 = new Rectangle(animY - sizeOfSqaure, startYPoint, sizeOfSqaure, sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), 0);
                break;
            case 6:
                //MOVE RIGHT TO LEFT, BOTTOM TO TOP
                r1 = new Rectangle(startXPoint, 0, sizeOfSqaure, sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), animX - sizeOfSqaure);
                moveYAxis = new KeyValue(r1.yProperty(), animY - sizeOfSqaure);
                break;

            default:
                System.out.println("default");
        }

        r1.setFill(Color.web("white"));
        r1.setOpacity(0.2);
        r1.toBack();

        KeyFrame keyFrame = new KeyFrame(Duration.millis(speedOfSqaure * 1000), moveXAxis, moveYAxis);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        pane.getChildren().add(pane.getChildren().size() - 1, r1);
    }
}
