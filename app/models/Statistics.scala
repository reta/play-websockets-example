package models

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

import akka.actor.ActorRef
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Iteratee
import play.api.libs.json.JsValue

case class Refresh()
case class Connect( host: Host )
case class Connected( enumerator: Enumerator[ JsValue ] )

object Statistics {
	implicit val timeout = Timeout( 5 second )
	var actors: Map[ String, ActorRef ] = Map()
  
    def actor( id: String ) = {
	   actors.find( _._1 == id ).map( _._2 ) match {
	     case Some( actor ) => actor
	     
	     case None => {
	       val actor = Akka.system.actorOf( Props( new StatisticsActor( id ) ), name = s"host-$id" )   
    	   Akka.system.scheduler.schedule( 0.seconds, 3.second, actor, Refresh )
    	   actors += ( id -> actor )
    	   actor
	     }
	   }
	}
	
	def attach( host: Host ): Future[ ( Iteratee[ JsValue, _ ], Enumerator[ JsValue ] ) ] = {
	  ( actor( host.id ) ? Connect( host ) ).map {      
      	case Connected( enumerator ) => ( Iteratee.ignore[JsValue], enumerator )
	  }
	}
}