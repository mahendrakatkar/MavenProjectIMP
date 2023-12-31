package Ninja.qa;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import NinjaTutorialQAUtiles.Utilities;
import Ninja_BaseClass.Base;
import ninjaQaPages.AccountPage;
import ninjaQaPages.LoginPage;
import ninjaQaPages.homePages;

public class LoginTest extends Base {

	LoginPage loginPage;

	public LoginTest() {
		super();
	}

	public WebDriver driver;

	@BeforeMethod
	public void setup() {

		driver = InitializeBrowserAndOpenApplication(prop.getProperty("browserName"));

		homePages homepage = new homePages(driver);
		 loginPage = homepage.navigateToLoginPage();
	}

	@AfterMethod
	public void BrowserCloseMethod() { // created method to close browser after each method irrespective of pass/fail
										// of TC
		driver.quit();
	}

	@Test(priority = 1, dataProvider = "validCredentialsSupplier")
	public void VerifyLoginWithValidCreden(String email, String password) {

		AccountPage AccountPage = loginPage.login(email, password);

		Assert.assertTrue(AccountPage.getDisplayStatusofEdityourAccountInformationOption(),
				"edit your account information is not dispayed");
		// driver.quit(); added at top to avoid browser remains open for failed TC
	}

	@DataProvider(name = "validCredentialsSupplier")
	public Object[][] supplyTestData() {
		Object[][] data = Utilities.getTetstDataFromExcel("Login");
		return data;

	}

	@Test(priority = 2)
	public void VerifyLoginWithINValidCreden() {

		loginPage.login(Utilities.generateEmailAdreeTimeStamp(), dataProp.getProperty("invalidPassword"));
		/*
		 * loginPage.enterEmailAddress(Utilities.generateEmailAdreeTimeStamp());
		 * loginPage.enterPassword(dataProp.getProperty("invalidPassword"));
		 * loginPage.clickOnLoginButton();
		 */

		String ActualWarningMessage = loginPage.retreiveEmailPasswordNotMatcingWarningMessageText(); // this line can be replaced in next 3rd line word(ActualWarningMessage) to reduce number of lines in code
		String ExpectedWarningMessage = dataProp.getProperty("emailPasswordNomatchWarning");
		Assert.assertTrue(ActualWarningMessage.contains(ExpectedWarningMessage),
				"expected warning message is not matching");
		// driver.quit();
	}

	@Test(priority = 3)
	public void VerifyLoginWithINValidEmailAndValidPassword() {

		loginPage.login(Utilities.generateEmailAdreeTimeStamp(), prop.getProperty("validPassword"));
		
		String ActualWarningMessage = loginPage.retreiveEmailPasswordNotMatcingWarningMessageText();
		String ExpectedWarningMessage = dataProp.getProperty("emailPasswordNomatchWarning");
		Assert.assertTrue(ActualWarningMessage.contains(ExpectedWarningMessage),
				"expected warning message is not matching");
		// driver.quit();
	}

	@Test(priority = 4)
	public void VerifyLoginWithValidEmailAndINValidPassword() {
		loginPage.login(prop.getProperty("validEmail"), prop.getProperty("invalidPassword"));
		
		String ActualWarningMessage = loginPage.retreiveEmailPasswordNotMatcingWarningMessageText();
		String ExpectedWarningMessage = dataProp.getProperty("emailPasswordNomatchWarning");
		Assert.assertTrue(ActualWarningMessage.contains(ExpectedWarningMessage),
				"expected warning message is not matching");
		// driver.quit();
	}

	@Test(priority = 5)
	public void VerifyLoginwithoutCredentials() {

		loginPage.clickOnLoginButton();

		String ActualWarningMessage = loginPage.retreiveEmailPasswordNotMatcingWarningMessageText();
		String ExpectedWarningMessage = dataProp.getProperty("emailPasswordNomatchWarning");
		Assert.assertTrue(ActualWarningMessage.contains(ExpectedWarningMessage),
				"expected warning message is not matching");
		// driver.quit();
	}

}
