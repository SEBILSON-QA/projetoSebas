package PageObject.Enums;
import org.openqa.selenium.By;

public enum Marca {
	
	AUDI(By.xpath("//*[@id='sltMake']/..//span[text()='Audi']"), "Audi"), 
	BMW(By.xpath("//*[@id='sltMake']/..//span[text()='BMW']"), "BMW");

	private Object[] option;

	Marca(Object... vals) {
		option = vals;
	}

	public By getBy() {
		return (By) option[0];
	}

	public String getTexto() {
		return (String) option[1];
	}

}
