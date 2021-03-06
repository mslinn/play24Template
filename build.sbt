import sbt._
import sbt.Keys._

name         := "changeMe"
organization := "com.micronautics"
version      := "0.1.0"

herokuAppName in Compile := "changeMe"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8", "-feature", "-target:jvm-1.6", "-unchecked",
  "-Ywarn-adapted-args", "-Ywarn-value-discard", "-Xlint")

javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked", "-source", "1.7", "-target", "1.7", "-g:vars")

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  evolutions,
  filters,
  json,
  "com.typesafe.play"      %% "anorm"              % "2.5.0" withSources(),
  "com.typesafe.play"      %% "play-ebean"         % "1.0.0" withSources(),
  "com.typesafe.play"      %% "play-mailer"        % "3.0.1" withSources(),
  "com.typesafe.akka"      %% "akka-slf4j"         % "2.3.9",
  "org.webjars"            %% "webjars-play"       % "2.4.0-1",
  "org.webjars"            %  "bootstrap"          % "3.1.1-2",
  "com.github.tototoshi"   %% "slick-joda-mapper"  % "2.0.0" withSources(),
  "com.typesafe"           %  "config"             % "1.2.1" withSources(),
  "com.typesafe.slick"     %% "slick"              % "3.0.0" withSources(),
  "postgresql"             %  "postgresql"         % "9.1-901-1.jdbc4" withSources(),
  "com.github.nscala-time" %% "nscala-time"        % "2.0.0" withSources(),
  "org.webjars"            %  "jquery-ui"          % "1.10.2-1",
  "org.webjars"            %  "jquery-ui-themes"   % "1.10.0",
  //
  "org.scalatestplus"      %% "play"               % "1.4.0-M3" % "test" withSources(),
  "junit"                  %  "junit"              % "4.8.1" % "test"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
enablePlugins(ApiMappings)

javaOptions in Test ++= Seq("-Dconfig.file=conf/dev.conf")
logBuffered in Test := false
Keys.fork in Test := false
//parallelExecution in Test := false

resolvers ++= Seq(
  "webjars" at "http://webjars.github.com/m2",
  //Resolver.file("Local Repository", file(sys.env.get("PLAY_HOME").map(_ + "/repository/local").getOrElse("")))(Resolver.ivyStylePatterns),
  Resolver.url("play-plugin-releases", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)
)

// define the statements initially evaluated when entering 'console', 'console-quick', or 'console-project'
initialCommands := """import scala.language.postfixOps
                     |import java.net.URL
                     |import java.text.DateFormat
                     |import java.util.Locale
                     |import play.api._
                     |import play.api.db.DB
                     |import play.api.i18n._
                     |import play.api.libs.json._
                     |import play.api.Play.current
                     |import play.Logger
                     |import scala.reflect.runtime.universe._
                     |import views.html.helper._
                     |""".stripMargin

logLevel := Level.Warn

logLevel in test := Level.Info // Level.Info is needed to see detailed output when running tests

logLevel in compile := Level.Warn

