package MastroGelataioPackage;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.border.EtchedBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class IngredientiPanel extends JPanel {

	private JPanel pnlTipo;
	private JPanel pnlIng;
	private TabellaGenerica tabellaTipiIng;
	private TabellaGenerica tabellaIngredienti;
	private JLabel lblIngredienti;
	private JLabel lblTipiIng;
	private JButton btnAggiungiIng;
	private JButton btnCancellaIng;
	private JButton btnAggiungiTipo;
	private JButton btnCancellaTipo;
	private DBMgrWrap DBMgr;
	private JButton btnSalva;
	private Integer iNextIDIng; 
	private Integer iNextIDTipo;
	private boolean bModificato;

	//protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();
	
	/**
	 * Create the panel.
	 */
	public IngredientiPanel(DBMgrWrap prtDBMgr) 
	{	
		DBMgr = new DBMgrWrap();
		DBMgr = prtDBMgr;
		
		bModificato = false;
		
		setLayout(null);
		
		/**
		 * Creo un JPanel solo per fare il riquadro delle classi di ingredienti
		 */
		pnlTipo = new JPanel();
		pnlTipo.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		pnlTipo.setLayout(null);
		add(pnlTipo);
		
		/**
		 * Creo un JPanel solo per fare il riquadro degli ingredienti
		 */
		pnlIng = new JPanel();
		pnlIng.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		pnlIng.setLayout(null);
		add(pnlIng);
		
		/**
		 * Creo il titolo
		 */
		lblIngredienti = new JLabel("Ingredienti");
		lblIngredienti.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		lblIngredienti.setHorizontalAlignment(JLabel.CENTER);
		pnlIng.add(lblIngredienti);
		
		/**
		 * Creo il titolo deii Tipi
		 */
		lblTipiIng = new JLabel("Classi di ingredienti");
		lblTipiIng.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		lblTipiIng.setHorizontalAlignment(JLabel.CENTER);
		pnlTipo.add(lblTipiIng);
		
		/**
		 * Creo la tabella dei tipi
		 */
		tabellaTipiIng = new TabellaGenerica(pnlTipo, 20, 120, 100, 100);
		
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
		 * Creo la tabella Ingredienti
		 */
		tabellaIngredienti = new TabellaGenerica(pnlIng, tabellaTipiIng.getX() + tabellaTipiIng.getWidth() + 40, 120, getWidth() - tabellaTipiIng.getWidth() - 80, tabellaTipiIng.getHeight());
		
		/**
		 * Bottone per aggiungere una riga nella tabella Tipi
		 */
		btnAggiungiTipo = new JButton("Aggiungi");
		btnAggiungiTipo.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		pnlTipo.add(btnAggiungiTipo);
		
		btnAggiungiTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AggiungiEmptyRowTipo();
			}
		});
		
		/**
		 * Bottone per aggiungere una riga nella tabella Tipi
		 */
		btnCancellaTipo = new JButton("Elimina");
		btnCancellaTipo.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		pnlTipo.add(btnCancellaTipo);
		
		btnCancellaTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CancellaRowTipo();
			}
		});
		
		/**
		 * Bottone per aggiungere una riga nella tabella Ingredienti
		 */
		btnAggiungiIng = new JButton("Aggiungi");
		btnAggiungiIng.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		pnlIng.add(btnAggiungiIng);
		
		btnAggiungiIng.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AggiungiEmptyRowIng();
			}
		});
		
		/**
		 * Bottone per cancellare una riga nella tabella Ingredienti
		 */
		btnCancellaIng = new JButton("Elimina");
		btnCancellaIng.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		pnlIng.add(btnCancellaIng);
		
		btnCancellaIng.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CancellaRowIng();
			}
		});
		
		/**
		 * Bottone per salvare le modifiche fatte
		 */
		btnSalva = new JButton("Salva");
		btnSalva.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		add(btnSalva);
		
		btnSalva.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Salva();
			}
		});
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
		int pnlIng_X, pnlIng_Y, pnlIng_W, pnlIng_H;
		int pnlTipo_X, pnlTipo_Y, pnlTipo_W, pnlTipo_H;
		int lblTipo_X, lblTipo_Y, lblTipo_W, lblTipo_H;
		int lblIng_X, lblIng_Y, lblIng_W, lblIng_H;
		int tabTipo_X, tabTipo_Y, tabTipo_W, tabTipo_H;
		int tabIng_X, tabIng_Y, tabIng_W, tabIng_H;
		int addTipo_X, addTipo_Y, addTipo_W, addTipo_H;
		int delTipo_X, delTipo_Y, delTipo_W, delTipo_H;
		int addIng_X, addIng_Y, addIng_W, addIng_H;
		int delIng_X, delIng_Y, delIng_W, delIng_H;
		int btnSave_X, btnSave_Y, btnSave_W, btnSave_H;
		
		setBounds(X, Y, Width, Height);
		
		pnlTipo_X = 0;
		pnlTipo_Y = 0;
		pnlTipo_W = 240;
		pnlTipo_H = getHeight() - 80;
		
		pnlIng_X = pnlTipo_X + pnlTipo_W;
		pnlIng_Y = pnlTipo_Y;
		pnlIng_W = getWidth() - pnlTipo_W;
		pnlIng_H = pnlTipo_H;
		
		lblTipo_X = 0;
		lblTipo_Y = 0;
		lblTipo_W = pnlTipo_W;
		lblTipo_H = 40;
		
		lblIng_X = 0;
		lblIng_Y = 0;
		lblIng_W = pnlIng_W;
		lblIng_H = 40;
		
		tabTipo_X = 20;
		tabTipo_Y = lblTipo_Y + lblTipo_H + 30;
		tabTipo_W = pnlTipo_W - 20 * 2;
		tabTipo_H = pnlTipo_H - tabTipo_Y - 60;
		
		tabIng_X = 20;
		tabIng_Y = lblIng_Y + lblIng_H + 30;
		tabIng_W = pnlIng_W - 20 * 2;
		tabIng_H = tabTipo_H;
		
		addTipo_X = 20;
		addTipo_Y = tabTipo_Y + tabTipo_H + 10;
		addTipo_W = Globals.BUTTON_WIDTH;
		addTipo_H = Globals.BUTTON_HEIGHT;
		
		delTipo_X = tabTipo_X + tabTipo_W - 90;
		delTipo_Y = tabTipo_Y + tabTipo_H + 10;
		delTipo_W = Globals.BUTTON_WIDTH;
		delTipo_H = Globals.BUTTON_HEIGHT;
		
		addIng_X = 20;
		addIng_Y = tabIng_Y + tabIng_H + 10;
		addIng_W = Globals.BUTTON_WIDTH;
		addIng_H = Globals.BUTTON_HEIGHT;
		
		delIng_X = addIng_X + addIng_W + 20;
		delIng_Y = tabIng_Y + tabIng_H + 10;
		delIng_W = Globals.BUTTON_WIDTH;
		delIng_H = Globals.BUTTON_HEIGHT;
		
		btnSave_X = pnlIng_X + pnlIng_W - Globals.BUTTON_WIDTH;
		btnSave_Y = pnlIng_Y + pnlIng_H + 10;
		btnSave_W = Globals.BUTTON_WIDTH;
		btnSave_H = Globals.BUTTON_HEIGHT;
		
		pnlTipo.setBounds(pnlTipo_X, pnlTipo_Y, pnlTipo_W, pnlTipo_H);
		pnlIng.setBounds(pnlIng_X, pnlIng_Y, pnlIng_W, pnlIng_H);
		lblTipiIng.setBounds(lblTipo_X, lblTipo_Y, lblTipo_W, lblTipo_H);
		lblIngredienti.setBounds(lblIng_X, lblIng_Y, lblIng_W, lblIng_H);
		tabellaTipiIng.setBounds(tabTipo_X, tabTipo_Y, tabTipo_W, tabTipo_H);
		tabellaIngredienti.setBounds(tabIng_X, tabIng_Y, tabIng_W, tabIng_H);
		btnAggiungiTipo.setBounds(addTipo_X, addTipo_Y, addTipo_W, addTipo_H);
		btnCancellaTipo.setBounds(delTipo_X, delTipo_Y, delTipo_W, delTipo_H);
		btnAggiungiIng.setBounds(addIng_X, addIng_Y, addIng_W, addIng_H);
		btnCancellaIng.setBounds(delIng_X, delIng_Y, delIng_W, delIng_H);
		
		btnSalva.setBounds(btnSave_X, btnSave_Y, btnSave_W, btnSave_H);
	}
	
	public boolean IsModified()
	{
		return bModificato;
	}
	
	/**
	 * Riempie le strutture dati ricavandoli dalla tabella Tipi_Ingredienti
	 */
	public void CaricaIngredienti()
	{	
		/**
		 * Riempio la tabella dei tipi
		 */
		CaricaTipiIng();
		tabellaTipiIng.MostraDati();
		
		/**
		 * Riempio la tabella degli Ingredienti
		 */
		CaricaDettagliIng();
	}
	
	public void CaricaTipiIng()
	{
		/**
		 * Ricavo l'header della tabella Ingredienti
		 */
		Vector<String> sColumnName = new Vector<String>();
		sColumnName = DBMgr.RicavaNomiColonne("Tipi_Ingredienti");
		tabellaTipiIng.setColumnName(sColumnName);
		
		/**
		 * Estraggo tutti gli Ingredienti dal DB
		 */
		tabellaTipiIng.CaricaDati("Tipi_Ingredienti", null, DBMgr);
		
		/**
		 * Ricavo l'ID più grande per determinare iNextIDTipo dei Tipi
		 */
		iNextIDTipo = tabellaTipiIng.getMaxInt("ID") + 1; 
		
		/**
		 * Setto le colonne invisibili
		 */
		tabellaTipiIng.setInvisibleColumn(new String[] {"ID", "Deleted"});
		
		/**
		 * Setto le colonne editabili
		 */
		tabellaTipiIng.setEditableColumns(new String[] {"ID", "Nome", "Deleted"});
		
		/**
		 * Setto il filtro per non mostrare le righe cancellate
		 */
		tabellaTipiIng.setRegexFilter(new String[] {"^(?!Y)"}, new String[] {"Deleted"});
		
		/**
		 * Marco che ciò che è stato modificato è salvato
		 */
		bModificato = false;
	}
	
	public void CaricaDettagliIng()
	{
		/**
		 * Ricavo l'header della tabella Ingredienti
		 */
		Vector<String> sColumnName = new Vector<String>();
		sColumnName = DBMgr.RicavaNomiColonne("Ingredienti");
		tabellaIngredienti.setColumnName(sColumnName);
		
		/**
		 * Estraggo tutti gli Ingredienti dal DB
		 */
		tabellaIngredienti.CaricaDati("Ingredienti", null, DBMgr);
		
		/**
		 * Ricavo l'ID più grande per determinare iNextIDIng degli Ingredienti
		 */
		iNextIDIng = tabellaIngredienti.getMaxInt("ID") + 1; 
		
		/**
		 * Riempio la tabella
		 */
		tabellaIngredienti.setInvisibleColumn(new String[] {"ID", "Deleted", "TipoIngID"});
		
		String[] sColumn = new String[sColumnName.size()];
		for (int i=0; i < sColumn.length; i++)
		{
			sColumn[i] = sColumnName.get(i);	
		}
		tabellaIngredienti.setEditableColumns(sColumn);
		
		/**
		 * Imposto la larghezza delle colonne
		 */
		String[] sWidthColumns = new String[] {"Acqua", "Zuccheri", "Grassi", "SLNG", "AltriSolidi"};
		Integer[] iWidthCols = new Integer[] {20, 20, 20, 20, 20};
		tabellaIngredienti.setColumnWidth(sWidthColumns, iWidthCols);
		
		/**
		 * Marco che ciò che è stato modificato è salvato
		 */
		bModificato = false;
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
	
	public void AggiungiEmptyRowTipo()
	{
		Integer iRow;
		
		iRow = tabellaTipiIng.AggiungiEmptyRow();
		tabellaTipiIng.setDataAt(iNextIDTipo, iRow, DBMgrWrap.TIPI_ING_ID);
		tabellaTipiIng.setDataAt("", iRow, DBMgrWrap.TIPI_ING_Nome);
		tabellaTipiIng.setDataAt("N", iRow, DBMgrWrap.TIPI_ING_Deleted);
		iNextIDTipo++;
		tabellaTipiIng.MostraDati();
		tabellaTipiIng.setSelectedRow(iRow);
		
		tabellaIngredienti.SvuotaDati();
		tabellaIngredienti.MostraDati();
		
		/**
		 * Marco che ciò che è stato modificato non è stato salvato
		 */
		bModificato = true;
	}
	
	public void CancellaRowTipo()
	{
		Integer iRow, iDataRow;
		
		/**
		 * Ricavo la riga selezionata nella JTable 
		 */
		iRow = tabellaTipiIng.getSelectedRow();
		
		if (iRow != -1)
		{
			/**
			 * Ricavo la riga selezionata nella matrice "data"
			 */
			iDataRow = tabellaTipiIng.getDataRowFromTableRow(iRow, "ID");
			
			/**
			 * Marco la riga come cancellata nella tabella Tipi
			 */
			tabellaTipiIng.setDataAt("Y", iDataRow, DBMgrWrap.TIPI_ING_Deleted);
			
			/**
			 * Marco la riga come cancellata nella tabella Ingredienti
			 */
			for (int i=0; i < tabellaIngredienti.getRowCount(); i++)
			{
				if (tabellaIngredienti.getValueAt(i, DBMgrWrap.INGREDIENTI_TipoIngID).equals(tabellaTipiIng.getSelectedValue("ID")))
				{
					tabellaIngredienti.setDataAt("Y", i, DBMgrWrap.INGREDIENTI_Deleted);
				}
			}
			
			/**
			 * Aggiorna la visualizzazione
			 */
			tabellaTipiIng.MostraDati();
			tabellaIngredienti.MostraDati();
			
			/**
			 * Marco che ciò che è stato modificato non è stato salvato
			 */
			bModificato = true;
		}			
	}
	
	public void AggiungiEmptyRowIng()
	{
		Integer iTmp;
		
		/**
		 * Ricavo la riga selezionata nella JTable 
		 */
		iTmp = tabellaTipiIng.getSelectedRow();
		
		if (iTmp == -1)
		{
			JOptionPane.showMessageDialog(this, "Selezionare la tipologia prima di aggiungere l'ingrediente.", "Attenzione", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		Integer iRow;
		
		iRow = tabellaIngredienti.AggiungiEmptyRow();
		tabellaIngredienti.setDataAt(iNextIDIng, iRow, DBMgrWrap.INGREDIENTI_ID);
		tabellaIngredienti.setDataAt("", iRow, DBMgrWrap.INGREDIENTI_Nome);
		tabellaIngredienti.setDataAt(0, iRow, DBMgrWrap.INGREDIENTI_Acqua);
		tabellaIngredienti.setDataAt(0, iRow, DBMgrWrap.INGREDIENTI_Zuccheri);
		tabellaIngredienti.setDataAt(0, iRow, DBMgrWrap.INGREDIENTI_Grassi);
		tabellaIngredienti.setDataAt(0, iRow, DBMgrWrap.INGREDIENTI_SLNG);
		tabellaIngredienti.setDataAt(0, iRow, DBMgrWrap.INGREDIENTI_AltriSolidi);
		tabellaIngredienti.setDataAt("N", iRow, DBMgrWrap.INGREDIENTI_Deleted);
		tabellaIngredienti.setDataAt(Long.parseLong(tabellaTipiIng.getSelectedValue("ID").toString()), iRow, DBMgrWrap.INGREDIENTI_TipoIngID);
		tabellaIngredienti.setDataAt(0, iRow, DBMgrWrap.INGREDIENTI_POD);
		tabellaIngredienti.setDataAt(0, iRow, DBMgrWrap.INGREDIENTI_PAC);
		iNextIDIng++;
		tabellaIngredienti.MostraDati();
		
		/**
		 * Marco che ciò che è stato modificato non è stato salvato
		 */
		bModificato = true;
	}
	
	/**
	 * Marca come Deleted la riga selezionata dall'utente
	 */
	private void CancellaRowIng()
	{
		Integer iRow, iDataRow;
		
		/**
		 * Ricavo la riga selezionata nella JTable 
		 */
		iRow = tabellaIngredienti.getSelectedRow();
		
		if (iRow != -1)
		{
			/**
			 * Ricavo la riga selezionata nella matrice "data"
			 */
			iDataRow = tabellaIngredienti.getDataRowFromTableRow(iRow, "ID");
			
			/**
			 * Marco la riga come cancellata
			 */
			tabellaIngredienti.setDataAt("Y", iDataRow, tabellaIngredienti.getColumnIndex("Deleted"));
			
			/**
			 * Aggiorna la visualizzazione
			 */
			tabellaIngredienti.MostraDati();
			
			/**
			 * Marco che ciò che è stato modificato non è stato salvato
			 */
			bModificato = true;
		}		
	}
	
	/**
	 * Salva nel DB
	 */
	public void Salva()
	{
		/**
		 * Salvo nella tabella Tipi
		 */
		SalvaTipo();
		
		/**
		 * Salvo nella tabella Ingredienti
		 */
		SalvaIng();
	}
	
	private void SalvaTipo()
	{
		/**
		 * Ricavo la matrice "data"
		 */
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		data = tabellaTipiIng.GetDataVector();
		
		if (DBMgr.UpdateTipiIng(data) == false)
		{
			JOptionPane.showMessageDialog(this, "Si sono verificati problemi durante il salvataggio. Contattare l'amministratore di sistema.", "Attenzione", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void SalvaIng()
	{
		/**
		 * Ricavo la matrice "data"
		 */
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		data = tabellaIngredienti.GetDataVector();
		
		/**
		 * UPDATE del DB
		 */
		if (DBMgr.UpdateAllIngredienti(data) == false)
		{
			JOptionPane.showMessageDialog(this, "Si sono verificati problemi durante il salvataggio. Contattare l'amministratore di sistema.", "Attenzione", JOptionPane.WARNING_MESSAGE);
		}
		else
		{
			/**
			 * Marco che ciò che è stato modificato è stato salvato
			 */
			bModificato = false;	
		}
	}
	
	private Vector<Ingredienti> Object2Ingredienti(Vector<Vector<Object>> data)
	{
		Vector<Object> data_obj;
		Vector<Ingredienti> output = new Vector<Ingredienti>();
		
		for (int i=0; i < data.size(); i++)
		{
			/**
			 * Copio la riga su un vettore di appoggio
			 */
			data_obj = data.get(i);
			
			/**
			 * Copio dal vettore di appoggio ad un oggetto Ingredienti
			 */
			Ingredienti ing = new Ingredienti();
			ing.setID(Integer.parseInt(data_obj.get(0).toString()));
			if (data_obj.get(1) == null)
			{
				data_obj.set(1, "Senza Nome");
			}
			ing.setName(data_obj.get(1).toString());
			if (data_obj.get(2) == null)
			{
				data_obj.set(2, 0);
			}
			ing.setAcqua(Double.parseDouble(data_obj.get(2).toString()));
			if (data_obj.get(3) == null)
			{
				data_obj.set(3, 0);
			}
			ing.setZuccheri(Double.parseDouble(data_obj.get(3).toString()));
			if (data_obj.get(4) == null)
			{
				data_obj.set(4, 0);
			}
			ing.setGrassi(Double.parseDouble(data_obj.get(4).toString()));
			if (data_obj.get(5) == null)
			{
				data_obj.set(5, 0);
			}
			ing.setSLNG(Double.parseDouble(data_obj.get(5).toString()));
			if (data_obj.get(6) == null)
			{
				data_obj.set(6, 0);
			}
			ing.setAltriSolidi(Double.parseDouble(data_obj.get(6).toString()));
			if (data_obj.get(7) == null)
			{
				data_obj.set(7, "N");
			}
			ing.setDeleted(data_obj.get(7).toString());
			if (data_obj.get(9) == null)
			{
				data_obj.set(9, 0);
			}
			ing.setPOD(Long.parseLong(data_obj.get(9).toString()));
			if (data_obj.get(10) == null)
			{
				data_obj.set(10, 0);
			}
			ing.setPAC(Long.parseLong(data_obj.get(10).toString()));

			/**
			 * Inserisco il vettore Ingredienti nel vettore di uscita
			 */
			output.add(ing);
		}
		return output;
	}
}
