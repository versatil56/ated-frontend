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

package views.editLiability

import java.util.UUID

import builders.AuthBuilder._
import forms.PropertyDetailsForms._
import models.{HasValueChanged, PropertyDetailsRevalued}
import org.joda.time.LocalDate
import org.jsoup.Jsoup
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, FeatureSpec, GivenWhenThen}
import org.scalatestplus.play.OneServerPerSuite
import play.api.test.FakeRequest

class editLiabilityValueSpec extends FeatureSpec with OneServerPerSuite with MockitoSugar with BeforeAndAfterEach with GivenWhenThen {

  val userId = s"user-${UUID.randomUUID}"
  implicit val user = createAtedContext(createUserAuthContext(userId, "name"))
  implicit val messages : play.api.i18n.Messages = play.api.i18n.Messages.Implicits.applicationMessages

  feature("The user can view an edit liability value page") {
    info("as a user I want to view the correct page content")

    scenario("user has visited the page for the first time") {

      Given("A user visits the page and clicks yes")
      When("The user views the page and clicks yes")

      implicit val request = FakeRequest()

      val html = views.html.editLiability.editLiabilityHasValueChanged(Some(BigDecimal(123.45)), "1", hasValueChangedForm, None, Some("http://backLink"))

      val document = Jsoup.parse(html.toString())
      Then("the page title : Has the value of your property changed for the purposes of ATED?")
      assert(document.title() === "Has the value of your property changed for the purposes of ATED?")

      assert(document.getElementById("pre-heading").text() === "This section is: Create return")

      assert(document.getElementById("value-text").text() === "Based on the information you have previously given us the value of your property for the purposes of ATED is £123")

      And("No data is populated")
      assert(document.getElementById("hasValueChanged").text() === "Has the value of your property changed for the purposes of ATED? Yes No")
      assert(document.getElementById("hasValueChanged-true").text() === "")
      assert(document.getElementById("hasValueChanged-false").text() === "")

      And("the save button is correct")
      assert(document.getElementById("submit").text() === "Save and continue")

      Then("The back link is correct")
      assert(document.getElementById("backLinkHref").text === "Back")
      assert(document.getElementById("backLinkHref").attr("href") === "http://backLink")
    }

  }

  feature("The user can edit a the setting to indicate that no value has change") {

    info("The yes option has been clicked may a user")

    scenario("The user views the page to edit the data") {

      Given("A user visits the page to edit data")
      When("The user views the page to edit data")

      implicit val request = FakeRequest()

      val html = views.html.editLiability.editLiabilityHasValueChanged(Some(BigDecimal(45678.12)), "1", hasValueChangedForm.fill(HasValueChanged(Some(true))), None, Some("http://backLink"))

      val document = Jsoup.parse(html.toString())
      Then("the page title : Has the value of your property changed for the purposes of ATED?")
      assert(document.title() === "Has the value of your property changed for the purposes of ATED?")

      assert(document.getElementById("pre-heading").text() === "This section is: Create return")

      assert(document.getElementById("value-text").text() === "Based on the information you have previously given us the value of your property for the purposes of ATED is £45,678")

      And("The data is populated for a property value set to true")
      assert(document.getElementById("hasValueChanged").text() === "Has the value of your property changed for the purposes of ATED? Yes No")
      assert(document.getElementById("hasValueChanged-true").attr("checked") === "checked")
      assert(document.getElementById("hasValueChanged-false").attr("checked") === "")

      assert(document.getElementById("submit").text() === "Save and continue")

      Then("The back link is correct")
      assert(document.getElementById("backLinkHref").text === "Back")
      assert(document.getElementById("backLinkHref").attr("href") === "http://backLink")
    }

  }

}
