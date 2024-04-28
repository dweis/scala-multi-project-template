val projectName = "scala-multi-project-template"
val projectVersion = "0.1.0-SNAPSHOT"
val scala3Version = "3.3.1"
val http4sVersion = "0.23.26"
val circeVersion = "0.14.7"

lazy val commonSettings = Seq(
  scalaVersion := scala3Version,
  organization := "com.example",
  version := projectVersion,
  assembly / assemblyMergeStrategy := {
    case "module-info.class" => MergeStrategy.discard
    case x                   => (assembly / assemblyMergeStrategy).value.apply(x)
  },
  Compile / run / fork := true
)

lazy val catsDeps = Seq(
  "ch.qos.logback" % "logback-classic" % "1.5.6",
  "org.typelevel" %% "cats-effect" % "3.5.4",
  "org.typelevel" %% "log4cats-slf4j" % "2.6.0"
)

lazy val http4sDeps = Seq(
  "org.http4s" %% "http4s-ember-client" % http4sVersion,
  "org.http4s" %% "http4s-ember-server" % http4sVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "org.http4s" %% "http4s-scalatags" % "0.25.2",
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "com.lihaoyi" %% "scalatags" % "0.13.1"
)

lazy val testDeps = Seq(
  "org.scalameta" %% "munit" % "0.7.29" % Test,
  "org.typelevel" %% "munit-cats-effect-3" % "1.0.6" % Test
)

lazy val test = project
  .in(file("libs/test"))
  .settings(
    commonSettings,
    name := "test-lib",
    libraryDependencies ++= testDeps
  )

lazy val server = project
  .in(file("apps/server"))
  .settings(
    commonSettings,
    name := "server",
    libraryDependencies ++= catsDeps ++ http4sDeps ++ testDeps,
    assembly / mainClass := Some("com.example.server.Main"),
    assembly / assemblyJarName := "server.jar"
  )
  .dependsOn(test)

lazy val cli = project
  .in(file("apps/cli"))
  .settings(
    commonSettings,
    name := "cli",
    libraryDependencies ++= catsDeps ++ testDeps,
    assembly / mainClass := Some("com.example.cli.Main"),
    assembly / assemblyJarName := "cli.jar"
  )
  .dependsOn(test)

lazy val root = project
  .in(file("."))
  .settings(
    commonSettings,
    name := projectName,
    organization := "com.example",
    version := "0.1.0-SNAPSHOT"
  )
