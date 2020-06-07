package command;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import searchelements.SearchElement;
import searchelements.SearchElementType;

public class CommandActionWeb extends CommandAction {

	/**
	 * Constructor
	 * 
	 * @param webDriver
	 */
	public CommandActionWeb(WebDriver webDriver) {
		super(webDriver);
	}

	private boolean isHtmlUnit() {
		return webDriver.toString().toLowerCase().contains("htmlunit") ? true : false;
	}

	@Override
	public void navigateMenu(String newNavegation) {
		logger.warn("Not implemented");
	}

	@Override
	public void focus(WebElement webElement) {
		webDriverWait.until(ExpectedConditions.visibilityOf(webElement));
		if (!isHtmlUnit())
			((JavascriptExecutor) webDriver).executeScript(FOCUS_SCRIPT, webElement);
	}

	@Override
	public void highlight(WebElement webElement) {
		webDriverWait.until(ExpectedConditions.visibilityOf(webElement));
		if (!isHtmlUnit())
			((JavascriptExecutor) webDriver).executeScript(HIGHLIGTH_SCRIPT, webElement);
	}

	
	@Override
	protected void screenshotReady() {
		SearchElement searchElement = SearchElementType.getSearchElement(SearchElementType.WEB, webDriver);
		try {
			searchElement.waitToBeReady();
		} catch (Exception e) {
			logger.error("Page not ready. Please check!");
		}
	}
	
}