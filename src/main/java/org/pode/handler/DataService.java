package org.pode.handler;

import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by patrick on 17.02.16.
 */
public class DataService extends Service<Void> {

    private double javaTime = 0;
    private double cTime = 0;
    private double pythonTime = 0;
    private double cybobTime = 0;

    private ObservableList<XYChart.Data<String, Number>> obsData;
    private NumberAxis axis;

    public DataService(ObservableList<XYChart.Data<String, Number>> obsData,
                      NumberAxis axis){
        this.obsData = obsData;
        this.axis = axis;
    }

    private void setObsData(){
        obsData.get(0).setYValue(javaTime);
        obsData.get(1).setYValue(cTime);
        obsData.get(2).setYValue(pythonTime);
        obsData.get(3).setYValue(cybobTime);
    }

    private void readTimes(Process proc) throws IOException {
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
                System.out.println("python");
                pythonTime = Double.parseDouble(line);
            }
            if(lineCounter == 3){
                System.out.println("cybol");
                cybobTime = Double.parseDouble(line);
            }
            lineCounter++;
        }

        System.out.println("FINISHED");
    }

    private Process runScript() throws IOException, InterruptedException {
        Process proc = null;
        proc = Runtime.getRuntime()
                .exec("/Users/patrick/Documents/projects/CYBOP-Benchmark/startBenchmark.sh");
        System.out.println("WAIT ... ");
        proc.waitFor();
        return proc;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            public Void call() {
                System.out.println("Benchmark is running ...");
                updateMessage("Benchmark is running ...");
                try {
                    readTimes(runScript());
                    axis.setAutoRanging(true);
                    setObsData();
                    updateMessage("Benchmark finished");
                } catch (IOException e) {
                    updateMessage(e.getMessage());
                } catch (InterruptedException e) {
                    updateMessage(e.getMessage());
                }
                return null;
            }
        };
    }


}

