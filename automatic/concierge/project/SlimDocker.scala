import com.typesafe.sbt.packager.Keys.{daemonGroup, dockerCmd}
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging.autoImport.{projectDependencyArtifacts, scriptClasspathOrdering}
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import com.typesafe.sbt.packager.docker.{Cmd, CmdLike, ExecCmd}
import com.typesafe.sbt.packager.linux.LinuxPlugin.autoImport.{daemonUser, defaultLinuxInstallLocation}

object SlimDocker {
  lazy val slimDockerSettings = Seq(
    dockerCommands := {
      val dockerBaseDirectory = (defaultLinuxInstallLocation in Docker).value
      val relativeBaseDirectory = dockerBaseDirectory.drop(1)
      def relativeToBuild(file: String): String = relativeBaseDirectory + "/" + file

      val fileMappings = scriptClasspathOrdering.value.toMap.map { case (k, v) => (k.getName, v)}
      val projectArtifacts = projectDependencyArtifacts.value.map(f => fileMappings(f.data.getName))
      val libraryArtifacts = scriptClasspathOrdering.value.map(_._2).diff(projectArtifacts)

      val relativeLibArtifacts = libraryArtifacts.map(relativeToBuild).mkString(" ")
      val relativeProjectArtifacts = projectArtifacts.map(relativeToBuild).mkString(" ")

      val user = (daemonUser in Docker).value
      val group = (daemonGroup in Docker).value

      Seq(
        Cmd("FROM",     dockerBaseImage.value),
        Cmd("WORKDIR",  dockerBaseDirectory),
        Cmd("ADD",      s"--chown=$user:$group", "opt/docker/aspectjweaver", "/opt/docker/aspectjweaver"),
        Cmd("ADD",      s"--chown=$user:$group", s"$relativeLibArtifacts /opt/docker/lib/"),
        Cmd("ADD",      s"--chown=$user:$group", s"$relativeProjectArtifacts /opt/docker/lib/"),
        Cmd("ADD",      s"--chown=$user:$group", "opt/docker/bin /opt/docker/bin"),
        Cmd("EXPOSE", dockerExposedPorts.value mkString " ")
      ) ++
        makeDockerVolumes(dockerExposedVolumes.value, user, group) ++ Seq(
        makeUser(user),
        makeEntrypoint(dockerEntrypoint.value),
        makeCmd(dockerCmd.value))
    }
  )

  // These definitions had to be copied from com.typesafe.sbt.packager.docker.DockerPlugin due
  // to private access.

  def makeUser(daemonUser: String): CmdLike =
    Cmd("USER", daemonUser)

  def makeEntrypoint(entrypoint: Seq[String]): CmdLike =
    ExecCmd("ENTRYPOINT", entrypoint: _*)

  def makeCmd(args: Seq[String]): CmdLike =
    ExecCmd("CMD", args: _*)

  def makeChown(daemonUser: String, daemonGroup: String, directories: Seq[String]): CmdLike =
    ExecCmd("RUN", Seq("chown", "-R", s"$daemonUser:$daemonGroup") ++ directories: _*)

  def makeDockerVolumes(exposedVolumes: Seq[String], daemonUser: String, daemonGroup: String): Seq[CmdLike] = {
    if (exposedVolumes.isEmpty) Seq.empty[CmdLike]
    else Seq(
      ExecCmd("RUN", Seq("mkdir", "-p") ++ exposedVolumes: _*),
      makeChown(daemonUser, daemonGroup, exposedVolumes),
      ExecCmd("VOLUME", exposedVolumes: _*)
    )
  }
}