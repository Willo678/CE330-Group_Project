package XP_Metrics;

public class Token {
    private int lineNumber;
    private String type;
    private String value;
    private int position;

    public Token(int lineNumber, String type, String value, int position) {
        this.lineNumber = lineNumber;
        this.type = type;
        this.value = value;
        this.position = position;
    }

    public String getType() {
        return type;
    }
}
