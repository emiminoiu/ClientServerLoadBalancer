package Model;

import org.json.simple.JSONObject;

public class Elev {
	private int id;
	private String nume;
	private String prenume;
	private int varsta;
	
	public Elev() {
		
	}
	
	public Elev(JSONObject json) {
		id = ((Long)json.get("id")).intValue();
		nume = (String)json.get("nume");
		prenume = (String)json.get("prenume");
		varsta = ((Long)json.get("varsta")).intValue();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNume() {
		return nume;
	}
	public void setNume(String nume) {
		this.nume = nume;
	}
	public String getPrenume() {
		return prenume;
	}
	public void setPrenume(String prenume) {
		this.prenume = prenume;
	}
	public int getVarsta() {
		return varsta;
	}
	public void setVarsta(int varsta) {
		this.varsta = varsta;
	}
	public JSONObject toJson()
	{
		JSONObject jsonElev = new JSONObject();      
		jsonElev.put("id",this.getId());
		jsonElev.put("nume", this.getNume());
		jsonElev.put("prenume", this.getPrenume());
		jsonElev.put("varsta", this.getVarsta());
		return jsonElev;
	}
	public String toXML() {
		return "<elev id =\""+id+"\"><nume>"+nume+"</nume><prenume>"+prenume+"</prenume><varsta>"+varsta+"</varsta></elev>";
	}
}
