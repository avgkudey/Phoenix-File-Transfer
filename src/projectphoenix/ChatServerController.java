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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author KASUN
 */
public class ChatServerController implements Initializable {

    static ChatterStates chatterstates = new ChatterStates();
    @FXML
    private AnchorPane txtInput;
    @FXML
    private ListView<msgs> tareaOutput;
    @FXML
    private Button btnSend;
    @FXML
    private TextField txtIn;

//    ChatServerReceiveTread receiver = new ChatServerReceiveTread();
    List<msgs> list = new ArrayList<>();
    ObservableList<msgs> tDetails;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        tareaOutput.getStylesheets().add(RingProgressIndicator.class.getResource("ChatViewStyle.css").toExternalForm());
        tareaOutput.getStylesheets().add(this.getClass().getResource("ListViewStyleChat.css").toExternalForm());
        txtIn.requestFocus();
//        receiver.start();

        chatterstates.msgCountProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
//                tareaOutput.appendText("Receiver : " + ChatServerReceiveTread.lkllk() + "\n");
                list.add(new msgs("  " + ChatServerReceiveTread.passMessage() + "  ", 1));
                tDetails = FXCollections.observableList(list);
                tareaOutput.setItems(tDetails);
//                tareaOutput.scrollTo(tareaOutput.getItems().size() - 1);
            }
        });
        txtIn.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                btnSend.fire();
            }
        });
    }

    public void send(ActionEvent event) {
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
            lblmsg.setStyle("-fx-background-radius:5; -fx-background-color: white; -fx-font-size:16pt");
            lblmsg.setTextFill(Color.web("#0000ff"));
            this.setAlignment(Pos.BASELINE_LEFT);
            this.getChildren().addAll(lblmsg);
            this.setWidth(200);
        }

        public void inco(String msg) {
            Label lblmsg = new Label(msg);
            this.setAlignment(Pos.BASELINE_RIGHT);
//            lblmsg.setStyle("-fx-border-color: brown");
            lblmsg.setStyle("-fx-background-radius:5; -fx-background-color: white; -fx-font-size:16pt");
            lblmsg.setTextFill(Color.web("#00ff00"));
            this.getChildren().addAll(lblmsg);
            this.setWidth(200);
        }

    }
    
    public void sendchat(ActionEvent e){
        System.err.println(",,;,;,;,lnjbk");
    }
}
