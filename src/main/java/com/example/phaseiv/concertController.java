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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class concertController {

    @FXML
    private ComboBox Festival_combo;
    @FXML
    private ComboBox Festival_run_combo;
    @FXML
    private TextField concert_id;
    @FXML
    private TextField concert_date;
    @FXML
    private TextField concert_duration;
    @FXML
    private TextField concert_description;
    @FXML
    private TextField concert_performer;
    @FXML
    public void initialize() throws  IOException, ParseException
    {
        listFestivals();
    }
    public void listFestivals() throws  IOException, ParseException
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
                Festival_combo.getItems().add(object.get("festivalId") + " - " + object.get("festivalName"));
            } catch (Exception ignored) {
            }
        }
    }
    public void afterFest(ActionEvent event) throws  IOException, ParseException
    {
        int festId = Character.getNumericValue(Festival_combo.getValue().toString().charAt(0));
        System.out.print(festId);
        HttpURLConnection connection = (HttpURLConnection)
                new URL("http://localhost:8080/getallfestivalruns/"+festId).openConnection();
        connection.setRequestMethod("GET");
        String response2 = "";
        if (connection.getResponseCode() == 200) {
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine())
                response2 += scanner.nextLine();
            scanner.close();
        }
        System.out.print(response2);
        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray) parser.parse(response2);
        for(int i=0; i<array.size();i++) {
            String record = "";
            JSONObject object = null;
            try {
                object = (JSONObject) array.get(i);
                Festival_run_combo.getItems().add(object.get("festivalRunId"));
            } catch (Exception ignored) {
            }
        }

    }
    public void onClickBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main_window.fxml"));
        Stage s = (Stage)((Node)event.getSource()).getScene().getWindow();
        s.setTitle("Main Window");
        s.setScene(new Scene(root, 600, 600));
        s.show();
    }

    public  void onSubmit(ActionEvent event) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/addconcert").openConnection();
                connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json; utf-8");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        JSONObject concert = new JSONObject();
        concert.put("performerName",concert_performer.getText());
        JSONObject festrun = new JSONObject();
        int festivalrunId = Character.getNumericValue(Festival_run_combo.getValue().toString().charAt(0));
        festrun.put("festivalRunId", festivalrunId);
        JSONObject event1 = new JSONObject();
        concert.put("festivalRun", festrun);
        concert.put("eventId", Integer.parseInt(concert_id.getText()));
        concert.put("date",concert_date.getText());
        concert.put("duration", Integer.parseInt(concert_duration.getText()));
        concert.put("description",concert_description.getText());
        //concert.put("eventId", Integer.parseInt(concert_id.getText()));
        try(OutputStream os = connection.getOutputStream()){
            byte[] input = concert.toJSONString().getBytes(StandardCharsets.UTF_8);
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