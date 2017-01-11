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

package uk.gov.hmrc.exampleplay25.audit

import uk.gov.hmrc.play.audit.AuditExtensions
import uk.gov.hmrc.play.audit.model.{Audit, DataEvent, EventTypes}
import uk.gov.hmrc.play.http.HeaderCarrier

class AuditClientSeam(audit: Audit) {

  def sendDataEvent(tags: Map[String, String], detail: Map[String, String], appName: String) {
    val event = DataEvent(auditSource = appName, auditType = EventTypes.Succeeded, detail = detail, tags = tags)
    audit.sendDataEvent(event)
  }
}


class AuditClientImpl(audit: AuditClientSeam, appName: String, appVersion: String) extends AuditClient {

  def succeeded(tags: Map[String, String], headerCarrier: HeaderCarrier, userAgent: Option[String], items: AuditItem*): Unit = {
    val detail = items.toSeq.flatMap(_.data).toMap ++ userAgent.map(ua => "callingClient" -> ua)
    val augmented = detail + ("appVersion" -> appVersion)
    val mergedTags = tags ++ AuditExtensions.auditHeaderCarrier(headerCarrier).toAuditTags("", "")
    audit.sendDataEvent(mergedTags, augmented, appName)
  }
}


trait AuditClient {

  def succeeded(tags: Map[String, String], headerCarrier: HeaderCarrier, userAgent: Option[String], items: AuditItem*): Unit
}
