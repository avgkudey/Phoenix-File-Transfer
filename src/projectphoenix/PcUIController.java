package projectphoenix;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import sun.plugin2.os.windows.Windows;

public class PcUIController implements Initializable {

    static public ObservableList<String> cmbiplist = FXCollections.observableArrayList();
    public ObservableList<String> cmbiplist2 = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> cmbIP;
    @FXML
    private AnchorPane pane;
    @FXML
    private Button btnSend;
    @FXML
    private Button btnReceive;
    @FXML
    private Button btnExit;
    @FXML
    private Label lblSysIP;
    @FXML
    private Button btnAbout;
    @FXML
    private ImageView imview;
    @FXML
    private ImageView img2;
    @FXML
    private Rectangle rectReceive;
    @FXML
    private Rectangle rectSend;
    @FXML
    private ImageView img3;

    @FXML
    private ImageView img1;
    @FXML
    private Button btnfacebook;

    static public String ipwifi = "";
    static public String iplan = "";
    String savepath;
//    MediaPlayer mp;
//    Media me;
    /**
     * Search local IP address
     */

    /**
     * Connection Information Class
     */
    public static Connection connection = new Connection();
    public static PortDetails portDetails = new PortDetails();

    /**
     * Background animation Limits
     */
    private int animX = 250;
    private int animY = 300;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rectSend.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                btnSend.fire();

            }
        });

        rectReceive.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                btnReceive.fire();

            }
        });
        lblSysIP.setVisible(false);

//       cmbiplist.add("127.0.0.1");
        try {
            if (cmbiplist.size() == 0) {
                cmbiplist2.add("localhost");

            }
            if (ipwifi.length() > 0) {
                cmbiplist2.add("WIFI");
//                cmbiplist2.add("localhost");
//                cmbIP.setItems();
//                cmbiplist.add(ip[0]);
            }
            if (iplan.length() > 0) {
                cmbiplist2.add("Ethernet");
//                cmbIP.setItems();
//                cmbiplist.add(ip[0]);
            }

//            if (ip[0] != null|| ip[0]=="") {
//                cmbiplist.add("WIFI");
//                 connection.setThisPc(ip[0]);
//            }
            connection.setThisPc(cmbiplist.get(0));
            cmbIP.setItems(cmbiplist2);
            cmbIP.getSelectionModel().selectFirst();
        } catch (Exception e) {
            System.err.println(e + "789");
        }
//        cmbIP.setSelectionModel();

//        File f2 = new File("C:\\Users\\Public\\settings.txt");
//         if (f2.exists()) {
//
//        } else {
//            System.err.println("no");
////            f2.mkdir();
//             try {
//                  Writer writer = new BufferedWriter(new OutputStreamWriter(
//                    new FileOutputStream("C:\\Users\\Public\\settings.txt", true), "UTF-8"));
//            BufferedWriter buffer = new BufferedWriter(writer);
//            buffer.write("C:\\Phoenix Receive");
//            buffer.close();
//             } catch (Exception e) {
//             }
//        }

//        try {
//            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Public\\settings.txt"));
//            StringBuilder sb = new StringBuilder();
//            String line = br.readLine();
//
//            while (line != null) {
//                savepath = line;
//                line = br.readLine();
//            }
//        } catch (Exception e) {
//            System.err.println(e);
//        }

        File f = new File("C:\\Phoenix Receive");
        if (f.exists() && f.isDirectory()) {

        } else {
            System.err.println("no");
            f.mkdir();
        }
        FadeTransition fd = new FadeTransition(Duration.seconds(2), imview);
        fd.setFromValue(0.4);
        fd.setToValue(0.8);
        fd.setCycleCount((int) Windows.INFINITE);
        fd.setAutoReverse(true);
        fd.play();

        lblSysIP.setText(connection.getThisPc());
        pane.setBackground(Background.EMPTY);
        int numberOfSquares = 30;
        while (numberOfSquares > 0) {
            generateAnimation();
            numberOfSquares--;
        }
        imview.toFront();
        img1.toFront();
        img2.toFront();
        img3.toFront();
        btnAbout.toFront();
        btnSend.toFront();
        btnReceive.toFront();
        btnExit.toFront();
//        btnfacebook.toFront();
//        btntwitter.toFront();
    }

    /**
     * Send Button Action
     *
     * @param event
     */
    @FXML
    private void send(ActionEvent event) {
        try {

            Stage primaryStage = (Stage) btnSend.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("ServerConnectionPermission.fxml").openStream());
//            Pane root = loader.load(getClass().getResource("SendToPc.fxml").openStream());
            Scene scene = new Scene(root);
            scene.setFill(null);
//            primaryStage.initStyle(StageStyle.DECORATED);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            ErrorWriter.writer(e + " : pc ui controller -> send");
        }
    }

    /**
     * Receive button action
     *
     * @param event
     */
    @FXML
    private void receive(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) btnExit.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("ReceiverConnectionPermission.fxml").openStream());
//            Pane root = loader.load(getClass().getResource("ReceiveFromPc.fxml").openStream());
            Scene scene = new Scene(root);
            scene.setFill(null);
//            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            ErrorWriter.writer(e + " : pc ui controller -> receive");
        }
    }

    /**
     * Exit button action
     *
     * @param event
     */
    @FXML
    private void exit(ActionEvent event) {
        try {
            ((Node) event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("Exit.fxml").openStream());
            Scene scene = new Scene(root);
            scene.setFill(null);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            ErrorWriter.writer(e + " : pc ui controller -> exit");
        }
    }

    /**
     * Developer Details Windows
     *
     * @param e
     */
    public void aboutUs(ActionEvent e) {
        try {
            Stage primaryStage = (Stage) btnExit.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("AboutUs.fxml").openStream());
            Scene scene = new Scene(root);
            scene.setFill(null);
//            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception ee) {
            ErrorWriter.writer(ee + " : pc ui controller -> about us");
        }
    }

    /**
     * Generate Background Animation
     */
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

        r1.setFill(Color.web("black"));
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

    public void history(ActionEvent e) {

        try {
            Stage primaryStage = (Stage) btnExit.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("History.fxml").openStream());
            Scene scene = new Scene(root);
            scene.setFill(null);
//            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception ee) {
            ErrorWriter.writer(ee + " : pc ui controller ->  history");
        }

//        try {
//            Stage primaryStage = new Stage();
//            FXMLLoader loader = new FXMLLoader();
//            primaryStage.setX(btnAbout.getScene().getWindow().getX() + btnAbout.getScene().getWindow().getWidth() + 2);
//            primaryStage.setY(btnAbout.getScene().getWindow().getY());
//            primaryStage.initOwner(btnAbout.getScene().getWindow());
//            Pane root = loader.load(getClass().getResource("History.fxml").openStream());
//            Scene scene = new Scene(root);
//            scene.setFill(null);
//            primaryStage.initStyle(StageStyle.TRANSPARENT);
//            primaryStage.setScene(scene);
//            primaryStage.show();
//        } catch (Exception e1) {
//            ErrorWriter.writer(e1 + " : pc ui controller -> history");
//        }
    }

    public void errorlog(ActionEvent e) {
        try {
            Runtime.getRuntime().exec("explorer.exe /open,C:\\Users\\Public\\errorlog.txt");
        } catch (IOException ex) {

        }

    }

    public void ipselect(ActionEvent e) {

        switch (cmbIP.getSelectionModel().getSelectedItem()) {
            case "localhost":
                connection.setThisPc("127.0.0.1");
                break;
            case "WIFI":
                connection.setThisPc(ipwifi);
                break;
            case "Ethernet":
                connection.setThisPc(iplan);
                break;
            default:
        }
//        connection.setThisPc(cmbIP.getSelectionModel().getSelectedItem());

    }

    public void facebook(ActionEvent e) {
        try {
            Desktop.getDesktop().browse(new URL("https://www.facebook.com/avgk.udayanga").toURI());
        } catch (Exception ec) {
        }
    }

    public void twitter(ActionEvent e) {
        try {
            Desktop.getDesktop().browse(new URL("https://twitter.com/kasunudaya1").toURI());
        } catch (Exception ec) {
        }
    }

    public void minimize(ActionEvent e) {
        Stage primaryStage = (Stage) btnExit.getScene().getWindow();
        primaryStage.setIconified(true);
    }

    public void settings(ActionEvent e) {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            primaryStage.initOwner(btnAbout.getScene().getWindow());
            Pane root = loader.load(getClass().getResource("settings.fxml").openStream());
            Scene scene = new Scene(root);
            scene.setFill(null);
            primaryStage.initStyle(StageStyle.UTILITY);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e1) {
            ErrorWriter.writer(e1 + " : pc ui controller -> settings");
        }
    }
}
