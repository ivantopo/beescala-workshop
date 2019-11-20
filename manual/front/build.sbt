name := "manual-front-service"
scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"              % "10.1.10",
  "com.typesafe.akka" %% "akka-actor"             % "2.6.0",
  "com.typesafe.akka" %% "akka-stream"            % "2.6.0",
  "com.typesafe.akka" %% "akka-slf4j"             % "2.6.0",
  "ch.qos.logback"     % "logback-classic"        % "1.2.3",


  "io.kamon" %% "kamon-bundle" % "2.0.4"
)
