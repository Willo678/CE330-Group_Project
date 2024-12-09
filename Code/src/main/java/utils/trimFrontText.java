package utils;

public class trimFrontText {
    public static String trimFrontText(String text, int trimLength) {
        if (text.length()>trimLength) {
            text = ".."+text.substring(text.length()-(trimLength+2));
        }
        while (text.length()<trimLength) {
            text+=" ";
        }
        return text;
    }
}
