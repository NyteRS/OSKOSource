package mgi.tools.parser;

public class PackNPCDefinitionsRunner {
    public static void main(String[] args) {
        TypeParser.packNPCDefinitionsMain(args);
        TypeParser.dumpNPCsMain(args);
        TypeParser.loadNPCsFromJsonMain(args);
    }
}

