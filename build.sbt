lazy val `sbt-autoprefixer` = (project in file(".")).enablePlugins(SbtWebBase)

organization := "se.sogeti.sbt"

name := "sbt-autoprefixer"

addSbtJsEngine("1.3.7")

publishMavenStyle := false
