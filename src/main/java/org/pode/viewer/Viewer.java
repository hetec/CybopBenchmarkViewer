package org.pode.viewer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.pode.handler.BtnHandler;

import javax.swing.*;
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
    private ObservableList<XYChart.Data<String, Number>> obsMemData;

    private Label textLabel;
    private Button startScriptBtn;
    private CategoryAxis categoryAxis = new CategoryAxis();
    private NumberAxis numberAxis = new NumberAxis(0, 10 , 10);
    private BarChart<String, Number> barChart;
    private XYChart.Series<String, Number> timeSeries;
    private XYChart.Series<String, Number> memSeries;
    private ProgressBar pb;
    private StackPane progressPane;
    private BtnHandler handler = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        final VBox vbox = new VBox();
        startScriptBtn = new Button();
        pb = new ProgressBar();
        textLabel = new Label();
        startScriptBtn.setText("Run Benchmark");
        barChart = new BarChart<>(categoryAxis, numberAxis);
        barChart.setTitle("Benchmark Results");
        barChart.setMaxWidth(700);
        categoryAxis.setLabel("Languages");
        numberAxis.setLabel("Time");

        initTimeData();
        initMemData();

        StackPane root = new StackPane();

        pb.setVisible(true);
        pb.setMinWidth(800);
        pb.setMinHeight(30);
        progressPane = new StackPane();
        progressPane.getChildren().addAll(pb, textLabel);
        progressPane.setVisible(false);

        handler = new BtnHandler(obsData, numberAxis, progressPane);
        startScriptBtn.setOnAction(handler);

        vbox.getChildren().add(progressPane);
        vbox.getChildren().add(startScriptBtn);
        vbox.getChildren().add(barChart);
        vbox.setAlignment(Pos.CENTER);
        vbox.setMargin(startScriptBtn, new Insets(0,0,40,0));
        barChart.getData().add(timeSeries);

        root.getChildren().add(vbox);

        Scene scene = new Scene(root);
        primaryStage.setTitle("TEST");
        primaryStage.setWidth(800);
        primaryStage.setHeight(500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initTimeData(){
        timeSeries = buildSeries();
        obsData = timeSeries.getData();
    }

    private void initMemData(){
        memSeries = new XYChart.Series<>();
        XYChart.Data dataMemJava = new XYChart.Data(JAVA, 0);
        XYChart.Data dataMemC = new XYChart.Data(C, 0);
        XYChart.Data dataMemPython = new XYChart.Data(PYTHON, 0);
        XYChart.Data dataMemCybol = new XYChart.Data(CYBOL, 0);
        obsMemData = FXCollections.observableArrayList();
        obsMemData.addAll(dataMemJava, dataMemC, dataMemPython, dataMemCybol);
        timeSeries.setData(obsData);
    }

    private XYChart.Series<String, Number> buildSeries(){
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        XYChart.Data dataJava = new XYChart.Data(JAVA, 0);
        XYChart.Data dataC = new XYChart.Data(C, 0);
        XYChart.Data dataPython = new XYChart.Data(PYTHON, 0);
        XYChart.Data dataCybol = new XYChart.Data(CYBOL, 0);
        series.setData(observeData(dataJava,dataC,dataPython,dataCybol));
        return series;
    }

    private ObservableList<XYChart.Data<String, Number>> observeData(
            XYChart.Data dataJava,
            XYChart.Data dataC,
            XYChart.Data dataPython,
            XYChart.Data dataCybol){
        ObservableList<XYChart.Data<String, Number>> obs = FXCollections.observableArrayList();
        obs.addAll(dataJava, dataC, dataPython, dataCybol);
        return obs;
    }
}

