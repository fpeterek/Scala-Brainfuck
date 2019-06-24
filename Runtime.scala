class Runtime {

  private var i = 0
  private val tape = new Array[Int](Runtime.tapesize)

  private def underflow(): Unit = if (tape(i) < 0) tape(i) += 256
  private def overflow(): Unit = if (tape(i) > 255) tape(i) -= 256
  private def checkBounds(): Unit = underflow(); overflow()

  private def mutate(value: Int): Unit = {
    tape(i) += value
    checkBounds()
  }

  private def lowerIndexBounds(): Unit = if (i < 0) i = 0
  private def upperIndexBounds(): Unit = if (i >= Runtime.tapesize) i = Runtime.tapesize - 1
  private def checkIndexBounds(): Unit = lowerIndexBounds(); upperIndexBounds()

  private def mutatePtr(value: Int): Unit = {
    i += value
    checkIndexBounds()
  }

  private def handleStatement(s: Statement): Unit = s match {
    case x: MutateValue => mutate(x.value)
    case x: MutatePtr => mutatePtr(x.value)
    case _: Input => tape(i) = scala.io.StdIn.readByte()
    case _: Output => print(tape(i).toChar)
    case x: Loop => while (tape(i) != 0) run(x.body)
  }

  def run(body: Body): Unit = body.body.foreach(handleStatement)

}

object Runtime {
  val tapesize = 30000
}
