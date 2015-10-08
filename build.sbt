name := """Beautify"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

//libraryDependencies ++= Seq(
//  jdbc,
//  cache,
//  ws,
//  specs2 % Test
//)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator


scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xlint")

val javacppVersion = "1.0"

val opencvVersion = "3.0.0"

// Platform classifier for native library dependencies
lazy val platform = org.bytedeco.javacpp.Loader.getPlatform

// Some dependencies like `javacpp` are packaged with maven-plugin packaging
classpathTypes += "maven-plugin"

libraryDependencies ++= Seq(
  "org.bytedeco"                 % "javacpp"         % javacppVersion,
  "org.bytedeco"                 % "javacv"          % javacppVersion,
  "org.bytedeco.javacpp-presets" % "opencv"          % ( opencvVersion + "-" + javacppVersion) classifier platform classifier "",
  "org.scala-lang.modules"      %% "scala-swing"     % "1.0.1",
  "junit"                        % "junit"           % "4.12" % "test",
  "com.novocode"                 % "junit-interface" % "0.11" % "test",
  "org.apache.kafka" %% "kafka" % "0.8.2.1"
    exclude("javax.jms", "jms")
    exclude("com.sun.jdmk", "jmxtools")
    exclude("com.sun.jmx", "jmxri"),
  "com.typesafe" % "config" % "1.2.1"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

autoCompilerPlugins := true

fork in run := false