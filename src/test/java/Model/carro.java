package Model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Command;

import com.itextpdf.text.DocumentException;

import PageObject.BaseSFPage;
import PageObject.carrosPO;
import PageObject.Enums.AnoMinimo;
import PageObject.Enums.Marca;
import PageObject.Enums.Modelo;
import utils.Evidencia;

@SuppressWarnings("unused")
public class carro extends BaseSFPage {

	private carrosPO carro;
	

	public carro(WebDriver webDriver) {
		super(webDriver);
		carro = new carrosPO(webDriver);
	}

	public boolean isDisplayed() {
		return false;
	}
	
	/**
	 * 
	 * @param marca
	 * @throws Exception
	 */
	public void selecionarMarca(Marca marca) throws Exception {
		Evidencia.evidencia();
		command.click(carro.selecionarMarca()); 
		command.click(carro.preencherMarca(marca));
		
	}
	
	public void selecionarModelo(Modelo modelo) throws Exception {
		command.click(carro.selecionarModelo()); 
		command.click(carro.preencherModelo(modelo));
		
	}
	public void selecionarAnoMinimo(AnoMinimo anoMinimo) throws Exception {
		command.click(carro.selecionarAnoMinimo()); 
		command.click(carro.preencherAnoMinimo(anoMinimo));
		
	}
	
	public void selecionarBuscar() throws Exception {
		Evidencia.evidencia();
		command.click(carro.selecionarBuscar());
		
	}
	
	
	public void validarConsultar() throws DocumentException, Exception {
		Evidencia.evidencia();
		if (carro.formCarro()==false) {
			Evidencia.gravar("Busca feita com sucesso", "Consultar Carros");
		}else {
			Evidencia.gravar("Nenhum carro Encontrado", "Consultar Carros");
		}
		
	}
}