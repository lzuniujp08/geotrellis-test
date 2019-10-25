# 问题记录文档
## 1、版本问题
### 问题描述
> not found: https://repo1.maven.org/maven2/org/scala-lang/scala-reflect/2.12/scala-reflect-2.12.pom
### 解决方式
> 访问https://repo1.maven.org/maven2/org/，查找对应的版本。
```
scalaVersion := "2.12.10"
// 单独引用
libraryDependencies += "org.locationtech.geotrellis" %% "geotrellis-raster" % "3.0.0"
// 多个引用
libraryDependencies ++= Seq(
  "org.locationtech.geotrellis" %% "geotrellis-raster" % geotrellisVersion,
  "org.locationtech.geotrellis" %% "geotrellis-spark" % geotrellisVersion
)
```
