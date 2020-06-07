package searchelements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

/**
 * Helper to get Objects for browser or devices
 * 
 * @author filipesantos
 *
 */
public abstract class SearchElement {

	final static Logger logger = Logger.getLogger(SearchElement.class);

	protected final static int TIME_OUT = 20;

	protected final static String FOCUS_SCRIPT = " arguments[0].scrollIntoView(true); arguments[0].focus();";

	protected static WebDriver webDriver;

	protected static WebDriverWait webDriverWait;

	/**
	 * Constructor
	 * 
	 * @param webDriver
	 */
	public SearchElement(WebDriver webDriver) {
		SearchElement.webDriver = webDriver;
		SearchElement.webDriverWait = new WebDriverWait(SearchElement.webDriver, TIME_OUT);
	}

	/**
	 * @param name
	 * @param friendly
	 * @return
	 * @throws Exception
	 */
	public WebElement findElementBy(By by, String reportName) throws Exception {
		try {
			waitToBeReady();
			WebElement webElement = null;
			webDriver.switchTo().defaultContent();

			List<WebElement> iframes = iFrames();

			if (iframes.size() == 0) {
				webElement = findelementDefaultContent(by, false);
			} else {
				try {
					webElement = findelementDefaultContent(by, true);
				} catch (Exception e) {
					Exception e1 = null;
					for (WebElement iframe : iframes) {

						try {
							webDriver.switchTo().defaultContent();
							webDriver.switchTo().frame(iframe);
							webElement = findelementDefaultContent(by, true);
							// webElement = webDriver.findElement(by);
							e1 = null;
							break;
						} catch (Exception e2) {
							e1 = e2;
						}
					}
					if (e1 != null) {
						throw new Exception(e1);

					}
				}

			}

			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
			((JavascriptExecutor) webDriver).executeScript(FOCUS_SCRIPT, webElement);

			if (reportName != null && !reportName.isEmpty()) {
				if (Reporter.getOutput().size() != 0) {
					Reporter.log("<br>");
				}
				Reporter.log("- Localizado " + reportName.trim());
			}

			return webElement;
		} catch (Exception e) {
			String errMsg = String.format(" Objeto %s não localizado por %s ", reportName.trim(), by.toString());
			logger.fatal(errMsg, e);
			throw new Exception(errMsg);
		}
	}

	private WebElement findelementDefaultContent(By by, boolean fast) throws Exception {
		if (!fast) {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(by));
		}
		return webDriver.findElement(by);

	}

	/**
	 * 
	 * @param name
	 * @param friendly
	 * @return
	 * @throws Exception
	 */
	public List<WebElement> findElementsBy(By by, String reportName) throws Exception {
		try {
			waitToBeReady();
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
			List<WebElement> webElements = null;

			webDriver.switchTo().defaultContent();
			List<WebElement> iframes = iFrames();

			if (iframes.size() == 0) {
				webElements = webDriver.findElements(by);
			} else {
				try {
					webElements = webDriver.findElements(by);
				} catch (Exception e) {

					Exception e1 = null;
					for (WebElement iframe : iframes) {

						try {
							webDriver.switchTo().defaultContent();
							webDriver.switchTo().frame(iframe);
							webElements = webDriver.findElements(by);
							e1 = null;
							break;
						} catch (Exception e2) {
							e1 = e2;
						}
					}
					if (e1 != null) {
						throw new Exception(e1);
					}
				}

			}

			if (reportName != null && !reportName.trim().isEmpty()) {
				Reporter.log("-Localizado os itens " + reportName.trim());
			}

			return webElements;
		} catch (Exception e) {
			String errMsg = String.format("Lista de Objeto %s não localizado por %s ", reportName.trim(),
					by.toString());
			logger.fatal(errMsg, e);
			throw new Exception(errMsg);
		}
	}

	private List<WebElement> iFrames() {
		return webDriver.findElements(By.xpath("//frame | //iframe"));
	}

	/**
	 * 
	 * @param name
	 * @param friendly
	 * @return
	 * @throws Exception
	 */
	public WebElement findChildElementBy(WebElement parent, By by, String reportName) throws Exception {
		try {
			waitToBeReady();
			webDriverWait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parent, by));
			WebElement webElement = parent.findElement(by);
			if (reportName != null && !reportName.trim().isEmpty()) {
				Reporter.log("-Localizados os objetos " + reportName.trim());
			}
			logger.info(by.toString() + " - " + parent.toString());
			return webElement;

		} catch (Exception e) {
			String errMsg = String.format(" Objeto %s não localizado por %s ", reportName.trim(), by.toString());
			logger.fatal(errMsg, e);
			throw new Exception(errMsg);
		}
	}

	/**
	 * 
	 * @param name
	 * @param friendly
	 * @return
	 * @throws Exception
	 */
	public List<WebElement> findChildElementsBy(WebElement parent, By by, String reportName) throws Exception {
		try {
			waitToBeReady();
			webDriverWait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parent, by));
			List<WebElement> webElements = parent.findElements(by);
			if (reportName != null && !reportName.trim().isEmpty()) {
				Reporter.log("-Localizados os objetos " + reportName.trim());
			}

			return webElements;
		} catch (Exception e) {
			String errMsg = String.format("Lista de objetos %s nao localizado por %s ", reportName.trim(),
					by.toString());
			logger.fatal(errMsg, e);
			throw new Exception(errMsg);
		}
	}

	/**
	 * 
	 * @param Xpath
	 * @param friendly
	 * @return
	 * @throws Exception
	 */
	public Boolean exists(By by, String reportName) throws Exception {
		return exists(by, reportName, TIME_OUT, true);
	}

	public Boolean exists(By by, String reportName, long timeWait) throws Exception {
		return exists(by, reportName, timeWait, true);
	}

	/**
	 * 
	 * @param xpath
	 * @param friendly
	 * @param timeWait
	 * @return
	 * @throws Exception
	 */
	private Boolean exists(By by, String reportName, long timeWait, boolean log) throws Exception {

		WebDriverWait waitExists = new WebDriverWait(SearchElement.webDriver, timeWait);
		waitToBeReady();

		try {
			List<WebElement> iframes = iFrames();

			if (iframes.size() == 0) {
				waitExists.until(ExpectedConditions.presenceOfElementLocated(by));
			} else {
				try {
					waitExists.until(ExpectedConditions.presenceOfElementLocated(by));
				} catch (Exception e) {
					Exception e1 = null;
					for (WebElement iframe : iframes) {

						try {
							webDriver.switchTo().defaultContent();
							webDriver.switchTo().frame(iframe);
							waitExists.until(ExpectedConditions.presenceOfElementLocated(by));
							e1 = null;
							break;
						} catch (Exception e2) {
							e1 = e2;
						}
					}
					if (e1 != null) {
						throw new Exception(e1);

					}
				}

			}
			waitExists.until(ExpectedConditions.visibilityOfElementLocated(by));
			if (log) {
				Reporter.log("- Localizado " + reportName.trim());
			}
		} catch (Exception e) {
			if (log) {
				String errorMsg = String.format(" Objeto %s não localizado %s ", reportName.trim(), by.toString());
				logger.debug(errorMsg, e);
			}
			return false;
		}
		return true;
	}

	public void waitToBeReady() throws Exception {
		try {

			List<By> loaders = new ArrayList<By>();
			// SalesForce
			loaders.add(By.xpath("//div[@class='loader']"));
			loaders.add(By.xpath("//section[@class='modalLoaderErro']"));
			loaders.add(By.xpath("//div[contains(@class,'LoadingBox')]"));
			loaders.add(By.xpath("//div[contains(@class,'slds-spinner')]"));
			// Corretor online
			loaders.add(By.xpath(".//*[contains(@class,'ps-ico-loading-modal')]"));

			webDriver.switchTo().defaultContent();

			

			List<WebElement> iframes = webDriver.findElements(By.xpath("//iframe"));

			if (iframes.size() != 0) {
				for (WebElement iframe : iframes) {
					try {
						webDriver.switchTo().frame(iframe);
						invisibility(loaders);
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
			webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		}
	}

	public static void invisibility(List<By> loaders) {
		for (By by : loaders) {
			new WebDriverWait(webDriver, 30).until(ExpectedConditions.invisibilityOfElementLocated(by));
		}
	}

	public boolean existsNoLog(By by, String reportName, int timeWait) throws Exception {
		return exists(by, reportName, timeWait, false);
	}
}