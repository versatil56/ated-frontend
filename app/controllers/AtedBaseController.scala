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

package controllers

import config.FrontendAuthConnector
import models.AtedContext
import play.api.mvc.{Request, AnyContent}
import uk.gov.hmrc.play.frontend.auth.{AuthContext, Actions}
import uk.gov.hmrc.play.frontend.auth.connectors.AuthConnector
import uk.gov.hmrc.play.frontend.controller.FrontendController

trait AtedBaseController extends FrontendController with Actions {

  val authConnector: AuthConnector = FrontendAuthConnector

  implicit def atedContext2Request(implicit atedContext: AtedContext): Request[AnyContent] = atedContext.request
  // $COVERAGE-OFF$
  implicit def atedContext2AuthContext(implicit atedContext: AtedContext): AuthContext = atedContext.user.authContext
  // $COVERAGE-ON$

}
