package projectphoenix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import tray.animations.AnimationType;
import tray.notification.TrayNotification;

public class ReceiveFromPcController implements Initializable {

    static String savePath = "c:\\Phoenix Receive\\";
    private final int initialState = 0; // initial State
    private final int permissionWatingState = 1; // waiting for permission
    private final int permissionAcceptedState = 2; // permission Accepted
    private final int permissionDeniedState = 3; //permission Denied
    private final int fileCountWaitState = 4; // file cout waiting
    private final int fileNameWaitState = 5; // file name waiting
    private final int fileSizeWaitState = 6; // file size waiting
    private final int fileWaitingState = 7; // waiting for incoming
    private final int fileReceivingState = 8; // file incomming
    private final int fileReceivedState = 9; // file received
    private final int operationCompletedState = 10; // file tranfer completed
    TrayNotification tr = new TrayNotification();
    @FXML
    private Label lblsize;
    @FXML
    private TableView<ReceiverTablePopulate> table;
    @FXML
    private TableColumn<ReceiverTablePopulate, String> name;
    @FXML
    private TableColumn<ReceiverTablePopulate, String> size;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnExit;
    @FXML
    private Label lblfc;
    @FXML
    private Button btnOutput;
    @FXML
    private VBox stack;
    @FXML
    private AnchorPane pane;
    @FXML
    private Button btnSend;
    @FXML
    private TextField txtIn;
    @FXML
    private ListView<msgs> tareaOutput;

//    @FXML
//    private Rectangle rec;
    static ChatterStates chatterstates = new ChatterStates();
    List<msgs> list = new ArrayList<>();
    ObservableList<msgs> tdetails;

    public static ReceiveStates receiveStates = new ReceiveStates();
    public static FileSize fileSizeProperty = new FileSize();
    static Alert al = new Alert(Alert.AlertType.CONFIRMATION, "Incomming File Transfer Request", ButtonType.YES, ButtonType.NO);
    static Optional<ButtonType> result;
    static ReceiveFromPcController rpc = new ReceiveFromPcController();
    ReceiverMessageReceive rmr = new ReceiverMessageReceive();
    int index = 0;
    ExecutorService fileReceiveExecutor = Executors.newCachedThreadPool();
    static FileReceiveThread fileReceiveThread = new FileReceiveThread();
    public ObservableList<ReceiverTablePopulate> tableDetails = FXCollections.observableArrayList();
    ChatReceiverThread receiver = new ChatReceiverThread();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Public\\settings.txt"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                savePath = line;
                line = br.readLine();
            }
        } catch (Exception e) {
            System.err.println(e);
        }

        stack.setVisible(false);
        chatShow(false);

        tareaOutput.getStylesheets().add(this.getClass().getResource("ListViewStyleChat.css").toExternalForm());
        txtIn.requestFocus();
        txtIn.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                btnSend.fire();
            }
        });

//        receiver.start();
        txtIn.requestFocus();
        chatterstates.msgCountProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                list.add(new msgs("  " + ChatReceiverThread.passMessage().trim() + "  ", 1));
                tdetails = FXCollections.observableList(list);
                tareaOutput.setItems(tdetails);

                if (!txtIn.isVisible()) {
                    chatShow(true);
                    txtIn.requestFocus();
                }
                return;

            }
        });
        ////////////////////////////////////////////////////

        receiver.start();
        stack.setBackground(Background.EMPTY);
        pane.setBackground(Background.EMPTY);
        btnBack.setVisible(false);
        FillProgressIndicator fl = new FillProgressIndicator();
        fl.setBackground(Background.EMPTY);
//        fl.
        fl.setStyle("-fx-background-color:transparent;");
        fl.setInnerCircleRadius(50);
        fl.makeIndeterminate();
//fl.setProgress(50); 

        stack.getChildren().add(fl);
        fileSizeProperty.fileSizeProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                Platform.runLater(() -> {
                    lblfc.setText(receiveStates.getFlist().get(FileReceiveThread.fileCount - 1));
                    lblsize.setText(String.valueOf(Integer.valueOf(receiveStates.getFsList().get(FileReceiveThread.fileCount - 1)) / (1024 * 1024)) + " Mb");
                });
                Platform.runLater(() -> fl.setProgress((int) (Double.valueOf(newValue.toString()) / Double.valueOf(receiveStates.getFsList().get(FileReceiveThread.fileCount - 1)) * 100) + 1));

            }
        });
        System.err.println("Receiver controller");
        name.setCellValueFactory(new PropertyValueFactory<ReceiverTablePopulate, String>("Name"));
        size.setCellValueFactory(new PropertyValueFactory<ReceiverTablePopulate, String>("Size"));

//        table.setItems(tableDetails);
        try {
            fileReceiveThread.start();
        } catch (Exception e) {
            System.out.println(e + " binding");
        }
        rmr.start();
        al.setTitle("Permission");
        receiveStates.fileCountProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                Platform.runLater(() -> lblfc.setText("file count = " + newValue.toString()));
                if (receiveStates.getFileCount() > 0) {
                    ReceiverMessageSend.messenger("received");
                    System.out.println("File count receive");
                    System.out.println("file count = " + receiveStates.getFileCount());
//                    pop();
                }
            }
        });
        receiveStates.stateProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                System.out.println(receiveStates.getState() + " (Listener)");
                if (receiveStates.getState() == operationCompletedState) {
                }
                if (receiveStates.getState() == permissionWatingState) {
                    Platform.runLater(() -> rpc.permissionMessage());
                }
                if (receiveStates.getState() == permissionAcceptedState) {
                    ReceiverMessageSend.messenger("accepted");
                    stack.setVisible(true);
                    receiveStates.setState(fileCountWaitState);
                    return;
                }
                if (receiveStates.getState() == fileCountWaitState) {
                    System.out.println("Waiting for file count");
                    System.out.println(rmr.getState());
                }
                if (receiveStates.getState() == fileNameWaitState) {
                    System.out.println("Waiting for file names");
                }
                if (receiveStates.getState() == fileSizeWaitState) {
                    System.out.println("Waiting for file Sizes");
                }
                if (receiveStates.getState() == fileWaitingState) {
                    try {
                        pop();
                        System.err.println("File Waiting State");
                        return;
//                        System.out.println(receiveStates.getFlist().get(0));
//                        System.out.println("waiting for file");
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    return;
                }
                if (receiveStates.getState() == operationCompletedState) {
                    System.exit(0);
                }
            }
        });

        receiveStates.fileCountProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
            }
        });
    }

    @FXML
    private void back(ActionEvent event) {
        try {
            ((Node) event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
//            Stage primaryStage = (Stage) btnExit.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("Exit.fxml").openStream());
            Scene scene = new Scene(root);
            scene.setFill(null);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            ErrorWriter.writer(e + " : receive from pc controller -> back");
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        try {
            ((Node) event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
//            Stage primaryStage = (Stage) btnExit.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("Exit.fxml").openStream());
            Scene scene = new Scene(root);
            scene.setFill(null);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            ErrorWriter.writer(e + " : receive from pc controller -> exit");

        }
    }

    public void permissionMessage() {
        tr.setTitle("File Inbound");
        tr.setMessage("Requesting Permission For Sending Files");
        tr.setAnimationType(AnimationType.FADE);
        tr.showAndDismiss(Duration.seconds(1));
        result = al.showAndWait();
        if (result.get() == ButtonType.YES) {
            ReceiveFromPcController.receiveStates.setState(permissionAcceptedState);

        }
        if (result.get() == ButtonType.NO) {
            ReceiverMessageSend.messenger("Denied");
            ReceiveFromPcController.receiveStates.setState(permissionDeniedState);
        }
    }

    public void outPath(ActionEvent e) {
    }

    public void savePath(ActionEvent e) throws IOException {
        Runtime.getRuntime().exec("explorer.exe /open," + savePath);
        
    }

    private void pop() {
        Thread tr = new Thread(new Runnable() {
            @Override
            public void run() {
                System.err.println("jjivhihifdifhichifghidhsei");
//               tableDetails.add(new ReceiverTablePopulate("dfdf", "sfsf00"));
//                while (true) {
                try {
//                        tableDetails.clear();
                    for (int i = 0; i < receiveStates.getFileCount(); i++) {
//                        tableDetails.clear();
                        tableDetails.add(new ReceiverTablePopulate(receiveStates.getFlist().get(i), receiveStates.getFsList().get(i)));
                    }

                    table.setItems(tableDetails);
                } catch (Exception e) {
                    ErrorWriter.writer(e + " : receive from pc controller -> pop");
                    System.err.println(e);
                }
            }
//            }
        });
        tr.start();
    }

    public void chat(ActionEvent e) {

        if (!txtIn.isVisible()) {
            chatShow(true);
            txtIn.requestFocus();
        } else {
            chatShow(false);
        }
//        try {
//            Stage primaryStage = new Stage();
//            FXMLLoader loader = new FXMLLoader();
//            primaryStage.setX(btnBack.getScene().getWindow().getX() + btnBack.getScene().getWindow().getWidth());
//            primaryStage.initOwner(btnBack.getScene().getWindow());
//            primaryStage.setY(btnBack.getScene().getWindow().getY());
//            Pane root = loader.load(getClass().getResource("ChatReceiver.fxml").openStream());
//            Scene scene = new Scene(root);
//            scene.setFill(null);
//            primaryStage.initStyle(StageStyle.TRANSPARENT);
//            primaryStage.setScene(scene);
//            primaryStage.show();
//        } catch (Exception e1) {
//        }
    }

    public void chatSend(ActionEvent e) {
        if (txtIn.getLength() > 0) {
            ChatReceiverSender.msge(txtIn.getText());
            list.add(new msgs("  " + txtIn.getText().trim() + "  ", 2));
            tdetails = FXCollections.observableList(list);
            tareaOutput.setItems(tdetails);
            tareaOutput.scrollTo(tdetails.size() - 1);
            txtIn.clear();
            txtIn.requestFocus();
        }
    }

    public static class msgs extends HBox {

        public msgs(String m, int k) {
            if (k == 1) {
                outgoing(m);
            } else {
                inco(m);
            }
        }

        public void outgoing(String msg) {
//            Media me = new Media(new File("src\\projectphoenix\\notification.wav").toURI().toString());
//            MediaPlayer mp = new MediaPlayer(me);
//            mp.play();
            Label lblmsg = new Label(msg);
            lblmsg.setOpacity(0.63);
            lblmsg.setWrapText(true);
            lblmsg.maxWidth(170);
            lblmsg.setStyle("-fx-background-radius:5; -fx-background-color: white; -fx-font-size:14pt");
            lblmsg.setTextFill(Color.web("#0000ff"));
            this.setAlignment(Pos.BASELINE_LEFT);
            this.getChildren().addAll(lblmsg);
            this.setWidth(200);
        }

        public void inco(String msg) {

            Label lblmsg = new Label(msg);
            this.setAlignment(Pos.BASELINE_RIGHT);
            lblmsg.setOpacity(0.63);
            lblmsg.setWrapText(true);
            lblmsg.maxWidth(170);
            lblmsg.setMaxWidth(170);
            lblmsg.setStyle("-fx-background-radius:5; -fx-background-color: white; -fx-font-size:14pt");
            lblmsg.setTextFill(Color.web("#00ff00"));
            this.getChildren().addAll(lblmsg);

            this.setWidth(200);

        }

    }

    public void chatShow(boolean option) {
//        rec.setVisible(option);
        txtIn.setVisible(option);
        btnSend.setVisible(option);
        tareaOutput.setVisible(option);
    }

    public void minimize(ActionEvent e) {
        Stage primaryStage = (Stage) btnExit.getScene().getWindow();
        primaryStage.setIconified(true);
    }
}
