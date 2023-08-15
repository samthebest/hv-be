
## Intro

Using `DepTreeUtils.scala` this file shows only which libs bring in which netty libs (without duplication)

We say a commit works provided `TestKinesisPut.main` works and 
doesn't throw a weird netty exception (like `ClassNotFoundException`, or `ClosedChannelException`)

## Commit 08c7233bf2a3418e1334311969f8f54ad8cfb843

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

## Commit ???

Versions:

```
val awssdk2 = "2.15.14"
val netty = "4.1.91.Final"
val twitter = "20.9.0"
```
