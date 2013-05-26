package models

import java.util.Date

import scala.util.Random

import akka.actor.Actor
import play.api.libs.iteratee.Concurrent
import play.api.libs.json.JsNumber
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsValue

class StatisticsActor( hostid: String ) extends Actor {
  val ( enumerator, channel ) = Concurrent.broadcast[JsValue]
  
  def receive = {
    case Connect( host ) => sender ! Connected( enumerator )
    case Refresh => broadcast( new Date().getTime(), hostid )     
  }
  
  def broadcast( timestamp: Long, id: String ) {
    val msg = JsObject( 
      Seq( 
        "id" -> JsString( id ),
	    "cpu" -> JsObject( 
	      Seq( 
	    	( "timestamp" -> JsNumber( timestamp ) ), 
	        ( "load" -> JsNumber( Random.nextInt( 100 ) ) ) 
	      ) 
	  	)
	  )
	)
	    
	channel.push( msg )
  }
}