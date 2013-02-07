package MastroGelataioPackage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class SelezionaRicettaPanel extends JPanel {

	DBManager DBMgr;
	JLabel lblRicettario;
	TabellaGenerica tabellaRicette;
	JButton btnCancellaRicetta;
	JButton btnApriRicetta;
	Integer iRicettaViewed;
	
	private int listRic_X, listRic_Y, listRic_Width, listRic_Height;
	private int lblRic_X, lblRic_Y, lblRic_Width, lblRic_Height;
	
	protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();
	
	/**
	 * Create the panel.
	 */
	public SelezionaRicettaPanel(DBManager prtDBMgr) 
	{
		setLayout(null);
		
		/**
		 * Inizializza le variabili
		 */
		DBMgr = new DBManager();
		DBMgr = prtDBMgr;
		iRicettaViewed = -1;
		
		/**
		 * Modifica dati generali del pannello
		 */
		CalcolaCoordinate();
		
		/**
		 * Creo il titolo
		 */
		lblRicettario = new JLabel("Elenco Ricette");
		//GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		//String envfonts[] = gEnv.getAvailableFontFamilyNames();
		lblRicettario.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lblRicettario.setBounds(lblRic_X, lblRic_Y, lblRic_Width, lblRic_Height);
		lblRicettario.setHorizontalAlignment(SwingConstants.CENTER);
		lblRicettario.setVerticalAlignment(SwingConstants.TOP);
		add(lblRicettario);
		
		/**
		 * Creo la lista degli Ingredienti tramite una JTable
		 */
		tabellaRicette = new TabellaGenerica(this, listRic_X, listRic_Y, listRic_Width, listRic_Height);
		
		/**
		 * Creo il JButton per la cancellazione della ricetta selezionata
		 */
		btnCancellaRicetta = new JButton("Elimina");
		btnCancellaRicetta.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		add(btnCancellaRicetta);
		
		btnCancellaRicetta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnCancellaRicettaSelected();
			}
		});
		
		/**
		 * Creo il JButton per la cancellazione della ricetta selezionata
		 */
		btnApriRicetta = new JButton("Apri");
		btnApriRicetta.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		add(btnApriRicetta);
		
		btnApriRicetta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnApriRicettaSelected();
			}
		});
		
		/**
		 * Creo il listener per l'evento di selezione della ricetta
		 */
		tabellaRicette.addMioEventoListener(new MioEventoListener() {
		    public void myEventOccurred(MioEvento evt) {
		    	
		    	ApriRicetta();
		    }
		});
	}
	
	public void addMioEventoListener(MioEventoListener listener) 
    {
        listenerList.add(MioEventoListener.class, listener);
    }
	
	private void CalcolaCoordinate()
	{
		lblRic_X = 20;
		lblRic_Y = 10;
		lblRic_Width = getWidth() - ((listRic_X) * 2);
		lblRic_Height = 60;
		
		listRic_X = lblRic_X;
		listRic_Y = lblRic_Y + lblRic_Height;
		listRic_Width = getWidth() - ((listRic_X) * 2);
		listRic_Height = (int)(getHeight() * 0.40);
	}
	
	public void CambiaSize(int iX, int iY, int iWidth, int iHeight)
	{
		this.setBounds(iX, iY, iWidth, iHeight);
		this.setPreferredSize(new Dimension(iWidth, iHeight));
		CalcolaCoordinate();
		tabellaRicette.setBounds(listRic_X, listRic_Y, listRic_Width, listRic_Height);
		btnCancellaRicetta.setBounds(tabellaRicette.getX(), tabellaRicette.getY() + tabellaRicette.getHeight() + 10, 80, 40);
		btnApriRicetta.setBounds(tabellaRicette.getX() + tabellaRicette.getWidth() - 83, tabellaRicette.getY() + tabellaRicette.getHeight() + 10, 80, 40);
		lblRicettario.setBounds(lblRic_X, lblRic_Y, lblRic_Width, lblRic_Height);
	}
	
	public void CaricaRicette()
	{
		String[] sInvisibleColumns = new String[2];
		sInvisibleColumns[0] = "ID";
		sInvisibleColumns[1] = "Note";
		
		tabellaRicette.CaricaDati("Ricette", null, DBMgr);
		
		/**
		 * Visualizzo i dati
		 */
		tabellaRicette.setInvisibleColumn(sInvisibleColumns);
		tabellaRicette.setEditableColumns(null);
		tabellaRicette.MostraDati();
	}
	
	/**
	 * Funzione chiamata per aprire la ricetta selezionata
	 */
	private void ApriRicetta()
	{
		/**
		 * Ricavo l'ID della ricetta selezionata
		 */
		Integer iID;
		iID = Integer.parseInt(tabellaRicette.getSelectedValue("ID").toString());
		
		/**
		 * Ricavo i dati
		 */
		Vector<Vector<Object>> data_ing = new Vector<Vector<Object>>();
		data_ing = DBMgr.Select("Composizione", " WHERE ID=" + iID);
		
		/**
		 * Scateno l'evento per riempire la tabella di Composizione della ricetta
		 */
		fireMyEvent(new MioEvento(data_ing));	
		
		/**
		 * Memorizzo l'ID della ricetta correntemente vista 
		 */
		iRicettaViewed = iID;
	}
	
	private void fireMyEvent(MioEvento evt) 
	{
		Object[] listeners = listenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i=0; i<listeners.length; i+=2) 
        {
            if (listeners[i]==MioEventoListener.class) 
            {
                ((MioEventoListener)listeners[i+1]).myEventOccurred(evt);
            }
        }
	}
	
	private void btnCancellaRicettaSelected()
	{
		/**
		 * Se non è stata selezionata nessuna ricetta esco subito
		 */
		if (tabellaRicette.getSelectedValue("ID") == null)
		{
			return;
		}		
		
		/**
		 * UPDATE del DB
		 */
		Object ID = tabellaRicette.getSelectedValue("ID");
		
		/**
		 * Creo il vettore delle colonne su cui fare la selezione
		 */
		Vector<String> sColumns = new Vector<String>();
		sColumns.add("ID");
		
		/**
		 * Creo il vettore dei valori per fare la selezione
		 */
		Vector<Object> objValues = new Vector<Object>();
		objValues.add(ID);
		
		/**
		 * Ricavo la matrice "data" da salvare in tabella
		 */
		if (DBMgr.Delete("Ricette", sColumns, objValues) == false)
		{
			JOptionPane.showMessageDialog(this, "Impossibile eliminare la ricetta.", "Attenzione", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		/**
		 * Elimino la ricetta anche dalla tabella di Composizione delle ricette
		 */
		if (DBMgr.Delete("Composizione", sColumns, objValues) == false)
		{
			JOptionPane.showMessageDialog(this, "Impossibile eliminare completamente la ricetta. Contattare l'amministratore del sistema.", "Attenzione", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		/**
		 * Sulla base dell'ID selezionato aggiorno la tabella delle ricette e la visualizzazione
		 */
		tabellaRicette.RimuoviRowDati(ID, "ID");
		tabellaRicette.MostraDati();
		
		/**
		 * Scateno l'evento per svuotare la tabella di Composizione della ricetta se stavo
		 * visualizzando proprio quella cancellata
		 */
		if (iRicettaViewed == Integer.parseInt(ID.toString()))
		{
			Vector<Vector<Object>> obj = new Vector<Vector<Object>>();
			fireMyEvent(new MioEvento(obj));	
			iRicettaViewed = -1;
		}
	}
	
	private void btnApriRicettaSelected()
	{
		ApriRicetta();
	}
}
