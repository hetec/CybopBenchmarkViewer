package org.pode.viewer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.pode.handler.BtnHandler;

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
    private CategoryAxis categoryAxisTime = new CategoryAxis();
    private NumberAxis numberAxisTime = new NumberAxis(0, 10 , 10);
    private CategoryAxis categoryAxisMem = new CategoryAxis();
    private NumberAxis numberAxisMem = new NumberAxis(0, 10 , 10);
    private BarChart<String, Number> barChartTime;
    private BarChart<String, Number> barChartMem;
    private XYChart.Series<String, Number> timeSeries;
    private XYChart.Series<String, Number> memSeries;
    private ProgressBar pb;
    private StackPane progressPane;
    private BorderPane borderPane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Cybol Benchmark");
        primaryStage.setWidth(800);
        primaryStage.setHeight(500);

        borderPane = new BorderPane();
        startScriptBtn = new Button("Start Benchmark");
        pb = new ProgressBar();
        textLabel = new Label();
        barChartTime = new BarChart<>(categoryAxisTime, numberAxisTime);
        barChartMem = new BarChart<>(categoryAxisMem, numberAxisMem);

        initTimeData();
        initMemData();
        initTimeChart(timeSeries);
        initMemChart(memSeries);
        initProgressBar(primaryStage);

        startScriptBtn.setOnAction(
                new BtnHandler(
                        obsData,
                        obsMemData,
                        numberAxisTime,
                        numberAxisMem,
                        progressPane));

        Scene scene = new Scene(buildUi());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Parent buildUi(){
        StackPane root = new StackPane();
        borderPane.setTop(progressPane);
        HBox charts = new HBox();
        charts.setAlignment(Pos.BASELINE_CENTER);
        charts.getChildren().addAll(barChartTime, barChartMem);
        HBox.setMargin(barChartTime, new Insets(10,10,10,10));
        HBox.setMargin(barChartMem, new Insets(10,10,10,10));
        borderPane.setCenter(charts);
        borderPane.setBottom(startScriptBtn);
        BorderPane.setAlignment(startScriptBtn, Pos.BASELINE_CENTER);
        BorderPane.setMargin(progressPane, new Insets(0,0,20,0));
        BorderPane.setMargin(charts, new Insets(0,0,70,0));
        BorderPane.setMargin(startScriptBtn, new Insets(0,0,20,0));
        root.getChildren().add(borderPane);
        return root;
    }

    private void initProgressBar(Stage primaryStage){
        pb.setMinHeight(30);
        pb.prefWidthProperty().bind(primaryStage.widthProperty());
        progressPane = new StackPane();
        progressPane.getChildren().addAll(pb, textLabel);
        progressPane.setVisible(false);

    }

    private void initChart(
            BarChart<String, Number> barChart,
            XYChart.Series<String, Number> series,
            String title){
        barChart.setTitle(title);
        barChart.getData().add(series);
        barChart.setLegendVisible(false);
    }

    private void initAxis(
            NumberAxis numberAxis,
            CategoryAxis catAxis,
            String numberAxisTitle,
            String catAxisTitle){
        numberAxis.setLabel(numberAxisTitle);
        catAxis.setLabel(catAxisTitle);
    }

    private void initTimeChart(XYChart.Series<String, Number> series){
        initChart(barChartTime, timeSeries, "Time");
        initAxis(numberAxisTime, categoryAxisTime, "Time in s", "Languages");
    }

    private void initMemChart(XYChart.Series<String, Number> series){
        initChart(barChartMem, memSeries, "Memory");
        initAxis(numberAxisMem, categoryAxisMem, "Memory in MB", "Languages");
    }

    private void initTimeData(){
        timeSeries = buildSeries();
        timeSeries.setName("Time");
        obsData = timeSeries.getData();
    }

    private void initMemData(){
        memSeries = buildSeries();
        memSeries.setName("Memory");
        obsMemData = memSeries.getData();
    }

    private XYChart.Series<String, Number> buildSeries(){
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        XYChart.Data<String, Number> dataJava = new XYChart.Data<>(JAVA, 0);
        XYChart.Data<String, Number> dataC = new XYChart.Data<>(C, 0);
        XYChart.Data<String, Number> dataPython = new XYChart.Data<>(PYTHON, 0);
        XYChart.Data<String, Number> dataCybol = new XYChart.Data<>(CYBOL, 0);
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

