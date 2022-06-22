package com.example.phaseiv;

import com.example.phaseiv.festivalRunController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class mainController {
    @FXML
    private RadioButton radio1;
    @FXML
    private RadioButton radio2;
    @FXML
    private RadioButton radio3;

    public void continueClicked(ActionEvent event) throws IOException, ParseException {

        if(radio1.isSelected())
            if (radio2.isSelected() | radio3.isSelected())
            {
                Notifications.create() .title("Error") .text("Please Choose One Only!").position(Pos.CENTER) .showWarning();
            }
        else
            {
                Parent root = FXMLLoader.load(getClass().getResource("adding_festival.fxml"));
                Stage s = (Stage)((Node)event.getSource()).getScene().getWindow();
                s.setTitle("add festivalRun");
                s.setScene(new Scene(root, 600, 600));
                s.show();
            }
        else if(radio2.isSelected())
            if (radio1.isSelected() | radio3.isSelected())
            {
                Notifications.create() .title("Error") .text("Please Choose One Only!").position(Pos.CENTER) .showWarning();
            }
            else
            {
                Parent root = FXMLLoader.load(getClass().getResource("adding_concert.fxml"));
                Stage s = (Stage)((Node)event.getSource()).getScene().getWindow();
                s.setTitle("add Concert");
                s.setScene(new Scene(root, 600, 600));
                s.show();
            }
        else if (radio3.isSelected())
            if(radio1.isSelected() | radio2.isSelected())
            {
                Notifications.create() .title("Error") .text("Please Choose One Only!").position(Pos.CENTER) .showWarning();
            }
            else {
                Parent root = FXMLLoader.load(getClass().getResource("statistics.fxml"));
                Stage s = (Stage)((Node)event.getSource()).getScene().getWindow();
                s.setTitle("Statistics");
                s.setScene(new Scene(root, 600, 600));
                s.show();
            }

    }
}
