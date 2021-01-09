import java.util.Scanner;
import java.io.PrintStream;
// import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

//issues with the jimi hendrix -- isProtein
public class DNA{

   public static final int MNC = 5; //the (M)inimun (N)umber of (C)odons a valid protein must have
   public static final double CG = 30.0; //the minimum percentage of mass from (C) and (G) nucleotides
   public static final int UN = 4; //minimum number of (U)nique (N)ucleotides
   public static final int NPC = 3; //the number of (N)ucleotides (P)er (C)odon
   
   public static void main(String[] args)throws FileNotFoundException{
      intro();
      
      Scanner sc = new Scanner(System.in);
      Scanner input = getInputFile(sc);
      PrintStream outputFile = getOutputFile(sc);
      
      while(input.hasNextLine()){
         String name = getName(input);
         String dna = getDNA(input);
         
         int[] count = getCounts(dna);
         double[] percentMass = new double[UN];
         double totalMass = getTotalMasses(dna, count, percentMass);
         
         String[] codons = getCodons(dna);
         
         boolean isProtein = getIsProtein(codons, percentMass);
         
         PrintOutput(outputFile, name, dna, count, totalMass, percentMass, codons, isProtein);
      }
   }
   public static void PrintOutput(PrintStream outputFile, String name, String dna, int[] count, double totalMass, double[] percentMass, String[] codons, boolean isProtein){
      //print the name of the protein
      outputFile.println("Region Name: " + name);
      //print the DNA strand
      outputFile.println("Nucleotides: " + dna);
      //print the count/number of nuc tides in the stand
      outputFile.println("Nuc. Counts: " + Arrays.toString(count));
      //Print the total mass/percents
      outputFile.println("Total Mass%: " + Arrays.toString(percentMass) + " of " + totalMass);
      //print the seperated codons
      outputFile.println("Codons List: " + Arrays.toString(codons));
      //print Yes or No if it is a protein sequence
      if(isProtein){
         outputFile.println("Is Protein?: YES");
      } 
      else {
         outputFile.println("Is Protein?: NO");
      }
      //add a space inbetween different protein checks
      outputFile.println();
   }
     
   public static void intro(){ 
      System.out.println("This program reports information about DNA");
      System.out.println("nucleotide sequences that may encode proteins.");
   }
 
   public static Scanner getInputFile(Scanner sc)throws FileNotFoundException{
      //prompt user for file name
      System.out.print("Input file name? ");
      String name = sc.nextLine();
      
      File f = new File(name);
      while(!f.exists()){
         System.out.print("File not found. Try again: ");
         name = sc.nextLine();
         f = new File(name);
      }
      Scanner inputFile = new Scanner(f);
      return inputFile;
   }
   
   //asks for a file to read from for the mad-lib game
   //and creates file (named by user) to input the information
   public static PrintStream getOutputFile(Scanner sc)throws FileNotFoundException{ 
      //user is creating an output txt file 
      System.out.print("Output file name? ");
      String name = sc.nextLine();
      File f = new File(name);
         
         //create a new file with the name
      PrintStream outputFile = new PrintStream(f);
      return outputFile;
   }
   
   public static String getName(Scanner sc){
      //gets the next line
      String name = sc.nextLine();
      
      return name;
   }
   
   public static String getDNA(Scanner sc){
      //gets the next line
      String DNA = sc.nextLine().toUpperCase();
      
      return DNA;
   }
   
   public static int[] getCounts(String dna){
      int a = 0;
      int c = 0;
      int t = 0;
      int g = 0;
      
      for(int i = 0; i < dna.length(); i++){
         char l = dna.charAt(i);
         
         if(l == 'A') a++;
         if(l == 'C') c++;
         if(l == 'T') t++;
         if(l == 'G') g++;    
      }
      int[] count = {a, c, g, t};
      
      return count;
   }
   
   public static double getTotalMasses(String dna, int[] counts, double[] percentMass){
      int totalCounts = 0;
      for(int i = 0; i < counts.length; i++){
         totalCounts += counts[i];
      }
      int dash = dna.length() - totalCounts;
      
      //calculate total mass for each nuc tide
      double a = counts[0]*135.128;
      double c = counts[1]*111.103;
      double g = counts[2]*151.128;
      double t = counts[3]*125.107;
      double j =      dash*100.000;
      
      //add to find total mass of dna strand
      double sum = a  + c + g + t + j;
      
      //calculate percent of each nuc tide and round to 1 digit
      //                     mass / sum -> percent -> round 1 decimal
      percentMass[0] = Math.round((a / sum * 100.0)   * 10.0) / 10.0;
      percentMass[1] = Math.round((c / sum * 100.0)   * 10.0) / 10.0;
      percentMass[2] = Math.round((g / sum * 100.0)   * 10.0) / 10.0;
      percentMass[3] = Math.round((t / sum * 100.0)   * 10.0) / 10.0;
      
      //return the sum rounded to 1 decimal
      return Math.round(sum* 10.0) / 10.0;
   }
   
   public static String[] getCodons(String dna){
      //remove the -/junk characters in the dna sequence
      dna = dna.replace("-", "");
      
      //num of codon in the dna sequence 
      int numCodon = dna.length()/ NPC;
      //new array to hold the sorted codons
      String[] codon = new String[numCodon];
   
      int j = 0;  //counter for each coddon -- should be equal/less than numcodon
      for(int i = 0; i < dna.length(); i += NPC){
         codon[j] = dna.substring(i, i + NPC);
         j++;
      }   
         
      return codon;
   }
   
   public static boolean getIsProtein(String[] codons, double[] percentMass){
      if(codons[0].equals("ATG")){
         if(codons.length >= MNC){  //the (M)inimun (N)umber of (C)odons a valid protein must have -- default 5
            if(codons[codons.length-1].equals("TAA") || codons[codons.length-1].equals("TAG") || codons[codons.length-1].equals("TGA")) {
               //             c  +             g      >=  CG //the minimum percentage of mass from (C) and (G) nucleotides -- default 30%
               if(percentMass[1] + percentMass[2] >= CG){
                  return true;
               }
            }
         }
      }
      return false;
   }//end isProtein
}