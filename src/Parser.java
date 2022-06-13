import java.util.ArrayList;

public class Parser {
    ArrayList<String> codelist;
    int currLine;
    
    public Parser(ArrayList<String> codelist) {
        this.codelist = codelist;
        this.currLine = 0;
    }

    Boolean hasMoreLines() {
        if(currLine < codelist.size()) return true;
        return false;
    }

    String convertNextLine() {
        
        String command = codelist.get(currLine++);
        //split string into array of words removing whitespace except space
        String[] parts = command.split(" ");
        //String[] parts = command.split("[^\\S ]+");
        String asm;

        CommandType cmd = getCommandType(parts[0]);
        SegmentType seg = null;
        int i = 0;

        if(cmd == CommandType.C_PUSH || cmd == CommandType.C_POP) {
        seg = getSegmentType(parts[1]);
        i = Integer.parseInt(parts[2]);
        }

        switch(cmd) {
            case C_PUSH:
                asm = CodeWriter.push(seg, i);
                break;
            case C_POP:
                asm = CodeWriter.pop(seg, i);
                break;
            case C_ARITHMETIC:
                asm = getArithemeticCommand(command);
                break;
            default:
                asm = "";
                break;
        }
        if(asm == null || asm.equals("")) {
            System.out.println("Error: " + command);
        }
        return asm;
    }

    private String getArithemeticCommand(String command) {
        switch(command) {
            case "add":
                return CodeWriter.add();
            case "sub":
                return CodeWriter.sub();
            case "neg":
                return CodeWriter.neg();
            case "eq":
                return CodeWriter.eq();
            case "gt":
                return CodeWriter.gt();
            case "lt":
                return CodeWriter.lt();
            case "and":
                return CodeWriter.and();
            case "or":
                return CodeWriter.or();
            case "not":
                return CodeWriter.not();
            default:
                return "";
        }
    }

    private CommandType getCommandType(String command) {
        switch(command) {
            case "add":
            case "sub":
            case "neg":
            case "eq":
            case "gt":
            case "lt":
            case "and":
            case "or":
            case "not":
                return CommandType.C_ARITHMETIC;
            case "push":
                return CommandType.C_PUSH;
            case "pop":
                return CommandType.C_POP;
            case "label":
                return CommandType.C_LABEL;
            case "goto":
                return CommandType.C_GOTO;
            case "if-goto":
                return CommandType.C_IF;
            case "function":
                return CommandType.C_FUNCTION;
            case "call":
                return CommandType.C_CALL;
            case "return":
                return CommandType.C_RETURN;
            default:
                return null;
        }
    }

    private SegmentType getSegmentType(String segment) {
        switch(segment) {
            case "constant":
                return SegmentType.SEG_CONST;
            case "argument":
                return SegmentType.SEG_ARG;
            case "local":
                return SegmentType.SEG_LOCAL;
            case "static":
                return SegmentType.SEG_STATIC;
            case "this":
                return SegmentType.SEG_THIS;
            case "that":
                return SegmentType.SEG_THAT;
            case "temp":
                return SegmentType.SEG_TEMP;
            case "pointer":
                return SegmentType.SEG_POINTER;
            default:
                return null;
        }
    }
}
