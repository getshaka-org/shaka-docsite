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
      "-Yindent-colons",
      "-language:noAutoTupling"
    ),
    scalaJSUseMainModuleInitializer := true,

    libraryDependencies ++= Seq(
      "org.getshaka" %%% "shaka" % "0.2.0",
      "org.getshaka" %%% "shaka-router" % "0.1.1"
    )
  )
