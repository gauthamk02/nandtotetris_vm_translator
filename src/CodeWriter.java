import java.util.HashMap;

public class CodeWriter {

    static HashMap<SegmentType, String> segmentMap = new HashMap<SegmentType, String>() {{
        put(SegmentType.SEG_CONST, "SP");
        put(SegmentType.SEG_ARG, "ARG");
        put(SegmentType.SEG_LOCAL, "LCL");
        put(SegmentType.SEG_STATIC, "STATIC");
        put(SegmentType.SEG_THIS, "THIS");
        put(SegmentType.SEG_THAT, "THAT");
    }}; 

    static String push(SegmentType seg, int i) {
        /*
         * @{segAddr}
         * D=A
         * @{i}
         * D=D+A
         * A=D
         * D=M
         * @SP
         * A=M
         * M=D
         * @SP
         * M=M+1
         */
        String pushTemplate = "@{segAddr}\nD=A\n@{i}\nD=D+A\nA=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
        return pushTemplate.replace("{segAddr}", segmentMap.get(seg)).replace("{i}", Integer.toString(i));
    }

    static String pop(SegmentType seg, int i) {
        /*
         * @{segAddr}
         * D=A
         * @{i}
         * D=D+A
         * @R13
         * M=D
         * @SP
         * M=M-1
         * A=M
         * D=M
         * @R13
         * A=M
         * M=D
         */
        String popTemplate = "@{segAddr}\nD=A\n@{i}\nD=D+A\n@R13\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@R13\nA=M\nM=D\n";
        return popTemplate.replace("{segAddr}", segmentMap.get(seg)).replace("{i}", Integer.toString(i));
    }

    static String add() {
        /*
         * @SP
         * M=M-1
         * A=M
         * D=M
         * @SP
         * M=M-1
         * A=M
         * M=M+D
         */
        return "@SP\nM=M-1\nA=M\nD=M\n@SP\nM=M-1\nA=M\nM=M+D\n";
    }

    static String sub() {
        /*
         * @SP
         * M=M-1
         * A=M
         * D=M
         * @SP
         * M=M-1
         * A=M
         * M=M-D
         */
        return "@SP\nM=M-1\nA=M\nD=M\n@SP\nM=M-1\nA=M\nM=M-D\n";
    }
}

enum CommandType {
    C_ARITHMETIC,
    C_PUSH,
    C_POP,
    C_LABEL,
    C_GOTO,
    C_IF,
    C_FUNCTION,
    C_RETURN,
    C_CALL
}

enum SegmentType {
    SEG_CONST,
    SEG_ARG,
    SEG_LOCAL,
    SEG_STATIC,
    SEG_THIS,
    SEG_THAT
}
