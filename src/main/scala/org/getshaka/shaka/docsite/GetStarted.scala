package org.getshaka.shaka.docsite

import org.getshaka.shaka.*

import scala.util.matching.Regex

class GetStarted extends Component:
  
  def template = Frag:
    import builders.*
    div{cls("doc-body")
      div:
        h1{cls("doc-title"); t"Get Started"}
        p{t"${b{t"Shaka"}} is a ${a{href("https://www.scala-js.org/"); target("_blank"); t"Scala.js"}} library for building user interfaces."}
        p{t"The fastest way to get Shaka is by ${a{href("https://www.scala-lang.org/download/"); target("_blank"); t"installing Scala with SBT"}} and generating a starter project:"}
        pre:
          code{cls("scala doc-code")
            t"""sbt new getshaka-org/shaka.g8
               |"""
          }
        p{t"It's recommended to start with the ${a{href("tutorial"); t"Tutorial"}}. Otherwise, check out the ${a{href("doct"); "Documentation".t}}. Finally, you're welcome to file any question, bug, or feature request on the ${a{href("https://github.com/getshaka-org/shaka"); target("_blank"); t"Github project"}}."}
        br{}
        br{}
        img{src("img/shaka-windsurf.jpg"); width("100%"); alt("shaka windsurfer")}
        p{t"Windsurfer Amado Vrieswijk sending the 'Shaka'. Photo credit: continentseven.com"}
    }
  
    highlightScala()
  
  end template
    