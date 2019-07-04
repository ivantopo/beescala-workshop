resolvers += Resolver.bintrayIvyRepo("kamon-io", "sbt-plugins")

addSbtPlugin("com.typesafe.play" % "sbt-plugin"           % "2.7.2")
addSbtPlugin("com.typesafe.sbt"  % "sbt-native-packager"  % "1.3.22")
addSbtPlugin("com.lightbend.sbt" % "sbt-javaagent"        % "0.1.5")
addSbtPlugin("io.kamon" % "sbt-kanela-runner-play-2.7" % "2.0.0-RC1")
