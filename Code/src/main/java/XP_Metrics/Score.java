package XP_Metrics;

public class Score {
    public int score;
    public String reason;

    public Score(int score, String reason) {
        this.score = score;
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "["+score + ", reason=" + reason + "]";
    }
}
