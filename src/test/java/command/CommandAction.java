package command;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

public abstract class CommandAction {

	final static Logger logger = Logger.getLogger(CommandAction.class);

	protected final long TIME_OUT;

	protected final String FOCUS_SCRIPT = " arguments[0].scrollIntoView(true); arguments[0].focus();";
	protected final String HIGHLIGTH_SCRIPT = "arguments[0].style.border='1px solid red'";
	protected static String msgError;

	protected WebDriver webDriver;

	protected WebDriverWait webDriverWait;

	/**
	 * Constructor
	 * 
	 * @param webDriver
	 */
	
	public CommandAction(WebDriver webDriver) {
		this.webDriver = webDriver;
		this.TIME_OUT = 10;
		this.webDriverWait = new WebDriverWait(this.webDriver, TIME_OUT);
	}

	/**
	 * Get text from object
	 * 
	 * @param webElement
	 * @return
	 */
	public String getText(WebElement webElement) {
		String text = null;
		try {
			text = webElement.getText();
		} catch (Exception e) {
			try {
				text = webElement.getAttribute("@value");
			} catch (Exception e2) {
				text = webElement.getAttribute("@label");
			}
		}
		return text;
	}

	/**
	 * Send Text
	 * 
	 * @param webElement
	 * @param value
	 * @throws Exception
	 */
	public void send(WebElement webElement, String value) throws Exception {
		try {
			webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
			webElement.click();
			webElement.sendKeys(value);
			value = webElement.getAttribute("type").equals("password") ? "*******" : value;

			if (webDriver instanceof PhantomJSDriver | webDriver instanceof ChromeDriver) {
				logger.debug("Objeto: 'By."
						+ webElement.toString().substring(0, webElement.toString().length() - 1).split("> ")[1]
						+ "' preenchido: '" + value + "'");
			} else {
				logger.debug("Objeto: '" + webElement.toString() + "' preenchido: '" + value + "'");
			}

			Reporter.log(" e inserido: '" + value + "'.");

		} catch (StaleElementReferenceException e) {
			msgError = "Objeto: '" + webElement.toString() + "' perdeu referencia ao preencher com '" + value + "'";
			logger.fatal(msgError, e);
			throw new Exception(msgError);
		} catch (Exception e) {
			try {
				webElement.click();
				Actions builder = new Actions(webDriver);
				builder.sendKeys(value).perform();
			} catch (Exception e1) {
				msgError = " Erro com objeto: '" + webElement.toString() + "' não preencheu com '" + value + "'";
				logger.fatal(msgError, e1);
				throw new Exception(msgError);
			}
		}
	}

	/**
	 * Select the text in dropdown element-type option
	 * 
	 * @param webElement
	 * @param value
	 * @throws Exception
	 */
	public void selectOption(WebElement webElement, String value) throws Exception {

		try {
			Select dropdown = new Select(webElement);
			dropdown.selectByVisibleText(value);
			logger.debug("Objeto: '" + webElement.toString() + "' selecionado: '" + value + "'");
			Reporter.log(" e selecionado: '" + value + "'.");
		} catch (TimeoutException e) {
			msgError = " TimeOut apos " + TIME_OUT + " seg., objeto: '" + webElement.toString() + "' nao selecionado '"
					+ value + "'";
			logger.fatal(msgError, e);
			throw new Exception(msgError);
		}
	}

	/**
	 * Send key ENTER
	 * 
	 * @param webElement
	 * @throws Exception sned
	 */
	public void pressEnter(WebElement webElement) throws Exception {
		pressKey(Keys.ENTER, webElement);
	}

	/**
	 * Send key TAB
	 * 
	 * @param webElement
	 * @throws Exception
	 */
	public void pressTab(WebElement webElement) throws Exception {
		pressKey(Keys.TAB, webElement);
	}

	/**
	 * Send key TAB to actual position
	 * 
	 * @throws Exception
	 */
	public void pressTab() throws Exception {
		pressKey(Keys.TAB, null);
	}

	private void pressKey(Keys key, WebElement webElement) {

		String keyPress = "";
		switch (key) {
		case ENTER:
			keyPress = "ENTER";
			break;
		case TAB:
			keyPress = "TAB";
			break;
		default:
			keyPress = key.toString();
			break;
		}

		try {
			if (webElement == null) {
				new Actions(webDriver).sendKeys(key).build().perform();
			} else {
				webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
				webElement.sendKeys(key);
			}

			Reporter.log("Pressionado'" + keyPress + "'");
		} catch (Exception e) {
			logger.error(webElement != null ? "objeto: '" + webElement.toString() + "' "
					: "" + "não enviou a tecla " + keyPress + "'", e);
		}
	}

	/**
	 * Simulate a click
	 * 
	 * @param webElement
	 * @throws Exception
	 */
	public void click(WebElement webElement) throws Exception {
		try {
			webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
			try {
				Thread.sleep(2000);
				webElement.click();
			} catch (Exception e) {
				Thread.sleep(3000);
				logger.debug("tentando clique por javaScript - " + webElement.toString());
			}

			if (webDriver instanceof PhantomJSDriver | webDriver instanceof ChromeDriver) {
				logger.debug("Objeto: 'By."
						+ webElement.toString().substring(0, webElement.toString().length() - 1).split("> ")[1]
						+ "' realizado click");
			} else {
				logger.debug("Objeto: '" + webElement.toString() + "' realizado click");
			}
			Reporter.log(" e recebeu click");
		} catch (Exception e) {
			msgError = "Objeto: '" + webElement.toString() + "' não conseguiu clicar";
			logger.fatal(msgError, e);
			throw new Exception(msgError);
		}
	}

	/**
	 * Insert Menu itens spaced by :
	 * 
	 * @param newNavegation
	 */
	public abstract void navigateMenu(String newNavegation);

	/**
	 * Focus
	 * 
	 * @param webElement
	 */
	public abstract void focus(WebElement webElement);

	/**
	 * highlight
	 * 
	 * @param webElement
	 */
	public abstract void highlight(WebElement webElement);

	/**
	 * @param element
	 */
	public void clear(WebElement webElement) {
		webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
		webElement.clear();
	}

	public byte[] screenshot() throws Exception {
		screenshotReady();
		if (!(webDriver instanceof HtmlUnitDriver)) {
			TakesScreenshot ts = (TakesScreenshot) webDriver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			String screenshotName = source.getName();
			String folder = new java.io.File(".").getCanonicalPath() + "/test-output/screenshot/";
			FileUtils.copyFile(source, new File(folder + screenshotName));
			logger.debug("Screenshot");
			String path = ("<img src=\"./screenshot/" + screenshotName + "\" width=\"100%\"");
			Reporter.log(path);
		}
		return null;
	}

	public boolean isAlertPresent() {
		try {
			webDriver.switchTo().alert();
			return true;
		} catch (Exception Ex) {
			return false;
		}
	}

	public void sendAlert(boolean accept) {
		if (accept) {
			webDriver.switchTo().alert().accept();
		} else {
			webDriver.switchTo().alert().dismiss();
		}
	}

	public String getAlertText() {
		if (isAlertPresent()) {
			String text = webDriver.switchTo().alert().getText();
			Reporter.log("Alerta - " + text);
			return text;
		} else {
			return null;
		}
	}

	protected abstract void screenshotReady();
}