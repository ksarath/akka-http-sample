
import sbt._

object Deps {
  val akkaV         = "2.5.1"
  val akkaHttpV     = "10.0.5"
  val jackson       = "2.8.5"

  val baseDeps = Seq(
    "com.typesafe.akka"               %% "akka-actor"                             % akkaV,
    "com.typesafe.akka"               %% "akka-contrib"                           % akkaV,
    "com.typesafe.akka"               %% "akka-http"                              % akkaHttpV,
    "com.typesafe.akka"               %% "akka-http-spray-json"                   % akkaHttpV,
    "com.typesafe.akka"               %% "akka-stream"                            % akkaV,
    "io.spray"                        %% "spray-json"                             % "1.3.3",
    "com.fasterxml.jackson.core"      %  "jackson-core"	      	                  % jackson,
    "com.typesafe.akka"               %% "akka-slf4j"                             % akkaV,
    "com.github.swagger-akka-http"    %% "swagger-akka-http"                      % "0.8.2",
    "com.typesafe"                    %  "config"                                 % "1.3.0",
    "com.typesafe.scala-logging"      %% "scala-logging"                          % "3.4.0",
    "ch.qos.logback"                  %  "logback-classic"                        % "1.1.3",
    "org.slf4j"                       %  "slf4j-api"                              % "1.7.12",
    "org.scalatest"                   %% "scalatest"                              % "3.0.1"    % "test",
    "com.typesafe.akka"               %% "akka-testkit"                           % akkaV      % "test",
    "com.typesafe.akka"               %% "akka-http-testkit"                      % akkaHttpV  % "test",
    "com.typesafe.akka"               %% "akka-stream-testkit"                    % akkaV      % "test"
  )

  val sampleDeps = baseDeps

  //val sample1Deps = baseDeps ++ Seq(
  //  "org.apache.commons"              %  "commons-email"                          % "1.4"
  //)
}
