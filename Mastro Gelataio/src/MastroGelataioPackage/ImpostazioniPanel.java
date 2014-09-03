package MastroGelataioPackage;

import java.awt.FileDialog;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Vector;

public class ImpostazioniPanel extends JPanel {

        DBMgrWrap DBMgr;
        JComboBox<String> cbbTipoBase;
        JTextField txtNomeBase;
        JLabel lblBase;
        JButton btnElimina;
        JButton btnSalva;
        JButton btnSvuotaDB;
        JButton btnEsportaDB;
        JButton btnImportaDB;
        JPanel pnlDB;
        JLabel lblDB;
        JPanel pnlSoglie;
        JLabel lblSoglie;
        TabellaGenerica tabellaSoglie;
        Vector<Vector<Object>> objTipiBasi;
        
        /**
         * Create the panel.
         */
        public ImpostazioniPanel(DBMgrWrap prtDBMgr) 
        {       
                DBMgr = new DBMgrWrap();
                DBMgr = prtDBMgr;
        
                setLayout(null);
                
                /**
                 * Creo parte delle soglie
                 */
                CreaSogliePart();
        
                /**
                 * Creo la parte del database
                 */
                CreaDatabasePart();
        }
        
        private void CreaSogliePart()
        {
                /**
                 * Creo il JPanel per il contorno
                 */
                pnlSoglie = new JPanel();
                pnlSoglie.setBounds(0, 0, 240, 330);
                pnlSoglie.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
                pnlSoglie.setLayout(null);
                add(pnlSoglie);
                
                /**
                 * Creo la JLabel del titolo
                 */
                lblSoglie = new JLabel("Valori di Controllo");
                lblSoglie.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
                lblSoglie.setHorizontalAlignment(JLabel.CENTER);
                lblSoglie.setBounds(0, 0, pnlSoglie.getWidth(), 40);
                pnlSoglie.add(lblSoglie);
                
                /**
                 * Creo la JComboBox per scegliere il tipo di base
                 */
                cbbTipoBase = new JComboBox<String>();
                cbbTipoBase.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                cbbTipoBase.setBounds(40, 50, 120, 20);
                pnlSoglie.add(cbbTipoBase);
                
                cbbTipoBase.addActionListener (new ActionListener () {
                    public void actionPerformed(ActionEvent e) {
                        CaricaTabellaSoglie();
                    }
                });
                
                /**
                 * Creo la JLabel per il nome della base
                 */
                lblBase = new JLabel("Nome");
                lblBase.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                lblBase.setBounds(20, 90, 80, 20);
                pnlSoglie.add(lblBase);
                
                /**
                 * Creo la JTextField per il nome della base
                 */
                txtNomeBase = new JTextField();
                txtNomeBase.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                txtNomeBase.setBounds(lblBase.getX() + 80, 90, 120, 20);
                pnlSoglie.add(txtNomeBase);
                
                /**
                 * Creo la tabella delle soglie
                 */
                tabellaSoglie = new TabellaGenerica(pnlSoglie, 20, txtNomeBase.getY() + txtNomeBase.getHeight() + 10, 200, 130);
                
                /**
                 * Creo il JButton per eliminare una base
                 */
                btnElimina = new JButton("Elimina");
                btnElimina.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                btnElimina.setBounds(20, tabellaSoglie.getY() + tabellaSoglie.getHeight() + 20, Globals.BUTTON_WIDTH, Globals.BUTTON_HEIGHT);
                pnlSoglie.add(btnElimina);
                
                btnElimina.addActionListener (new ActionListener () {
                    public void actionPerformed(ActionEvent e) {
                        EliminaBase();
                    }
                });
                
                /**
                 * Creo il JButton per salvare una base
                 */
                btnSalva = new JButton("Salva");
                btnSalva.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                btnSalva.setBounds(tabellaSoglie.getX() + tabellaSoglie.getWidth() - Globals.BUTTON_WIDTH, btnElimina.getY(), Globals.BUTTON_WIDTH, Globals.BUTTON_HEIGHT);
                pnlSoglie.add(btnSalva);
                
                btnSalva.addActionListener (new ActionListener () {
                    public void actionPerformed(ActionEvent e) {
                        SalvaBase();
                    }
                });
                
                /**
                 * Carico i tipi di base nella JComboBox
                 */
                CaricaBasi();
                
                /**
                 * Aggiungo la base per l'inserimento di una nuova base
                 */
                cbbTipoBase.addItem("Nuovi Valori");    
        }
        
        private void CreaDatabasePart()
        {
                /**
                 * Creo il JPanel per il contorno
                 */
                pnlDB = new JPanel();
                pnlDB.setBounds(tabellaSoglie.getX() + tabellaSoglie.getWidth() + 150, 0, 150, 210);
                pnlDB.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
                pnlDB.setLayout(null);
                add(pnlDB);
                
                /**
                 * Creo la JLabel del titolo
                 */
                lblDB = new JLabel("Database");
                lblDB.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
                lblDB.setHorizontalAlignment(JLabel.CENTER);
                lblDB.setBounds(0, 0, pnlDB.getWidth(), 40);
                pnlDB.add(lblDB);
                
                /**
                 * Creo il JButton per svuotare il DB
                 */
                btnSvuotaDB = new JButton("Cancella");
                btnSvuotaDB.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                btnSvuotaDB.setBounds(30, 50, Globals.BUTTON_WIDTH, Globals.BUTTON_HEIGHT);
                pnlDB.add(btnSvuotaDB);
                
                btnSvuotaDB.addActionListener (new ActionListener () {
                    public void actionPerformed(ActionEvent e) {
                        SvuotaDB();
                    }
                });
                
                /**
                 * Creo il JButton per esportare il DB
                 */
                btnEsportaDB = new JButton("Esporta");
                btnEsportaDB.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                btnEsportaDB.setBounds(30, 50 + 50, Globals.BUTTON_WIDTH, Globals.BUTTON_HEIGHT);
                pnlDB.add(btnEsportaDB);
                
                btnEsportaDB.addActionListener (new ActionListener () {
                    public void actionPerformed(ActionEvent e) {
                        EsportaDB();
                    }
                });
                
                /**
                 * Creo il JButton per importare il DB
                 */
                btnImportaDB = new JButton("Importa");
                btnImportaDB.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                btnImportaDB.setBounds(30, 50 + 100, Globals.BUTTON_WIDTH, Globals.BUTTON_HEIGHT);
                pnlDB.add(btnImportaDB);
                
                btnImportaDB.addActionListener (new ActionListener () {
                    public void actionPerformed(ActionEvent e) {
                        ImportaDB();
                    }
                });
        }
        
        public void CambiaSize(int X, int Y, int Width, int Height)
        {
                this.setBounds(X, Y, Width, Height);
        }
        
        private void CaricaBasi()
        {
                objTipiBasi = null;
                
                objTipiBasi = new Vector<Vector<Object>>();
                
                objTipiBasi = DBMgr.Select("Basi", null);
                
                if (objTipiBasi != null)
                {
                        for (int i=0; i < objTipiBasi.size(); i++)
                        {
                                cbbTipoBase.addItem(objTipiBasi.get(i).get(1).toString());      
                        }
                }       
        }
        
        private void CaricaTabellaSoglie()
        {
                Integer iBase;
        
                iBase = cbbTipoBase.getSelectedIndex();
                
                /**
                 * Ripulisco la visualizzazione corrente
                 */
                tabellaSoglie.SvuotaDati();
                tabellaSoglie.MostraDati();
                
                /**
                 * Ricavo le soglie dal DB
                 */
                if ((objTipiBasi != null) && (objTipiBasi.size() != 0) && (iBase < objTipiBasi.size()))
                {
                        tabellaSoglie.CaricaDati("Soglie", " WHERE ID=" + objTipiBasi.get(iBase).get(0), DBMgr);
                        txtNomeBase.setText(objTipiBasi.get(iBase).get(1).toString());
                }
                else
                {
                        CaricaTabellaVuota();
                        txtNomeBase.setText("");
                }
                
                /**
                 * Imposto quali colonne non devono essere visualizzate
                 */
                String[] sInvisibleColumns = new String[] {"ID"};
                tabellaSoglie.setInvisibleColumn(sInvisibleColumns);
                
                /**
                 * Imposto quali colonne possono essere editate
                 */
                String[] sEditableColumns = new String[] {"Min", "Max"};
                tabellaSoglie.setEditableColumns(sEditableColumns);
                
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
        }
        
        private void CaricaTabellaVuota()
        {
                /**
                 * Creo le colonne della tabella
                 */
                Vector<String> sColumns = new Vector<String>();
                
                sColumns.add("ID");
                sColumns.add("Composizione");
                sColumns.add("Min");
                sColumns.add("Max");
                
                /**
                 * Creo i dati della tabella
                 */
                Vector<Vector<Object>> data = new Vector<Vector<Object>>();
                
                Integer iID = FindNextID();
                
                for (int i=0; i < 6; i++)
                {
                        Vector<Object> dataRow = new Vector<Object>();
                        
                        dataRow.add(iID);
                        switch(i)
                        {
                        case 0:
                                dataRow.add("Acqua");
                                break;
                        case 1:
                                dataRow.add("Solidi");
                                break;
                        case 2:
                                dataRow.add("Zuccheri");
                                break;
                        case 3:
                                dataRow.add("Grassi");
                                break;
                        case 4:
                                dataRow.add("SLNG");
                                break;
                        case 5:
                                dataRow.add("Altri Solidi");
                                break;
                        default:
                                break;
                        }
                        dataRow.add(0.0);
                        dataRow.add(0.0);
                        
                        data.add(dataRow);
                }
                
                tabellaSoglie.ImportaDati(data, sColumns);
        }
        
        private Integer FindNextID()
        {
                Integer output = 0;
                
                output = DBMgr.getMaxValue("Basi", "ID"); 
                if (output == -1)
                {
                        output = 0;
                }
                else
                {
                        output++;
                }
                
                return output;
        }
        
        /**
         * Elimina la base selezionata nella JComboBox aggiornando il DB, objTipiDati e la JComboBox stessa
         */
        private void EliminaBase()
        {
                Integer iBase = cbbTipoBase.getSelectedIndex();
                Integer iID = -1;
                String sNomeBase;
                
                if ((objTipiBasi != null) && (objTipiBasi.size() != 0) && (iBase < objTipiBasi.size()) && (objTipiBasi.get(iBase).get(1).toString() != "Nuovi Valori"))
                {
                        /**
                         * Ricavo l'ID della Base selezionata
                         */
                        iID = Integer.parseInt(objTipiBasi.get(iBase).get(0).toString());
                                
                        /**
                         * Elimino la riga dalla tabella Basi
                         */
                        sNomeBase = objTipiBasi.get(iBase).get(1).toString();
                        
                        Vector<String> sCondColsBasi = new Vector<String>();
                        sCondColsBasi.add("ID");
                        sCondColsBasi.add("Nome");
                        Vector<Object> objValuesBasi = new Vector<Object>();
                        objValuesBasi.add(iID);
                        objValuesBasi.add(sNomeBase);
                        
                        if (DBMgr.Delete("Basi", sCondColsBasi, objValuesBasi) == false)
                        {
                                JOptionPane.showMessageDialog(this, "Impossibile eliminare la base.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                                return;
                        }
                        else
                        {
                                /**
                                 * Elimino la riga da objTipiBasi
                                 */
                                objTipiBasi.removeElementAt(iBase);
                                
                                /**
                                 * Rimuovo la base dalla JComboBox
                                 */
                                cbbTipoBase.removeItemAt(iBase);
                                
                                /**
                                 * Cambio la selezione nella JComboBox
                                 */
                                if (iBase > 0)
                                {
                                        cbbTipoBase.setSelectedIndex(iBase - 1);        
                                }
                                else
                                {
                                        cbbTipoBase.setSelectedIndex(iBase);
                                }
                                
                                /**
                                 * Rimuovo la riga dalla tabella Soglie
                                 */
                                Vector<String> sCondColsSoglie = new Vector<String>();
                                sCondColsSoglie.add("ID");
        
                                Vector<Object> objValuesSoglie = new Vector<Object>();
                                objValuesSoglie.add(iID);
                                if (DBMgr.Delete("Soglie", sCondColsSoglie, objValuesSoglie) == false)
                                {
                                        JOptionPane.showMessageDialog(this, "Impossibile eliminare completamente la base. Contattare l'amministratore del sistema.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                                        return;
                                }
                        }
                }
                
        }
        
        private void SalvaBase()
        {
                boolean bModifcaBase = false;
                
                /**
                 * Se il nome della base non è stato impostato avviso con una finestra e esco
                 */
                if(txtNomeBase.getText().isEmpty() == true)
                {
                        JOptionPane.showMessageDialog(this, "Impossibile salvare le soglie. Il nome non è impostato", "Attenzione", JOptionPane.WARNING_MESSAGE);
                        return;
                }
                
                /**
                 * Ricavo l'ID della base e lo imposto nella tabella se si tratta di una nuova base
                 */
                Vector<String> sCols = new Vector<String>();
                sCols.add("ID");
                
                Vector<Object> objValues = new Vector<Object>();
                objValues.add(tabellaSoglie.getValueAt(0, tabellaSoglie.getColumnIndex("ID")));
                
                /**
                 * Controllo se esiste una base con lo stesso ID
                 */
                bModifcaBase = DBMgr.IsPresent("Basi", sCols, objValues);
                sCols.add("Nome");
                objValues.add(txtNomeBase.getText());
                
                /**
                 * Controllo se esiste una base con lo stesso ID e stesso Nome
                 */
                bModifcaBase = bModifcaBase && DBMgr.IsPresent("Basi", sCols, objValues);
                
                /**
                 * Se è una nuova base devo determinare il nuovo ID
                 */
                Integer iID = -1;
                if (bModifcaBase == false)
                {
                        iID = FindNextID();
                        for (int i=0; i < tabellaSoglie.getRowCount(); i++)
                        {
                                tabellaSoglie.setDataAt(iID, i, tabellaSoglie.getColumnIndex("ID"));
                        }       
                }
                
                /**
                 * Ricavo la matrice "data" con le composizioni che compongono la base
                 */
                Vector<Vector<Object>> data = new Vector<Vector<Object>>();
                data = tabellaSoglie.GetDataVector();
                
                /**
                 * UPDATE del DB
                 */
                DBMgr.UpdateSoglieTable(data);
                
                /**
                 * Se è una nuova base devo anche aggiornare la tabella Basi
                 */
                if (bModifcaBase == false)
                {
                        /**
                         * Aggiungo la base aggiunta alla matrice "objTipiBasi"
                         */
                        Vector<Object> BaseRow = new Vector<Object>();
                        BaseRow.add(iID);
                        BaseRow.add(txtNomeBase.getText());
                        
                        if (objTipiBasi == null)
                        {
                                objTipiBasi = new Vector<Vector<Object>>();
                        }
                        objTipiBasi.add(BaseRow);
                        
                        /**
                         * Aggiungo la base aggiunta alla JComboBox
                         */
                        cbbTipoBase.insertItemAt(txtNomeBase.getText(), objTipiBasi.size() - 1);
                        cbbTipoBase.setSelectedIndex(objTipiBasi.size() - 1);
                        
                        /**
                         * UPDATE del DB
                         */
                        DBMgr.UpdateBasiTable(objTipiBasi);
                }
        }
        
        private void SvuotaDB()
        {
                int option = JOptionPane.showConfirmDialog(null, "Tutti le ricette, gli ingredienti e le soglie verranno cancellate. Continuare?", "Attenzione", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.NO_OPTION)
                {
                        return;
                }
                
                DBMgr.Delete(DBMgrWrap.TABELLA_INGREDIENTI, null, null);
                DBMgr.Delete(DBMgrWrap.TABELLA_COMPOSIZIONE, null, null);
                DBMgr.Delete(DBMgrWrap.TABELLA_RICETTE, null, null);
                DBMgr.Delete(DBMgrWrap.TABELLA_BASI, null, null);
                DBMgr.Delete(DBMgrWrap.TABELLA_SOGLIE, null, null);
                DBMgr.Delete(DBMgrWrap.TABELLA_TIPI_ING, null, null);
                
                /**
                 * Aggiorno la visualizzazione della base corrente
                 */
                
                cbbTipoBase.setSelectedIndex(cbbTipoBase.getItemCount() - 1);
                
                while (cbbTipoBase.getItemCount() > 1)
                {
                        cbbTipoBase.removeItemAt(0);
                }
                CaricaBasi();
        }
        
        private void EsportaDB()
        {
                FileDialog fd = new FileDialog(new Frame(), "Esporta DB", FileDialog.SAVE);
                fd.setVisible(true);
                String dir = fd.getDirectory();
                String f = fd.getFile();

                
                File fSource = new File(DBMgrWrap.DBFile);
                File fDest = new File(dir + "\\" + f);
                
                try
                {
                        Files.copy(fSource.toPath(), fDest.toPath(), StandardCopyOption.REPLACE_EXISTING);      
                }
                catch (Exception e)
                {
                        JOptionPane.showMessageDialog(this, "Si è verificato un errore. Il DB non è stato esportato.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                        return;
                }
        }
        
        private void ImportaDB()
        {
                FileDialog fd = new FileDialog(new Frame(), "Importa DB", FileDialog.LOAD);
                fd.setVisible(true);
                String dir = fd.getDirectory();
                String f = fd.getFile();
                
                File fDest = new File(DBMgrWrap.DBFile);
                File fSource = new File(dir + "\\" + f);
                
                DBMgr.DisconnectDB();
                
                try
                {
                        Files.copy(fSource.toPath(), fDest.toPath(), StandardCopyOption.REPLACE_EXISTING);      
                }
                catch (Exception e)
                {
                        JOptionPane.showMessageDialog(this, "Si è verificato un errore. Il DB non è stato importato.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                }
                
                try
                {
                        DBMgr.ConnectDB();
                }
                catch (Exception e)
                {
                        
                }
        }
}