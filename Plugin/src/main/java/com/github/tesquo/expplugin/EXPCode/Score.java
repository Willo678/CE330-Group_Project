package com.github.tesquo.expplugin.EXPCode;

public class Score {
    public int score;
    public String reason;
    public int line;

    public Score(int score, String reason, int line) {
        this.score = score;
        this.reason = reason;
        this.line = line;
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
        return "["+score + ", reason=" + reason + ", at line " + line + "]";
    }
}

