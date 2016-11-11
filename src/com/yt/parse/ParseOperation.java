/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yt.parse;

import com.yt.util.SharedData;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.parse4j.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zeus
 */
public class ParseOperation {

    public static void IniParse() {
        // Add your initialization code here
        Parse.initialize(SharedData.ApplicationId_,
                SharedData.RestId_);

    }

    public static void ClearAllQueueExecutioners() {
        ParseQuery<ParseObject> requests = new ParseQuery("Queue");
        requests.whereNotEqualTo("executioner", "none");
        List<ParseObject> doMe = new ArrayList<ParseObject>();
        try {
            doMe = requests.find();
            if (doMe != null) {
                for (int i = 0; i < doMe.size(); i++) {
                    doMe.get(i).put("executioner", "none");
                    doMe.get(i).saveInBackground();
                }
                System.out.println("All Queue items set to executioner=none");
            } else {
                System.out.println("No executioners found to be be NOT none");

            }
        } catch (ParseException ex) {
            Logger.getLogger(ParseOperation.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    public static List<ParseObject> GetWork(String workerId) {
        try {
            // Get the top requets that have no executioner.
            ParseQuery<ParseObject> requests = new ParseQuery("Queue");
            requests.orderByDescending("priority,-createdAt");
            //requests.orderByAscending("createdAt");
            // requests.addAscendingOrder("createdAt");

            requests.whereEqualTo("executioner", "none");
            requests.limit(SharedData.WorkBatchCount);
            List<ParseObject> doMe = new ArrayList<ParseObject>();
            doMe = requests.find();

            if (doMe != null) {
                // Set all the requets you got to have the executioner as yourself.
                for (int i = 0; i < doMe.size(); i++) {
                    System.out.println(workerId + ", got:" + doMe.get(0).getString("videoId"));
                    doMe.get(i).put("executioner", workerId);
                    doMe.get(i).save();
                }

                // Now fetch work that has your name as executioner.
                ParseQuery<ParseObject> getMyWork = new ParseQuery("Queue");
                getMyWork.orderByDescending("priority");
                getMyWork.whereEqualTo("executioner", workerId);
                List<ParseObject> myTasks = getMyWork.find();
                return myTasks;

            } else {
                // System.out.println(workerId + ", No work found");
                Thread.sleep(5000);
            }
        } catch (Exception er) {
            System.out.println("Exception: " + er.getMessage());
        }

        return null;
    }

    public static void UploadFileAndDeleteQueueEntry(ParseObject qEntry, byte[] data) {
        try {
            ParseObject completedDBEntry = new ParseObject("Completed");
            completedDBEntry.put("videoId", qEntry.getString("videoId"));
            completedDBEntry.put("executioner", qEntry.getString("executioner"));
            completedDBEntry.put("title", qEntry.getString("title"));
            completedDBEntry.put("duration", qEntry.getString("duration"));
            completedDBEntry.put("priority", qEntry.getInt("priority"));
            completedDBEntry.put("userId", qEntry.getParseObject("userId"));

            ParseFile music = new ParseFile("abc.mp3", data);
            music.save();
            completedDBEntry.put("storedFile", music);
            completedDBEntry.put("status", "1");
            completedDBEntry.save();

            qEntry.delete();
        } catch (Exception er) {
            System.out.println("Exception: " + er.getMessage());
        }

    }

    public static void UploadServerSettings() {
        try {
            ParseObject completedDBEntry = new ParseObject("ServerSettings");
            completedDBEntry.put("executioner", SharedData.baseId);
            completedDBEntry.put("cores", SharedData.Cores);
            completedDBEntry.put("workBatchCount", SharedData.WorkBatchCount);
            completedDBEntry.put("waitAfterWorkCompletion", SharedData.WaitAfterWorkComplete);
            completedDBEntry.put("waitBeforeGetNextWork", SharedData.WaitBeforeGetNextWork);
            completedDBEntry.put("logServerGetSettingsInterval", SharedData.LogServerGetSettingsInterval);
            completedDBEntry.put("logServerPerformanceInterval", SharedData.LogServerPerformanceInterval);

            completedDBEntry.saveInBackground();

        } catch (Exception er) {
            System.out.println("Exception: " + er.getMessage());
        }
    }

    public static void UploadError(String exceptionMessage, String className) {
        ParseObject completedErrorEntry = new ParseObject("Errors");
        completedErrorEntry.put("executioner", SharedData.baseId);
        completedErrorEntry.put("errorClassName", className);
        completedErrorEntry.put("exceptionMessage", exceptionMessage);
        completedErrorEntry.saveInBackground();
    }

    public static void LogServerData() {
        ParseObject completedErrorEntry = new ParseObject("Logs");
        float total = SharedData.TaskCompletedCount + SharedData.TaskIdleCount;
        boolean totalIsZero = false;
        if (total == 0) {
            total = 1;
            totalIsZero = true;
        }
        completedErrorEntry.put("executioner", SharedData.baseId);
        completedErrorEntry.put("idle", SharedData.TaskIdleCount / total * 100);
        completedErrorEntry.put("completed", SharedData.TaskCompletedCount / total * 100);

        if (totalIsZero) {
            total = 0;
        }
        completedErrorEntry.put("total", total);

        completedErrorEntry.saveInBackground();
    }

    public static void GetServerSettings() {
        try {
            ParseQuery<ParseObject> requests = new ParseQuery("ServerSettings");
            requests.limit(1);
            requests.whereEqualTo("executioner", SharedData.baseId);
            List<ParseObject> doMe = new ArrayList<ParseObject>();

            doMe = requests.find();

            if (doMe != null) {
                if (doMe.size() > 0) {
                    ParseObject Settings = doMe.get(0);
                    SharedData.WaitBeforeGetNextWork = Settings.getInt("waitBeforeGetNextWork");
                    SharedData.WorkBatchCount = Settings.getInt("workBatchCount");
                    SharedData.WaitAfterWorkComplete = Settings.getInt("waitAfterWorkCompletion");;

                    SharedData.LogServerPerformanceInterval = Settings.getInt("logServerPerformanceInterval");

                    SharedData.LogServerGetSettingsInterval = Settings.getInt("logServerGetSettingsInterval");

                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(ParseOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
