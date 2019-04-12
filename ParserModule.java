import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
/**
 *
 * @author Kevin
 */
public class ParserModule {
    private static ArrayList<String> arithCmds;
    private String currCommand = null;
    private Scanner scanner = null;
    private String arg0 = null;
    private String arg1 = null;
    private String arg2 = null;
    private String cmdType = null;

    // opens input file/stream and gets ready to parse it
    public ParserModule(File file) {
        arithCmds = new ArrayList<>();
        arithCmds.add("add");
        arithCmds.add("sub");
        arithCmds.add("neg");
        arithCmds.add("eq");
        arithCmds.add("gt");
        arithCmds.add("lt");
        arithCmds.add("and");
        arithCmds.add("or");
        arithCmds.add("not");
        
        try {
            scanner = new Scanner(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    // are there more commands in the input?
    public boolean hasMoreCommands() {
        boolean hasMore = false;
        if (scanner.hasNextLine()) {
            hasMore = true;
        }
        return hasMore;
    }


    // test if the line argument has comments in it
    private boolean hasComments(String strLine) {
        boolean bHasComments = false;
        if (strLine.contains("//")) {
            bHasComments = true;
        }
        return bHasComments;

    }

    // removes comments from a line
    private String removeComments(String strLine) {
        String strNoComments = strLine;
        if (hasComments(strLine)) {
            int offSet = strLine.indexOf("//");
            strNoComments = strLine.substring(0, offSet).trim();

        }
        return strNoComments;
    }
    // reads next command from input and makes it current command; should be called only if hasMoreCommands() is true
    // initially there is no current command
    public void advance() {
        String strLine = scanner.nextLine();
        while (strLine.equals("") || hasComments(strLine)) {
            if (hasComments(strLine)) {
                strLine = removeComments(strLine);
            }
            if (strLine.trim().equals("")) {
                strLine = scanner.nextLine();
            }
        }

        currCommand = strLine;
        String[] cmds = currCommand.split(" ");
        arg0 = cmds[0];
        if (cmds.length > 1) {
            arg1 = cmds[1];
        }
        if (cmds.length > 2) {
            arg2 = cmds[2];
        }

        if (arg0.equals("push")) {
            cmdType = "C_PUSH";
        } 
        else if (arg0.equals("pop")) {
            cmdType = "C_POP";
        } 
        else if (arithCmds.contains(arg0)) {
            cmdType = "C_ARITHMETIC";
        }
        else if (arg0.equals("label")) {
            cmdType = "C_LABEL";
        }
        else if (arg0.equals("goto")) {
            cmdType = "C_GOTO";
        }
        else if (arg0.equals("if-goto")) {
            cmdType = "C_IF";
        }
        else if (arg0.equals("function")) {
            cmdType = "C_FUNCTION";
        }
        else if (arg0.equals("return")) {
            cmdType = "C_RETURN";
        }
        else if (arg0.equals("call")) {
            cmdType = "C_CALL";
        }
     }


    // returns type for current VM command, C_ARITHMETIC returned for all arithmetic commands
    public String commandType() {
        return cmdType;
    }

    // returns first argument of current command, in case of C_ARITHMETIC the command itself
    // (add, sub, etc) is returned. should not be called if current command is C_RETURN
    public String arg1() {
        String strArg1 = null;
        if (cmdType.equals("C_ARITHMETIC")) {
            strArg1 = arg0;
        } else if (!(cmdType.equals("C_RETURN"))) {
            strArg1 = arg1;
        }
        return strArg1;
    }

    // returns 2nd arg of curr command - should be called only if curr command is C_PUSH, C_POP, C_FUNCTION, or C_CALL
    public int arg2() {
        int intArg2 = 0;
        if (cmdType.equals("C_PUSH") || cmdType.equals("C_POP") || cmdType.equals("C_FUNCTION") || cmdType.equals("C_CALL")) {
            intArg2 = Integer.parseInt(arg2);
        }
        return intArg2;

    }

}