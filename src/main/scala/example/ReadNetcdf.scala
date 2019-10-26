package example

import example.KernelDensity.extent
import geotrellis.proj4.LatLng
import geotrellis.raster.histogram.StreamingHistogram
import geotrellis.raster.io.geotiff.GeoTiff
import geotrellis.raster.{FloatArrayTile, FloatUserDefinedNoDataArrayTile, FloatUserDefinedNoDataCellType, RasterExtent, Tile}
import geotrellis.raster.mapalgebra.focal.Kernel
import geotrellis.raster.render.{ColorMap, ColorRamp, ColorRamps, RGB}
import ucar.nc2._
import geotrellis.vector.{Extent, Feature, Point, PointFeature}
import org.apache.spark.unsafe.array.LongArray

import scala.collection.mutable.ListBuffer

object ReadNetcdf {
  val ncUri: String = "D:\\data\\zzrw_coefficient_gust.nc"

  def ncPointFeatures(
                       lonArray: Array[Float],
                       latArray: Array[Float],
                       valArray: Array[Float]
                     ): List[PointFeature[Float]] = {
    val list: ListBuffer[Feature[Point, Float]] = ListBuffer[Feature[Point, Float]]()
    for (i <- 0 to latArray.length - 1) {
      val lat = latArray(i)
      for (j <- 0 to lonArray.length - 1) {
        val lon = lonArray(j)
        val idx = i * lonArray.length + j;
        val v = valArray(idx)
        val f = Feature(Point(lon, lat), v)
        list += f
      }
    }
    list.toList
  }

  def main(args: Array[String]): Unit = {
    val ncfile = NetcdfFile.open(ncUri)
    val vs = ncfile.getVariables()
    val ucarType = vs.get(0).getDataType().getClassType
    val valType = vs.get(2).getDataType().getClassType
    val latArray = vs.get(0).read().get1DJavaArray(ucarType).asInstanceOf[Array[Float]]
    val lonArray = vs.get(1).read().get1DJavaArray(ucarType).asInstanceOf[Array[Float]]
    val extent = Extent(lonArray.min, latArray.min, lonArray.max, latArray.max)
    val pathBase: String = "D:\\lzugis19\\code\\geotrellis-test\\result\\"
    val nodata = Float.NaN
    val tile = {
      val valArray = vs.get(2).read().get1DJavaArray(valType).asInstanceOf[Array[Float]]
      FloatUserDefinedNoDataArrayTile(valArray, lonArray.length, latArray.length, FloatUserDefinedNoDataCellType(nodata)).rotate180.flipVertical
    }
//    val histogram = StreamingHistogram.fromTile(tile)
//    val breaks = histogram.quantileBreaks(3)
//    val colorMap = ColorRamps.BlueToRed.toColorMap(breaks);
//    val colorMap =
//        ColorRamp(RGB(0,0,255), RGB(255,0,0))
//          .stops(20)
//          .setAlphaGradient(0xFF, 0xAA)
    val colorMap = ColorRamps.HeatmapYellowToRed
    tile.renderPng(colorMap).write(pathBase + "test.png")
    GeoTiff(tile, extent, LatLng).write(pathBase + "test.tif")
    ncfile.close()
  }
}
