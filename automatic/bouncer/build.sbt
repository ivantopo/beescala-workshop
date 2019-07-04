import com.typesafe.sbt.packager.docker.{DockerChmodType, DockerPermissionStrategy}
import kamon.instrumentation.sbt.SbtKanelaRunner.Keys.kanelaVersion
import scala.sys.process.Process

name := "apm-demo.bouncer"
organization := "io.kamon"
version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .enablePlugins(DockerPlugin)
  .enablePlugins(JavaAgent)

scalaVersion := "2.12.8"
libraryDependencies += guice
dockerRepository := Some("kamon")
dockerExposedPorts := Seq(9000)
dockerChmodType := DockerChmodType.UserGroupWriteExecute
daemonUserUid in Docker := Some("777")
daemonGroupGid in Docker := Some("700")
daemonUser in Docker    := "demogorgon"
daemonGroup in Docker    := "nomad"
version in Docker := Process("git rev-parse HEAD").lineStream.head
packageName in Docker := name.value
javaOptions in Universal ++= Seq(
  "-J-Xmx768m"
)

resolvers += Resolver.mavenLocal
resolvers += Resolver.bintrayRepo("kamon-io", "snapshots")
resolvers += Resolver.bintrayRepo("kamon-io", "releases")
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "3.0.0",
  "org.postgresql" % "postgresql" % "42.2.5",
)