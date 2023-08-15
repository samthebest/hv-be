
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

## Commit 2ae957d571ff842d790b656914ca7a8b4fc910f1

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

Deps


