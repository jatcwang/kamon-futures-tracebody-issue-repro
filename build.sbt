ThisBuild / scalaVersion := "2.12.10"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .enablePlugins(JavaAgent)
  .settings(
    javaAgents += "io.kamon" % "kanela-agent" % "1.0.3" % "runtime;test",
//    javaAgents += "org.aspectj" % "aspectjweaver" % "1.9.2" % "runtime;test",
    name := "kamon-futures-context-propagation-behaviour",
    run / fork := true,
    test / fork := true,
    libraryDependencies ++= Seq(
      "io.kamon" %% "kamon-core" % "2.0.2",
      "io.kamon" %% "kamon-scala-future" % "2.0.1",
      "io.kamon" %% "kamon-logback" % "2.0.2",
      "io.kamon" %% "kamon-testkit" % "2.0.2" % "test",
      "io.kamon" %% "kamon-cats-io" % "2.0.1",
      "io.kamon" %% "kamon-akka" % "2.0.1",
//      "io.kamon" %% "kamon-core" % "1.1.6",
//      "io.kamon" %% "kamon-scala-future" % "1.0.0",
//      "io.kamon" %% "kamon-cats-io" % "1.1.0",
//      "io.kamon" %% "kamon-testkit" % "1.1.1" % "test",
//      "io.kamon" %% "kamon-akka-http-2.5" % "1.1.2",
      "com.typesafe.akka" %% "akka-http" % "10.1.9",
      "com.typesafe.akka" %% "akka-actor" % "2.5.13",
      "com.typesafe.akka" %% "akka-stream" % "2.5.25",
      "com.typesafe" %% "ssl-config-core" % "0.4.0",
      "org.typelevel" %% "cats-effect" % "2.0.0",
      "org.scalatest" %% "scalatest" % "3.1.0" % "test",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
    )
  )
