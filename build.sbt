lazy val shakaVersion = "0.1.0"

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    organization := "org.getshaka",
    name := "shaka-docsite",
    version := "0.1.0-SNAPSHOT",
    versionScheme := Some("early-semver"),

    scalaVersion := "3.0.0-RC1",
    scalacOptions ++= Seq(
      "-Ycheck-init",
      "-Yindent-colons"
    ),
    scalaJSUseMainModuleInitializer := true,

    libraryDependencies ++= Seq(
      "org.getshaka" %%% "shaka" % shakaVersion,
      "org.getshaka" %%% "shaka-router" % shakaVersion
    )
  )
