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

import play.api.data.Form
import play.api.data.Forms._


object HelloForm {
  val helloForm = Form[HelloData] {
    mapping(
      "id" -> optional(text),
      "name" -> optional(text),
      "story" -> optional(text)
    )(HelloData.apply)(HelloData.unapply)
  }
}


case class HelloData(id: Option[String], name: Option[String] = None, story: Option[String] = None)
