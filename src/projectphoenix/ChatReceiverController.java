package projectphoenix;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class ChatReceiverController implements Initializable {

    @FXML
    private AnchorPane pane;
    @FXML
    private ListView<msgs> tareaOutput;
    @FXML
    private Button btnSend;
    @FXML
    private TextField txtIn;
    
    
    

//    ChatReceiverThread receiver = new ChatReceiverThread();
    static ChatterStates chatterstates = new ChatterStates();
    List<msgs> list = new ArrayList<>();
    ObservableList<msgs> tdetails;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pane.setBackground(Background.EMPTY);
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
                return;

            }
        });
    }

    public void send(ActionEvent e) {
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

        public msgs(String labelText, int selector) {

            if (selector == 1) {
                outgoing(labelText);
            } else {
                inco(labelText);
            }
        }

        public void outgoing(String msg) {
            
            Label lblmsg = new Label(msg);
            lblmsg.setStyle("-fx-background-radius:5; -fx-background-color: white; -fx-font-size:16pt");
            lblmsg.setTextFill(Color.web("#0000ff"));
            this.setAlignment(Pos.BASELINE_LEFT);
            HBox.setHgrow(lblmsg, Priority.SOMETIMES);
            this.getChildren().addAll(lblmsg);
            this.setWidth(200);
            this.requestFocus();
        }

        public void inco(String msg) {
            Label lblmsg = new Label(msg);
            this.setAlignment(Pos.BASELINE_RIGHT);
//            lblmsg.setStyle("-fx-border-color: brown;-fx-font-size:14");
            lblmsg.setStyle("-fx-background-radius:5; -fx-background-color: white; -fx-font-size:16pt");
            lblmsg.setTextFill(Color.web("#00ff00"));
            HBox.setHgrow(lblmsg, Priority.ALWAYS);
            this.requestFocus();
            this.getChildren().addAll(lblmsg);
            this.setWidth(200);
        }

    }
}
