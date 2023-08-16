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


  val algebird = "0.13.7"
  val auth0JavaJwt = "3.10.1"
  val auth0JwksRsa = "0.11.0"



  val bouncyCastle = "1.67"
  val caffeine = "3.0.2"
  val chimney = "0.7.5"
  val circe = "0.13.0"
  val config = "1.3.4"
  val dropwizard = "4.2.15"
  val elastic4s = "7.9.1"
  val googleAPIVersion = "1.30.4"
  val jam = "0.1.0"
//  val kinesisAdaptor = "1.5.1"
  val logback = "1.4.7"
//  val nettyRouter = "2.2.0"
  val scalacheck = "3.2.10.0"
  val scalatest = "3.2.10"
  val scanamo = "1.0.0-M15"
  val scodec = "1.11.7"
  val shapeless = "2.3.3"
  val slf4j = "2.0.7"
  val squants = "1.6.0"
  val testContainers = "0.40.8"
//  val twitter = "20.9.0"
  val typesafeConfig = "1.3.4"
  val util = "0.57.0"
  val lambda = "1.2.2"
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
      "com.github.finagle" %% "finchx-circe"     % Versions.finch,


//      // Most deps from device-front-end
////      "ch.qos.logback" % "logback-classic" % Versions.logback,
//      "com.amazonaws" % "dynamodb-streams-kinesis-adapter" % Versions.kinesisAdaptor,
////      "com.github.ben-manes.caffeine" % "caffeine" % Versions.caffeine,
//      "com.github.finagle" %% "finchx-core" % Versions.finch,
//      "com.github.finagle" %% "finchx-circe" % Versions.finch,
//      "com.github.finagle" %% "finchx-fs2" % Versions.finch,
//      "com.twitter" %% "twitter-server" % Versions.twitter,
////      "com.typesafe" % "config" % Versions.config,
//      "software.amazon.awssdk" % "ses" % Versions.awssdk2,
//      "io.netty" % "netty-handler" % Versions.netty,
//      "io.netty" % "netty-transport-native-epoll" % Versions.netty,
//      "io.netty" % "netty-codec-mqtt" % Versions.netty,
//      "tv.cntt" % "netty-router" % Versions.nettyRouter,
////      "com.google.api-client" % "google-api-client" % Versions.googleAPIVersion,
////      "org.bouncycastle" % "bcprov-jdk15on" % Versions.bouncyCastle,
//      // Worked up to here
//      "org.typelevel" %% "cats-effect" % "2.3.3",
////      "com.softwaremill.sttp.client3" %% "core" % "3.8.15" % "it, test",
//      "com.softwaremill.sttp.client3" %% "core" % "3.8.15" % "test",
////      "org.scalatest" %% "scalatest" % Versions.scalatest % "it,test",
//      "org.scalatestplus" %% "mockito-4-11" % "3.2.16.0" % Test,
//
//
//      // auth0
////      "org.bouncycastle" % "bcprov-jdk15on" % Versions.bouncyCastle,
////      "com.auth0" % "jwks-rsa" % Versions.auth0JwksRsa exclude("com.fasterxml.jackson.core", "jackson-databind"),
////      "com.auth0" % "java-jwt" % Versions.auth0JavaJwt exclude("com.fasterxml.jackson.core", "jackson-databind"),
////      "io.circe" %% "circe-generic" % Versions.circe,
////      "com.outworkers" %% "util-samplers" % Versions.util % Test,
//
//
//      // Adoption
//      "org.scanamo" %% "scanamo" % Versions.scanamo,
//      "io.circe" %% "circe-generic" % Versions.circe,
//      "com.outworkers" %% "util-samplers" % Versions.util % Test,
//
//      // Worked up to here
//      // charger
//      "org.scanamo" %% "scanamo" % Versions.scanamo,
//      "io.circe" %% "circe-generic" % Versions.circe,
//      "com.outworkers" %% "util-samplers" % Versions.util % Test,
//
//      // chargerState
//      "org.scanamo" %% "scanamo" % Versions.scanamo,
//      "io.circe" %% "circe-generic" % Versions.circe,

      // collector
      "ch.qos.logback" % "logback-classic" % Versions.logback,
      "com.amazonaws" % "dynamodb-streams-kinesis-adapter" % Versions.kinesisAdaptor,
      "com.github.finagle" %% "finchx-core" % Versions.finch,
      "com.github.finagle" %% "finchx-circe" % Versions.finch,
      "com.github.yakivy" %% "jam-core" % Versions.jam,
      "com.twitter" %% "algebird-core" % Versions.algebird,
      "com.twitter" %% "finagle-http" % Versions.twitter,
      "com.twitter" %% "finagle-stats" % Versions.twitter,
      "com.twitter" %% "twitter-server" % Versions.twitter,
      "com.typesafe" % "config" % Versions.config,
      "io.dropwizard.metrics" % "metrics-core" % Versions.dropwizard,
      "io.netty" % "netty-handler" % Versions.netty,
      "io.netty" % "netty-transport-native-epoll" % Versions.netty,
      "software.amazon.awssdk" % "cloudwatch" % Versions.awssdk2,
      "software.amazon.awssdk" % "kinesis" % Versions.awssdk2,
      "io.scalaland" %% "chimney" % Versions.chimney,
      "com.github.pathikrit" %% "better-files" % "3.9.1",
      "com.outworkers" %% "util-samplers" % Versions.util % Test,
      "com.outworkers" %% "util-validators-cats" % Versions.util % Test,
      "org.scalatestplus" %% "scalacheck-1-15" % Versions.scalacheck % Test,
    )
  )

asciiGraphWidth := 1000000
