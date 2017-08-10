
name := "Akka Http Sample"

version := "0.1"

scalaVersion := Common.scalaVersion

resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/snapshots/")

libraryDependencies ++= Deps.sampleDeps

enablePlugins(JavaAppPackaging)
