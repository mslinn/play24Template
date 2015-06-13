import sbt._
import sbt.Keys._

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

// Comment to get more information during initialization
//logLevel := Level.Warn

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.0")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.8")
