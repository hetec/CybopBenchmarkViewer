package main.handler;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;

/**
 * Created by patrick on 17.02.16.
 */
public class BtnHandler implements EventHandler<ActionEvent> {

    private ObservableList<XYChart.Data<String, Number>> obsData;
    private ObservableList<XYChart.Data<String, Number>> obsMemData;
    private NumberAxis axisTime;
    private NumberAxis axisMem;
    private ProgressBar pb;
    private Label textLabel;
    
    private String scriptPath;
    private String programPath;
    private int runTimes;
    
    public BtnHandler(ObservableList<XYChart.Data<String, Number>> obsData,
                      ObservableList<XYChart.Data<String, Number>> obsMemData,
                      NumberAxis axisTime,
                      NumberAxis axisMem,
                      ProgressBar pb,
                      Label textLabel,
                      String scriptPath,
                      String programPath,
                      int runTimes) {
        this.obsData = obsData;
        this.obsMemData = obsMemData;
        this.axisTime = axisTime;
        this.axisMem = axisMem;
        this.pb = pb;
        this.textLabel = textLabel;
        this.scriptPath = scriptPath;
        this.programPath = programPath;
        this.runTimes = runTimes;
    }

    @Override
    public void handle(ActionEvent event) {
    	this.pb.setVisible(true);
        DataService w = new DataService(this.obsData, this.obsMemData, this.axisTime, this.axisMem, this.scriptPath, this.programPath, this.runTimes);
        this.textLabel.textProperty().bind(w.messageProperty());
        w.restart();
        w.setOnSucceeded((e) -> {
            this.textLabel.textProperty().unbind();
            installTooltip();
            this.pb.setVisible(false);
        });
        
    }

    private void installTooltip() {
        for (XYChart.Data<String, Number> data : obsData) {
        	String formattedData = String.format("%.2f Sec", data.getYValue());
            Tooltip.install(data.getNode(), new Tooltip(formattedData));
            setColor(data.getNode());
        }
        for (XYChart.Data<String, Number> data : obsMemData) {
        	Double dataValueByte = (data.getYValue().doubleValue()) * 1000 * 1000;
            Tooltip.install(data.getNode(), new Tooltip(dataValueByte + " Byte"));
            setColor(data.getNode());
        }
    }

    private void setColor(Node node) {
        node.setStyle("-fx-bar-fill:dodgerblue");
    }
}
