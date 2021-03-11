package org.getshaka.shaka.docsite

import org.getshaka.shaka
import org.getshaka.shaka.{Component, ComponentBuilder}
import org.getshaka.shaka.router.Routable

import scala.scalajs.js

import scala.util.matching.Regex

class Docs extends Component with Routable:
  
  override val path: Regex = "/docs".r
  
  override def template: ComponentBuilder =
    import shaka.builders._
    div(cls("doc-body")):
      div:
        h1(cls("doc-title"), t"Hello World")
        h3(t"The smallest Shaka example looks something like this:")
        pre:
          code(cls("scala doc-code")):
            """val myApp: ComponentBuilder = h1(t"Hello, world!")
              |shaka.renderBuilder(myApp)
              |""".stripMargin.t
        p(t"""It renders ${code(t"<h1>Hello, world!</h1>")} to document.body. An optional second parameter is the Element Shaka should render to:""")
        pre:
          code(cls("scala doc-code")):
            """val mainElement = js.Dynamic.global.document.getElementById("main")
              |shaka.renderBuilder(h1(t"Hello, world!"), mainElement.asInstanceOf[Element])
              |""".stripMargin.t
        p(t"shaka.Element is a light facade of HTMLElement, with a few methods & properties needed to manipulate DOM. You can cast any type that represents HTMLElement to shaka.Element.")
        h2WithAnchor("introducing-component", "Introducing Components")
        p(t"Most of the time Shaka users will make classes implementing the Component trait:")
        pre:
          code(cls("scala doc-code")):
            """class HelloMessage(user: String) extends Component:
              |
              |  override val template: ComponentBuilder =
              |    import shaka.builders.*
              |    
              |    div(color("purple"), t"Hello $user")
              |
              |@main def launchApp: Unit =
              |  shaka.render(HelloMessage(user = "Nikki"))
              |""".stripMargin.t
        p(t"shaka.render is just like shaka.renderBuilder, but accepts Components. The contract of Component is as follows:")
        pre:
          code(cls("scala doc-code")):
            """/**
              | * Consumes a given parent Element and parent Binding
              | * to construct DOM.
              | */
              |type ComponentBuilder = (Element, Binding[?]) ?=> Unit
              |
              |/**
              | * A Shaka Component provides a template for some part of an application,
              | * as well as the means to render said template to the DOM.
              | */
              |trait Component:
              |
              |  /**
              |   * Returns the component's template. Since a template should always
              |   * be a pure function of Component state, it is recommended to
              |   * override this method as a val.
              |   */
              |  def template: ComponentBuilder
              |
              |  /**
              |   * Renders this Component's template. render should only be overriden
              |   * when implementing new Component types. For example, {@link WebComponent}
              |   * overrides render to wrap super.render in a Custom Element, allowing 
              |   * you to use shadow-dom and lifecycle callbacks.
              |   */
              |  def render(using parentElement: Element, parentBinding: Binding[?]): Unit =
              |    template(using parentElement, parentBinding)
              |""".stripMargin.t
        h3WithAnchor("composing-components", "Composing Components")
        p(t"Use Component::render to compose Components:")
        pre:
          code(cls("scala doc-code")):
            """val hello = HelloMessage("Ray")
              |val myApp: ComponentBuilder = div(hello.render, hello.render)
              |shaka.renderBuilder(myApp)
              |""".stripMargin.t
        p(t"It is ${b(t"very")} important to specify myApp as a function type, otherwise div(..) will be evaluated eagerly.")
        h2WithAnchor("web-component", "WebComponent")
        p(t"""A WebComponent is a Component wrapped in a ${a(href("https://developer.mozilla.org/en-US/docs/Web/Web_Component"), target("_blank"), t"Custom Element")}, the ultimate means of browser encapsulation. The contract is as follows:""")
        pre:
          code(cls("scala doc-code")):
            """/**
              | * A {@link Component} wrapped in a Custom Element. Custom Elements
              | * have useful lifecycle callbacks and can attach shadow-dom.
              | */
              |trait WebComponent extends Component:
              |
              |  /**
              |   * Triggered whenever this WebComponent is appended into a
              |   * document-connected element. WebComponent ensures
              |   * connectedCallback is never triggered when the element is
              |   * not connected (a non-obvious behavior of regular 
              |   * custom elements).
              |   */
              |  def connectedCallback(): Unit = ()
              |
              |  /**
              |   * Invoked each time the custom element is disconnected
              |   * from the document's DOM.
              |   */
              |  def disconnectedCallback(): Unit = ()
              |
              |  /**
              |   * Any scoped styles are applied to this and this WebComponent only.
              |   * When scopedStyle is not null, an open Shadow DOM is appended to this
              |   * WebComponent. That means ids, class names, and selectors can be
              |   * used without fear of conflicts.
              |   */
              |  def scopedStyle: String|Null = null
              |  
              |  def render(using parentElement: Element, parentBinding: Binding[?]): Unit = ???
              |""".stripMargin.t
        h2WithAnchor("builders", "Builders")
        p(t"The shaka.builders package has helpers useful for the construction of DOM. Importing shaka.builders.* brings in many identifiers, so it's best to scope the import within the method using them. There are three types of helpers available; Element builders, CSS props, and JavaScript props.")
        h3WithAnchor("element-builders", "Element Builders")
        pre:
          code(cls("scala doc-code")):
            """val template: ComponentBuilder =
              |  import shaka.builders.*
              |
              |  h1(color("blue"), t"hello")
              |  
              |  h1(color("blue")) {
              |    t"hello"
              |  }
              |  
              |  h1(color("blue")):
              |    t"hello"
              |    
              |  h1:
              |    color("blue")
              |    t"hello"
              |""".stripMargin.t
        p(t"All four uses of h1 above make the same DOM. Use whatever you're most comfortable with!")
        p(t"To use an empty element like <br>, you need empty parens:")
        pre:
          code(cls("scala doc-code")):
            """val aLineBreak: ComponentBuilder =
              |  import shaka.builders.*
              |  
              |  br()
              |""".stripMargin.t
        p(t"""Here's the definition of h1. It's ok if you don't understand it, but if you want to, the docs on ${a(href("https://dotty.epfl.ch/docs/reference/contextual/context-functions.html#example-builder-pattern"), t"Context Functions")} are helpful.""")
        pre:
          code(cls("scala doc-code")):
            """/**
              | * Function that appends to a given parent Element and returns Unit
              | */
              |type ElementBuilder = Element ?=> Unit
              |
              |/**
              | * Consumer function that accepts a function appending to a given
              | * parent Element.
              | */
              |type ElementBuilderBody = (Element ?=> Unit) => Unit
              |
              |inline def h1(init: ElementBuilder*)(using parent: Element): ElementBuilderBody =
              |  makeElement("h1", parent, init: _*)
              |""".stripMargin.t
        h3WithAnchor("css-props", "Css Props")
        p(t"As we've already seen, shaka.builders provides helpers for defining CSS properties. Here's how color is defined:")
        pre:
          code(cls("scala doc-code")):
            """val color = shaka.builders.CssProp("color")
              |""".stripMargin.t
        p(t"You can define your own CssProps with the CssProp constructor. There's so many CSS selectors, we've only defined about 100 of the most important ones. If you think a good one is missing, please make an issue.")
        h3WithAnchor("javascript-props", "JavaScript Props")
        pre:
          code(cls("scala doc-code")):
            """val myApp: ComponentBuilder =
              |  import shaka.builders.{h1, txt}
              |  
              |  val onclick = Prop("onclick")
              |  h1(onclick(println("clicked")), t"click me")
              |""".stripMargin.t
        p(t"""While CssProps like className only accept Strings, instances of Prop take any type with a given ${a(href("https://github.com/getshaka-org/native-converter"), target("_blank"), t"NativeConverter")} typeclass available. Functions and many other types have NativeConverters already predefined. And if the parameter is of type Unit, no `() =>` is needed.""")
        h3WithAnchor("t", "t - TextNode Interpolater")
        p(t"t is a String interpolator very helpful for rich text. For example, the link to NativeConverter was defined like this:")
        pre:
          code(cls("scala doc-cod")):
            "p(t\"... with a given ${a(href(\"https://github.com/getshaka-org/native-converter\"), t\"NativeConverter\")} typeclass available.\")".t
        p(t"t is also defined as an extension method on String.")
        h2WithAnchor("state-and-bindings", "State and Bindings")
        p(t"""To see State and Bindings used in a real app, checkout the ${a(href("tutorial"), t"Tutorial")}.""")
        h3WithAnchor("state", "abstract State")
        pre:
          code(cls("scala doc-code")):
            """/**
              | * A State that can be used to bind Nodes and their properties. You can
              | * get the current value with myState.value, and update with
              | * myState.value = newValue. These two methods are protected, so that
              | * state update logic is encapsulated within the object itself. This provides
              | * the same benefit as Redux's action - reducer - store trifecta,
              | * but dramatically simpler and not requiring copying the whole state value
              | * on every change (although if you believe in encapsulation and
              | * program in a purely functional style, you will still do this..
              | * but it will be your choice!).
              | * <br>
              | * @param initialValue The State's initial value, discarded if
              | *                     the StorageManager can fetch a value.
              | * @param storageManager How the State should be persisted, such
              | *                       as in LocalStorage. By default,
              | *                       the State has no storage.
              | * @tparam V the value.
              | */
              |abstract class State[V](initialValue: => V, storageManager: StorageManager[V] = NoStorage[V]()):
              |  /**
              |   * Returns this State's value
              |   */
              |  protected def value: V = ???
              |
              |  /**
              |   * Sets this State's new value. All depdendent Bindings are recomputed.
              |   */
              |  protected def value_=(newValue: V): Unit = ???
              |  
              |  /**
              |   * Function from V => Unit, given a parent Element and parent Binding[?]
              |   */
              |  type NodeBindingBuilder = (Element, Binding[?]) ?=> (V => Unit)
              |
              |  /**
              |   * Construct a Binding for DOM Nodes
              |   */
              |  def bind(builder: NodeBindingBuilder)(using pe: Element, pb: Binding[?]): Unit = ???
              |  
              |  /**
              |   * Function from V => Unit, given a parent Binding[?]
              |   */
              |  type PropBindingBuilder = Binding[?] ?=> V => Unit
              |
              |  /**
              |   * Construct a lighter-weight Binding for CSS/JS Props.
              |   */
              |  def bindProps(builder: PropBindingBuilder)(using pb: Binding[?]): Unit = ???
              |  
              |  /**
              |   * Removes a Binding from this State.
              |   */
              |  def removeBinding(b: Binding[V]): Unit = ??
              |""".stripMargin.t
        p(t"When the value setter is called, all dependent bind() expressions are recomputed. You can bind multiple Elements without needing something like React.Fragment. And you can nest bindings without worrying about memory leaks.")
        h3WithAnchor("useState", "shaka.useState")
        p(t"useState provides an api similar in spirit to React's useState hook.")
        pre:
          code(cls("scala doc-code")):
            """class ClickHole extends Component:
              |  private val numClicks: OpenState[Int] = useState(0)
              |  
              |  override val template: ComponentBuilder =
              |    import shaka.builders.*
              |
              |    button(onclick(numClicks.value += 1)):
              |      t"click me"
              |    p(t"numClicks: ${numClicks.bind(_.toString.t)}")
              |""".stripMargin.t
        hr()
        ClickHole().render
        hr()
        p(t"useState returns an instance of OpenState, a subclass of State. Because OpenState's value getter and setter are public, it must always be encapsulated.")
        h3WithAnchor("storage-manager", "Storage Managers")
        p(t"A cool property about State is that you can configure its persistence, whether with shaka.LocalStorage, shaka.SessionStorage, or any provider implmenting trait shaka.StorageManager. By default, a no-op StorageManager is used.")
        pre:
          code(cls("scala doc-code")):
            """object ClickState extends State(0, LocalStorage("click-state"))
              |""".stripMargin.t
        p(t"shaka.LocalStorage and SessionStorage uses a given NativeConverter typeclass to serialize the state to a JSON String and back.")
//        h2WithAnchor("shaka-router", "Shaka Router")
//        p(t"When evaluating web frameworks, extensibility is very important. shaka-router, The SPA router used in this website, is quite feature-rich and only 160 lines of Scala.")

    highlightScala()

  def h2WithAnchor(id: String, text: String): ComponentBuilder =
    import shaka.builders.{id as htmlId, *}
    h2(htmlId(id)):
      a(href("#" + id), """🔗 """.t)
      text.t

  def h3WithAnchor(id: String, text: String): ComponentBuilder =
    import shaka.builders.{id as htmlId, *}
    h3(htmlId(id)):
      a(href("#" + id), """🔗 """.t)
      text.t


  def anchorForId(id: String, text: String): ComponentBuilder =
    import shaka.builders.{id as htmlId, *}
    a(href("#" + id), text.t)

