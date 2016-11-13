
#YouMp3Server


![](https://github.com/fahadzafar/AppYouMp3/blob/master/app/src/main/res/drawable-mdpi/logo.png)

#Goal of the App:

This app strips the MP3 audio off any YouTube video selected through an android app. The YouTube videos are displayed in a visual list with image + title, and can be played from within the app. Once the request for mp3 extraction is submitted, the app will also download the resulting mp3 file to the phone.



#Description:

It requires a server running at the backend that does the extraction and places the MP3 into a Parse table that can be downloaded from the app. The design of this entire application can be seen [here](https://drive.google.com/file/d/0BzkvMWM-w80JWXpCRkoybmpZcnc/view?usp=sharing).

The server works using a passive approach, saves on a lot of money and dont need to use AWS 24/7. The server can run on any laptop and is multi-threaded. This server also auto adjusts pool timing whenc checking the work request queue and can alter pooling time based on usage. It runs "youtube-dl" to extract the audio, then updates the audio-strip request to allow the app to pick up the audio from the online datastore.

The Parse data tables that store the MP3 extraction request can be seen [here](https://drive.google.com/open?id=0BzkvMWM-w80JdWVQMTVQZFdXTXM).


The table where the MP3 is stored after extraction can be seen [here](https://drive.google.com/open?id=0BzkvMWM-w80JaG5zR0lWU1Z6ckE).

App code for this YouMp3 app can be found [here](https://github.com/fahadzafar/AppYouMp3).


#Code Review:

- Start reviewing code from here: https://github.com/fahadzafar/ServerYouMp3/blob/master/src/com/yt/main/Yt_ExtractServer.java
- Change your info here: https://github.com/fahadzafar/ServerYouMp3/blob/master/src/com/yt/util/SharedData.java
- The actual command that runs on the server to extract mp3 can be found here: https://github.com/fahadzafar/ServerYouMp3/blob/master/src/com/yt/util/SharedData.java
- The worker thread code which is the core of this server can be found here: https://github.com/fahadzafar/ServerYouMp3/blob/master/src/com/yt/thread/WorkerThread.java


Fore more images about the app go [here](https://drive.google.com/open?id=0BzkvMWM-w80JNFE3VVUyYTI0czQ).





Note: Parse will be discontinued after January 2017, this app+server will not be migrated.

