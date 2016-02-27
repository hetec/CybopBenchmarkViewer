package main.handler;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
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
    private StackPane progressPane;
    
    private String scriptPath;
    private String programPath;
    private int runTimes;
    
    public BtnHandler(ObservableList<XYChart.Data<String, Number>> obsData,
                      ObservableList<XYChart.Data<String, Number>> obsMemData,
                      NumberAxis axisTime,
                      NumberAxis axisMem,
                      StackPane progressPane,
                      String scriptPath,
                      String programPath,
                      int runTimes) {
        this.obsData = obsData;
        this.obsMemData = obsMemData;
        this.axisTime = axisTime;
        this.axisMem = axisMem;
        this.progressPane = progressPane;
        this.scriptPath = scriptPath;
        this.programPath = programPath;
        this.runTimes = runTimes;
    }

    @Override
    public void handle(ActionEvent event) {
        this.progressPane.setVisible(true);
        DataService w = new DataService(this.obsData, this.obsMemData, this.axisTime, this.axisMem, this.scriptPath, this.programPath, this.runTimes);
        Label label = (Label) progressPane.getChildren().get(1);
        label.textProperty().bind(w.messageProperty());
        w.restart();
        w.setOnSucceeded((e) -> {
            label.textProperty().unbind();
            installTooltip();
            progressPane.setVisible(false);
        });
    }

    private void installTooltip() {
        for (XYChart.Data<String, Number> data : obsData) {
            Tooltip.install(data.getNode(), new Tooltip(data.getYValue() + ""));
            setColor(data.getNode());
        }
        for (XYChart.Data<String, Number> data : obsMemData) {
            Tooltip.install(data.getNode(), new Tooltip(data.getYValue() + ""));
            setColor(data.getNode());
        }
    }

    private void setColor(Node node) {
        node.setStyle("-fx-bar-fill:dodgerblue");
    }
}
