ThisBuild / scalaVersion := "3.2.2"
ThisBuild / organization := "eu.monniot"
ThisBuild / homepage := Some(url("https://github.com/fmonniot/scala3mock"))
ThisBuild / licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))
ThisBuild / releaseNotesURL := Some(
  url("https://github.com/fmonniot/scala3mock/releases")
)
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/fmonniot/scala3mock"),
    "git@github.com:fmonniot/scala3mock.git"
  )
)
ThisBuild / versionScheme := Some("early-semver")

ThisBuild / developers := List(
  Developer(
    "fmonniot",
    "François Monniot",
    "francoismonniot@gmail.com",
    url("https://francois.monniot.eu")
  )

  // Not sure if we should include the original ScalaMock devs here as well ?
)

lazy val scala3mock = project
  .in(file("."))
  .aggregate(core, cats, scalatest)
  .settings(publish / skip := true)

lazy val core = project
  .in(file("./core"))
  .settings(
    name := "scala3mock",
    scalacOptions ++= Seq("-feature", "-explain"),
    Test / scalacOptions += "-Xcheck-macros",

    // Useful when using the PrintAst[type] macro. That will provides the implementation
    // details of classes. Without it, only the signatures are available.
    // Test / scalacOptions += "-Yretain-trees", // For debugging when writing macros

    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0-M10" % Test
  )

lazy val cats = project
  .in(file("./cats"))
  .dependsOn(core)
  .settings(
    name := "scala3mock-cats",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.10.0",
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "1.0.0-M10" % Test,
      "org.typelevel" %% "cats-effect" % "3.5.2" % Test
    )
  )

lazy val scalatest = project
  .in(file("./scalatest"))
  .dependsOn(core)
  .settings(
    name := "scala3mock-scalatest",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.17"
  )

lazy val docs = project
  .in(file("site-docs")) // important: it must not be docs/
  .dependsOn(core, cats, scalatest)
  .enablePlugins(MdocPlugin, DocusaurusPlugin)
  .settings(
    publish / skip := true,
    mdocVariables := Map(
      "VERSION" -> (core / version).value
    ),
    libraryDependencies += "org.typelevel" %% "cats-effect" % "3.5.2"
  )
