/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yt.util;

import java.io.*;
import java.net.*;

import java.util.Random;

/**
 *
 * @author zeus
 */
public class Util {

    public static String GetMyIp() {

        URL url = null;
        BufferedReader in = null;
        String ipAddress = "";
        try {
            url = new URL("http://bot.whatismyipaddress.com");
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            ipAddress = in.readLine().trim();
            /* IF not connected to internet, then
             * the above code will return one empty
             * String, we can check it's length and
             * if length is not greater than zero, 
             * then we can go for LAN IP or Local IP
             * or PRIVATE IP
             */
            if (!(ipAddress.length() > 0)) {
                try {
                    InetAddress ip = InetAddress.getLocalHost();
                    System.out.println((ip.getHostAddress()).trim());
                    ipAddress = (ip.getHostAddress()).trim();
                } catch (Exception exp) {
                    ipAddress = "ERROR";
                }
            }
        } catch (Exception ex) {
            // This try will give the Private IP of the Host.
            try {
                InetAddress ip = InetAddress.getLocalHost();
                System.out.println((ip.getHostAddress()).trim());
                ipAddress = (ip.getHostAddress()).trim();
            } catch (Exception exp) {
                ipAddress = "ERROR";
            }
            //ex.printStackTrace();
        }
        System.out.println("IP Address: " + ipAddress);
        return ipAddress;
    }

    public static String ToSignificantFiguresString(String n, int toDigits){
        int diff = toDigits - n.length(); 
        if (diff > 0) {
            for (int i =0; i < diff; i++)
                n = "0" + n; 
        }
        return n;
    }

    public static String GetSuperUniqueId() {
        String id = GetMyIp();
        Random rand = new Random();

        int n = rand.nextInt(1000000) + 1;
        String finalOutput = id + "_" + ToSignificantFiguresString(n + "", 8);
        System.out.println("My super unique ID: " + finalOutput);
        return finalOutput;

    }
}
