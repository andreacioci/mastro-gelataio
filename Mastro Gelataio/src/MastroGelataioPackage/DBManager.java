package MastroGelataioPackage;

import java.sql.*;
import java.util.Vector;

public class DBManager {
	
	protected Connection conn;
	protected Statement stat;
	
	public Vector<Vector<Object>> Select(String sTable, String sCondizione)
	{
		ResultSet rs;
		ResultSetMetaData rsmd;
		Integer iColumnCount;
		String sQuery;
		
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();	/** Dati della tabella */

		try
		{
			if (sCondizione == null)
			{
				sCondizione = "";
			}
			sQuery = "SELECT * FROM " + sTable + " " + sCondizione;
			rs = stat.executeQuery(sQuery);
			rsmd = rs.getMetaData();
			iColumnCount = rsmd.getColumnCount();
			
			/**
			* riempio l'oggetto che contiene i dati
			*/
			while (rs.next()) 
			{
				Vector<Object> row = new Vector<Object>();
				for (int i=0; i < iColumnCount; i++)
				{
					row.add(rs.getObject(i + 1));
				}
				data.add(row);
			}
			rs.close();
			
			return data;
		}
		catch (Exception e)
		{
			return data;
		}
	}
	
	/**
	 * Calcola il numero delle righe presenti nella sTable
	 * @param sTable
	 * @return iRowCount
	 */
	public Integer CalculateRowCount(String sTable)
	{
		ResultSet rs;
		Integer iRowCount;
		
		try
		{
			rs = stat.executeQuery("SELECT * FROM " + sTable);
			
			iRowCount = 0;
			while (rs.next())
			{
				iRowCount++;
			}
			
			rs.close();
			
			return iRowCount;
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	
	public Vector<String> RicavaNomiColonne(String sTable)
	{
		ResultSet rs;
		ResultSetMetaData rsmd;
		Integer iColumnCount;
		Vector<String> output = new Vector<String>();
		
		try
		{
			rs = stat.executeQuery("SELECT * FROM " + sTable);
			rsmd = rs.getMetaData();
			
			/**
			 * ricavo il numero delle colonne che compongono la tabella dati 
			 */
			iColumnCount = rsmd.getColumnCount();
			
			/**
			 * ricavo l'header della tabella
			 */
			for(int i = 0 ; i < iColumnCount; i++){
				output.add(rsmd.getColumnName(i+1).toString());
			}
			
			rs.close();
			
			return output;
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	
	
	/**
	 * Aggiorna tutta la tabella sTable
	 * @param sTable - nome della tabella da aggiornare
	 * @param sTableColumn - colonne che compongono la tabella da aggiornare
	 * @param data - dati da mettere in tabella
	 * @param sCondCols - colonne su cui fare la selezione
	 */
	public boolean UpdateAll(String sTable, Vector<String> sTableColumn, Vector<Vector<Object>> data, Vector<String> sCondCols)
	{
		boolean bEseguito = false;
		
		/**
		 * Se la matrice "data" ha un numero di colonne diverso da sTableColumn esco subito 
		 */
		if (sTableColumn.size() != data.get(0).size())
		{
			return false;
		}
		
		/**
		 * Elimino le righe della tabella che non devono essere più presenti
		 */
		Vector<String> sExtractCols = new Vector<String>();
		DeleteRows(sTable, sTableColumn, data, sCondCols, sExtractCols);
		
		/**
		 * Costruisco una query per ogni riga e la eseguo
		 */
		for (int i=0; i < data.size(); i++)
		{
			Vector<Object> dataRow = new Vector<Object>();
			dataRow = data.get(i);
			
			/**
			 * Faccio l'UPDATE della sTable
			 */
			bEseguito = UpdateRow(i, sTable, sTableColumn, dataRow, sCondCols);	
			
			/**
			 * Se la query di UPDATE è andata male vuol dire che si tratta di una nuova riga
			 */
			if (bEseguito == false)
			{
				bEseguito = InsertRow(i, sTable, sTableColumn, dataRow, sCondCols);
			}
		}
		
		return bEseguito;
	}
	
	/**
	 * Confronta le righe presenti in sTable con quelle in data ed elimina da sTable quelle in eccesso
	 * @param sTable - nome tabella
	 * @param data - dati con cui confrontare la tabella
	 * @param sCondCols - colonne per eseguire il confronto
	 */
	private void DeleteRows(String sTable, Vector<String> sTableColumn, Vector<Vector<Object>> data, Vector<String> sCondCols, Vector<String> sExtractCols)
	{
		if (data == null)
		{
			/* Rimuovere tutte le righe */
			return;
		}
		
		/**
		 * Estraggo dalla sTable tutte le righe identificate da sExtractCols
		 */
		Vector<Object> objTmp = new Vector<Object>();
		for (int i=0; i < sExtractCols.size(); i++)
		{
			objTmp.add(data.get(0).get(sTableColumn.indexOf(sExtractCols.get(i))));
		}
		String sCondizione = CostruisciCondizione(objTmp, sExtractCols);
		Vector<Vector<Object>> dataRead = new Vector<Vector<Object>>();
		dataRead = Select(sTable, sCondizione);
		
		/**
		 * Ciclo su tutte le righe della tabella del DB
		 */
		for (int i=0; i < dataRead.size(); i++)
		{
			/**
			 * Estraggo la riga
			 */
			Vector<Object> dataReadRow = new Vector<Object>();
			dataReadRow = dataRead.get(i);
			
			/**
			 * Ciclo su tutte le righe della matrice "data" 
			 */
			int j = 0;
			boolean bFound = false;
			while ((j < data.size()) && (bFound == false))
			{
				/**
				 * Estraggo la riga dalla matrice "data"
				 */
				Vector<Object> dataRow = new Vector<Object>();
				dataRow = data.get(j);
				
				/**
				 * Confronto le due righe su tutte le colonne espresse in sCondCols
				 */
				int w = 0;
				boolean bEqual = true;
				while ((w < sCondCols.size()) && (bEqual == true))
				{
					int iCol = sTableColumn.indexOf(sCondCols.get(w));
					if (dataReadRow.get(iCol).equals(dataRow.get(iCol)) == false)
					{
						bEqual = false;
					}
					w++;
				}
				
				/**
				 * Se le righe sono diverse vado avanti
				 */
				bFound = bEqual;
				
				j++;
			}
			
			/**
			 * Se la riga è da togliere esegue la Delete
			 */
			if (bFound == false)
			{
				Vector<Object> objValues = new Vector<Object>();
				for (int z=0; z < sCondCols.size(); z++)
				{
					objValues.add(dataReadRow.get(sTableColumn.indexOf(sCondCols.get(z))));	
				}
				Delete(sTable, sCondCols, objValues);
			}
		}
	}
	
	private boolean UpdateRow(int iRow, String sTable, Vector<String> sTableColumn, Vector<Object> dataRow, Vector<String> sCondCols)
	{
		boolean bEseguito = false;
		
		/**
		 * Costruisco la query di UPDATE
		 */
		String sQuery = "";
		
		sQuery = "UPDATE " + sTable + " SET ";
		
		for (int j=0; j < sTableColumn.size(); j++)
		{
			sQuery = sQuery + sTableColumn.get(j) + "=";
			switch (dataRow.get(j).getClass().getName())
			{
			case "java.lang.Integer":
			case "java.lang.Long":
			case "java.lang.Double":
				if (j < sTableColumn.size() - 1)
				{
					sQuery = sQuery + dataRow.get(j).toString() + ", ";	
				}
				else
				{
					sQuery = sQuery + dataRow.get(j).toString();
				}
				break;
			case "java.lang.String":
				if (j < sTableColumn.size() - 1)
				{
					sQuery = sQuery + "'" + dataRow.get(j).toString() + "', ";	
				}
				else
				{
					sQuery = sQuery + "'" + dataRow.get(j).toString() + "'";
				}
				break;
			default:
				break;
			}	
		}
		
		sQuery = sQuery + " WHERE ";
		
		for (int j=0; j < sCondCols.size(); j++)
		{
			sQuery = sQuery + sCondCols.get(j) + "=";
			switch (dataRow.get(j).getClass().getName())
			{
			case "java.lang.Integer":
			case "java.lang.Long":
			case "java.lang.Double":
				if (j < sCondCols.size() - 1)
				{
					sQuery = sQuery + dataRow.get(j).toString() + " AND ";	
				}
				else
				{
					sQuery = sQuery + dataRow.get(j).toString();
				}
				break;
			case "java.lang.String":
				if (j < sCondCols.size() - 1)
				{
					sQuery = sQuery + "'" + dataRow.get(j).toString() + "' AND ";	
				}
				else
				{
					sQuery = sQuery + "'" + dataRow.get(j).toString() + "'";
				}
				break;
			default:
				break;
			}	
		}
		
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
		
		return bEseguito;
	}

	private boolean InsertRow(int iRow, String sTable, Vector<String> sTableColumn, Vector<Object> dataRow, Vector<String> sCondCols)
	{
		boolean bEseguito = false;
		
		/**
		 * Costruisco la query di INSERT
		 */
		String sQuery = "";
		
		sQuery = "INSERT INTO " + sTable + " VALUES (";
		
		for (int j=0; j < sTableColumn.size(); j++)
		{
			switch (dataRow.get(j).getClass().getName())
			{
			case "java.lang.Integer":
			case "java.lang.Long":
			case "java.lang.Double":
				if (j < sTableColumn.size() - 1)
				{
					sQuery = sQuery + dataRow.get(j).toString() + ", ";	
				}
				else
				{
					sQuery = sQuery + dataRow.get(j).toString() + ")";
				}
				break;
			case "java.lang.String":
				if (j < sTableColumn.size() - 1)
				{
					sQuery = sQuery + "'" + dataRow.get(j).toString() + "', ";	
				}
				else
				{
					sQuery = sQuery + "'" + dataRow.get(j).toString() + "')";
				}
				break;
			default:
				break;
			}	
		}
		
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
		
		return bEseguito;
	}
	
	private String CostruisciCondizione(Vector<Object> objValues, Vector<String> sCondCols)
	{
		String output;
		
		output = " WHERE ";
		
		for (int i=0; i < sCondCols.size(); i++)
		{
			output = output + sCondCols.get(i) + "=";
			switch (objValues.get(i).getClass().getName())
			{
			case "java.lang.Integer":
			case "java.lang.Long":
			case "java.lang.Double":
				if (i < sCondCols.size() - 1)
				{
					output = output + objValues.get(i).toString() + " AND ";	
				}
				else
				{
					output = output + objValues.get(i).toString();
				}
				break;
			case "java.lang.String":
				if (i < sCondCols.size() - 1)
				{
					output = output + "'" + objValues.get(i).toString() + "' AND ";	
				}
				else
				{
					output = output + "'" + objValues.get(i).toString() + "'";
				}
				break;
			default:
				break;
			}	
		}
		
		return output;
	}
	
	/**
	 * Ricavo il valore INTERO più alto presente nella sTable alla colonna sTableColumn
	 * @param sTable - nome tabella
	 * @param sTableColumn - nome colonna
	 * @return -1 se la tabella è vuota
	 */
	public Integer getMaxValue(String sTable, String sTableColumn)
	{
		ResultSet rs;
		String sQuery;
		Integer output = -1;
		
		sQuery = "SELECT " + sTableColumn + " FROM " + sTable;
		
		try
		{
			rs = stat.executeQuery(sQuery);	
			
			/**
			* riempio l'oggetto che contiene i dati
			*/
			while (rs.next()) 
			{
				Integer iTmp = Integer.parseInt(rs.getObject(1).toString());
				
				if (iTmp > output)
				{
					output = iTmp;
				}
			}
			rs.close();
		}
		catch (Exception e)
		{
			output = -1;
		}
		
		return output;
	}
	
	public boolean Delete(String sTable, Vector<String> sColumns, Vector<Object> objValues)
	{
		boolean output = false;
		String sQuery;
		
		/**
		 * Se tra i parametri c'è solo il nome della tabella vuol dire che tutto il contenuto
		 * deve essere cancellato
		 */
		if ((sColumns == null) && (objValues == null))
		{
			sQuery = "DELETE FROM " + sTable;
			
			/**
			 * Eseguo la query
			 */
			try
			{
				if (sQuery != "")
				{
					if (stat.executeUpdate(sQuery) != 0)
					{
						output = true;
					}
				}	
			}
			catch (Exception e)
			{	
				output = false;
			}
			
			return output;
		}
		/**
		 * Se sColumns e objValues hanno un diverso numero di elementi esco subito
		 */
		if (sColumns.size() != objValues.size())
		{
			return false;
		}
		
		/**
		 * Aggiorno la tabella sTable
		 */
		sQuery = "DELETE FROM " + sTable + " WHERE ";
		
		for (int i=0; i < sColumns.size(); i++)
		{
			sQuery = sQuery + sColumns.get(i) + "=";
			switch (objValues.get(i).getClass().getName())
			{
			case "java.lang.Integer":
			case "java.lang.Long":
			case "java.lang.Double":
				if (i < sColumns.size() - 1)
				{
					sQuery = sQuery + objValues.get(i).toString() + " AND ";	
				}
				else
				{
					sQuery = sQuery + objValues.get(i).toString();
				}
				break;
			case "java.lang.String":
				if (i < sColumns.size() - 1)
				{
					sQuery = sQuery + "'" + objValues.get(i).toString() + "' AND ";	
				}
				else
				{
					sQuery = sQuery + "'" + objValues.get(i).toString() + "'";
				}
				break;
			default:
				break;
			}	
		}
		
		/**
		 * Eseguo la query
		 */
		try
		{
			if (sQuery != "")
			{
				if (stat.executeUpdate(sQuery) != 0)
				{
					output = true;
				}
			}	
		}
		catch (Exception e)
		{	
			output = false;
		}
		
		return output;
	}
	
	public boolean IsPresent(String sTable, Vector<String> sColumns, Vector<Object> objValues)
	{
		boolean output = false;
		
		/**
		 * Se sColumns e objValues hanno un diverso numero di elementi esco subito
		 */
		if (sColumns.size() != objValues.size())
		{
			return false;
		}
		
		/**
		 * Controllo la tabella sTable
		 */
		String sQuery;
		
		sQuery = "SELECT * FROM " + sTable + " WHERE ";
		
		for (int i=0; i < sColumns.size(); i++)
		{
			sQuery = sQuery + sColumns.get(i) + "=";
			switch (objValues.get(i).getClass().getName())
			{
			case "java.lang.Integer":
			case "java.lang.Long":
			case "java.lang.Double":
				if (i < sColumns.size() - 1)
				{
					sQuery = sQuery + objValues.get(i).toString() + " AND ";	
				}
				else
				{
					sQuery = sQuery + objValues.get(i).toString();
				}
				break;
			case "java.lang.String":
				if (i < sColumns.size() - 1)
				{
					sQuery = sQuery + "'" + objValues.get(i).toString() + "' AND ";	
				}
				else
				{
					sQuery = sQuery + "'" + objValues.get(i).toString() + "'";
				}
				break;
			default:
				break;
			}	
		}
		
		/**
		 * Eseguo la query
		 */
		try
		{
			if (sQuery != "")
			{
				ResultSet rs;
				rs = stat.executeQuery(sQuery);
				if (rs.next())
				{
					output = true;
				}
				else
				{
					output = false;
				}
				rs.close();
			}	
		}
		catch (Exception e)
		{	
			output = false;
		}
		
		return output;
	}
}
