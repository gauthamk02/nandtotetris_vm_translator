import java.io.*;
import java.util.*;

public class VirtualMachine{
    public static void main(String args[]) {
        if(args.length != 1) {
            System.out.println("Argument Error!\nUsage: java VirtualMachine <inputfile>");
            return;
        }

        String inFilename = args[0];
        
        String outFilename = "/home/gautham/Desktop/files/nandtotetris/nand2tetris/projects/07/MemoryAccess/BasicTest" + inFilename.substring(inFilename.lastIndexOf('/'),inFilename.lastIndexOf('.')) + ".asm";//inFilename.substring(0,inFilename.lastIndexOf('.')) + ".asm";
        File infile = new File(inFilename);
        Scanner filein;
        ArrayList<String> vmFile = new ArrayList<>();
        ArrayList<String> hackFile = new ArrayList<>();

        try{
            filein = new Scanner(infile);
            while(filein.hasNextLine()){
                String line = filein.nextLine();
                
                //Remove inline comments and and skip comment lines
                if(line.startsWith("//") || line.isEmpty()) continue;
                if(line.contains("//")) {
                    line = line.substring(0, line.indexOf("//"));
                } 

                vmFile.add(line.replaceAll("[\\s&&[^ ]]", ""));
            }

            Parser parser = new Parser(vmFile);

            while(parser.hasMoreLines()) {
                hackFile.add(parser.convertNextLine());
            }

            printFile(hackFile);

            writetoFile(hackFile, outFilename);
        }
        catch(IOException e) {
            System.out.println(e);
            System.out.println(e.getStackTrace());
            return;
        }

        System.out.println("Assembly code stored at " + outFilename);
    }
    static void writetoFile(ArrayList<String> list, String filename) throws IOException {
        FileWriter fw = new FileWriter(filename);
        for(String str : list) {
            fw.write(str + System.lineSeparator());
        }

        fw.close();
    }
    static void printFile(ArrayList<String> file){
        for(String str : file) System.out.println(str);
        System.out.println();
    }
}