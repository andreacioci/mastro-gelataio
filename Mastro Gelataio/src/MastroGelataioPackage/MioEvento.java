package MastroGelataioPackage;

import java.util.EventObject;
import java.util.Vector;

public class MioEvento extends EventObject{

	String sName;
	
	public MioEvento(Object source)
	{
		super(source);
	}
	
	public MioEvento(Vector<Vector<Object>> source)
	{
		super(source);
	}
	
	public Object getSourceObject()
	{
		return source;
	}
	
	public Vector<Vector<Object>> getSourceVector()
	{
		if (source.getClass().getName() == "java.util.Vector")
		{
			return (Vector<Vector<Object>>) source;	
		}
		else
		{
			return null;
		}
	}
	
	public void setMioEventoName(String sInput)
	{
		sName = sInput;
	}
	
	public String getMioEventoName()
	{
		return sName;
	}
}
