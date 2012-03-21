import sbt._
import sbt.Keys._
import sbt.Package.ManifestAttributes

object SBTModuleBuild extends Build { 
  val sharedSettings = Seq[Setting[_]](
    resolvers += "cloudbees snapshots" at "https://repository-belfry.forge.cloudbees.com/snapshot",
    credentials += {
      val credsFile = (Path.userHome / ".ivy2" / ".credentials")
      (if (credsFile.exists) Credentials(credsFile)
       else Credentials(file("/private/belfry/.credentials/.credentials")))
    }
  )
    
  lazy val project = Project("SBTModule", file(".")) settings(sharedSettings ++ Seq(
      packageOptions := Seq(ManifestAttributes(
          ("OpenIDE-Module", "com.belfrygames.sbttest"),
          ("OpenIDE-Module-Localizing-Bundle", "com/belfrygames/sbttest/Bundle.properties"),
          ("OpenIDE-Module-Specification-Version", "1.0"),
          ("OpenIDE-Module-Public-Packages", "com.belfrygames.sbttest"))),
      resourceDirectory in Compile <<= baseDirectory(_ / "src/main/java"),
      
      mappings in (Compile,packageBin) ~= { (ms: Seq[(File, String)]) =>
        ms filter { case (file, toPath) =>
            file.getName.endsWith(".properties") || file.getName.endsWith(".class")
        }
      },
      
      libraryDependencies += "starkengine" %% "starkengine" % "0.1-SNAPSHOT"
    ): _*)
}
