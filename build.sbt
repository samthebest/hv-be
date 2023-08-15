import sbt.Keys._
import sbt._
//import Dependencies._

// scalafmt: { maxColumn = 120, align.preset = more }

lazy val Versions = new {
  val awssdk = "1.11.728" // matches kinesis adaptor
  //    val awssdk = "1.12.528"
//  val awssdk2 = "2.15.14"
//  val awssdk2 = "2.20.125" // works with 4.1.91.Final
//  val awssdk2 = "2.17.295" // Pulls in 4.1.77.Final
  val awssdk2 = "2.16.104" // Pulls in 4.1.73.Final and WORKS
  val finch          = "0.33.0" // max version, pulls in 4.1.73.Final
  val kinesisAdaptor = "1.5.1"
//  val netty          = "4.1.91.Final"
  // Max version for finch
  val netty          = "4.1.73.Final"
//  val netty          = "4.1.46.Final"
  //    val netty = "4.1.53.Final"
  val nettyRouter = "2.2.0"
  val twitter     = "20.9.0"
}

lazy val scala213 = "2.13.8"

ThisBuild / scalaVersion := scala213

lazy val sharedSettings: Seq[Def.Setting[_]] = Seq(
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
//    assemblyShadeRules in assembly ++= Seq(
////      ShadeRule.rename("io.netty.**" -> "aws_version_of_netty.@1")
////        .inLibrary("com.amazonaws" % "aws-java-sdk-kinesis" % Versions.awssdk),
//
//      // Doesn't work
//      ShadeRule.rename("io.netty.**" -> "aws2_version_of_netty.@1")
//        .inLibrary("software.amazon.awssdk" % "kinesis" % Versions.awssdk2)
////        .inProject
//    ),
    moduleName := "hv-domain",
    libraryDependencies ++= Seq(
      "software.amazon.awssdk" % "kinesis"      % Versions.awssdk2,
      "com.twitter"           %% "util-core"    % Versions.twitter,
      "com.lihaoyi"           %% "upickle"      % "1.6.0",
      "io.netty"               % "netty-buffer" % Versions.netty,

      // "0.32.1" causes deduplicate errors in the build
      // "0.33.0" builds but throws exception
      "com.github.finagle" %% "finchx-circe"     % Versions.finch
    )
  )

asciiGraphWidth := 1000000
