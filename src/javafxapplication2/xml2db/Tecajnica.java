package javafxapplication2.xml2db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Tecajnica {
	public List<Tecaj> listaTecajev;
	public Date datum;
	public List<Tecaj> getListaTecajev() {
		return listaTecajev;
	}
	public void setListaTecajev(List<Tecaj> listaTecajev) {
		this.listaTecajev = listaTecajev;
	}
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	
	public Date str2Date(String strDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse ( strDate );
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
