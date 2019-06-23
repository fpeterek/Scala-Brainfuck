import scala.io.Source

object Reader {

  def read(filename: String): List[String] = {
    val src = Source.fromFile(filename)
    val lines = src.getLines().toList
    src.close()
    lines
  }

}
