import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

javaAgents += "io.kamon" % "kanela-agent" % "1.0.1"


lazy val root = (project in file("."))
  .enablePlugins(JavaAgent)
  .settings(
    name := "example_project",
    run / fork := true,
    libraryDependencies ++= Seq(
      "io.kamon"                   %% "kamon-logback"   % "2.0.0",
      "ch.qos.logback"             % "logback-classic" % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging"  % "3.9.2"
    )

  )
