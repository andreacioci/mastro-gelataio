package MastroGelataioPackage;

public class Ingredienti {

	private Integer ID;
	private String Name;
	private Double Acqua;
	private Double Zucchero;
	private Double Grassi;
	private Double SLNG;
	private Double AltriSolidi;
	private String Deleted;
	private Long POD;
	private Long PAC;
	public static final Integer NUM_ELEMENTI = 8;
	
	public Integer getID()
	{
		return ID;
	}
	
	public String getName()
	{
		return Name;
	}
	
	public Double getAcqua()
	{
		return Acqua;
	}
	
	public Double getZuccheri()
	{
		return Zucchero;
	}
	
	public Double getGrassi()
	{
		return Grassi;
	}
	
	public Double getSLNG()
	{
		return SLNG;
	}
	
	public Double getAltriSolidi()
	{
		return AltriSolidi;
	}
	
	public String getDeleted()
	{
		return Deleted;
	}
	
	public Long getPOD()
	{
		return POD;
	}
	
	public Long getPAC()
	{
		return PAC;
	}
	
	public void setID(Integer iID)
	{
		ID = iID;
	}
	
	public void setName(String sName)
	{
		Name = sName;
	}
	
	public void setAcqua(Double dAcqua)
	{
		Acqua = dAcqua;
	}
	
	public void setZuccheri(Double dZucchero)
	{
		Zucchero = dZucchero;
	}
	
	public void setGrassi(Double dGrassi)
	{
		Grassi = dGrassi;
	}
	
	public void setSLNG(Double dSLNG)
	{
		SLNG = dSLNG;
	}
	
	public void setAltriSolidi(Double dAltriSolidi)
	{
		AltriSolidi = dAltriSolidi;
	}
	
	public void setDeleted(String sDeleted)
	{
		Deleted = sDeleted;
	}
	
	public void setPOD(Long lPOD)
	{
		POD = lPOD;
	}
	
	public void setPAC(Long lPAC)
	{
		PAC = lPAC;
	}
}
