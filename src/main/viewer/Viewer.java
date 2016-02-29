package main.viewer;

import java.util.Map;

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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.handler.BtnHandler;

/**
 * Created by patrick on 17.02.16.
 */
public class Viewer extends Application {

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
    private BorderPane borderPane;
    
    private String scriptPath;
    private String programPath;
    private int runTimes;

    @Override
    public void init() throws Exception {
    	super.init();
    	
    	scriptPath = "";
    	programPath = "";
    	runTimes = 0;
    	
    	Parameters parameters = getParameters();
    	Map<String, String> namedParameters = parameters.getNamed();

    	for (Map.Entry<String, String> entry : namedParameters.entrySet()) {
    		if ("scriptpath".equals(entry.getKey())) {
    			System.out.println(entry.getKey() + " : " + entry.getValue());
    			scriptPath = entry.getValue();
    		}
    		if ("programpath".equals(entry.getKey())) {
    			System.out.println(entry.getKey() + " : " + entry.getValue());
    			programPath = entry.getValue();
    		}
    		if ("runtimes".equals(entry.getKey())) {
    			System.out.println(entry.getKey() + " : " + entry.getValue());
    			runTimes = Integer.valueOf(entry.getValue());
    		}
    	}
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
    	 	
        primaryStage.setTitle("Cybop Benchmark");
        primaryStage.setWidth(800);
        primaryStage.setHeight(500);

        borderPane = new BorderPane();
        startScriptBtn = new Button("Start Benchmark");
        pb = new ProgressBar();
        textLabel = new Label();
        textLabel.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 20));
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
                        pb,
                        textLabel,
                        scriptPath,
                        programPath,
                        runTimes)
                );

        Scene scene = new Scene(buildUi());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Parent buildUi() {
        StackPane root = new StackPane();
        
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.BASELINE_CENTER);
        vbox.getChildren().add(pb);
        vbox.getChildren().add(textLabel);
         
        HBox charts = new HBox();
        charts.setAlignment(Pos.BASELINE_CENTER);
        charts.getChildren().addAll(barChartTime, barChartMem);
        
        HBox.setMargin(barChartTime, new Insets(10,10,10,10));
        HBox.setMargin(barChartMem, new Insets(10,10,10,10));
        
        borderPane.setTop(vbox);
        borderPane.setCenter(charts);
        borderPane.setBottom(startScriptBtn);
        
        BorderPane.setAlignment(startScriptBtn, Pos.BASELINE_CENTER);
        
        BorderPane.setMargin(pb, new Insets(0,0,20,0));
        BorderPane.setMargin(textLabel, new Insets(50, 0, 50, 0));
        BorderPane.setMargin(charts, new Insets(30,0,40,0));
        BorderPane.setMargin(startScriptBtn, new Insets(0,0,20,0));
        
        root.getChildren().add(borderPane);
        
        return root;
    }

    private void initProgressBar(Stage primaryStage) {
        pb.setMinHeight(30);
        pb.prefWidthProperty().bind(primaryStage.widthProperty());
        pb.setVisible(false);
    }

    private void initChart (
            BarChart<String, Number> barChart,
            XYChart.Series<String, Number> series,
            String title) {
        barChart.setTitle(title);
        barChart.getData().add(series);
        barChart.setLegendVisible(false);
    }

    private void initAxis (
            NumberAxis numberAxis,
            CategoryAxis catAxis,
            String numberAxisTitle,
            String catAxisTitle) {
        numberAxis.setLabel(numberAxisTitle);
        catAxis.setLabel(catAxisTitle);
    }

    private void initTimeChart(XYChart.Series<String, Number> series) {
        initChart(barChartTime, timeSeries, "Time");
        initAxis(numberAxisTime, categoryAxisTime, "Time in s", "Languages");
    }

    private void initMemChart(XYChart.Series<String, Number> series) {
        initChart(barChartMem, memSeries, "Memory");
        initAxis(numberAxisMem, categoryAxisMem, "Memory in MB", "Languages");
    }

    private void initTimeData() {
        timeSeries = buildSeries();
        timeSeries.setName("Time");
        obsData = timeSeries.getData();
    }

    private void initMemData() {
        memSeries = buildSeries();
        memSeries.setName("Memory");
        obsMemData = memSeries.getData();
    }

    private XYChart.Series<String, Number> buildSeries() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        XYChart.Data<String, Number> dataJava = new XYChart.Data<>(JAVA, 0);
        XYChart.Data<String, Number> dataC = new XYChart.Data<>(C, 0);
        XYChart.Data<String, Number> dataPython = new XYChart.Data<>(PYTHON, 0);
        XYChart.Data<String, Number> dataCybol = new XYChart.Data<>(CYBOL, 0);
        series.setData(observeData(dataPython,dataJava,dataC,dataCybol));
        return series;
    }

    private ObservableList<XYChart.Data<String, Number>> observeData (
            XYChart.Data dataJava,
            XYChart.Data dataC,
            XYChart.Data dataPython,
            XYChart.Data dataCybol) {
        ObservableList<XYChart.Data<String, Number>> obs = FXCollections.observableArrayList();
        obs.addAll(dataJava, dataC, dataPython, dataCybol);
        return obs;
    }
    
    
    public static void main(String[] args) {
		Application.launch(args);
	}
}

