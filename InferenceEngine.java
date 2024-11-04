import java.util.ArrayList;
import java.util.List;


public interface InferenceEngine {
    void addRule(InferenceRule rule);
    void addExpression(Expression exp);
    Expression applyRules();
}
class MyInferenceEngine implements InferenceEngine {
    private List<InferenceRule> rules = new ArrayList<>();
    private List<Expression> expressions = new ArrayList<>();

    @Override
    public void addRule(InferenceRule rule) {
        rules.add(rule);
    }

    @Override
    public void addExpression(Expression exp) {
        expressions.add(exp);
    }

    @Override
    public Expression applyRules() {
        for (Expression exp1 : expressions) {
            for (Expression exp2 : expressions) {
                if (exp1 != exp2) {
                    for (InferenceRule rule : rules) {
                        if (rule.matches(exp1, exp2)) {
                            Expression result = rule.apply(exp1, exp2);
                            expressions.add(result);
                            return result;
                        }
                    }
                }
            }
        }
        return null;
    }
}