package logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ExpressionFormatter {
    public static char[] render(String expression) {
        String formatString = expression.replaceAll("\\s", "");
        formatString = formatString.replaceAll("\\)\\(", ")*(");

        Pattern pattern = Pattern.compile("[-+*/(]-\\(");
        Matcher matcher = pattern.matcher(formatString);

        while (matcher.find()) {
            formatString = formatString.replace(matcher.group(), matcher.group().charAt(0)+"-1*(");
        }
        pattern = Pattern.compile("[0-9]\\(");
        matcher = pattern.matcher(formatString);

        while (matcher.find()) {
            formatString = formatString.replace(matcher.group(), matcher.group().charAt(0)+"*(");
        }

        formatString = formatString.replaceAll("^-\\(", "-1*(");

        long openBrackets = formatString.chars().filter(ch -> ch == '(').count();
        long closeBrackets = formatString.chars().filter(ch -> ch == ')').count();
        long newSize = openBrackets - closeBrackets;

        char[] tokens = new char[(int) (formatString.length() + newSize)];

        System.arraycopy(formatString.toCharArray(),0,tokens,0,formatString.length());

        for (int i = formatString.length(); i < tokens.length; i++) {
            tokens[i] = ')';
        }

        return tokens;
    }
}
