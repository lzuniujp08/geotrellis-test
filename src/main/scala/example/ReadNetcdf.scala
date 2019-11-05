package example

import geotrellis.proj4.LatLng
import geotrellis.raster.io.geotiff.GeoTiff
import geotrellis.raster._
import geotrellis.raster.render.{ColorMap, ColorRamp, GreaterThanOrEqualTo, RGB}
import ucar.nc2._
import geotrellis.vector.Extent

object ReadNetcdf {
  val ncUri: String = "D:\\data\\zzrw_coefficient_gust.nc"

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
val colorMap =
  ColorMap(
    Map(
      1.507 -> RGB(43,131,186),
      1.599 -> RGB(171,211,164),
      1.692 -> RGB(255,255,191),
      1.784 -> RGB(253,174,97),
      1.876 -> RGB(215,25,28)
    ),
    ColorMap.Options(
      classBoundaryType = GreaterThanOrEqualTo,
      noDataColor = 0xFFFFFFFF,
      fallbackColor = 0xFFFFFFFF
    )
  )
    tile.renderPng(colorMap).write(pathBase + "test.png")
    GeoTiff(tile, extent, LatLng).write(pathBase + "test.tif")
    ncfile.close()
  }
}
