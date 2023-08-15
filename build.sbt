import sbt.Keys._
import sbt._
//import Dependencies._

// scalafmt: { maxColumn = 120, align.preset = more }

lazy val Versions = new {
  val awssdk = "1.11.728" // matches kinesis adaptor
  //    val awssdk = "1.12.528"
  val awssdk2 = "2.15.14" // 2.14.x series lacks waiter classes
//  val awssdk2 = "2.20.125" // 2.14.x series lacks waiter classes
  val finch          = "0.32.1"
  val kinesisAdaptor = "1.5.1"
  val netty          = "4.1.91.Final"
  //    val netty = "4.1.53.Final"
  val nettyRouter = "2.2.0"
  val twitter     = "20.9.0"
}

lazy val scala213 = "2.13.8"

ThisBuild / scalaVersion := scala213

lazy val sharedSettings: Seq[Def.Setting[_]] = Seq(
  Compile / packageDoc / publishArtifact := false,
  asciiGraphWidth := 1000000,
  javacOptions ++= Seq("-source", "11", "-target", "11")
)

lazy val defaultSettings = Defaults.coreDefaultSettings ++ sharedSettings

lazy val hypervolt = (project in file("."))
  .settings(
    name := "hypervolt",
    moduleName := "hypervolt",
    // Commenting this line sometimes works around Finagle bug, depends on the day of week, see https://github.com/twitter/finagle/issues/955
    ThisBuild / useCoursier := false,
    defaultSettings
  )
  .aggregate(
    hvDomain
  )

lazy val hvDomain = (project in file("libs/hv-domain"))
  .settings(
    defaultSettings,
    sharedSettings,
    CustomMergeStrat.mergeStrat,
    assemblyShadeRules in assembly ++= Seq(
//      ShadeRule.rename("io.netty.**" -> "aws_version_of_netty.@1")
//        .inLibrary("com.amazonaws" % "aws-java-sdk-kinesis" % Versions.awssdk),

      // Doesn't work
      ShadeRule.rename("io.netty.**" -> "aws2_version_of_netty.@1")
        .inLibrary("software.amazon.awssdk" % "kinesis" % Versions.awssdk2)
//        .inProject
    ),
    moduleName := "hv-domain",
    libraryDependencies ++= Seq(
      "software.amazon.awssdk" % "kinesis"      % Versions.awssdk2,
      "com.twitter"           %% "util-core"    % Versions.twitter,
      "com.lihaoyi"           %% "upickle"      % "1.6.0",
      "io.netty"               % "netty-buffer" % Versions.netty
    )
  )

asciiGraphWidth := 1000000
