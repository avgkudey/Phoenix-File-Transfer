package projectphoenix;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ServerConnectionPermissionController implements Initializable {

    @FXML
    private AnchorPane pane;

    @FXML
    private TextField txtIP;

    @FXML
    private Button btnConnect;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnBack;
    @FXML
    private Button btntryAgain;
    @FXML
    private Label lblSecurityKey;

    @FXML
    private Label lblCurrentIP;

    @FXML
    private VBox vbox;

    @FXML
    private ListView<String> listView;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnsender;
    /**
     * States Stages
     */
    private final int searchStarted = 1;
    private final int online = 2;
    private final int securityKeySend = 3;
    private final int securityKeyCheck = 4;
    private final int securityKeyAccepted = 5;
    private final int securityKeyDenied = 6;
    /**
     *
     */
    private String securityKey;
    static PermissionStates permissionStates = new PermissionStates();
    ServerPermissionThreadReceive permissionThreadReceive = new ServerPermissionThreadReceive();
    /**
     *
     */
    FunIndicator funIndicator = new FunIndicator();
    SearchClients searchClients = new SearchClients();
    TrayNotification notification = new TrayNotification();
    private int animX = 500;
    private int animY = 500;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int numberOfSquares = 30;
        while (numberOfSquares > 0) {
            generateAnimation();
            numberOfSquares--;
        }
        pane.setBackground(Background.EMPTY);
        listView.setBackground(Background.EMPTY);
        lblSecurityKey.setVisible(false);
//        listView.setStyle(".list-cell {-fx-background-color:transparent;} .list-view{-fx-background-color:transparent;}");
        btntryAgain.setVisible(false);
        listView.getStylesheets().add(this.getClass().getResource("ListViewStyle.css").toExternalForm());
        txtIP.setVisible(false);
        btnConnect.setVisible(false);
        btnSearch.arm();
        btnsender.setVisible(false);
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

//                btnConnect.setVisible(true);
                btnConnect.fire();

            }
        });
        funIndicator.setFromColor(Color.BLACK);
        funIndicator.setToColor(Color.BLACK);
        vbox.getChildren().add(funIndicator);
        lblCurrentIP.setText(PcUIController.connection.getThisPc());
        permissionThreadReceive.start();
        permissionStates.stateProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                System.out.println(permissionStates.getState());
                if (permissionStates.getState() == searchStarted) {
                    System.err.println("Search Started");
                    populate();
                }

                if (permissionStates.getState() == online) {
                    ServerPermissionSend.messenger("permission");
                    permissionStates.setState(securityKeySend);

                }
                if (permissionStates.getState() == securityKeyCheck) {
                    if (securityKey.matches(permissionStates.incomingSecurityKey)) {
                        System.err.println("Match");
                        ServerPermissionSend.messenger("accepted");
                        permissionStates.setState(securityKeyAccepted);
                        return;
                    } else {
                        System.err.println("Not Match");
                        ServerPermissionSend.messenger("denied");
                        permissionStates.setState(securityKeyDenied);
                        return;
                    }
                }

                if (permissionStates.getState() == securityKeyAccepted) {
                    System.err.println("permision accepted");
                    Platform.runLater(() -> {
                        btnsender.fire();
                        searchClients.stop();
                        return;
                    });
                }

                if (permissionStates.getState() == securityKeyDenied) {
                    System.err.println("permision Denied");
                    permissionStates.setState(searchStarted);
                    Platform.runLater(() -> {
                        notification.setTitle("Security Info");
                        notification.setAnimationType(AnimationType.FADE);
                        notification.setNotificationType(NotificationType.ERROR);
                        notification.setMessage("Security Key Is Not Match");
                        notification.showAndDismiss(Duration.seconds(3));
                        lblSecurityKey.setVisible(false);
                        listView.setVisible(true);
                        return;
                    });
                }
            }
        });
    }

    @FXML
    private void connect(ActionEvent event) {
        try {
            txtIP.setText(listView.getSelectionModel().getSelectedItem());
            if (txtIP.getText().length() > 2) {
                lblSecurityKey.setVisible(true);
                listView.setVisible(false);
                PcUIController.connection.setClient(txtIP.getText());
                lblCurrentIP.setText(PcUIController.connection.getThisPc());
                securityKey = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(5, 10);
                lblSecurityKey.setText(securityKey);
                ServerPermissionSend.messenger(PcUIController.connection.getThisPc());
                searchClients.stop();
                btnConnect.setVisible(false);
                btntryAgain.setVisible(true);
            }
        } catch (Exception e) {
            ErrorWriter.writer(e + " : Server connection permission controller -> connect");
        }
    }

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
            ErrorWriter.writer(e + " : Server connection permission controller -> exit");
        }
    }

    @FXML
    private void back(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) btnExit.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("PcUI.fxml").openStream());
            Scene scene = new Scene(root);
//            primaryStage.initStyle(StageSt5yle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            ErrorWriter.writer(e + " : Server connection permission controller -> back");
        }
    }

    public void search(ActionEvent e) {
        permissionStates.setState(searchStarted);
        funIndicator.start();
        searchClients.start();
        btnSearch.setVisible(false);
    }

    public void populate() {
        Thread thr = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        listView.setItems(permissionStates.serverIplist);
                        listView.refresh();
                    }
                } catch (Exception e) {
                    System.err.println(e);
                    ErrorWriter.writer(e + " : Server connection permission controller -> populate");
                }
            }
        });
        thr.start();
    }

    public void handleAccept(ActionEvent e) {
        try {
            Stage primaryStage = (Stage) btnExit.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("SendToPc.fxml").openStream());
            Scene scene = new Scene(root);
            scene.setFill(null);
//            primaryStage.initStyle(StageSt5yle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.getScene().getStylesheets().add(getClass().getResource("tbl.css").toExternalForm());
            primaryStage.show();
        } catch (Exception ex) {
            ErrorWriter.writer(e + " : Server connection permission controller -> handle accept");
        }
    }

    public void tryAgain(ActionEvent e) {
        lblSecurityKey.setVisible(false);
        listView.setVisible(true);
        btntryAgain.setVisible(false);
    }

    public void generateAnimation() {
        Random rand = new Random();
        int sizeOfSqaure = rand.nextInt(50) + 1;
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

    public void minimize(ActionEvent e) {
        Stage primaryStage = (Stage) btnExit.getScene().getWindow();
        primaryStage.setIconified(true);
    }
}
