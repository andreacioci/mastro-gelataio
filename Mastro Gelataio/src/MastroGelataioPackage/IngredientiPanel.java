package MastroGelataioPackage;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
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
		add(pnlTipo);
		
		/**
		 * Creo un JPanel solo per fare il riquadro degli ingredienti
		 */
		pnlIng = new JPanel();
		pnlIng.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		add(pnlIng);
		
		/**
		 * Creo il titolo
		 */
		lblIngredienti = new JLabel("Ingredienti");
		lblIngredienti.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		lblIngredienti.setHorizontalAlignment(JLabel.CENTER);
		add(lblIngredienti);
		
		/**
		 * Creo il titolo deii Tipi
		 */
		lblTipiIng = new JLabel("Classi di ingredienti");
		lblTipiIng.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		lblTipiIng.setHorizontalAlignment(JLabel.CENTER);
		add(lblTipiIng);
		
		/**
		 * Creo la tabella dei tipi
		 */
		tabellaTipiIng = new TabellaGenerica(this, 20, 120, 200, (int)(getHeight() * 0.40));
		
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
		tabellaIngredienti = new TabellaGenerica(this, tabellaTipiIng.getX() + tabellaTipiIng.getWidth() + 40, 120, getWidth() - tabellaTipiIng.getWidth() - 80, tabellaTipiIng.getHeight());
		
		/**
		 * Bottone per aggiungere una riga nella tabella Tipi
		 */
		btnAggiungiTipo = new JButton("Aggiungi");
		btnAggiungiTipo.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		add(btnAggiungiTipo);
		
		btnAggiungiTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AggiungiEmptyRowTipo();
			}
		});
		
		/**
		 * Bottone per aggiungere una riga nella tabella Tipi
		 */
		btnCancellaTipo = new JButton("Elimina");
		btnCancellaTipo.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		add(btnCancellaTipo);
		
		btnCancellaTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CancellaRowTipo();
			}
		});
		
		/**
		 * Bottone per aggiungere una riga nella tabella Ingredienti
		 */
		btnAggiungiIng = new JButton("Aggiungi");
		btnAggiungiIng.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		add(btnAggiungiIng);
		
		btnAggiungiIng.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AggiungiEmptyRowIng();
			}
		});
		
		/**
		 * Bottone per cancellare una riga nella tabella Ingredienti
		 */
		btnCancellaIng = new JButton("Elimina");
		btnCancellaIng.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		add(btnCancellaIng);
		
		btnCancellaIng.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CancellaRowIng();
			}
		});
		
		/**
		 * Bottone per salvare le modifiche fatte
		 */
		btnSalva = new JButton("Salva");
		btnSalva.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
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
		setBounds(X, Y, Width, Height);
		lblTipiIng.setBounds(20, 70, 200, 40);
		lblIngredienti.setBounds(260, 70, Width - 260 - 20, 30);
		tabellaTipiIng.setBounds(20, 120, 200, (int)(Height * 0.40));
		tabellaIngredienti.setBounds(tabellaTipiIng.getX() + tabellaTipiIng.getWidth() + 40, 120, getWidth() - tabellaTipiIng.getWidth() - 80, tabellaTipiIng.getHeight());
		btnAggiungiTipo.setBounds(20, tabellaIngredienti.getY() + tabellaIngredienti.getHeight() + 10, 90, 40);
		btnCancellaTipo.setBounds(130, tabellaIngredienti.getY() + tabellaIngredienti.getHeight() + 10, 90, 40);
		btnAggiungiIng.setBounds(tabellaIngredienti.getX(), tabellaIngredienti.getY() + tabellaIngredienti.getHeight() + 10, 90, 40);
		btnCancellaIng.setBounds(tabellaIngredienti.getX() + 110, tabellaIngredienti.getY() + tabellaIngredienti.getHeight() + 10, 90, 40);
		pnlTipo.setBounds(0, 0, 240, btnAggiungiTipo.getY() + btnAggiungiTipo.getHeight() + 40);
		pnlIng.setBounds(240, 0, Width - 240, btnAggiungiIng.getY() + btnAggiungiIng.getHeight() + 40);
		btnSalva.setBounds(pnlIng.getX() + pnlIng.getWidth() - 110, pnlIng.getY() + pnlIng.getHeight() + 30, 90, 40);
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

			/**
			 * Inserisco il vettore Ingredienti nel vettore di uscita
			 */
			output.add(ing);
		}
		return output;
	}
}
