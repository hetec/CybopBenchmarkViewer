package org.pode.viewer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by patrick on 17.02.16.
 */
public class Viewer extends Application{

    private final static String PYTHON = "Python";
    private final static String JAVA = "Java";
    private final static String C = "C++";
    private final static String CYBOL = "CYBOL";

    private ObservableList<XYChart.Data<String, Number>> obsData;

    private double javaTime = 0;
    private double cTime = 0;
    private double pythonTime = 0;
    private double cybobTime = 0;

    private Label textLabel;
    private Button startScriptBtn;
    private CategoryAxis categoryAxis = new CategoryAxis();
    private NumberAxis numberAxis = new NumberAxis(0, 10 , 10);
    private BarChart<String, Number> barChart;
    private XYChart.Series<String, Number> timeSeries;

    private EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent event) {
            textLabel.setText("HELLO");
            Process proc = null;
            try {
                proc = Runtime.getRuntime()
                        .exec("/Users/patrick/Documents/projects/CYBOP-Benchmark/startBenchmark.sh");
                System.out.println("WAIT ... ");
                proc.waitFor();

                BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                String line = "";
                int lineCounter = 0;
                while((line = br.readLine()) != null){
                    System.out.println(line);
                    if(lineCounter == 0){
                        System.out.println("java");
                        javaTime = Double.parseDouble(line);
                    }
                    if(lineCounter == 1){
                        System.out.println("c");
                        cTime = Double.parseDouble(line);
                    }
                    if(lineCounter == 2){
                        System.out.println("c");
                        pythonTime = Double.parseDouble(line);
                    }
                    lineCounter++;
                }

                System.out.println("FINISHED:");
                System.out.println("JAVA: " + javaTime);
                System.out.println("C++: " + cTime);

                numberAxis.setAutoRanging(true);

                obsData.get(0).setYValue(javaTime);
                obsData.get(1).setYValue(cTime);
                obsData.get(2).setYValue(pythonTime);
                //obsData.get(3).setYValue(cybobTime);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void start(Stage primaryStage) throws Exception {
        final VBox vbox = new VBox();
        startScriptBtn = new Button();
        textLabel = new Label();
        startScriptBtn.setText("Run Benchmark");
        startScriptBtn.setOnAction(handler);
        barChart = new BarChart<>(categoryAxis, numberAxis);
        barChart.setTitle("Benchmark Results");
        categoryAxis.setLabel("Languages");
        numberAxis.setLabel("Time");

        timeSeries = new XYChart.Series<>();
        XYChart.Data dataJava = new XYChart.Data(JAVA, javaTime);
        XYChart.Data dataC = new XYChart.Data(C, cTime);
        XYChart.Data dataPython = new XYChart.Data(PYTHON, pythonTime);
        XYChart.Data dataCybol = new XYChart.Data(CYBOL, cybobTime);
        obsData = FXCollections.observableArrayList();
        obsData.addAll(dataJava, dataC, dataPython, dataCybol);
        timeSeries.setData(obsData);
        StackPane root = new StackPane();
        vbox.getChildren().add(textLabel);
        vbox.getChildren().add(startScriptBtn);
        vbox.getChildren().add(barChart);
        barChart.getData().add(timeSeries);
        root.getChildren().add(vbox);

        Scene scene = new Scene(root);
        primaryStage.setTitle("TEST");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
