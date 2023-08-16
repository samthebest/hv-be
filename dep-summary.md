
## Intro

Using `DepTreeUtils.scala` this file shows only which libs bring in which netty libs (without duplication)

We say a commit works provided the following works

```
sbt "project hvDomain" assembly
java -cp libs/hv-domain/target/scala-2.13/hvDomain-assembly-0.1.0-SNAPSHOT.jar hypervolt.TestKinesisPut
```

and doesn't throw a weird netty exception (like `ClassNotFoundException`, or `ClosedChannelException`)

## Commit 08c7233bf2a3418e1334311969f8f54ad8cfb843

Works.

Versions:

```
val awssdk2 = "2.20.125"
val netty = "4.1.91.Final"
val twitter = "20.9.0"
```

Deps

```
scala> findNetty("deps-08c7233bf2a3418e1334311969f8f54ad8cfb843.txt")
hvdomain:hv-domain_2.13:0.1.0-SNAPSHOT [S]
  +-software.amazon.awssdk:kinesis:2.20.125
    +-software.amazon.awssdk:netty-nio-client:2.20.125
      +-io.netty:netty-transport-classes-epoll:4.1.94.Final
      +-io.netty:netty-codec-http2:4.1.94.Final
        +-io.netty:netty-codec-http:4.1.94.Final
          +-io.netty:netty-handler:4.1.94.Final
            +-io.netty:netty-transport-native-unix-common:4.1.94.Final
          +-io.netty:netty-codec:4.1.94.Final
            +-io.netty:netty-transport:4.1.94.Final
              +-io.netty:netty-resolver:4.1.94.Final
  +-io.netty:netty-buffer:4.1.94.Final
    +-io.netty:netty-common:4.1.94.Final
  +-io.netty:netty-buffer:4.1.91.Final (evicted by: 4.1.94.Final)
```

## Commit 2ae957d571ff842d790b656914ca7a8b4fc910f1

Fails with:

```
...
Caused by: java.nio.channels.ClosedChannelException
	at io.netty.channel.AbstractChannel$AbstractUnsafe.newClosedChannelException(AbstractChannel.java:957)
	at io.netty.channel.AbstractChannel$AbstractUnsafe.ensureOpen(AbstractChannel.java:976)
	at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe.connect(AbstractNioChannel.java:237)
	...
```

Versions:

```
val awssdk2 = "2.15.14"
val netty = "4.1.91.Final"
val twitter = "20.9.0"
```

Deps

```
scala> findNetty("deps-2ae957d571ff842d790b656914ca7a8b4fc910f1.txt")
hvdomain:hv-domain_2.13:0.1.0-SNAPSHOT [S]
  +-software.amazon.awssdk:kinesis:2.15.14
    +-software.amazon.awssdk:netty-nio-client:2.15.14
      +-io.netty:netty-transport-native-epoll:4.1.46.Final
        +-io.netty:netty-transport-native-unix-common:4.1.46.Final
      +-io.netty:netty-codec-http2:4.1.46.Final
      +-com.typesafe.netty:netty-reactive-streams-http:2.0.4
        +-io.netty:netty-codec-http:4.1.46.Final
        +-io.netty:netty-codec-http:4.1.43.Final (evicted by: 4.1.46.Final)
        +-com.typesafe.netty:netty-reactive-streams:2.0.4
          +-io.netty:netty-handler:4.1.46.Final
            +-io.netty:netty-codec:4.1.46.Final
              +-io.netty:netty-transport:4.1.46.Final
                +-io.netty:netty-resolver:4.1.46.Final
              +-io.netty:netty-common:4.1.46.Final (evicted by: 4.1.91.Final)
            +-io.netty:netty-buffer:4.1.46.Final (evicted by: 4.1.91.Final)
          +-io.netty:netty-handler:4.1.43.Final (evicted by: 4.1.46.Final)
  +-io.netty:netty-buffer:4.1.91.Final
    +-io.netty:netty-common:4.1.91.Final
```

## Commit 374bd27c2009ac5e5716ed7e927e985b6a712155

Works

Versions:

```
val awssdk2 = "2.15.14"
val netty = "4.1.46.Final"
val twitter = "20.9.0"
```

Deps

```
hvdomain:hv-domain_2.13:0.1.0-SNAPSHOT [S]
  +-software.amazon.awssdk:kinesis:2.15.14
    +-software.amazon.awssdk:netty-nio-client:2.15.14
      +-io.netty:netty-transport-native-epoll:4.1.46.Final
        +-io.netty:netty-transport-native-unix-common:4.1.46.Final
      +-io.netty:netty-codec-http2:4.1.46.Final
      +-com.typesafe.netty:netty-reactive-streams-http:2.0.4
        +-io.netty:netty-codec-http:4.1.46.Final
        +-io.netty:netty-codec-http:4.1.43.Final (evicted by: 4.1.46.Final)
        +-com.typesafe.netty:netty-reactive-streams:2.0.4
          +-io.netty:netty-handler:4.1.46.Final
            +-io.netty:netty-codec:4.1.46.Final
              +-io.netty:netty-transport:4.1.46.Final
                +-io.netty:netty-resolver:4.1.46.Final
          +-io.netty:netty-handler:4.1.43.Final (evicted by: 4.1.46.Final)
  +-io.netty:netty-buffer:4.1.46.Final
    +-io.netty:netty-common:4.1.46.Final
```

## Commit ???

Fails with

```
...
Caused by: java.lang.ClassNotFoundException: io.netty.internal.tcnative.CertificateCompressionAlgo
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:581)
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:178)
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:522)
	... 47 more
```

Versions

```
val awssdk2 = "2.20.125"
val netty = "4.1.91.Final"
val twitter = "20.9.0"
val finch = "0.33.0"
```

Seems to pull in `4.1.73.Final` and `io.netty:netty-tcnative-boringssl-static:2.0.46.Final`

Deps


```
scala> findNetty("deps-374bd27c2009ac5e5716ed7e927e985b6a712155.txt")
hvdomain:hv-domain_2.13:0.1.0-SNAPSHOT [S]
  +-com.github.finagle:finchx-circe_2.13:0.33.0 [S]
    +-com.github.finagle:finchx-core_2.13:0.33.0 [S]
      +-com.twitter:finagle-http_2.13:22.3.0 [S]
        +-com.twitter:finagle-http2_2.13:22.3.0 [S]
          +-io.netty:netty-tcnative-boringssl-static:2.0.46.Final
            +-io.netty:netty-tcnative-classes:2.0.46.Final
          +-io.netty:netty-codec-http2:4.1.94.Final
          +-io.netty:netty-codec-http2:4.1.73.Final (evicted by: 4.1.94.Final)
          +-com.twitter:finagle-netty4-http_2.13:22.3.0 [S]
            +-com.twitter:finagle-netty4_2.13:22.3.0 [S]
        +-com.twitter:finagle-base-http_2.13:22.3.0 [S]
          +-io.netty:netty-transport-native-epoll:4.1.73.Final
            +-io.netty:netty-transport-native-unix-common:4.1.73.Final (evicted by: 4.1.94.Final)
            +-io.netty:netty-transport-classes-epoll:4.1.94.Final
            +-io.netty:netty-transport-classes-epoll:4.1.73.Final (evicted by: 4.1.94.Final)
          +-io.netty:netty-handler:4.1.73.Final (evicted by: 4.1.94.Final)
          +-io.netty:netty-handler-proxy:4.1.73.Final
            +-io.netty:netty-codec-socks:4.1.73.Final
              +-io.netty:netty-transport:4.1.73.Final (evicted by: 4.1.94.Final)
              +-io.netty:netty-common:4.1.73.Final (evicted by: 4.1.94.Final)
              +-io.netty:netty-codec:4.1.73.Final (evicted by: 4.1.94.Final)
            +-io.netty:netty-buffer:4.1.73.Final (evicted by: 4.1.94.Final)
          +-io.netty:netty-codec-http:4.1.94.Final
            +-io.netty:netty-handler:4.1.94.Final
              +-io.netty:netty-transport-native-unix-common:4.1.94.Final
            +-io.netty:netty-codec:4.1.94.Final
              +-io.netty:netty-transport:4.1.94.Final
                +-io.netty:netty-resolver:4.1.94.Final
            +-io.netty:netty-buffer:4.1.94.Final
              +-io.netty:netty-common:4.1.94.Final
          +-io.netty:netty-codec-http:4.1.73.Final (evicted by: 4.1.94.Final)
  +-io.netty:netty-buffer:4.1.91.Final (evicted by: 4.1.94.Final)
  +-io.netty:netty-buffer:4.1.94.Final
    +-io.netty:netty-common:4.1.94.Final
  +-software.amazon.awssdk:kinesis:2.20.125
    +-software.amazon.awssdk:netty-nio-client:2.20.125
      +-io.netty:netty-transport-classes-epoll:4.1.94.Final
      +-io.netty:netty-codec-http2:4.1.94.Final
        +-io.netty:netty-codec-http:4.1.94.Final
          +-io.netty:netty-handler:4.1.94.Final
            +-io.netty:netty-transport-native-unix-common:4.1.94.Final
          +-io.netty:netty-codec:4.1.94.Final
            +-io.netty:netty-transport:4.1.94.Final
              +-io.netty:netty-resolver:4.1.94.Final
      +-io.netty:netty-buffer:4.1.94.Final
        +-io.netty:netty-common:4.1.94.Final


```


## Commit 20703bfcf7e6a379665c408d222a7706931d557a

Works

Versions

```
val awssdk2 = "2.16.104"
val netty = "4.1.73.Final"
val twitter = "20.9.0"
val finch = "0.33.0"
```

Seems to pull in `4.1.73.Final` and `io.netty:netty-tcnative-boringssl-static:2.0.46.Final`

Deps

```
scala> findNetty("deps-20703bfcf7e6a379665c408d222a7706931d557a.txt")
hvdomain:hv-domain_2.13:0.1.0-SNAPSHOT [S]
  +-com.github.finagle:finchx-circe_2.13:0.33.0 [S]
    +-com.github.finagle:finchx-core_2.13:0.33.0 [S]
      +-com.twitter:finagle-http_2.13:22.3.0 [S]
        +-com.twitter:finagle-http2_2.13:22.3.0 [S]
          +-io.netty:netty-tcnative-boringssl-static:2.0.46.Final
          +-io.netty:netty-codec-http2:4.1.73.Final
          +-com.twitter:finagle-netty4-http_2.13:22.3.0 [S]
            +-com.twitter:finagle-netty4_2.13:22.3.0 [S]
        +-com.twitter:finagle-base-http_2.13:22.3.0 [S]
          +-io.netty:netty-transport-native-epoll:4.1.73.Final
            +-io.netty:netty-transport-classes-epoll:4.1.73.Final
              +-io.netty:netty-transport-native-unix-common:4.1.73.Final
          +-io.netty:netty-handler-proxy:4.1.73.Final
            +-io.netty:netty-codec-socks:4.1.73.Final
          +-io.netty:netty-codec-http:4.1.73.Final
            +-io.netty:netty-handler:4.1.73.Final
              +-io.netty:netty-tcnative-classes:2.0.46.Final
            +-io.netty:netty-codec:4.1.73.Final
              +-io.netty:netty-transport:4.1.73.Final
                +-io.netty:netty-resolver:4.1.73.Final
            +-io.netty:netty-buffer:4.1.73.Final
              +-io.netty:netty-common:4.1.73.Final
  +-io.netty:netty-buffer:4.1.73.Final
    +-io.netty:netty-common:4.1.73.Final
  +-software.amazon.awssdk:kinesis:2.16.104
    +-software.amazon.awssdk:netty-nio-client:2.16.104
      +-io.netty:netty-transport-native-epoll:4.1.73.Final
        +-io.netty:netty-transport-classes-epoll:4.1.73.Final
      +-io.netty:netty-transport-native-epoll:4.1.63.Final (evicted by: 4.1.73.Final)
        +-io.netty:netty-transport-native-unix-common:4.1.73.Final
        +-io.netty:netty-transport-native-unix-common:4.1.63.Final (evicted by: 4.1.73.Final)
      +-io.netty:netty-codec-http2:4.1.73.Final
      +-io.netty:netty-codec-http2:4.1.63.Final (evicted by: 4.1.73.Final)
      +-com.typesafe.netty:netty-reactive-streams-http:2.0.5
        +-io.netty:netty-codec-http:4.1.73.Final
        +-io.netty:netty-codec-http:4.1.63.Final (evicted by: 4.1.73.Final)
        +-io.netty:netty-codec-http:4.1.52.Final (evicted by: 4.1.73.Final)
        +-com.typesafe.netty:netty-reactive-streams:2.0.5
          +-io.netty:netty-handler:4.1.73.Final
            +-io.netty:netty-tcnative-classes:2.0.46.Final
          +-io.netty:netty-handler:4.1.63.Final (evicted by: 4.1.73.Final)
            +-io.netty:netty-codec:4.1.73.Final
            +-io.netty:netty-codec:4.1.63.Final (evicted by: 4.1.73.Final)
              +-io.netty:netty-transport:4.1.73.Final
              +-io.netty:netty-transport:4.1.63.Final (evicted by: 4.1.73.Final)
                +-io.netty:netty-resolver:4.1.73.Final
                +-io.netty:netty-resolver:4.1.63.Final (evicted by: 4.1.73.Final)
              +-io.netty:netty-common:4.1.63.Final (evicted by: 4.1.73.Final)
            +-io.netty:netty-buffer:4.1.73.Final
              +-io.netty:netty-common:4.1.73.Final
            +-io.netty:netty-buffer:4.1.63.Final (evicted by: 4.1.73.Final)
          +-io.netty:netty-handler:4.1.52.Final (evicted by: 4.1.73.Final)
```

## Commit 9b0d2f9b22c02fe9ad0fda85f6a3ad97358b261f

TestChannel fails

Versions

```
val awssdk2 = "2.16.104"
val netty = "4.1.73.Final"
val twitter = "20.9.0"
val finch = "0.33.0"
// "com.twitter" %% "finagle-stats" % Versions.twitter,
```

Netty deps look OK, must have something to do with twitter


```
scala> hypervolt.DepTreeUtils.find("deps-9b0d2f9b22c02fe9ad0fda85f6a3ad97358b261f.txt", "util-core")
hvdomain:hv-domain_2.13:0.1.0-SNAPSHOT [S]
  +-com.github.finagle:finchx-circe_2.13:0.33.0 [S]
    +-com.github.finagle:finchx-core_2.13:0.33.0 [S]
      +-com.twitter:finagle-http_2.13:22.3.0 [S]
        +-com.twitter:finagle-http2_2.13:22.3.0 [S]
          +-com.twitter:finagle-netty4-http_2.13:22.3.0 [S]
            +-com.twitter:finagle-netty4_2.13:22.3.0 [S]
        +-com.twitter:finagle-base-http_2.13:22.3.0 [S]
          +-com.twitter:finagle-core_2.13:22.3.0 [S]
            +-com.twitter:util-tunable_2.13:22.3.0 [S]
            +-com.twitter:util-security_2.13:22.3.0 [S]
            +-com.twitter:util-routing_2.13:22.3.0 [S]
            +-com.twitter:util-jvm_2.13:22.3.0 [S]
            +-com.twitter:util-codec_2.13:22.3.0 [S]
            +-com.twitter:util-cache_2.13:22.3.0 [S]
            +-com.twitter:finagle-toggle_2.13:22.3.0 [S]
              +-com.twitter:util-logging_2.13:22.3.0 [S]
                +-com.twitter:util-stats_2.13:22.3.0 [S]
                  +-com.twitter:util-lint_2.13:22.3.0 [S]
              +-com.twitter:util-app_2.13:22.3.0 [S]
                +-com.twitter:util-registry_2.13:22.3.0 [S]
                +-com.twitter:util-app-lifecycle_2.13:22.3.0 [S]
                  +-com.twitter:util-core_2.13:22.3.0 [S]
  +-com.twitter:finagle-stats_2.13:20.9.0 [S]
    +-com.twitter:finagle-stats-core_2.13:20.9.0 [S]
      +-com.twitter:finagle-tunable_2.13:20.9.0 [S]
        +-com.twitter:util-core_2.13:20.9.0 [S] (evicted by: 22.3.0)
      +-com.twitter:finagle-http_2.13:22.3.0 [S]
        +-com.twitter:finagle-http2_2.13:22.3.0 [S]
          +-com.twitter:finagle-netty4-http_2.13:22.3.0 [S]
            +-com.twitter:finagle-netty4_2.13:22.3.0 [S]
        +-com.twitter:finagle-base-http_2.13:22.3.0 [S]
      +-com.twitter:finagle-core_2.13:22.3.0 [S]
        +-com.twitter:util-tunable_2.13:22.3.0 [S]
        +-com.twitter:util-security_2.13:22.3.0 [S]
        +-com.twitter:util-routing_2.13:22.3.0 [S]
        +-com.twitter:util-jvm_2.13:22.3.0 [S]
        +-com.twitter:util-codec_2.13:22.3.0 [S]
        +-com.twitter:util-cache_2.13:22.3.0 [S]
        +-com.twitter:finagle-toggle_2.13:22.3.0 [S]
          +-com.twitter:util-logging_2.13:22.3.0 [S]
            +-com.twitter:util-stats_2.13:22.3.0 [S]
              +-com.twitter:util-lint_2.13:22.3.0 [S]
          +-com.twitter:util-app_2.13:22.3.0 [S]
            +-com.twitter:util-registry_2.13:22.3.0 [S]
            +-com.twitter:util-app-lifecycle_2.13:22.3.0 [S]
              +-com.twitter:util-core_2.13:22.3.0 [S]
  +-com.twitter:util-core_2.13:20.9.0 [S] (evicted by: 22.3.0)
  +-com.twitter:util-core_2.13:22.3.0 [S]
```




