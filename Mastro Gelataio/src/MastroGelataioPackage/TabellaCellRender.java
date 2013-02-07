package MastroGelataioPackage;

import java.awt.Component;
import java.awt.Font;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TabellaCellRender extends DefaultTableCellRenderer 
{
	Vector<String> sColumnName;
	Vector<String> sCondCols;
	Vector<Object> objVals;
	Vector<String> sInsideIntervalCols;
	Vector<Object> objInsideMinVals;
	Vector<Object> objInsideMaxVals;
	Vector<String> sOutsideIntervalCols;
	Vector<Object> objOutsideMinVals;
	Vector<Object> objOutsideMaxVals;
	
	public TabellaCellRender()
	{
		sColumnName = new Vector<String>();
		sCondCols = new Vector<String>();
		objVals = new Vector<Object>();
		
		sInsideIntervalCols = new Vector<String>();
		objInsideMinVals = new Vector<Object>();
		objInsideMaxVals = new Vector<Object>();
		
		sOutsideIntervalCols = new Vector<String>();
		objOutsideMinVals = new Vector<Object>();
		objOutsideMaxVals = new Vector<Object>();
	}
	
	public void setColumnName(Vector<String> sColumns)
	{
		sColumnName = sColumns;
	}
	
	public void setEqualConditions(Vector<String> sColumns, Vector<Object> objValues)
	{
		sCondCols = sColumns;
		objVals = objValues;
	}
	
	public void setInsideIntervalConditions(Vector<String> sColumns, Vector<Object> objMinValues, Vector<Object> objMaxValues)
	{
		sInsideIntervalCols = sColumns;
		objInsideMinVals = objMinValues;
		objInsideMaxVals = objMaxValues;
	}
	
	public void setOutsideIntervalConditions(Vector<String> sColumns, Vector<Object> objMinValues, Vector<Object> objMaxValues)
	{
		sOutsideIntervalCols = sColumns;
		objOutsideMinVals = objMinValues;
		objOutsideMaxVals = objMaxValues;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        /**
         * Imposto il Font
         */
        c.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        
        /**
         * Verifico se c'è un valore impostato
         */
        if (sCondCols.size() > 0)
        {
        	ControllaUguaglianza(c, table, value, row, column);
        }
        
        /**
         * Verifico se c'è un intervallo interno impostato e se la cella in oggetto non contiene una string
         */
        if ((sInsideIntervalCols.size() > 0) && (value.getClass().getName() != "java.lang.String"))
        {
        	ControllaIntervalloInside(c, table, value, row, column);
        }
        
        /**
         * Verifico se c'è un intervallo esterno impostato e se la cella in oggetto non contiene una string
         */
        if ((sOutsideIntervalCols.size() > 0) && (value.getClass().getName() != "java.lang.String"))
        {
        	ControllaIntervalloOutside(c, table, value, row, column);
        }
        
        return c;
    }
	
	private void ControllaUguaglianza(Component c, JTable table, Object value, int row, int column)
	{
		for (int i=0; i < sCondCols.size(); i++)
        {
			if (table.getSelectedRow() != row)
    		{
				if (objVals.get(i).equals(table.getValueAt(row, sColumnName.indexOf(sCondCols.get(i)))))
	        	{
	        		c.setBackground(new java.awt.Color(255, 72, 72));	/* Rosso */
	        	}
				else
				{
					c.setBackground(new java.awt.Color(255, 255, 255));	/* Bianco */
				}
    		}
        }
	}
	
	private void ControllaIntervalloInside(Component c, JTable table, Object value, int row, int column)
	{
       	int i = 0;
       	boolean bFound = false;
       	Integer iIndex = -1;
        	
       	/**
       	 * Cerco se la cella che sto considerando ha un intervallo associato da controllare
       	 */
       	while ((i < sInsideIntervalCols.size()) && (bFound == false))
       	{
       		if (sInsideIntervalCols.get(i).toString() == table.getColumnName(column))
        	{
       			iIndex = i;
        		bFound = true;
        	}
       		i++;
       	}
       	if (bFound == true)
       	{
       		Double dMin;
        	Double dMax;
	        
        	/**
        	 * Se la soglia Min non è impostata la setto a 100 così la verifica non sarà mai soddisfatta
        	 */
        	if(objInsideMinVals.size() == 0)
        	{
        		dMin = 100.0;
        	}
        	else
        	{
        		dMin = Double.parseDouble(objInsideMinVals.get(iIndex).toString());
        	}
	        
        	/**
        	 * Se la soglia Max non è impostata la setto a 0 così la verifica non sarà mai soddisfatta
        	 */
        	if (objInsideMaxVals.size() == 0)
        	{
        		dMax = 0.0;
        	}
        	else
        	{
        		dMax = Double.parseDouble(objInsideMaxVals.get(iIndex).toString());
        	}
        	
        	if ((Double.parseDouble(value.toString()) > dMin) && (Double.parseDouble(value.toString()) < dMax))
        	{
        		c.setBackground(new java.awt.Color(255, 72, 72));	/* Rosso */
        	}
        	else
        	{
        		c.setBackground(new java.awt.Color(255, 255, 255));	/* Bianco */
        	}
       	}
	}
	
	private void ControllaIntervalloOutside(Component c, JTable table, Object value, int row, int column)
	{
       	int i = 0;
       	boolean bFound = false;
       	Integer iIndex = -1;
        	
       	/**
       	 * Cerco se la cella che sto considerando ha un intervallo associato da controllare
       	 */
       	while ((i < sOutsideIntervalCols.size()) && (bFound == false))
       	{
       		if (sOutsideIntervalCols.get(i).toString() == table.getColumnName(column))
        	{
       			iIndex = i;
        		bFound = true;
        	}
       		i++;
       	}
       	if (bFound == true)
       	{
       		Double dMin;
        	Double dMax;
	        
        	/**
        	 * Se la soglia Min non è impostata la setto a 100 così la verifica non sarà mai soddisfatta
        	 */
        	if(objOutsideMinVals.size() == 0)
        	{
        		dMin = 0.0;
        	}
        	else
        	{
        		dMin = Double.parseDouble(objOutsideMinVals.get(iIndex).toString());
        	}
	        
        	/**
        	 * Se la soglia Max non è impostata la setto a 0 così la verifica non sarà mai soddisfatta
        	 */
        	if (objOutsideMaxVals.size() == 0)
        	{
        		dMax = 100.0;
        	}
        	else
        	{
        		dMax = Double.parseDouble(objOutsideMaxVals.get(iIndex).toString());
        	}
        	
        	if ((Double.parseDouble(value.toString()) < dMin) || (Double.parseDouble(value.toString()) > dMax))
        	{
        		c.setBackground(new java.awt.Color(255, 72, 72));	/* Rosso */
        	}
        	else
        	{
        		c.setBackground(new java.awt.Color(255, 255, 255));	/* Bianco */
        	}
       	}
	}
}
