/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectphoenix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class SettingsController implements Initializable {

    @FXML
    private TextField txtloc;
    @FXML
    private Rectangle btntwitter;
    @FXML
    private Button btnbrs;
    @FXML
    private Button btnsave;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Public\\settings.txt"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                txtloc.setText(line);
                line = br.readLine();
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void loc(ActionEvent e) {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        primaryStage.initOwner(btnbrs.getScene().getWindow());
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(primaryStage);
        System.err.println(selectedDirectory);
        txtloc.setText(selectedDirectory.getPath());
        System.err.println(selectedDirectory.getPath());
    }

    public void save(ActionEvent e) {

        try {
            PrintWriter writer1 = new PrintWriter("C:\\Users\\Public\\settings.txt");
            writer1.print("");
            writer1.close();
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("C:\\Users\\Public\\settings.txt", true), "UTF-8"));
            BufferedWriter buffer = new BufferedWriter(writer);
            buffer.write(txtloc.getText());
            buffer.close();
            ((Node) e.getSource()).getScene().getWindow().hide();
        } catch (Exception e2) {
            System.err.println(e);
        }
    }
}
