package application;

import javafx.application.Application;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Scanner;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.LineChart;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;

public class PartiesApplication extends Application{

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage window) {
        ArrayList<String> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(Paths.get("partiesdata.tsv"))) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        String[] years = lines.get(0).split("\t");
        
        HashMap<String, Map<Integer, Double>> master = new HashMap<>();
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\t");
            HashMap<Integer, Double> secondary = new HashMap<>();
            for (int j = 1; j < parts.length; j++) {               
                    String valueStr = parts[j];
                    if (!valueStr.trim().isEmpty() && !valueStr.equals("-")) {
                        secondary.put(Integer.parseInt(years[j]), Double.parseDouble(valueStr));
                    }
            }
            master.put(parts[0], secondary);
        }
        
        NumberAxis xAxis = new NumberAxis(1968, 2008, 5);
        NumberAxis yAxis = new NumberAxis(0, 30, 5);
        
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Relative support of the parties");
        
        master.keySet().stream().forEach(party -> {
            XYChart.Series<Number, Number> data = new XYChart.Series();
            data.setName(party);
            master.get(party).entrySet().stream().forEach(pair -> {
                data.getData().add(new XYChart.Data(pair.getKey(), pair.getValue()));
            });
            lineChart.getData().add(data);
        });

        Scene view = new Scene(lineChart, 640, 480);
        window.setScene(view);
        window.show();         
    }

}
