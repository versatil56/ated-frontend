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

package controllers.editLiability

import config.FrontendDelegationConnector
import connectors.{BackLinkCacheConnector, DataCacheConnector}
import controllers.propertyDetails.{AddressLookupController, PropertyDetailsAddressController}
import controllers.{AtedBaseController, BackLinkController}
import controllers.auth.{AtedFrontendAuthHelpers, AtedRegime, ClientHelper}
import forms.AtedForms._
import uk.gov.hmrc.play.frontend.auth.DelegationAwareActions
import play.api.i18n.Messages.Implicits._
import play.api.Play.current

import scala.concurrent.Future

trait EditLiabilityTypeController extends BackLinkController
  with AtedFrontendAuthHelpers with DelegationAwareActions with ClientHelper {

  def editLiability(oldFormBundleNo: String, periodKey: Int, editAllowed: Boolean) = AuthAction(AtedRegime) {
    implicit atedContext =>
      ensureClientContext {
        Future.successful(
          Ok(views.html.editLiability.editLiability(editLiabilityReturnTypeForm, oldFormBundleNo, periodKey, editAllowed, returnToFormBundle(oldFormBundleNo, periodKey)))
        )
      }
  }

  def continue(oldFormBundleNo: String, periodKey: Int, editAllowed: Boolean) = AuthAction(AtedRegime) {
    implicit atedContext =>
      ensureClientContext {
        editLiabilityReturnTypeForm.bindFromRequest.fold(
          formWithErrors =>
            Future.successful(BadRequest(views.html.editLiability.editLiability(formWithErrors, oldFormBundleNo, periodKey, editAllowed, returnToFormBundle(oldFormBundleNo, periodKey)))),
          data => {
            val backLink = Some(controllers.editLiability.routes.EditLiabilityTypeController.editLiability(oldFormBundleNo, periodKey, editAllowed).url)
            data.editLiabilityType match {
              case Some("CR") =>
                RedirectWithBackLink(
                  PropertyDetailsAddressController.controllerId,
                  controllers.propertyDetails.routes.PropertyDetailsAddressController.editSubmittedReturn(oldFormBundleNo),
                  backLink,
                  List(AddressLookupController.controllerId)
                )
              case Some("DP") =>
                RedirectWithBackLink(
                  DisposePropertyController.controllerId,
                  controllers.editLiability.routes.DisposePropertyController.view(oldFormBundleNo),
                  backLink
                )
              case _ =>
                Future.successful(Redirect(controllers.editLiability.routes.EditLiabilityTypeController.editLiability(oldFormBundleNo, periodKey, editAllowed)))
            }
          }
        )
      }
  }

  private def returnToFormBundle(oldFormBundleNo: String, periodKey: Int) = {
    Some(controllers.routes.FormBundleReturnController.view(oldFormBundleNo, periodKey).url)
  }
}

object EditLiabilityTypeController extends EditLiabilityTypeController {
  val delegationConnector = FrontendDelegationConnector
  override val controllerId = "EditLiabilityTypeController"
  val dataCacheConnector = DataCacheConnector
  override val backLinkCacheConnector = BackLinkCacheConnector
}
