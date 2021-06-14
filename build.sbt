lazy val `sbt-autoprefixer` = (project in file(".")).enablePlugins(SbtWebBase)

organization := "se.sisyfosdigital.sbt"

name := "sbt-autoprefixer"

addSbtJsEngine("1.2.3")

ThisBuild / scalaVersion := "2.12.13"

publishMavenStyle := false
bintrayOrganization := Some("sisyfos-digital")
bintrayRepository := "sbt-plugins"
