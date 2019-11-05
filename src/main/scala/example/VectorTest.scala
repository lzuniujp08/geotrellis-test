package example

import geotrellis.vector._
import geotrellis.vector.io.json.JsonFeatureCollectionMap

import scala.io.Source

object VectorTest {

  def main(args: Array[String]): Unit = {
    val pt = Point(100, 40)
    var data = Map(("height", 200),("lon", 200),("lat", 39))
    val ptFeature = PointFeature(pt, data)
    val json = ptFeature.toGeoJson
    println(json)

    val ptBuffer = pt.buffer(1)
    val ptbFeature = Feature(ptBuffer, data)
    val jsonB = ptbFeature.toGeoJson
    println(jsonB)

    println(ptBuffer.intersects(Point(100, 39.5)))
//    val path: String = "D:\\lzugis19\\code\\geotrellis-test\\result\\capital.json";
//    val jsonStr = Source.fromFile(path).mkString
//    val collection = jsonStr.parseGeoJson[JsonFeatureCollectionMap]
//    println(collection)
  }
}
