/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yt.main;

import com.yt.parse.ParseOperation;
import com.yt.thread.WorkerThread;
import com.yt.util.SharedData;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Yt_ExtractServer {

    static int counterGetServerSettings = 1000;
    static boolean flagGetserverSettings = false;

    public static void main(String[] args) {
        ParseOperation.IniParse();

        // Make the storage directory
        new File(SharedData.RootStoragePath).mkdir();
        ParseOperation.ClearAllQueueExecutioners();

        // Threading logic that runs infinitely
        SharedData.Cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(SharedData.Cores);

        // Save the server settings on starting, only once;
        ParseOperation.UploadServerSettings();

        // When this is 0, get settings.
        counterGetServerSettings = SharedData.LogServerGetSettingsInterval / SharedData.LogServerPerformanceInterval;

        // Start the server logger
        try {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    ParseOperation.LogServerData();
                    SharedData.TaskCompletedCount = 0;

                    SharedData.TaskIdleCount = 0;

                    if (counterGetServerSettings <= 0) {
                        ParseOperation.GetServerSettings();
                        counterGetServerSettings = SharedData.LogServerGetSettingsInterval / SharedData.LogServerPerformanceInterval;

                    } else {
                        counterGetServerSettings--;
                    }
                }
            }, 0, SharedData.LogServerPerformanceInterval);
        } catch (Exception er) {

        }
        // Counter that launches the get job and execute queue. 
        int i = 0;
        while (true) {
            long startTime = System.nanoTime();

            Runnable worker = new WorkerThread(i);
            executor.execute(worker);
            try {
                Thread.sleep(SharedData.WaitBeforeGetNextWork);
                i++;
                if (i == Integer.MAX_VALUE) {
                    i = 0;
                    // Henche executioner is the process id that executed a task.
                }

                long endTime = System.nanoTime();
                long duration = (endTime - startTime);
                // System.out.println(SharedData.baseId+ " Launch Start-End. TTE=" + (duration / 1000000000) + " secs"
        //);

            } catch (Exception er) {
//                ParseOperation.UploadError(er.getMessage() + "--- i= " + i, "yt_ExtractServer");
            }
        }
    }
    /*
     executor.shutdown();
     while (!executor.isTerminated()) {
     }
     System.out.println("Finished all threads");*/
}
