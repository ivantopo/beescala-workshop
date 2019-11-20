import SlimDocker.slimDockerSettings
import com.typesafe.sbt.packager.docker.{DockerChmodType, DockerPermissionStrategy}
import sbtrelease.ReleaseStateTransformations._

import scala.sys.process.Process

enablePlugins(JavaAppPackaging, DockerPlugin)

name := "workshop-concierge"
organization := "io.kamon"
organizationName := "Kamon APM"
scalaVersion := "2.12.8"
dockerUpdateLatest := true
dockerRepository := Some("kamon")
dockerExposedPorts := Seq(8080, 2550)
dockerChmodType := DockerChmodType.UserGroupWriteExecute
daemonUserUid in Docker := Some("777")
daemonGroupGid in Docker := Some("700")
daemonUser in Docker    := "demogorgon"
daemonGroup in Docker    := "nomad"
version in Docker := Process("git rev-parse HEAD").lineStream.head
packageName in Docker := name.value
resolvers += Resolver.bintrayRepo("kamon-io", "snapshots")
resolvers += Resolver.bintrayRepo("kamon-io", "releases")
libraryDependencies ++= Seq(
  "com.typesafe.akka"       %% "akka-http"              % "10.1.8",
  "com.typesafe.akka"       %% "akka-http-spray-json"   % "10.1.8",

  "com.typesafe.akka"   %% "akka-actor"             % "2.5.22",
  "com.typesafe.akka"   %% "akka-remote"            % "2.5.22",
  "com.typesafe.akka"   %% "akka-cluster"           % "2.5.22",
  "com.typesafe.akka"   %% "akka-cluster-sharding"  % "2.5.22",
  "com.typesafe.akka"   %% "akka-slf4j"             % "2.5.22",
  "com.lightbend.akka.management" %% "akka-management"              % "0.15.0",
  "com.lightbend.akka.management" %% "akka-management-cluster-http" % "0.15.0",

  "com.h2database"      %  "h2"                     % "1.3.148",
  "ch.qos.logback"      %  "logback-classic"        % "1.2.3",
  "com.orbitz.consul"   %  "consul-client"          % "1.3.5",
)
