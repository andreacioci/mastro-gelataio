package MastroGelataioPackage;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SelezionaRicettaPanel extends JPanel {

        private DBMgrWrap DBMgr;
        private JLabel lblRicettario;
        private TabellaGenerica tabellaRicette;
        private JButton btnCancellaRicetta;
        private JButton btnModificaRicetta;
        private JButton btnSalva;
        private Integer iRicettaViewed;
        
        private int listRic_X, listRic_Y, listRic_W, listRic_H;
        private int lblRic_X, lblRic_Y, lblRic_W, lblRic_H;
        
        private boolean bModificato;
        
        protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();
        
        /**
         * Create the panel.
         */
        public SelezionaRicettaPanel(DBMgrWrap prtDBMgr) 
        {
                setLayout(null);
                
                /**
                 * Inizializza le variabili
                 */
                DBMgr = new DBMgrWrap();
                DBMgr = prtDBMgr;
                iRicettaViewed = -1;
                bModificato = false;
                
                /**
                 * Modifica dati generali del pannello
                 */
                CalcolaCoordinate();
                
                /**
                 * Creo il titolo
                 */
                lblRicettario = new JLabel("Elenco Ricette");
                lblRicettario.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
                lblRicettario.setBounds(0, 0, getWidth(), 40);
                lblRicettario.setHorizontalAlignment(SwingConstants.CENTER);
                lblRicettario.setVerticalAlignment(SwingConstants.TOP);
                add(lblRicettario);
                
                /**
                 * Creo la lista degli Ingredienti tramite una JTable
                 */
                tabellaRicette = new TabellaGenerica(this, listRic_X, listRic_Y, listRic_W, listRic_H);
                
                /**
                 * Creo il JButton per la cancellazione della ricetta selezionata
                 */
                btnCancellaRicetta = new JButton("Elimina");
                btnCancellaRicetta.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                add(btnCancellaRicetta);
                
                btnCancellaRicetta.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent arg0) {
                                btnCancellaRicettaSelected();
                        }
                });
      
                /**
                 * Creo il JButton per la modifica della ricetta selezionata
                 */
                btnModificaRicetta = new JButton("Modifica");
                btnModificaRicetta.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                add(btnModificaRicetta);
                
                btnModificaRicetta.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent arg0) {
                                btnModificaRicettaSelected();
                        }
                });
                
                /**
                 * Creo il bottone per le modifiche alla lista delle ricette nel DB
                 */
                btnSalva = new JButton("Salva");
                btnSalva.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                add(btnSalva);
                
                btnSalva.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent arg0) {
                                Salva();        
                        }
                });
                
                /**
                 * Creo il listener per l'evento di selezione della ricetta.
                 * Questo evento viene scatenato dalla tabella stessa.
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
                lblRic_W = getWidth() - ((listRic_X) * 2);
                lblRic_H = 60;
                
                listRic_X = lblRic_X;
                listRic_Y = lblRic_Y + lblRic_H;
                listRic_W = getWidth() - ((listRic_X) * 2);
                listRic_H = (int)(getHeight() * 0.40);
        }
        
        public void CambiaSize(int iX, int iY, int iWidth, int iHeight)
        {
                this.setBounds(iX, iY, iWidth, iHeight);

                CalcolaCoordinate();
                tabellaRicette.setBounds(listRic_X, listRic_Y, listRic_W, listRic_H);
                btnCancellaRicetta.setBounds(listRic_X, listRic_Y + listRic_H + 10, Globals.BUTTON_WIDTH, Globals.BUTTON_HEIGHT);
                btnModificaRicetta.setBounds(listRic_X + listRic_W - Globals.BUTTON_WIDTH, listRic_Y + listRic_H + 10, Globals.BUTTON_WIDTH, Globals.BUTTON_HEIGHT);
                btnSalva.setBounds(iX + (iWidth - Globals.BUTTON_WIDTH)/2 , iY + iHeight - Globals.BUTTON_HEIGHT - 10, Globals.BUTTON_WIDTH, Globals.BUTTON_HEIGHT);
                lblRicettario.setBounds(lblRic_X, lblRic_Y, lblRic_W, lblRic_H);
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
                
                bModificato = false;
                
                /**
                 * Se non è la prima volta che carico il pannello potrebbe essere che c'era una ricetta precedentemente selezionata.
                 * Setto di nuovo la selezione e riapro la ricetta.
                 */
                if (iRicettaViewed != -1)
                {
                	tabellaRicette.setSelectedRow("ID", iRicettaViewed, false);
                	ApriRicetta();
                }
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
                MioEvento evt = new MioEvento(data_ing);
                evt.setMioEventoName("Apri");
                fireMyEvent(evt);       
                
                /**
                 * Memorizzo l'ID della ricetta correntemente vista 
                 */
                iRicettaViewed = iID;
        }
   
        /**
         * Funzione chiamata per modificare la ricetta selezionata.
         */
        private void ModificaRicetta()
        {
        		Integer iRow;
        	
	        	/**
		         * Ricavo la riga selezionata nella JTable 
		         */
		        iRow = tabellaRicette.getSelectedRow();
		                
		        if (iRow != -1)
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
	                MioEvento evt = new MioEvento(data_ing);
	                evt.setMioEventoName("Modifica");
	                fireMyEvent(evt);       
	                
	                /**
	                 * Memorizzo l'ID della ricetta correntemente vista 
	                 */
	                iRicettaViewed = iID;
		        }
        }
        
        /**
         * Funzione chiamata per salvare l'elenco delle ricette.
         */
        private void Salva()
        {   
        	/** 
        	 * Ricavo la matrice "data" da salvare nel DB
        	 */
        	Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        	data = tabellaRicette.GetDataVector();
        	
        	/**
             * UPDATE tabella Composizione nel DB
             */
            if (DBMgr.UpdateRicetteTable(data) == false)
            {
                    JOptionPane.showMessageDialog(null, "Impossibile salvare la ricetta.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                    return;
            }
            
            /**
             * Notifico all'utente che il salvataggio è avvenuto con successo
             */
            JOptionPane.showMessageDialog(null, "Salvataggio riuscito!", "Attenzione", JOptionPane.INFORMATION_MESSAGE);
            
            bModificato = false;
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
                Integer iRow, iDataRow; 
        
                /**
		         * Ricavo la riga selezionata nella JTable 
		         */
		        iRow = tabellaRicette.getSelectedRow();
		                
		        if (iRow != -1)
		        {
		                /**
		                 * Ricavo la riga selezionata nella matrice "data"
		                 */
		                iDataRow = tabellaRicette.getDataRowFromTableRow(iRow, "ID");
		                Object ID = tabellaRicette.getSelectedValue("ID");
		                
		                tabellaRicette.RimuoviRowDati(iDataRow);
		                tabellaRicette.MostraDati();
		                
		                /**
		                * Scateno l'evento per svuotare la tabella di Composizione della ricetta se stavo
		                * visualizzando proprio quella cancellata
		                */
		                if (iRicettaViewed == Integer.parseInt(ID.toString()))
		                {
		                	Vector<Vector<Object>> obj = new Vector<Vector<Object>>();
		                	MioEvento evt = new MioEvento(obj);
		                    evt.setMioEventoName("Elimina");
		                    fireMyEvent(evt);   
		                    
		                    iRicettaViewed = -1;
		                }
		                      
		                bModificato = true;
		        }
        }
   
        private void btnModificaRicettaSelected()
        {
                ModificaRicetta();
        }
        
        public boolean IsModified()
        {
        	return bModificato;
        }
}