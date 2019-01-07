sbt-autoprefixer
================

[![Build Status](https://api.travis-ci.org/mebur/sbt-autoprefixer-1.png?branch=master)](https://travis-ci.org/mebur/sbt-autoprefixer-1)

[sbt-web](https://github.com/sbt/sbt-web) plugin that uses [Autoprefixer](https://github.com/ai/autoprefixer) to post-process CSS and add vendor prefixes to rules by [Can I Use](http://caniuse.com).

To use this plugin use the addSbtPlugin command within your project's plugins.sbt (or as a global setting) i.e.:

    addSbtPlugin("se.sisyfosdigital.sbt" % "sbt-autoprefixer" % "0.2.2")

Your project's build file also needs to enable sbt-web plugins. For example with build.sbt:

```scala
    lazy val root = (project in file(".")).enablePlugins(SbtWeb)
```

Declare the execution order of the asset pipeline:
```scala
pipelineStages in Assets := Seq(autoprefixer)
```

The following option are supported:

Option              | Description
--------------------|------------
browsers            | Specify browsers versions. Default: empty
cascade           	| Creates nice visual cascade of prefixes. Default: true.
sourceMap           | Enables source maps. Default: true.
inlineSourceMap     | Enables inline source maps by data:uri to annotation comment. Default: false.
    
The following sbt code illustrates how to include inline source maps 

```scala
AutoprefixerKeys.inlineSourceMap in Assets := true
```

To include all CSS files for post processing

```scala
includeFilter in autoprefixer := GlobFilter("*.css"),
```

You also have to use npm in order to install required dependencies.
Modify your package.json (in root):

```json
{
  "dependencies": {
    "postcss-cli": "6.1.0",
    "autoprefixer": "9.3.1"
  }
}
```
or invoke:
```
npm install postcss-cli@">=2.5.0" autoprefixer@">=6.3.0" --save
```

If you receive duplicate map errors when using in combination with LESS, turn sourceMapping off.
