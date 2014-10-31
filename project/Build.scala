import sbt._
import sbt.Configuration
import sbt.Keys._
import Release._

import de.johoop.jacoco4sbt.JacocoPlugin._
import sbtunidoc.Plugin._
import sbtunidoc.Plugin.UnidocKeys._

import scala.language.postfixOps

object Build extends Build {

  val writeVersion = taskKey[Unit]("Write the version into a file.")

  val moduleDependencyGraph = taskKey[Unit]("creates an image which shows the dependencies between the SBT modules")

  val genDoc = taskKey[Seq[File]]("generates the documentation")

  lazy val `jvm-sdk` = (project in file(".")).
    settings(standardSettings:_*).
    settings(
      libraryDependencies += Libs.junitDep
    ).
    settings(unidocSettings:_*).
    settings(docSettings:_*).
    settings(javaUnidocSettings:_*).
    aggregate(common, customers, `java-client`, models, `integration-test-lib`, inventory, products, `sphere-play-sdk`).
    dependsOn(`sphere-play-sdk`, `integration-test-lib`).settings(
      writeVersion := {
        IO.write(target.value / "version.txt", version.value)
      },
      unidoc in Compile <<= (unidoc in Compile).dependsOn(writeVersion),
      moduleDependencyGraph in Compile := {
        val projectToDependencies = projects.filterNot(_.id.toLowerCase.contains("test")).map { p =>
          val id = p.id
          val deps = p.dependencies.map(_.project).collect { case LocalProject(id) => id} filterNot(_.toLowerCase.contains("test"))
          (id, deps)
        }.toList
        val x = projectToDependencies.map { case (id, deps) =>
          deps.map(dep => '"' + id + '"' + "->" + '"' + dep + '"').mkString("\n")
        }.mkString("\n")
        val content = s"""digraph TrafficLights {
                        |$x
                        |
                        |
                        |overlap=false
                        |label="Module Structure in JVM SDK"
                        |fontsize=12;
                        |}
                        |""".stripMargin
        val graphvizFile = target.value / "deps.txt"
        val imageFile = baseDirectory.value / "documentation-resources" / "architecture" / "deps.png"
        IO.write(graphvizFile, content)
        //requires graphviz http://www.graphviz.org/Download_macos.php
        s"neato -Tpng $graphvizFile" #> imageFile !
      },
      genDoc <<= (baseDirectory, target in unidoc) map { (baseDir, targetDir) =>
        val destination = targetDir / "javaunidoc" / "documentation-resources"
        IO.copyDirectory(baseDir / "documentation-resources", destination)
        IO.listFiles(destination)
      },
      genDoc <<= genDoc.dependsOn(unidoc in Compile)
    )

  lazy val models = project.settings(javaClientSettings:_*).dependsOn(`integration-test-lib` % "it", products).configs(IntegrationTest)

  lazy val `sphere-play-sdk` = (project in file("play-sdk"))
    .dependsOn(models, `java-client`)
    .settings(standardSettings:_*)
    .settings(javacSettings:_*)
    .settings(genjavadocSettings:_*)
    .settings(docSettings:_*)
    .configs(IntegrationTest)

  def javaProject(name: String) =
    Project(id = name, base = file(name), settings = javaClientSettings ++ jacoco.settings ++ standardSettings).
    configs(IntegrationTest)

  lazy val `java-client` = Project(
    id = "java-client",
    base = file("java-client"),
    settings = javaClientSettings
  ).configs(IntegrationTest).dependsOn(common).settings(docSettings: _*)

  lazy val common = javaProject("common").settings(
//sbt buildinfo plugin cannot be used since the generated class requires Scala
    sourceGenerators in Compile <+= (sourceManaged in Compile, version) map { (outDir, v) =>
      val file = outDir / "io" / "sphere" / "sdk" / "meta" / "BuildInfo.java"
      IO.write(file, """
package io.sphere.sdk.meta;

// Don't edit this file - it is autogenerated by sbt
public final class BuildInfo {
    private BuildInfo() {
      //utility class
    }

    public static String version() {
      return """" + v + """";
    }
}
""")
      Seq(file)
    }
  )

  lazy val customers = javaProject("customers").dependsOn(`integration-test-lib` % "test,it", common)

  lazy val inventory = javaProject("inventory").dependsOn(`integration-test-lib` % "test,it", common)

  lazy val products = javaProject("products").dependsOn(`integration-test-lib` % "test,it", customers, inventory)

  lazy val orders = javaProject("orders").dependsOn(`integration-test-lib` % "test,it", common, customers)

  lazy val `integration-test-lib` = javaProject("integration-test-lib").
    dependsOn(`java-client`, common).
    settings(
      libraryDependencies ++= Seq(Libs.festAssert, Libs.junitDepRaw, Libs.junitInterface)
    )

  lazy val jacksonJsonMapperLibraries =
      "com.fasterxml.jackson.core" % "jackson-annotations" % "2.4.3" ::
      "com.fasterxml.jackson.core" % "jackson-core" % "2.4.3" ::
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.3" ::
      "com.fasterxml.jackson.module" % "jackson-module-parameter-names" % "2.4.3" ::
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % "2.4.3" ::
      "org.zapodot" % "jackson-databind-java-optional" % "2.4.1" ::
      Nil

  lazy val javaClientSettings = Defaults.defaultConfigs ++ standardSettings ++ javacSettings ++
    genjavadocSettings ++ docSettings ++
    testSettings(Libs.logbackClassic, Libs.javaLoggerBridge, Libs.junitDep) ++ Seq(
    crossPaths := false,
    parallelExecution in IntegrationTest := false,
    libraryDependencies ++= jacksonJsonMapperLibraries,
    libraryDependencies ++=
      "com.ning" % "async-http-client" % "1.8.7" ::
        "net.jcip" % "jcip-annotations" % "1.0" ::
        "com.typesafe" % "config" % "1.2.0" ::
        "com.neovisionaries" % "nv-i18n" % "1.12" ::
        "org.apache.commons" % "commons-lang3" % "3.3.2" ::
        "com.github.slugify" % "slugify" % "2.1.2" ::
        "org.javamoney" % "moneta" % "0.9" ::
        Libs.junitInterface % "test,it" ::
        Libs.junitDepRaw % "test,it" ::
        Nil
  )

  val Snapshot = "SNAPSHOT"
  def isOnJenkins() = scala.util.Properties.envOrNone("JENKINS_URL").isDefined

  lazy val standardSettings = publishSettings ++ Seq(
    version <<= version in ThisBuild,
    licenses := Seq("Apache" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    homepage := Some(url("https://github.com/sphereio/sphere-jvm-sdk")),
    version in ThisBuild <<= (version in ThisBuild) { v =>
      //Jenkins is supposed to publish every snapshot artifact which can be distinguished per Git commit.
      if (isOnJenkins) {
        val shortenedGitHash = Process("git rev-parse HEAD").lines.head.substring(0, 7)
        if(v.endsWith(Snapshot) && !v.contains(shortenedGitHash)){
          v.replace(Snapshot, "") + shortenedGitHash + "-" + Snapshot
        }else {
          v
        }
      } else {
        v
      }
    }
  ) ++ docSettings

  lazy val docSettings = Seq(
    javacOptions in (Compile, doc) := Seq("-overview", "documentation-resources/javadoc-overview.html", "-notimestamp", "-taglet", "CodeTaglet", "-taglet", "DocumentationTaglet",
      "-tagletpath", "./project/target/scala-2.10/sbt-0.13/classes",
      "-bottom", """<link rel='stylesheet' href='http://yandex.st/highlightjs/7.4/styles/default.min.css'><script src='http://yandex.st/highlightjs/7.4/highlight.min.js'></script><script>hljs.initHighlightingOnLoad();</script><style>code {font-size: 1.0em;font-family: monospace;}</style><script src='https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js'></script>""" +
        """\n<span id='custom-javascripts'></span>\n<script>var pathPrefix = $(\".navList a[href$='index-all.html']\").attr(\"href\").replace(\"index-all.html\", \"\"); var closingScriptTag = \"</\" + \"script>\"; \n$('#custom-javascripts').append(\"<script src='\" + pathPrefix + \"documentation-resources/javascripts/main.js'>\" + closingScriptTag + \"<link rel='stylesheet' href='\" + pathPrefix + \"documentation-resources/stylesheets/main.css'>\");</script>""",
      "-encoding", "UTF-8", "-charset", "UTF-8", "-docencoding", "UTF-8")
  )

  lazy val javacSettings = Seq[Setting[_]](
    javacOptions ++= Seq("-deprecation", "-Xlint:unchecked", "-source", "1.8", "-target", "1.8", "-Xlint:all", "-Xlint:-options", "-Xlint:-path", "-Werror", "-parameters")
  )

  def testSettings(testLibs: ModuleID*): Seq[Setting[_]] = {
     Defaults.itSettings ++ jacoco.settings ++ itJacoco.settings ++ Seq(Test, jacoco.Config, IntegrationTest, itJacoco.Config).map { testScope: Configuration =>
       Seq[Setting[_]](
         parallelExecution in testScope := false,
         libraryDependencies ++= Seq(testLibs:_*)
       )
     }.flatten
  }

  object Libs {
    lazy val logbackClassic  = "ch.qos.logback" % "logback-classic" % "1.1.2" % "it"
    lazy val javaLoggerBridge  = "org.slf4j" % "jul-to-slf4j" % "1.7.7" % "it"//Java Money logs on java.util.logging
    lazy val junitDep = junitDepRaw % "test"
    lazy val junitDepRaw = "junit" % "junit-dep" % "4.11"
    lazy val junitInterface = "com.novocode" % "junit-interface" % "0.10"
    lazy val festAssert = "org.easytesting" % "fest-assert" % "1.4"
  }
}
