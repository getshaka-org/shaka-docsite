package org.getshaka.shaka.docsite

import org.getshaka.shaka.{Binding, Component, ComponentBuilder, Element}
import org.getshaka.shaka
import org.getshaka.nativeconverter.NativeConverter
import org.getshaka.shaka.router.Routable

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import scala.util.matching.Regex

class Home extends Component with Routable:
  
  override val path: Regex = "/".r
  
  override val template: ComponentBuilder =
    import shaka.builders.*

    div{id("title-box")
      h1{t"Shaka"}
      h2{t"A Scala.js library for building user interfaces"}
      div{id("title-box-links"); cls("row")
        a{cls("col"); href("get-started")
          div{id("title-get-started"); h3{t"Get Started"}}
        }
        a{cls("col"); href("tutorial")
          div{id("title-take-tutorial"); h3{t"Take the Tutorial"}}
        }
      }
    }
    div{id("main-body")
      div{id("three-features"); cls("row-main")
        div{cls("col-main")
          h3{t"Precise Binding"}
          p{t"Shaka makes it easy to build interactive, declarative UIs."}
          p{
            """
              |It starts with precise and efficient data binding. No need for VDOM, confusing macros, or AOT compilation.
              |Bindings are automatically managed to prevent leaks.
              |""".stripMargin.t
          }
        }
        div{cls("col-main")
          h3{t"Functional Components"}
          p{t"Components are encapsulated and composable."}
          p{t"State management is simplified, with the most useful benefits of Redux built in."}
          p{t"Use the same language on your JVM backend for massive code reuse."}
        }
        div{cls("col-main")
          h3{t"Strongly Typed"}
          p{t"Let ${a{href("https://github.com/getshaka-org/shaka"); target("_blank"); t"Scala 3's"}} rich type system work for you."}
          p{
            s"""
              |Using an object-oriented, functional, and even dynamic language makes it easy to model and import
              |JavaScript & Typescript libraries. Incremental compilation is fast and editor support outstanding.
              |""".stripMargin.t
          }
        }
      }
      div{cls("example-row")
        div{cls("example-descr")
          h2{t"A Simple Component"}
          p{
            """A Shaka Component implements a template used to construct the component.
              |
              |Package shaka.builders has methods useful for the construction of DOM. div builds a div Element, and 't' interpolates TextNodes.
              |""".stripMargin.t
          }
        }
        div{cls("code-in-out")
          pre{
            code{cls("scala")
              """
                |import org.getshaka.shaka
                |import org.getshaka.shaka.{Component, ComponentBuilder}
                |
                |class HelloMessage(user: String) extends Component:
                |
                |  override val template: ComponentBuilder =
                |    import shaka.builders.*
                |    
                |    div {
                |      color("purple")
                |      t"Hello $user"
                |    }
                |
                |@main def launchApp: Unit =
                |  shaka.render(HelloMessage(user = "Nikki"))
                |
                |""".stripMargin.t
            }
          }
          div{cls("code-out")
            div{cls("code-out-title"); p(t"RESULT")}
            HelloMessage("Nikki").render
          }
        }
      }
      div{cls("example-row")
        div{cls("example-descr")
          h2{t"Generated Code"}
          p{t"""To the right is the (unminified) JavaScript for HelloMessage's template, generated with `sbt fullLinkJS`."""}
          p{t"By using Scala 3's ${a{href("https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html"); target("_blank"); t"Guaranteed Inlining"}}, highly efficient JavaScript is produced."}
        }
        div{cls("code-in-out")
          pre{
            code{cls("scala")
              """                                                       
                 |Lk.prototype.jm = function(a) {
                 |  var b = document.createElement("div");
                 |  b.style.color = "purple";
                 |  b.appendChild(document.createTextNode("Hello "));
                 |  var c = this.Wo;
                 |  null !== c && void 0 === c ||
                 |    b.appendChild(document.createTextNode(c));
                 |  b.appendChild(document.createTextNode(""));
                 |  a.appendChild(b);
                 |}
                 |
                 |
                 |
                 |
                 |
                 |
                 |""".stripMargin.t
            }
          }
          div{cls("code-out")
            div{cls("code-out-title"); p(t"RESULT")}
            HelloMessage("Nikki").render
          }
        }
      }
      div{cls("example-row")
        div{cls("example-descr")
          h2{t"A Stateful Component"}
          p{t"In addition to passing state through constructors, a component can maintain internal state with the useState 'hook'."}
          p{t"A WebComponent is a Component wrapped in a ${a{href("https://developer.mozilla.org/en-US/docs/Web/Web_Components"); target("_blank"); t"Custom Element"}}, allowing you to used scoped styles and lifecycle callbacks."}
          p{t"If Timer is removed from the DOM, the interval and binding are destroyed."}
        }
        div{cls("code-in-out")
          pre{
            code{cls("scala")
              """
                |import scala.scalajs.js
                |import org.getshaka.shaka
                |import org.getshaka.shaka.WebComponent                 
                |import org.getshaka.shaka.ComponentBuilder
                |
                |class Timer extends WebComponent:
                |  private val seconds = shaka.useState(0)
                |  private var interval: js.Dynamic = null
                |
                |  override def connectedCallback(): Unit =
                |    interval = js.Dynamic.global
                |      .setInterval(() => seconds.setValue(_ + 1), 1000)
                |
                |  override def disconnectedCallback(): Unit =
                |    js.Dynamic.global.clearInterval(interval)
                |
                |  override val template: ComponentBuilder =
                |    import shaka.builders.*
                |    seconds.bind(value => t"Seconds: $value")
                |
                |""".stripMargin.t
            }
          }
          div{cls("code-out")
            div{cls("code-out-title"); p{t"RESULT"}}
            Timer().render
          }
        }
      }
      div{cls("example-row")
        div{cls("example-descr")
          h2{t"An Application"}
          p{
            """Using constructors and State, we can put together a small Todo application. This example uses state hooks to keep track of the current list of todo items as well as the text entered by the user.
              |
              |One benefit of Shaka using code blocks is that you can do anything in them, like defining variables and using for loops. This is a major benefit versus JSX, which can only have simple expressions inside {}. As long as the template remains a pure function of component state, the possibilities are endless.
              |""".stripMargin.t
          }
        }
        div{cls("code-in-out")
          pre{
            code{cls("scala")
              """
                |import scala.collection.Seq
                |import scala.scalajs.js
                |import org.getshaka.shaka
                |import org.getshaka.shaka.useState
                |import org.getshaka.shaka.{Component, ComponentBuilder}
                |
                |case class Item(date: js.Date, text: String)
                |
                |class TodoApp extends Component:
                |  private val items = useState(IArray.empty[Item])
                |  private val text = useState("")
                |
                |  override val template: ComponentBuilder =
                |    import shaka.builders.*
                |    div{
                |      h3{color("royalblue"); t"TODO"}
                |      items.bind(TodoList(_).render)
                |      form{onsubmit(handleSubmit)
                |        label{`for`("new-todo")
                |          t"What needs to be done?"
                |        }
                |        br{}
                |        input{
                |          id("new-todo")
                |          width("100px")
                |          autocomplete("off")
                |          onchange(handleChange)
                |          text.bindProps(txt => value(txt))
                |        }
                |        button{items.bind(i =>
                |          t"Add #${i.size + 1}")}
                |      }
                |    }
                |
                |  private def handleSubmit(e: js.Dynamic): Unit =
                |    e.preventDefault()
                |    if text.value.isEmpty then return
                |    items.setValue(_ :+ Item(new js.Date, text.value))
                |    text.setValue("")
                |
                |  private def handleChange(e: js.Dynamic): Unit =
                |    text.setValue(e.target.value.asInstanceOf[String])
                |
                |class TodoList(items: Seq[Item]) extends Component:
                |  override val template: ComponentBuilder =
                |    import shaka.builders.*
                |    ul{
                |      for item <- items do
                |        li{item.text.t}
                |    }
                |""".stripMargin.t
            }
          }
          div{cls("code-out")
            div{cls("code-out-title"); p{t"RESULT"}}
            TodoApp().render
          }
        }
      }
      div{cls("example-row")
        div{cls("example-descr")
          h2{t"A Component using External Plugins"}
          p{
            """Scala.js makes it easy to interface with libraries and frameworks, with a design similar to Typescript type definitions.
              |
              |This example uses Remarkable, an external Markdown library, to convert the textarea's value in real time.
              |""".stripMargin.t
          }
        }
        div{cls("code-in-out")
          pre{
            code{cls("scala")
              """
                |import scala.scalajs.js
                |import scala.scalajs.js.annotation.JSGlobal
                |import org.getshaka.shaka
                |import org.getshaka.shaka.useState
                |import org.getshaka.shaka.{Component, ComponentBuilder}
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
                |  override val template: ComponentBuilder =
                |    import shaka.builders.*
                |
                |    div{
                |      h3{t"Input"}
                |      label{`for`("markdown-content")
                |        t"Enter some markdown"
                |      }
                |      br{}
                |      textarea{
                |        id("markdown-content")
                |        oninput(updateMarkdown)
                |        initialMd.t
                |      }
                |      h3{t"Output"}
                |      mdHtml.bind(html =>
                |        div{maxWidth("100px")
                |         dangerouslySetInnerHtml(html)
                |        }
                |      )
                |    }
                |
                |  def updateMarkdown(e: js.Dynamic): Unit =
                |    val input = e.target.value.asInstanceOf[String]
                |    mdHtml.value = remarkable.render(input) 
                |""".stripMargin.t
            }
          }
          div{cls("code-out")
            div{cls("code-out-title"); p{t"RESULT"}}
            MarkdownEditor().render
          }
        }
      }
    }
    highlightScala()
  end template
