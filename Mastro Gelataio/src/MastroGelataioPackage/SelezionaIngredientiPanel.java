package MastroGelataioPackage;

import java.awt.Dimension;
import java.awt.Font;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

public class SelezionaIngredientiPanel extends JPanel {

	private JLabel lblTipi;
	private JLabel lblIngredienti;
	private TabellaGenerica tabellaTipiIng;
	private TabellaGenerica tabellaIngredienti;
	private JButton btnInserisci;
	private int btnIns_X, btnIns_Y, btnIns_Width, btnIns_Height;
	private int btnRim_X, btnRim_Y, btnRim_Width, btnRim_Height;
	private JButton btnRimuovi;
	private DBManager DBMgr;
	
	private int listTip_X, listTip_Y, listTip_Width, listTip_Height;
	private int listIng_X, listIng_Y, listIng_Width, listIng_Height;
	private int lblTip_X, lblTip_Y, lblTip_Width, lblTip_Height;
	private int lblIng_X, lblIng_Y, lblIng_Width, lblIng_Height;
	
	protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();
	
	/**
	 * Create the panel.
	 */
	public SelezionaIngredientiPanel(DBManager prtDBMgr) 
	{
		/**
		 * Inizializza le variabili
		 */
		DBMgr = new DBManager();
		DBMgr = prtDBMgr;
		
		/**
		 * Modifica dati generali del pannello
		 */
		setLayout(null);
		this.setMinimumSize(new Dimension(70, 50));
		
		CalcolaCoordinate();
		
		/**
		 * Creo il titolo della tabella Tipi
		 */
		lblTipi = new JLabel("Classi di ingredienti");
		lblTipi.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		lblTipi.setHorizontalAlignment(JLabel.CENTER);
		lblTipi.setVerticalAlignment(JLabel.TOP);
		add(lblTipi);
		
		/**
		 * Creo il titolo della tabella Ingredienti
		 */
		lblIngredienti = new JLabel("Elenco Ingredienti");
		lblIngredienti.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		lblIngredienti.setHorizontalAlignment(JLabel.CENTER);
		lblIngredienti.setVerticalAlignment(JLabel.TOP);
		add(lblIngredienti);
		
		/**
		 * Creo la lista dei Tipi
		 */
		tabellaTipiIng = new TabellaGenerica(this, listTip_X, listTip_Y, listTip_Width, listTip_Height);
		
		tabellaTipiIng.addMioEventoListener(new MioEventoListener() {
		    public void myEventOccurred(MioEvento evt) 
		    {	
		    	/**
		    	 * Devo inserire il try perchè il casting su MouseEvent potrebbe generare un eccezione
		    	 * se evt non è un MouseEvent
		    	 */
		    	try
		    	{
		    		MouseEvent e = (MouseEvent) evt.getSourceObject();
		    		if (e.getClickCount() == 1)
			    	{
			    		TipoSelected();
			    	}
		    	}
		    	catch (Exception exc)
		    	{
		    	}
		    }
		});
		
		/**
		 * Creo la lista degli Ingredienti tramite una JTable
		 */
		tabellaIngredienti = new TabellaGenerica(this, listIng_X, listIng_Y, listIng_Width, listIng_Height);

		tabellaIngredienti.addMioEventoListener(new MioEventoListener() {
		    public void myEventOccurred(MioEvento evt) {
		    	
		    	/**
		    	 * Devo inserire il try perchè il casting su MouseEvent potrebbe generare un eccezione
		    	 * se evt non è un MouseEvent
		    	 */
		    	try
		    	{
		    		MouseEvent e = (MouseEvent) evt.getSourceObject();
		    		if (e.getClickCount() == 2)
			    	{
		    			btnInserisciSelected();
			    	}
		    	}
		    	catch (Exception exc)
		    	{
		    	}
		    }
		});
		
		/**
		 * Creo il JButton per Inserire un ingrediente
		 */
		btnInserisci = new JButton(">>");
		btnInserisci.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		btnInserisci.setBounds(btnIns_X, btnIns_Y, btnIns_Width, btnIns_Height);
		add(btnInserisci);
		
		btnInserisci.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnInserisciSelected();
			}
		});
		
		/**
		 * Creo il JButton di Rimuovere un ingrediente
		 */
		btnRimuovi = new JButton("<<");
		btnRimuovi.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		btnRimuovi.setBounds(btnRim_X, btnRim_Y, btnRim_Width, btnRim_Height);
		add(btnRimuovi);
		
		btnRimuovi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnRimuoviSelected();
			}
		});
	}
	
	public void addMioEventoListener(MioEventoListener listener) 
    {
        listenerList.add(MioEventoListener.class, listener);
    }
	
	private void CalcolaCoordinate()
	{
		btnIns_Width = 50;
		btnIns_Height = 50;
		
		btnRim_Width = 50;
		btnRim_Height = 50;
		
		lblTip_X = 20;
		lblTip_Y = 10;
		lblTip_Width = getWidth() - ((lblIng_X) * 3) - btnIns_Width;
		lblTip_Height = 40;
		
		listTip_X = 20;
		listTip_Y = lblTip_Y + lblTip_Height;
		listTip_Width = getWidth() - (listTip_X * 3) - btnIns_Width;
		listTip_Height = (int)(getHeight() * 0.30);
								
		lblIng_X = 20;
		lblIng_Y = listTip_Y + listTip_Height + 40;
		lblIng_Width = getWidth() - ((lblIng_X) * 3) - btnIns_Width;
		lblIng_Height = 40;
		
		listIng_X = 20;
		listIng_Y = lblIng_Y + lblIng_Height;
		
		listIng_Width = getWidth() - (listIng_X * 3) - btnIns_Width;
		listIng_Height = (int)(getHeight() * 0.40);
		
		btnIns_X = listIng_X * 2 + listIng_Width;
		btnIns_Y = listIng_Y + 10;
		
		btnRim_X = btnIns_X;
		btnRim_Y = btnIns_Y + btnIns_Width + 10;
	}
	
	public void CambiaSize(int iX, int iY, int iWidth, int iHeight)
	{
		this.setBounds(iX, iY, iWidth, iHeight);
		this.setPreferredSize(new Dimension(iWidth, iHeight));
		CalcolaCoordinate();
		lblTipi.setBounds(lblTip_X, lblTip_Y, lblTip_Width, lblTip_Height);
		lblIngredienti.setBounds(lblIng_X, lblIng_Y, lblIng_Width, lblIng_Height);
		tabellaTipiIng.setBounds(listTip_X, listTip_Y, listTip_Width, listTip_Height);
		tabellaIngredienti.setBounds(listIng_X, listIng_Y, listIng_Width, listIng_Height);
		btnInserisci.setBounds(btnIns_X, btnIns_Y, btnIns_Width, btnIns_Height);
		btnRimuovi.setBounds(btnRim_X, btnRim_Y, btnRim_Width, btnRim_Height);
	}
	
	public void CaricaIngredienti()
	{
		/**
		 * Carico la tabella dei Tipi
		 */
		CaricaTipiIng();
		tabellaTipiIng.MostraDati();
		
		/**
		 * Carico la tabella degli Ingredienti
		 */
		CaricaIng();
	}
	
	private void CaricaTipiIng()
	{
		/**
		 * Ricavo l'header della tabella Ingredienti
		 */
		Vector<String> sColumnName = new Vector<String>();
		sColumnName = DBMgr.RicavaNomiColonne(DBMgrWrap.TABELLA_TIPI_ING);
		tabellaTipiIng.setColumnName(sColumnName);
		
		/**
		 * Estraggo tutti gli Ingredienti dal DB
		 */
		tabellaTipiIng.CaricaDati(DBMgrWrap.TABELLA_TIPI_ING, null, DBMgr);
		
		/**
		 * Setto le colonne invisibili
		 */
		tabellaTipiIng.setInvisibleColumn(new String[] {"ID", "Deleted"});
		
		/**
		 * Setto il filtro per non mostrare le righe cancellate
		 */
		tabellaTipiIng.setRegexFilter(new String[] {"^(?!Y)"}, new String[] {"Deleted"});
	}
	
	private void CaricaIng()
	{
		/**
		 * Ricavo l'header della tabella Ingredienti
		 */
		Vector<String> sColumnName = new Vector<String>();
		sColumnName = DBMgr.RicavaNomiColonne(DBMgrWrap.TABELLA_INGREDIENTI);
		tabellaIngredienti.setColumnName(sColumnName);
		
		/**
		 * Estraggo tutti gli Ingredienti dal DB
		 */
		tabellaIngredienti.CaricaDati(DBMgrWrap.TABELLA_INGREDIENTI, null, DBMgr);
		
		/**
		 * Setto le colonne invisibili
		 */
		String[] sInvisibleColumns = new String[8];
		sInvisibleColumns[0] = "ID";
		sInvisibleColumns[1] = "Acqua";
		sInvisibleColumns[2] = "Zuccheri";
		sInvisibleColumns[3] = "Grassi";
		sInvisibleColumns[4] = "SLNG";
		sInvisibleColumns[5] = "AltriSolidi";
		sInvisibleColumns[6] = "Deleted";
		sInvisibleColumns[7] = "TipoIngID";
		
		tabellaIngredienti.setInvisibleColumn(sInvisibleColumns);
		
		/**
		 * Setto le colonne editabili
		 */
		tabellaIngredienti.setEditableColumns(null);
		
		/**
		 * Imposto il filtro per la visualizzazione delle righe
		 */
		tabellaIngredienti.setRegexFilter(new String[] {"^(?!Y)"}, new String[] {"Deleted"});
	}
	
	private void TipoSelected()
	{
		FiltraTabellaIngredienti(Long.parseLong(tabellaTipiIng.getSelectedValue("ID").toString()));
		tabellaIngredienti.MostraDati();
	}
	
	private void FiltraTabellaIngredienti(Long lTipoID)
	{
		tabellaIngredienti.setRegexFilter(new String[] {"^(?!Y)", lTipoID.toString()}, new String[] {"Deleted", "TipoIngID"});
	}
	
	private void btnInserisciSelected()
	{
		/**
		 * Controllo se è stato selezionato un ingrediente
		 */
		int i = 0;
		boolean bFound = false;
		while ((i < tabellaIngredienti.getRowCount()) && (bFound == false))
		{
			if (tabellaIngredienti.IsRowSelected(i) == true)
			{
				bFound = true;
			}
			i++;
		}
		
		if (bFound == true)
		{
			Long iID;
			iID = Long.parseLong(tabellaIngredienti.getSelectedValue("ID").toString());
			
			fireMyEvent(new MioEvento(iID));
		}
	}
	
	private void btnRimuoviSelected()
	{	
		fireMyEvent(new MioEvento("Rimuovi"));
	}
	
	void fireMyEvent(MioEvento evt) 
	{
		Object[] listeners = listenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i=0; i<listeners.length; i+=2) {
            if (listeners[i]==MioEventoListener.class) {
                ((MioEventoListener)listeners[i+1]).myEventOccurred(evt);
            }
        }
	}
}