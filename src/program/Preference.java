package program;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.prefs.Preferences;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;


//this is the class storing all the path to resource file
//user's preference setting
public class Preference {
		//block_start information
		//THERE ARE THREE TYPES OF INFORMATION STORED IN PREFERENCE
		//int,string,double
		/*
		 * int:
		 * SystemFontSize -- font size of the system
		 * 
		 * string:
		 * Content -- the practice set in the program, whether in progress or new
		 * FontSyle //?? can be divided to SystemFontStyle and TreeFontStyle
		 * grammar_text
		 * 
		 * double:
		 * HeightIndex -- the index deciding the vertical distance among nodes in the tree
		 * WidthIndex -- the index deciding the horizontal distance among nodes in the tree
		 * 
		 */
		//the resource path is stored under Constants
	
		//block_end information
	
		//attributes
		private Preferences prefs=Preferences.userRoot().node(this.getClass().getName());
		
		//<< Constants		
		public static String newline=System.getProperty("line.separator");	    
		public static Font default_font= UIManager.getDefaults().getFont("TabbedPane.font");
		public static int default_font_size=default_font.getSize();
		public static String default_font_name=default_font.getFontName();
		
		public static String grammar_path="grammar.txt";
		//>> Constants
	
		//<< int_setter_and_getter
		
		public void setInt(String x, int i){
			prefs.putInt(x, i);
		}
		
		public int getInt(String x) {
			return prefs.getInt(x, default_font_size);
		}
		//>> int_setter_and_getter
		
		
		//<< string_setter_and_getter
		public void setString(String string, String i) {
			prefs.put(string, i);
		}
		
		public String getString(String x){
			
			if(x.equals("FontStyle") || x.equals("TreeFontStyle") ){
				return prefs.get(x, default_font_name);
			}
			else if (x.equals("grammar_text")){
				String default_grammar_text="";
				try {
					default_grammar_text = get_string_from_jar(grammar_path);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return prefs.get(x, default_grammar_text);
			}
			
			//??need to improve here for error log
			return prefs.get(x, "error");
		}
		
	
		//>> string_setter_and_getter
		

		//<< double_setter_and_getter
		public void setDouble(String string, double i) {
			prefs.putDouble(string, i);
		}
		
		public double getDouble(String x){
			//?? now only have HeightIndex and WidthIndex so directly return 1 by default
			//need to modify later if more doubles needed to be stored
			return prefs.getDouble(x, 1);
		}
		//>> double_setter_and_getter

		public static String readFile(String path, Charset encoding) 
				  throws IOException 
				{
				  byte[] encoded = Files.readAllBytes(Paths.get(path));
				  return new String(encoded, encoding);
				}
		
		
		//this function can get string from a file inside a Jar file
		//path the the path of that file relative to resource folder
		//if grammar.txt is in resource folder, than the path is "grammar.txt"
		//if grammar.txt is in resource/grammar folder, than the path is "grammar/grammar.txt"
		public static String get_string_from_jar(String path) throws IOException {
			InputStream iStream = Preference.class.getClassLoader().getResourceAsStream(path);
			//build a Stream Reader, it can read char by char
		    InputStreamReader iStreamReader = new InputStreamReader(iStream);
		    //build a buffered Reader, so that i can read whole line at once
		    BufferedReader bReader = new BufferedReader(iStreamReader);
		    String line = null;
		    StringBuilder builder = new StringBuilder();
		    while((line = bReader.readLine()) != null) {  //Read till end
		        builder.append(line);
		        builder.append("\n"); // append new line to preserve lines
		    }
		    bReader.close();         //close all opened stuff
		    iStreamReader.close();
		    iStream.close(); //EDIT: Let the creator of the stream close it!
		    return builder.toString();
		}
		
				
		static public void main(String args[]) throws IOException{
			Preference pp=new Preference();	

			

			String grammar_text_from_jar=Preference.get_string_from_jar(grammar_path);
			
			//the path used as argument is the relative path to the .class file
			//the "/" is optional
			
			
//this will only work for files outside of jar
//			URL url=pp.getClass().getResource("/grammar.txt");
//			System.out.println("this is the url: "+url);
//			String path=url.getPath();
//			System.out.println("this is the path: "+path);			
//			String content = readFile(path, Charset.defaultCharset());
//			System.out.println(url.getFile());
//			System.out.println(content);
			

			
		}

		
}
