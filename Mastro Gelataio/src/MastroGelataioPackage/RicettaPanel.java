package MastroGelataioPackage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import java.awt.Dimension;
import java.util.Vector;

public class RicettaPanel extends JPanel {

        private SelezionaIngredientiPanel pnlSelectIngredienti;
        private RicettaComposizionePanel pnlComposizione;
        private DBMgrWrap DBMgr;
        
        private int pnlSel_X, pnlSel_Y, pnlSel_Width, pnlSel_Height;
        private int pnlCom_X, pnlCom_Y, pnlCom_Width, pnlCom_Height;
        
        /**
         * Create the panel.
         */
        public RicettaPanel(DBMgrWrap prtDBMgr) 
        {       
                setLayout(null);
                
                DBMgr = new DBMgrWrap();
                DBMgr = prtDBMgr;
                
                CalcolaCoordinate();
                
                /**
                 * Creo il JPanel della lista degli Ingredienti
                 */
                pnlSelectIngredienti = new SelezionaIngredientiPanel(prtDBMgr);
                pnlSelectIngredienti.setBounds(pnlSel_X, pnlSel_Y, pnlSel_Width, pnlSel_Height);
                pnlSelectIngredienti.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
                add(pnlSelectIngredienti);
                
                /**
                 * Creo il JPanel della composizione della Ricetta
                 */
                pnlComposizione = new RicettaComposizionePanel(prtDBMgr);
                pnlComposizione.setBounds(pnlCom_X, pnlCom_Y, pnlCom_Width, pnlCom_Height);
                pnlComposizione.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
                add(pnlComposizione);
                
                /**
                 * Creo il listener per l'evento di aggiunta ingrediente
                 */
                pnlSelectIngredienti.addMioEventoListener(new MioEventoListener() {
                    public void myEventOccurred(MioEvento evt) {
                        if (evt.getSourceObject().toString() != "Rimuovi")
                        {
                                pnlComposizione.AggiungiIngrediente(Long.parseLong(evt.getSourceObject().toString()), 0.0);     
                        }
                    }
                });
                
                /**
                 * Creo il listener per l'evento di rimozione ingrediente
                 */
                pnlSelectIngredienti.addMioEventoListener(new MioEventoListener() {
                    public void myEventOccurred(MioEvento evt) {
                        if (evt.getSourceObject().toString() == "Rimuovi")
                        {
                                pnlComposizione.RimuoviIngrediente();   
                        }
                    }
                });
        }
        
        private void CalcolaCoordinate()
        {
                pnlSel_X = 0;
                pnlSel_Y = 0;
                pnlSel_Width = 300;
                pnlSel_Height = getHeight();
                
                pnlCom_X = pnlSel_X + pnlSel_Width + 20;
                pnlCom_Y = pnlSel_Y;
                pnlCom_Width = getWidth() - pnlCom_X;
                pnlCom_Height = pnlSel_Height;
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
                pnlSelectIngredienti.CambiaSize(pnlSel_X, pnlSel_Y, pnlSel_Width, pnlSel_Height);
                pnlComposizione.CambiaSize(pnlCom_X, pnlCom_Y, pnlCom_Width, pnlCom_Height);
        }
        
        public void CaricaPannello()
        {       
                pnlSelectIngredienti.CaricaIngredienti();
                pnlComposizione.CaricaBasi();
                pnlComposizione.AggiornaIngredienti();
        }
        
        public void TrovaIDNuovaRicetta()
        {
                pnlComposizione.TrovaIDNuovaRicetta();
        }
        
        public void ApriRicetta(Vector<Vector<Object>> data_ing, Vector<String> sColumns)
        {
                /**
                 * Se il dato in ingresso è null o vuoto allora svuoto la tabella
                 */
                if (data_ing == null)
                {
                        pnlComposizione.SvuotaTabelle();
                        pnlComposizione.setRicettaNome("");
                        return;
                }
                if (data_ing.size() == 0)
                {
                        pnlComposizione.SvuotaTabelle();
                        pnlComposizione.setRicettaNome("");
                        return;
                }
                
                /**
                 * Carico il nome della ricetta
                 */
                pnlComposizione.setRicettaNome(RicavaNomeRicetta(Long.parseLong(data_ing.get(0).get(sColumns.indexOf("ID")).toString())));
                pnlComposizione.setRicettaID(Integer.parseInt(data_ing.get(0).get(sColumns.indexOf("ID")).toString()));
                
                /**
                 * Carico le Note
                 */
                pnlComposizione.setNote(RicavaNote(Long.parseLong(data_ing.get(0).get(sColumns.indexOf("ID")).toString())));
                
                /**
                 * Carico gli ingredienti e le quantità in tabella di Composizione
                 */
                pnlComposizione.SvuotaTabelle();
                for (int i=0; i < data_ing.size(); i++)
                {
                        Long iID;
                        Double dQuant;
                        
                        iID = Long.parseLong(data_ing.get(i).get(sColumns.indexOf("ID_Ing")).toString());
                        dQuant = Double.parseDouble(data_ing.get(i).get(sColumns.indexOf("Quantità")).toString());
                        
                        pnlComposizione.AggiungiIngrediente(iID, dQuant);       
                }
        }
        
        /**
         * Ricava il nome della ricetta determinata dall'ID
         * @param iID - ID della ricetta corrispondente alla colonna ID nella tabella Ricette
         * @return output - il nome della ricetta corrispondente alla colonna Nome nella tabella Ricette
         */
        private String RicavaNomeRicetta(Long iID)
        {
                String output;
                
                Vector<Vector<Object>> nomeRicetta = new Vector<Vector<Object>>();
                nomeRicetta = DBMgr.Select("Ricette", " WHERE ID=" + iID);
        
                output = nomeRicetta.get(0).get(1).toString();
                
                return output;
        }
        
        /**
         * Ricava le Note della ricetta determinata dall'ID
         * @param iID - ID della ricetta corrispondente alla colonna ID nella tabella Ricette
         * @return output - le Note della ricetta corrispondente alla colonna Note nella tabella Ricette
         */
        private String RicavaNote(Long iID)
        {
                String output;
                
                Vector<Vector<Object>> nomeRicetta = new Vector<Vector<Object>>();
                nomeRicetta = DBMgr.Select("Ricette", " WHERE ID=" + iID);
        
                output = nomeRicetta.get(0).get(2).toString();
                
                return output;
        }
}