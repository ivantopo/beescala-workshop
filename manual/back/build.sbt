name := "manual-back-service"
scalaVersion := "2.12.8"
resolvers += Resolver.bintrayRepo("kamon-io", "releases")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"              % "10.1.8",
  "com.typesafe.akka" %% "akka-actor"             % "2.5.22",
  "com.typesafe.akka" %% "akka-stream"            % "2.5.22",
  "com.typesafe.akka" %% "akka-slf4j"             % "2.5.22",
  "ch.qos.logback"     % "logback-classic"        % "1.2.3",

  "io.kamon" %% "kamon-bundle" % "2.0.4"
)
