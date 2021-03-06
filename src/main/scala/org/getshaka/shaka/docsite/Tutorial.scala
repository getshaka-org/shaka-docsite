package org.getshaka.shaka.docsite

import org.getshaka.shaka
import org.getshaka.shaka.{Binding, Component, ComponentBuilder, Element}
import org.getshaka.shaka.router.Routable

import scala.scalajs.js
import scala.util.matching.Regex

class Tutorial extends Component with Routable:

  override val path: Regex = "/tutorial".r

  override val template: ComponentBuilder =
    import shaka.builders.*
    div{cls("doc-body")
      div{
        h1{cls("doc-title"); t"Tutorial: Intro to Shaka"}
        h3{t"Lets build a simple tic-tac-toe game, just like in the React tutorial."}
        hr{}
        div{TicTac7().render}
        hr{}
      }
      h2WithAnchor("setup", "Setup for the Tutorial")
      p{t"Clone this repo for the starter code:"}
      pre{
        code{cls("scala doc-code")
          """git clone https://github.com/getshaka-org/shaka-tutorial-starter""".t
        }
      }
      p{t"Then compile the project and open index.html in your favorite browser:"}
      pre{
        code{cls("scala doc-code")
          """cd shaka-tutorial-starter
            |sbt ~fastLinkJS
            |""".stripMargin.t
        }
      }
      p{t"""See the ${a{href("get-started"); t"Get Started"}} section if you have questions. To jump to the final code, ${anchorForId("final-result", "click here")}."""}
      h2WithAnchor("what-is-shaka", "What is Shaka?")
      p{t"Shaka is a declarative Scala.js library for building user interfaces. It lets you compose UIs from small and encapsulated pieces. The typical way to use Shaka is by building classes that extend the ${a{"Component".t; target("_blank"); href("https://javadoc.io/doc/org.getshaka/shaka_sjs1_3/latest/api/org/getshaka/shaka/Component.html")}} trait:"}
      pre{
        code{cls("scala doc-code")
          """class ShoppingList(name: String) extends Component:
            |
            |  override val template: ComponentBuilder =
            |    import shaka.builders.{name as _, *}
            |
            |    div{className("shopping-list")
            |      h1{t"Shopping list for $name"}
            |      ul{
            |        li{t"Scala 3 Books"}
            |        li{t"Scala.js Tutorials"}
            |        li{t"Cooking guides!"}
            |      }
            |    }
            |
            |@main def launchApp(): Unit =
            |  val doc = js.Dynamic.global.document
            |  shaka.render(TodoList("John"), doc.body.asInstanceOf[Element])
            |""".stripMargin.t
        }
      }
      p{t"Although in this example, it's easier to just define a method:"}
      pre{
        code{cls("scala doc-code")
          """def shoppingList(name: String): ComponentBuilder =
            |  import shaka.builders.{name as _, *}
            |
            |  div{className("shopping-list")
            |    h1{t"Shopping list for $name"}
            |    ul{
            |      li{t"Scala 3 Books"}
            |      li{t"Scala.js Tutorials"}
            |      li{t"Cooking guides!"}
            |    }
            |  }
            |
            |@main def launchApp(): Unit =
            |  val doc = js.Dynamic.global.document
            |  shaka.renderBuilder(todoList("John"), doc.body.asInstanceOf[Element])
            |""".stripMargin.t
        }
      }
      p{t"The ComponentBuilder type is a function that, given a parent Element and parent Binding, returns Unit. The ComponentBuilders in ${a{"shaka.builders".t; target("_blank"); href("https://javadoc.io/doc/org.getshaka/shaka_sjs1_3/latest/api/org/getshaka/shaka/builders.html")}} use these contextual parameters to construct the DOM. A 'contextual' parameter is just one that we don't need to specify, although we can if we need to:"}
      pre{
        code{cls("scala doc-code")
          """def explicitHello(name: String): ComponentBuilder =
            |  (parentElement: Element, parentBinding: Binding[?]) ?=>
            |    import shaka.builders.{name as _, *}
            |    
            |    h1{h1Element ?=>
            |      "hello world".t(using h1Element)
            |    }(using parentElement)
            |""".stripMargin.t
        }
      }
      p{t"""While you don't need to know this, using ${a{href("https://dotty.epfl.ch/docs/reference/contextual/context-functions.html#example-builder-pattern"); target("_blank"); t"Context Functions"}} in this way allows Shaka to efficiently build and manage Bindings."""}
      p{t"The Component trait defines two methods: template, which returns a ComponentBuilder, and render, which uses the template to render the component. Render is a default method and should only be overriden when implementing new Component types. For example, ${a{"WebComponent".t; target("_blank"); href("https://javadoc.io/doc/org.getshaka/shaka_sjs1_3/latest/api/org/getshaka/shaka/WebComponent.html")}} overrides render to use Custom Elements, enabling scoped-CSS and lifecycle callbacks."}
      p{t"Notice that the builder methods are implemented with plain Scala code. That means you can can put anything inside them, including vals and for loops. As long as the template remains a pure function of component state, the possibilities are endless."}
      h2WithAnchor("starter-code", "Starter Code")
      hr{}
      TicTac1().render
      hr{}
      p{t"After building the starter project you should see something like this. Inspecting the code, you'll notice we have three Components:"}
      ul{
        li{t"Square"}
        li{t"Board"}
        li{t"TicTacToe"}
      }
      p{t"The Square class renders a single square, and Board renders 9 Squares. TicTacToe will show the Board, next player, and (eventually) game history."}
      h3WithAnchor("passing-data-through-constructors", "Passing Data Through Constructors")
      p{t"The easiest way to pass state to a Shaka Component is with a constructor. This is the equivalent of React 'props', with the benefit of an api contract and static typing. Lets update Square and Board to make each Square display its position in the Board:"}
      pre{
        code{cls("scala doc-code")
          """class Square(position: Int) extends Component:
            |  override val template: ComponentBuilder =
            |    import shaka.builders.{position as _, *}
            |    button{className("square"); position.toString.t}
            |""".stripMargin.t
        }
      }
      p{t"And in Board, we change renderSquare to use the constructor. While we're at it, lets reduce the repetitive renderSquare invocations with a for loop."}
      pre{
        code{cls("scala doc-code")
          """class Board extends Component:
            |  override val template: ComponentBuilder =
            |    import shaka.builders.*
            |
            |    def renderSquare(i: Int): ComponentBuilder =
            |      Square(position = i).render
            |
            |    val status = "Next player: X"
            |
            |    div{
            |      div{className("status"); status.t}
            |      for i <- 0 until 9 by 3 do
            |        div{className("board-row")
            |          renderSquare(i)
            |          renderSquare(i + 1)
            |          renderSquare(i + 2)
            |        }
            |    }
            |""".stripMargin.t
        }
      }
      p{t"After recompilation with fastLinkJS and refreshing the browser you should see a number in each square of the rendered output."}
      hr{}
      TicTac2().render
      hr{}
      p{t"Pretty easy so far, right?! :)"}
      h3WithAnchor("making-an-interactive-component", "Making an Interactive Component")
      p{t"""Like in the React tutorial, lets fill a Square with "X" when we click it. As a first step, remove the Square constructor parameter and add an onclick listener showing a browser alert."""}
      pre{
        code{cls("scala doc-code")
          """class Square extends Component:
            |  override val template: ComponentBuilder =
            |    import shaka.builders.*
            |    button{className("square"); onclick(() => js.Dynamic.global.alert("clicked"))}
            |""".stripMargin.t
        }
      }
      hr{}
      TicTac3().render
      hr{}
      p{t"""onclick will assign its parameter to the onclick property of whatever given Element is found first in scope, in this case a <button> Element. Unlike the css props which accept only String, onclick takes any parameter with a given ${a{href("https://github.com/getshaka-org/native-converter"); t"NativeConverter typeclass"}} available. Functions and many other types have NativeConverters already predefined. If you click a Square now, you should see a browser alert."""}
      p{t"Next, we'll make Square keep track of when it's been clicked by using a State Hook. "}
      pre{
        code{cls("scala doc-code")
          """class Square extends Component:
            |  private val wasClicked: OpenState[Boolean] = shaka.useState(false)
            |
            |  override val template: ComponentBuilder =
            |    import shaka.builders.*
            |    button{className("square"); onclick(() => wasClicked.setValue(true))
            |      wasClicked.bind(clicked =>
            |        if clicked then t"X" else t""
            |      )
            |    }
            |""".stripMargin.t
        }
      }
      hr{}
      TicTac4().render
      hr{}
      p{t"useState returns an instance of ${a{"OpenState".t; target("_blank"); href("https://javadoc.io/doc/org.getshaka/shaka_sjs1_3/latest/api/org/getshaka/shaka/OpenState.html")}} and should always be encapsulated. When the state's value setter is called, every dependent Binding gets recomputed. While this example does not show it, Bindings can be nested and composed without worrying about memory leaks. This is because Bindings are constructed into a managed directed acyclic dependency graph (DAG). When a Binding's Element is no longer part of the DOM, it is destroyed. Unlike VDOM, Shaka data bindings are precise and only recompute what is needed."}
      h3WithAnchor("developer-tools", "Developer Tools")
      p{t"Shaka generates DOM elements transparently; no browser extension like React Devtools is needed to understand the DOM. Scala.js automatically generates source maps for line-by-line debugging."}
      h2WithAnchor("completing-the-game", "Completing the Game")
      p{t"The building blocks of our tic-tac-toe game are now in place. To complete the game we need to alternate X and Os on the board, as well as make a method computing the winner."}
      h3WithAnchor("lifting-state-up", "Lifting State Up")
      p{t"Each Square currently maintains its own state, and to determine a winner we must know the value of each Square. Unfortunately, it's a bad practice for parent components to depend on the state of children. We could lift the state for all 9 Squares into Board, and pass a function for Square to execute onclick. But passing state down the component hierarchy quickly gets laborious and confusing. A Redux-like alternative is implementing the abstract State class. Because the value getter and setter are protected, the State object must define all the update methods (Redux 'actions') in one centralized, maintainable location."}
      p{t"First, define case class Game."}
      pre{
        code{cls("scala doc-code")
          """enum SquareValue(val display: String):
            |  case X extends SquareValue("X")
            |  case O extends SquareValue("O")
            |  case Empty extends SquareValue("")
            |
            |import SquareValue.*
            |
            |case class Game(xIsNext: Boolean, boardState: IArray[SquareValue])
            |""".stripMargin.t
        }
      }
      p{t"""Notice that type SquareValue is a Scala 3 enum, being stored in an immutable Array! We can now create the GameState object, with all-empty squareValues and X going first:"""}
      pre{
        code{cls("scala doc-code")
          """object GameState extends State(Game(true, IArray.fill(9)(Empty))):
            |
            |  def setSquare(position: Int): Unit =
            |    if value.boardState(position) != Empty then return
            |
            |    val newBoardState: IArray[SquareValue] =
            |      value.boardState.updated(position, if value.xIsNext then X else O)
            |
            |    setValue(Game(!value.xIsNext, newBoardState))
            |""".stripMargin.t
        }
      }
      p{t"""Having the State's exposed type be immutable is critical to prevent user modification in a bind() expression. With GameState thus defined, we can make Square call setSquare when clicked."""}
      pre{
        code{cls("scala doc-code")
          """class Square(position: Int) extends Component:
            |  override val template: ComponentBuilder =
            |    import shaka.builders.{position as _, *}
            |
            |    button{className("square"); onclick(() => GameState.setSquare(position))
            |      GameState.bind(_.boardState(position).display.t)
            |    }
            |""".stripMargin.t
        }
      }
      p{t"Likewise, we can make Board bind GameState to show the next player."}
      pre{
        code{cls("scala doc-code")
          """class Board extends Component:
            |  override val template: ComponentBuilder =
            |    import shaka.builders.*
            |
            |    def renderSquare(i: Int): ComponentBuilder =
            |      Square(position = i).render
            |
            |    val nextPlayer: ComponentBuilder =
            |      GameState.bind(s => if s.xIsNext then t"X" else t"O")
            |
            |    div{
            |      div{className("status")
            |        t"Next player: $nextPlayer"
            |      }
            |      for i <- 0 until 9 by 3 do
            |        div{className("board-row")
            |          renderSquare(i)
            |          renderSquare(i + 1)
            |          renderSquare(i + 2)
            |        }
            |    }
            |""".stripMargin.t
        }
      }
      p{t"${a{"For now".t; target("_blank"); href("https://github.com/getshaka-org/shaka/issues/4")}}, it is ${b{t"very"}} important to specify the type for nextPlayer, and all other builder vals & defs we want to evaluate in different contexts. If we don't, the methods will be evaluated eagerly."}
      p{t"Something else to mention is the TextNode interpolater, t. Did you know it can make rich text?"}
      pre{
        code{cls("scala doc-code")
          "p(t\"Hello, ${b(t\"bold\")} world!\")".t
        }
      }
      p{t"""Hello, ${b{t"bold"}} world!"""}
      p{t"t is also an extension method on String. With Square and Board upgraded the base game is nearly complete:"}
      hr{}
      TicTac5().render
      hr{}
      h2WithAnchor("declaring-a-winner", "Declaring a Winner")
      p{t"Copy this helper method to the end of the file:"}
      pre{
        code{cls("scala doc-code")
          """val lines = IArray(
            |  IArray(0, 1, 2),
            |  IArray(3, 4, 5),
            |  IArray(6, 7, 8),
            |  IArray(0, 3, 6),
            |  IArray(1, 4, 7),
            |  IArray(2, 5, 8),
            |  IArray(0, 4, 8),
            |  IArray(2, 4, 6)
            |)
            |
            |def calculateWinner(boardState: IArray[SquareValue]): SquareValue =
            |  for IArray(a, b, c) <- lines do
            |    if
            |      boardState(a) != Empty &&
            |      boardState(a) == boardState(b) &&
            |      boardState(a) == boardState(c)
            |    then return boardState(a)
            |  return Empty
            |""".stripMargin.t
        }
      }
      p{t"Then, all we need to do is make Board test the game squares when GameState changes:"}
      pre{
        code{cls("scala doc-code")
          """class Board extends Component:
            |  override val template: ComponentBuilder =
            |    import shaka.builders.*
            |
            |    def renderSquare(i: Int): ComponentBuilder =
            |      Square(position = i).render
            |
            |    val status: ComponentBuilder =
            |      GameState.bind(s => calculateWinner(s.boardState) match
            |        case X => t"Winner: X"
            |        case O => t"Winner: O"
            |        case Empty => t"NextPlayer: ${if s.xIsNext then "X" else "O"}"
            |      )
            |
            |    div{
            |      div{className("status")
            |        status
            |      }
            |      for i <- 0 until 9 by 3 do
            |        div{className("board-row")
            |          renderSquare(i)
            |          renderSquare(i + 1)
            |          renderSquare(i + 2)
            |        }
            |    }
            |""".stripMargin.t
        }
      }
      p{t"And have setSquare short-circuit when a winner is determined."}
      pre{
        code{cls("scala doc-code")
          """  def setSquare(position: Int): Unit =
            |   if value.boardState(position) != Empty
            |     || calculateWinner(value.boardState) != Empty then return
            |
            |   val newBoardState: IArray[SquareValue] =
            |     value.boardState.updated(position, if value.xIsNext then X else O)
            |
            |   setValue(Game(!value.xIsNext, newBoardState))
            |""".stripMargin.t
        }
      }
      p{t"Since GameState is the only object able to update the state, it would be smart to add a `winner` field to Game, and calculateWinner only once as part of setSquare. But for such a small & fast method it doesn't really matter."}
      hr{}
      TicTac6().render
      hr{}
      h2WithAnchor("adding-time-travel", "Adding Time Travel")
      p{t"The final exercise is to add history and the ability to revert previous moves in the game"}
      h3WithAnchor("storing-a-history-of-moves", "Storing a History of Moves")
      p{t"We will update class Game to hold a history of SquareValues, and keep track of the current position in the history."}
      pre{
        code{cls("scala doc-code")
          """type BoardState = IArray[SquareValue]
            |
            |case class Game(xIsNext: Boolean, stepNumber: Int, history: IArray[BoardState])
            |""".stripMargin.t
        }
      }
      p{t"Next, make setSquare update the history based on the current stepNumber and selected square. We also add method jumpTo, which allows one to change the stepNumber (time travel)."}
      pre{
        code{cls("scala doc-code")
          """object GameState extends State(Game(true, 0, IArray(IArray.fill(9)(Empty)))):
            |
            |  def setSquare(position: Int): Unit =
            |    val hist = value.history.slice(0, value.stepNumber + 1)
            |    val currBoardState = hist.last
            |    if currBoardState(position) != Empty || calculateWinner(currBoardState) != Empty
            |      then return
            |
            |    val newBoardState = currBoardState.updated(position, if value.xIsNext then X else O)
            |    setValue(value.copy(
            |      xIsNext = !value.xIsNext,
            |      stepNumber = hist.length,
            |      history = hist :+ newBoardState
            |    ))
            |
            |  def jumpTo(step: Int): Unit =
            |    setValue(value.copy(
            |      stepNumber = step,
            |      xIsNext = (step % 2) == 0
            |    ))
            |""".stripMargin.t
        }
      }
      p{t"Square must be updated to show the SquareValue for the current stepNumber"}
      pre{
        code{cls("scala doc-code")
          """class Square(position: Int) extends Component:
            |  override val template: ComponentBuilder =
            |    import shaka.builders.{position as _, *}
            |
            |    button{className("square"); onclick(() => GameState.setSquare(position))
            |      GameState.bind(s => s.history(s.stepNumber)(position).display.t)
            |    }
            |""".stripMargin.t
        }
      }
      p{t"The only change to Board is removing the status, since we'll show it in TicTacToe now."}
      pre{
        code{cls("scala doc-code")
          """class Board extends Component:
            |  override val template: ComponentBuilder =
            |    import shaka.builders.*
            |
            |    def renderSquare(i: Int): ComponentBuilder =
            |      Square(position = i).render
            |
            |    div{
            |      for i <- 0 until 9 by 3 do
            |        div{className("board-row")
            |          renderSquare(i)
            |          renderSquare(i + 1)
            |          renderSquare(i + 2)
            |        }
            |    }
            |""".stripMargin.t
        }
      }
      p{t"And finally, we can implement the todos in TicTacToe. Every history entry available can be jumped to by clicking the list."}
      pre{
        code{cls("scala doc-code")
          """override val template: ComponentBuilder =
            |  import shaka.builders.*
            |
            |  val status: ComponentBuilder =
            |    GameState.bind(s => calculateWinner(s.history(s.stepNumber)) match
            |      case X => t"Winner: X"
            |      case O => t"Winner: O"
            |      case Empty => t"NextPlayer: ${if s.xIsNext then "X" else "O"}"
            |    )
            |
            |  val moves: ComponentBuilder =
            |    GameState.bind(_.history.indices.foreach(move =>
            |      val desc =
            |        if move > 0 then "Go to move #" + move
            |        else "Go to game start"
            |      li{button{onclick(() => GameState.jumpTo(move)); desc.t}}
            |    ))
            |
            |  div{className("game")
            |    div{className("game-board")
            |      Board().render
            |    }
            |    div{className("game-info")
            |      div{status}
            |      ol{moves}
            |    }
            |  }
            |""".stripMargin.t
        }
      }
      h2WithAnchor("final-result", "Final Result")
      p{t"We did it! Checkout the git branch 'final-code' for a local copy."}
      hr{}
      TicTac7().render
      hr{}
      h2WithAnchor("state-persistence", "Bonus: State Persistence")
      p{t"A final cool property about State is that you can configure its persistence, whether in the LocalStorage, SessionStorage, or any provider implmenting trait shaka.StorageManager. First, add an instance of the desired StorageManager to State's constructor."}
      pre{
        code{cls("scala doc-code")
          """object GameState extends State(Game(true, 0, IArray(IArray.fill(9)(Empty))), LocalStorage("game-state"))
            |""".stripMargin.t
        }
      }
      p{t"""shaka.LocalStorage uses a given NativeConverter typeclass to serialize Game to a JSON String and back. NativeConverter supports ${a{href("https://dotty.epfl.ch/docs/reference/contextual/derivation.html"); target("_blank"); t"Typeclass derivation"}}, making it easy to generate an implementation."""}
      pre{
        code{cls("scala doc-code")
          """case class Game(xIsNext: Boolean, stepNumber: Int, history: IArray[IArray[SquareValue]]) derives NativeConverter
            |""".stripMargin.t
        }
      }
      p{t"And now, the game state will persist when you refresh your browser."}
      hr{}
      TicTac8().render
      hr{}
    }
    highlightScala()
  end template

  def h2WithAnchor(id: String, text: String): ComponentBuilder =
    import shaka.builders.{id as htmlId, *}
    h2{htmlId(id)
      a{href("tutorial#" + id); """???? """.t}
      text.t
    }

  def h3WithAnchor(id: String, text: String): ComponentBuilder =
    import shaka.builders.{id as htmlId, *}
    h3{htmlId(id)
      a{href("tutorial#" + id); """???? """.t}
      text.t
    }

  def anchorForId(id: String, text: String): ComponentBuilder =
    import shaka.builders.{id as htmlId, *}
    a{href("tutorial#" + id); text.t}
