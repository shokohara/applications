ThisBuild / scalaVersion := "2.12.8"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.github.shokohara"
ThisBuild / organizationName := "Sho Kohara"

lazy val root = (project in file(".")).aggregate(seed, lol, slack, web)
lazy val slack = (project in file("slack"))
  .settings(commonSettings)
  .settings(
    scalacOptions +=  "-Ypartial-unification",
    libraryDependencies ++= Seq(
      "com.github.seratch" % "jslack" % "1.1.7",
      "eu.timepit" %% "refined" % refinedVersion,
      "eu.timepit" %% "refined-pureconfig" % refinedVersion,
      "com.github.pureconfig" %% "pureconfig" % "0.10.1",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "org.typelevel" %% "kittens" % "1.2.0",
      "io.chrisdavenport" % "cats-time_2.12" % "0.2.0",
      "org.typelevel" %% "cats-effect" % "1.2.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-postgres" % doobieVersion,
      "org.tpolecat" %% "doobie-specs2" % doobieVersion
    ) ++ commonLibraryDependencies
  )
lazy val seed = (project in file("seed")).settings(
  libraryDependencies += scalaTest
)
lazy val lol = (project in file("lol"))
  .settings(commonSettings)
  .settings(
//    fork in run := true,
//    connectInput := true,
    resolvers += "jitpack" at "https://jitpack.io",
    libraryDependencies ++= Seq(scalaTest,
      "com.merakianalytics.orianna" % "orianna" % "4.0.0-rc1",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.github.taycaldwell" % "riot-api-java" % "4.1.0",
      "org.typelevel" %% "cats-effect" % "1.2.0")
  )

lazy val web = (project in file("web"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      scalaTest,
      "be.venneborg" %% "play26-refined" % "0.3.0",
      "com.softwaremill.macwire" %% "macros" % "2.3.1" % Provided,
      "com.dripower" %% "play-circe" % "2711.0",
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "io.circe" %% "circe-java8" % circeVersion,
      "io.circe" %% "circe-refined" % circeVersion
    ) ++ silencers ++ commonLibraryDependencies,
    buildInfoKeys := Seq[BuildInfoKey](
      "gitHeadCommit" -> git.gitHeadCommit.value.getOrElse(""),
      "gitHeadCommitDate" -> git.gitHeadCommitDate.value.getOrElse(""),
      "gitHeadMessage" -> git.gitHeadMessage.value.getOrElse(""),
      "gitBranch" -> git.gitCurrentBranch.value
    ),
    buildInfoOptions += BuildInfoOption.ToMap,
    routesImport ++= Seq(
      "be.venneborg.refined.play.RefinedPathBinders._",
      "be.venneborg.refined.play.RefinedQueryBinders._",
      "com.github.shokohara.playextra.QueryStringBindable._",
      "eu.timepit.refined.types.string._"
    ),
    dockerBaseImage := "openjdk:8u181-jdk-stretch",
    daemonUser in Docker := "root",
    dockerEntrypoint := Seq("/bin/sh", "-c"),
    dockerCmd := Seq("/opt/docker/bin/web"),
  )
  .enablePlugins(BuildInfoPlugin)
  .enablePlugins(PlayScala)
  .dependsOn(slack)

lazy val circeVersion = "0.11.1"
lazy val refinedVersion = "0.9.4"
lazy val doobieVersion = "0.6.0"

lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % Test

lazy val silencers = {
  val version = "1.3.0"
  "com.github.ghik" %% "silencer-lib" % version % Provided ::
    compilerPlugin("com.github.ghik" %% "silencer-plugin" % version) :: Nil
}

lazy val commonLibraryDependencies = Seq(
  "com.github.bigwheel" %% "util-backports" % "1.1"
)
lazy val commonSettings = Seq(
  addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.8")
)
addCommandAlias("fmt", "; compile:scalafmt; test:scalafmt; scalafmtSbt")
addCommandAlias("prePR", "; fmt; test")
