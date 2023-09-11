---
title: Installation
---

## Packages

- releases
- maven coordinates
- no snapshots
- where to find the sources

## SBT
## Mill
## Gradle
## Maven

## Moving from ScalaMock to Scala3Mock

- Syntax changes
- Stub feature dropped
- No cross compilation with Scala2
- Support for ScalaTest's `path.FunSpec` removed (help welcome)
- Support for inner types ([help welcome](https://github.com/fmonniot/scala3mock/issues/3))
- By-name parameters ([help welcome](https://github.com/fmonniot/scala3mock/issues/4)) currently have issues with type inference, but work fine when fully specifiying types when defining the expectations (eg. `when[String, Int](mock.function).expects("val").returns(1)`)
