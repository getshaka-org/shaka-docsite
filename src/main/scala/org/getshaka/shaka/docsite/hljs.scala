package org.getshaka.shaka.docsite

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import org.scalajs.dom.Element

import scala.concurrent.Future

@js.native
@JSGlobal("hljs")
object hljs extends js.Object:
  def highlightBlock(block: js.Any): Unit = js.native

def highlightScala()(using parentElement: Element): Unit =
  for block <- parentElement.querySelectorAll("pre code") do
    hljs.highlightBlock(block)