package PageObject;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import command.CommandType;
import searchelements.SearchElementType;

public abstract class BaseSFPage extends Page {

	static final Logger logger = Logger.getLogger(BaseSFPage.class);

	/*
	 * protected WebDriver webDriver; protected CommandAction command; protected
	 * SearchElement searchElement; protected WebDriverWait webDriverWait;
	 */

	/**
	 * Default options to Page Objects
	 * 
	 * @param webDriver
	 * @param command
	 * @param searchElement
	 */
	public BaseSFPage(WebDriver webDriver) {
		super(webDriver, CommandType.WEB.getCommand(webDriver), SearchElementType.WEB.getSearchElement(webDriver));
	}

	/**
	 * @return true if the current UI state is that which is represented by this
	 *         page object. false otherwise
	 * @throws Exception 
	 */
	public abstract boolean isDisplayed() throws Exception;
}