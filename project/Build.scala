import sbt._

/**
 * Multi-project setup
 */
object AkkaHttpSampleBuild extends Build {

  lazy val akkahttpsample = project.in( file(".") )
                 .aggregate(sample)

  lazy val sample = project

  //lazy val sample2 = project dependsOn(sample, sample1 % "test")
}
