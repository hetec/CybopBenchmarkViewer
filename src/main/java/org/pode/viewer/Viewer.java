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
import org.pode.handler.BtnHandler;

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

    private Label textLabel;
    private Button startScriptBtn;
    private CategoryAxis categoryAxis = new CategoryAxis();
    private NumberAxis numberAxis = new NumberAxis(0, 10 , 10);
    private BarChart<String, Number> barChart;
    private XYChart.Series<String, Number> timeSeries;

    private BtnHandler handler = new BtnHandler(obsData, numberAxis, textLabel);

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
        XYChart.Data dataJava = new XYChart.Data(JAVA, 0);
        XYChart.Data dataC = new XYChart.Data(C, 0);
        XYChart.Data dataPython = new XYChart.Data(PYTHON, 0);
        XYChart.Data dataCybol = new XYChart.Data(CYBOL, 0);
        obsData = FXCollections.observableArrayList();
        obsData.addAll(dataJava, dataC, dataPython, dataCybol);
        handler.setObsData(obsData);
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
