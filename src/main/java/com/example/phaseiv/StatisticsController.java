package com.example.phaseiv;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class StatisticsController {
    @FXML
    private CheckBox longestconcerts;
    @FXML
    private CheckBox popularfestivals;
    @FXML
    private ListView listconcert;
    @FXML
    private ListView listfestival;

    public void ShowPressed(ActionEvent event) throws IOException, ParseException {
        listconcert.getItems().clear();
        listfestival.getItems().clear();
        if (longestconcerts.isSelected()) {
            HttpURLConnection connection = (HttpURLConnection)
                    new URL("http://localhost:8080/longestconcerts").openConnection();
            connection.setRequestMethod("GET");
            String response = "";
            if (connection.getResponseCode() == 200) {
                Scanner scanner = new Scanner(connection.getInputStream());
                while (scanner.hasNextLine())
                    response += scanner.nextLine();
                scanner.close();
            }
            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(response);
            for (int i = 0; i < array.size(); i++) {
                JSONObject object = null;
                try {
                    object = (JSONObject) array.get(i);
                    listconcert.getItems().add("ID : " + object.get("eventId"));
                    listconcert.getItems().add("Date : " + object.get("date"));
                    listconcert.getItems().add("Duration : " + object.get("duration"));
                    listconcert.getItems().add("Description : " + object.get("description"));
                    listconcert.getItems().add("Performer Name : " + object.get("performerName"));
                } catch (Exception ignored) {
                    ;
                }
            }
        }
            if (popularfestivals.isSelected())
        {

                HttpURLConnection connection2 = (HttpURLConnection)
                        new URL("http://localhost:8080/popularfestivals").openConnection();
                connection2.setRequestMethod("GET");
                String response2 = "";
                if (connection2.getResponseCode() == 200) {
                    Scanner scanner = new Scanner(connection2.getInputStream());
                    while (scanner.hasNextLine())
                        response2 += scanner.nextLine();
                    scanner.close();
                }
                JSONParser parser = new JSONParser();
                JSONArray array = (JSONArray) parser.parse(response2);
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = null;
                    try {
                        object = (JSONObject) array.get(i);
                        listfestival.getItems().add("ID : " + object.get("festivalId"));
                        listfestival.getItems().add("Name : " + object.get("festivalName"));
                        listfestival.getItems().add("City : " + object.get("cityName"));
                    } catch (Exception ignored) {
                        ;
                    }
            }
        }
    }
    public void BackPressed(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("main_window.fxml"));
        Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
        s.setTitle("Main Window");
        s.setScene(new Scene(root, 600, 600));
        s.show();
    }

}

