package org.getshaka.shaka.docsite

import org.getshaka.shaka
import org.getshaka.shaka.{Binding, Component, ComponentBuilder, Element, WebComponent}
import org.getshaka.shaka.router.Router

import scala.collection.mutable.ArrayBuffer
import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

class Main extends Component:

  override val template: ComponentBuilder =
    import shaka.builders._

    val doc = js.Dynamic.global.document

    def updateFlashlight(e: js.Dynamic): Unit =
      val event = e.asInstanceOf[js.Dynamic]
      val x = event.clientX.asInstanceOf[Double]
      val y = event.clientY.asInstanceOf[Double]
      doc.body.style.setProperty("--cursorX", s"${x}px")
      doc.body.style.setProperty("--cursorY", s"${y}px")

    doc.addEventListener("mousemove", updateFlashlight)

    var darkMode = false
    def toggleDarkMode(e: js.Dynamic): Unit =
      darkMode = !darkMode
      if darkMode then doc.body.classList.add("flashlight")
      else doc.body.classList.remove("flashlight")

    var showMobileMenu = false
    def toggleMobileMenu(e: js.Dynamic): Unit =
      showMobileMenu = !showMobileMenu
      if showMobileMenu then doc.querySelector("#mobile-menu").style.display = "flex"
      else doc.querySelector("#mobile-menu").style.display = "none"

    val homePage = Home()
    val router = Router()
      .route(GetStarted())
      .route(Tutorial())
      .route(Docs())
      .route(homePage)
      .catchAll(homePage)

    header{
      div{id("mobile-menu")
        a{cls("header-link"); href("get-started"); h3(t"Get Started"); onclick(toggleMobileMenu)}
        a{cls("header-link"); href("tutorial"); h3(t"Tutorial"); onclick(toggleMobileMenu)}
        a{cls("header-link"); href("docs"); h3(t"Docs"); onclick(toggleMobileMenu)}
        a{cls("header-link"); href("https://github.com/getshaka-org/shaka"); target("_blank"); h3{t"Github"}}
        span{id("dark-mode-menu-a"); cls("header-link"); onclick(toggleDarkMode); h3{t"Dark Mode"}}
      }
      div{id("nav-bar")
        a{cls("row header-link"); href("/")
          img{id("header-icon"); cls("col"); src("dist/img/shaka-icon.png"); alt("shaka-icon")}
          h2{cls("col"); t"Shaka"}
        }
        div{id("header-pages")
          a{cls("header-link"); href("get-started"); h3{t"Get Started"}}
          a{cls("header-link"); href("tutorial"); h3{t"Tutorial"}}
          a{cls("header-link"); href("docs"); h3{t"Docs"}}
          a{cls("header-link"); href("https://github.com/getshaka-org/shaka"); target("_blank"); h3{t"Github"}}
        }
        span{id("header-dark-mode"); cls("header-link"); onclick(toggleDarkMode); h3{t"Dark Mode"}}
        a{id("menu-toggle"); onclick(toggleMobileMenu)
          img{src("dist/img/menu-icon.png"); alt("menu")}
        }
      }
    }

    main{
      router.render
    }
    footer{
      div{id("footer-box-links")
        a{href("get-started")
          div{id("footer-get-started"); h3{t"Get Started"}}
        }
        a{href("tutorial")
          div{id("footer-take-tutorial"); h3{t"Take the Tutorial"}}
        }
      }
    }
  end template

@main def launchApp: Unit = shaka.render(Main())

