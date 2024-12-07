package XP_Metrics;

public class Score {
    private int score;
    private String description;

    public Score(int score, String description) {
        this.score = score;
        this.description = description;
    }

    public int getScore() {
        return score;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Score{" +
                "score=" + score +
                ", description='" + description + '\'' +
                '}';
    }
}
