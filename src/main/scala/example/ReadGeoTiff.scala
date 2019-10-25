package example

import java.util.Date

import geotrellis.raster.Tile
import geotrellis.raster.io.geotiff._
import geotrellis.raster.io.geotiff.reader.GeoTiffReader
import geotrellis.raster.render.{ColorMap, ColorRamps}

object ReadGeoTiff {
  val path: String = "D:/data/gis_data/dem/beijing.tif"

  def main(args: Array[String]): Unit = {
    val start =new Date().getTime
    //readSingleband(byteReader: ByteReader, streaming: Boolean, withOverviews: Boolean)
    val geoTiff: SinglebandGeoTiff = GeoTiffReader.readSingleband(path)
    //  val geoTiff: MultibandGeoTiff = GeoTiffReader.readMultiband(path)
    val extent = geoTiff.extent
    val tile: Tile = geoTiff.tile
    val colorMap = ColorMap(
      (0 to tile.findMinMax._2 by 4).toArray,
      ColorRamps.HeatmapBlueToYellowToRedSpectrum
    )
    tile.renderPng(colorMap).write("result/test.png")
    val end =new Date().getTime
    println(end - start)
  }
}
