package test_sample_data;
import java.io.*;
public class sampleCode1 {

        //Example Comment

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
