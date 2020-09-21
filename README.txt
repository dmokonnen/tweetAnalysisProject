/*
*Twitter streaming and Analysis project
*Group members: Demisew Mokonnen, Dereje Kenno, Amare Ayele
*
*/

To run the projects 
1. Download and install ScalaIDE from http://scala-ide.org/download/sdk.html.
2. follow the steps provided in the presentation to install kafka. (check slide 12).
2. Import SparkStreaming and Sparkstreamingconsumer eclipse projects to your desired workspace in ScalaIDE
3. Provide the important keys from your Twitter App into StreamTweets.scala. The following is the sample.
    val consumerKey = "9b94jjNgvXWZ8wAnvlpzjI6V0" // Your consumerKey
    val consumerSecret = "P8izzS6mV2r4wulm9nBlfXl70NBF6CdknKXyAPMKfgSGgXoHej" // your API secret
    val accessToken ="327368651-oMLEdu9Ksjwsnk5Jtd2XXINz4JlIT6CRLSOzXSDc" // your access token
    val accessTokenSecret = "q69KeohvMC3E6ff0Ns2l48hEScr8WTDKxbp0VE5ZsBR2A" // your token secret
   Another way of doing this is giving the keys in particular order as arguments to the app. But here we directly
   provided the keys in the code.
4. Right click on the StreamTweets.scala, then run as scala application. This starts accepting streaming data from twitter. And sending 
   the streams to kafka under 'tweets' topic
   One way of seeing this is by uncommenting '//statuses.print()' on line 42, you will see the streaming data being displayed in console.
5. Right click on the SparkTweetsConsumer.scala, then run as scala application. This starts accepting tweet streams from kafka and saving directly to hdfs
   parquet file and at the same time being integrated as hive table. you can see in the hive in default database. 
6. When you are running this project since two apps are running at the same time, you can switch between consoles to see what is being displayed.
7. The output file in hdfs is multiple snappy.parquet files. those kind files you will get by just running the project as stated above. In addition,
   We included the CSV file exported from hive and .xlsx file from hive in output directory.
   Furthermore, the Jupyter notebook output for visualization also included in output directory under data Visualization directory.
7. For Data Visualization The instruction to install necessary softwares and running is given in Data Visualization.docx file.
