import java.util.HashMap;

public class CodeWriter {

    static HashMap<SegmentType, String> segmentMap = new HashMap<SegmentType, String>() {
        {
            put(SegmentType.SEG_CONST, "SP");
            put(SegmentType.SEG_ARG, "ARG");
            put(SegmentType.SEG_LOCAL, "LCL");
            put(SegmentType.SEG_THIS, "THIS");
            put(SegmentType.SEG_THAT, "THAT");
        }
    };

    static String push(SegmentType seg, int i) {
        if (seg == SegmentType.SEG_CONST) {
            /*
             * @i
             * D=A
             * @SP
             * A=M
             * M=D
             * @SP
             * M=M+1
             */
            String pushTemplate = "@{i}\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1";
            return pushTemplate.replace("{i}", Integer.toString(i));
        } else if (seg == SegmentType.SEG_TEMP) {
            /*
             * @i
             * D=A
             * @5
             * A=D+A
             * D=M
             * @SP
             * A=M
             * M=D
             * @SP
             * M=M+1
             */
            String pushTemplate = "@{i}\nD=A\n@5\nA=D+A\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
            return pushTemplate.replace("{i}", Integer.toString(i));
        } else if (seg == SegmentType.SEG_STATIC) {
            /*
             * @i
             * D=A
             * @16
             * A=D+A
             * D=M
             * @SP
             * A=M
             * M=D
             * @SP
             * M=M+1
             */
            String pushTemplate = "@{i}\nD=A\n@16\nA=D+A\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
            return pushTemplate.replace("{i}", Integer.toString(i));
        } else if (seg == SegmentType.SEG_POINTER) {

            if (i == 0) {
                /*
                 * @THIS
                 * D=M
                 * @SP
                 * A=M
                 * M=D
                 * @SP
                 * M=M+1
                 */
                String pushTemplate = "@THIS\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
                return pushTemplate;
            } else {
                /*
                 * @THAT
                 * D=M
                 * @SP
                 * A=M
                 * M=D
                 * @SP
                 * M=M+1
                 */
                String pushTemplate = "@THAT\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
                return pushTemplate;
            }
        }

        /*
         * @{i}
         * D=A
         * @{segName}
         * A=D+M
         * D=M
         * @SP
         * A=M
         * M=D
         * @SP
         * M=M+1
         */
        String pushTemplate = "@{i}\nD=A\n@{segName}\nA=D+M\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
        return pushTemplate.replace("{segName}", segmentMap.get(seg)).replace("{i}", Integer.toString(i));
    }

    static String pop(SegmentType seg, int i) {

        if (seg == SegmentType.SEG_TEMP) {
            /*
             * @i
             * D=A
             * @5
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
            String popTemplate = "@{i}\nD=A\n@5\nD=D+A\n@R13\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@R13\nA=M\nM=D";
            return popTemplate.replace("{i}", Integer.toString(i));
        } else if (seg == SegmentType.SEG_STATIC) {
            /*
             * @i
             * D=A
             * @16
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
            String popTemplate = "@{i}\nD=A\n@16\nD=D+A\n@R13\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@R13\nA=M\nM=D";
            return popTemplate.replace("{i}", Integer.toString(i));
        } else if (seg == SegmentType.SEG_POINTER) {
            if (i == 0) {
                /*
                 * @SP
                 * M=M-1
                 * A=M
                 * D=M
                 * @THIS
                 * M=D
                 */
                String popTemplate = "@SP\nM=M-1\nA=M\nD=M\n@THIS\nM=D";
                return popTemplate;
            } else {
                /*
                 * @SP
                 * M=M-1
                 * A=M
                 * D=M
                 * @THAT
                 * M=D
                 */
                String popTemplate = "@SP\nM=M-1\nA=M\nD=M\n@THAT\nM=D";
                return popTemplate;
            }
        }

        /*
         * @{i}
         * D=A
         * @{segName}
         * D=D+M
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
        String popTemplate = "@{i}\nD=A\n@{segName}\nD=D+M\n@R13\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@R13\nA=M\nM=D";
        return popTemplate.replace("{segName}", segmentMap.get(seg)).replace("{i}", Integer.toString(i));
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
         * @SP
         * M=M+1
         */
        return "@SP\nM=M-1\nA=M\nD=M\n@SP\nM=M-1\nA=M\nM=M+D\n@SP\nM=M+1";
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
         * @SP
         * M=M+1
         */
        return "@SP\nM=M-1\nA=M\nD=M\n@SP\nM=M-1\nA=M\nM=M-D\n@SP\nM=M+1";
    }

    static String neg() {
        /*
         * @SP
         * M=M-1
         * A=M
         * M=-M
         */
        return "@SP\nM=M-1\nA=M\nM=-M";
    }

    static String eq() {
        /*
         * @SP
         * M=M-1
         * A=M
         * D=M
         * @SP
         * M=M-1
         * A=M
         * D=M-D
         * @SP
         * A=M
         * M=D
         * @SP
         * M=M+1
         */
        return "@SP\nM=M-1\nA=M\nD=M\n@SP\nM=M-1\nA=M\nD=M-D\n@SP\nA=M\nM=D\n@SP\nM=M+1";
    }

    static String lt() {
        /*
         * @SP
         * M=M-1
         * A=M
         * D=M
         * @SP
         * M=M-1
         * A=M
         * D=M-D
         * @SP
         * A=M
         * M=-1
         * @SP
         * M=M+1
         * @SP
         * M=M-1
         */
        return "@SP\nM=M-1\nA=M\nD=M\n@SP\nM=M-1\nA=M\nD=M-D\n@SP\nA=M\nM=-1\n@SP\nM=M+1\n@SP\nM=M-1";
    }

    static String gt() {
        /*
         * @SP
         * M=M-1
         * A=M
         * D=M
         * @SP
         * M=M-1
         * A=M
         * D=M-D
         * @SP
         * A=M
         * M=0
         * @SP
         * M=M+1
         * @SP
         * M=M-1
         */
        return "@SP\nM=M-1\nA=M\nD=M\n@SP\nM=M-1\nA=M\nD=M-D\n@SP\nA=M\nM=0\n@SP\nM=M+1\n@SP\nM=M-1";
    }

    static String and() {
        /*
         * @SP
         * M=M-1
         * A=M
         * D=M
         * @SP
         * M=M-1
         * A=M
         * M=D&M
         */
        return "@SP\nM=M-1\nA=M\nD=M\n@SP\nM=M-1\nA=M\nM=D&M";
    }

    static String or() {
        /*
         * @SP
         * M=M-1
         * A=M
         * D=M
         * @SP
         * M=M-1
         * A=M
         * M=D|M
         */
        return "@SP\nM=M-1\nA=M\nD=M\n@SP\nM=M-1\nA=M\nM=D|M";
    }

    static String not() {
        /*
         * @SP
         * M=M-1
         * A=M
         * M=!M
         */
        return "@SP\nM=M-1\nA=M\nM=!M";
    }

    static String goTo(String label) {
        /*
         * @{label}
         * 0;JMP
         */
        return "@{label}\n0;JMP".replace("{label}", label);
    }

    static String ifGoTo(String label) {
        /*
         * @SP
         * M=M-1
         * A=M
         * D=M
         * @{label}
         * D;JNE
         */
        return "@SP\nM=M-1\nA=M\nD=M\n@{label}\nD;JNE".replace("{label}", label);
    }
}

enum CommandType {
    C_ARITHMETIC,
    C_PUSH,
    C_POP,
    C_LABEL,
    C_GOTO,
    C_IFGOTO,
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
    SEG_THAT,
    SEG_TEMP,
    SEG_POINTER
}
