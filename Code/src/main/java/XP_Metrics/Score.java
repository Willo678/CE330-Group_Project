package XP_Metrics;

public class Score {
    public int score;
    public String reason;

    public Score(int score, String reason) {
        this.score = score;
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "["+score + ", reason=" + reason + "]";
    }
}
