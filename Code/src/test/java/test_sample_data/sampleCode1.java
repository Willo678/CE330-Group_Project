package test_sample_data;

public class sampleCode1 {

        public int score;
        public String reason;

        public sampleCode1(int score, String reason) {
            this.score = score;
            this.reason = reason;
        }

        @Override
        public String toString() {
            return "["+score + ", reason=" + reason + "]";
        }


}
