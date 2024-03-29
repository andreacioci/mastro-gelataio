package MastroGelataioPackage;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class RicettaComposizionePanel extends JPanel {

        private JLabel lblRicetta;
        private JTextField txtNomeRicetta;
        private JLabel lblComposizione;
        private TabellaGenerica tabellaComposizione;
        private JLabel lblTotali;
        private TabellaGenerica tabellaTotali;
        private TabellaGenerica tabellaSoglie;
        private JButton btnRicalcola;
        private JTextField txtRicalcola;
        private JButton btnStampa;
        private JButton btnSalva;
        private JLabel lblNote;
        private JScrollPane scrollPane;
        private JTextArea txtNote;
        private DBMgrWrap DBMgr;
        private Vector<Ingredienti> data_ing; 
        private Vector<String> sColumnNameComp;
        private Vector<String> sColumnNameTot;
        private Vector<String> sColumnNameSoglie;
        private JLabel lblBasi;
        private JComboBox<String> cbbTipoBase;
        private DefaultComboBoxModel<String> cbbModel;
        private Vector<Vector<Object>> objTipiBasi;
        private Integer iIDRicettaViewed;
        private Integer iIDBaseViewed;
        private boolean bCbbListenerActive;
        
        private int tblCom_X, tblCom_Y, tblCom_Width, tblCom_Height;
        private int tblTot_X, tblTot_Y, tblTot_Width, tblTot_Height;
        private int lblNom_X, lblNom_Y, lblNom_Width, lblNom_Height;
        private int lblCom_X, lblCom_Y, lblCom_Width, lblCom_Height;
        private int lblTot_X, lblTot_Y, lblTot_Width, lblTot_Height;
        private int txtNom_X, txtNom_Y, txtNom_Width, txtNom_Height;
        private int btnRic_X, btnRic_Y, btnRic_Width, btnRic_Height;
        private int btnSta_X, btnSta_Y, btnSta_Width, btnSta_Height;
        private int btnSal_X, btnSal_Y, btnSal_Width, btnSal_Height;
        private int tblSog_X, tblSog_Y, tblSog_Width, tblSog_Height;
        private int lblBas_X, lblBas_Y, lblBas_Width, lblBas_Height;
        private int cbbBas_X, cbbBas_Y, cbbBas_Width, cbbBas_Height;
        private int lblNot_X, lblNot_Y, lblNot_Width, lblNot_Height;
        private int txtNot_X, txtNot_Y, txtNot_Width, txtNot_Height;
        private int txtRic_X, txtRic_Y, txtRic_Width, txtRic_Height;
        
        /**
         * Create the panel.
         */
        public RicettaComposizionePanel(DBMgrWrap prtDBMgr) 
        {       
                setLayout(null);
                
                /**
                 * Inizializzo le variabili
                 */
                DBMgr = new DBMgrWrap();
                DBMgr = prtDBMgr;
                
                iIDRicettaViewed = -1;
                iIDBaseViewed = -1;
                bCbbListenerActive = true;
                
                CalcolaCoordinate();

                /**
                 * Creo la parte superiore
                 */
                CreaTopPart();
                
                /**
                 * Creo la parte centrale
                 */
                CreaCenterPart();
                
                /**
                 * Creo la parte in basso
                 */
                CreaBottomPart();
                
                /**
                 * Creo le colonne delle tabelle 
                 */
                CreoTabelleColumn();
                
                /**
                 * Carico le basi che ci sono nel DB dentro la JComboBox
                 */
                CaricaBasi();
        }

        private void CreaTopPart()
        {
                /**
                 * Creo la JLabel "Nome Ricetta"
                 */
                lblRicetta = new JLabel("Nome Ricetta");
                lblRicetta.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
                add(lblRicetta);
                
                /**
                 * Creo la JTextField per l'inserimento del nome della ricetta
                 */
                txtNomeRicetta = new JTextField();
                txtNomeRicetta.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
                txtNomeRicetta.setDocument(new FixedSizeDocument(35));
                add(txtNomeRicetta);
                
                /**
                 * Creo il bottone per il ricalcolo delle quantita'�
                 */
                btnRicalcola = new JButton("Ricalcola gr.");
                btnRicalcola.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                add(btnRicalcola);
                
                btnRicalcola.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent arg0) {
                                Ricalcola();    
                        }
                });
                
                /**
                 * Creo la JTextField per il ricalcolo delle quantita'�
                 */
                txtRicalcola = new JTextField();
                txtRicalcola.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
                txtRicalcola.setDocument(new FixedSizeDocument(5));
                add(txtRicalcola);
        }

        private void CreaCenterPart()
        {
                /**
                 * Carico gli Ingredienti in memoria 
                 */
                data_ing = new Vector<Ingredienti>();
                data_ing = DBMgr.SelectIngredienti("WHERE Deleted='N'");
                
                /**
                 * Creo la JLabel "Composizione"
                 */
                lblComposizione = new JLabel("Composizione:");
                lblComposizione.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
                add(lblComposizione);
                
                /**
                 * Creo la tabella della composizione della Ricetta
                 */
                tabellaComposizione = new TabellaGenerica(this, tblCom_X, tblCom_Y, tblCom_Width, tblCom_Height);
                
                /**
                 * Creo la JLabel "Totali"
                 */
                lblTotali = new JLabel("Totali:");
                lblTotali.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
                add(lblTotali);
                
                /**
                 * Creo la tabella dei totali 
                 */
                tabellaTotali = new TabellaGenerica(this, tblTot_X, tblTot_Y, tblTot_Width, tblTot_Height);
                
                /**
                 * Creo la JLabel per le Note
                 */
                 lblNote = new JLabel("Note:");
                 lblNote.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
                 add(lblNote);
                 
                /**
                 * Creo la JTextField per l'inserimento delle Note 
                 */
                txtNote = new JTextArea();
                txtNote.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
                txtNote.setLineWrap(true);
                
                /**
                 * Creo la JScrollPane
                 */
                scrollPane = new JScrollPane(txtNote);
                add(scrollPane);
                
                /**
                 * Creo il listener per l'evento di aggiornamento delle quantita'� in tabella
                 */
                tabellaComposizione.addMioEventoListener(new MioEventoListener() {
                    public void myEventOccurred(MioEvento evt) {
                        
                        if (evt.getSource().getClass().getName() == "java.awt.event.MouseEvent")
                        {
                                return;
                        }
                        /**
                         * Aggiorno la tabella di Composizione
                         */
                        CalcolaQuantity();      
                        tabellaComposizione.MostraDati();
                        
                        /**
                         * Aggiorno la tabella dei Totali
                         */
                        AggiornaTotali();
                        tabellaTotali.MostraDati();
                        
                        /**
                         * Aggiorno la colorazione della tabella dei Totali
                         */
                        ControlloSoglie();
                    }
                });
        }
        
        private void CreaBottomPart()
        {
                /**
                 * Creo il bottone per salvare la ricetta nel DB
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
                 * Creo il bottone per stampare la ricetta
                 */
                btnStampa = new JButton("Stampa");
                btnStampa.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                add(btnStampa);
                
                btnStampa.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent arg0) {
                                Stampa();       
                        }
                });
                
                /**
                 * Creo la JLabel "Valori di Controllo"
                 */
                lblBasi = new JLabel("Valori di Controllo:");
                lblBasi.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
                add(lblBasi);
                
                /**
                 * Creo la JComboBox per le Basi e la riempio con i le basi nel DB
                 */
                cbbTipoBase = new JComboBox<String>();
                cbbTipoBase.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                add(cbbTipoBase);
                
                cbbModel = new DefaultComboBoxModel<String>();
                cbbTipoBase.setModel(cbbModel);
                
                cbbTipoBase.addActionListener (new ActionListener () {
                    public void actionPerformed(ActionEvent e) {
                    	if (bCbbListenerActive)
                    	{
                    		CaricaTabellaSoglie();
                    	}
                    }
                });
                
                /**
                 * Creo la tabella delle soglie
                 */
                tabellaSoglie = new TabellaGenerica(this, tblSog_X, tblSog_Y, tblSog_Width, tblSog_Height);
        }
        
        private void CalcolaCoordinate()
        {       
                lblNom_X = 20;
                lblNom_Y = 20;
                lblNom_Width = 100;
                lblNom_Height = 23;
                
                txtNom_X = lblNom_X + lblNom_Width + 10;
                txtNom_Y = lblNom_Y;
                txtNom_Width = 250;
                txtNom_Height = 23;
                           
                lblCom_X = 20;
                lblCom_Y = lblNom_Height + 30;
                lblCom_Width = 100;
                lblCom_Height = 23;
                
                tblCom_X = 20;
                tblCom_Y = lblCom_Y + lblCom_Height;
                tblCom_Width = getWidth() - (tblCom_X * 2);
                tblCom_Height = 150;
                
                lblTot_X = 20;
                lblTot_Y = tblCom_Y + tblCom_Height + 10;
                lblTot_Width = 100;
                lblTot_Height = 23;
                
                tblTot_X = 20;
                tblTot_Y = lblTot_Y + lblTot_Height;
                tblTot_Width = tblCom_Width;
                tblTot_Height = 50; 
                                
                txtRic_Width = 50;
                txtRic_Height = 23;
                                
                btnRic_Y = 20;
                btnRic_Width = 130;
                btnRic_Height = 23;
                btnRic_X = tblTot_X + tblTot_Width - btnRic_Width - txtRic_Width - 10;
                
                txtRic_X = tblTot_X + tblTot_Width - txtRic_Width;
                txtRic_Y = btnRic_Y;
                
                lblNot_X = tblCom_X;
                lblNot_Y = tblTot_Y + tblTot_Height + 20;
                lblNot_Width = 50;
                lblNot_Height = 20;
                                                
                txtNot_X = tblCom_X;
                txtNot_Y = lblNot_Y + lblNot_Height + 10;
                txtNot_Width = tblTot_Width; 
                txtNot_Height = 60; 
                
                btnSal_Y = txtNot_Y + txtNot_Height + 20;
                btnSal_Width = 80;
                btnSal_Height = 40;
                btnSal_X = tblTot_X + tblTot_Width - Globals.BUTTON_WIDTH;
                
                btnSta_Y = btnSal_Y;
                btnSta_Width = 80;
                btnSta_Height = 40;
                btnSta_X = btnSal_X - Globals.BUTTON_WIDTH - 20;
                
                lblBas_X = txtNot_X;
                lblBas_Y = txtNot_Y + txtNot_Height + 20;;
                lblBas_Width = 200;
                lblBas_Height = 20;
                
                cbbBas_X = txtNot_X;
                cbbBas_Y = txtNot_Y + txtNot_Height + 40;
                cbbBas_Width = 120; 
                cbbBas_Height = 20;
                
                tblSog_X = tblCom_X;
                tblSog_Y = cbbBas_Y + cbbBas_Height + 10;
                tblSog_Width = 200;
                tblSog_Height = 130;
        }
        
        public void CambiaSize(int iX, int iY, int iWidth, int iHeight)
        {
                setBounds(iX, iY, iWidth, iHeight);
                setPreferredSize(new Dimension(iWidth, iHeight));
                CalcolaCoordinate();
                lblRicetta.setBounds(lblNom_X, lblNom_Y, lblNom_Width, lblNom_Height);
                txtNomeRicetta.setBounds(txtNom_X, txtNom_Y, txtNom_Width, txtNom_Height);
                lblComposizione.setBounds(lblCom_X, lblCom_Y, lblCom_Width, lblCom_Height);
                tabellaComposizione.setBounds(tblCom_X, tblCom_Y, tblCom_Width, tblCom_Height);
                lblTotali.setBounds(lblTot_X, lblTot_Y, lblTot_Width, lblTot_Height);
                tabellaTotali.setBounds(tblTot_X, tblTot_Y, tblTot_Width, tblTot_Height);
                tabellaSoglie.setBounds(tblSog_X, tblSog_Y, tblSog_Width, tblSog_Height);
                btnRicalcola.setBounds(btnRic_X, btnRic_Y, btnRic_Width, btnRic_Height);
                btnSalva.setBounds(btnSal_X, btnSal_Y, Globals.BUTTON_WIDTH, Globals.BUTTON_HEIGHT);
                btnStampa.setBounds(btnSta_X, btnSta_Y, Globals.BUTTON_WIDTH, Globals.BUTTON_HEIGHT);
                txtRicalcola.setBounds(txtRic_X, txtRic_Y, txtRic_Width, txtRic_Height);
                lblBasi.setBounds(lblBas_X, lblBas_Y, lblBas_Width, lblBas_Height);
                cbbTipoBase.setBounds(cbbBas_X, cbbBas_Y, cbbBas_Width, cbbBas_Height);
                lblNote.setBounds(lblNot_X,lblNot_Y, lblNot_Width, lblNot_Height);
                scrollPane.setBounds(txtNot_X, txtNot_Y, txtNot_Width, txtNot_Height);
                cbbTipoBase.setBounds(cbbBas_X, cbbBas_Y, cbbBas_Width, cbbBas_Height);
        }
        
        private void CreoTabelleColumn()
        {
                /**
                 * Creo le colonne della tabella di Composizione
                 */
                sColumnNameComp = new Vector<String>();  
                
                sColumnNameComp.add("ID");
                sColumnNameComp.add("Nome");
                sColumnNameComp.add("Quantita (g)");
                sColumnNameComp.add("Acqua (g)");
                sColumnNameComp.add("Zuccheri (g)");
                sColumnNameComp.add("Grassi (g)");
                sColumnNameComp.add("SLNG (g)");
                sColumnNameComp.add("AltriSolidi (g)");
                sColumnNameComp.add("Deleted");
                sColumnNameComp.add("POD");
                sColumnNameComp.add("PAC");
                
                tabellaComposizione.setColumnName(sColumnNameComp);
                
                tabellaComposizione.setInvisibleColumn(new String[] {"ID", "Deleted"});
                tabellaComposizione.setEditableColumns(new String[] {"Quantita (g)"});
                tabellaComposizione.setRegexFilter(null, null);

                Vector<String> sColorCols = new Vector<String>();
                sColorCols.add("Deleted");
                
                Vector<Object> sColorVals = new Vector<Object>();
                sColorVals.add("Y");
                
                tabellaComposizione.setColorEqualConditions(sColorCols, sColorVals);
                
                /**
                 * Creo le colonne della tabella dei Totali
                 */
                sColumnNameTot = new Vector<String>();  
                
                sColumnNameTot.add("Nome");
                sColumnNameTot.add("Quantita (g)");
                sColumnNameTot.add("Acqua (%)");
                sColumnNameTot.add("Zuccheri (%)");
                sColumnNameTot.add("Grassi (%)");
                sColumnNameTot.add("SLNG (%)");
                sColumnNameTot.add("AltriSolidi (%)");
                sColumnNameTot.add("POD");
                sColumnNameTot.add("PAC");
                
                tabellaTotali.setColumnName(sColumnNameTot);
                
                /**
                 * Creo le colonne della tabella delle Soglie
                 */
                sColumnNameSoglie = new Vector<String>();  
                sColumnNameSoglie.add("ID");
                sColumnNameSoglie.add("Composizione");
                sColumnNameSoglie.add("Min (%)");
                sColumnNameSoglie.add("Max (%)");
                
                tabellaSoglie.setColumnName(sColumnNameSoglie);
        }
        
        public void CaricaBasi()
        {
                objTipiBasi = null;
                
                objTipiBasi = new Vector<Vector<Object>>();
                
                objTipiBasi = DBMgr.Select("Basi", null);
                
                if (cbbModel.getSize() > 0)
                {
                        cbbModel.setSelectedItem(-1);
                        cbbModel.removeAllElements();
                }
                if (objTipiBasi != null)
                {       
                        for (int i=0; i < objTipiBasi.size(); i++)
                        {
                        		bCbbListenerActive = false;
                                cbbModel.addElement(objTipiBasi.get(i).get(1).toString());
                                bCbbListenerActive = true;
                                
                                /**
                                 * Se avevo gi� visualizzato una base, riapro il pannello con la stessa selezione di prima
                                 */
                                if (iIDBaseViewed == Integer.parseInt(objTipiBasi.get(i).get(0).toString()))
                        		{
                        			cbbTipoBase.setSelectedIndex(i);
                        		}
                                else if (iIDBaseViewed == -1)
                                {
                        			cbbTipoBase.setSelectedIndex(0);
                        		}
                        }
                }       
        }
        
        private void CaricaTabellaSoglie()
        {
                if (tabellaSoglie == null)
                {
                        return;
                }
                
                Integer iBase;
                
                iBase = cbbTipoBase.getSelectedIndex();
                
                /**
                 * Salvo la base selezionata nel caso in cui cambiassi pannello
                 */
                if (iBase >= 0)
                {
                	iIDBaseViewed = Integer.parseInt(objTipiBasi.get(iBase).get(0).toString());
                }
                
                /**
                 * Ripulisco la visualizzazione corrente
                 */
                tabellaSoglie.SvuotaDati();
                tabellaSoglie.MostraDati();
                
                /**
                 * Ricavo le soglie dal DB
                 */
                if ((objTipiBasi != null) && (objTipiBasi.size() != 0) && (iBase < objTipiBasi.size()) && (iBase != -1))
                {
                	if (iIDBaseViewed == -1)
                	{
                		tabellaSoglie.CaricaDati("Soglie", " WHERE ID=" + objTipiBasi.get(iBase).get(0), DBMgr);
                	}
                	else
                	{
                		tabellaSoglie.CaricaDati("Soglie", " WHERE ID=" + iIDBaseViewed, DBMgr);
                	}
                }
                
                /**
                 * Cambio il nome delle colonne per aggiungere il simbolo (%)
                 */
                Vector<String> sColumns = new Vector<String>();
        		sColumns.add("ID");
        		sColumns.add("Composizione");
        		sColumns.add("Min (%)");
        		sColumns.add("Max (%)");
        		tabellaSoglie.setColumnName(sColumns);
                
                /**
                 * Imposto quali colonne non devono essere visualizzate
                 */
                String[] sInvisibleColumns = new String[] {"ID"};
                tabellaSoglie.setInvisibleColumn(sInvisibleColumns);
                
                /**
                 * Imposto quali colonne possono essere editate
                 */
                //String[] sEditableColumns = new String[] {"Min", "Max"};
                //tabellaSoglie.setEditableColumns(sEditableColumns);
                
                /**
                 * Imposto la larghezza della colonna "Composizione"
                 */
                String[] sWidthColumns = new String[] {"Composizione"};
                Integer[] iWidthCols = new Integer[] {150};
                tabellaSoglie.setColumnWidth(sWidthColumns, iWidthCols);
                
                /**
                 * Visualizzo i dati
                 */
                tabellaSoglie.MostraDati();
                
                ControlloSoglie();
        }
        
        /**
         * Controllo Soglie e aggiorno colorazione nella tabella dei Totali
         */
        private void ControlloSoglie()
        {
                if (tabellaSoglie.getRowCount() == 0)
                {
                        return;
                }
                
                /**
                 * Verifico la composizione della ricetta contro le nuove soglie
                 */
                Vector<String> sCols = new Vector<String>();
                sCols.add("Acqua (%)");
                sCols.add("Zuccheri (%)");
                sCols.add("Grassi (%)");
                sCols.add("SLNG (%)");
                sCols.add("AltriSolidi (%)");
                
                Vector<Object> dMinValues = new Vector<Object>();
                dMinValues.add(tabellaSoglie.getValueAt(tabellaSoglie.getDataRowFromTableRow(tabellaSoglie.getRow("Composizione", "Acqua"), "Composizione"), tabellaSoglie.getColumnIndex("Min (%)")));
                dMinValues.add(tabellaSoglie.getValueAt(tabellaSoglie.getDataRowFromTableRow(tabellaSoglie.getRow("Composizione", "Zuccheri"), "Composizione"), tabellaSoglie.getColumnIndex("Min (%)")));
                dMinValues.add(tabellaSoglie.getValueAt(tabellaSoglie.getDataRowFromTableRow(tabellaSoglie.getRow("Composizione", "Grassi"), "Composizione"), tabellaSoglie.getColumnIndex("Min (%)")));
                dMinValues.add(tabellaSoglie.getValueAt(tabellaSoglie.getDataRowFromTableRow(tabellaSoglie.getRow("Composizione", "SLNG"), "Composizione"), tabellaSoglie.getColumnIndex("Min (%)")));
                dMinValues.add(tabellaSoglie.getValueAt(tabellaSoglie.getDataRowFromTableRow(tabellaSoglie.getRow("Composizione", "Altri Solidi"), "Composizione"), tabellaSoglie.getColumnIndex("Min (%)")));
                
                Vector<Object> dMaxValues = new Vector<Object>();
                dMaxValues.add(tabellaSoglie.getValueAt(tabellaSoglie.getDataRowFromTableRow(tabellaSoglie.getRow("Composizione", "Acqua"), "Composizione"), tabellaSoglie.getColumnIndex("Max (%)")));
                dMaxValues.add(tabellaSoglie.getValueAt(tabellaSoglie.getDataRowFromTableRow(tabellaSoglie.getRow("Composizione", "Zuccheri"), "Composizione"), tabellaSoglie.getColumnIndex("Max (%)")));
                dMaxValues.add(tabellaSoglie.getValueAt(tabellaSoglie.getDataRowFromTableRow(tabellaSoglie.getRow("Composizione", "Grassi"), "Composizione"), tabellaSoglie.getColumnIndex("Max (%)")));
                dMaxValues.add(tabellaSoglie.getValueAt(tabellaSoglie.getDataRowFromTableRow(tabellaSoglie.getRow("Composizione", "SLNG"), "Composizione"), tabellaSoglie.getColumnIndex("Max (%)")));
                dMaxValues.add(tabellaSoglie.getValueAt(tabellaSoglie.getDataRowFromTableRow(tabellaSoglie.getRow("Composizione", "Altri Solidi"), "Composizione"), tabellaSoglie.getColumnIndex("Max (%)")));
                
                tabellaTotali.setColorOutsideConditions(sCols, dMinValues, dMaxValues);
                tabellaTotali.MostraDati();
        }
        
        public void AggiungiIngrediente(Long iID, Double dQuant)
        {       
	        	/**
	             * Ricarico gli Ingredienti in memoria perche' ce ne potrebbe essere qualcuno nuovo 
	             */
	            data_ing = null;
	            data_ing = new Vector<Ingredienti>();
	            data_ing = DBMgr.SelectIngredienti("WHERE Deleted='N'");
            
                /**
                 * Tra tutti gli ingredienti seleziono quello con lo stesso ID
                 * Ricavo gli ingredienti dalla tabella Ingredienti tramite l'ID
                 */
                Vector<Vector<Object>> data_tmp = new Vector<Vector<Object>>();
                data_tmp = DBMgr.Select("Ingredienti", "WHERE ID=" + iID);
                
                /**
                 * Se l'ingrediente in tabella non esiste esco subito
                 */
                if (data_tmp == null)
                {
                        return;
                }
                
                /**
                 * Creo una matrice con i dati aggiunti
                 */
                Vector<Vector<Object>> data = new Vector<Vector<Object>>();
                
                for (int i=0; i<data_tmp.size(); i++)
                {
                        Vector<Object> dataRow = new Vector<Object>();
                        if (tabellaComposizione.IsPresent("ID", data_tmp.get(i).get(0)) == false)
                        {
                                dataRow.add(sColumnNameComp.indexOf("ID"), data_tmp.get(i).get(0));
                                dataRow.add(sColumnNameComp.indexOf("Nome"), data_tmp.get(i).get(1));
                                dataRow.add(sColumnNameComp.indexOf("Quantita (g)"), dQuant);
                                dataRow.add(sColumnNameComp.indexOf("Acqua (g)"), 0);
                                dataRow.add(sColumnNameComp.indexOf("Zuccheri (g)"), 0);
                                dataRow.add(sColumnNameComp.indexOf("Grassi (g)"), 0);
                                dataRow.add(sColumnNameComp.indexOf("SLNG (g)"), 0);
                                dataRow.add(sColumnNameComp.indexOf("AltriSolidi (g)"), 0);
                                dataRow.add(sColumnNameComp.indexOf("Deleted"), data_tmp.get(i).get(7));
                                dataRow.add(sColumnNameComp.indexOf("POD"), 0);
                                dataRow.add(sColumnNameComp.indexOf("PAC"), 0);
                                
                                data.add(dataRow); 
                        }
                }
                
                /**
                 * Salvo i dati e i nomi colonna nella matrice "data";
                 */
                tabellaComposizione.AggiungiDati(data, sColumnNameComp);
                
                /**
                 * Aggiorno le quantità dei campi non editabili
                 */
                CalcolaQuantity();
                
                /**
                 * Aggiorno i Totali
                 */
                AggiornaTotali();
                
                /**
                 * Visualizzo
                 */
                tabellaComposizione.MostraDati();               
                tabellaTotali.MostraDati();
        }

        public void RimuoviIngrediente()
        {
                Integer iRow;
                Integer iID;
                iRow = tabellaComposizione.getSelectedRow();
                
                if (iRow != -1)
                {
                        /**
                         * Ricavo l'ID dell'ingrediente selezionato
                         */
                        iID = Integer.parseInt(tabellaComposizione.getSelectedValue("ID").toString());
                        
                        /**
                         * Rimuovo la riga usando l'ID
                         */
                        tabellaComposizione.RimuoviRowDati(iID, "ID");
                        
                        /**
                         * Aggiorno la JTable
                         */
                        tabellaComposizione.MostraDati();
                        
                        /**
                         * Aggiorno i Totali
                         */
                        AggiornaTotali();
                        
                        /**
                         * Aggiorno la visualizzazione dei Totali
                         */
                        tabellaTotali.MostraDati();
                }               
        }
        
        /**
         * Viene chiamata quando l'utente ricarica il pannello
         */
        public void AggiornaIngredienti()
        {
        	/**
        	 * Ricavo la matrice dei dati che compone la tabella Composizione
        	 */
        	Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        	
        	data = tabellaComposizione.GetDataVector();
        	if (data == null)
        	{
        		return;
        	}
        	
        	/**
        	 * Ciclo su tutti gli ingredienti e quelli presenti nel DB vengono ricaricati.
        	 */
        	for (int i=0; i<data.size(); i++)
            {
        		AggiungiIngrediente(Long.parseLong(data.get(i).get(tabellaComposizione.getColumnIndex("ID")).toString()), Double.parseDouble(data.get(i).get(tabellaComposizione.getColumnIndex("Quantita (g)")).toString()));
            }
        }
        
        /**
         * Viene chiamata quando l'utente cambia il valore di una quantità nella tabella
         */
        private void CalcolaQuantity()
        {
                int i, j;
                boolean bFound;
                Double dQuant;
                
                /**
                 * Ciclo lungo la tabella 
                 */
                for (i=0; i < tabellaComposizione.getRowCount(); i++)
                {
                        Integer iColumn;
                        iColumn = Integer.parseInt(tabellaComposizione.getColumnIndex("Quantita (g)").toString());

                        /**
                         * Controllo se la Quantità è impostata
                         */
                        if (tabellaComposizione.getValueAt(i, iColumn) != null)
                        {
                                dQuant = Double.parseDouble(tabellaComposizione.getValueAt(i, tabellaComposizione.getColumnIndex("Quantita (g)")).toString());
                                
                                /**
                                 * Cerco lo stesso ingrediente in data_ing tramite l'ID
                                 */
                                j=0;
                                bFound = false; 
                                while ((j<data_ing.size()) && bFound == false)
                                {
                                        if (Integer.parseInt(tabellaComposizione.getValueAt(i, tabellaComposizione.getColumnIndex("ID")).toString())== data_ing.get(j).getID())
                                        {
                                                CalcolaPercentuali(dQuant, i, j);
                                                bFound = true;
                                        }
                                        j++;
                                }
                        }       
                }
        }
        
        private void CalcolaPercentuali(Double dQuant, int iTblRow, int iIngRow)
        {
                Double newAcqua = Arrotonda(dQuant * data_ing.get(iIngRow).getAcqua() / 100);
                Double newZucchero = Arrotonda(dQuant * data_ing.get(iIngRow).getZuccheri() / 100);
                Double newGrassi = Arrotonda(dQuant * data_ing.get(iIngRow).getGrassi() / 100);
                Double newSLNG = Arrotonda(dQuant * data_ing.get(iIngRow).getSLNG() / 100);
                Double newAltriSolidi = Arrotonda(dQuant * data_ing.get(iIngRow).getAltriSolidi() / 100);
                Double newPOD = Arrotonda(newZucchero * data_ing.get(iIngRow).getPOD() / 100);
                Double newPAC = Arrotonda(newZucchero * data_ing.get(iIngRow).getPAC() / 100);
                
                tabellaComposizione.setDataAt(newAcqua, iTblRow, tabellaComposizione.getColumnIndex("Acqua (g)"));
                tabellaComposizione.setDataAt(newZucchero, iTblRow, tabellaComposizione.getColumnIndex("Zuccheri (g)"));
                tabellaComposizione.setDataAt(newGrassi, iTblRow, tabellaComposizione.getColumnIndex("Grassi (g)"));
                tabellaComposizione.setDataAt(newSLNG, iTblRow, tabellaComposizione.getColumnIndex("SLNG (g)"));
                tabellaComposizione.setDataAt(newAltriSolidi, iTblRow, tabellaComposizione.getColumnIndex("AltriSolidi (g)"));
                tabellaComposizione.setDataAt(newPOD, iTblRow, tabellaComposizione.getColumnIndex("POD"));
                tabellaComposizione.setDataAt(newPAC, iTblRow, tabellaComposizione.getColumnIndex("PAC"));
        }
        
        /**
         * Viene chiamata quando viene cambiato il valore di una quantità nella tabella
         */
        private void AggiornaTotali()
        {
                /**
                 * Calcolo il nuovo totale e le nuove percentuali
                 */
                double dQuantTot, dPercAcqua, dPercZucchero, dPercGrassi, dPercSLNG, dPercAltriSolidi, dPercPOD, dPercPAC;
                
                dQuantTot = Arrotonda(tabellaComposizione.getSum("Quantita (g)"));
                dPercAcqua = Arrotonda(tabellaComposizione.getSum("Acqua (g)") / dQuantTot * 100);
                dPercZucchero = Arrotonda(tabellaComposizione.getSum("Zuccheri (g)") / dQuantTot * 100);
                dPercGrassi = Arrotonda(tabellaComposizione.getSum("Grassi (g)") / dQuantTot * 100);
                dPercSLNG = Arrotonda(tabellaComposizione.getSum("SLNG (g)") / dQuantTot * 100);
                dPercAltriSolidi = Arrotonda(tabellaComposizione.getSum("AltriSolidi (g)") / dQuantTot * 100);
                dPercPOD = Arrotonda(tabellaComposizione.getSum("POD") / dQuantTot * 100);
                dPercPAC = Arrotonda(tabellaComposizione.getSum("PAC") / dQuantTot * 100);
                
                /**
                 * Aggiorno i dati in tabella
                 */
                Vector<Vector<Object>> dataTot = new Vector<Vector<Object>>();
                Vector<Object> dataTotRow = new Vector<Object>();
                
                dataTotRow.add(tabellaTotali.getColumnIndex("Nome"), "Totali");
                dataTotRow.add(tabellaTotali.getColumnIndex("Quantita (g)"), dQuantTot);
                dataTotRow.add(tabellaTotali.getColumnIndex("Acqua (%)"), dPercAcqua);
                dataTotRow.add(tabellaTotali.getColumnIndex("Zuccheri (%)"), dPercZucchero);
                dataTotRow.add(tabellaTotali.getColumnIndex("Grassi (%)"), dPercGrassi);
                dataTotRow.add(tabellaTotali.getColumnIndex("SLNG (%)"), dPercSLNG);
                dataTotRow.add(tabellaTotali.getColumnIndex("AltriSolidi (%)"), dPercAltriSolidi);
                dataTotRow.add(tabellaTotali.getColumnIndex("POD"), dPercPOD);
                dataTotRow.add(tabellaTotali.getColumnIndex("PAC"), dPercPAC);
                
                dataTot.add(dataTotRow);
                tabellaTotali.ImportaDati(dataTot, sColumnNameTot);
                
                /**
                 * Aggiorno il totale nella JTextField per il ricalcolo
                 */
                txtRicalcola.setText(String.valueOf((int)dQuantTot));
        }
        
        private Double Arrotonda(Double dInput)
        {
                Double output;
                output = (double) Math.round(dInput * 10);
                output = output / 10;
                
                return output;
        }
        
        private void Ricalcola()
        {
                String sNewTot = txtRicalcola.getText();
                
                /**
                 * Se la JTextField e' vuota esco subito
                 */
                if (sNewTot.isEmpty() == true)
                {
                        return;
                }
                
                /**
                 * Se non ci sono ingredienti da ricalcolare esco subito
                 */
                if (tabellaComposizione.getHeight() == 0)
                {
                    return;
                }
                
                Double dOldTot;
                Double dNewTot;
                Double dQuant;
                
                dOldTot = Double.parseDouble(tabellaTotali.getValueAt(0, tabellaTotali.getColumnIndex("Quantita (g)")).toString());
                dNewTot = Double.parseDouble(sNewTot);
                
                /**
                 * Ciclo nella tabella di Composizione per cambiare le quantita'�
                 */
                for (int i=0; i < tabellaComposizione.getRowCount(); i++)
                {
                        /**
                         * Modifico la Quantita'�
                         */
                        dQuant = Double.parseDouble(tabellaComposizione.getValueAt(i, tabellaComposizione.getColumnIndex("Quantita (g)")).toString());
                        tabellaComposizione.setDataAt(Arrotonda(dQuant * dNewTot / dOldTot), i, tabellaComposizione.getColumnIndex("Quantita (g)"));
                }
                
                /**
                 * Aggiorno tabella di Composizione
                 */
                CalcolaQuantity();
                tabellaComposizione.MostraDati();
                
                /**
                 * Aggiorno tabella dei Totali
                 */
                AggiornaTotali();
                tabellaTotali.MostraDati();
        }

        /**
         * Salvo la ricetta nel DB
         */
        private void Salva()
        {
                /**
                 * Se il nome della ricetta non è stato impostato avviso con una finestra e esco
                 */
                if(txtNomeRicetta.getText().isEmpty() == true)
                {
                        JOptionPane.showMessageDialog(null, "Impossibile salvare la ricetta. Il nome ricetta non è impostato", "Attenzione", JOptionPane.WARNING_MESSAGE);
                        return;
                }
                
                Integer iIDRicetta;
                
                if (IsRicettaPresent() == false)
                {
                        iIDRicetta = TrovaIDNuovaRicetta();
                }
                else
                {
                        iIDRicetta = iIDRicettaViewed;
                }
                
                /**
                 * Ricavo la matrice "data" con gli ingredienti che compongono la ricetta
                 */
                Vector<String> sColumns = new Vector<String>();
                sColumns.add("ID");
                sColumns.add("Quantita (g)");

                Vector<Vector<Object>> data = new Vector<Vector<Object>>();
                data = tabellaComposizione.GetDataVector(sColumns, null, null);
                
                /**
                 * Se nella ricetta non è stato incluso nessun ingrediente esco subito.
                 */
                if (data == null)
                {
                        JOptionPane.showMessageDialog(null, "Impossibile salvare la ricetta. Almeno un ingrediente deve essere selezionato.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                        return;
                }
                if (data.size() == 0)
                {
                        JOptionPane.showMessageDialog(null, "Impossibile salvare la ricetta. Almeno un ingrediente deve essere selezionato.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                        return;
                }
                
                /**
                 * Modifico la matrice "data" per includere alcuni campi necessari al salvataggio in tabella
                 */
                Vector<String> sColComp = new Vector<String>();
                sColComp.add("ID");
                sColComp.add("ID_Ing");
                sColComp.add("Quantita");
                
                for (int i=0; i < data.size(); i++)
                {
                        data.get(i).insertElementAt(iIDRicetta, 0);
                }
                
                /**
                 * UPDATE tabella Composizione nel DB
                 */
                if (DBMgr.UpdateComposizioneTable(data) == false)
                {
                        JOptionPane.showMessageDialog(null, "Impossibile salvare la ricetta.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                        return;
                }
                
                /**
                 * Ricavo la riga "dataRow" per la tabella Ricette
                 */
                Vector<Object> dataRow = new Vector<Object>();
                dataRow.add(iIDRicetta);
                dataRow.add(txtNomeRicetta.getText());
                dataRow.add(txtNote.getText());
                
                /**
                 * UPDATE tabella Ricette nel DB
                 */
                if (DBMgr.AggiornoRicetteTable(dataRow) == false)
                {
                        JOptionPane.showMessageDialog(null, "Impossibile salvare completamente la ricetta. Contattare l'amministratore del sistema.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                        return;
                }
                
                /**
                 * Notifico all'utente che il salvataggio � avvenuto con successo
                 */
                JOptionPane.showMessageDialog(null, "Ricetta salvata!", "", JOptionPane.INFORMATION_MESSAGE);
        }
        
        private void Stampa()
        {
                PrintRicetta prtRct = new PrintRicetta();
                prtRct.ImpostaTitoloRicetta(txtNomeRicetta.getText());
                prtRct.ImpostaIngredienti(tabellaComposizione.getTable());
                prtRct.ImpostaTotali(tabellaTotali.getTable());
                prtRct.ImpostaSoglie(tabellaSoglie.getTable());
                prtRct.ImpostaNote(txtNote.getText());
                prtRct.Stampa();
        }
        
        private boolean IsRicettaPresent()
        {
                boolean bIsOld = false;
                
                /**
                 * Ricavo l'ID della ricetta e lo imposto nella tabella se si tratta di una nuova base
                 */
                Vector<String> sCols = new Vector<String>();
                sCols.add("ID");
                
                Vector<Object> objValues = new Vector<Object>();
                objValues.add(iIDRicettaViewed);
                
                /**
                 * Controllo se esiste una base con lo stesso ID
                 */
                bIsOld = DBMgr.IsPresent("Ricette", sCols, objValues);
                sCols.add("Nome");
                objValues.add(txtNomeRicetta.getText());
                
                /**
                 * Controllo se esiste una base con lo stesso ID e stesso Nome
                 */
                bIsOld = bIsOld && DBMgr.IsPresent("Ricette", sCols, objValues);        
                
                return bIsOld;
        }
        
        /**
         * Ricavo l'ID per la Ricetta che sto creando 
         */
        public Integer TrovaIDNuovaRicetta()
        {
                Integer output;
                
                output = DBMgr.getMaxValue("Ricette", "ID");
                
                if (output == -1)
                {
                        output = 0;
                }
                else
                {
                        output++;       /* Devo incrementare di 1 perchè mi serve il prox ID libero  */
                }
                
                return output;
        }
        
        public void setRicettaNome(String sNomeRicetta)
        {
                txtNomeRicetta.setText(sNomeRicetta);
        }
        
        public void setRicettaID(Integer iID)
        {
                iIDRicettaViewed = iID;
        }
        
        public void setNote(String sNote)
        {
                txtNote.setText(sNote);
        }
        
        public void SvuotaTabelle()
        {
                tabellaComposizione.SvuotaDati();
                tabellaComposizione.MostraDati();
                
                tabellaTotali.SvuotaDati();
                tabellaTotali.MostraDati();
                
                txtRicalcola.setText("");
        }
        
        /**
         * Setto la colonna Quantita'� come editabile o no. Questo viene utilizzato per 
         * consultare o modificare una ricetta.
         * @param bEditable - true = editabile, false = altrimenti
         */
        public void setRicettaEditable(boolean bEditable)
        {
        	if (bEditable == true)
        	{
        		tabellaComposizione.setEditableColumns(new String[] {"Quantita (g)"});
        		tabellaComposizione.MostraDati();
        		btnSalva.setVisible(true);
        	}
        	else
        	{
        		tabellaComposizione.setEditableColumns(new String[] {});
        		tabellaComposizione.MostraDati();
        		btnSalva.setVisible(false);
        	}
        }
}