package utils;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import org.openqa.selenium.WebDriver;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

		public class Evidencia {
		public static  WebDriver driver;
		public static int contador=0;
		 
		
		public Evidencia(WebDriver webDriver) {
			driver=webDriver;
		}

		/**
		 * Tirando print da tela
		 * 
		 * @throws Exception
		 */
		public static void evidencia() throws Exception {
	         Thread.sleep(1000);
	        try {
	            Robot robot = new Robot();
	            contador=contador+1;
	            String num=Integer.toString(contador);
	            String format = "png";
	            String path = Paths.get("").toAbsolutePath().toString() + "\\data\\Massa\\Img";
	            String fileName = path+"\\"+num+"."+format;
	            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
	            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
	            ImageIO.write(screenFullImage, format, new File(fileName));
	           System.out.println("Print da tela salva!");
	        } catch (AWTException | IOException ex) {
	            System.err.println(ex);
	       }
	    }
		
		/**
		 * Gravando as Evidencias
		 * 
		 * @param id
		 * @param situacao
		 * @return 
		 * @throws Exception
		 * @throws DocumentException
		 */
		public static String gravar(String situacao,String tituloTeste) throws Exception, DocumentException {
			String path = new File("..\\Custom Office Templates\\data\\Massa\\Img") .getCanonicalPath();
			File arquivo = new File(path);
			File[] arquivos = arquivo.listFiles();
			int aux = 0;
			if (arquivos != null) {
				int length = arquivos.length;
				for (int i = 0; i < length; ++i) {
					File f = arquivos[i];
					if (f.isFile()) {
						aux++;
					}
				}
			}
			Document  doc = new Document();
			String pathP = new File("..\\Custom Office Templates\\data\\Massa").getCanonicalPath();
			File diretorio = new File(pathP+"\\"+tituloTeste);
		    if (!diretorio.exists()) {
		    	diretorio.mkdir();
			}
		    
		    String caminhoTeste=diretorio+"\\"+tituloTeste+"ID";
			PdfWriter.getInstance(doc, new FileOutputStream(diretorio+"\\"+tituloTeste+"ID"+".pdf"));
			
			doc.open();
	        for (int i = 1; i <= aux; i++) {
	        	doc.setPageSize(PageSize.A4);
	        	doc.add(cabecalho(situacao, tituloTeste));
	        	Image figura = Image.getInstance(path+"\\"+i+".png");
	            figura.setAbsolutePosition(20, 400);
	            figura.scaleToFit(550, 750);
	            doc.add(figura);
	            doc.newPage();
	        }
	        doc.close();
			contador=0;
			return caminhoTeste;
		}

		/**
		 * Criando CabeÃ§alho da Evidencia
		 * 
		 * @param id
		 * @param situacao
		 * @return
		 * @throws Exception
		 * @throws DocumentException
		 */
		public static PdfPTable cabecalho(String situacao, String tituloTeste) throws Exception, DocumentException {
			String dataAtual = getDateTime();
	        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	        Font boldFontStatus = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.RED);
	        PdfPTable tableheader = new PdfPTable(new float[] { 0.20f, 0.35f,0.13f,0.32f });
	        PdfPCell header = new PdfPCell(new Paragraph("Evidencia De Teste"));
	        header.setUseBorderPadding(true);
	        header.setBorderColor(BaseColor.BLUE);
	        header.setHorizontalAlignment(Element.ALIGN_CENTER);
	        header.setColspan(4);
	        tableheader.setSpacingBefore(20);
	        tableheader.addCell(header);
	        PdfPCell lblSistema = new PdfPCell(new Paragraph("Sistema", boldFont));
	        PdfPCell txtSistema = new PdfPCell(new Paragraph("Portal Icarros"));
	        PdfPCell lblVersao = new PdfPCell(new Paragraph("Ambiente", boldFont));
	        PdfPCell txtVersao = new PdfPCell(new Paragraph("Produção"));
	        PdfPCell lblID = new PdfPCell(new Paragraph("Teste", boldFont));
	        PdfPCell txtID = new PdfPCell(new Paragraph(tituloTeste));
	        PdfPCell lblStatus = new PdfPCell(new Paragraph("Status",boldFont));
	        PdfPCell txtStatus = new PdfPCell(new Paragraph(situacao,boldFontStatus));
	        PdfPCell lblData = new PdfPCell(new Paragraph("Data", boldFont));
	        PdfPCell txtData = new PdfPCell(new Paragraph(dataAtual));
	        lblSistema.setBorderColor(BaseColor.BLUE);
	        lblSistema.setHorizontalAlignment(Element.ALIGN_TOP);
	        txtSistema.setBorderColor(BaseColor.BLUE);  
	        lblVersao.setBorderColor(BaseColor.BLUE);
	        lblVersao.setHorizontalAlignment(Element.ALIGN_TOP);
	        txtVersao.setBorderColor(BaseColor.BLUE);
	        lblID.setBorderColor(BaseColor.BLUE);
	        lblID.setHorizontalAlignment(Element.ALIGN_TOP);
	        txtID.setBorderColor(BaseColor.BLUE);
	        lblStatus.setBorderColor(BaseColor.BLUE);
	        lblStatus.setHorizontalAlignment(Element.ALIGN_TOP);
	        txtStatus.setBorderColor(BaseColor.BLUE);
	        lblData.setBorderColor(BaseColor.BLUE);
	        lblData.setHorizontalAlignment(Element.ALIGN_TOP);
	        txtData.setBorderColor(BaseColor.BLUE);
	        tableheader.addCell(lblSistema);
	        tableheader.addCell(txtSistema);
	        tableheader.addCell(lblVersao);
	        tableheader.addCell(txtVersao);
	        tableheader.addCell(lblID);
	        tableheader.addCell(txtID);
	        tableheader.addCell(lblStatus);
	        tableheader.addCell(txtStatus);
	        tableheader.addCell(lblData);
	        tableheader.addCell(txtData);
	        tableheader.setSpacingAfter(20);
			return tableheader;
		}
		
		/**
		 * Pegando a Data do sitema
		 * @return
		 */
		public static String  getDateTime() { 
			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
			return dateFormat.format(date); 
		}	
}
	
