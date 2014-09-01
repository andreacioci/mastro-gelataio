package MastroGelataioPackage;

import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

public class TabellaGenerica implements TableModelListener {

        private JTable table;
        private TabellaModello TblModel;
        private TableRowSorter<TabellaModello> sorter;
        private JScrollPane scrollPane;
        private Vector<Vector<Object>> data;
        private Vector<String> sColumnName;
        private String[] sInvisibleColumns;
        private String[] sEditableColumns;
        private String[] sRegexFilter;
        private String[] sColumnFilter;
        private Vector<Vector<Object>> columnsWidth;
        private String sColumnUnique;
        private TabellaCellRender tabellaRenderer;
        
        protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();
        
        public TabellaGenerica(JPanel pnlContainer, int iX, int iY, int iWidth, int iHeight)
        {               
                /**
                 * Creo la JTable
                 */
                table = new JTable();
                table.setBounds(iX, iY, iWidth, iHeight);
                
                /**
                 * Creo la JScrollPane
                 */
                scrollPane = new JScrollPane(table);
                scrollPane.setBounds(iX, iY, iWidth, iHeight);
                pnlContainer.add(scrollPane);
                
                /**
                 * Creo il DefaultTableModel
                 */
                TblModel = new TabellaModello();
                table.setModel(TblModel);
                TblModel.addTableModelListener(this);
                
                /**
                 * Creo il TabellaCellRenderer per colorare le celle
                 */
                tabellaRenderer = new TabellaCellRender();
                table.setDefaultRenderer(Object.class, tabellaRenderer);
                table.setDefaultRenderer(Integer.class, tabellaRenderer);
                table.setDefaultRenderer(Double.class, tabellaRenderer);
                table.setDefaultRenderer(String.class, tabellaRenderer);
                tabellaRenderer.setHorizontalAlignment(JLabel.CENTER);

                /**
                 * Creo il filtro
                 */
                sorter = new TableRowSorter<TabellaModello>(TblModel);
                table.setRowSorter(sorter);
                
                /**
                 * Creo il listener per l'evento di selezione nella JTable
                 */             
                table.addMouseListener(new MouseAdapter()
                {
                     public void mouseClicked(MouseEvent e)
                     {
                         fireMyEvent(new MioEvento(e));
                     }
                } );
                
                /**
                 * Creo il listener per mettere il tooltip sulle celle
                 */
                table.addMouseMotionListener(new MouseMotionAdapter(){
                           public void mouseMoved(MouseEvent e)
                           {
                                Point p = e.getPoint(); 
                                int row = table.rowAtPoint(p);
                                int column = table.columnAtPoint(p);
                                table.setToolTipText(String.valueOf(table.getValueAt(row,column)));
                            }
                });
        }
        
        public void setColumnName(Vector<String> sColumn)
        {
                if (sColumnName == null)
                {
                        sColumnName = new Vector<String>();
                        sColumnName = sColumn;
                        
                        tabellaRenderer.setColumnName(sColumn);
                }
                else if (sColumnName.size() == 0)
                {
                        sColumnName = sColumn;
                        
                        tabellaRenderer.setColumnName(sColumn);
                }
        }
        
        public Vector<String> getColumnName()
        {
                return sColumnName;
        }
        
        public void setColorEqualConditions(Vector<String> sColumns, Vector<Object> objValues)
        {
                tabellaRenderer.setEqualConditions(sColumns, objValues);
        }
        
        public void setColorInsideConditions(Vector<String> sColumns, Vector<Object> objMinValues, Vector<Object> objMaxValues)
        {
                tabellaRenderer.setInsideIntervalConditions(sColumns, objMinValues, objMaxValues);
        }
        
        public void setColorOutsideConditions(Vector<String> sColumns, Vector<Object> objMinValues, Vector<Object> objMaxValues)
        {
                tabellaRenderer.setOutsideIntervalConditions(sColumns, objMinValues, objMaxValues);
        }
        
        public void setColumnUnique(String sColumn)
        {
                sColumnUnique = sColumn;
        }
        
        public void setInvisibleColumn(String[] sColumn)
        {
                sInvisibleColumns = null;
                if (sColumn != null)
                {
                        sInvisibleColumns = new String[sColumn.length]; 
                }
                sInvisibleColumns = sColumn;
        }
        
        public void setEditableColumns(String[] sColumn)
        {
                sEditableColumns = null;
                if (sColumn != null)
                {
                        sEditableColumns = new String[sColumn.length];  
                }
                sEditableColumns = sColumn;
        }
        
        public void setRegexFilter(String[] sRegex, String[] sFilter)
        {       
                /**
                 * Resetto le impostazioni precedenti
                 */
                sRegexFilter = null;
                sColumnFilter = null;
                
                /**
                 * Se almeno uno dei due vettori passati è vuoto esco subito
                 */
                if (sRegex == null)
                {
                        return;
                }
                else if (sFilter == null)
                {
                        return;
                }
                
                /**
                 * Se il vettore sRegex ha un numero di elementi diverso da sFilter esco subito
                 */
                if (sRegex.length != sFilter.length)
                {
                        return;
                }
                
                sRegexFilter = new String[sRegex.length];       

                sRegexFilter = sRegex;

                sColumnFilter = new String[sFilter.length];     

                sColumnFilter = sFilter;
        }
        
        public void setColumnWidth(String[] sColumn, Integer[] iColWidth)
        {
                columnsWidth = null;
                if (sColumn != null)
                {
                        /**
                         * Se il vettore sColumn ha un numero di elementi diverso da iColWidth esco subito
                         */
                        if (sColumn.length != iColWidth.length)
                        {
                                return;
                        }
                        
                        columnsWidth = new Vector<Vector<Object>>();
                }
                
                for (int i=0; i < sColumn.length; i++)
                {
                        Vector<Object> objRow = new Vector<Object>();
                        objRow.add(sColumn[i]);
                        objRow.add(iColWidth[i]);
                        
                        columnsWidth.add(objRow);
                }
        }
        
        public Integer getWidth()
        {
                return table.getWidth();
        }
        
        public Integer getHeight()
        {
                return table.getHeight();
        }
        
        public Integer getX()
        {
                return table.getX();
        }
        
        public Integer getY()
        {
                return table.getY();
        }
        
        /**
         * Ricava l'Integer più alto nella colonna sColumn
         * @param sColumn - nome colonna
         * @return - valore max
         */
        public Integer getMaxInt(String sColumn)
        {
                Integer output = -1;
                Integer iTmp;
                
                for (int i=0; i < data.size(); i++)
                {
                        iTmp = Integer.parseInt(data.get(i).get(getColumnIndex(sColumn)).toString());
                        if (output < iTmp)
                        {
                                output = iTmp;
                        }
                }
                
                return output;
        }
        
        /**
         * Ricava la somma dei valori della colonna sColumn
         * @param sColumn - nome colonna
         * @return - somma delle celle
         */
        public Double getSum(String sColumn) 
        {
                Double output = 0.0;
                
                for (int i=0; i < data.size(); i++)
                {
                        output = output + Double.parseDouble(data.get(i).get(getColumnIndex(sColumn)).toString());
                }
                
                return output;
        }
        
        /**
         * Aggiunge una riga vuota alla matrice "data"
         * @return - l'indice della riga inserita
         */
        public Integer AggiungiEmptyRow()
        {       
                Integer output = -1;
                
                if (sColumnName != null)
                {
                        Vector<Object> emptyRow = new Vector<Object>();
                        for (int i=0; i<sColumnName.size(); i++)
                        {
                                Object obj = new Object();
                                emptyRow.add(obj);
                        }
                        data.add(emptyRow);
                        output =  data.size() - 1;
                }               
                
                return output;
        }
        
        /**
         * Ricava il numero di righe dell'oggetto "data"
         * @return
         */
        public Integer getRowCount()
        {
                if (data != null)
                {
                        return data.size();     
                }
                else
                {
                        return 0;
                }
                        
        }
        
        /**
         * Ricavo la prima riga della JTable la cui colonna sColumn contiene il valore objValue
         */
        public Integer getRow(String sColumn, Object objValue)
        {
                Integer output = -1;
                int i = 0;
                boolean bFound = false;
                
                while((i < table.getRowCount()) && (bFound == false))
                {
                        if (objValue.equals(table.getValueAt(i, getColumnIndex(sColumn))))
                        {
                                output = i;
                                bFound = true;
                        }
                        i++;
                }

                return output;
        }
        
        /**
         * Ricava il numero della riga selezionato nell'oggetto JTable
         * @return - Integer
         */
        public Integer getSelectedRow()
        {
                return table.getSelectedRow();
        }
        
        /**
         * Setta come selezionata la riga la cui colonna (sColumn) ha il valore uguale a objValue
         */
        public void setSelectedRow(String sColumn, Object objValue, boolean bEdit)
        {
        	Integer iRow;
        	
        	iRow = getRow(sColumn, objValue);
        	
        	table.changeSelection(iRow, 1, false, false);
        	
        	if (bEdit == true)
        	{
        		table.editCellAt(iRow, 1);
            	table.transferFocus();	
        	}
        }

        public Integer getDataRowFromTableRow(Integer iRow, String sColumn)
        {
                Integer output = -1;
                Object obj;
                
                /**
                 * Ricavo il valore da usare per fare la ricerca nella matrice "data"
                 */
                obj = table.getValueAt(iRow, TblModel.findColumn(sColumn));
                
                /**
                 * Cerco nella matrice "data" usando obj
                 */
                int i = 0;
                boolean bFound = false;
                
                while ((i < data.size()) && (bFound == false))
                {
                        Object obj_tmp = data.get(i).get(sColumnName.indexOf(sColumn)); 
                        if (obj.equals(obj_tmp))
                        {
                                output = i;
                        }
                        i++;
                }
        
                return output;
        }
        
        public Integer getColumnIndex(String sColumn)
        {
                if (sColumnName == null)
                {
                        return -1;
                }
                
                return sColumnName.indexOf(sColumn);
        }
        
        public Object getValueAt(int iRow, int iColumn)
        {
                //return table.getValueAt(iRow, iColumn);
                return data.get(iRow).get(iColumn);
        }
        
        /**
         * Aggiorna l'oggetto "data" con il nuovo valore
         * @param obj - valore da impostare
         * @param iRow - riga della matrice "data"
         * @param iColumn - colonna della matrice "data"
         */
        public void setDataAt(Object obj, int iRow, int iColumn)
        {
                data.get(iRow).set(iColumn, obj);       
        }
        
        public void setBounds(int X, int Y, int Width, int Height)
        {
                table.setBounds(X, Y, Width, Height);
                scrollPane.setBounds(X, Y, Width, Height);
        }
        
        /**
         * Carica i dati dalla tabella all'oggetto "data" e "sColumnName"
         * @param sTable - il nome tabella
         * @param sCondizione - la condizione per fare la SELECT nella query se desiderata
         * @param DBMgr - il gestore del DB
         */
        public void CaricaDati(String sTable, String sCondizione, DBManager DBMgr)
        {
                /**
                 * Ricavo l'header della tabella Ingredienti
                 */
                if (sColumnName == null)
                {
                        sColumnName = new Vector<String>();     
                }
                sColumnName = DBMgr.RicavaNomiColonne(sTable);
                
                /**
                 * Estraggo tutti i dati dalla sTable
                 */
                if (data == null)
                {
                        data = new Vector<Vector<Object>>();    
                } 
                data = DBMgr.Select(sTable, sCondizione);
        }
        
        /**
         * Riempie la JTable e la DefaultTableModel con i dati nell'oggetto "data" e "sColumnName"
         * @param sInvisibleColumns - vettore di colonne che non devono essere mostrate
         * @param sReadableColumn - vettore di colonne read-only
         * @param sRegexFilter - vettore di espressioni per il filtraggio delle righe
         * @param sColumnFilter - vettore di colonne sulle quali insistono le espressioni 
         */
        public void MostraDati()
        {
                /**
                 * Se non sono stati caricati i dati esco subito
                 */
                if (sColumnName == null)
                {
                        return;
                }
                
                /**
                 * Setto l'header
                 */
                if (TblModel.getColumnCount() == 0)
                {
                        TblModel.setColumnIdentifiers(sColumnName);     
                        table.getTableHeader().setFont(new Font("Comic Sans MS" , Font.PLAIN, 13 ));
                        TableCellRenderer renderer = table.getTableHeader().getDefaultRenderer();
                        JLabel label = (JLabel)renderer;
                        label.setHorizontalAlignment(JLabel.CENTER);
                }
                
                /**
                 * Aggiungo i dati tenendo in considerazione la colonna per i valori unici, se presente
                 */
                if (sColumnUnique != null)
                {
                        Vector<Vector<Object>> data_unique = new Vector<Vector<Object>>();
                        data_unique = FilterUniqueValue();
                        TblModel.setDataVector(data_unique, sColumnName);
                }
                else
                {
                        TblModel.setDataVector(data, sColumnName);      
                }
                
                /**
                 * Rendo editabili le colonne desiderate
                 */
                if (sEditableColumns != null)
                {
                        TblModel.setEditableColumns(sEditableColumns);
                }
                
                /**
                 * Nascondo le colonne indesiderate 
                 */
                if (sInvisibleColumns != null)
                {
                        for (int i=0; i < sInvisibleColumns.length; i++)
                        {
                                table.getColumnModel().getColumn(table.getColumnModel().getColumnIndex(sInvisibleColumns[i])).setMaxWidth(0);
                                table.getColumnModel().getColumn(table.getColumnModel().getColumnIndex(sInvisibleColumns[i])).setMinWidth(0);
                                table.getColumnModel().getColumn(table.getColumnModel().getColumnIndex(sInvisibleColumns[i])).setPreferredWidth(0);
                        }       
                }
                
                /**
                 * Filtraggio per non mostrare le righe indesiderate
                 */
                ApplicaRegexFilter();
                
                /**
                 * Imposto le colonne come non-resizable
                 */
                /*for (int i=0; i < table.getColumnCount(); i++)
                {
                        table.getColumnModel().getColumn(i).setResizable(false);
                }*/
                
                /**
                 * Imposto le colonne come unmovable e le righe non riordinabili
                 */
                table.getTableHeader().setReorderingAllowed(false);
                
                /**
                 * Imposto la larghezza delle colonne se necessario
                 */
                if (columnsWidth != null)
                {
                        for (int i=0; i < columnsWidth.size(); i++)
                        {
                                Integer iCol = table.getColumnModel().getColumnIndex(columnsWidth.get(i).get(0));
                                Integer iWidth = Integer.parseInt(columnsWidth.get(i).get(1).toString());
                                table.getColumnModel().getColumn(iCol).setPreferredWidth(iWidth);
                        }
                }
        }
        
        private void ApplicaRegexFilter()
        {
                if ((sRegexFilter != null) && (sColumnFilter != null))
                {
                        if (sRegexFilter.length == sColumnFilter.length)
                        {
                                switch (sRegexFilter.length)
                                {
                                case 1:
                                        sorter.setRowFilter(RowFilter.regexFilter(sRegexFilter[0], TblModel.findColumn(sColumnFilter[0])));
                                        break;
                                case 2:
                                        java.util.List<RowFilter<Object,Object>> filters = new ArrayList<RowFilter<Object,Object>>(3);  
                            filters.add(RowFilter.regexFilter(sRegexFilter[0], TblModel.findColumn(sColumnFilter[0])));  
                            filters.add(RowFilter.regexFilter(sRegexFilter[1], TblModel.findColumn(sColumnFilter[1])));    
        
                            RowFilter<Object,Object> serviceFilter = RowFilter.andFilter(filters); 
                                        sorter.setRowFilter(serviceFilter);
                                        break;
                                default:
                                        break;
                                }
                        }
                }
        }
        
        /**
         * Ricavo il valore della riga selezionata identificato dal campo sColumnName nella JTable
         * @param sColumnName - nome colonna in cui leggere il valore
         * @return output - valore cercato
         */
        public Object getSelectedValue(String sColumnName)
        {
                Integer iRow;
                Object output;
                
                /**
                 * Ricavo il numero di riga selezionata nella JTable
                 */
                iRow = table.getSelectedRow();
                
                if (iRow != -1)
                {
                        /**
                         * Ricavo il valore della colonna indicata da sColumnName
                         */
                        //output = table.getValueAt(iRow, table.getColumnModel().getColumnIndex(sColumnName)).toString();
                        output = table.getValueAt(iRow, table.getColumnModel().getColumnIndex(sColumnName));
                        
                        return output;
                }
                else
                {
                        return null;
                }
                        
        }

        /**
         * Importa i dati in ingresso cancellando quello che è già presente
         * @param data_input - la matrice dei dati
         * @param sColumn - il vettore dei nomi colonna
         */
        public void ImportaDati(Vector<Vector<Object>> data_input, Vector<String> sColumn)
        {       
                /**
                 * Carico i nomi delle colonne
                 */
                sColumnName = null;
                if (sColumn == null)
                {
                        return;
                }
                sColumnName = new Vector<String>();     
                sColumnName = sColumn;
                
                /**
                 * Carico i dati
                 */
                data = null;
                if (data_input == null)
                {
                        return;         
                }
                data = new Vector<Vector<Object>>();
                data = data_input;
        }

        public void AggiungiDati(Vector<Vector<Object>> data_input, Vector<String> sColumn)
        {
                /**
                 * Creo le colonne se necessario
                 */
                if (sColumnName == null)
                {
                        sColumnName = new Vector<String>();
                }
                sColumnName = sColumn;
                
                /**
                 * Se la struttura dei dati che sto aggiungendo non è la stessa di quelli presenti esco subito
                 */
                for (int i=0; i < sColumn.size(); i++)
                {
                        if (sColumn.get(i) != sColumnName.get(i))
                        {
                                return;
                        }
                }
                
                /**
                 * Aggiungo i dati
                 */
                if (data == null)
                {
                        data = new Vector<Vector<Object>>();    
                }
                
                for (int i=0; i < data_input.size(); i++)
                {
                        data.add(data_input.get(i));
                }
        }

        /**
         * Controlla se nella struttura dati è già presente il valore obj nella colonna sColumn.
         * E' necessario che sColumnName sia già avvalorato
         * @param sColumn - colonna in cui cercare
         * @param obj - valore da cercare
         * @return - true se presente, false altrimenti
         */
        public boolean IsPresent(String sColumn, Object obj)
        {
                /**
                 * Se sColumnName e la matrice "data" sono vuoti esco subito
                 */
                if (sColumnName == null)
                {
                        return false;
                }
                if (data == null)
                {
                        return false;
                }
                
                /**
                 * Controllo se il valore obj è presente nella colonna sColumn
                 */
                boolean bFound = false;
                int i = 0;
                while ((i < data.size()) && (bFound == false))
                {
                        Object obj_tmp = data.get(i).get(sColumnName.indexOf(sColumn));
                        if (obj_tmp.equals(obj))
                        {
                                bFound = true;
                        }
                        i++;
                }
                
                return bFound;  
        }

        /**
         * Elimino dalla matrice "data" le righe i cui valori alla colonna sColumn 
         * sono uguali a obj
         * @param obj - valore da confrontare
         * @param sColumn - colonna usata per la selezione
         */
        public void RimuoviRowDati(Object obj, String sColumn)
        {
                int i = 0;
                
                while ((i < data.size()) && (data.size() > 0))
                {
                        Object obj_tmp = data.get(i).get(sColumnName.indexOf(sColumn));
                        if (obj.equals(obj_tmp))
                        {
                                data.remove(i);
                        }
                        else
                        {
                                i++;
                        }
                }
        }
        
        /**
         * Elimino dalla matrice "data" la riga iDataRow 
         * @param iDataRow - indice della riga da eliminare
         */
        public void RimuoviRowDati(int iDataRow)
        {
                data.remove(iDataRow);
        }
        
        public void SvuotaDati()
        {
                if (data != null)
                {
                        data.clear();   
                }
        }
        
        public Vector<Vector<Object>> GetDataVector()
        {
                return data;
        }
        
        /**
         * Ricava una matrice "data" con solo le colonne espresse in sColumns e tale per cui i campi espressi in sCondCols sono uguali
         * a quelli presenti in sCondValues
         * @param sColumns - colonne che compongono la matrice di uscita
         * @param sCondCols - colonne sulle quali applicare la condizione per la selezione
         * @param sCondValues - valori da controllare per applicare la selezione
         * @return - matrice "data"
         */
        public Vector<Vector<Object>> GetDataVector(Vector<String> sColumns, Vector<String> sCondCols, Vector<Object> sCondValues)
        {
                /**
                 * Se sCondCols ha un numero di elementi diverso da sCondValues esco subito
                 */
                if ((sCondCols != null) && (sCondValues != null))
                {               
                        if(sCondCols.size() != sCondValues.size())
                        {
                                return null;
                        }
                }
        
                /**
                 * Ricavo la matrice "data" di uscita
                 */
                Vector<Vector<Object>> output = new Vector<Vector<Object>>();
                boolean bChecked;
                
                if (data != null)
                {
                        for (int i=0; i < data.size(); i++)
                        {
                                /**
                                 * Creo una riga di appoggio
                                 */
                                Vector<Object> outRow = new Vector<Object>();
                                
                                /**
                                 * Verifico che le condizioni siano rispettate
                                 */
                                bChecked = true;
                                if ((sCondCols != null) && (sCondValues != null))
                                {
                                        for (int j=0; j < sCondCols.size(); j++)
                                        {
                                                if (data.get(i).get(getColumnIndex(sCondCols.get(j))).equals(sCondValues.get(j)) == false)
                                                {
                                                        bChecked = false;
                                                }
                                        }       
                                }
                                
                                /**
                                 * Se le condizioni sono rispettate aggiorno la matrice "data" di uscita
                                 */
                                if (bChecked == true)
                                {
                                        for (int j=0; j < sColumns.size(); j++)
                                        {
                                                
                                                outRow.add(data.get(i).get(getColumnIndex(sColumns.get(j))));
                                        }
                                        
                                        output.add(outRow);     
                                }
                        }
                }
                
                return output;
        }
        
        public boolean IsRowSelected(Integer iRow)
        {
                return table.isRowSelected(iRow);
        }
        
        public void addMioEventoListener(MioEventoListener listener) 
    {
        listenerList.add(MioEventoListener.class, listener);
    }
        
        private void fireMyEvent(MioEvento evt) 
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
        
        public void tableChanged(TableModelEvent e)
        {
                for (int i=e.getFirstRow(); i <= e.getLastRow(); i++)
                {
                        if (TblModel.isCellEditable(i, e.getColumn()) == true)
                        {
                                fireMyEvent(new MioEvento(e));
                        }
                }
        }
        
        /**
         * Elimina dalla matrice "data" le ripetizioni dei valori nella colonna sColumn
         * @param sColumn - colonna in cui verificare le ripetizioni
         */
        private Vector<Vector<Object>> FilterUniqueValue()
        {
                Object firstObj = new Object();
                Object secondObj = new Object();
                
                /**
                 * Creo una copia della matrice "data"
                 */
                Vector<Vector<Object>> output = new Vector<Vector<Object>>();
                for (int i=0; i < data.size(); i++)
                {
                        output.add(data.get(i));
                }
                
                /**
                 * Ciclo sulla matrice "output" per eliminare le duplicazioni
                 */
                for (int i=0; i < output.size(); i++)
                {
                        int j = i + 1;
                        while (j < output.size())
                        {
                                firstObj = output.get(i).get(sColumnName.indexOf(sColumnUnique));
                                secondObj = output.get(j).get(sColumnName.indexOf(sColumnUnique));
                                
                                if (firstObj.equals(secondObj))
                                {
                                        output.remove(j);
                                }
                                else
                                {
                                        j++;
                                }
                        }
                }
                
                return output;
        }
        
        public JTable getTable()
        {
                return table;
        }
}