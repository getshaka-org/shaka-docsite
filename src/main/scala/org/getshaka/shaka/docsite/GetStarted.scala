package org.getshaka.shaka.docsite

import org.getshaka.shaka
import org.getshaka.shaka.{Binding, Component, ComponentBuilder, Element}
import org.getshaka.shaka.router.Routable

import scala.util.matching.Regex

class GetStarted extends Component with Routable:
  
  override val path: Regex = "/get-started".r
  
  override val template: ComponentBuilder =
    import shaka.builders._
    div{cls("doc-body")
      div{
        h1{cls("doc-title"); t"Get Started"}
        p{t"${b{t"Shaka"}} is a ${a{href("https://www.scala-js.org/"); target("_blank"); t"Scala.js"}} library for building user interfaces."}
        p{t"The fastest way to get Shaka is by ${a{href("https://www.scala-lang.org/download/"); target("_blank"); t"installing Scala with SBT"}} and generating a starter project:"}
        pre{
          code{cls("scala doc-code")
            """sbt new augustnagro/shaka.g8
              |""".stripMargin.t
          }
        }
        p{t"It's recommended to start with the ${a{href("tutorial"); t"Tutorial"}}. Otherwise, check out the ${a{href("doct"); "Documentation".t}}. Finally, you're welcome to file any question, bug, or feature request on the ${a{href("https://github.com/getshaka-org/shaka"); target("_blank"); t"Github project"}}."}
        br{}
        br{}
        img{src("dist/img/shaka-windsurf.jpg"); width("100%"); alt("shaka windsurfer")}
        p{t"Windsurfer Amado Vrieswijk performing the 'Shaka'. Photo credit: continentseven.com"}
      }
    }
  
    highlightScala()
  
  end template
    