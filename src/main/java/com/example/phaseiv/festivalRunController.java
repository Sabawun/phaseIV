package com.example.phaseiv;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class festivalRunController {
    @FXML
    private TextField fRunId;
    @FXML
    private TextField fRunDuration;
    @FXML
    private TextField fRunDate;
    @FXML
    private TextField fRunTime;
    @FXML
    private ComboBox fBox;
    @FXML
    public void initialize() throws  IOException, ParseException
    {
        HttpURLConnection connection = (HttpURLConnection)
                new URL("http://localhost:8080/getallfestivals").openConnection();
        connection.setRequestMethod("GET");
        String response = "";
        if (connection.getResponseCode() == 200){
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine())
                response += scanner.nextLine();
            scanner.close();
        }
        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray) parser.parse(response);
        for(int i=0; i<array.size();i++) {
            String record = "";
            JSONObject object = null;
            try {
                object = (JSONObject) array.get(i);
                record += object.get("festivalId") + " " + object.get("festivalName") + " " + object.get("cityName");
                fBox.getItems().add(object.get("festivalId") + " - " + object.get("festivalName"));
            } catch (Exception ignored) {
                ;
            }
        }
    }
    public void backClicked(ActionEvent event) throws  IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("main_window.fxml"));
        Stage s = (Stage)((Node)event.getSource()).getScene().getWindow();
        s.setTitle("Main Window");
        s.setScene(new Scene(root, 600, 600));
        s.show();
    }
    public void submitClicked(ActionEvent event) throws IOException
    {
        System.out.print("Helll");
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/addfestivalrun").openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json; utf-8");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        JSONObject festivalRun = new JSONObject();
        festivalRun.put("festivalRunDuration", fRunDuration.getText());
        festivalRun.put("festivalRunTime", Integer.parseInt(fRunTime.getText()));
        festivalRun.put("festivalRunDate", fRunDate.getText());
        int festId = Character.getNumericValue(fBox.getValue().toString().charAt(0));
        JSONObject fest = new JSONObject();
        fest.put("festivalId", festId);
        festivalRun.put("festival", fest);

        try(OutputStream os = connection.getOutputStream()){
            byte[] input = festivalRun.toJSONString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        String response = "";
        int responsecode = connection.getResponseCode();
        if(responsecode == 200){
            Scanner scanner = new Scanner(connection.getInputStream());
            while(scanner.hasNextLine()){
                response += scanner.nextLine();
            }
            scanner.close();
        }
    }
}

