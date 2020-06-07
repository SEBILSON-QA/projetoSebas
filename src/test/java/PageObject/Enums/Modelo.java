package PageObject.Enums;
import org.openqa.selenium.By;

public enum Modelo {
	
	A8(By.xpath("//*[@id='sltModel']/..//span[text()='A8']"), "A8"), 
	A6(By.xpath("//*[@id='sltModel']/..//span[text()='A6']"), "A6");

	private Object[] option;

	Modelo(Object... vals) {
		option = vals;
	}

	public By getBy() {
		return (By) option[0];
	}

	public String getTexto() {
		return (String) option[1];
	}

}
