import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
/**
 * Translation from virtual machine file to assembly file
 * @author Kevin
 */
public class vm {

    public static void main(String[] args) throws IOException {
        File fileIn = new File(args[0]);
        File fileOut;
        ArrayList<File> fileContents = new ArrayList<>();
        if (args.length != 1){
            throw new IllegalArgumentException("Use the format java vm + filename");
        }
        else if (fileIn.isFile() && !(args[0].endsWith(".vm"))){
            throw new IllegalArgumentException("Not the correct file type. Please enter a .vm file or a directory containing .vm files. ");
        }
        else {
            if (fileIn.isFile() && args[0].endsWith(".vm")) {
                fileContents.add(fileIn);
                String firstPart = args[0].substring(0, args[0].length() - 3);
                fileOut = new File(firstPart + ".asm");
            } 
            else 
            {
                fileContents = getFiles(fileIn);
                fileOut = new File(fileIn + ".asm");

            }
        }
        // construct CodeWriter to generate code into corresponding output file
        CodeWriter codeWriter = new CodeWriter(fileOut);
        codeWriter.writeInit();
        for (File file : fileContents) {
            codeWriter.setFileName(file);
            // construct parser to parse VM input files
            ParserModule parser = new ParserModule(file);
            // read through VM commands in input file, generate assembly code
            while (parser.hasMoreCommands()) {
                parser.advance();
                if (parser.commandType().equals("C_ARITHMETIC")) {
                    codeWriter.writeArithmetic(parser.arg1());
                } 
                else if (parser.commandType().equals("C_PUSH") || parser.commandType().equals("C_POP")) {
                    codeWriter.writePushPop(parser.commandType(), parser.arg1(), parser.arg2());
                } 
                else if (parser.commandType().equals("C_LABEL")) {
                    codeWriter.writeLabel(parser.arg1());
                } 
                else if (parser.commandType().equals("C_GOTO")) {
                    codeWriter.writeGoto(parser.arg1());
                } 
                else if (parser.commandType().equals("C_IF")) {
                    codeWriter.writeIf(parser.arg1());


                } 
                else if (parser.commandType().equals("C_FUNCTION")) {
                    codeWriter.writeFunction(parser.arg1(), parser.arg2());
                } 
                else if (parser.commandType().equals("C_RETURN")) {
                    codeWriter.writeReturn();
                } 
                else if (parser.commandType().equals("C_CALL")) {
                    codeWriter.writeCall(parser.arg1(), parser.arg2());
                }

            }

        }

        codeWriter.close();

    }


    // gather all files in the directory argument into an arraylist
    public static ArrayList<File> getFiles(File directory) {
        File[] fileContents = directory.listFiles();
        ArrayList<File> fResults = new ArrayList<>();
        if (fileContents != null) {
            for (File file : fileContents) {
                if (file.getName().endsWith(".vm")) fResults.add(file);
            }
        }
        return fResults;

    }
}