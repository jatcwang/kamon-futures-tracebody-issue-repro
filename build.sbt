ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .enablePlugins(JavaAgent)
  .settings(
    javaAgents += "io.kamon" % "kanela-agent" % "1.0.2" % "runtime",
    name := "example_project",
    run / fork := true,
    libraryDependencies ++= Seq(
      "io.kamon"                   %% "kamon-core"   % "2.0.1",
      "io.kamon"                   %% "kamon-scala-future"   % "2.0.1",
//      "io.kamon"                   %% "kamon-executors"   % "2.0.2",
      "io.kamon"                   %% "kamon-zipkin"   % "2.0.0",
      "io.kamon"                   %% "kamon-logback"   % "2.0.2",
      "ch.qos.logback"             % "logback-classic" % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging"  % "3.9.2"
    )

  )
