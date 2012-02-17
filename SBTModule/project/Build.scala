import sbt._
import sbt.Keys._
import sbt.Package.ManifestAttributes

object SBTModuleBuild extends Build { 
  lazy val project = Project("SBTModule", file(".")) settings(Seq(
      packageOptions := Seq(ManifestAttributes(
          ("OpenIDE-Module", "com.belfrygames.sbttest"),
          ("OpenIDE-Module-Localizing-Bundle", "com/belfrygames/sbttest/Bundle.properties"),
          ("OpenIDE-Module-Specification-Version", "1.0"))),
      resourceDirectory in Compile <<= baseDirectory(_ / "src/main/java"),
      
      mappings in (Compile,packageBin) ~= { (ms: Seq[(File, String)]) =>
        ms filter { case (file, toPath) =>
            file.getName.endsWith(".properties") || file.getName.endsWith(".class")
        }
      }
    ): _*)
}
