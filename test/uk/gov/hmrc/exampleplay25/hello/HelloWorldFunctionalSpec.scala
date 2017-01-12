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

import org.jsoup.Jsoup
import org.scalatestplus.play.{HtmlUnitFactory, OneBrowserPerSuite, OneServerPerSuite, PlaySpec}
import play.api.libs.ws.WS
import play.api.test.Helpers.{contentAsString, _}

class HelloWorldFunctionalSpec extends PlaySpec with OneServerPerSuite with OneBrowserPerSuite with HtmlUnitFactory {

  lazy val base = s"http://localhost:$port/example-frontend"

  "hello world" should {

    "display html page" in {
      val response = await(WS.url(s"$base/hello/world")(app).withMethod("GET").withHeaders("Accept" -> "text/html").execute())
      val doc = Jsoup.parse(response.body)
      doc.select("body.hello-form").size mustBe 1
    }

    "display message" in {
      val response = await(WS.url(s"$base/hello/world")(app).withMethod("GET").withHeaders("Accept" -> "application/json").execute())
      response.status must be(200)
//      response.body must contain("\"Hello world\"")
    }

  }

}
