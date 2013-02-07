package MastroGelataioPackage;

import javax.swing.table.DefaultTableModel;

public class TabellaModello extends DefaultTableModel {

	String[] sEditableColumns;
	
	public void setEditableColumns(String[] iColumns)
	{
		sEditableColumns = new String[iColumns.length];
		sEditableColumns = iColumns;
	}
	
	@Override  
    public Class getColumnClass(int col) 
	{  
		switch (getColumnName(col))
		{
		case "Acqua":
		case "Solidi":
		case "Zuccheri":
		case "Grassi":
		case "SLNG":
		case "AltriSolidi":
		case "Min":
		case "Max":
			return Double.class;
		case "Name":
		case "Nome":
		case "Composizione":
			return String.class;
		case "ID":
		case "ID_Ing":
			return Long.class;
		default:
			break;
		}
		return Object.class;
	}  
	
	@Override
	public boolean isCellEditable(int iRow, int iColumn)
	{
		/**
		 * Controllo se sono state impostate le colonne editabili. Altrimenti sono tutte read-only
		 */
		if (sEditableColumns == null)
		{
			return false;
		}
		
		int i;
		boolean bFound;
		
		/**
		 * Controllo se la cella in oggetto appartiene ad una delle colonne editabili
		 */
		i = 0;
		bFound = false;
		while ((i < sEditableColumns.length) && (bFound == false))
		{
			if (iColumn == findColumn(sEditableColumns[i]))
			{
				bFound = true;		
			}
			i++;
		}
		
		if (bFound == true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
