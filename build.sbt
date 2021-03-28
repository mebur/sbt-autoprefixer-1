lazy val `sbt-autoprefixer` = (project in file(".")).enablePlugins(SbtWebBase)

organization := "net.matthewrennie.sbt"

name := "sbt-autoprefixer"

version := "0.2.1-SNAPSHOT"

addSbtJsEngine("1.2.3")

publishMavenStyle := false

publishTo := {
  if (isSnapshot.value) Some(Resolver.sbtPluginRepo("snapshots"))
  else Some(Resolver.sbtPluginRepo("releases"))
}

//scriptedSettings

//scriptedBufferLog := false

//scriptedLaunchOpts += s"-Dproject.version=${version.value}"
