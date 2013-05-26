package models

case class Host( id: String, name: String )

object Hosts {  
  def hosts(): List[ Host ] = {
    return List( new Host( "h1", "Host 1" ), new Host( "h2", "Host 2" ) )
  }	
}