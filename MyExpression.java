import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

 interface Expression {
    public String getPostfix();
    String getRepresentation();
    void setRepresentation(String representation);
    boolean isOperator(char c);
    public Set<Character>getVariables();
    public void setValues(HashMap<Character, Boolean> map);
    public HashMap<Character, Boolean> getValues();
    public boolean validateExpression();
    public String getRule();
    public String[] splitExpression(String expression);
    public String[] getSplittedExpression();

}
public class MyExpression implements Expression {
    private String representation;
    private String postfix;
    private Set<Character> variables;
    private HashMap<Character, Boolean> values; // User input for variable values
    private String[] splitExpression;
    private String rule;

    // Constructor without rule
    public MyExpression(String expression) {
        expression = expression.replaceAll(" ", "").replaceAll("~~", "");
        this.representation = expression;
        this.splitExpression = splitExpression(expression);
        setRepresentation(expression); // Generate postfix
    }

    // Constructor with rule
    public MyExpression(String expression, String rule) {
        expression = expression.replaceAll(" ", "").replaceAll("~~", "");
        this.representation = expression;
        this.splitExpression = splitExpression(expression);
        this.rule = rule;
        setRepresentation(expression); // Generate postfix
    }

    @Override
    public String getPostfix() {
        return this.postfix;
    }

    @Override
    public String getRepresentation() {
        return this.representation;
    }

    @Override
    public String[] getSplittedExpression() {
        return this.splitExpression;
    }

    @Override
    public String getRule() {
        return this.rule;
    }

    @Override
    public String[] splitExpression(String expression) {
        List<String> tokens = new ArrayList<>();
        String pattern = "((~+)\\w|[^\\s])";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(expression);

        while (matcher.find()) {
            tokens.add(matcher.group());
        }

        return tokens.toArray(new String[0]);
    }

    public boolean isOperator(char c) {
        return (c == '~' || c == '^' || c == 'v' || c == '>' || c == '(' || c == ')');
    }

    @Override
    public void setRepresentation(String representation) {
        this.representation = representation;
        Stack<Character> operators = new Stack<>();
        StringBuilder postfixBuilder = new StringBuilder();
        HashMap<Character, Integer> precedence = new HashMap<>();
        precedence.put('~', 4);
        precedence.put('^', 3);
        precedence.put('v', 2);
        precedence.put('>', 1);

        this.variables = new HashSet<>();
        int length = representation.length();
        
        for (int i = 0; i < length; i++) {
            char current = representation.charAt(i);
            if (isOperator(current)) {
                if (current == '(') {
                    operators.push(current);
                } else if (current == ')') {
                    while (!operators.isEmpty() && operators.peek() != '(') {
                        postfixBuilder.append(operators.pop());
                    }
                    operators.pop(); // Remove the '('
                } else { // Other operators
                    while (!operators.isEmpty() && operators.peek() != '(' &&
                            precedence.get(current) <= precedence.get(operators.peek())) {
                        postfixBuilder.append(operators.pop());
                    }
                    operators.push(current);
                }
            } else { // Operand
                postfixBuilder.append(current);
                if (Character.isLetter(current)) {
                    variables.add(current);
                }
            }
        }

        while (!operators.isEmpty()) {
            postfixBuilder.append(operators.pop());
        }

        this.postfix = postfixBuilder.toString();
    }

    @Override
    public Set<Character> getVariables() {
        return this.variables;
    }

    @Override
    public void setValues(HashMap<Character, Boolean> map) {
        this.values = map;
    }

    @Override
    public HashMap<Character, Boolean> getValues() {
        return this.values != null ? this.values : new HashMap<>();
    }

    @Override
    public boolean validateExpression() {
        if (this.representation == null) {
            return false; // Invalid if representation is null
        }

        String infix = this.representation;
        Stack<Character> brackets = new Stack<>();
        int length = infix.length();

        // Check if last character is invalid
        if (isOperator(infix.charAt(length - 1)) && infix.charAt(length - 1) != ')') {
            return false;
        }

        for (int i = 0; i < length - 1; i++) {
            char current = infix.charAt(i);

            if (current == '(') {
                brackets.push(current);
            } else if (current == ')') {
                if (brackets.isEmpty() || brackets.peek() != '(') {
                    return false;
                }
                brackets.pop();
            } else if (isOperator(current) && i + 1 < length && isOperator(infix.charAt(i + 1))) {
                if (infix.charAt(i + 1) != '~' && infix.charAt(i + 1) != '(') {
                    return false;
                }
            } else if (!isOperator(current) && !isOperator(infix.charAt(i + 1))) {
                return false;
            }
        }

        if (!brackets.isEmpty()) {
            return false;
        }

        String postfix = this.postfix;
        int operands = 0;

        for (int i = 0; i < postfix.length(); i++) {
            char current = postfix.charAt(i);
            if (!isOperator(current)) {
                operands++;
            } else if (current != '~' && operands < 2) {
                return false;
            } else if (current != '~') {
                operands--; // For binary operators
            }
        }

        return operands == 1;
    }
}