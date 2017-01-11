/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.exampleplay25.hello

import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

import uk.gov.hmrc.play.frontend.controller.FrontendController
import play.api.mvc._

import scala.concurrent.Future
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.http.{HeaderNames, MimeTypes}
import play.api.i18n.Lang
import play.api.libs.json.Json
import HelloForm.helloForm
import uk.gov.hmrc.exampleplay25.audit.{AuditClient, AuditItem}
import uk.gov.hmrc.exampleplay25.views.html.helloworld._
import uk.gov.hmrc.play.http.HeaderCarrier



class HelloWorld @Inject()(auditClient: AuditClient) extends FrontendController {

  private val sequence = new AtomicInteger(0)
  private implicit val responseWriter = Json.writes[ResponseContent]
  private val cymraeg = Lang("cy")

  private val XTrackingID = "X-Tracking-ID"

  // This demonstrates some content negotiation patterns
  def hello(name: String, id: Option[String]): Action[AnyContent] = Action.async { implicit request =>
    val preferredLanguage = request.acceptLanguages.headOption
    val content = preferredLanguage match {
      case Some(lang) if lang.satisfies(cymraeg) =>
        ResponseContent("Croesawu " + name, sequence.incrementAndGet)
      case _ =>
        ResponseContent("Hello " + name, sequence.incrementAndGet)
    }

    writeAudit(Context(hc(request), request.headers), content, true)

    val response = if (request.accepts(MimeTypes.HTML)) {
      Ok(hello_world(content.message, helloForm))
    } else {
      Ok(Json.toJson(content))
    }

    Future.successful(response)
  }

  def postStory: Action[AnyContent] = Action { implicit request =>
    // server-side processing goes here
    val bound = helloForm.bindFromRequest()(request)
    SeeOther(routes.HelloWorld.hello(bound.get.name.getOrElse("world"), bound.get.id).url)
  }


  private def writeAudit(context: Context, response: ResponseContent, success: Boolean = true) {
    val trackingId = context.headers.get(XTrackingID)
    val userAgent = context.headers.get(HeaderNames.USER_AGENT)

    val tags: Map[String, String] =
      if (trackingId.isEmpty) Map.empty
      else Map(XTrackingID -> trackingId.get)

    val auditItemList = List(
      AuditItem.fromProduct("response.", response)
    )

    auditClient.succeeded(tags, context.headerCarrier, userAgent, auditItemList: _*)
  }
}

case class ResponseContent(message: String, count: Int)


case class Context(headerCarrier: HeaderCarrier, headers: Headers)
