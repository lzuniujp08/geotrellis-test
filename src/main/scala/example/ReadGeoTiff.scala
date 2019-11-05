package example

import java.util.Date

import example.KernelDensity.extent
import geotrellis.proj4.LatLng
import geotrellis.raster.Tile
import geotrellis.raster.io.geotiff._
import geotrellis.raster.io.geotiff.reader.GeoTiffReader
import geotrellis.raster.render.{ColorMap, ColorRamp, ColorRamps}

object ReadGeoTiff {
  val path: String = "D:/data/gis_data/dem/beijing.tif"

  def main(args: Array[String]): Unit = {
    val start =new Date().getTime
    //readSingleband(byteReader: ByteReader, streaming: Boolean, withOverviews: Boolean)
    val geoTiff: SinglebandGeoTiff = GeoTiffReader.readSingleband(path)
    //  val geoTiff: MultibandGeoTiff = GeoTiffReader.readMultiband(path)
    val extent = geoTiff.extent
    val tile1: Tile = geoTiff.tile
    var tile2: Tile = geoTiff.tile
    var tile : Tile = tile1.localAdd(tile2)
    // tile1.map { z => z + 1 } // Map over integer values.
    // tile2.mapDouble { z => z + 1.1 } // Map over double values.
    // tile1.dualMap({ z => z + 1 })({ z => z + 1.1 }) // Call either the integer value or double version, depending on cellType.
    // tile1.combine(tile2) { (z1, z2) => z1 + z2 }
    val colorMap = ColorRamp(0xFF0000FF, 0x0000FFFF)
      .stops(10)
      .toColorMap(tile.histogram)
    val pathOut: String = "D:\\lzugis19\\code\\geotrellis-test\\result\\test.png"
    tile.renderPng(colorMap).write(pathOut)
    val pathBase: String = "D:/data/gis_data/dem/"
    GeoTiff(tile, extent, LatLng).write(pathBase + "beijing1.tif")
    val end =new Date().getTime
    println(end - start)
  }
}
