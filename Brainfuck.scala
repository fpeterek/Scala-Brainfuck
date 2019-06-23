class Brainfuck {

  val runtime = new Runtime
  val parser = new Parser

  private def readLine() = scala.io.StdIn.readLine()
  private def readFile(filename: String) = Reader.read(filename)

  private def parse(str: String) = parser.parse(str)
  private def parse(lines: List[String]) = parser.parse(lines)

  private def run(body: Body): Unit = runtime.run(body)

  def repl(): Unit = while (true) {
    print("$")
    run(parse(readLine()))
  }

  def interpretFile(file: String): Unit = run(parse(readFile(file)))

}
