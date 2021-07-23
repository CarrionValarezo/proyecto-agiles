package gestor;


import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import javax.swing.JTable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author carri
 */
public class GeneradorPDF {

	Document documento; 

	public GeneradorPDF(){
		documento = new Document(); 
		
	}

	public void generarPdf(JTable table, String path, String nombre) throws Exception{
		PdfWriter.getInstance(documento, new FileOutputStream(path+nombre+".pdf"));
		documento.open();

		Font font = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
		PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
		float [] columnsWidth = new float[]{5f, 7f, 10f, 20f, 10f, 10f, 10f, 0f,5f,20f,0f}; 
		//float [] columnsWidth = new float[]{5f, 7f, 10f, 20f, 10f, 10f, 10f,5f,20f}; 
		pdfTable.setWidths(columnsWidth);
		pdfTable.setWidthPercentage(100);
		//adding table headers
		for (int i = 0; i < table.getColumnCount(); i++) {
			pdfTable.addCell(new PdfPCell(new Phrase(table.getColumnName(i),font)));
		}
	
		//pdfTable.addCell(table.getColumnName(i));
		//pdfTable.addCell(new PdfPCell(new Phrase(table.getColumnName(i),font)));

		//extracting data from the JTable and inserting it to PdfPTable
		for (int rows = 0; rows < table.getRowCount() ; rows++) {
			for (int cols = 0; cols < table.getColumnCount(); cols++) {
				String valor = table.getModel().getValueAt(rows, cols).toString();
				pdfTable.addCell(new PdfPCell(new Phrase(valor,font)));
			}
		}
		documento.add(pdfTable);
		documento.close();
		System.out.println("done");
	}
	
}
