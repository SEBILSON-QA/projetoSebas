package utils;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;



/**
 * Classe para coleta de dados
 * da massa, no excel de nome:
 * icarros.xlsx
 * @author Leonardo
 *
 */

public class MassaExcelSeguroViagem {

	/**
	 * Logger
	 */
	static final Logger logger = Logger.getLogger(MassaExcelSeguroViagem.class);
	private LoadWorkbook loadWorkbook;
	private String[] colunasExcelMassa;
	List ajuda ;
	
	/**
	 * 
	 * @param produto
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<String>> obterDadosMassa() throws Exception {
		Map<String, List<String>> dictionary = null;
		List<String> createTables = new ArrayList<String>();
		String pathFile = Paths.get("").toAbsolutePath().toString() + "\\data\\Massa\\icarros.xlsx";
		loadWorkbook = new LoadWorkbook(pathFile);
		deParaPlanilhaCamposMassa();
			dictionary = loadWorkbook.loadData(colunasExcelMassa, "icarros", Integer.toString(1), "teste");
			createTables.add(dictionary.toString().replaceAll("[\\[\\]]",  ""));
			//createTables.add("CREATE TABLE aa" + " AS " + dictionary.toString().replaceAll("[\\[\\]]",  ""));

	
		return dictionary;
	}

	/**
	 * Mapeamento dos campos da planilha
	 * de carregamento de massa do icarros
	 */
	private void deParaPlanilhaCamposMassa() {
		colunasExcelMassa = new String[] {"Marca","Modelo","Ano"};
	}
}
