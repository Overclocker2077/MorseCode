import java.util.*;
import java.io.*;
import java.util.HashMap;

    public class MorseCode
   {
      //here are two arrays, one of English characters and one of their corresponding Morse Code equivalents.
      //english[i] is the English equivalent of code[i] for every index i in the arrays 
      public static final String [] english = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
                                   "�","�","�","Ch","�","�","�","�",
            							  "0","1","2","3","4","5","6","7","8","9",
            							  ".",",",":","?","'","-","_","(",""+(char)(34),"@","="};	//char 34 is the quote character
      public static final String [] code = {".-","-...","-.-.","-..",".","..-.","--.","....","..",".---","-.-",".-..","--","-.","---",".--.","--.-",".-.","...","-","..-","...-",".--","-..-","-.--","--..",
                                ".-.-",".--.-",".--.-","----","..-..","--.--","---.","..--",
            						  "-----",".----","..---","...--","....-",".....","-....","--...","---..","----.",
            						  ".-.-.-","--..--","---...","..--..",".----.","-....-","-..-.","-.--.-",".-..-.",".--.-.","-...-"};
                                
      public HashMap<String, String> encode = new HashMap();
      public HashMap<String, String> decode = new HashMap();
      
      // Constructor 
      public MorseCode() {
         // populate the encode and decode with the parallel arrays
         // O(n)
         for (int i = 0; i < MorseCode.code.length; i++) {
            decode.put(MorseCode.code[i], MorseCode.english[i]);
         }
         // O(n)
         for (int i = 0; i < MorseCode.code.length; i++) {
            encode.put(MorseCode.english[i], MorseCode.code[i]);
         }
      }

      // pre: valid morse code characters 
      // post: returns english translation
      // O(n)
      public String decode(String file_content) {
         // set up variables for the for loop
         String res = "";   // Result
         String current_character = "";  // Current Morse code character
         // loop through morse code sequence
         //  -..- - -.-./ .....
         // O(n) time 
         for (int i = 0; i < file_content.length(); i++) {
            // space indicates morse code character sequence is complete
            if (file_content.charAt(i) == ' ' && !current_character.equals("")) {
               res += decode.get(current_character);
               current_character = ""; // reset for new character sequence
            }
            else {
               if (file_content.charAt(i) == '/') 
                  res += " ";
               else
                  if (file_content.charAt(i) != ' ')
                     current_character += "" + file_content.charAt(i);
            }
         }
         return res;   
      }

      // pre: file_content string with english characters  
      // post: returns morse code translation
      // O(n)
      public String encode(String file_content) {

         // initalize variables for for loop
         String res = "";
         String current_char = "";

         // O(n) time complexity
         for (int i = 0; i < file_content.length(); i++) {
            current_char = "" + file_content.charAt(i);
            // space 
            if (current_char.equals(" "))
               res += "/ ";  // insert a /
            else
               res += encode.get(current_char.toUpperCase()) + " ";
         }

         return res;
      }
   }
      
