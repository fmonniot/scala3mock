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

    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0-M11" % Test
  )

lazy val cats = project
  .in(file("./cats"))
  .dependsOn(core)
  .settings(
    name := "scala3mock-cats",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.9.0",
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "1.0.0-M11" % Test,
      "org.typelevel" %% "cats-effect" % "3.5.7" % Test
    )
  )

lazy val scalatest = project
  .in(file("./scalatest"))
  .dependsOn(core)
  .settings(
    name := "scala3mock-scalatest",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19"
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
    libraryDependencies += "org.typelevel" %% "cats-effect" % "3.5.7"
  )

/* This project is a bit particular in how it operates. Because compiling the library itself
  is restricted to 3.2.x at the moment, we cannot use the regular cross compilation scheme for
  testing. Instead, what we do is that we compile the library using 3.2.2, and then depend on
  that compiled jar in the integration tests using more recent version of the compiler.

  This project is only used to provide this integration point (the src directory only contain
  a symlink to the core tests). See the GitHub Actions to see how to use it.

 */
lazy val integration = project
  .in(file("./integration"))
  .settings(
    name := "scala3mock-integration-tests",
    crossScalaVersions := Seq("3.3.3", "3.4.0", "3.5.0-RC1"),

    // Note that this means we need to publish core via publishLocal first.
    libraryDependencies += "eu.monniot" %% "scala3mock" % version.value % Test,
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0-M11" % Test
  )
