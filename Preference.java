import java.util.prefs.Preferences;

public class Preference {
	//Constants	
		String newline=System.getProperty("line.separator");
	    public Preferences prefs=Preferences.userRoot().node(this.getClass().getName());
	    
		public Preference(){
			 String ID1 = "SystemFontSize";
			 String ID2 = "TreeSize";
			 String ID3 = "HeightIndex";
			 String ID4 = "WidthIndex";
			 
			 
			 
			 prefs.getInt(ID1, 20);
			 prefs.getInt(ID2, 20);
			 prefs.getInt(ID3, 0);
			 prefs.getInt(ID4, 0);
			 
			 String input;
				input = "S-->NP (Aux) VP"+newline;
				input += "NP-->({Art NP}) (AP)* N (PP)* (CP)"+newline;
				input += "NP-->Pro"+newline;
				input += "AP-->(Adv) A"+newline;
				input += "VP-->V (NP) (NP) (PP)* (CP)"+newline;
				input += "VP-->V AP (PP)*"+newline;
				input += "PP-->P NP"+newline;
				input += "CP-->(Comp) S"+newline;
				input += "NP-->NP (Conj NP)*"+newline;
				input += "VP-->VP (Conj VP)*"+newline;
				input += "PP-->PP (Conj PP)*"+newline;
				input += "S-->S (Conj S)*"+newline;
				input += "CP-->CP (Conj CP)*"+newline;
				input += "AP-->AP (Conj AP)*"+newline;
				input += "V-->V (Conj V)*"+newline;
				input += "V"+newline;
				
				
			 prefs.get("grammar", input);
			 prefs.put("grammar", input);
			 prefs.get("FontStyle", "Serif");
			 prefs.getDouble("WidthIndex", 1);
			 prefs.getDouble("HeightIndex", 1);
		}
		
		public void setInt(String x, int i){
			prefs.putInt(x, i);
		}
		
		public int getInt(String x){
//			prefs.putInt("SystemFontSize", 20);
			int r=prefs.getInt(x, 10);
			return r;
		}
		
		
		public void setString(String string, String i) {
			// TODO Auto-generated method stub
			prefs.put(string, i);
		}
		
		public String getString(String x){
			return prefs.get(x, "Serif");
		}
		
		public String getString2(String x){
			 String input;
				input = "S-->NP (Aux) VP"+newline;
				input += "NP-->({Art NP}) (AP)* N (PP)* (CP)"+newline;
				input += "NP-->Pro"+newline;
				input += "AP-->(Adv) A"+newline;
				input += "VP-->V (NP) (NP) (PP)* (CP)"+newline;
				input += "VP-->V AP (PP)*"+newline;
				input += "PP-->P NP"+newline;
				input += "CP-->(Comp) S"+newline;
				input += "NP-->NP (Conj NP)*"+newline;
				input += "VP-->VP (Conj VP)*"+newline;
				input += "PP-->PP (Conj PP)*"+newline;
				input += "S-->S (Conj S)*"+newline;
				input += "CP-->CP (Conj CP)*"+newline;
				input += "AP-->AP (Conj AP)*"+newline;
				input += "V-->V (Conj V)*"+newline;
				input += "V"+newline;
			return prefs.get(x, input);
		}
		
		public void setDouble(String string, double i) {
			// TODO Auto-generated method stub
			prefs.putDouble(string, i);
		}
		
		public double getDouble(String x){
			return prefs.getDouble(x, 1);
		}

		
		public void main(String args[]){
			Preference pp=new Preference();
			System.out.println(pp.getString("grammar"));
		}

		
}
