package XP_Metrics;

public class Token {
    private int id;
    private String type;
    private String value;
    private int lineNumber;

    public Token(int id, String type, String value, int lineNumber) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.lineNumber = lineNumber;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public String getValue() { return value; }
    public int getLineNumber() { return lineNumber; }
}
