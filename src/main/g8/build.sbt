import Dependencies._

name := "lagom-scala-mysql-es"

version := "0.1"

scalaVersion := "2.12.8"

lagomKafkaEnabled in ThisBuild := false

lazy val `lagom-scala-mysql-es` = (project in file("."))
  .aggregate(`user-api`, `user-impl`)

lazy val `user-api` = (project in file("user-api"))
  .settings(libraryDependencies ++= Seq(lagomScaladslApi))
  .settings(coverageExcludedPackages := ".*UserService.*;")

lazy val `user-impl` = (project in file("user-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(lagomScaladslTestKit, MacWire, FilterHelper, ScalaTest,
      lagomScaladslPersistenceJdbc, lagomScaladslApi, Mockito, MySqlConnector, TypeSafeConf))
  .settings(
    coverageExcludedPackages := ".*UserLoader.*;.*UserSerializerRegistry.*;.*UserApplication.*;.*UserConstants.*;")
  .dependsOn(`user-api`)

lagomCassandraEnabled in ThisBuild := false

dependencyOverrides in ThisBuild ++= Seq(ScalaParser, NettyHandler, AkkaStream, AkkaActor, Guava)
