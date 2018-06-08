package com.vegw.compiler.Compiler;

public class Options {
    static final String ASM_POSTFIX = "s";
    static final String OBJ_POSTFIX = "o";
    private String srcPath;
    private String asmPath;
    private String dstPath;

    public Options() {
        srcPath = null;
        dstPath = null;
    }

    public Options(Options other) {
        this.srcPath = other.srcPath;
        this.dstPath = other.dstPath;
    }

    public static Options parse(String[] args) {   
        Options tempOp = new Options();

        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            switch (arg) {
                // case "-no-ssa":
                //     CompilerOptions.enableSSA = false;
                //     break;

                // case "-print-ast":
                //     CompilerOptions.ifPrintAST = true;
                //     break;

                // case "-print-ir":
                //     CompilerOptions.ifPrintRawIR = true;
                //     break;

                // case "-print-ssa-ir":
                //     CompilerOptions.ifPrintSSAIR = true;
                //     break;

                // case "-reg-alloc":
                //     if (i+1 >= args.length) printHelpAndExit(true);
                //     CompilerOptions.registerAllocator = args[++i];
                //     break;

                case "-o":
                    if (i+1 >= args.length) printHelpAndExit(true);
                    tempOp.dstPath = args[++i];
                    break;

                // case "-no-inline":
                //     CompilerOptions.enableInline = false;
                //     break;

                // case "-no-naive-dce":
                //     CompilerOptions.eliminateDeadCode = false;
                //     break;

                // case "-no-scp":
                //     CompilerOptions.simpleConstantPropagate = false;
                //     break;

                // case "-help":
                // case "--help":
                // case "-?":
                // case "/?":
                //     printHelpAndExit(false);
                //     break;

                default:
                    if (tempOp.srcPath != null) printHelpAndExit(true);
                    tempOp.srcPath = arg;
            }
            return tempOp;
        }
        if (tempOp.srcPath == null) printHelpAndExit(true);
        String[] splitRes = tempOp.srcPath.split("\\.");
        tempOp.asmPath = splitRes[0] + ASM_POSTFIX;
        if (tempOp.dstPath == null) {
            // The destination is not determined by user
            // Use default settings "*.s"
            tempOp.dstPath = splitRes[0] + OBJ_POSTFIX;
        }
        return tempOp;
    }

    private static void printHelpAndExit(boolean illegal) {
        if (illegal) System.out.println("Error! Unknown argument sequence.");
        System.out.println("Mill - Mx* language implementation made with love by abcdabcd987");
        System.out.println("Usage: mill [options] [input]");
        System.out.println("Options:");
        System.out.println("  -help              Print this help message");
        System.out.println("  -o <file>          Write output to <file>");
        System.out.println("  -reg-alloc <val>   Set register allocator to <val>");
        System.out.println("                     Available register allocators:");
        System.out.println("                       no:    Don't allocate at all. (CISC-like)");
        System.out.println("                       local: Local bottom-up allocator");
        System.out.println("                       color: Global allocation by interference graph coloring");
        System.out.println("  -print-ast         Print the abstract semantic tree");
        System.out.println("  -print-ir          Print the intermediate representation");
        System.out.println("  -print-ssa-ir      Print the intermediate representation after SSA transforms");
        System.out.println("  -no-inline         Disable function inlining");
        System.out.println("  -no-ssa            Disable single static assignment analysis and transforms");
        System.out.println("  -no-naive-dce      Disable naive dead code elimination");
        System.out.println("  -no-scp            Disable simple constant propagate");
        System.exit(illegal ? 1 : 0);
    }

    public String SourceFile() {
        return this.srcPath;
    }

    public String asmFileName() {
        return this.asmPath;
    }

    public String objFileName() {
        return this.dstPath;
    }
}