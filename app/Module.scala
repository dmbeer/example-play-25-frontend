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

import javax.inject.Singleton

import com.google.inject.{AbstractModule, Provides}
import play.api.libs.concurrent.AkkaGuiceSupport
import play.api.{Configuration, Environment}
import uk.gov.hmrc.exampleplay25.audit.{AuditClient, AuditClientImpl, AuditClientSeam}
import uk.gov.hmrc.exampleplay25.config.AppVersion
import uk.gov.hmrc.play.audit.http.config.LoadAuditingConfig
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.audit.model.Audit

class Module(environment: Environment,
             configuration: Configuration) extends AbstractModule with AkkaGuiceSupport with AppVersion {
  def configure() {}

//  @Provides @Singleton
//  def provideLogger: SimpleLogger = new LoggerFacade(play.api.Logger.logger)

  @Provides @Singleton
  def provideAuditClient: AuditClient = {
    val appName = configuration.getString("appName").getOrElse("APP NAME NOT SET")

    val auditor = Audit(appName, new AuditConnector {
//      private val key = s"${environment.mode}.auditing"
      private val key = "auditing"
      override lazy val auditingConfig = LoadAuditingConfig(key)
    })

    val auditClientSeam = new AuditClientSeam(auditor)
    new AuditClientImpl(auditClientSeam, appName, appVersion)
  }

}
