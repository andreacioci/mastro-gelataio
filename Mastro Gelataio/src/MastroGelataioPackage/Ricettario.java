package MastroGelataioPackage;

import java.awt.Dimension;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class Ricettario extends JPanel {

	DBMgrWrap DBMgr;
	SelezionaRicettaPanel pnlSelectRicetta;
	RicettaComposizionePanel pnlComposizione;
	
	private int pnlSel_X, pnlSel_Y, pnlSel_Width, pnlSel_Height;
	private int pnlRic_X, pnlRic_Y, pnlRic_Width, pnlRic_Height;
	
	/**
	 * Create the panel.
	 */
	public Ricettario(DBMgrWrap prtDBMgr) 
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
		add(pnlSelectRicetta);
		
		/**
		 * Creo il JPanel della composizione della Ricetta
		 */
		pnlComposizione = new RicettaComposizionePanel(prtDBMgr);
		pnlComposizione.setBounds(pnlRic_X, pnlRic_Y, pnlRic_Width, pnlRic_Height);
		pnlComposizione.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		pnlComposizione.setRicettaEditable(false);
		add(pnlComposizione);
		
		/**
		 * Creo il listener per l'evento di selezione della ricetta
		 */
		pnlSelectRicetta.addMioEventoListener(new MioEventoListener() {
		    public void myEventOccurred(MioEvento evt) {
		    	
		    	if (evt.getMioEventoName() == "Modifica")
		    	{
		    		ModificaRicetta(evt);
		    	}
		    	if (evt.getMioEventoName() == "Apri")
		    	{
		    		ApriRicetta(evt);
		    	}
		    	if (evt.getMioEventoName() == "Elimina")
		    	{
		    		EliminaRicetta(evt);
		    	}
		    }
		});
	}
	
	public void addMioEventoListener(MioEventoListener listener) 
    {
        listenerList.add(MioEventoListener.class, listener);
    }
	
	private void CalcolaCoordinate()
	{
		pnlSel_X = 0;
		pnlSel_Y = 0;
		pnlSel_Width = 230;
		pnlSel_Height = (int)(getHeight() * 0.60);
		
		pnlRic_X = pnlSel_X + pnlSel_Width + 20;
		pnlRic_Y = pnlSel_Y;
		pnlRic_Width = getWidth() - pnlRic_X - 40;
		pnlRic_Height = getHeight() - pnlRic_Y;
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
		pnlComposizione.CambiaSize(pnlRic_X, pnlRic_Y, pnlRic_Width, pnlRic_Height);
	}
	
	private void ApriRicetta(MioEvento evt)
	{
		Vector<String> sColumns = new Vector<String>();
		sColumns.add("ID");
		sColumns.add("ID_Ing");
		sColumns.add("Quantita");
		
		ApriRicetta(evt.getSourceVector(), sColumns);	
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
		 * Carico gli ingredienti e le quantità  in tabella di Composizione
		 */
		pnlComposizione.SvuotaTabelle();
		for (int i=0; i < data_ing.size(); i++)
		{
			Long iID;
			Double dQuant;
			
			iID = Long.parseLong(data_ing.get(i).get(sColumns.indexOf("ID_Ing")).toString());
			dQuant = Double.parseDouble(data_ing.get(i).get(sColumns.indexOf("Quantita")).toString());
			
			pnlComposizione.AggiungiIngrediente(iID, dQuant);	
		}
		
		/**
         * Setto le quantitÃ  come non editabili perchÃ¨ sono in consultazione
         */
        pnlComposizione.setRicettaEditable(false);
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
	
	public void CaricaRicette()
	{
		pnlSelectRicetta.CaricaRicette();
	}
	
	/**
	 * Viene chiamata quando il pulsante Modifica viene premuto nel SelezionaRicettaPanel.
	 * Genera un evento per l'applicazione padre in modo che possa essere aperta la ricetta in un nuovo pannello.
	 */
	private void ModificaRicetta(MioEvento evt)
	{
		fireMyEvent(evt);
	}
	
	/**
	 * Viene chiamata quando il pulsante Elimina viene premuto nel SelezionaRicettaPanel.
	 * Svuota la ricetta mostrata nel RicettaComposizionePanel.
	 */
	private void EliminaRicetta(MioEvento evt)
	{
		pnlComposizione.SvuotaTabelle();
		pnlComposizione.setRicettaNome("");
	}
	
	/**
	 * Viene chiamata per verificare se sono state fatte modifiche al ricettario
	 * (tipo eliminare una ricetta) senza aver salvato.
	 * @return - true = modifica fatta ma non salvata, false = altrimenti
	 */
	public boolean IsModified()
	{
		return pnlSelectRicetta.IsModified();
	}
	
	/**
	 * Viene chiamata per settare se sono state fatte modifiche al ricettario
	 * (tipo eliminare una ricetta) senza aver salvato.
	 * @return - true = modifica fatta ma non salvata, false = altrimenti
	 */
	public void SetModified(boolean bInput)
	{
		pnlSelectRicetta.SetModified(bInput);
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
}
