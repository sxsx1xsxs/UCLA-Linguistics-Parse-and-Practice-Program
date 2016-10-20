
import java.io.IOException;

import javax.swing.JLabel;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

public class JacksonTester {
   public static void main(String args[]){
   
      ObjectMapper mapper = new ObjectMapper();
      Something x=new Something();
      x.hello="bye";
      x.pp="popop";
      
    //  IndexNode x=new IndexNode("public","d","f");
//      x.setAge(10);
//      x.setName("hello");
      
//      JLabel x=new JLabel ("test");
      String jsonString ;
//      = "{\"name\":\"Mahesh\", \"age\":21}";
      
      //map json to student
		
      try{
//         Student student = mapper.readValue(jsonString, Student.class);
//         
//         System.out.println(student);
         
         mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
       //  mapper.disable(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
       //  mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
         jsonString = mapper.writeValueAsString(x);
        Something pp=mapper.readValue(jsonString, Something.class);
         
         System.out.println(jsonString);
         System.out.println(pp);
      }
      catch (JsonParseException e) { e.printStackTrace();}
      catch (JsonMappingException e) { e.printStackTrace(); }
      catch (IOException e) { e.printStackTrace(); }
   }
}

class Student {
   private String name;
   private int age;
   private int haha=9;
	
   public Student(){}
	
   public String getName() {
      return name;
   }
	
   public int gethaha(){
	   return haha;
   }
   
   public void sethaha(int x){
	   this.haha=x;
   }
   public void setName(String name) {
      this.name = name;
   }
	
   public int getAge() {
      return age;
   }
	
   public void setAge(int age) {
      this.age = age;
   }
   public String toString(){
      return "Student [ name: "+name+", age: "+ age+ ", haha: "+ haha+ " ]";
   }
}