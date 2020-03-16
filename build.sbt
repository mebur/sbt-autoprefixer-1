lazy val `sbt-autoprefixer` = project in file(".")

addSbtJsEngine("1.2.3")

sbtPlugin:=true

name := "sbt-autoprefixer"
organization := "se.sisyfosdigital.sbt"
description in ThisBuild := "plugin that uses Autoprefixer (https://github.com/ai/autoprefixer) to post-process CSS and add vendor prefixes to rules by Can I Use (http://caniuse.com)"
licenses in ThisBuild += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

publishMavenStyle := false
bintrayOrganization := Some("sisyfos-digital")
bintrayRepository := "sbt-plugins"