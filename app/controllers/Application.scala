package controllers

import play.api._
import play.api.libs.Files.TemporaryFile
import play.api.libs.json.{JsNull, JsValue}
import play.api.mvc._
import play.api.http.MimeTypes
import views.{ html => v }

import scala.xml.NodeSeq

object Application extends Controller {
  val Logger = org.slf4j.LoggerFactory.getLogger("application")

  def welcome = Action { implicit request =>
    Ok(v.welcome("Default Welcome Page"))
  }

  def int(i: Int) = Action { implicit request =>
    Ok(v.show("One Int parameter", s"Value of Int: $i."))
  }

  def long(i: Long) = Action { implicit request =>
    Ok(v.show("One Long parameter", s"Value of Long: $i."))
  }

  def string1(s1: String) = Action { implicit request =>
    Ok(v.show("One String parameter", s"Value of String: $s1."))
  }

  def string2(s1: String, s2: String) = Action { implicit request =>
    Ok(v.show("Two String parameters", s"Value of Strings: $s1, $s2."))
  }

  def qs(size: Int, name: String) = Action  { implicit request =>
    Ok(v.show("Two queryString parameters", s"<b>size</b>: $size; <b>name</b>: $name."))
  }

  def qs2(b: Boolean, l: Long, size: Int, name: String) = Action { implicit request =>
    Ok(v.show("2 URL params, 2 qs params", s"<b>b</b>: $b; <b>l</b>: $l; <b>size</b>: $size; <b>name</b>: $name."))
  }

  def dimensions(width: Int, height: Int) = Action { implicit request =>
    Ok(v.show("Dimensions from regex", s"<b>width</b>: $width; <b>height</b>: $height."))
  }

  def uuid(uid: java.util.UUID) = Action { implicit request =>
    Ok(v.show("One UUID parameter", s"Value of UUID: $uid."))
  }

  def bodyParsers = Action { implicit request =>
    val contentType = request.contentType match {
      case Some(ct) =>
        s"<p>The request had <code>Content-Type $ct</code></p>"
      case _ =>
        "<p>The request did not have a <code>Content-Type</code> header.</p>"
    }

    def ok(body: Any) = Ok(v.bodyParsers(s"$contentType <p>Got: <code>$body</code></p>"))

    Logger.debug(s"contentType=$contentType")
    (request.contentType match {
      case Some(MimeTypes.TEXT) =>
        request.body.asText.map { ok }

      case Some(MimeTypes.HTML) =>
        request.body.asText.map { ok }

      case Some(MimeTypes.JSON) =>
        request.body.asJson.map { ok }

      case Some(MimeTypes.XML) =>
        request.body.asXml.map { ok }

      case Some(MimeTypes.CSS) =>
        request.body.asText.map { ok }

      case Some(MimeTypes.JAVASCRIPT) =>
        request.body.asText.map { ok }

      case Some(MimeTypes.EVENT_STREAM) =>
        request.body.asRaw.map { ok }

      case Some(MimeTypes.BINARY) =>
        request.body.asRaw.map { ok }

      case Some(MimeTypes.FORM) =>
        request.body.asFormUrlEncoded.map { form =>
          val keys = form.keySet
          val result = keys.map( k => s"$k -> ${keys(k)}").mkString("\n")
          Ok(v.bodyParsers(s"$contentType <p>Got: <p/>\n<pre>$result</pre>"))
        }

      case Some(MimeTypes.HTML) => // TODO figure out what to do with this duplicate case
        request.body.asMultipartFormData.map { data =>
          val files = data.files.mkString("\n")
          val keys = data.asFormUrlEncoded.keySet
          val result = keys.map( k => s"$k -> ${keys(k)}").mkString("\n")
          Ok(v.bodyParsers(s"$contentType <p>Got files:\n$files\n\n... and got form fields:\n<code>$result</code></p>"))
        }

      case _ =>
        None
    }).getOrElse {
      BadRequest(v.bodyParsers(contentType))
    }
  }

  def includer = Action { implicit request =>
    Ok(v.includer(request))
  }

  def programming = Action { implicit request =>
    Ok(v.programming(request))
  }

  def warningPage = Action { implicit request =>
    Ok(v.warningPage(request))
  }

  def flashing = Action { implicit request =>
    def russianRoulette: Long = 10 / (System.currentTimeMillis % 2)

    Redirect(routes.Application.includer()).flashing(try {
      val remainder = russianRoulette
      "success" -> s"No worries, mate, remainder = $remainder"
    } catch {
      case e: Exception => "error" -> s"Error: ${e.getMessage}"
    })
  }

  def parseRaw = Action(parse.raw) { implicit request =>
    val charArray: Array[Char] = for {
      byteArray <- request.body.asBytes().toArray
      byte      <- byteArray
    } yield byte.toChar
    val rawPayload = new String(charArray)
    Ok(s"rawPayload = $rawPayload")
  }

  def parseText = Action(parse.text) { implicit request =>
    val body: String = request.body
    Ok(body)
  }

  def parseTolerantText = Action(parse.tolerantText) { implicit request =>
    val body: String = request.body
    Ok(body)
  }

  def parseJson = Action(parse.json) { implicit request =>
    val body: JsValue = request.body
    Ok(body)
  }

  def parseTolerantJson = Action(parse.tolerantJson) { implicit request =>
    val body: JsValue = request.body
    Ok(body)
  }

  def parseXml = Action(parse.xml) { implicit request =>
    val nodes: NodeSeq = request.body
    Ok(nodes.toString())
  }

  def parseTolerantXml = Action(parse.tolerantXml) { implicit request =>
    val nodes: NodeSeq = request.body
    Ok(nodes.toString())
  }

  protected[controllers] def nameValues(form: Map[String, Seq[String]]): String =
    (for {
      name <- form.keySet
      value <- form(name)
    } yield s"$name -> $value").mkString("\n")

  def parseForm = Action(parse.urlFormEncoded) { implicit request =>
    Ok(nameValues(request.body))
  }

  def parseTolerantForm = Action(parse.tolerantFormUrlEncoded) { implicit request =>
    Ok(nameValues(request.body))
  }

  def parseFile = Action(parse.multipartFormData) { implicit request =>
    val body: MultipartFormData[TemporaryFile] = request.body
    val formVars = "Form variables: " + nameValues(body.dataParts)
    val result = (for {
      file <- body.files
      ref = file.ref
    } yield  s"Uploaded ${file.filename} to ${ref.file.getAbsolutePath} (${ref.file.length} bytes)").mkString("\n")
    Ok(result)
  }

  def multiParse = Action { implicit request =>

    /** Not tolerant; returns None if Content-type is not application/x-www-form-urlencoded */
    val maybeFormResult: Option[Result] = request.body.asFormUrlEncoded.map { form =>
      val result = (for { // you can traverse Iterables only once
        tuple <- form
      } yield {
        val name = tuple._1
        val values = tuple._2.mkString(", ")
        s"$name=$values"
      }).mkString("\n")
      if (request.headers("User-Agent").contains("curl"))
        Ok(result)
      else
        Ok(v.bodyParsers(s"You submitted these form fields: <pre>$result</pre>"))
    }.headOption

    /** Not tolerant; returns None if Content-type is not application/json */
    def maybeJson: Option[Result] = request.body.asJson.map { json =>
      val value1: JsValue = (json \ "param1").getOrElse(JsNull) // JsValue strings start and end with double quotes
      val value2: JsValue = (json \ "param2").getOrElse(JsNull)
      Ok(s"""{
            |  "param1": $value1,
            |  "param2": $value2,
            |  "array": [ 1, 2, 3 ],
            |  "boolean": true,
            |  "null": null,
            |  "number": 123,
            |  "object": {
            |    "a": "b",
            |    "c": "d",
            |    "e": "f"
            |  },
            |  "string": "Hello World"
            |}""".stripMargin)
    }

    maybeFormResult.getOrElse(maybeJson.getOrElse(BadRequest(v.bodyParsers("Expected JSON or form-urlencoded data"))))
  }
}
