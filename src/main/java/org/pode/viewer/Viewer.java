package org.pode.viewer;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
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
    private CategoryAxis categoryAxis = new CategoryAxis();
    private NumberAxis numberAxis = new NumberAxis(0, 10 , 10);
    private BarChart<String, Number> barChart;
    private XYChart.Series<String, Number> timeSeries;
    private XYChart.Series<String, Number> memSeries;
    private ProgressBar pb;
    private StackPane progressPane;
    private BorderPane borderPane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        borderPane = new BorderPane();
        startScriptBtn = new Button("Start Benchmark");
        pb = new ProgressBar();
        textLabel = new Label();
        barChart = new BarChart<>(categoryAxis, numberAxis);

        initTimeData();
        initMemData();
        initChart(timeSeries, memSeries);
        initProgressBar();

        startScriptBtn.setOnAction(new BtnHandler(obsData, numberAxis, progressPane));

        Scene scene = new Scene(buildUi());
        primaryStage.setTitle("Cybol Benchmark");
        primaryStage.setWidth(800);
        primaryStage.setHeight(500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Parent buildUi(){
        StackPane root = new StackPane();
        borderPane.setTop(progressPane);
        borderPane.setCenter(barChart);
        borderPane.setBottom(startScriptBtn);
        BorderPane.setAlignment(startScriptBtn, Pos.BASELINE_CENTER);
        BorderPane.setMargin(progressPane, new Insets(0,0,50,0));
        BorderPane.setAlignment(barChart, Pos.BASELINE_CENTER);
        BorderPane.setMargin(barChart, new Insets(0,0,50,0));
        BorderPane.setMargin(startScriptBtn, new Insets(0,0,50,0));
        root.getChildren().add(borderPane);
        return root;
    }

    private void initProgressBar(){
        pb.setMinWidth(800);
        pb.setMinHeight(30);
        progressPane = new StackPane();
        progressPane.getChildren().addAll(pb, textLabel);
        progressPane.setVisible(false);
    }

    private void initChart(XYChart.Series<String, Number> ...series){
        barChart.setTitle("Benchmark Results");
        barChart.setMaxWidth(700);
        categoryAxis.setLabel("Languages");
        numberAxis.setLabel("Time in ms");
        for(XYChart.Series<String, Number> s : series){
            barChart.getData().add(s);
        }
    }

    private void initTimeData(){
        timeSeries = buildSeries();
        obsData = timeSeries.getData();
    }

    private void initMemData(){
        memSeries = buildSeries();
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

