package org.getshaka.shaka.docsite

import org.getshaka.shaka.*

import scala.scalajs.js
import scala.util.matching.Regex

class Docs extends Component:
  
  override val template = Frag {
    import builders.*
    div{cls("doc-body")
      div{
        h1{cls("doc-title"); t"Hello World"}
        h3{t"The smallest Shaka example looks something like this:"}
        pre{
          code{cls("scala doc-code")
            t"""import org.getshaka.shaka.*
               |import org.getshaka.shaka.builders.*
               |import org.scalajs.dom.*
               |
               |val myApp = Frag {
               |  h1{t"Hello, world!"}
               |}
               |
               |render(myApp, document.body)
               |"""
          }
        }
        p{t"""It renders ${code(t"<h1>Hello, world!</h1>")} to document.body."""}
        h2WithAnchor("introducing-component", "Introducing Components").render
        p{t"Most of the time Shaka users will make classes implementing the ${a{href("https://javadoc.io/doc/org.getshaka/shaka_sjs1_3/latest/api/org/getshaka/shaka/Component.html"); target("_blank"); t"Component"}} trait's template method."}
        pre{
          code{cls("scala doc-code")
            t"""class HelloMessage(user: String) extends Component:
               |  override val template = Frag {
               |    import builders.*
               |
               |    div{
               |      color("purple")
               |      p{t"Hello $$user"}
               |    }
               |  }
               |
               |
               |@main def launchApp: Unit =
               |  render(HelloMessage(user = "Nikki"), document.body)
               |"""
          }
        }
        h3WithAnchor("composing-components", "Composing Components").render
        p{t"Use Component::render and Frag::render to compose applications"}
        pre{
          code{cls("scala doc-code")
            t"""val hello = HelloMessage("Ray")
               |
               |val goodbye = Frag { t".. and goodbye." }
               |
               |val myApp: Frag = div{hello.render; goodbye.render}
               |
               |render(myApp, document.body)
               |"""
          }
        }
        h2WithAnchor("web-component", "WebComponent").render
        p{t"""A WebComponent is a Component wrapped in a ${a{href("https://developer.mozilla.org/en-US/docs/Web/Web_Component"); target("_blank"); t"Custom Element"}}, the ultimate means of browser encapsulation. The contract is as follows:"""}
        pre{
          code{cls("scala doc-code")
            t"""/**
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
               |   * Whether this WebComponent should use ShadowDom
               |   * and/or scoped styles. Defaults to Disabled.
               |   *
               |   * Shadow Dom means that means ids, class names, and selectors can be
               |   * used without fear of conflicts.
               |   * @see https://developer.mozilla.org/en-US/docs/Web/Web_Components/Using_shadow_DOM
               |   */
               |  def shadowDom: ShadowDom = ShadowDom.Disabled
               |
               |  def render(using parentElement: Element, parentBinding: Binding[?]): Unit = ???
               |"""
          }
        }
        h2WithAnchor("builders", "Builders").render
        p{t"The ${a{href("https://javadoc.io/doc/org.getshaka/shaka_sjs1_3/latest/api/org/getshaka/shaka/builders.html"); target("_blank"); t"shaka.builders"}} package has helpers useful for the construction of DOM. Importing shaka.builders.* brings in many identifiers, so it's best to scope the import within the method using them. There are three types of helpers available; Element builders, CSS props, and JavaScript props."}
        h3WithAnchor("element-builders", "Element Builders").render
        pre{
          code{cls("scala doc-code")
            t"""val template = Frag {
               |  import builders.*
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
               |}
               |"""
          }
        }
        p{t"All three uses of h1 above make the same DOM. The second option is closest to HTML, but use whatever you're most comfortable with!"}
        p{t"To use an empty element like <br>, you need empty brackets:"}
        pre{
          code{cls("scala doc-code")
            t"""val aLineBreak = Frag {
               |  import builders.*
               |
               |  br{}
               |}
               |"""
          }
        }
        h3WithAnchor("custom-tags", "Custom Tags").render
        p{t"To define your own tags, use shaka.builders.tag. It is recommended to use inline for best performance."}
        pre{
          code{cls("scala doc-code")
            t"""override def template = Frag {
               |  import shaka.builders.*
               |
               |  inline def FIXML(init: Element ?=> Unit)(using Element): Unit = tag("FIXML")(init)
               |
               |  FIXML { t"100 shares" }
               |}
               |"""
          }
        }
        hr{}
        CustomTag().render
        hr{}
        h3WithAnchor("custom-css-props", "Custom Css Properties").render
        p{t"shaka.builders.* also has helpers for defining CSS props. Only String arguments are supported. There's so many CSS selectors, only about 100 of the most important ones are defined. If you think a good one is missing, please make an issue. To define a custom Css property, use shaka.builders.cssProp:"}
        pre{
          code{cls("scala doc-code")
            t"""override def template = Frag {
               |  import builders.*
               |
               |  inline def radiola(style: String)(using Element): Unit = cssProp("border")(style)
               |
               |  p{radiola("solid")
               |    t"Has a solid border"
               |  }
               |}
               |"""
          }
        }
        hr{}
        CustomCssProp().render
        hr{}
        h3WithAnchor("javascript-props", "JavaScript Props").render
        p{t"To define a custom Javascript property, use shaka.builders.prop."}
        pre{
          code{cls("scala doc-code")
            t"""override def template = Frag {
               |  import shaka.builders.*
               |
               |  p{prop("onclick")(() => js.Dynamic.global.alert("clicked!"))
               |    t"click me"
               |  }
               |}
               |"""
          }
        }
        hr{}
        CustomJsProp().render
        hr{}
        h3WithAnchor("t", "t - TextNode Interpolater").render
        p{t"t is a String interpolator very helpful for rich text. For example:"}
        pre{
          code{cls("scala doc-cod")
            "p(t\"Hello, ${b(t\"bold\")} world!\")".t
          }
        }
        p{t"t is also defined as an extension method on String."}
        h2WithAnchor("state-and-bindings", "State and Bindings").render
        p{t"${a{href("https://javadoc.io/doc/org.getshaka/shaka_sjs1_3/latest/api/org/getshaka/shaka/State.html"); target("_blank"); "State".t}} and ${a{href("https://javadoc.io/doc/org.getshaka/shaka_sjs1_3/latest/api/org/getshaka/shaka/Binding.html"); target("_blank"); "Bindings".t}} are managed in a hierarchical DAG. To see State and Bindings used in a real app, checkout the ${a{href("tutorial"); t"Tutorial"}}."}
        p{t"When a State's setValue setter is called, all dependent bind() expressions are recomputed. You can bind multiple Elements without needing something like React.Fragment. And you can nest bindings without worrying about memory leaks."}
        h3WithAnchor("useState", "shaka.useState").render
        p{t"useState provides an api similar in spirit to React's useState hook."}
        pre{
          code{cls("scala doc-code")
            t"""class ClickHole extends Component:
               |  private val numClicks = useState(0)
               |
               |  override val template = Frag {
               |    import builders.*
               |
               |    button{onclick(() => numClicks.value += 1)
               |      t"click me"
               |    }
               |    p{t"numClicks: $${numClicks.bind(_.toString.t)}"}
               |  }
               |"""
          }
        }
        hr{}
        ClickHole().render
        hr{}
        p{t"useState returns an instance of OpenState, a subclass of State. Because OpenState's value getter and setter are public, it must always be encapsulated."}
        h3WithAnchor("storage-manager", "Storage Managers").render
        p{t"A cool property about State is that you can configure its persistence, whether with shaka.LocalStorage, shaka.SessionStorage, or any provider implementing trait shaka.StorageManager. By default, a no-op StorageManager is used."}
        pre{
          code{cls("scala doc-code")
            t"""object ClickState extends State(0, LocalStorage("click-state"))
               |"""
          }
        }
        p{t"shaka.LocalStorage and SessionStorage uses an implicit NativeConverter typeclass to serialize the state to a JSON String and back."}
      }
    }

    highlightScala()
  }

  def h2WithAnchor(id: String, text: String): Frag = Frag {
    import builders.{id as htmlId, *}
    h2{htmlId(id)
      a{href("docs#" + id); """🔗 """.t}
      text.t
    }
  }

  def h3WithAnchor(id: String, text: String): Frag = Frag {
    import builders.{id as htmlId, *}
    h3{htmlId(id)
      a{href("docs#" + id); """🔗 """.t}
      text.t
    }
  }

