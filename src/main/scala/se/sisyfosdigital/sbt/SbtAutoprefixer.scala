package se.sisyfosdigital.sbt

import sbt._
import sbt.Keys._
import com.typesafe.sbt.web.{Compat, SbtWeb}
import com.typesafe.sbt.web.pipeline.Pipeline
import com.typesafe.sbt.jse.{SbtJsEngine, SbtJsTask}

object Import {

  val autoprefixer = TaskKey[Pipeline.Stage]("autoprefixer", "Parse CSS and adds vendor prefixes to rules by Can I Use")

  object AutoprefixerKeys {
    val buildDir = SettingKey[File]("autoprefixer-build-dir", "Where autoprefixer will read from.")
    val browsers = SettingKey[String]("autoprefixer-browsers", "Which browsers autoprefixer will support.")
    val inlineSourceMap = SettingKey[Boolean]("autoprefixer-inline-source-map", "Enables inline source maps by data:uri to annotation comment. The default is that inline source maps are dsiabled (false).")
    val sourceMap = SettingKey[Boolean]("autoprefixer-map", "Enables source maps. The default is that source maps are enabled (true).")
  }

}

object SbtAutoprefixer extends AutoPlugin {

  override def requires = SbtJsTask

  override def trigger = AllRequirements

  val autoImport = Import

  import SbtWeb.autoImport._
  import WebKeys._
  import SbtJsEngine.autoImport.JsEngineKeys._
  import SbtJsTask.autoImport.JsTaskKeys._
  import autoImport._
  import AutoprefixerKeys._

  override def projectSettings = Seq(
    buildDir := (autoprefixer / resourceManaged).value / "build",
    autoprefixer / excludeFilter := HiddenFileFilter,
    autoprefixer / includeFilter := GlobFilter("*.css"),
    autoprefixer / resourceManaged := webTarget.value / autoprefixer.key.label,
    sourceMap := true,
    inlineSourceMap := false,
    browsers := "",
    autoprefixer := runAutoprefixer.dependsOn(Assets / WebKeys.nodeModules).value
  )

  private def runAutoprefixer: Def.Initialize[Task[Pipeline.Stage]] = Def.task {
    val include = (autoprefixer / includeFilter).value
    val exclude = (autoprefixer / excludeFilter).value
    val streamsValue = streams.value
    val buildDirValue = buildDir.value
    val inlineSourceMapValue = inlineSourceMap.value
    val sourceMapValue = sourceMap.value
    val browsersValue = browsers.value
    val stateValue = state.value
    val engineTypeValue = (autoprefixer / engineType).value
    val commandValue = (autoprefixer / command).value
    val nodeModuleDirectoriesValue = (Assets / nodeModuleDirectories).value


    mappings =>
      val autoprefixerMappings = mappings.filter(f => !f._1.isDirectory && include.accept(f._1) && !exclude.accept(f._1))

      SbtWeb.syncMappings(
        streamsValue.cacheStoreFactory.make("autoprefixer-cache"),
        autoprefixerMappings,
        buildDirValue
      )

      val buildMappings = autoprefixerMappings.map(o => buildDirValue / o._2)

      val cacheDirectory = streamsValue.cacheDirectory / autoprefixer.key.label
      val runUpdate = FileFunction.cached(cacheDirectory, FilesInfo.hash) {
        inputFiles =>
          streamsValue.log.info("Autoprefixing CSS")

          val inputFileArgs = inputFiles.map(_.getPath)

          val useAutoprefixerArg = Seq("--use", "autoprefixer", "--replace")

          val sourceMapArgs = if (inlineSourceMapValue && sourceMapValue) { Nil } else { if (sourceMapValue) Seq("--map") else Seq("--no-map") }

          val browsersArg = if (browsersValue.nonEmpty) Seq("--autoprefixer.browsers", browsersValue) else Nil

          val allArgs = Seq() ++
            sourceMapArgs ++
            useAutoprefixerArg ++
            browsersArg ++
            inputFileArgs

          SbtJsTask.executeJs(
            stateValue,
            engineTypeValue,
            commandValue,
            nodeModuleDirectoriesValue.map(_.getPath),
            nodeModuleDirectoriesValue.last / "postcss-cli" / "bin" / "postcss",
            allArgs
          )

          buildDirValue.**(AllPassFilter).get.filter(!_.isDirectory).toSet
      }

      val autoPrefixedMappings = runUpdate(buildMappings.toSet).pair(Path.relativeTo(buildDirValue))
      (mappings.toSet -- autoprefixerMappings ++ autoPrefixedMappings).toSeq
  }

}
