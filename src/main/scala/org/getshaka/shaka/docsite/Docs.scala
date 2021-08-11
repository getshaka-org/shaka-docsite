package org.getshaka.shaka.docsite

import org.getshaka.shaka
import org.getshaka.shaka.{Component, ComponentBuilder}
import org.getshaka.shaka.router.Routable

import scala.scalajs.js

import scala.util.matching.Regex

class Docs extends Component with Routable:
  
  override val path: Regex = "/docs".r
  
  override val template: ComponentBuilder =
    import shaka.builders._
    div{cls("doc-body")
      div{
        h1{cls("doc-title"); t"Hello World"}
        h3{t"The smallest Shaka example looks something like this:"}
        pre{
          code{cls("scala doc-code")
            """val myApp: ComponentBuilder = h1{t"Hello, world!"}
              |shaka.renderBuilder(myApp)
              |""".stripMargin.t
          }
        }
        p{t"""It renders ${code(t"<h1>Hello, world!</h1>")} to document.body. An optional second parameter is the Element Shaka should render to:"""}
        pre{
          code{cls("scala doc-code")
            """val mainElement = js.Dynamic.global.document.getElementById("main")
              |shaka.renderBuilder(h1{t"Hello, world!"}, mainElement.asInstanceOf[Element])
              |""".stripMargin.t
          }
        }
        p{t"shaka.Element is a light facade of HTMLElement, with a few methods & properties needed to manipulate DOM. You can cast any type that represents HTMLElement to shaka.Element."}
        h2WithAnchor("introducing-component", "Introducing Components")
        p{t"Most of the time Shaka users will make classes implementing the ${a{href("https://javadoc.io/doc/org.getshaka/shaka_sjs1_3/latest/api/org/getshaka/shaka/Component.html"); target("_blank"); t"Component"}} trait's template method."}
        p{t"Because our templates should not cause side effects, we can override the template def as a val, such as in the example below. This can increase performance by not having to reconstruct the template on every render."}
        pre{
          code{cls("scala doc-code")
            """class HelloMessage(user: String) extends Component:
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
              |""".stripMargin.t
          }
        }
        p{t"shaka.render is just like shaka.renderBuilder, but accepts Components."}
        h3WithAnchor("composing-components", "Composing Components")
        p{t"Use Component::render to compose Components:"}
        pre{
          code{cls("scala doc-code")
            """val hello = HelloMessage("Ray")
              |val myApp: ComponentBuilder = div{hello.render; hello.render}
              |shaka.renderBuilder(myApp)
              |""".stripMargin.t
          }
        }
        p{t"${a{"For now".t; target("_blank"); href("https://github.com/getshaka-org/shaka/issues/4")}}, It is ${b(t"very")} important to declare myApp as a ComponentBuilder type, otherwise div(..) will be evaluated eagerly."}
        h2WithAnchor("web-component", "WebComponent")
        p{t"""A ${a{href("https://javadoc.io/doc/org.getshaka/shaka_sjs1_3/latest/api/org/getshaka/shaka/WebComponent.html"); target("_blank"); t"WebComponent"}} is a Component wrapped in a ${a{href("https://developer.mozilla.org/en-US/docs/Web/Web_Component"); target("_blank"); t"Custom Element"}}, the ultimate means of browser encapsulation."""}
        h2WithAnchor("builders", "Builders")
        p{t"The ${a{href("https://javadoc.io/doc/org.getshaka/shaka_sjs1_3/latest/api/org/getshaka/shaka/builders.html"); target("_blank"); t"shaka.builders"}} package has helpers useful for the construction of DOM. Importing shaka.builders.* brings in many identifiers, so it's best to scope the import within the method using them. There are three types of helpers available; Element builders, CSS props, and JavaScript props."}
        h3WithAnchor("element-builders", "Element Builders")
        pre{
          code{cls("scala doc-code")
            """val template: ComponentBuilder =
              |  import shaka.builders.*
              |
              |  h1{color("blue"); border("solid"); t"hello"}
              |  
              |  h1{color("blue"); border("solid")
              |    t"hello"
              |  }
              |  
              |  h1{
              |    color("blue")
              |    border("solid")
              |    t"hello"
              |  }
              |""".stripMargin.t
          }
        }
        p{t"All three uses of h1 above make the same DOM. The second option is closest to HTML, but use whatever you're most comfortable with!"}
        p{t"To use an empty element like <br>, you need empty brackets:"}
        pre{
          code{cls("scala doc-code")
            """val aLineBreak: ComponentBuilder =
              |  import shaka.builders.*
              |  
              |  br{}
              |""".stripMargin.t
          }
        }
        h3WithAnchor("custom-tags", "Custom Tags")
        p{t"To define your own tags, use shaka.builders.tag:"}
        pre{
          code{cls("scala doc-code")
            """override def template: ComponentBuilder =
              |  import shaka.builders.t
              |  tag("p"){t"my custom tag"}
              |""".stripMargin.t
          }
        }
        hr{}
        CustomTag().render
        hr{}
        p{t"You can also define a method. It is recommended to use inline for the best performance:"}
        pre{
          code{cls("scala doc-code")
            """override def template: ComponentBuilder =
              |  import shaka.builders.{ElementBuilder, tag, t}
              |  inline def p(init: ElementBuilder)(using Element): Unit = tag("p")(init)
              |  p{t"my custom tag"}
              |""".stripMargin.t
          }
        }
        h3WithAnchor("custom-css-props", "Custom Css Properties")
        p{t"As we've already seen, shaka.builders provides helpers for defining CSS properties. Only String arguments are supported. There's so many CSS selectors, only about 100 of the most important ones are defined. If you think a good one is missing, please make an issue. To define a custom Css property, use shaka.builders.cssProp:"}
        pre{
          code{cls("scala doc-code")
            """override def template: ComponentBuilder =
              |  import shaka.builders.*
              |  p{cssProp("border")("solid")
              |    t"Has a solid border"
              |  }
              |""".stripMargin.t
          }
        }
        hr{}
        CustomCssProp().render
        hr{}
        p{t"Likewise, you can define a method. Use inline for the best performance."}
        pre{
          code{cls("scala doc-code")
            """override def template: ComponentBuilder =
              |  import shaka.builders.{p, t, cssProp}
              |  inline def border(style: String)(using Element): Unit = cssProp("border")(style)
              |  p{border("solid")
              |    t"Has a solid border"
              |  }
              |""".stripMargin.t
          }
        }
        h3WithAnchor("javascript-props", "JavaScript Props")
        p{t"""While CssProps like className only accept Strings, JavaScript props take any type with a given ${a{href("https://github.com/getshaka-org/native-converter"); target("_blank"); t"NativeConverter"}} typeclass available. Functions and many other types have NativeConverters already predefined. You can even add `derives NativeConverter` to a case class or enum for automatic generation in the companion object."""}
        p{t"To define a custom Javascript property, use shaka.builders.prop."}
        pre{
          code{cls("scala doc-code")
            """override def template: ComponentBuilder =
              |  import shaka.builders.*
              |  p{prop("onclick")(() => js.Dynamic.global.alert("clicked!"))
              |    t"click me"
              |  }
              |""".stripMargin.t
          }
        }
        hr{}
        CustomJsProp().render
        hr{}
        p{t"Alternatively, you can define a method. It is recommended to use inline for best performance."}
        pre{
          code{cls("scala doc-code")
            """override def template: ComponentBuilder =
              |  import shaka.builders.{p, t, prop}
              |  inline def onclick(fn: () => Unit)(using Element): Unit = prop("onclick")(fn)
              |  p{onclick(() => js.Dynamic.global.alert("clicked!"))
              |    t"click me"
              |  }
              |""".stripMargin.t
          }
        }
        h3WithAnchor("t", "t - TextNode Interpolater")
        p{t"t is a String interpolator very helpful for rich text. For example:"}
        pre{
          code{cls("scala doc-cod")
            "p(t\"Hello, ${b(t\"bold\")} world!\")".t
          }
        }
        p{t"t is also defined as an extension method on String."}
        h2WithAnchor("state-and-bindings", "State and Bindings")
        p{t"${a{href("https://javadoc.io/doc/org.getshaka/shaka_sjs1_3/latest/api/org/getshaka/shaka/State.html"); target("_blank"); "State".t}} and ${a{href("https://javadoc.io/doc/org.getshaka/shaka_sjs1_3/latest/api/org/getshaka/shaka/Binding.html"); target("_blank"); "Bindings".t}} are managed in a hierarchical DAG. To see State and Bindings used in a real app, checkout the ${a{href("tutorial"); t"Tutorial"}}."}
        p{t"When a State's setValue setter is called, all dependent bind() expressions are recomputed. You can bind multiple Elements without needing something like React.Fragment. And you can nest bindings without worrying about memory leaks."}
        h3WithAnchor("useState", "shaka.useState")
        p{t"useState provides an api similar in spirit to React's useState hook."}
        pre{
          code{cls("scala doc-code")
            """class ClickHole extends Component:
              |  private val numClicks = useState(0)
              |  
              |  override val template: ComponentBuilder =
              |    import shaka.builders.*
              |
              |    button{onclick(() => numClicks.value += 1)
              |      t"click me"
              |    }
              |    p{t"numClicks: ${numClicks.bind(_.toString.t)}"}
              |""".stripMargin.t
          }
        }
        hr{}
        ClickHole().render
        hr{}
        p{t"useState returns an instance of ${a{"OpenState".t; target("_blank"); href("https://javadoc.io/doc/org.getshaka/shaka_sjs1_3/latest/api/org/getshaka/shaka/OpenState.html")}}, a subclass of State. Because OpenState's value getter and setter are public, it must always be encapsulated."}
        h3WithAnchor("storage-manager", "Storage Managers")
        p{t"A cool property about State is that you can configure its persistence, whether with ${a{"LocalStorage".t; target("_blank"); href("https://javadoc.io/doc/org.getshaka/shaka_sjs1_3/latest/api/org/getshaka/shaka/LocalStorage.html")}}, ${a{"SessionStorage".t; target("_blank"); href("https://javadoc.io/doc/org.getshaka/shaka_sjs1_3/latest/api/org/getshaka/shaka/SessionStorage.html")}}, or any provider implmenting trait ${"StorageManager".t; target("_blank"); href("https://javadoc.io/doc/org.getshaka/shaka_sjs1_3/latest/api/org/getshaka/shaka/StorageManager.html")}. By default, a no-op StorageManager is used."}
        pre{
          code{cls("scala doc-code")
            """object ClickState extends State(0, LocalStorage("click-state"))
              |""".stripMargin.t
          }
        }
        p{t"LocalStorage and SessionStorage uses a given NativeConverter typeclass to serialize the state to a JSON String and back."}
      }
    }

    highlightScala()

  def h2WithAnchor(id: String, text: String): ComponentBuilder =
    import shaka.builders.{id as htmlId, *}
    h2{htmlId(id)
      a{href("docs#" + id); """🔗 """.t}
      text.t
    }

  def h3WithAnchor(id: String, text: String): ComponentBuilder =
    import shaka.builders.{id as htmlId, *}
    h3{htmlId(id)
      a{href("docs#" + id); """🔗 """.t}
      text.t
    }

