package MastroGelataioPackage;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class RicettarioPanel extends JPanel {

	DBMgrWrap DBMgr;
	SelezionaRicettaPanel pnlSelectRicetta;
	RicettaPanel pnlRicettaPanel;	
	
	private int pnlSel_X, pnlSel_Y, pnlSel_Width, pnlSel_Height;
	private int pnlRic_X, pnlRic_Y, pnlRic_Width, pnlRic_Height;
	
	/**
	 * Create the panel.
	 */
	public RicettarioPanel(DBMgrWrap prtDBMgr) 
	{
		setLayout(null);
		
		DBMgr = new DBMgrWrap();
		DBMgr = prtDBMgr;
		
		CalcolaCoordinate();
		
		/**
		 * Creo il JPanel della lista degli Ingredienti
		 */
		pnlSelectRicetta = new SelezionaRicettaPanel(prtDBMgr);
		pnlSelectRicetta.setBounds(pnlSel_X, pnlSel_Y, pnlSel_Width, pnlSel_Height);
		pnlSelectRicetta.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		pnlSelectRicetta.setPreferredSize(new Dimension(pnlSel_Width, pnlSel_Height));
		add(pnlSelectRicetta);
		
		/**
		 * Creo il JPanel della composizione della Ricetta
		 */
		pnlRicettaPanel = new RicettaPanel(prtDBMgr);
		pnlRicettaPanel.setBounds(pnlRic_X, pnlRic_Y, pnlRic_Width, pnlRic_Height);
		pnlRicettaPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		pnlRicettaPanel.setPreferredSize(new Dimension(pnlRic_Width, pnlRic_Height));
		add(pnlRicettaPanel);
		
		/**
		 * Creo il listener per l'evento di selezione della ricetta
		 */
		pnlSelectRicetta.addMioEventoListener(new MioEventoListener() {
		    public void myEventOccurred(MioEvento evt) {
		    	
		    	ApriRicetta(evt);
		    }
		});
	}
	
	private void CalcolaCoordinate()
	{
		pnlSel_X = 10;
		pnlSel_Y = 30;
		pnlSel_Width = 250;
		pnlSel_Height = (int)(getHeight() * 0.60);
		
		pnlRic_X = pnlSel_X + pnlSel_Width + 20;
		pnlRic_Y = pnlSel_Y;
		pnlRic_Width = getWidth() - pnlRic_X - 20;
		pnlRic_Height = getHeight() - pnlRic_Y - 50;
	}
	
	/**
	 * Invocata quando cambia la dimensione della finestra
	 * @param X - upperleft corner
	 * @param Y - upperleft corner
	 * @param Width 
	 * @param Height
	 */
	public void CambiaSize(int X, int Y, int Width, int Height)
	{
		this.setBounds(X, Y, Width, Height);
		CalcolaCoordinate();
		pnlSelectRicetta.CambiaSize(pnlSel_X, pnlSel_Y, pnlSel_Width, pnlSel_Height);
		pnlRicettaPanel.CambiaSize(pnlRic_X, pnlRic_Y, pnlRic_Width, pnlRic_Height);
	}

	public void CaricaRicette()
	{
		pnlSelectRicetta.CaricaRicette();
		pnlRicettaPanel.CaricaPannello();
	}
	
	private void ApriRicetta(MioEvento evt)
	{
		Vector<String> sColumns = new Vector<String>();
		sColumns.add("ID");
		sColumns.add("ID_Ing");
		sColumns.add("Quantità");
		
		pnlRicettaPanel.ApriRicetta(evt.getSourceVector(), sColumns);	
	}
}
