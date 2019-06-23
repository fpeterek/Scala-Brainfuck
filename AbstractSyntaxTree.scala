import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class ASTBuilder {

  private val mainBody: Body = new Body(ListBuffer())

  private val stack = mutable.Stack(mainBody)
  private var currentScope: Body = mainBody

  def inc(): Unit = mut(new MutateValue(1))
  def dec(): Unit = mut(new MutateValue(-1))
  def incPtr(): Unit = mut(new MutatePtr(1))
  def decPtr(): Unit = mut(new MutatePtr(-1))

  def mut(a: Arithmetic): Unit =
    if (currentScope.body.isEmpty || !currentScope.body.last.isInstanceOf[a.type]) {
      add(a)
    } else {
      currentScope.body.last.asInstanceOf[a.type].value += a.value
    }

  def read(): Unit = add(new Input)
  def write(): Unit = add(new Output)
  def loop(): Unit = {
    add(new Loop)
    enterScope()
  }
  def endLoop(): Unit = leaveScope()
  def allLoopsClosed: Boolean = stack.size == 1
  def get: Body = mainBody


  private def enterScope(): Unit = {
    currentScope = currentScope.body.last.asInstanceOf[Loop].body
    stack.push(currentScope)
  }

  private def leaveScope(): Unit = {
    stack.pop()
    currentScope = stack.top
  }

  private def add(statement: Statement): Unit = currentScope.body += statement

}

class Statement
class Body(var body: ListBuffer[Statement]) extends Statement
class Loop(val body: Body = new Body(ListBuffer())) extends Statement
class Arithmetic(var value: Int) extends Statement
class MutateValue(value: Int) extends Arithmetic(value)
class MutatePtr(value: Int) extends Arithmetic(value)
class Input extends Statement
class Output extends Statement
