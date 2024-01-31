import Dependencies.*

ThisBuild / scalaVersion := "2.13.12"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

val kieVersion = "9.44.0.Final"
resolvers += Resolver.mavenLocal
lazy val root = (project in file("."))
  .settings(
    name := "dmn-bench",
    libraryDependencies ++= Seq(
      munit % Test,
      "org.kie" % "kie-dmn-core" % kieVersion
    )
  )
  .enablePlugins(JmhPlugin)

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
