lazy val `sbt-autoprefixer` = (project in file(".")).enablePlugins(SbtWebBase)

organization := "net.matthewrennie.sbt"

name := "sbt-autoprefixer"

version := "0.2.1-SNAPSHOT"

resolvers ++= Seq(
  "Typesafe Releases Repository" at "https://repo.typesafe.com/typesafe/releases/",
  Resolver.url("sbt snapshot plugins", url("https://repo.scala-sbt.org/scalasbt/sbt-plugin-snapshots"))(Resolver.ivyStylePatterns),
  Resolver.sonatypeRepo("snapshots"),
  "Typesafe Snapshots Repository" at "https://repo.typesafe.com/typesafe/snapshots/"
)

addSbtJsEngine("1.2.3")

publishMavenStyle := false

publishTo := {
  if (isSnapshot.value) Some(Resolver.sbtPluginRepo("snapshots"))
  else Some(Resolver.sbtPluginRepo("releases"))
}

//scriptedSettings

//scriptedBufferLog := false

//scriptedLaunchOpts += s"-Dproject.version=${version.value}"
