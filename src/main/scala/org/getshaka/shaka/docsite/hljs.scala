package org.getshaka.shaka.docsite

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import org.getshaka.shaka.Element
import org.getshaka.shaka.builders.ElementBuilder

import scala.concurrent.Future

@js.native
@JSGlobal("hljs")
object hljs extends js.Object:
  def highlightBlock(block: js.Any): Unit = js.native

def highlightScala()(using parentElement: Element): Unit = 
  parentElement.asInstanceOf[js.Dynamic].querySelectorAll("pre code")
    .forEach(block => hljs.highlightBlock(block))