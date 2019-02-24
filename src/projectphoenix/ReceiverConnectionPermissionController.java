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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class ReceiverConnectionPermissionController implements Initializable {

    @FXML
    private AnchorPane pane;
    @FXML
    private Button btnReceiver;
    @FXML
    private TextField txtThisPcIP;
    @FXML
    private Button btnSet;
    @FXML
    private Label lblIP;
    @FXML
    private Button btnSubmit;
    @FXML
    TextField txtSecurityKey;
    @FXML
    private VBox vbox;
    @FXML
    private Button btnBack;

    static PermissionStates permissionStates = new PermissionStates();
    private final int initialState = 0;
    private final int ipReceived = 1;
    private final int receiverIpsend = 2;
    private final int permissionWaiting = 3;
    private final int securityKeySend = 4;
    private final int securityKeyAccepted = 5;
    private final int securityKeyDenied = 6;
    private final int securityKeyAcceptedAfter = 7;
    private final int waitingForSenderIp = 8;
    ReceiverPermissionThreadReceive permissionReceive = new ReceiverPermissionThreadReceive();
    private int animX = 500;
    private int animY = 500;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        int numberOfSquares = 30;
        while (numberOfSquares > 0) {
            generateAnimation();
            numberOfSquares--;
        }
//        txtSecurityKey.setStyle("-fx-text-color:white");
        btnSubmit.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                btnSubmit.fire();
            }
        });
        txtSecurityKey.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                btnSubmit.fire();
            }
        });

        pane.setBackground(Background.EMPTY);
        txtThisPcIP.setVisible(false);
        btnSet.setVisible(false);
        btnSubmit.setVisible(false);
        btnReceiver.setVisible(false);
        FunIndicator funIndicator = new FunIndicator();
        vbox.getChildren().add(funIndicator);
        funIndicator.start();
        txtSecurityKey.setVisible(false);
        lblIP.setText(PcUIController.connection.getThisPc());
        permissionReceive.start();
        funIndicator.setFromColor(Color.RED);
        permissionStates.stateProperty().addListener(new ChangeListener<Object>() {

            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                System.out.println(permissionStates.getState());

                if (permissionStates.getState() == ipReceived) {
                    System.err.println("Sender has started Searching");
                    ReceiverPermissionSend.messenger("online");
                }
                if (permissionStates.getState() == permissionWaiting) {
                    Platform.runLater(() -> {
                        txtSecurityKey.setVisible(true);
                        btnSubmit.setVisible(true);
                        txtSecurityKey.requestFocus();
                    });
                }

                if (permissionStates.getState() == securityKeyAccepted) {
                    System.err.println("Security Key Accepted");
                    permissionStates.setState(securityKeyAcceptedAfter);
                    return;
                }

                if (permissionStates.getState() == securityKeyAcceptedAfter) {
                    Platform.runLater(() -> {
                        btnReceiver.fire();
                        return;
                    });
                }
            }
        });
    }

    @FXML
    private void ipset(ActionEvent event) {
        PcUIController.connection.setThisPc(txtThisPcIP.getText());
        lblIP.setText(PcUIController.connection.getThisPc());
    }

    public void exit(ActionEvent e) {
        try {
            ((Node) e.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
//            Stage primaryStage = (Stage) btnExit.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("Exit.fxml").openStream());
            Scene scene = new Scene(root);
            scene.setFill(null);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception ed) {
            ErrorWriter.writer(ed+" : receiver connection permission controller -> exit");
        }
    }

    public void securityKeySubmit(ActionEvent e) {
        ReceiverPermissionSend.messenger(txtSecurityKey.getText());
        permissionStates.setState(securityKeySend);
        txtSecurityKey.clear();
        txtSecurityKey.setVisible(false);
        btnSubmit.setVisible(false);
    }

    public void btnreceiver(ActionEvent e) {
        try {
//            ((Node) e.getSource()).getScene().getWindow().hide();
//            Stage primaryStage = new Stage();
            Stage primaryStage = (Stage) btnSubmit.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("ReceiveFromPc.fxml").openStream());
            Scene scene = new Scene(root);
            scene.setFill(null);
//            primaryStage.initStyle(StageSt5yle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception ex) {
            ErrorWriter.writer(ex+" : receiver connection permission controller -> btnreceiver");
        }
    }

    public void back(ActionEvent eee) {
        try {
//            ((Node) eee.getSource()).getScene().getWindow().hide();
//            Stage primaryStage = new Stage();
            Stage primaryStage = (Stage) btnBack.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("PcUI.fxml").openStream());
            Scene scene = new Scene(root);
//            primaryStage.initStyle(StageSt5yle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception ex) {
             ErrorWriter.writer(ex+" : receiver connection permission controller -> back");
        }
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
        Stage primaryStage = (Stage) btnBack.getScene().getWindow();
        primaryStage.setIconified(true);
    }
}
