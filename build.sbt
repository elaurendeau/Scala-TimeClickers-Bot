name := "ScalaTimeClickers"

version := "1.0"

scalaVersion := "2.11.8"


javacOptions in Compile ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-Xlint:deprecation")
resolvers += "mvnrepository" at "http://mvnrepository.com/artifact/"

libraryDependencies +=  "org.scala-lang" % "scala-compiler" % "2.11.8"
libraryDependencies +=  "org.scala-lang" % "scala-reflect" % "2.11.8"
libraryDependencies +=  "org.scala-lang" % "scala-library" % "2.11.8"

libraryDependencies += "org.scalafx" % "scalafx_2.11" % "8.0.92-R10"
libraryDependencies += "org.scalafx" % "scalafxml-core_2.11" % "0.2.1"
libraryDependencies += "org.scalafx" % "scalafxml-subcut_2.11" % "0.2.1"

libraryDependencies += "org.scalamacros" % "paradise_2.11.8" % "2.1.0"

libraryDependencies += "joda-time" % "joda-time" % "2.9.4"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.4.7"


addCompilerPlugin(("org.scalamacros" % "paradise" % "2.1.0") cross CrossVersion.full)


mergeStrategy in assembly <<= (mergeStrategy in assembly) {
  (old) => {
    case "scalac-plugin.xml" => MergeStrategy.discard
    case x => old(x)
  }
}