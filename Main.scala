object Main {

  private val bf = new Brainfuck

  def main(args: Array[String]): Unit =
    if (args.isEmpty) {
      bf.repl()
    } else {
      bf.interpretFile(args.head)
    }
}
