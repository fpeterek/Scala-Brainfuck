
class Parser {

  private var isComment = false
  private var builder = new ASTBuilder
  private var lineNum = 0
  private var charNum = 0

  private def endLoop(): Unit = try {
    builder.endLoop()
  } catch {
    case _: Throwable => throw new RuntimeException(f"Unmatched token ']', line: $lineNum, character: $charNum")
  }

  private def matchChar(char: Char): Unit = char match {
    case '#' => isComment = true
    case '+' => builder.inc()
    case '-' => builder.dec()
    case '>' => builder.incPtr()
    case '<' => builder.decPtr()
    case '.' => builder.write()
    case ',' => builder.read()
    case '[' => builder.loop()
    case ']' => endLoop()
    case _ => ()
  }

  private def parseChar(char: Char): Unit = {
    charNum += 1
    if (!isComment) matchChar(char)
  }

  private def reset(): Unit = {
    charNum = 0
    isComment = false
  }

  private def hardReset(): Unit = {
    builder = new ASTBuilder
    lineNum = 0
  }

  private def parseLine(line: String): Unit = {
    lineNum += 1
    reset()
    line.foreach(parseChar)
  }

  private def check(): Unit = if (!builder.allLoopsClosed) {
    throw new RuntimeException("Unclosed loop")
  }

  private def parse(fun: () => Unit): Body = {
    hardReset()
    fun()
    check()
    builder.get
  }

  def parse(lines: List[String]): Body = parse({() => lines.foreach(parseLine)})
  def parse(str: String): Body = parse({() => parseLine(str)})

}
