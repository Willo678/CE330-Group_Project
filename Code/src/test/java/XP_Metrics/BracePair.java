package XP_Metrics;

public class BracePair {
    private int id;
    private String methodName;
    private String type;
    private int start;
    private int end;

    public BracePair(int id, String methodName, String type, int start, int end) {
        this.id = id;
        this.methodName = methodName;
        this.type = type;
        this.start = start;
        this.end = end;
    }

    // Getter and Setter
    public int getId() { return id; }
    public String getMethodName() { return methodName; }
    public String getType() { return type; }
    public int getStart() { return start; }
    public int getEnd() { return end; }
}
