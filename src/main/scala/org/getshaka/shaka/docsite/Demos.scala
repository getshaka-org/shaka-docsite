package org.getshaka.shaka.docsite

import org.getshaka.nativeconverter.NativeConverter
import org.getshaka.shaka
import org.getshaka.shaka.{Component, ComponentBuilder, Element, State, WebComponent, LocalStorage, OpenState}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import scala.collection.Seq
import scala.collection.mutable.Buffer


class HelloMessage(user: String) extends Component:
  override def template: ComponentBuilder =
    import shaka.builders.*

    div{color("purple"); p{t"Hello $user"}}

class Timer extends WebComponent:
  val seconds = shaka.useState(0)
  var interval: js.Dynamic|Null = null

  override def connectedCallback(): Unit =
    interval = js.Dynamic.global
      .setInterval(() => seconds.value += 1, 1000)

  override def disconnectedCallback(): Unit =
    js.Dynamic.global.clearInterval(interval)

  override val template: ComponentBuilder =
    import shaka.builders.*
    seconds.bind(value => t"Seconds: $value")

case class Item(date: js.Date, text: String)

class TodoApp extends Component:
  val items = shaka.useState(Buffer.empty[Item])
  val text = shaka.useState("")

  override val template: ComponentBuilder =
    import shaka.builders.*
    div{
      h3{color("royalblue"); t"TODO"}
      items.bind(TodoList(_).render)
      form{onsubmit(handleSubmit)
        label{`for`("new-todo")
          t"What needs to be done?"
        }
        br{}
        input{
          id("new-todo")
          width("100px")
          autocomplete("off")
          onchange(handleChange)
          text.bindProps(txt => value(txt))
        }
        button{items.bind(i =>
          t"Add #${i.size + 1}")}
      }
    }

  def handleSubmit(e: js.Dynamic): Unit =
    e.preventDefault()
    if text.value.isEmpty then return
    val newItem = Item(new js.Date, text.value)
    items.value = items.value.addOne(newItem)
    text.value = ""

  def handleChange(e: js.Dynamic): Unit =
    text.value = e.target.value.asInstanceOf[String]


class TodoList(items: Seq[Item]) extends Component:
  override val template: ComponentBuilder =
    import shaka.builders.*
    ul{
      for item <- items do
        li{item.text.t}
    }

import org.getshaka.shaka.useState

@js.native
@JSGlobal("remarkable.Remarkable")
class Remarkable extends js.Object:
  def render(markdown: String): String = js.native

class MarkdownEditor extends Component:
  private val remarkable = Remarkable()
  private val initialMd = "Hello, **world**!"
  private val mdHtml = useState(
    remarkable.render(initialMd))

  override val template: ComponentBuilder =
    import shaka.builders.*

    div{
      h3{t"Input"}
      label{`for`("markdown-content")
        t"Enter some markdown"
      }
      br{}
      textarea{
        id("markdown-content")
        oninput(updateMarkdown)
        initialMd.t
      }
      h3{t"Output"}
      mdHtml.bind(html =>
        div{maxWidth("100px")
          dangerouslySetInnerHtml(html)
        }
      )
    }

  def updateMarkdown(e: js.Dynamic): Unit =
    val input = e.target.value.asInstanceOf[String]
    mdHtml.value = remarkable.render(input)

class ShoppingList(name: String) extends Component:

  override def template: ComponentBuilder =
    import shaka.builders.{name as _, *}
    div{className("shopping-list")
      h1{t"Shopping list for $name"}
      ul{
        li{t"Scala 3 Books"}
        li{t"Scala.js Tutorials"}
        li{t"Cooking guides!"}
      }
    }

def shoppingList(name: String): ComponentBuilder =
  import shaka.builders.{name as _, *}
  div{className("shopping-list")
    h1{t"Shopping list for $name"}
    ul{
      li{t"Scala 3 Books"}
      li{t"Scala.js Tutorials"}
      li{t"Cooking guides!"}
    }
  }

import shaka.Binding

def explicitHello(name: String): ComponentBuilder =
  (parentElement: Element, parentBinding: Binding[?]) ?=>
    import shaka.builders.{name as _, *}
    
    h1{h1Element ?=>
      "hello world".t(using h1Element)
    }(using parentElement)

val TicTacStyles =
  """
    |
    |ol, ul {
    |  padding-left: 30px;
    |}
    |
    |.board-row:after {
    |  clear: both;
    |  content: "";
    |  display: table;
    |}
    |
    |.status {
    |  margin-bottom: 10px;
    |}
    |
    |.square {
    |  background: #fff;
    |  border: 1px solid #999;
    |  float: left;
    |  font-size: 24px;
    |  font-weight: bold;
    |  line-height: 34px;
    |  height: 34px;
    |  margin-right: -1px;
    |  margin-top: -1px;
    |  padding: 0;
    |  text-align: center;
    |  width: 34px;
    |}
    |
    |.square:focus {
    |  outline: none;
    |}
    |
    |.kbd-navigation .square:focus {
    |  background: #ddd;
    |}
    |
    |.game {
    |  display: flex;
    |  flex-direction: row;
    |}
    |
    |.game-info {
    |  margin-left: 20px;
    |}
    |""".stripMargin


class TicTac1 extends WebComponent:
  override def scopedStyle: String = TicTacStyles

  override val template: ComponentBuilder =
    import shaka.builders.*
    div{className("game")
      div{className("game-board")
        Board().render
      }
      div{className("game-info")
        div{/* status */}
        ol{/* todo */}
      }
    }

  class Square extends Component:
    override val template: ComponentBuilder =
      import shaka.builders.*
      button{className("square") /* todo */}

  class Board extends Component:
    override val template: ComponentBuilder =
      import shaka.builders.*

      def renderSquare(i: Int): ComponentBuilder =
        Square().render

      val status = "Next player: X"

      div{
        div{className("status"); status.t}
        div{className("board-row")
          renderSquare(0)
          renderSquare(1)
          renderSquare(2)
        }
        div{className("board-row")
          renderSquare(3)
          renderSquare(4)
          renderSquare(5)
        }
        div{className("board-row")
          renderSquare(6)
          renderSquare(7)
          renderSquare(8)
        }
      }
    end template
end TicTac1

class TicTac2 extends WebComponent:
  override def scopedStyle: String = TicTacStyles

  override val template: ComponentBuilder =
    import shaka.builders.*
    div{className("game")
      div{className("game-board")
        Board().render
      }
      div{className("game-info")
        div{/* status */}
        ol{/* todo */}
      }
    }

  class Square(position: Int) extends Component:
    override val template: ComponentBuilder =
      import shaka.builders.{position as _, *}
      button{className("square"); position.toString.t}

  class Board extends Component:
    override val template: ComponentBuilder =
      import shaka.builders.*

      def renderSquare(i: Int): ComponentBuilder =
        Square(position = i).render

      val status = "Next player: X"

      div{
        div{className("status"); status.t}
        for i <- 0 until 9 by 3 do
          div{className("board-row")
            renderSquare(i)
            renderSquare(i + 1)
            renderSquare(i + 2)
          }
      }
    end template
end TicTac2


class TicTac3 extends WebComponent:
  override def scopedStyle: String = TicTacStyles

  override val template: ComponentBuilder =
    import shaka.builders.*
    div{className("game")
      div{className("game-board")
        Board().render
      }
      div{className("game-info")
        div{/* status */}
        ol{/* todo */}
      }
    }

  class Square extends Component:
    override val template: ComponentBuilder =
      import shaka.builders.*
      button{className("square"); onclick(() => js.Dynamic.global.alert("clicked"))}

  class Board extends Component:
    override val template: ComponentBuilder =
      import shaka.builders.*

      def renderSquare(i: Int): ComponentBuilder =
        Square().render

      val status = "Next player: X"

      div{
        div{className("status"); status.t}
        for i <- 0 until 9 by 3 do
          div{className("board-row")
            renderSquare(i)
            renderSquare(i + 1)
            renderSquare(i + 2)
          }
      }
end TicTac3

class TicTac4 extends WebComponent:
  override def scopedStyle: String = TicTacStyles

  override val template: ComponentBuilder =
    import shaka.builders.*
    div{className("game")
      div{className("game-board")
        Board().render
      }
      div{className("game-info")
        div{/* status */}
        ol{/* todo */}
      }
    }

  class Square extends Component:
    private val wasClicked: OpenState[Boolean] = shaka.useState(false)

    override val template: ComponentBuilder =
      import shaka.builders.*
      button{className("square"); onclick(() => wasClicked.value = true)
        wasClicked.bind(clicked =>
          if clicked then t"X" else t""
        )
      }

  class Board extends Component:
    override val template: ComponentBuilder =
      import shaka.builders.*

      def renderSquare(i: Int): ComponentBuilder =
        Square().render

      val status = "Next player: X"

      div{
        div{className("status"); status.t}
        for i <- 0 until 9 by 3 do
          div{className("board-row")
            renderSquare(i)
            renderSquare(i + 1)
            renderSquare(i + 2)
          }
      }
end TicTac4

class TicTac5 extends WebComponent:
  import TicTac5.*

  override def scopedStyle: String = TicTacStyles

  override val template: ComponentBuilder =
    import shaka.builders.*
    div{className("game")
      div{className("game-board")
        Board().render
      }
      div{className("game-info")
        div{/* status */}
        ol{/* todo */}
      }
    }

  class Square(position: Int) extends Component:
    override val template: ComponentBuilder =
      import shaka.builders.{position as _, *}

      button{className("square"); onclick(() => GameState.setSquare(position))
        GameState.bind(_.squareValues(position) match
          case xOrY: String => xOrY.t
          case null => "".t
        )
      }

  class Board extends Component:
    override val template: ComponentBuilder =
      import shaka.builders.*

      def renderSquare(i: Int): ComponentBuilder =
        Square(position = i).render

      val nextPlayer: ComponentBuilder =
        GameState.bind(s => if s.xIsNext then t"X" else t"O")

      div{
        div{className("status")
          t"Next player: $nextPlayer"
        }
        for i <- 0 until 9 by 3 do
          div{className("board-row")
            renderSquare(i)
            renderSquare(i + 1)
            renderSquare(i + 2)
          }
      }
end TicTac5
object TicTac5:
  type SquareValue = "X"|"O"|Null

  case class Game(xIsNext: Boolean, squareValues: Seq[SquareValue])

  object GameState extends State(Game(true, Seq.fill(9)(null))):

    def setSquare(position: Int): Unit =
      if value.squareValues(position) != null then return

      val squareValues: Array[SquareValue] = value.squareValues.toArray
      squareValues(position) = if value.xIsNext then "X" else "O"
      value = Game(!value.xIsNext, squareValues)


class TicTac6 extends WebComponent:
  import TicTac6.*

  override def scopedStyle: String = TicTacStyles

  override val template: ComponentBuilder =
    import shaka.builders.*
    div{className("game")
      div{className("game-board")
        Board().render
      }
      div{className("game-info")
        div{/* status */}
        ol{/* todo */}
      }
    }

  class Square(position: Int) extends Component:
    override val template: ComponentBuilder =
      import shaka.builders.{position as _, *}

      button{className("square"); onclick(() => GameState.setSquare(position))
        GameState.bind(_.squareValues(position) match
          case xOrY: String => xOrY.t
          case null => "".t
        )
      }

  class Board extends Component:
    override val template: ComponentBuilder =
      import shaka.builders.*

      def renderSquare(i: Int): ComponentBuilder =
        Square(position = i).render

      val status: ComponentBuilder =
        GameState.bind(s => calculateWinner(s.squareValues) match
          case xOrY: String => t"Winner: $xOrY"
          case null => t"NextPlayer: ${if s.xIsNext then "X" else "O"}"
        )

      div{
        div{className("status")
          status
        }
        for i <- 0 until 9 by 3 do
          div{className("board-row")
            renderSquare(i)
            renderSquare(i + 1)
            renderSquare(i + 2)
          }
      }
end TicTac6
private object TicTac6:
  type SquareValue = "X"|"O"|Null

  case class Game(xIsNext: Boolean, squareValues: Seq[SquareValue])

  object GameState extends State(Game(true, Seq.fill(9)(null))):

    def setSquare(position: Int): Unit =
      if value.squareValues(position) != null
        || calculateWinner(value.squareValues) != null then return

      val squareValues: Array[SquareValue] = value.squareValues.toArray
      squareValues(position) = if value.xIsNext then "X" else "O"
      value = Game(!value.xIsNext, squareValues)
  end GameState

  def calculateWinner(squares: Seq[SquareValue]): SquareValue =
    val lines = Array(
      Array(0, 1, 2),
      Array(3, 4, 5),
      Array(6, 7, 8),
      Array(0, 3, 6),
      Array(1, 4, 7),
      Array(2, 5, 8),
      Array(0, 4, 8),
      Array(2, 4, 6)
    )
    for Array(a, b, c) <- lines do
      if squares(a) != null && squares(a) == squares(b) && squares(a) == squares(c) then
        return squares(a)
    return null

class TicTac7 extends WebComponent:
  import TicTac7.*

  override def scopedStyle: String = TicTacStyles

  override val template: ComponentBuilder =
    import shaka.builders.*

    val status: ComponentBuilder =
      GameState.bind(s => calculateWinner(s.history(s.stepNumber)) match
        case xOrY: String => t"Winner: $xOrY"
        case null => t"NextPlayer: ${if s.xIsNext then "X" else "O"}"
      )

    val moves: ComponentBuilder =
      GameState.bind(_.history.indices.foreach(move =>
        val desc =
          if move > 0 then "Go to move #" + move
          else "Go to game start"
        li{button{onclick(() => GameState.jumpTo(move)); desc.t}}
      ))

    div{className("game")
      div{className("game-board")
        Board().render
      }
      div{className("game-info")
        div{status}
        ol{moves}
      }
    }

  class Square(position: Int) extends Component:
    override val template: ComponentBuilder =
      import shaka.builders.{position as _, *}

      button{className("square"); onclick(() => GameState.setSquare(position))
        GameState.bind(s => s.history(s.stepNumber)(position) match
          case xOrY: String => xOrY.t
          case null => t""
        )
      }

  class Board extends Component:
    override val template: ComponentBuilder =
      import shaka.builders.*

      def renderSquare(i: Int): ComponentBuilder =
        Square(position = i).render

      div{
        for i <- 0 until 9 by 3 do
          div{className("board-row")
            renderSquare(i)
            renderSquare(i + 1)
            renderSquare(i + 2)
          }
      }
end TicTac7
object TicTac7:
  type SquareValue = "X"|"O"|Null
  type SquareValues = Seq[SquareValue]

  case class Game(xIsNext: Boolean, stepNumber: Int, history: Seq[SquareValues])

  object GameState extends State(Game(true, 0, Seq(Seq.fill(9)(null)))):

    def setSquare(position: Int): Unit =
      val hist = value.history.slice(0, value.stepNumber + 1)
      val squares: Array[SquareValue] = hist.last.toArray
      if squares(position) != null || calculateWinner(squares) != null
        then return

      squares(position) = if value.xIsNext then "X" else "O"
      value = value.copy(
        xIsNext = !value.xIsNext,
        stepNumber = hist.length,
        history = hist :+ squares
      )

    def jumpTo(step: Int): Unit =
      value = value.copy(
        stepNumber = step,
        xIsNext = (step % 2) == 0
      )

  end GameState

  def calculateWinner(squares: Seq[SquareValue]): SquareValue =
    val lines = Array(
      Array(0, 1, 2),
      Array(3, 4, 5),
      Array(6, 7, 8),
      Array(0, 3, 6),
      Array(1, 4, 7),
      Array(2, 5, 8),
      Array(0, 4, 8),
      Array(2, 4, 6)
    )
    for Array(a, b, c) <- lines do
      if squares(a) != null && squares(a) == squares(b) && squares(a) == squares(c) then
        return squares(a)
    return null


class TicTac8 extends WebComponent:
  import TicTac8.*

  override def scopedStyle: String = TicTacStyles

  override val template: ComponentBuilder =
    import shaka.builders.*

    val status: ComponentBuilder =
      GameState.bind(s => calculateWinner(s.history(s.stepNumber)) match
        case xOrY: String => t"Winner: $xOrY"
        case null => t"NextPlayer: ${if s.xIsNext then "X" else "O"}"
      )

    val moves: ComponentBuilder =
      GameState.bind(_.history.indices.foreach(move =>
        val desc =
          if move > 0 then "Go to move #" + move
          else "Go to game start"
        li{button{onclick(() => GameState.jumpTo(move)); desc.t}}
      ))

    div{className("game")
      div{className("game-board")
        Board().render
      }
      div{className("game-info")
        div{status}
        ol{moves}
      }
    }

  class Square(position: Int) extends Component:
    override val template: ComponentBuilder =
      import shaka.builders.{position as _, *}

      button{className("square"); onclick(() => GameState.setSquare(position))
        GameState.bind(s => s.history(s.stepNumber)(position) match
          case xOrY: String => xOrY.t
          case null => t""
        )
      }

  class Board extends Component:
    override val template: ComponentBuilder =
      import shaka.builders.*

      def renderSquare(i: Int): ComponentBuilder =
        Square(position = i).render

      div{
        for i <- 0 until 9 by 3 do
          div{className("board-row")
            renderSquare(i)
            renderSquare(i + 1)
            renderSquare(i + 2)
          }
      }
end TicTac8
object TicTac8:
  type SquareValue = "X"|"O"|Null
  type SquareValues = Seq[SquareValue]
  
  case class Game(xIsNext: Boolean, stepNumber: Int, history: Seq[SquareValues]) derives NativeConverter
  
  object GameState extends State(Game(true, 0, Seq(Seq.fill(9)(null))), LocalStorage("game-state")):

    def setSquare(position: Int): Unit =
      val hist = value.history.slice(0, value.stepNumber + 1)
      val squares: Array[SquareValue] = hist.last.toArray
      if squares(position) != null || calculateWinner(squares) != null
        then return

      squares(position) = if value.xIsNext then "X" else "O"
      value = value.copy(
        xIsNext = !value.xIsNext,
        stepNumber = hist.length,
        history = hist :+ squares
      )

    def jumpTo(step: Int): Unit =
      value = value.copy(
        stepNumber = step,
        xIsNext = (step % 2) == 0
      )

  end GameState

  def calculateWinner(squares: Seq[SquareValue]): SquareValue =
    val lines = Array(
      Array(0, 1, 2),
      Array(3, 4, 5),
      Array(6, 7, 8),
      Array(0, 3, 6),
      Array(1, 4, 7),
      Array(2, 5, 8),
      Array(0, 4, 8),
      Array(2, 4, 6)
    )
    for Array(a, b, c) <- lines do
      if squares(a) != null && squares(a) == squares(b)
        && squares(a) == squares(c) then
        return squares(a)
    return null


class ClickHole extends Component:
  private val numClicks = useState(0)
  
  override val template: ComponentBuilder =
    import shaka.builders.*

    button{onclick(() => numClicks.value += 1)
      t"click me"
    }
    p{t"numClicks: ${numClicks.bind(_.toString.t)}"}

class CustomTag extends Component:
  
  override def template: ComponentBuilder =
    import shaka.builders.{ElementBuilder, tag, t}
    inline def p(init: ElementBuilder)(using Element): Unit = tag("p")(init)
    p{t"my custom tag"}
    

class CustomCssProp extends Component:
  override def template: ComponentBuilder =
    import shaka.builders.{p, t, cssProp}
    inline def border(style: String)(using Element): Unit = cssProp("border")(style)
    p{border("solid")
      t"Has a solid border"
    }

class CustomJsProp extends Component:
  override def template: ComponentBuilder =
    import shaka.builders.{p, t, prop}
    inline def onclick(fn: () => Unit)(using Element): Unit = prop("onclick")(fn)
    p{onclick(() => js.Dynamic.global.alert("clicked!"))
      t"click me"
    }