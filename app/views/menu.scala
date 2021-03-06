package views.html

import controllers.routes.{Application => route}
import play.api.mvc.{Call, RequestHeader}
import play.twirl.api.Html

object menu {
  protected[html] def listItem(call: Call, linkText: String)(implicit request: RequestHeader): String = {
    val uri = call.toString()
    if (uri==request.uri) s"""<li class="active"><a href="#">$linkText</a></li>""" else s"""<li><a href="$uri">$linkText</a></li>"""
  }

  def apply(implicit request: RequestHeader) =
    Html(s"""<nav class="navbar navbar-default navbar-inverse navbar-static-top" role="navigation">
            |  <ul class="nav navbar-nav">
            |    ${listItem(route.hello("Everybody"), "Welcome")}
            |    ${listItem(route.help, "Help")}
            |  </ul>
            |  <ul class="nav navbar-nav">
            |  </ul>
            |</nav>""".stripMargin)
}
