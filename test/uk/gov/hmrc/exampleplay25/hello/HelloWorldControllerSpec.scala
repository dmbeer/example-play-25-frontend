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

import play.api.http.Status
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import uk.gov.hmrc.exampleplay25.audit.AuditClient


class HelloWorldControllerSpec extends UnitSpec with WithFakeApplication with MockitoSugar {

  val fakeRequest = FakeRequest("GET", "/")

  "GET /" should {
    "return 200" in {
      val auditClient = mock[AuditClient]
      val result = new HelloWorld(auditClient).hello("Joe", None)(fakeRequest)
      status(result) shouldBe Status.OK
    }

    "return HTML" in {
      val auditClient = mock[AuditClient]
      val result = new HelloWorld(auditClient).hello("Joe", None)(fakeRequest)
      contentType(result) shouldBe Some("text/html")
      charset(result) shouldBe Some("utf-8")
    }


  }


}
