lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    organization := "org.getshaka",
    name := "shaka-docsite",
    version := "0.1.0-SNAPSHOT",
    versionScheme := Some("early-semver"),

    scalaVersion := "3.0.0-RC3",
    scalacOptions ++= Seq(
      "-language:noAutoTupling"
    ),
    // todo remove when fixed: https://github.com/lampepfl/dotty/issues/11943
    Compile / doc / sources := Seq(),
    scalaJSUseMainModuleInitializer := true,

    libraryDependencies ++= Seq(
      "org.getshaka" %%% "shaka" % "0.2.1",
      "org.getshaka" %%% "shaka-router" % "0.1.2"
    )
  )
