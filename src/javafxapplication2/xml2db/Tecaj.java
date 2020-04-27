package javafxapplication2.xml2db;

public class Tecaj {
		private String oznaka;
		private String sifra;
		private float vrednost;
		private String vrednoststring;
		
		public String getOznaka() {
			return oznaka;
		}
		public void setOznaka(String oznaka) {
			this.oznaka = oznaka;
		}
		public String getSifra() {
			return sifra;
		}
		public void setSifra(String sifra) {
			this.sifra = sifra;
		}
		public float getVrednost() {
			return vrednost;
		}
		public void setVrednost(float vrednost) {
			this.vrednost = vrednost;
		}
		public String getVrednoststring() {
			vrednost = String2Float(vrednoststring);
			return vrednoststring;
		}
		public void setVrednoststring(String vrednoststring) {
			this.vrednoststring = vrednoststring;
		}
		public Tecaj(String oznaka, String sifra, float vrednost) {
			super();
			this.oznaka = oznaka;
			this.sifra = sifra;
			this.vrednost = vrednost;
		}
		public Tecaj(String oznaka, String sifra, String vrednost) {
			super();
			this.oznaka = oznaka;
			this.sifra = sifra;
			this.vrednoststring = vrednost;
			this.vrednost = String2Float(vrednost);
		}
		
		public float String2Float(String value) {
			float f = 0.0f;
			
			try 
			{
				f = Float.parseFloat(value);
			} 
			catch (NullPointerException e) {
				System.out.println("Napaka, vrednost je null");
			}
			catch(NumberFormatException e){
				System.out.println("Napaka, niz ni tipa float");
			}
			catch(Exception e) {
				System.out.println("Napaka: " + e.getMessage().toString());
			}
			return f;
		}
                
                public Object[] toObjectArray(String datum) {
                    return new Object[] {
                      datum,
                      this.getOznaka(),
                      this.getSifra(),
                      this.getVrednoststring()
                    };
                  }
		
}
