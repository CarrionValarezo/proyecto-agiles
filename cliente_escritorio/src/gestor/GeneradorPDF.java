package gestor;


import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import componentes.TablaActivosProceso;
import java.io.FileOutputStream;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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

	public void generarPdf(TablaActivosProceso table, String path, String nombre) throws Exception{
		int filasTabla = table.getRowCount(); 
		int columnasTabla = table.getColumnCount(); 
		System.out.println("FIlas: "+filasTabla);
		System.out.println("Columnas: "+columnasTabla);
		PdfWriter.getInstance(documento, new FileOutputStream(path+nombre+".pdf"));
		documento.open();

		Font font = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
		PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
		float [] columnsWidth = new float[]{5f, 7f, 10f, 20f, 10f, 10f, 10f, 0f,5f,20f}; 
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

		for (int rows = 0; rows < filasTabla ; rows++) {
			for (int cols = 0; cols < columnasTabla; cols++) {
				String valor = ((DefaultTableModel)table.getModel()).getValueAt(rows, cols).toString();
				pdfTable.addCell(new PdfPCell(new Phrase(valor,font)));
			}
		}
		documento.add(pdfTable);
		documento.close();
		System.out.println("done");
	}
	
}
