import java.util.Scanner;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) {


        MyInferenceEngine engine = new MyInferenceEngine();
        Scanner in = new Scanner(System.in);
        engine.addRule(new ModusPonensRule());
        engine.addRule(new ModusTollensRule());
        engine.addRule(new HypotheticalSyllogismRule());
        engine.addRule(new DisjunctiveSyllogismRule());
        engine.addRule(new ResolutionRule());
        System.out.print("Enter the first expression: ");
        String ex1 = in.nextLine();
        MyExpression exp1 = new MyExpression(ex1);
        System.out.print("Enter the second expression: ");
        String ex2 = in.nextLine();
        MyExpression exp2 = new MyExpression(ex2);
        if (!exp1.validateExpression()) {
            System.out.println("Expression 1 is invalid!");
            exit(1);
        }
        if (!exp2.validateExpression()) { // invalid
            System.out.println("Expression 2 is invalid!");
            exit(1);
        }
        engine.addExpression(exp1);
        engine.addExpression(exp2);
        Expression result = engine.applyRules();
        if (result != null) {
            System.out.println("result :"+result.getRepresentation());
            System.out.println("rule: "+result.getRule());
        } else {
            System.out.println("error");
        }
    }
}