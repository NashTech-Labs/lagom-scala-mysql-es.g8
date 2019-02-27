import sbt._

object Dependencies {
  lazy val MacWire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
  lazy val ScalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test
  lazy val TypeSafeConf = "com.typesafe" % "config" % "1.3.0"
  lazy val Mockito = "org.mockito" % "mockito-all" % "1.10.19" % Test
  lazy val FilterHelper = "com.typesafe.play" %% "filters-helpers" % "2.6.12"
  lazy val MySqlConnector = "mysql" % "mysql-connector-java" % "8.0.15"
  lazy val ScalaParser = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
  lazy val NettyHandler = "io.netty" % "netty-handler" % "4.1.25.Final"
  lazy val AkkaStream = "com.typesafe.akka" %% "akka-stream" % "2.5.14"
  lazy val AkkaActor = "com.typesafe.akka" %% "akka-actor" % "2.5.14"
  lazy val Guava = "com.google.guava" % "guava" % "22.0"
}
