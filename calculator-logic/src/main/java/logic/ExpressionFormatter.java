package logic;

import exceptions.InvalidExpressionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ExpressionFormatter {
    public static char[] render(String expression) throws InvalidExpressionException {
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

        if(openBrackets - closeBrackets != 0){
            throw new InvalidExpressionException("Wrong number of brackets");
        }

        return formatString.toCharArray();
    }
}
