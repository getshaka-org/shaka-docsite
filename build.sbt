lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    organization := "org.getshaka",
    name := "shaka-docsite",
    version := "0.1.0-SNAPSHOT",
    versionScheme := Some("early-semver"),

    scalaVersion := "3.0.1",
    scalaJSUseMainModuleInitializer := true,

    libraryDependencies ++= Seq(
      "org.getshaka" %%% "shaka-router" % "0.2.1"
    )
  )
