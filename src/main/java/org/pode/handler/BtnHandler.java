package org.pode.handler;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by patrick on 17.02.16.
 */
public class BtnHandler implements EventHandler<ActionEvent>{

    private ObservableList<XYChart.Data<String, Number>> obsData;
    private NumberAxis axis;
    private StackPane progressPane;

    public BtnHandler(ObservableList<XYChart.Data<String, Number>> obsData,
                      NumberAxis axis,
                      StackPane progressPane){
        this.obsData = obsData;
        this.axis = axis;
        this.progressPane = progressPane;
    }

    @Override
    public void handle(ActionEvent event) {
        this.progressPane.setVisible(true);
        DataService w = new DataService(this.obsData,this.axis);
        Label label = (Label) progressPane.getChildren().get(1);
        label.textProperty().bind(w.messageProperty());
        w.restart();
        w.setOnSucceeded((e) -> {
            label.textProperty().unbind();
            installTooltip();
            progressPane.setVisible(false);
        });
    }

    private void installTooltip(){
        for(XYChart.Data<String, Number> data : obsData){
            Tooltip.install(data.getNode(), new Tooltip(data.getYValue() + ""));
        }
    }


}
