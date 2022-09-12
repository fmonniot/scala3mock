val scala3Version = "3.2.0"

lazy val root = project
  .in(file("."))
  .aggregate(scala3mock)

lazy val scala3mock = project.in(file("./scala3mock"))
  .settings(
    name := "scala3mock",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    // The following system property enable a (very) verbose output when
    // this project's macros are executing. I do not know of a better way
    // to set the system property and have it made available to the compiler.
    // Hack found at https://github.com/scalameta/scalameta/issues/840#issuecomment-299962849
    initialize := {
      val () = sys.props("scala3mock.debug.macros") = "true"
      initialize.value
    },

    // Useful when using the PrintAst[type] macro. That will provides the implementation
    // details of classes. Without it, only the signatures are available.
    //scalacOptions += "-Yretain-trees", // For debugging when writing macros

    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
  )
