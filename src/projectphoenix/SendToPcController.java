package projectphoenix;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.TrayNotification;

public class SendToPcController implements Initializable {

    public static int finfo = 0;
    @FXML
    private Label lblfileName;

    @FXML
    private Label lblPath;

    @FXML
    private Label lblSize;
    @FXML
    private AnchorPane pane;
    @FXML
    private TableView<ServerTablePopulate> table;
    @FXML
    private TableColumn<ServerTablePopulate, String> name;
    @FXML
    private TableColumn<ServerTablePopulate, String> size;

    @FXML
    private TableColumn<ServerTablePopulate, String> path;
    @FXML
    private Button btnBack;
    @FXML
    private Label lblFileCount;
    @FXML
    private Button btnExit;
    @FXML
    private Button btnSelectFiles;
    @FXML
    private Button btnSend;

    @FXML
    private ImageView imgadd;

    @FXML
    private VBox vbox;
    ///////////////////////////////////////////////////////////////////
    @FXML
    private TextField txtIn;

    @FXML
    private ListView<msgs> tareaOutput;
    @FXML
    private Button btnChatSend;
    static ChatterStates chatterstates = new ChatterStates();
    List<msgs> list = new ArrayList<>();
    ObservableList<msgs> tDetails;
//////////////////////////////////////////////////////////////////
    ServerMessageReceive srm = new ServerMessageReceive();
    private final int initialState = 0; // initial State
    private final int permissionReqState = 1; // requesting Permission
    private final int permissionAcceptedState = 2; // connection Live
    private final int fileCountSendState = 3; //file count send
    private final int fileNameSendState = 4; //file name send
    private final int fileSizeSendState = 5; //file size send
    private final int filePendingState = 6; // file waiting to send
    private final int fileSendingState = 7; // sending file
    private final int fileSentState = 8; // file sending complete
    private final int operationCompletedState = 9; // transfer complete
    private final int permissionDeniedState = 10; // denied state;

    Set<File> hs = new HashSet<>();
    public static SendStates sendStates = new SendStates();
    FileChooser fileChooser = new FileChooser();
    List<File> selectedFiles = null;
    static ObservableList<File> fileList = FXCollections.observableArrayList();
    public ObservableList<ServerTablePopulate> tableDetails = FXCollections.observableArrayList();
    static SendToPcController sendToPCController = new SendToPcController();
    String fileNames = "";
    String fileSizes = "";
    static int sentFileCount = 0;
    TrayNotification tr = new TrayNotification();
    static FileSize fs = new FileSize();

    ChatServerReceiveTread receiver = new ChatServerReceiveTread();

    @Override
    @SuppressWarnings("Convert2Diamond")
    public void initialize(URL url, ResourceBundle rb) {
//        table.setStyle("-fx-background-color:transparent");
        table.setVisible(false);
        btnSelectFiles.setText("");
        sendStates.fileinfoproperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                Platform.runLater(() -> {
                    lblfileName.setText(fileList.get(sendStates.getfileinfo() - 1).getName());
                    lblSize.setText(String.valueOf(fileList.get(sendStates.getfileinfo() - 1).length() / (1024 * 1024)) + " Mb");
                    lblPath.setText(fileList.get(sendStates.getfileinfo() - 1).getAbsolutePath());
                });
            }
        });

        btnSend.setVisible(false);
        vbox.setVisible(false);
        pane.setBackground(Background.EMPTY);

        chatShow(false);

        tareaOutput.getStylesheets().add(this.getClass().getResource("ListViewStyleChat.css").toExternalForm());
//        receiver.start();

        chatterstates.msgCountProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
//                tareaOutput.appendText("Receiver : " + ChatServerReceiveTread.lkllk() + "\n");
                list.add(new msgs("  " + ChatServerReceiveTread.passMessage() + "  ", 1));
                tDetails = FXCollections.observableList(list);
                tareaOutput.setItems(tDetails);
                if (!txtIn.isVisible()) {
                    chatShow(true);
                    txtIn.requestFocus();
                }

//                tareaOutput.scrollTo(tareaOutput.getItems().size() - 1);
            }
        });
        txtIn.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                btnChatSend.fire();
            }
        });

        /////////////////////////////////////////////////////////////
        receiver.start();

        btnBack.setVisible(false);

        FillProgressIndicator ring = new FillProgressIndicator();
        ring.makeIndeterminate();
        vbox.getChildren().add(ring);

        name.setCellValueFactory(new PropertyValueFactory<ServerTablePopulate, String>("Name"));
        size.setCellValueFactory(new PropertyValueFactory<ServerTablePopulate, String>("Size"));
        path.setCellValueFactory(new PropertyValueFactory<ServerTablePopulate, String>("Path"));
        srm.start();
        btnSend.setDisable(true);
        fs.fileSizeProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
//                Platform.runLater(() -> lblfileName.setText(fileList.get(finfo).getName()));
//                Platform.runLater(() -> lblSize.setText(String.valueOf(fileList.get(finfo).length() / (1024 * 1024))));
//                Platform.runLater(() -> {
//                    try {
//                        lblSize.setText(fileList.get(finfo).getCanonicalPath());
//                    } catch (Exception ex) {
//                        System.err.println(ex);
//                    }
//                });

                try {
                    Platform.runLater(() -> ring.setProgress(1 + (int) (Double.valueOf(newValue.toString()) * 100 / (double) fileList.get(sentFileCount).length())));
                } catch (Exception e) {
                }
            }
        });

        sendStates.fileCountProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                if (sendStates.getFileCount() > 0) {
                    btnSend.setVisible(true);
                    Platform.runLater(() -> btnSend.setDisable(false));
                    tableDetails.clear();
                    try {
                        for (int i = 0; i < fileList.size(); i++) {
                            tableDetails.add(new ServerTablePopulate(fileList.get(i).getName(), String.valueOf((fileList.get(i).length()) / 1024) + " kb", fileList.get(i).getPath()));
                        }
                        table.setItems(tableDetails);
                    } catch (Exception e) {
                    }

                    Platform.runLater(() -> {
                        lblFileCount.setText("File Count : " + fileList.size());
                    });
                }
            }
        });
        sendStates.stateProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                System.out.println(sendStates.getState() + " Listener Check");
                if (sendStates.getState() == permissionAcceptedState) {
                    System.out.println("Permission Accepted (Listener)");
                    sendStates.setState(fileCountSendState);
                    Platform.runLater(() -> {
                        table.setVisible(false);
                        btnSend.setVisible(false);
                        lblfileName.setText(fileList.get(0).getName());
                        lblSize.setText(String.valueOf(fileList.get(0).length() / (1024 * 1024)) + " Mb");
                        lblPath.setText(fileList.get(0).getAbsolutePath());

                        vbox.setVisible(true);
                        tr.setAnimationType(AnimationType.FADE);
                        tr.setTitle("Permission Accepted");
                        tr.showAndDismiss(Duration.seconds(3));
                    });

                }
                if (sendStates.getState() == permissionDeniedState) {
                    System.out.println("Permission denied");
                }
                if (sendStates.getState() == fileCountSendState) {
                    ServerMessageSend.messenger(String.valueOf(sendStates.getFileCount()));
                    return;
                }
                if (sendStates.getState() == fileNameSendState) {
                    ServerMessageSend.messenger(fileNames);
                    ServerMessageSend.messenger("fnend");
                    sendStates.setState(fileSizeSendState);
                    return;
                }
                if (sendStates.getState() == fileSizeSendState) {
                    ServerMessageSend.messenger((fileSizes));
                    ServerMessageSend.messenger(("fsend"));
                    sendStates.setState(fileSendingState);
                    return;
                }
                if (sendStates.getState() == fileSendingState) {
                    Platform.runLater(() -> {
                        Thread sendThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    for (int i = 0; i < fileList.size(); i++) {
                                        FIleSendThread1.setpath(fileList.get(sentFileCount).toString());
//                                    fs.setfileSize(0);
//Platform.runLater(() -> lblFileCount.setText(fileList.get(sentFileCount).getName()));
                                        FIleSendThread1.fileSendThreadRun();
                                        sentFileCount++;

                                    }
                                } catch (Exception e) {
                                }
                            }
                        });
                        sendThread.start();
                    });
                }
                if (sendStates.getState() == fileSentState) {
                    System.out.println("File Sent State");
                }
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
            Pane root = loader.load(getClass().getResource("PcUI.fxml").openStream());
            Scene scene = new Scene(root);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            ErrorWriter.writer(e + " : Send to pc controller -> back");
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
            ErrorWriter.writer(e + " : Send to pc controller -> exit");
        }
    }

    @FXML
    private void selectFiles(ActionEvent event) {
        try {
            selectedFiles = fileChooser.showOpenMultipleDialog(((Node) event.getSource()).getScene().getWindow());
            if (selectedFiles != null) {
                fileList.addAll(selectedFiles);
                hs.addAll(fileList);
                fileList.clear();
                fileList.addAll(hs);
                sendStates.setFileCount(fileList.size());
                sendStates.setFilePath(fileList.get(0).toString());
                sendStates.setFileSize(String.valueOf(fileList.get(0).length()));
                sendStates.setFileName(fileList.get(0).getName());
                table.setVisible(true);
                btnSelectFiles.setLayoutX(80);
                btnSelectFiles.setLayoutY(90);
                btnSelectFiles.setPrefSize(98, 30);
//                btnSelectFiles
//                pane.getChildren().remove(imgadd);
                imgadd.setImage(null);
                imgadd.setFitHeight(0);
                imgadd.setFitWidth(0);
                btnSelectFiles.setStyle("-fx-background-color: black");
                btnSelectFiles.setOpacity(0.63);
                btnSelectFiles.setLayoutX(80);
                btnSelectFiles.setLayoutY(90);
                btnSelectFiles.setPrefSize(98, 30);
                btnSelectFiles.setText("Add");
                
            }

        } catch (Exception e) {
            System.out.println(e);
            ErrorWriter.writer(e + " : Send to pc controller -> select files");
        }
    }

    @FXML
    private void selectFolder(ActionEvent event) {
        table.setItems(tableDetails);
    }

    public void send(ActionEvent e) throws MalformedURLException {

        try {

            for (int i = 0; i < sendStates.getFileCount(); i++) {
                fileNames = fileNames + fileList.get(i).getName() + ",";
                fileSizes = fileSizes + fileList.get(i).length() + ",";
            }
            System.out.println(fileNames);
            System.out.println(fileSizes);
            sendStates.setFileCount(fileList.size());
            System.out.println("File Count = " + fileList.size());
            ServerMessageSend.messenger("permission");
            sendStates.setState(permissionReqState);
            btnSend.setDisable(true);
        } catch (Exception ex) {
            System.out.println(ex);
            ErrorWriter.writer(e + " : Send to pc controller -> send");
        }
    }

    public void handlePermission() {

        System.out.println(" Handle Permission Denied");
    }

    public void handleFileSend() {
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
//            primaryStage.setY(btnBack.getScene().getWindow().getY());
//            primaryStage.initOwner(btnBack.getScene().getWindow());
//            Pane root = loader.load(getClass().getResource("ChatServer.fxml").openStream());
//            Scene scene = new Scene(root);
//            scene.setFill(null);
//            primaryStage.initStyle(StageStyle.TRANSPARENT);
//            primaryStage.setScene(scene);
//            primaryStage.show();
//        } catch (Exception e1) {
//        }
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
            lblmsg.setStyle("-fx-background-radius:5; -fx-background-color: white; -fx-font-size:14pt");
            lblmsg.setTextFill(Color.web("#00ff00"));
            this.getChildren().addAll(lblmsg);
            this.setWidth(200);
        }

    }

    public void sendchat(ActionEvent e) {
        if (txtIn.getLength() > 0) {
            ChatServerSender.messenger(txtIn.getText());
            list.add(new msgs("  " + txtIn.getText() + "  ", 2));
            tDetails = FXCollections.observableList(list);
            tareaOutput.setItems(tDetails);
            tareaOutput.scrollTo(tDetails.size() - 1);
            txtIn.clear();
            txtIn.requestFocus();
        }
    }

    public void chatShow(boolean option) {
//        rect.setVisible(option);
        txtIn.setVisible(option);
        btnChatSend.setVisible(option);
        tareaOutput.setVisible(option);
    }
    
    
      public void minimize(ActionEvent e) {
        Stage primaryStage = (Stage) btnExit.getScene().getWindow();
        primaryStage.setIconified(true);
    }
}
