package platform;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromePlatform  {

	final static Logger logger = Logger.getLogger(ChromePlatform.class);

	private static String OS;
	private static String GETBINARYPATH;

	public static ChromePlatform StartWebDriver() {
		return new ChromePlatform();
	}

	public ChromePlatform() {
		OS = System.getProperty("os.name").toLowerCase();

		GETBINARYPATH = "src/test/resources/chromeDriver/";
		GETBINARYPATH = GETBINARYPATH + (OS.contains("windows") ? "win_chromedriver.exe"
				: OS.contains("mac") ? "mac_chromedriver" : "lin_chromedriver");
	}

	public WebDriver getLocalWebDriver() {
	    if(OS.toLowerCase().contains("windows")) {
			try {
				logger.info("Fechado instancias de Chrome abertas");
				Process process = Runtime. getRuntime(). exec("taskkill /F /IM win_chromedriver.exe");
				for (int i = 0; i < 10; i++) {
					if (process.isAlive()) {
						Thread.sleep(1000);
					}
				}
				process.destroy();
				
				process = Runtime. getRuntime(). exec("taskkill /F /IM chrome.exe");
				for (int t = 0; t < 10; t++) {
					if (process.isAlive()) {
						Thread.sleep(1000);
					}
					
				}
				
			} catch (Exception e) {
				logger.fatal("Erro ao finalizar driver do chrome aberto");
			}
            
	    }
		
		
		logger.info("Inciando o Chrome local");
		String currentPath = "";
		try {
			currentPath = new java.io.File(".").getCanonicalPath() + "/downloads";
		} catch (IOException e) {
			logger.error("Erro ao acessar o diretorio de Download", e);
		}

		System.setProperty("webdriver.chrome.driver", GETBINARYPATH);
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("download.default_directory=" + currentPath);
        
		WebDriver webDriver = new ChromeDriver(chromeOptions);
		webDriver.manage().window().maximize();
		webDriver.get("https://www.icarros.com.br/principal/index.jsp");

		return webDriver;
	}

}