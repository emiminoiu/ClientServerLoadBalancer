package Model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;;

public class TabelModelElevi extends DefaultTableModel {
	private List<Elev> elevi;
	
	public TabelModelElevi(List<Elev> elevi) {
		super();
		if(elevi == null)
		{
			this.elevi = new ArrayList<Elev>();
		}
		else
		{
			this.elevi = elevi;
		}
	}
	
	public void setElevi(List<Elev>elevi) {
		this.elevi = elevi;
		fireTableDataChanged();
	}
	
	public void addElev(Elev elev) {
		this.elevi.add(elev);
		fireTableDataChanged();
	}
	
	public Elev getElev(int index) {
		if (index >= 0 && index < elevi.size()) {
			return elevi.get(index);
		}
		return null;
	}
	
	@Override
	public int getColumnCount() {
		return 4;
	}
	
	@Override
	public int getRowCount() {
		if(elevi == null) return 0;
		return elevi.size();
	}
	
	@Override
	public Object getValueAt(int row, int column) {
		Elev elev = getElev(row);
		if (elev == null) return null;
		if(column == 0)
			return elev.getId();
		if (column == 1)
			return elev.getNume();
		if (column == 2)
			return elev.getPrenume();
		if (column == 3)
			return elev.getVarsta();
		return null;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0 || columnIndex == 3)
			return Integer.class;
		return String.class;
	}
	
	@Override
	public String getColumnName(int column) {
		if(column == 0)
			return "Id";
		if (column == 1)
			return "Nume";
		if (column == 2)
			return "Prenume";
		if (column == 3)
			return "Varsta";
		return "";
	}
	
	public void insert(Elev elev) {
		if(elev != null) {
			elevi.add(elev);
			fireTableDataChanged();
		}
	}
	public void update(Elev elev) {
		fireTableDataChanged();
	}
	
	public void delete(Elev elev) {
		elevi.remove(elev);
		fireTableDataChanged();
	}
}
