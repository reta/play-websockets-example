name := "play-websockets-example"

scalaVersion := "2.11.4"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  
libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws
)     

EclipseKeys.skipParents in ThisBuild := false
