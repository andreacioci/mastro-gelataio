package MastroGelataioPackage;

import java.sql.DriverManager;
import java.util.Vector;

public class DBMgrWrap extends DBManager{

	/**
	 * DB File
	 */
	public static final String DBPath = "";
	public static final String DBFile = "Gelati31.db";
	
	/**
	 * Tabella Ricette
	 */
	public static final String TABELLA_RICETTE = "Ricette";
	public static final int RICETTE_ID = 0;
	public static final int RICETTE_Nome = 1;
	public static final int RICETTE_Note = 2;
	public static final Vector<String> RICETTE_COLS = new Vector<String>() {};
	
	/**
	 * Tabella Tipi Ingredienti
	 */
	public static final String TABELLA_TIPI_ING = "Tipi_Ingredienti";
	public static final int TIPI_ING_ID = 0;
	public static final int TIPI_ING_Nome = 1;
	public static final int TIPI_ING_Deleted = 2;
	public static final Vector<String> TIPI_ING_COLS = new Vector<String>() {};
	
	/**
	 * Tabella Dettagli Ingredienti
	 */
	public static final String TABELLA_INGREDIENTI = "Ingredienti";
	public static final int INGREDIENTI_ID = 0;
	public static final int INGREDIENTI_Nome = 1;
	public static final int INGREDIENTI_Acqua = 2;
	public static final int INGREDIENTI_Zuccheri = 3;
	public static final int INGREDIENTI_Grassi = 4;
	public static final int INGREDIENTI_SLNG = 5;
	public static final int INGREDIENTI_AltriSolidi = 6;
	public static final int INGREDIENTI_Deleted = 7;
	public static final int INGREDIENTI_TipoIngID = 8;
	public static final int INGREDIENTI_POD = 9;
	public static final int INGREDIENTI_PAC = 10;
	public static final Vector<String> INGREDIENTI_COLS = new Vector<String>() {};
	
	/**
	 * Tabella Basi
	 */
	public static final String TABELLA_BASI = "Basi";
	public static final int BASI_ID = 0;
	public static final int BASI_Nome = 1;
	public static final Vector<String> BASI_COLS = new Vector<String>() {};
	
	/**
	 * Tabella Composizione
	 */
	public static final String TABELLA_COMPOSIZIONE = "Composizione";
	public static final int COMPOSIZIONE_ID = 0;
	public static final int COMPOSIZIONE_ID_Ing = 1;
	public static final int COMPOSIZIONE_Quantità = 2;
	public static final Vector<String> COMPOSIZIONE_COLS = new Vector<String>() {};
	
	/**
	 * Tabella Soglie
	 */
	public static final String TABELLA_SOGLIE = "Soglie";
	public static final int SOGLIE_ID = 0;
	public static final int SOGLIE_Composizione = 1;
	public static final int SOGLIE_Min = 2;
	public static final int SOGLIE_Max = 3;
	public static final Vector<String> SOGLIE_COLS = new Vector<String>() {};
	
	public DBMgrWrap()
	{
		TIPI_ING_COLS.add("ID");
		TIPI_ING_COLS.add("Nome");
		TIPI_ING_COLS.add("Deleted");
		
		INGREDIENTI_COLS.add("ID");
		INGREDIENTI_COLS.add("Nome");
		INGREDIENTI_COLS.add("Acqua");
		INGREDIENTI_COLS.add("Zuccheri");
		INGREDIENTI_COLS.add("Grassi");
		INGREDIENTI_COLS.add("SLNG");
		INGREDIENTI_COLS.add("AltriSolidi");
		INGREDIENTI_COLS.add("Deleted");
		INGREDIENTI_COLS.add("TipoIngID");
		INGREDIENTI_COLS.add("POD");
		INGREDIENTI_COLS.add("PAC");
		
		RICETTE_COLS.add("ID");
		RICETTE_COLS.add("Nome");
		RICETTE_COLS.add("Note");
		
		COMPOSIZIONE_COLS.add("ID");	
		COMPOSIZIONE_COLS.add("ID_Ing");
		COMPOSIZIONE_COLS.add("Quantità");
		
		BASI_COLS.add("ID");
		BASI_COLS.add("Nome");
		
		SOGLIE_COLS.add("ID");
		SOGLIE_COLS.add("Composizione");
		SOGLIE_COLS.add("Min");
		SOGLIE_COLS.add("Max");
	}
	
	public void ConnectDB() throws Exception 
	{
		Class.forName("org.sqlite.JDBC");
	    conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath + DBFile); 
	    stat = conn.createStatement();
	  
	    /**
    	 * Creo la Tabella dei Tipi Ingredienti
    	 */
	    try
	    {	
	    	stat.execute("CREATE TABLE " + TABELLA_TIPI_ING + " (ID Long PRIMARY KEY, Nome varchar(50), Deleted varchar(1), UNIQUE(Nome));");
	    }
	    catch (Exception e)
	    {
	    }
	    
	    /**
    	 * Creo la Tabella degli Ingredienti
    	 */
	    try
	    {
	    	stat.execute("CREATE TABLE " + TABELLA_INGREDIENTI + " (ID Long, Nome varchar(50), Acqua Double, "
	    			+ "Zuccheri Double, Grassi Double, SLNG Double, AltriSolidi Double, "
	    			+ "Deleted varchar(1), TipoIngID Long, POD Long, PAC Long, PRIMARY KEY (ID));");	
	    }
	    catch (Exception e)
	    {
	    }
	    
	    /**
    	 * Creo la Tabella delle Ricette
    	 */
	    try
	    {	
	    	stat.execute("CREATE TABLE " + TABELLA_RICETTE + " (ID Long PRIMARY KEY, Nome varchar(50), "
	    			+ "Note varchar(512));");
	    }
	    catch (Exception e)
	    {
	    }
	    
	    /**
    	 * Creo la Tabella della Composizione delle ricette
    	 */
	    try
	    {	
	    	stat.execute("CREATE TABLE " + TABELLA_COMPOSIZIONE + " (ID Long, ID_Ing Long, Quantità Double, "
	    			+ "PRIMARY KEY (ID, ID_Ing));");
	    }
	    catch (Exception e)
	    {
	    }
	    
	    /**
    	 * Creo la Tabella delle Basi
    	 */
	    try
	    {	
	    	stat.execute("CREATE TABLE " + TABELLA_BASI + " (ID Long PRIMARY KEY, Nome varchar(50), UNIQUE (ID,Nome));");
	    }
	    catch (Exception e)
	    {
	    }
	    
	    /**
    	 * Creo la Tabella delle Soglie
    	 */
	    try
	    {	
	    	stat.execute("CREATE TABLE " + TABELLA_SOGLIE +" (ID Long, Composizione varchar(50), Min Double, Max Double, PRIMARY KEY(ID,Composizione));");
	    }
	    catch (Exception e)
	    {
	    }
	}
	
	public void DisconnectDB()
	{
		try
		{
			conn.close();
		}
		catch (Exception e)
		{
			
		}		
	}
	
	/**
	 * Estrae tutta la tabella degli Ingredienti
	 * @return output - array tipo Ingredienti
	 */
	public Vector<Ingredienti> SelectIngredienti(String sCondizione)
	{	
		/**
		 * Ricavo i dati dalla tabella 
		 */
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		if (sCondizione == null)
		{
			sCondizione = "";
		}
		data = Select(TABELLA_INGREDIENTI, sCondizione);
		
		/**
		 * Converto i dati nel formato desiderato
		 */
		Vector<Ingredienti> output = new Vector<Ingredienti>();
		output = Object2Ingredienti(data);
		
		return output;
	}
	
	/**
	 * Converte il vettore di vettori di Object in un vettore di Ingredienti
	 * @param data
	 * @return Vector<Ingredienti>
	 */
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
			 * Copio dal vettore di appoggio in un oggetto Ingredienti
			 */
			Ingredienti ing = new Ingredienti();
			ing.setID(Integer.parseInt(data_obj.get(INGREDIENTI_ID).toString()));
			if (data_obj.get(INGREDIENTI_Nome) == null)
			{
				data_obj.set(INGREDIENTI_Nome, "Senza Nome");
			}
			ing.setName(data_obj.get(INGREDIENTI_Nome).toString());
			if (data_obj.get(INGREDIENTI_Acqua) == null)
			{
				data_obj.set(INGREDIENTI_Acqua, 0);
			}
			ing.setAcqua(Double.parseDouble(data_obj.get(INGREDIENTI_Acqua).toString()));
			if (data_obj.get(INGREDIENTI_Zuccheri) == null)
			{
				data_obj.set(INGREDIENTI_Zuccheri, 0);
			}
			ing.setZuccheri(Double.parseDouble(data_obj.get(INGREDIENTI_Zuccheri).toString()));
			if (data_obj.get(INGREDIENTI_Grassi) == null)
			{
				data_obj.set(INGREDIENTI_Grassi, 0);
			}
			ing.setGrassi(Double.parseDouble(data_obj.get(INGREDIENTI_Grassi).toString()));
			if (data_obj.get(INGREDIENTI_SLNG) == null)
			{
				data_obj.set(INGREDIENTI_SLNG, 0);
			}
			ing.setSLNG(Double.parseDouble(data_obj.get(INGREDIENTI_SLNG).toString()));
			if (data_obj.get(INGREDIENTI_AltriSolidi) == null)
			{
				data_obj.set(INGREDIENTI_AltriSolidi, 0);
			}
			ing.setAltriSolidi(Double.parseDouble(data_obj.get(INGREDIENTI_AltriSolidi).toString()));
			if (data_obj.get(INGREDIENTI_Deleted) == null)
			{
				data_obj.set(INGREDIENTI_Deleted, "N");
			}
			ing.setDeleted(data_obj.get(INGREDIENTI_Deleted).toString());
			if (data_obj.get(INGREDIENTI_POD) == null)
			{
				data_obj.set(INGREDIENTI_POD, 0);
			}
			ing.setPOD(Long.parseLong(data_obj.get(INGREDIENTI_POD).toString()));
			if (data_obj.get(INGREDIENTI_PAC) == null)
			{
				data_obj.set(INGREDIENTI_PAC, 0);
			}
			ing.setPAC(Long.parseLong(data_obj.get(INGREDIENTI_PAC).toString()));
			
			/**
			 * Inserisco il vettore Ingredienti nel vettore di uscita
			 */
			output.add(ing);
		}
		return output;
	}
	
	public boolean UpdateTipiIng(Vector<Vector<Object>> dataNew)
	{
		boolean output = true;
		boolean bEseguito = false;
		
		/**
		 * Ciclo su tutte le righe di dataNew
		 */
		for (int i=0; i < dataNew.size(); i++)
		{
			Vector<Object> dataNewRow = new Vector<Object>();
			dataNewRow = dataNew.get(i);
			
			/**
			 * Faccio l'UPDATE della tabella Tipi Ingredienti
			 */
			String sQuery;
			sQuery = "UPDATE " + TABELLA_TIPI_ING + " SET " 
					+ "Deleted='" + dataNewRow.get(TIPI_ING_Deleted).toString() + "', "
					+ "Nome='" + dataNewRow.get(TIPI_ING_Nome).toString() + "'"
					+ " WHERE "
					+ "ID=" + dataNewRow.get(TIPI_ING_ID).toString();;	
			
			/**
			 * Eseguo la query
			 */
			bEseguito = EseguiQuery(sQuery);
			
			/**
			 * Se la query di UPDATE è andata male vuol dire che si tratta di una nuova riga
			 */
			if (bEseguito == false)
			{
				sQuery = "INSERT INTO " + TABELLA_TIPI_ING + " VALUES (" 
						+ dataNewRow.get(TIPI_ING_ID).toString() + ", '"
						+ dataNewRow.get(TIPI_ING_Nome).toString() + "', '"	
						+ dataNewRow.get(TIPI_ING_Deleted).toString() + "')";
				
				/**
				 * Eseguo la query
				 */
				bEseguito = EseguiQuery(sQuery);	
			}
			
			output = output && bEseguito;
		}
		
		return output;
	}
	
	/**
	 * Aggiorna tutta la tabella Ingredienti
	 * @param data - dati provienti dalla DefaultTableModel
	 */
	public boolean UpdateAllIngredienti(Vector<Vector<Object>> dataNew)
	{
		String sQuery = "";
		boolean bEseguito;
		boolean output = true;
		
		/**
		 * Costruisco una query per ogni riga e la eseguo
		 */
		for (int i=0; i < dataNew.size(); i++)
		{
			Vector<Object> dataNewRow = new Vector<Object>();
			dataNewRow = dataNew.get(i);
		
			bEseguito = false;
			/**
			 * Costruisco la query di UPDATE
			 */
			sQuery = "";
			
			sQuery = "UPDATE " + TABELLA_INGREDIENTI + " SET "
					+ "ID=" + dataNewRow.get(INGREDIENTI_ID) + ", " 
					+ "Nome='" + dataNewRow.get(INGREDIENTI_Nome) + "', "
					+ "Acqua=" + dataNewRow.get(INGREDIENTI_Acqua) + ", "
					+ "Zuccheri=" + dataNewRow.get(INGREDIENTI_Zuccheri) + ", "
					+ "Grassi=" + dataNewRow.get(INGREDIENTI_Grassi) + ", "
					+ "SLNG=" + dataNewRow.get(INGREDIENTI_SLNG) + ", "
					+ "AltriSolidi=" + dataNewRow.get(INGREDIENTI_AltriSolidi) + ", " 
					+ "Deleted='" + dataNewRow.get(INGREDIENTI_Deleted) + "', "
					+ "TipoIngID=" + dataNewRow.get(INGREDIENTI_TipoIngID) + ", "
					+ "POD=" + dataNewRow.get(INGREDIENTI_POD) + ", "
					+ "PAC=" + dataNewRow.get(INGREDIENTI_PAC) + " "
					+ "WHERE ID=" + dataNewRow.get(INGREDIENTI_ID);
			
			/**
			 * Eseguo la query
			 */
			try
			{
				if (sQuery != "")
				{
					if (stat.executeUpdate(sQuery) != 0)
					{
						bEseguito = true;
					}
				}	
			}
			catch (Exception e)
			{	
				bEseguito = false;
			}
			
			/**
			 * Se la query di UPDATE è andata male vuol dire che si tratta di una nuova riga
			 */
			if (bEseguito == false)
			{
				/**
				 * Costruisco la query di INSERT
				 */
				sQuery = "";
				
				sQuery = "INSERT INTO " + TABELLA_INGREDIENTI + " VALUES ("
						+ dataNewRow.get(INGREDIENTI_ID) + ", '" 
						+ dataNewRow.get(INGREDIENTI_Nome) + "', "
						+ dataNewRow.get(INGREDIENTI_Acqua) + ", "
						+ dataNewRow.get(INGREDIENTI_Zuccheri) + ", "
						+ dataNewRow.get(INGREDIENTI_Grassi) + ", "
						+ dataNewRow.get(INGREDIENTI_SLNG) + ", "
						+ dataNewRow.get(INGREDIENTI_AltriSolidi) + ", '" 
						+ dataNewRow.get(INGREDIENTI_Deleted) + "', "
						+ dataNewRow.get(INGREDIENTI_TipoIngID) + ", "
						+ dataNewRow.get(INGREDIENTI_POD) + ", "
						+ dataNewRow.get(INGREDIENTI_PAC) + ")";
				
				/**
				 * Eseguo la query
				 */
				try
				{
					if (sQuery != "")
					{
						if (stat.executeUpdate(sQuery) != 0)
						{
							bEseguito = true;
						}
					}	
				}
				catch (Exception e)
				{	
					bEseguito = false;
				}
				
				output = output && bEseguito;
			}
		}
		
		return output;
	}
	
	public boolean UpdateComposizioneTable(Vector<Vector<Object>> dataNew)
	{
		boolean output = false;
		
		String sCondizione = "";
		
		sCondizione = " WHERE ID=" + dataNew.get(0).get(COMPOSIZIONE_ID).toString();
		
		Vector<Vector<Object>> dataDB = new Vector<Vector<Object>>(); 
		dataDB = Select("Composizione", sCondizione);
		
		/**
		 * Elimino le righe della tabella che non devono essere più presenti
		 */
		output = DeleteComposizioneRows(dataNew, dataDB);
		
		/**
		 * Aggiorno le righe della tabella che sono state modificate
		 */
		output = output && AggiornoComposizioneRows(dataNew);
		
		return output;
	}
	
	/**
	 * Elimina dalla tabella Composizione quelle righe di dataDB che non sono presenti in dataNew
	 * @param dataNew - Nuova tabella
	 * @param dataDB - Tabella presente nel DB
	 * @return - true se l'aggiornamento ha avuto successo, false altrimenti
	 */
	private boolean DeleteComposizioneRows(Vector<Vector<Object>> dataNew, Vector<Vector<Object>> dataDB)
	{
		boolean output = true;
		
		/**
		 * Ciclo su tutte le righe della tabella del DB
		 */
		for (int i=0; i < dataDB.size(); i++)
		{
			/**
			 * Estraggo la riga
			 */
			Vector<Object> dataDBRow = new Vector<Object>();
			dataDBRow = dataDB.get(i);
			
			/**
			 * Ciclo su tutte le righe della matrice "data" 
			 */
			int j = 0;
			boolean bFound = false;
			while ((j < dataNew.size()) && (bFound == false))
			{
				/**
				 * Estraggo la riga dalla matrice "data"
				 */
				Vector<Object> dataNewRow = new Vector<Object>();
				dataNewRow = dataNew.get(j);
				
				/**
				 * Confronto le due righe
				 */
				if(dataDBRow.get(COMPOSIZIONE_ID).equals(dataNewRow.get(COMPOSIZIONE_ID)) == true)
				{
					if (dataDBRow.get(COMPOSIZIONE_ID_Ing).equals(dataNewRow.get(COMPOSIZIONE_ID_Ing)) == true)
					{
						bFound = true;
					}
				}

				j++;
			}
			
			/**
			 * Se la riga è da togliere esegue la Delete
			 */
			if (bFound == false)
			{
				String sQuery = "DELETE FROM " + TABELLA_COMPOSIZIONE + " WHERE "
						+ "ID=" + dataDBRow.get(COMPOSIZIONE_ID).toString() + " AND "
						+ "ID_Ing=" + dataDBRow.get(COMPOSIZIONE_ID_Ing).toString();
			
				try
				{
					if (stat.executeUpdate(sQuery) != 0)
					{
						output = output && true;
					}	
				}
				catch (Exception e)
				{	
					output = false;
				}
			}
		}
		
		return output;
	}
	
	private boolean AggiornoComposizioneRows(Vector<Vector<Object>> dataNew)
	{
		boolean output = true;
		boolean bEseguito = false;
		
		/**
		 * Ciclo su tutte le righe di dataNew
		 */
		for (int i=0; i < dataNew.size(); i++)
		{
			Vector<Object> dataNewRow = new Vector<Object>();
			dataNewRow = dataNew.get(i);
			
			/**
			 * Faccio l'UPDATE della tabella Composizione
			 */
			String sQuery;
			sQuery = "UPDATE " + TABELLA_COMPOSIZIONE + " SET " 
					+ "Quantità=" + dataNewRow.get(COMPOSIZIONE_Quantità).toString() 
					+ " WHERE "
					+ "ID=" + dataNewRow.get(COMPOSIZIONE_ID).toString() + " AND "
					+ "ID_Ing=" + dataNewRow.get(COMPOSIZIONE_ID_Ing).toString();	
			
			/**
			 * Eseguo la query
			 */
			bEseguito = EseguiQuery(sQuery);
			
			/**
			 * Se la query di UPDATE è andata male vuol dire che si tratta di una nuova riga
			 */
			if (bEseguito == false)
			{
				sQuery = "INSERT INTO " + TABELLA_COMPOSIZIONE + " VALUES (" 
						+ dataNewRow.get(COMPOSIZIONE_ID).toString() + ", "
						+ dataNewRow.get(COMPOSIZIONE_ID_Ing).toString() + ", "	
						+ dataNewRow.get(COMPOSIZIONE_Quantità).toString() + ")";
				
				/**
				 * Eseguo la query
				 */
				bEseguito = EseguiQuery(sQuery);	
			}
			
			output = output && bEseguito;
		}
		
		return output;
	}
	
	public boolean AggiornoRicetteTable(Vector<Object> dataNewRow)
	{
		boolean output = true;
		boolean bEseguito = false;

		String sQuery;
		
		/**
		 * Faccio l'UPDATE della tabella Ricette
		 */
		sQuery = "UPDATE " + TABELLA_RICETTE + " SET " 
				+ "Note='" + dataNewRow.get(RICETTE_Note).toString() + "'" 
				+ " WHERE "
				+ "ID=" + dataNewRow.get(RICETTE_ID).toString() + " AND "
				+ "Nome='" + dataNewRow.get(RICETTE_Nome).toString() + "'";	
		
		/**
		 * Eseguo la query
		 */
		bEseguito = EseguiQuery(sQuery);
		
		if (bEseguito == false)
		{
			/**
			 * Faccio l'INSERT della tabella Ricette
			 */
			sQuery = "INSERT INTO " + TABELLA_RICETTE + " VALUES (" 
					+ dataNewRow.get(RICETTE_ID).toString() + ", '"
					+ dataNewRow.get(RICETTE_Nome).toString() + "', '"
					+ dataNewRow.get(RICETTE_Note).toString() + "')";
				
			/**
			 * Eseguo la query
			 */
			bEseguito = EseguiQuery(sQuery);
		}
		
		output = bEseguito;
		return output;
	}
	
	/**
	 * Questa funzione viene chiamata quando è stata cancellata una o più ricette ma le
	 * modifiche non sono state ancora riporate sul DB
	 * @param dataNew - l'elenco aggiornato delle ricette
	 * @return true = salvato, false altrimenti
	 */
	public boolean UpdateRicetteTable(Vector<Vector<Object>> dataNew)
	{
		boolean output = false;
		
		Vector<Vector<Object>> dataDB = new Vector<Vector<Object>>(); 
		dataDB = Select("Ricette", null);
		
		/**
		 * Elimino le righe della tabella che non devono essere più presenti
		 */
		output = DeleteRicetteRows(dataNew, dataDB);
		
		return output;
	}
	
	/**
	 * Elimina dalla tabella Ricette quelle righe di dataDB che non sono presenti in dataNew
	 * @param dataNew - Nuova tabella
	 * @param dataDB - Tabella presente nel DB
	 * @return - true se l'aggiornamento ha avuto successo, false altrimenti
	 */
	private boolean DeleteRicetteRows(Vector<Vector<Object>> dataNew, Vector<Vector<Object>> dataDB)
	{
		boolean output = true;
	
		/**
		 * Ciclo su tutte le righe della tabella del DB
		 */
		for (int i=0; i < dataDB.size(); i++)
		{
			/**
			 * Estraggo la riga
			 */
			Vector<Object> dataDBRow = new Vector<Object>();
			dataDBRow = dataDB.get(i);
			
			/**
			 * Ciclo su tutte le righe della matrice "data" 
			 */
			int j = 0;
			boolean bFound = false;
			while ((j < dataNew.size()) && (bFound == false))
			{
				/**
				 * Estraggo la riga dalla matrice "data"
				 */
				Vector<Object> dataNewRow = new Vector<Object>();
				dataNewRow = dataNew.get(j);
				
				/**
				 * Confronto le due righe
				 */
				if(dataDBRow.get(RICETTE_ID).equals(dataNewRow.get(RICETTE_ID)) == true)
				{
					if (dataDBRow.get(RICETTE_ID).equals(dataNewRow.get(RICETTE_ID)) == true)
					{
						bFound = true;
					}
				}

				j++;
			}
			
			/**
			 * Se la riga è da togliere esegue la Delete
			 */
			if (bFound == false)
			{
				String sQuery = "DELETE FROM " + TABELLA_RICETTE + " WHERE "
						+ "ID=" + dataDBRow.get(RICETTE_ID).toString();
			
				try
				{
					if (stat.executeUpdate(sQuery) != 0)
					{
						output = output && true;
					}	
				}
				catch (Exception e)
				{	
					output = false;
				}
			}
		}
		
		return output;
	}
	
	public boolean UpdateSoglieTable(Vector<Vector<Object>> dataNew)
	{
		boolean output = true;
		boolean bEseguito = false;
		
		/**
		 * Ciclo su tutte le righe di dataNew
		 */
		for (int i=0; i < dataNew.size(); i++)
		{
			Vector<Object> dataNewRow = new Vector<Object>();
			dataNewRow = dataNew.get(i);
			
			/**
			 * Faccio l'UPDATE della tabella Soglie
			 */
			String sQuery;
			sQuery = "UPDATE " + TABELLA_SOGLIE + " SET " 
					+ "Min=" + dataNewRow.get(SOGLIE_Min).toString() + ", "
					+ "Max=" + dataNewRow.get(SOGLIE_Max).toString() + " "
					+ " WHERE "
					+ "ID=" + dataNewRow.get(SOGLIE_ID).toString() + " AND "
					+ "Composizione='" + dataNewRow.get(SOGLIE_Composizione).toString() + "'";	
			
			/**
			 * Eseguo la query
			 */
			bEseguito = EseguiQuery(sQuery);
			
			/**
			 * Se la query di UPDATE è andata male vuol dire che si tratta di una nuova riga
			 */
			if (bEseguito == false)
			{
				sQuery = "INSERT INTO " + TABELLA_SOGLIE + " VALUES (" 
						+ dataNewRow.get(SOGLIE_ID).toString() + ", '"
						+ dataNewRow.get(SOGLIE_Composizione).toString() + "', "	
						+ dataNewRow.get(SOGLIE_Min).toString() + ", "
						+ dataNewRow.get(SOGLIE_Max).toString() + ")";
				
				/**
				 * Eseguo la query
				 */
				bEseguito = EseguiQuery(sQuery);	
			}
			
			output = output && bEseguito;
		}
		
		
		return output;
	}
	
	public boolean UpdateBasiTable(Vector<Vector<Object>> dataNew)
	{
		boolean output = false;
		
		Vector<Vector<Object>> dataDB = new Vector<Vector<Object>>(); 
		dataDB = Select(TABELLA_BASI, null);
		
		/**
		 * Elimino le righe della tabella che non devono essere più presenti
		 */
		output = DeleteBasiRows(dataNew, dataDB);
		
		/**
		 * Aggiorno le righe della tabella che sono state modificate
		 */
		output = output && AggiornoBasiRows(dataNew);
		
		return output;
	}
	
	private boolean DeleteBasiRows(Vector<Vector<Object>> dataNew, Vector<Vector<Object>> dataDB)
	{
		boolean output = true;
		
		/**
		 * Ciclo su tutte le righe della tabella del DB
		 */
		for (int i=0; i < dataDB.size(); i++)
		{
			/**
			 * Estraggo la riga
			 */
			Vector<Object> dataDBRow = new Vector<Object>();
			dataDBRow = dataDB.get(i);
			
			/**
			 * Ciclo su tutte le righe della matrice "data" 
			 */
			int j = 0;
			boolean bFound = false;
			while ((j < dataNew.size()) && (bFound == false))
			{
				/**
				 * Estraggo la riga dalla matrice "data"
				 */
				Vector<Object> dataNewRow = new Vector<Object>();
				dataNewRow = dataNew.get(j);
				
				/**
				 * Confronto le due righe
				 */
				if(dataDBRow.get(BASI_ID).equals(dataNewRow.get(BASI_ID)) == false)
				{
					if (dataDBRow.get(BASI_Nome).equals(dataNewRow.get(BASI_Nome)) == false)
					{
						bFound = true;
					}
				}

				j++;
			}
			
			/**
			 * Se la riga è da togliere esegue la Delete
			 */
			if (bFound == false)
			{
				String sQuery = "DELETE FROM " + TABELLA_BASI + " WHERE "
						+ "ID=" + dataDBRow.get(BASI_ID).toString() + " AND "
						+ "ID_Ing=" + dataDBRow.get(BASI_Nome).toString();
			
				try
				{
					if (stat.executeUpdate(sQuery) != 0)
					{
						output = output && true;
					}	
				}
				catch (Exception e)
				{	
					output = false;
				}
			}
		}
		
		return output;
	}
	
	private boolean AggiornoBasiRows(Vector<Vector<Object>> dataNew)
	{
		boolean output = true;
		boolean bEseguito = false;
		
		/**
		 * Ciclo su tutte le righe di dataNew
		 */
		for (int i=0; i < dataNew.size(); i++)
		{
			Vector<Object> dataNewRow = new Vector<Object>();
			dataNewRow = dataNew.get(i);
			
			/**
			 * Faccio l'UPDATE della tabella Composizione
			 */
			String sQuery;
			sQuery = "UPDATE " + TABELLA_BASI + " SET " 
					+ "ID=" + dataNewRow.get(BASI_ID).toString() + ", " 
					+ "Nome='" + dataNewRow.get(BASI_Nome).toString() + "'" 
					+ " WHERE "
					+ "ID=" + dataNewRow.get(BASI_ID).toString() + " AND "
					+ "Nome='" + dataNewRow.get(BASI_Nome).toString() + "'";	
			
			/**
			 * Eseguo la query
			 */
			bEseguito = EseguiQuery(sQuery);
			
			/**
			 * Se la query di UPDATE è andata male vuol dire che si tratta di una nuova riga
			 */
			if (bEseguito == false)
			{
				sQuery = "INSERT INTO " + TABELLA_BASI + " VALUES (" 
						+ dataNewRow.get(BASI_ID).toString() + ", '"
						+ dataNewRow.get(BASI_Nome).toString() + "')";	
				
				/**
				 * Eseguo la query
				 */
				bEseguito = EseguiQuery(sQuery);	
			}
			
			output = output && bEseguito;
		}
		
		return output;	
	}
	
	private boolean EseguiQuery(String sQuery)
	{
		boolean output = false;
		
		try
		{
			if (stat.executeUpdate(sQuery) != 0)
			{
				output = true;
			}
		}
		catch (Exception e)
		{	
			output = false;
		}
		
		return output;
	}
}
