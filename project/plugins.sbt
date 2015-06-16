import sbt._
import sbt.Keys._

resolvers ++= Seq(
  "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/",
  "SBT Community repository" at "http://dl.bintray.com/sbt/sbt-plugin-releases/"
)

// Comment to get more information during initialization
//logLevel := Level.Warn

addSbtPlugin("com.typesafe.play" % "sbt-plugin"       % "2.4.0")

// optional plugins
addSbtPlugin("com.typesafe.sbt"  % "sbt-coffeescript" % "1.0.0")
addSbtPlugin("com.typesafe.sbt"  % "sbt-less"         % "1.0.6")
addSbtPlugin("com.typesafe.sbt"  % "sbt-jshint"       % "1.0.3")
//addSbtPlugin("com.typesafe.sbt"  % "sbt-rjs"          % "1.0.7")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.8")

addSbtPlugin("de.johoop" % "findbugs4sbt" % "1.4.0")

libraryDependencies ++= Seq(
  "com.puppycrawl.tools" % "checkstyle" % "6.3"
)
