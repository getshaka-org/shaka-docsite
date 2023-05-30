package org.getshaka.shaka.docsite

import org.getshaka.shaka.*
import org.getshaka.nativeconverter.NativeConverter

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import scala.util.matching.Regex

class Home extends Component:

  def template = Frag:
    import builders.*

    div:
      id("title-box")
      h1 { t"Shaka" }
      h2 { t"A Scala.js library for building user interfaces" }
      div:
        id("title-box-links");
        cls("row")
        a:
          cls("col");
          href("get-started")
          div:
            id("title-get-started")
            h3 { t"Get Started" }
        a:
          cls("col");
          href("tutorial")
          div:
            id("title-take-tutorial")
            h3 { t"Take the Tutorial" }
    div:
      id("main-body")
      div:
        id("three-features"); cls("row-main")
        div:
          cls("col-main")
          h3 { t"Precise Binding" }
          p { t"Shaka makes it easy to build interactive, declarative UIs." }
          p:
            t"""
               |It starts with precise and efficient data binding. No need for VDOM, confusing macros, or AOT compilation.
               |Bindings are automatically managed to prevent leaks.
               |"""
        div:
          cls("col-main")
          h3 { t"Functional Components" }
          p { t"Components are encapsulated and composable." }
          p:
            t"State management is simplified, with the most useful benefits of Redux built in."
          p:
            t"Use the same language on your JVM backend for massive code reuse."
        div:
          cls("col-main")
          h3 { t"Strongly Typed" }
          p:
            t"Let ${a {
                href("https://github.com/getshaka-org/shaka"); target("_blank"); t"Scala 3's"
              }} rich type system work for you."
          p:
            t"""
               |Using an object-oriented, functional, and even dynamic language makes it easy to model and import
               |JavaScript & Typescript libraries. Incremental compilation is fast and editor support outstanding.
               |"""
      div:
        cls("example-row")
        div:
          cls("example-descr")
          h2 { t"A Simple Component" }
          p:
            t"""Shaka Components have a template that is used during rendering.
               |
               |The builders package has methods to construct DOM. div builds a div Element, color applies the CSS property, and 't' interpolates TextNodes.
               |"""
        div:
          cls("code-in-out")
          pre:
            code:
              cls("scala")
              t"""${alignCodeBlock}
                 |import org.getshaka.shaka.*
                 |import org.scalajs.dom.*
                 |
                 |class HelloMessage(user: String) extends Component:
                 |
                 |  def template = Frag:
                 |    import builders.*
                 |    div:
                 |      color("purple")
                 |      t"Hello $$user"
                 |
                 |@main def launchApp(): Unit =
                 |  render(HelloMessage(user = "Nikki"), document.body)
                 |
                 |"""
          div:
            cls("code-out")
            div { cls("code-out-title"); p(t"RESULT") }
            HelloMessage("Nikki").render
      div:
        cls("example-row")
        div:
          cls("example-descr")
          h2 { t"Generated Code" }
          p:
            t"""To the right is the (unminified) JavaScript for HelloMessage, generated with `sbt fullLinkJS`. Shaka uses ${a {
                href(
                  "https://dotty.epfl.ch/docs/reference/contextual/context-functions.html#example-builder-pattern"
                ); target("_blank"); t"Context Functions"
              }} for its fluent builder api, but executing a function for every DOM element could be slow. So in addition we use Scala 3's ${a {
                href(
                  "https://dotty.epfl.ch/docs/reference/metaprogramming/inline.html"
                ); target("_blank"); t"Guaranteed Inlining"
              }} to elide the functions at compile time, producing highly efficient JavaScript."""
        div:
          cls("code-in-out")
          pre:
            code:
              cls("scala")
              t"""${alignCodeBlock}
                 |function sm(a) {
                 |  this.Tm = null;
                 |  this.Tm = new I((b => c => {
                 |    var d = document.createElement("div");
                 |    d.style.color = "purple";
                 |    var f = document.createElement("p");
                 |    f.appendChild(document.createTextNode("Hello "));
                 |    f.appendChild(document.createTextNode(b));
                 |    f.appendChild(document.createTextNode(""));
                 |    d.appendChild(f);
                 |    c.appendChild(d)
                 |  })(a))
                 |}
                 |
                 |"""
          div:
            cls("code-out")
            div { cls("code-out-title"); p(t"RESULT") }
            HelloMessage("Nikki").render
      div:
        cls("example-row")
        div:
          cls("example-descr")
          h2 { t"A Stateful Component" }
          p:
            t"""In addition to passing state through constructors, a component can maintain internal state with the useState 'hook'.
               |
               |A WebComponent is a Component wrapped in a Custom Element, allowing you to used scoped styles and lifecycle callbacks.
               |
               |If Timer is removed from the DOM, the interval and binding are destroyed.
               |"""
        div:
          cls("code-in-out")
          pre:
            code:
              cls("scala")
              t"""${alignCodeBlock}
                 |import org.getshaka.shaka.*
                 |import scala.scalajs.js.timers.*
                 |import scala.concurrent.duration.*
                 |
                 |class Timer extends WebComponent:
                 |  private val seconds = useState(0)
                 |  private var interval: SetIntervalHandle = null
                 |
                 |  override def connectedCallback(): Unit =
                 |    interval = setInterval(1.second):
                 |      seconds.setValue(_ + 1)
                 |
                 |  override def disconnectedCallback(): Unit =
                 |    clearInterval(interval)
                 |
                 |  override def shadowDom = ShadowDom.WithStyle(
                 |    " span { color: green; } "
                 |  )
                 |
                 |  def template = Frag:
                 |    import builders.*
                 |    seconds.bind: value =>
                 |      span { t"Seconds: $$value" }
                 |
                 |"""
          div:
            cls("code-out")
            div { cls("code-out-title"); p { t"RESULT" } }
            Timer().render
      div:
        cls("example-row")
        div:
          cls("example-descr")
          h2 { t"An Application" }
          p:
            t"""Using Components and useState, we can put together a small Todo application. This example uses state hooks to keep track of the current list of todo items as well as the text entered by the user.
               |
               |One benefit of Shaka using code blocks is that you can do anything in them, like defining variables and using for loops. This is a major benefit versus JSX, which can only have simple expressions inside {}. As long as the template remains a pure function of component state, the possibilities are endless.
               |"""
        div:
          cls("code-in-out")
          pre:
            code:
              cls("scala")
              t"""${alignCodeBlock}
                 |import org.getshaka.shaka.*
                 |import org.scalajs.dom.*
                 |
                 |case class Item(date: js.Date, text: String)
                 |
                 |class TodoApp extends Component:
                 |  private val items = useState(IArray.empty[Item])
                 |  private val text = useState("")
                 |
                 |  def template = Frag:
                 |    import builders.*
                 |    div:
                 |      h3{color("royalblue"); t"TODO"}
                 |      items.bind(TodoList(_).render)
                 |      form:
                 |        onsubmit(handleSubmit)
                 |        label:
                 |          `for`("new-todo")
                 |          t"What needs to be done?"
                 |        br{}
                 |        input:
                 |          id("new-todo")
                 |          width("100px")
                 |          autocomplete("off")
                 |          onchange(handleChange)
                 |          text.bindProps(txt => value(txt))
                 |        button:
                 |          items.bind: i =>
                 |            t"Add #$${i.size + 1}"
                 |
                 |  private def handleSubmit(e: Event): Unit =
                 |    e.preventDefault()
                 |    if text.value.isEmpty then return
                 |    items.setValue(_ :+ Item(new js.Date, text.value))
                 |    text.setValue("")
                 |
                 |  private def handleChange(e: Event): Unit =
                 |    text.setValue(
                 |      e.target.asInstanceOf[HTMLInputElement].value)
                 |
                 |
                 |class TodoList(items: Seq[Item]) extends Component:
                 |  def template = Frag:
                 |    import builders.*
                 |    ul:
                 |      for item <- items do
                 |        li{item.text.t}
                 |
                 |"""
          div:
            cls("code-out")
            div { cls("code-out-title"); p { t"RESULT" } }
            TodoApp().render
      div:
        cls("example-row")
        div:
          cls("example-descr")
          h2 { t"A Component using External Plugins" }
          p:
            t"""Scala.js makes it easy to interface with libraries and frameworks, with a design similar to Typescript type definitions.
               |
               |This example uses Remarkable, an external Markdown library, to convert the textarea's value in real time.
               |"""
        div:
          cls("code-in-out")
          pre:
            code:
              cls("scala")
              t"""${alignCodeBlock}
                 |import org.getshaka.shaka.*
                 |import org.scalajs.dom.*
                 |
                 |@js.native
                 |@JSGlobal("remarkable.Remarkable")
                 |class Remarkable extends js.Object:
                 |  def render(markdown: String): String = js.native
                 |
                 |class MarkdownEditor extends Component:
                 |  private val remarkable = Remarkable()
                 |  private val initialMd = "Hello, **world**!"
                 |  private val mdHtml = useState(
                 |    remarkable.render(initialMd))
                 |
                 |  def template = Frag:
                 |    import builders.*
                 |
                 |    div:
                 |      h3{t"Input"}
                 |      label:
                 |        `for`("markdown-content")
                 |        t"Enter some markdown"
                 |      br{}
                 |      textarea:
                 |        id("markdown-content")
                 |        oninput(updateMarkdown)
                 |        initialMd.t
                 |      h3{t"Output"}
                 |      mdHtml.bind: html =>
                 |        div:
                 |          maxWidth("100px")
                 |          dangerouslySetInnerHtml(html)
                 |
                 |  private def updateMarkdown(e: Event): Unit =
                 |    val input = e.target
                 |      .asInstanceOf[HTMLTextAreaElement].value
                 |    mdHtml.setValue(remarkable.render(input))
                 |
                 |"""
          div:
            cls("code-out")
            div { cls("code-out-title"); p { t"RESULT" } }
            MarkdownEditor().render
    highlightScala()
  end template
end Home
