/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yt.util;

/**
 *
 * @author zeus
 */
public class SharedData {

    public static String ApplicationId_ = "USEYOUROWN";
    public static String RestId_ = "USEYOUROWN";
    public static String baseId = Util.GetSuperUniqueId();
    public static String RootStoragePath = "MusicStorage/";

    public static String BaseCommandExtractAudio = "youtube-dl --extract-audio --audio-format mp3 -o ";

    // 1 hour = 3600000;
    
    // These values are set through the settings.
    public static int Cores = 1;
    public static int WaitBeforeGetNextWork = 10000;
    public static int WorkBatchCount = 1;
    public static int WaitAfterWorkComplete = 1000;
    
    public static int LogServerPerformanceInterval = 3600000;
    public static int LogServerGetSettingsInterval = 3600000;
    
 
    public static float TaskIdleCount = 0;
    public static float TaskCompletedCount = 0;
    
}
