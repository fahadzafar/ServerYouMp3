/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yt.thread;

import com.yt.parse.ParseOperation;
import com.yt.util.SharedData;
import java.io.File;
import java.util.List;
import org.parse4j.ParseObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WorkerThread implements Runnable {

    private String myCompleteId;

    public WorkerThread(int threadNo) {
        this.myCompleteId = threadNo + "_" + SharedData.baseId;

    }

    @Override
    public void run() {
        long startTime = System.nanoTime();
        processCommand();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

       // System.out.println(myCompleteId + "      Work processing Start-End. TTE=" + (duration / 1000000000) + " secs"
       // );
    }

    private void processCommand() {
        try {
            List<ParseObject> queueItem = ParseOperation.GetWork(myCompleteId);
            if (queueItem != null) {
                if (queueItem.size() > 0)    
                    SharedData.TaskCompletedCount ++;
                
                for (int i = 0; i < queueItem.size(); i++) {
                    String vidId = queueItem.get(i).getString("videoId");
                    String title = queueItem.get(i).getString("title");

                    // Perform the work -----------------
                    if (vidId == null || vidId.equals("")) {
                        return;
                    }

                    try {

                        Process p = Runtime.getRuntime().exec(SharedData.BaseCommandExtractAudio + vidId + ".mp3"
                                + " https://www.youtube.com/watch?v=" + vidId, null, new File(SharedData.RootStoragePath));
                        // 10 min vid = qYILKnaVPgw
                        // 3.5 mins = d3bUg8wsgVE
                        p.waitFor();

                        // Now upload the file to Parse.
                        Path path = Paths.get(SharedData.RootStoragePath + vidId + ".mp3");
                        ParseOperation.UploadFileAndDeleteQueueEntry(queueItem.get(i), Files.readAllBytes(path));
                        String det = path.toString();
                        File storedFile = new File(SharedData.RootStoragePath + vidId + ".mp3");
                        System.out.println("Finished:" + title + "\n \t \t, state, idle:" + SharedData.TaskIdleCount +
                                ", completed:" + SharedData.TaskCompletedCount);
                        storedFile.delete();
                    } catch (Exception ex) {
                        ParseOperation.UploadError(ex.getMessage() + "--- i= " + i + " + , queueItem.size= " + queueItem.size(), "WorkerThread (queueItem loop)");

                    }
                    // ------------------------
                } // for each retrieved item.
            } else {
                SharedData.TaskIdleCount++;
            }
            
            Thread.sleep(SharedData.WaitAfterWorkComplete);

        } catch (InterruptedException e) {
            ParseOperation.UploadError(e.getMessage(), "WorkerThread (processCommand())");

        }
    }

    @Override
    public String toString() {
        return this.myCompleteId;
    }
}
