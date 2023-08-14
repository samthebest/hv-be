import sbt._
import sbtassembly.AssemblyKeys.{assembly, assemblyMergeStrategy}
import sbtassembly._

object CustomMergeStrat {
  val mergeStrat = assembly / assemblyMergeStrategy := {
    // jar description
    case PathList("META-INF", "io.netty.versions.properties") =>
      MergeStrategy.discard
    // copyright notices
    case PathList("NOTICE")                 => MergeStrategy.concat
    case PathList("META-INF", "NOTICE")     => MergeStrategy.concat
    case PathList("META-INF", "NOTICE.txt") => MergeStrategy.concat
    case PathList("META-INF", "NOTICE.md")  => MergeStrategy.concat
    // licences
    case PathList("LICENSE")                 => MergeStrategy.concat
    case PathList("META-INF", "LICENSE")     => MergeStrategy.concat
    case PathList("META-INF", "LICENSE.txt") => MergeStrategy.concat
    case PathList("META-INF", "LICENSE.md")  => MergeStrategy.concat
    // we aren't OSGI
    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
    // weird netty meta stuff, seems unused.
    case PathList("META-INF", "INDEX.LIST") => MergeStrategy.discard
    // weird apache meta stuff, seems unused.
    case PathList("META-INF", "DEPENDENCIES") => MergeStrategy.discard

    // jar description produced by scala libs
    case PathList("rootdoc.txt") => MergeStrategy.discard
    // hocon files are fine to concatenate
    case PathList("reference.conf") => MergeStrategy.concat

    // this should probably either
    // 1. discard both and get a more up-to-date copy from mozilla.
    // 2. custom merge that respects the format.
    case PathList("mozilla", "public-suffix-list.txt") =>
      MergeStrategy.filterDistinctLines
    // as above
    case PathList("mime.types") => MergeStrategy.filterDistinctLines

    // The represent module linkage, there is no merge possible.
    case PathList("module-info.class")                            => MergeStrategy.discard
    case PathList("META-INF", "versions", _, "module-info.class") => MergeStrategy.discard
    // we aren't native
    case PathList(ps @ _*)
        if ps.last == "native-image.properties" || ps.last == "reflection-config.json" =>
      MergeStrategy.discard

    // This keeps our aws sdk version vs the shaded one from flink
    case PathList("VersionInfo.java") => MergeStrategy.last

    // Two implementations of the same interface can be loaded
    case PathList("META-INF", "services", _) => MergeStrategy.concat

    // take the flink connectors lib as newer
    case PathList("META-INF", "native", "libnetty_transport_native_epoll_x86_64.so") =>
      MergeStrategy.last

    // delete aws codegen json
    case PathList("codegen-resources", p @ _) if p.endsWith(".json") =>
      MergeStrategy.discard

    // delete aws codegen json
    case PathList("codegen-resources", "customization.config") =>
      MergeStrategy.discard

    // un-sign jars
    case PathList(p @ _*)
        if p.last.toLowerCase
          .endsWith(".dsa") || p.last.toLowerCase.endsWith(".sf") || p.last.toLowerCase
          .endsWith(".rsa") =>
      MergeStrategy.discard

    case PathList("arrow-git.properties")                                => MergeStrategy.discard
    case PathList("org", "apache", "log4j", _*)                          => MergeStrategy.last
    case PathList("org", "slf4j", _*)                                    => MergeStrategy.last
    case PathList("javax", "xml", "bind", _*)                            => MergeStrategy.last
    case PathList("javax", "ws", "rs", _*)                               => MergeStrategy.last
    case PathList("META-INF", "maven", "com.fasterxml.jackson.core", _*) => MergeStrategy.last
    case PathList("META-INF", "maven", "com.google.errorprone", _*)      => MergeStrategy.last
    case PathList("META-INF", "maven", "com.google.guava", _*)           => MergeStrategy.last
    case PathList("META-INF", "maven", "com.google.protobuf", _*)        => MergeStrategy.last
    case PathList("META-INF", "maven", "net.minidev", _*)                => MergeStrategy.last

    case x if x.contains("scala/annotation/nowarn.class")                  => MergeStrategy.first
    case x if x.contains("scala/annotation/nowarn$.class")                 => MergeStrategy.first
    case x if x.endsWith("netty-tcnative-boringssl-static/pom.properties") => MergeStrategy.first
    case x if x.endsWith("netty-tcnative-boringssl-static/pom.xml")        => MergeStrategy.first
    case x if x.contains("native-image/io.netty/codec-http/native-image.properties") =>
      MergeStrategy.discard
    case x if x.contains("libnetty_transport_native_epoll_aarch_64.so") => MergeStrategy.first
    case x if x.endsWith("com.fasterxml.jackson.core.JsonFactory")      => MergeStrategy.first
    case x if x.endsWith("reactor.blockhound.integration.BlockHoundIntegration") =>
      MergeStrategy.first
    case x if x.endsWith("VersionInfo.java")       => MergeStrategy.discard
    case x if x.endsWith("mime.types")             => MergeStrategy.first
    case x if x.endsWith("public-suffix-list.txt") => MergeStrategy.first
    case _                                         => MergeStrategy.deduplicate
  }
}
