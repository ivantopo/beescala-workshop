name := "manual-start"
scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-core" % "2.0.2",
  "io.kamon" %% "kamon-status-page" % "2.0.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
)
