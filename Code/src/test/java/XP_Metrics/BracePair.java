package XP_Metrics;

public class BracePair {
    private int lineNumber;
    private String name;
    private String type;
    private int startBrace;
    private int endBrace;

    public BracePair(int lineNumber, String name, String type, int startBrace, int endBrace) {
        this.lineNumber = lineNumber;
        this.name = name;
        this.type = type;
        this.startBrace = startBrace;
        this.endBrace = endBrace;
    }

    public String getName() {
        return name;
    }
}
