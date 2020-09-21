
package edu.miu.cs523

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.twitter.TwitterUtils
import twitter4j.auth.OAuthAuthorization
import twitter4j.conf.ConfigurationBuilder
import org.apache.log4j.{Level, Logger}

/** Simple application to listen to a stream of Tweets and print them out */
object StreamTweets {
 
  /** Our main function where the action happens */
  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("PrintTweets").setMaster("local[*]")
    val sc  = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(1))
    Logger.getRootLogger().setLevel(Level.ERROR)
    // values of Twitter API.
    val consumerKey = "9b94jjNgvXWZ8wAnvlpzjI6V0" // Your consumerKey
    val consumerSecret = "P8izzS6mV2r4wulm9nBlfXl70NBF6CdknKXyAPMKfgSGgXoHej" // your API secret
    val accessToken ="327368651-oMLEdu9Ksjwsnk5Jtd2XXINz4JlIT6CRLSOzXSDc" // your access token
    val accessTokenSecret = "q69KeohvMC3E6ff0Ns2l48hEScr8WTDKxbp0VE5ZsBR2A" // your token secret

    //Connection to Twitter API
    val cb = new ConfigurationBuilder
    cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret)
    .setOAuthAccessToken(accessToken).setOAuthAccessTokenSecret(accessTokenSecret)

    val auth = new OAuthAuthorization(cb.build)
    val tweets = TwitterUtils.createStream(ssc, Some(auth))
   
    val statuses = tweets.map(status => (status.getUser.getId, status.getLang,status.getUser.getFriendsCount,
      status.getUser.getLocation,status.getUser.getFollowersCount,status.getSource.split("[<,>]")(2),
      status.getRetweetCount,status.isPossiblySensitive,status.isRetweet,status.isRetweeted,status.getCreatedAt.toString,
      status.getText)).map(_.productIterator.mkString("\t"))
  
    statuses.foreachRDD { (rdd, time) =>
      rdd.foreachPartition { partitionIter =>
        val props = new Properties()
        val bootstrap = "localhost:9092" 
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        props.put("bootstrap.servers", bootstrap)
        val producer = new KafkaProducer[String, String](props)
        partitionIter.foreach { elem =>
          val dat = elem.toString()
          val data = new ProducerRecord[String, String]("tweets", null, dat) // "tweets" is the name of Kafka topic
          producer.send(data)
        }
        producer.flush()
        producer.close()
      }
    }
    
    ssc.start()
    ssc.awaitTermination()
  }  
}
