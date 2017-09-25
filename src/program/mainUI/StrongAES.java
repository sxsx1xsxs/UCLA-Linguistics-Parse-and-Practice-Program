package program.mainUI;



import java.security.Key;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class StrongAES 
{
	static String newline = System.getProperty("line.separator");
	
	public String encrypt(String text){
		String r=null;
		try 
        {
            
            String key = "Bop12345Bar12345"; // 128 bit key
            // Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b: encrypted) {
                sb.append((char)b);
            }
            // the encrypted String
            String enc = sb.toString();
            r=enc;

         

        }
        catch(Exception e) 
        {
            e.printStackTrace();
        }
		return r;
	}
	
	
	public String encrypt(String text, String password){
		text="password:"+password+newline+text;
		return encrypt(text);
	}
	
	public boolean containPassword(String text){

		
		Pattern p = Pattern.compile("^password:");
		Matcher m = p.matcher(text);
		
		if(m.find()){
			return true;
		}
		else{
			return false;
		}
	
	}


	
	
	
	public String decrypt(String enc){
		String r=null;
		try 
        {
            
            String key = "Bop12345Bar12345"; // 128 bit key
            // Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            

            // now convert the string to byte array
            // for decryption
            byte[] bb = new byte[enc.length()];
            for (int i=0; i<enc.length(); i++) {
                bb[i] = (byte) enc.charAt(i);
            }

            // decrypt the text
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String decrypted = new String(cipher.doFinal(bb));
            r=decrypted;

        }
        catch(Exception e) 
        {
            e.printStackTrace();
        }
		return r;
	}
    
	
	
	public String getpassword(String text){
		String[] x=text.split(newline);
		String xx=
		x[0].substring(x[0].indexOf(":")+1,x[0].length());
		return xx;
	}
	
	public String getTextWithoutPassword(String text){
		String[] x=text.split(newline);
		String r="";
		for(int i=1;i<x.length;i++){
			r+=x[i]+newline;
		}
		return r;
	}
	
	
	
	
    public static void main(String[] args) 
    {
        StrongAES app = new StrongAES();
    }
}