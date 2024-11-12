import java.io.*;
import java.util.Scanner;

// audio imports
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 

import java.lang.Thread;

    public class MorseCodeDriver
   {
      public static void main(String[]arg)throws IOException
      {
         String user_input;
         Scanner input = new Scanner(System.in);

         // This is used to decode and encode English and Morsecode 
         MorseCode MorseCoder = new MorseCode();

         while (true) {

            // Show options 
            System.out.println("Enter d to decode");
            System.out.println("Enter e to encode");
            System.out.println("Enter p to play audio from a MorseCode file");
            System.out.println("Enter q to quit");
            
            // get option
            user_input = input.nextLine().toLowerCase();
            
            // process user input and decode or encode, or play morse from a file
            if (user_input.equals("d")) {

               // Ask for file path to Morse code text
               System.out.println("Enter the file path to the Morse Code: ");
               String file_content = get_file_content(input);
               
               // O(n) time
               String res = MorseCoder.decode(file_content);  // MorseCode to English 
            
               System.out.println("Would you like to output to a file? (Y/N)");
               boolean output_option = yes_or_no(input); // true = yes, false = no

               // yes
               if (output_option)
                  writeToNewFile(res, input);
               // no
               else // display results in terminal
                  System.out.println(res); 
            }
            else {
               if (user_input.equals("e")) {
                  // Ask for file path to English text and extract file content
                  System.out.println("Enter the file path to the English text: ");
                  String file_content = get_file_content(input);
                  
                  System.out.println("Would you like to output to a file? (Y/N)");
                  boolean output_option = yes_or_no(input); // true = yes, false = no

                  // O(n) 
                  String res = MorseCoder.encode(file_content); // English to MorseCode

                  // yes 
                  if (output_option)
                     writeToNewFile(res, input);
   
                  // no
                  else // display results in terminal
                     System.out.println(res); 

                  System.out.println("Do you want to play the Morse Code? (Y/N)");
                  boolean output_option2 = yes_or_no(input);  // true = yes, false = no
                  
                  // PLay audio else nothing happens
                  if (output_option2) {
                     try {
                        play_morse_code(res);
                     } catch (Exception e) {
                        System.out.println(e);
                     }   
                  }

               }
               else 
                  if (user_input.equals("q")) 
                     break; // exit loop
                  else
                     // Play MorseCode from text file
                     if (user_input.equals("p")) {
                        // Ask for file path to Morse code text
                        System.out.println("Enter the file path to the Morse Code: ");
                        String file_content = get_file_content(input);
                        // Play audio
                        try {
                           play_morse_code(file_content);
                        } catch (Exception e) {
                           e.printStackTrace();
                        }   

                     }
            }
         }      
      }
      
      // read file to String
      // pre:  file_path contains valid file_path
      // post: returns the files content as a string
      // O(n)
      public static String file_reader(String file_path) throws IOException {
         String file_content = "";
         Scanner input = new Scanner(new FileReader(file_path));
         // O(n)
         while (input.hasNextLine()) {
            file_content += input.nextLine() + " ";
         }
         input.close();
   
         return file_content;
      }

      // pre: requires input scanner object
      // post: returns all file content
      // This function handles mis typed file names and uses file_reader to get the file content
      // O(n)
      public static String get_file_content(Scanner input) {
         String file_content;
         while (true) {
            try {
               String file_path = input.nextLine();
               file_content = file_reader(file_path); // O(n)
               break;
            } catch (IOException e) {
               System.out.println("Enter a valid file path: ");
            }
         }
         return file_content;
      }  

      // pre: requires input scanner object
      // post: returns boolean True = Yes False = No
      // O(1) time
      public static boolean yes_or_no(Scanner input) {
         String user_input;
         while (true) {
            user_input = input.nextLine().toLowerCase();
            if (user_input.equals("y") || user_input.equals("yes")) 
               return true;
            else
               if (user_input.equals("n") || user_input.equals("no")) 
                  return false; 
         }
      }

      // pre:  string data is the data to be written and input scanner to ask the user for a file name
      // post: void, If an I/O error occurres then it throws an IOException
      // O(1) time not including the write file method
      public static void writeToNewFile(String data, Scanner input) throws IOException {
         String file_name;
         // Create new file 
         while (true) {
            System.out.println("Choose a name for this file (include .txt): ");
            file_name = input.nextLine();
            File file_obj = new File(file_name);
            if (file_obj.createNewFile()) 
               break;  // File has been successfully created
            else 
               System.out.println("That file name is alright used");
         }
         // Write to the new file
         FileWriter fileWriter = new FileWriter(file_name);
         fileWriter.write(data);
         fileWriter.close();
      }

      // Pre: string of morse code
      // Post: returns void 
      // O(n) time 
      public static void play_morse_code(String code) throws UnsupportedAudioFileException, 
                                                            IOException, LineUnavailableException, InterruptedException {

         // Create some variables containing the relative file paths to the audio
         String short_beep = "short_beep.wav", long_beep = "long_beep.wav";

         // load Audio input stream for short and long beep
         AudioInputStream  audioInputStream_short_beep = AudioSystem.getAudioInputStream(new File(short_beep).getAbsoluteFile()); 
         AudioInputStream  audioInputStream_long_beep = AudioSystem.getAudioInputStream(new File(long_beep).getAbsoluteFile()); 

         // Create clips to play back the audio
         Clip clip_short_beep = AudioSystem.getClip();
         Clip clip_long_beep = AudioSystem.getClip(); 
         
         // Open clip
         clip_short_beep.open(audioInputStream_short_beep); 
         clip_long_beep.open(audioInputStream_long_beep); 

         for (int i = 0; i < code.length(); i++) {

            // Not sure why, but sometimes at i = 0  it plays the same audio twice 
            // It's probably a problem with the AudioSystem Class or the Clip class
            
            System.out.print(code.charAt(i)); // show user the current character

            if (code.charAt(i) == '.') {
               clip_short_beep.loop(1); // This playes asynchronously
               await_clip(clip_short_beep);  // wait for clip to finish
            }
            else {
               if (code.charAt(i) == '-') {
                  clip_long_beep.loop(1); // This playes asynchronously
                  await_clip(clip_long_beep);   // wait for clip to finish
               }
               else if (code.charAt(i) == ' ')
                  // 200 mil a second delay
                  Thread.sleep(200);

               else if (code.charAt(i) == '/')
                  //  500 mil a second delay
                  Thread.sleep(500);
            }
         }
         clip_short_beep.close();
         clip_long_beep.close();
         System.out.println();
      }

      // pre: pass in a clip object to wait for it
      // post: void, this function waits for the clip to finish playing
      // O(n)
      public static void await_clip(Clip clip) throws InterruptedException {
         // getMicrosecondPosition() Obtains the current position in the audio data, in microseconds
         // This runs until current position equal to the length of the audio
         while(clip.getMicrosecondLength() > clip.getMicrosecondPosition()) {
            // Do nothing just wait
         }
         // 200 mil a second delay, any delay less then 200 will cause it to skip some characters 
         // Longer delay fixes the skipping issue. 
         Thread.sleep(350);
         
         clip.flush();  // I think flushing the mixers databuffer  fixed the skipping. oh nvm
      }
 
      // pre: pass in the clip object
      // post: void, this function waits until the buffer is open
      // public static void wait_until_buffer_is_open(Clip clip) {
      //    // waits util the bufffer is open
      //    while (clip.isOpen()) {   // true if the line is open, otherwise false
      //       // Do nothing just wait
      //    }
      // }
}
      
