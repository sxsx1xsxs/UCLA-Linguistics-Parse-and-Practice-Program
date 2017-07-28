import java.util.prefs.Preferences;

public class PreferenceTest {
  private Preferences prefs;

  public void setPreference() {
    // This will define a node in which the preferences can be stored
    prefs = Preferences.userRoot().node(this.getClass().getName());
    String ID1 = "Test11";
    String ID2 = "Test22";
    String ID3 = "Test33";

    // First we will get the values
    // Define a boolean value
    System.out.println(prefs.getBoolean(ID1, true));
    // Define a string with default "Hello World
    System.out.println(prefs.get(ID2, "Hello 2"));
    // Define a integer with default 50
    System.out.println(prefs.getInt(ID3, 50));

    // now set the values
    prefs.putBoolean(ID1, false);
    prefs.put(ID2, "Hello Europa");
    prefs.putInt(ID3, 45);

    // Delete the preference settings for the first value
    //prefs.remove(ID1);
    
    prefs.remove(ID2);
    //prefs.get(ID2,"HELLO PLEASE");
    System.out.println(prefs.get(ID2, "Hello please"));

  }

  public static void main(String[] args) {
    PreferenceTest test = new PreferenceTest();
    test.setPreference();
  }
}
