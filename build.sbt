import sbt._
import sbt.Keys._

name         := "play24Template"
organization := "com.micronautics"
version      := "0.1.0"

scalaVersion := "2.11.6"

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
  "com.typesafe.play"      %% "anorm"              % "2.4.0",
  "com.typesafe.play"      %% "play-ebean"         % "1.0.0"
  "com.typesafe.akka"      %% "akka-slf4j"         % "2.3.9",
  "org.webjars"            %% "webjars-play"       % "2.4.0-M3-1",
  "org.webjars"            % "bootstrap"           % "3.1.1-2",
  "com.github.tototoshi"   %% "slick-joda-mapper"  % "0.4.1" withSources(),
  "com.typesafe"           %  "config"             % "1.2.1" withSources(),
  "com.typesafe.slick"     %% "slick"              % "1.0.1" withSources(),
  "postgresql"             %  "postgresql"         % "9.1-901-1.jdbc4" withSources(),
  "com.github.nscala-time" %% "nscala-time"        % "0.2.0" withSources(),
  "org.webjars"            %  "jquery-ui"          % "1.10.2-1",
  "org.webjars"            %  "jquery-ui-themes"   % "1.10.0",
  "org.webjars"            %% "webjars-play"       % "2.2.2-1",
  "securesocial"           %% "securesocial"       % "2.1.4" withSources(),
  //
  "org.scalatestplus"      %% "play"               % "1.2.0" % "test"
  "junit"                  %  "junit"              % "4.8.1"  % "test",
  "com.typesafe.play"      %% "play-test"          % "2.2.0"  % "test" withSources()
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)

javaOptions in Test ++= Seq("-Dconfig.file=conf/dev.conf")
logBuffered in Test := false
Keys.fork in Test := false
//parallelExecution in Test := false


fork in run := true

resolvers ++= Seq(
  "webjars" at "http://webjars.github.com/m2",
  //Resolver.file("Local Repository", file(sys.env.get("PLAY_HOME").map(_ + "/repository/local").getOrElse("")))(Resolver.ivyStylePatterns),
  Resolver.url("play-plugin-releases", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)
)

// define the statements initially evaluated when entering 'console', 'console-quick', or 'console-project'
initialCommands := """ // make app resources accessible
                     |Thread.currentThread.setContextClassLoader(getClass.getClassLoader)
                     |new play.core.StaticApplication(new java.io.File("."))
                     |
                     |//import play.api.{ DefaultApplication, Mode, Play }
                     |//val applicationPath = new java.io.File(".")
                     |//val classLoader = this.getClass.getClassLoader
                     |//val sources = None
                     |//val applicationMode = Mode.Dev
                     |//Play.start(new DefaultApplication(applicationPath, classLoader, sources, applicationMode))
                     |import com.github.tototoshi.slick.JodaSupport._
                     |import java.net.URL
                     |import java.text.DateFormat
                     |import java.util.Locale
                     |import org.joda.time._
                     |import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
                     |import play.api.db.DB
                     |import play.api.libs.json._
                     |import play.api.Play.current
                     |import play.Logger
                     |import scala.slick.driver.PostgresDriver.simple._
                     |import scala.reflect.runtime.universe._
                     |import views.html.helper._
                     |import views.html.tags._
                     |""".stripMargin

logLevel := Level.Warn

logLevel in test := Level.Info // Level.Info is needed to see detailed output when running tests

logLevel in compile := Level.Warn

