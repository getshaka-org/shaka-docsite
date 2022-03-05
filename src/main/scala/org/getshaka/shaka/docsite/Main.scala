package org.getshaka.shaka.docsite

import org.getshaka.shaka.*
import org.getshaka.shaka.router.Router

import scala.collection.mutable.ArrayBuffer
import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import org.scalajs.dom.{Event, HTMLElement, MouseEvent, document}

class Main extends Component:

  def template = Frag {
    import builders.*

    def updateFlashlight(e: MouseEvent): Unit =
      val x = e.clientX
      val y = e.clientY
      document.body.style.setProperty("--cursorX", s"${x}px")
      document.body.style.setProperty("--cursorY", s"${y}px")

    document.addEventListener("mousemove", e => updateFlashlight(e))

    var darkMode = false
    def toggleDarkMode(e: Event): Unit =
      darkMode = !darkMode
      if darkMode then document.body.classList.add("flashlight")
      else document.body.classList.remove("flashlight")

    var showMobileMenu = false
    def toggleMobileMenu(e: Event): Unit =
      showMobileMenu = !showMobileMenu
      if showMobileMenu then
        document.querySelector("#mobile-menu").asInstanceOf[HTMLElement].style.display = "flex"
      else
        document.querySelector("#mobile-menu").asInstanceOf[HTMLElement].style.display = "none"

    Router
      .route("/".r, _ => Home())
      .route("/get-started".r, _ => GetStarted())
      .route("/tutorial".r, _ => Tutorial())
      .route("/docs".r, _ => Docs())
      .catchAll(_ => Home())

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
          img{id("header-icon"); cls("col"); src("img/shaka-icon.png"); alt("shaka-icon")}
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
          img{src("img/menu-icon.png"); alt("menu")}
        }
      }
    }

    main{
      Router.render
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
  }
  end template

@main def launchApp(): Unit = render(Main(), document.body)

