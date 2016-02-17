package org.pode.handler;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by patrick on 17.02.16.
 */
public class BtnHandler implements EventHandler<ActionEvent>{

    private double javaTime = 0;
    private double cTime = 0;
    private double pythonTime = 0;
    private double cybobTime = 0;

    private ObservableList<XYChart.Data<String, Number>> obsData;
    private NumberAxis axis;
    private Label stateLabel;

    public BtnHandler(ObservableList<XYChart.Data<String, Number>> obsData,
                      NumberAxis axis,
                      Label stateLabel){
        this.obsData = obsData;
        this.axis = axis;
        this.stateLabel = stateLabel;
    }

    @Override
    public void handle(ActionEvent event) {
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

            axis.setAutoRanging(true);

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

    public ObservableList<XYChart.Data<String, Number>> getObsData() {
        return obsData;
    }

    public void setObsData(ObservableList<XYChart.Data<String, Number>> obsData) {
        this.obsData = obsData;
    }
}
