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

import uk.gov.hmrc.exampleplay25.config.JacksonPropertyMapper


case class AuditItem(data: Map[String, String])


object AuditItem {
  def fromProduct(prefix: String, p: Product): AuditItem = new AuditItem(prefixAllKeys(prefix, JacksonPropertyMapper.productToKeyVals(p).toMap))

  def fromList(prefix: String, list: Seq[String]) = new AuditItem(list.zipWithIndex.map(kv => prefix + (kv._2 + 1).toString -> kv._1).toMap)

  def fromMap(prefix: String, m: Map[String, String]): AuditItem = new AuditItem(prefixAllKeys(prefix, m))

  def fromMap(prefix: String, m: Option[Map[String, String]]): AuditItem = new AuditItem(prefixAllKeys(prefix, m getOrElse Map()))

  def prefixAllKeys(prefix: String, m: Map[String, String]) =
    if (prefix.isEmpty) m
    else m.map(kv => prefix + kv._1 -> kv._2)
}
