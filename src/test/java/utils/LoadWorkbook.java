package utils;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Assert;



public class LoadWorkbook {
	private static final String OCORREU_ERRO_AO_CARREGAR_O_DICTIONARY_APACHE_POI_FOR_LOADING_DICTIONARY = "Ocorreu erro ao carregar o dictionary apache poi - forLoadingDictionary";
	private static final String NAO_FOI_POSSIVEL_REALIZAR_A_LEITURA_DO_ARQUIVO = "Nao foi possivel realizar a leitura do arquivo ";
	public static final Logger logger = Logger.getLogger(LoadWorkbook.class);
	private Workbook workbook;
	private String messageError;
	
	/**
	 * Construtor
	 */
	public LoadWorkbook(){}
	
	/**
	 * Construtor sobrecarga
	 * @param path
	 * @throws Exception
	 */
	public LoadWorkbook(String path){
		this.setPath(path);
	}
	
	/**
	 *
	 * @param path
	 */
	public void setPath(String path) {
		try {
			workbook = WorkbookFactory.create(new FileInputStream(path.replace("\\", "//")));
		} catch (Exception e) {
			Assert.fail("Falha ao tentar abrir o arquivo " + path +
					"\n Verifique se o mesmo existe ou está aberto por outro recurso.");
		}
		logger.debug("setFile : Path informado = " + path);
	}
	
	/**
	 * 
	 * @param nameSheet
	 * @return
	 * @throws Exception
	 */
	public synchronized Map<String, String> loadDictionary(String nameSheet)throws Exception{		
			Map<String, String> dictionary = new HashMap<>();
			Sheet sheet = workbook.getSheet(nameSheet);
			validateOpenSheet(nameSheet, sheet);
			logger.debug("Localizada a aba " + sheet.getSheetName());
			forLoadingDictionary(dictionary, sheet);
			logger.debug("Finalizado Leitura da aba " + sheet.getSheetName());
			workbook.close();
			return dictionary;
		}

	/**
	 * @param dictionary
	 * @param sheet
	 * @throws Exception
	 */
	private void forLoadingDictionary(Map<String, String> dictionary, Sheet sheet) {
		int colKeyNum = -1;
		int colValueNum = -1;	
		try {
			for(int rowID = 0; rowID <= sheet.getLastRowNum(); rowID++){
				Row row = sheet.getRow(rowID);
				if(row != null){
					if (row.getRowNum() == 0) {
						colKeyNum = getColNum(row, "key");
						colValueNum = getColNum(row, "value");
						logger.debug("Localizado key e value na aba " + sheet.getSheetName());
					}
					if(row.getCell(colKeyNum) !=  null){
						validacaoString(dictionary, sheet, colKeyNum, colValueNum, row);
					}else{
						logger.debug(String.format("Aba %s, Linha %s, Coluna %s celula com valor nulo.{Esperado:Cell_Type_String}", sheet.getSheetName(), (row.getRowNum()+1), colKeyNum ));
					}
				}
			}
		} catch (Exception e) {
			logger.error(OCORREU_ERRO_AO_CARREGAR_O_DICTIONARY_APACHE_POI_FOR_LOADING_DICTIONARY);
			Assert.fail(OCORREU_ERRO_AO_CARREGAR_O_DICTIONARY_APACHE_POI_FOR_LOADING_DICTIONARY);
		}
	}

	/**
	 * @param dictionary
	 * @param sheet
	 * @param colKeyNum
	 * @param colValueNum
	 * @param row
	 * @throws Exception
	 */
	private void validacaoString(Map<String, String> dictionary, Sheet sheet, int colKeyNum, int colValueNum, Row row)
			throws Exception {
		if(row.getCell(colKeyNum).getCellType() == Cell.CELL_TYPE_STRING){
			logger.debug(String.format("key localizada = %s", row.getCell(colKeyNum).getStringCellValue()));
			dictionary.put(row.getCell(colKeyNum).getStringCellValue(), getValueCell(row.getCell(colValueNum)));
		}
		else{
			messageError = String.format("Linha %s, Coluna %s com tipo de celula invalida na aba %s, somente eh permitido texto para a coluna key.", (row.getRowNum()+1), colKeyNum, sheet.getSheetName());
			logger.fatal(messageError);
			Assert.fail(messageError);
		}
	}
	
	/**
	 * 
	 * @param namesColumns
	 * @param nameSheet
	 * @param filter
	 * @param amarracao
	 * @return
	 * @throws Exception
	 */
	public synchronized Map<String, List<String>> loadData(String[] namesColumns, String nameSheet,String filter, String amarracao) throws Exception {
		Map<String,List<String>> data = new HashMap<>();
		Sheet sheet = workbook.getSheet(nameSheet);
		validateOpenSheet(nameSheet, sheet);
		int columnNumFilter = getColNum(sheet.getRow(0), amarracao);
		if(namesColumns != null){
			loadDataAddList(namesColumns, filter, data, sheet, columnNumFilter);
		}else{
			throw new Exception("Nao foi definido nenhuma coluna para busca de valores!");
		}
		workbook.close();
		return data;
	}

	/**
	 * @param namesColumns
	 * @param filter
	 * @param Data
	 * @param sheet
	 * @param columnNumFilter
	 * @throws Exception
	 */
	private void loadDataAddList(String[] namesColumns, String filter, Map<String, List<String>> data, Sheet sheet,
			int columnNumFilter) throws Exception {
		for(String nameColumn : namesColumns){
			List<String> listItem = new ArrayList<String>();
			int columnNum = getColNum(sheet.getRow(0), nameColumn);
			for(int i=1; i <= sheet.getLastRowNum();i++ ){
				if(sheet.getRow(i) != null){
					if(filter == null){
						listItem.add(getValueCell(sheet.getRow(i).getCell(columnNum)));
					}
					else if(getValueCell(sheet.getRow(i).getCell(columnNumFilter)).toLowerCase().equals(filter.toLowerCase())){     
//					else if(getValueCell(sheet.getRow(i).getCell(columnNumFilter)).toLowerCase().contains(filter.toLowerCase())){     antes 14:10
						listItem.add(getValueCell(sheet.getRow(i).getCell(columnNum)));
					}
				}
				else{
					logger.debug("linha "+ (i+1) +" sem valores");
				}
			}
			data.put(nameColumn, listItem);	
		}
	}

	/**
	 * @param nameSheet
	 * @param sheet
	 * @throws Exception
	 */
	private void validateOpenSheet(String nameSheet, Sheet sheet) throws Exception {
		if(sheet == null){
			throw new Exception("Falha ao tentar abrir a sheet " + nameSheet);
		}
	}

	/**
	 * Retorna uma abastracao de uma planilha onde a key eh o nome da sheet e a lista contem os valores
	 * das celulas.
	 * @return HashMap<String, List<List<String>>>
	 * @throws Exception
	 */
	public synchronized Map<String, List<List<String>>> loadData() throws Exception{
		Map<String,List<List<String>>> Data = new HashMap<>();
		for(Sheet sheet : workbook){
			List<List<String>> listItem = new ArrayList<List<String>>();
			for(int i=0; i <= sheet.getLastRowNum();i++){
				if(sheet.getRow(i) != null){
					List<String> cels = new ArrayList<String>();
					for(int j=0;j<=sheet.getRow(i).getLastCellNum();j++){
						cels.add(getValueCell(sheet.getRow(i).getCell(j)));
					}
					listItem.add(cels);
				}
			}
			Data.put(sheet.getSheetName(), listItem);
			workbook.close();
		}
		return Data;
	}
	
	public synchronized Map<String, List<List<String>>> buscarPrimeiraColunaMassa() throws Exception{
		HashMap<String,List<List<String>>> Data = new HashMap<String,List<List<String>>>();
		for(Sheet sheet : workbook){
			List<List<String>> listItem = new ArrayList<List<String>>();
			for(int i=0; i <= 200;i++){
				if(sheet.getRow(i) != null){
					List<String> cels = new ArrayList<String>();
					cels.add(getValueCell(sheet.getRow(i).getCell(0)));
					listItem.add(cels);
				}
			}
			Data.put(sheet.getSheetName(), listItem);
			workbook.close();
		}
		return Data;
	}

	/**
	 * 
	 * @param row
	 * @param nameCol
	 * @return
	 * @throws Exception
	 */
	private int getColNum(Row row, String nameCol) throws Exception {
		if (row != null) {
			for (int colID = 0; colID <= (row.getLastCellNum() - 1); colID++) {
				if (row.getCell(colID) != null && row.getCell(colID).getCellType() == Cell.CELL_TYPE_STRING) {
					if (row.getCell(colID).getStringCellValue().equalsIgnoreCase(nameCol.toLowerCase())) {
						logger.debug("Localizado o titulo " + nameCol + " na coluna de numero " + colID);
						return colID;
					}
				}
			}
		}
		logger.fatal("Nao foi localizado o titulo vinculado à " + nameCol);
		throw new Exception(messageError);
	}
	
	/**
	 * 
	 * @param cell
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	private String getValueCell(Cell cell) throws Exception{
		if(cell != null){
			switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING :
					logger.debug(String.format("Value STRING localizada = %s", cell.getStringCellValue()));
					return cell.getStringCellValue();
			
				case Cell.CELL_TYPE_NUMERIC :
					if (HSSFDateUtil.isCellDateFormatted(cell)) {		
						logger.debug(String.format("Value DATE localizada = %s",String.valueOf(cell.getDateCellValue())));
				        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(cell.getDateCellValue());
				    }
					logger.debug(String.format("Value NUMERIC localizada = %s",String.valueOf(cell.getNumericCellValue())));
					Double d = new Double (String.valueOf(cell.getNumericCellValue()));
					if (d%1==0) return String.valueOf(d.longValue());
					else return String.valueOf(d) ;
				case Cell.CELL_TYPE_BOOLEAN :
					logger.debug(String.format("Value BOOLEAN localizada = %s", String.valueOf(cell.getBooleanCellValue())));
					return String.valueOf(cell.getBooleanCellValue());
			
				case Cell.CELL_TYPE_FORMULA :
					logger.debug(String.format("Value FORMULA localizada = %s", String.valueOf(cell.getCellFormula())));
					return getValueCell(workbook.getCreationHelper().createFormulaEvaluator().evaluateInCell(cell));
			
				case Cell.CELL_TYPE_BLANK :
					logger.debug(String.format("Value BLANK localizada = %s", String.valueOf(cell.getStringCellValue())));
					return cell.getStringCellValue();	
				 default:
					    logger.error("Ocorreu erro na seleção do tipo de célula - loadworkbook");
					    break;
			}
		}
		else{
			logger.debug("Value NULL localizada");
			return new String();
		}
		messageError = String.format("Linha %s, Coluna %s com tipo de celula invalida.", (cell.getRowIndex()+1) , cell.getColumnIndex());
		logger.fatal(messageError);
		throw new Exception(messageError);
	}
	
	public static List<List<String>> loadCSV(String path, String filter) throws Exception{
		return loadCSV(path, filter, 0);
		
	}
		
	public static List<List<String>> loadCSV(String path, String filter, int columnFilter) throws Exception{
		List<List<String>> CSV = new ArrayList<List<String>>();
		String row = "";
		BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
		try {
			while((row = bufferedReader.readLine()) != null){
				String[] rowSplit = row.split(";");
					if(rowSplit.length > 0){
						List<String> Columns = new ArrayList<String>();
						if(rowSplit[columnFilter].toLowerCase().contains(filter.toLowerCase())){
							for(String column : rowSplit){
								if(!column.contains(filter)){
									Columns.add(column);
								}
							}
							CSV.add(Columns);
						}
					}				
			}
		} catch (Exception e) {
			throw new Exception(NAO_FOI_POSSIVEL_REALIZAR_A_LEITURA_DO_ARQUIVO+ path);
		}finally {
			bufferedReader.close();
		}
			return CSV;	
	}
	
}
