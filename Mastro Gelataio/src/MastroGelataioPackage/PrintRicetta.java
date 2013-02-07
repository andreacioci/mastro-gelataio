package MastroGelataioPackage;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;

import javax.swing.JTable;

public class PrintRicetta implements Printable {

	//--- Private instances declarations
	  private PrinterJob printJob;
	  private String sTitoloRicetta;
	  private JTable tableIng;
	  private JTable tableTot;
	  private JTable tableSoglie;
	  private String sNote;
	  private Graphics2D g2d;
	  double titleX;
	  
	  private int iMargineBordo;
	  private int iMargineTotali;
	  
	public PrintRicetta() 
	{
	    //--- Create a printerJob object
	    printJob = PrinterJob.getPrinterJob();

	    //--- Set the printable class to this one since we
	    //--- are implementing the Printable interface
	    printJob.setPrintable(this);
	}
	
	public void ImpostaTitoloRicetta(String sTitolo)
	{
		sTitoloRicetta = sTitolo;
	}
	
	public void ImpostaIngredienti(JTable tabellaInput)
	{
		tableIng = null;
		tableIng = new JTable();
		tableIng = tabellaInput;
	}
	
	public void ImpostaTotali(JTable tabellaInput)
	{
		tableTot = null;
		tableTot = new JTable();
		tableTot = tabellaInput;
	}
	
	public void ImpostaSoglie(JTable tabellaInput)
	{
		tableSoglie = null;
		tableSoglie = new JTable();
		tableSoglie = tabellaInput;
	}
	
	public void ImpostaNote(String note)
	{
		sNote = note;
	}
	
	public void Stampa()
	{
	    if (printJob.printDialog()) 
	    {
	    	try 
	    	{
	    		printJob.print();
	    	} 
	    	catch (Exception PrintException) 
	    	{
	    		PrintException.printStackTrace();
	    	}
	    }
	}
	
	public int print(Graphics g, PageFormat pageFormat, int page) {

	    int iY;
	    
	    //--- Validate the page number, we only print the first page
	    if (page == 0) 
	    {  
	    	/**
	    	 * Inizializza la stampa
	    	 */
	    	InitStampa(g, pageFormat);
	        
	        /**
	         * Stampo il bordo
	         */
	        StampaBordo(10, 10, g, pageFormat);
	        
	    	/**
	    	 * Stampo il titolo
	    	 */
	    	iY = StampaTitolo((int)titleX, 50, g, pageFormat);
	    	
	    	/**
	    	 * Stampo gli Ingredienti
	    	 */
	    	iY = StampaIngredienti(30, 150, g, pageFormat);
	        
	    	/**
	    	 * Stampo i Totali
	    	 */
	    	int iTmp = iY;
	    	iY = StampaTotali(30, iTmp + 40, g, pageFormat);
	
	    	/**
	    	 * Stampo i valori di riferimento
	    	 */
	    	StampaSoglie((int)(pageFormat.getImageableWidth() - 30 - 300), iTmp + 40, g, pageFormat);

	    	/**
	    	 * Stampo le note
	    	 */
	    	iY = StampaNote(30, iY, g, pageFormat);
	    	
	    	return (PAGE_EXISTS);
	    } 
	    else
	    {
	      return (NO_SUCH_PAGE);
	    }
	  }
	
	private void InitStampa(Graphics g, PageFormat pageFormat)
	{
		//--- Create a graphic2D object a set the default parameters
    	g2d = (Graphics2D) g;
    	g2d.setColor(Color.black);

    	//--- Translate the origin to be (0,0)
    	g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
    	
    	//--- Compute the horizontal center of the page
    	FontMetrics fontMetrics = g2d.getFontMetrics();
        titleX = (pageFormat.getImageableWidth() / 2) - (fontMetrics.stringWidth(sTitoloRicetta) / 2);
        
    	/**
    	 * Imposto le variabili di stampa
    	 */
    	iMargineBordo = 10;
    	iMargineTotali = 20;
	}
	
	private void StampaBordo(int X, int Y, Graphics g, PageFormat pageFormat)
	{
		//--- Draw a border arround the page
        Rectangle2D.Double border = new Rectangle2D.Double(X, Y, pageFormat
            .getImageableWidth() - (X * 2), pageFormat.getImageableHeight() - (Y * 2));
        g2d.draw(border);
	}
	
	private int StampaTitolo(int X, int Y, Graphics g, PageFormat pageFormat)
	{   
		g2d.drawString(sTitoloRicetta, X, Y);
		
		return Y + g2d.getFont().getSize();
	}
	
	/**
	 * Printa gli ingredienti presenti nella tableIng
	 * @param g
	 * @param pageFormat
	 * @return - coordinata Y oltre la quale è possibile printare altro
	 */
	private int StampaIngredienti(int X, int Y, Graphics g, PageFormat pageFormat)
	{
		int i;
		
		for (i=0; i < tableIng.getRowCount(); i++)
    	{
    		g2d.drawString(tableIng.getValueAt(i, 2).toString() + " \t\t " + tableIng.getValueAt(i, 1).toString(), X, (40 * i) + Y);
    	}
		
		return (i * 40) + Y;
	}
	
	private int StampaTotali(int X, int Y, Graphics g, PageFormat pageFormat)
	{
		Line2D.Double line = new Line2D.Double();
	    
	    /**
	     * Printo le 3 righe verticali della tabella dei Totali
	     */
	    line.setLine(X, Y, X, Y + (tableTot.getColumnCount()) * 30);
		g2d.draw(line);
		line.setLine(X + 100, Y, X + 100, Y + (tableTot.getColumnCount()) * 30);
		g2d.draw(line);
		line.setLine(X + 200, Y, X + 200, Y + (tableTot.getColumnCount()) * 30);
		g2d.draw(line);
		
		/**
		 * Printo le righe orizzontali della tabella Totali
		 */
		for (int i=0; i <= tableTot.getColumnCount(); i++)
		{
			line.setLine(X, Y + i * 30, X + 200, Y + i * 30);
			g2d.draw(line);
		}
		
		FontMetrics fontMetrics = g2d.getFontMetrics();
		/**
		 * Scrivo all'interno della tabella dei Totali
		 */
		for (int i=0; i < tableTot.getColumnCount(); i++)
		{
			g2d.drawString(tableTot.getColumnModel().getColumn(i).getHeaderValue().toString(), X + 20, Y + 20 + i * 30);
			
			int iTmp = (int)(50 - fontMetrics.stringWidth(tableTot.getValueAt(0, i).toString()) / 2);
			g2d.drawString(tableTot.getValueAt(0, i).toString(), X + 100 + iTmp, Y + 20 + i * 30);
		}
		
		return Y + (tableTot.getColumnCount()) * 30;
	}
	
	private int StampaSoglie(int X, int Y, Graphics g, PageFormat pageFormat)
	{
		if (tableSoglie == null)
		{
			return Y;
		}
		if (tableSoglie.getRowCount() == 0)
		{
			return Y;
		}
		Line2D.Double line = new Line2D.Double();
		
		/**
	     * Printo le 4 righe verticali della tabella dei Totali
	     */
	    line.setLine(X, Y, X, Y + (tableSoglie.getRowCount() + 1) * 30);
		g2d.draw(line);
		line.setLine(X + 100, Y, X + 100, Y + (tableSoglie.getRowCount() + 1) * 30);
		g2d.draw(line);
		line.setLine(X + 200, Y, X + 200, Y + (tableSoglie.getRowCount() + 1) * 30);
		g2d.draw(line);
		line.setLine(X + 300, Y, X + 300, Y + (tableSoglie.getRowCount() + 1) * 30);
		g2d.draw(line);
		
		/**
		 * Printo le righe orizzontali della tabella Totali
		 */
		for (int i=0; i <= tableSoglie.getRowCount() + 1; i++)
		{
			line.setLine(X, Y + i * 30, X + 300, Y + i * 30);
			g2d.draw(line);
		}
		
		FontMetrics fontMetrics = g2d.getFontMetrics();
		
		/**
		 * Scrivo l'header all'interno della tabella delle Soglie
		 */
		{
			g2d.drawString(tableSoglie.getColumnModel().getColumn(DBMgrWrap.SOGLIE_Composizione).getHeaderValue().toString(), X + 10 + (DBMgrWrap.SOGLIE_Composizione - 1) * 100, Y + 20);
			
			int iTmp = (int)(50 - fontMetrics.stringWidth(tableSoglie.getColumnModel().getColumn(DBMgrWrap.SOGLIE_Min).getHeaderValue().toString()) / 2);
			g2d.drawString(tableSoglie.getColumnModel().getColumn(DBMgrWrap.SOGLIE_Min).getHeaderValue().toString(), X + 100 + iTmp, Y + 20);
			
			int iTmp2 = (int)(50 - fontMetrics.stringWidth(tableSoglie.getColumnModel().getColumn(DBMgrWrap.SOGLIE_Max).getHeaderValue().toString()) / 2);
			g2d.drawString(tableSoglie.getColumnModel().getColumn(DBMgrWrap.SOGLIE_Max).getHeaderValue().toString(), X + 200 + iTmp2, Y + 20);
		}
		
		/**
		 * Scrivo i valori all'interno della tabella delle Soglie
		 */
		for (int i=0; i < tableSoglie.getRowCount(); i++)
		{	
			g2d.drawString(tableSoglie.getValueAt(i, DBMgrWrap.SOGLIE_Composizione).toString(), X + 10, Y + 20 + (i + 1) * 30);
			
			int iTmp = (int)(50 - fontMetrics.stringWidth(tableSoglie.getValueAt(i, DBMgrWrap.SOGLIE_Min).toString()) / 2);
			g2d.drawString(tableSoglie.getValueAt(i, DBMgrWrap.SOGLIE_Min).toString(), X + 100 + iTmp, Y + 20 + (i + 1) * 30);
			
			int iTmp2 = (int)(50 - fontMetrics.stringWidth(tableSoglie.getValueAt(i, DBMgrWrap.SOGLIE_Max).toString()) / 2);
			g2d.drawString(tableSoglie.getValueAt(i, DBMgrWrap.SOGLIE_Max).toString(), X + 200 + iTmp2, Y + 20 + (i + 1) * 30);
		}
		
		return Y + (tableSoglie.getRowCount() + 1) * 30;
	}
	
	private int StampaNote(int X, int Y, Graphics g, PageFormat pageFormat)
	{
		g2d.drawString("Note:", X, Y + 50);
		
		boolean bFinito = false;
		
		Y = Y + 80;
		String sTmp = sNote;
		
		while (bFinito == false)
		{
			int id = sTmp.indexOf("\n");
			if (id > 0)
			{
				g2d.drawString(sTmp.substring(0, id), X + 10, Y);
				sTmp = sTmp.substring(id + 1);
				Y = Y + 20;
			}
			else
			{
				if (sTmp != "")
				{
					g2d.drawString(sTmp, X + 10, Y);
					sTmp = "";
					Y = Y + 20;
				}
				bFinito = true;
			}
		}
		
		return Y + 30;
	}
}
