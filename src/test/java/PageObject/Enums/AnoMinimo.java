package PageObject.Enums;
import org.openqa.selenium.By;

public enum AnoMinimo {
	
	D2015(By.xpath("//*[@id='sltYearMin']/..//span[text()='De 2015']"), "De 2015"), 
	D2016(By.xpath("//*[@id='sltYearMin']/...//span[text()='De 2016']"), "De 2016");

	private Object[] option;

	AnoMinimo(Object... vals) {
		option = vals;
	}

	public By getBy() {
		return (By) option[0];
	}

	public String getTexto() {
		return (String) option[1];
	}

}
