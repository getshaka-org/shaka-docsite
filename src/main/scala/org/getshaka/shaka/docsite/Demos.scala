package org.getshaka.shaka.docsite

import org.getshaka.nativeconverter.NativeConverter
import org.getshaka.shaka.*
import org.scalajs.dom.*

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import scala.collection.Seq
import scala.collection.mutable.Buffer

class HelloMessage(user: String) extends Component:
  override val template = Frag {
    import builders.*

    div{color("purple"); p{t"Hello $user"}}
  }

import scala.scalajs.js.timers.*
import scala.concurrent.duration.*

class Timer extends WebComponent:
  private val seconds = useState(0)
  private var interval: SetIntervalHandle = null

  override def connectedCallback(): Unit =
    interval = setInterval(1.second)(seconds.setValue(_ + 1))

  override def disconnectedCallback(): Unit =
    clearInterval(interval)

  override val shadowDom = ShadowDom.WithStyle(
    " div { color: green; } "
  )

  override val template = Frag {
    import builders.*
    seconds.bind(value => t"Seconds: $value")
  }

case class Item(date: js.Date, text: String)

class TodoApp extends Component:
  private val items = useState(IArray.empty[Item])
  private val text = useState("")

  override val template = Frag {
    import builders.*
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
  }

  private def handleSubmit(e: Event): Unit =
    e.preventDefault()
    if text.value.isEmpty then return
    items.setValue(_ :+ Item(new js.Date, text.value))
    text.setValue("")

  private def handleChange(e: Event): Unit =
    text.setValue(e.target.asInstanceOf[HTMLInputElement].value)


class TodoList(items: Seq[Item]) extends Component:
  override val template = Frag {
    import builders.*
    ul{
      for item <- items do
        li{item.text.t}
    }
  }

@js.native
@JSGlobal("remarkable.Remarkable")
class Remarkable extends js.Object:
  def render(markdown: String): String = js.native

class MarkdownEditor extends Component:
  private val remarkable = Remarkable()
  private val initialMd = "Hello, **world**!"
  private val mdHtml = useState(
    remarkable.render(initialMd))

  override val template = Frag {
    import builders.*

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
  }

  private def updateMarkdown(e: Event): Unit =
    val input = e.target.asInstanceOf[HTMLTextAreaElement].value
    mdHtml.setValue(remarkable.render(input))

class ShoppingList(userName: String) extends Component:

  override val template = Frag {
    import builders.*
    div{className("shopping-list")
      h1{t"Shopping list for $userName"}
      ul{
        li{t"Scala 3 Books"}
        li{t"Scala.js Tutorials"}
        li{t"Cooking guides!"}
      }
    }
  }

def shoppingList(userName: String): Frag = Frag {
  import builders.*
  div{className("shopping-list")
    h1{t"Shopping list for $userName"}
    ul{
      li{t"Scala 3 Books"}
      li{t"Scala.js Tutorials"}
      li{t"Cooking guides!"}
    }
  }
}

def explicitHello(name: String): Frag = Frag {
  (parentElement: Element, parentBinding: Binding[?]) ?=>
    import builders.{name as _, *}
    
    h1{h1Element ?=>
      "hello world".t(using h1Element)
    }(using parentElement)
}

val TicTacStyles = ShadowDom.WithStyle(
  //language=CSS
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
)


class TicTac1 extends WebComponent:
  override val shadowDom: ShadowDom = TicTacStyles

  override val template = Frag {
    import builders.*
    div{className("game")
      div{className("game-board")
        Board().render
      }
      div{className("game-info")
        div{/* status */}
        ol{/* todo */}
      }
    }
  }

  class Square extends Component:
    override val template = Frag {
      import builders.*
      button{className("square") /* todo */}
    }

  class Board extends Component:
    override val template = Frag {
      import builders.*

      val status = "Next player: X"

      div{
        div{className("status"); status.t}
        div{className("board-row")
          Square().render
          Square().render
          Square().render
        }
        div{className("board-row")
          Square().render
          Square().render
          Square().render
        }
        div{className("board-row")
          Square().render
          Square().render
          Square().render
        }
      }
    }
    end template
end TicTac1

class TicTac2 extends WebComponent:
  override val shadowDom: ShadowDom = TicTacStyles

  override val template = Frag {
    import builders.*
    div{className("game")
      div{className("game-board")
        Board().render
      }
      div{className("game-info")
        div{/* status */}
        ol{/* todo */}
      }
    }
  }

  class Square(position: Int) extends Component:
    override val template = Frag {
      import builders.{position as _, *}
      button{className("square"); t"$position"}
    }

  class Board extends Component:
    override val template = Frag {
      import builders.*

      val status = "Next player: X"

      div{
        div{className("status"); status.t}
        for i <- 0 until 9 by 3 do
          div{className("board-row")
            Square(i).render
            Square(i + 1).render
            Square(i + 2).render
          }
      }
    }
    end template
end TicTac2


class TicTac3 extends WebComponent:
  override val shadowDom: ShadowDom = TicTacStyles

  override val template = Frag {
    import builders.*
    div{className("game")
      div{className("game-board")
        Board().render
      }
      div{className("game-info")
        div{/* status */}
        ol{/* todo */}
      }
    }
  }

  class Square extends Component:
    override val template = Frag {
      import builders.*
      button{className("square"); onclick(_ => window.alert("clicked"))}
    }

  class Board extends Component:
    override val template = Frag {
      import builders.*

      val status = "Next player: X"

      div{
        div{className("status"); status.t}
        for i <- 0 until 9 by 3 do
          div{className("board-row")
            Square().render
            Square().render
            Square().render
          }
      }
    }
end TicTac3

class TicTac4 extends WebComponent:
  override val shadowDom: ShadowDom = TicTacStyles

  override val template = Frag {
    import builders.*
    div{className("game")
      div{className("game-board")
        Board().render
      }
      div{className("game-info")
        div{/* status */}
        ol{/* todo */}
      }
    }
  }

  class Square extends Component:
    private val wasClicked: OpenState[Boolean] = useState(false)

    override val template = Frag {
      import builders.*
      button{className("square"); onclick(_ => wasClicked.setValue(true))
        wasClicked.bind(clicked =>
          if clicked then t"X" else t""
        )
      }
    }

  class Board extends Component:
    override val template = Frag {
      import builders.*

      val status = "Next player: X"

      div{
        div{className("status"); status.t}
        for i <- 0 until 9 by 3 do
          div{className("board-row")
            Square().render
            Square().render
            Square().render
          }
      }
    }
end TicTac4

enum SquareValue(val display: String):
  case X extends SquareValue("X")
  case O extends SquareValue("O")
  case Empty extends SquareValue("")

import SquareValue.*

class TicTac5 extends WebComponent:
  import TicTac5.*

  override val shadowDom: ShadowDom = TicTacStyles

  override val template = Frag {
    import builders.*
    div{className("game")
      div{className("game-board")
        Board().render
      }
      div{className("game-info")
        div{/* status */}
        ol{/* todo */}
      }
    }
  }

  class Square(position: Int) extends Component:
    override val template = Frag {
      import builders.{position as _, *}

      button{className("square"); onclick(_ => GameState.setSquare(position))
        GameState.bind(_.boardState(position).display.t)
      }
    }

  class Board extends Component:
    override val template = Frag {
      import builders.*

      val nextPlayer = Frag {
        GameState.bind(s => if s.xIsNext then t"X" else t"O")
      }

      div{
        div{className("status")
          t"Next player: $nextPlayer"
        }
        for i <- 0 until 9 by 3 do
          div{className("board-row")
            Square(i).render
            Square(i + 1).render
            Square(i + 2).render
          }
      }
    }
end TicTac5
object TicTac5:

  case class Game(xIsNext: Boolean, boardState: IArray[SquareValue])
  val InitialGameState = Game(true, IArray.fill(9)(Empty))

  object GameState extends State(InitialGameState):

    def setSquare(position: Int): Unit =
      if value.boardState(position) != Empty then return

      val newBoardState: IArray[SquareValue] =
        value.boardState.updated(position, if value.xIsNext then X else O)

      setValue(Game(!value.xIsNext, newBoardState))


class TicTac6 extends WebComponent:
  import TicTac6.*

  override val shadowDom: ShadowDom = TicTacStyles

  override val template = Frag {
    import builders.*
    div{className("game")
      div{className("game-board")
        Board().render
      }
      div{className("game-info")
        div{/* status */}
        ol{/* todo */}
      }
    }
  }

  class Square(position: Int) extends Component:
    override val template = Frag {
      import builders.{position as _, *}

      button{className("square"); onclick(_ => GameState.setSquare(position))
        GameState.bind(_.boardState(position).display.t)
      }
    }

  class Board extends Component:
    override val template = Frag {
      import builders.*

      val status = Frag {
        GameState.bind(s => calculateWinner(s.boardState) match
          case X => t"Winner: X"
          case O => t"Winner: O"
          case Empty => t"NextPlayer: ${if s.xIsNext then "X" else "O"}"
        )
      }

      div{
        div{className("status")
          status.render
        }
        for i <- 0 until 9 by 3 do
          div{className("board-row")
            Square(i).render
            Square(i + 1).render
            Square(i + 2).render
          }
      }
    }
end TicTac6
private object TicTac6:
  case class Game(xIsNext: Boolean, boardState: IArray[SquareValue])
  val InitialGameState = Game(true, IArray.fill(9)(Empty))

  object GameState extends State(InitialGameState):

    def setSquare(position: Int): Unit =
      if value.boardState(position) != Empty
        || calculateWinner(value.boardState) != Empty then return

      val newBoardState: IArray[SquareValue] =
        value.boardState.updated(position, if value.xIsNext then X else O)

      setValue(Game(!value.xIsNext, newBoardState))
  end GameState

  val lines = IArray(
    IArray(0, 1, 2),
    IArray(3, 4, 5),
    IArray(6, 7, 8),
    IArray(0, 3, 6),
    IArray(1, 4, 7),
    IArray(2, 5, 8),
    IArray(0, 4, 8),
    IArray(2, 4, 6)
  )

  def calculateWinner(boardState: IArray[SquareValue]): SquareValue =
    lines.collectFirst {
      case IArray(a, b, c)
        if boardState(a) != Empty
          && boardState(a) == boardState(b)
          && boardState(a) == boardState(c)
        => boardState(a)
    }.getOrElse(Empty)

class TicTac7 extends WebComponent:
  import TicTac7.*

  override val shadowDom: ShadowDom = TicTacStyles

  override val template = Frag {
    import builders.*

    val status = Frag {
      GameState.bind(s => calculateWinner(s.history(s.stepNumber)) match
        case X => t"Winner: X"
        case O => t"Winner: O"
        case Empty => t"NextPlayer: ${if s.xIsNext then "X" else "O"}"
      )
    }

    val moves = Frag {
      GameState.bind(_.history.indices.foreach(move =>
        val desc =
          if move > 0 then "Go to move #" + move
          else "Go to game start"
        li{button{onclick(_ => GameState.jumpTo(move)); desc.t}}
      ))
    }

    div{className("game")
      div{className("game-board")
        Board().render
      }
      div{className("game-info")
        div{status.render}
        ol{moves.render}
      }
    }
  }

  class Square(position: Int) extends Component:
    override val template = Frag {
      import builders.{position as _, *}

      button{className("square"); onclick(_ => GameState.setSquare(position))
        GameState.bind(s => s.history(s.stepNumber)(position).display.t)
      }
    }

  class Board extends Component:
    override val template = Frag {
      import builders.*

      div{
        for i <- 0 until 9 by 3 do
          div{className("board-row")
            Square(i).render
            Square(i + 1).render
            Square(i + 2).render
          }
      }
    }
end TicTac7
object TicTac7:
  type BoardState = IArray[SquareValue]

  case class Game(xIsNext: Boolean, stepNumber: Int, history: IArray[BoardState])
  val InitialGameState = Game(true, 0, IArray(IArray.fill(9)(Empty)))

  object GameState extends State(InitialGameState):

    def setSquare(position: Int): Unit =
      val hist = value.history.slice(0, value.stepNumber + 1)
      val currBoardState = hist.last
      if currBoardState(position) != Empty || calculateWinner(currBoardState) != Empty
        then return

      val newBoardState = currBoardState.updated(position, if value.xIsNext then X else O)
      setValue(value.copy(
        xIsNext = !value.xIsNext,
        stepNumber = hist.length,
        history = hist :+ newBoardState
      ))

    def jumpTo(step: Int): Unit =
      setValue(value.copy(
        stepNumber = step,
        xIsNext = (step % 2) == 0
      ))

  end GameState

  val lines = IArray(
    IArray(0, 1, 2),
    IArray(3, 4, 5),
    IArray(6, 7, 8),
    IArray(0, 3, 6),
    IArray(1, 4, 7),
    IArray(2, 5, 8),
    IArray(0, 4, 8),
    IArray(2, 4, 6)
  )

  def calculateWinner(boardState: IArray[SquareValue]): SquareValue =
    lines.collectFirst {
      case IArray(a, b, c)
        if boardState(a) != Empty
          && boardState(a) == boardState(b)
          && boardState(a) == boardState(c)
        => boardState(a)
    }.getOrElse(Empty)

class TicTac8 extends WebComponent:
  import TicTac8.*

  override val shadowDom: ShadowDom = TicTacStyles

  override val template = Frag {
    import builders.*

    val status = Frag {
      GameState.bind(s => calculateWinner(s.history(s.stepNumber)) match
        case X => t"Winner: X"
        case O => t"Winner: O"
        case Empty => t"NextPlayer: ${if s.xIsNext then "X" else "O"}"
      )
    }

    val moves = Frag {
      GameState.bind(_.history.indices.foreach(move =>
        val desc =
          if move > 0 then "Go to move #" + move
          else "Go to game start"
        li{button{onclick(_ => GameState.jumpTo(move)); desc.t}}
      ))
    }

    div{className("game")
      div{className("game-board")
        Board().render
      }
      div{className("game-info")
        div{status.render}
        ol{moves.render}
      }
    }
  }

  class Square(position: Int) extends Component:
    override val template = Frag {
      import builders.{position as _, *}

      button{className("square"); onclick(_ => GameState.setSquare(position))
        GameState.bind(s => s.history(s.stepNumber)(position).display.t)
      }
    }

  class Board extends Component:
    override val template = Frag {
      import builders.*

      div{
        for i <- 0 until 9 by 3 do
          div{className("board-row")
            Square(i).render
            Square(i + 1).render
            Square(i + 2).render
          }
      }
    }
end TicTac8
object TicTac8:
  
  case class Game(
    xIsNext: Boolean,
    stepNumber: Int,
    history: IArray[IArray[SquareValue]]
  ) derives NativeConverter
  
  val InitialGameState = Game(true, 0, IArray(IArray.fill(9)(Empty)))
  
  object GameState extends State(InitialGameState, LocalStorage("game-state")):

    def setSquare(position: Int): Unit =
      val hist = value.history.slice(0, value.stepNumber + 1)
      val currBoardState = hist.last
      if currBoardState(position) != Empty || calculateWinner(currBoardState) != Empty
        then return

      val newBoardState = currBoardState.updated(position, if value.xIsNext then X else O)
      setValue(value.copy(
        xIsNext = !value.xIsNext,
        stepNumber = hist.length,
        history = hist :+ newBoardState
      ))

    def jumpTo(step: Int): Unit =
      setValue(value.copy(
        stepNumber = step,
        xIsNext = (step % 2) == 0
      ))

  end GameState

  val lines = IArray(
    IArray(0, 1, 2),
    IArray(3, 4, 5),
    IArray(6, 7, 8),
    IArray(0, 3, 6),
    IArray(1, 4, 7),
    IArray(2, 5, 8),
    IArray(0, 4, 8),
    IArray(2, 4, 6)
  )
  def calculateWinner(boardState: IArray[SquareValue]): SquareValue =
    lines.collectFirst {
      case IArray(a, b, c)
        if boardState(a) != Empty
          && boardState(a) == boardState(b)
          && boardState(a) == boardState(c)
      => boardState(a)
    }.getOrElse(Empty)

class ClickHole extends Component:
  private val numClicks = useState(0)
  
  override val template = Frag {
    import builders.*

    button{onclick(_ => numClicks.setValue(_ + 1))
      t"click me"
    }
    p{t"numClicks: ${numClicks.bind(_.toString.t)}"}
  }

class CustomTag extends Component:
  
  override val template = Frag {
    import builders.{tag, t}
    inline def FIXML(init: HTMLParagraphElement ?=> Unit)(using Element): Unit = tag("FIXML")(init)
    FIXML{t"100 shares"}
  }

class CustomCssProp extends Component:
  override val template = Frag {
    import builders.{p, t, cssProp}
    inline def border(style: String)(using HTMLElement): Unit = cssProp("border")(style)
    p{border("solid")
      t"Has a solid border"
    }
  }

class CustomJsProp extends Component:
  override val template = Frag {
    import builders.{p, t, prop}
    inline def onclick(fn: Event => Unit)(using Element): Unit = prop("onclick")(fn)
    p{onclick(_ => window.alert("clicked!"))
      t"click me"
    }
  }