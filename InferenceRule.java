public interface InferenceRule {
    boolean matches(Expression exp1, Expression exp2);
    Expression apply(Expression exp1, Expression exp2);  
}
//"P > Q" and "P", the rule  allows inferring "Q" 
 class ModusPonensRule implements InferenceRule{ 
    @Override
    public boolean matches(Expression exp1, Expression exp2) {
        String[] expression1 = exp1.getSplittedExpression();
        String[] expression2 = exp2.getSplittedExpression();

        if(expression1.length==3&&expression2.length==1){
            if(expression1[0].equals(expression2[0])&&expression1[1].equals(">")){
                return true;
            }
        }
        return false;
    }
    @Override
    public Expression apply(Expression exp1, Expression exp2) {
        String[] expression1 = exp1.getSplittedExpression();
        return new MyExpression(expression1[2], "Modus Ponens");
    }
}
//"P > Q" and "~Q", the rule allows inferring "~P" 
 class ModusTollensRule implements InferenceRule{ 
    @Override
    public boolean matches(Expression exp1, Expression exp2) {
        String[] expression1 = exp1.getSplittedExpression();
        String[] expression2 = exp2.getSplittedExpression();

        if(expression1.length==3&&expression2.length==1){
            if ((expression1[2].equals("~" + expression2[0]) || (expression2[0].equals("~" + expression1[2]))) && expression1[1].equals(">"))
            return true;
            }
            return false;
        }
    @Override
    public Expression apply(Expression exp1, Expression exp2) {
        String[] expression1 = exp1.getSplittedExpression();
        String result;
        if(expression1[0].length()==2){
            result= String.valueOf(expression1[0].charAt(1));
        }else{
            result="~"+expression1[0];
        }
        return new MyExpression(result, "Modus Tollens");
    }
}
//"P > Q" and "Q > R", the rule allows inferring "P > R"
 class HypotheticalSyllogismRule implements InferenceRule{ 
    @Override
    public boolean matches(Expression exp1, Expression exp2) {
        String[] expression1 = exp1.getSplittedExpression();
        String[] expression2 = exp2.getSplittedExpression();

        if(expression1.length==3&&expression2.length==3){
            if (expression2[1].equals(">") && expression1[1].equals(">") && expression1[2].equals(expression2[0])){
            return true;
          }
        }
        return false;
    }
    @Override
    public Expression apply(Expression exp1, Expression exp2) {
        String[] expression1 = exp1.getSplittedExpression();
        String[] expression2 = exp2.getSplittedExpression();
        return new MyExpression(expression1[0]+">"+expression2[2], "Hypothetical Syllogism");
    }
}
// "P v Q" and "~P", the rule allows inferring "Q"
 class DisjunctiveSyllogismRule implements InferenceRule{ 
    @Override
    public boolean matches(Expression exp1, Expression exp2) {
        String[] expression1 = exp1.getSplittedExpression();
        String[] expression2 = exp2.getSplittedExpression();

        if(expression1.length==3&&expression2.length==1){
            if (expression1[1].equals("v")) {
                if (expression1[0].equals("~" + expression2[0]) || expression2[0].equals("~" + expression1[0])){
                    return true;
                }
                if (expression1[2].equals("~" + expression2[0]) || expression2[0].equals("~" + expression1[2])){
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public Expression apply(Expression exp1, Expression exp2) {
        String[] expression1 = exp1.getSplittedExpression();
        String[] expression2 = exp2.getSplittedExpression();
        String result;
        if (expression1[0].equals("~" + expression2[0]) || expression2[0].equals("~" + expression1[0])){
          result = expression1[2];
        }
       else{
        result = expression1[0];
       }
    return new MyExpression(result, "Disjunctive Syllogism");
    }
}
    //  "P v Q" and "~P v R", the rule allows inferring "Q v R"
    class ResolutionRule implements InferenceRule{ 
        @Override
        public boolean matches(Expression exp1, Expression exp2) {
            String[] expression1 = exp1.getSplittedExpression();
            String[] expression2 = exp2.getSplittedExpression();
            if (expression1.length == 3 && expression2.length == 3) {
                if (expression1[1].equals("v") && expression2[1].equals("v")) {
                    if (expression1[0].equals("~" + expression2[0]) || expression2[0].equals("~" + expression1[0])){
                        return true;
                    }
                    if (expression1[0].equals("~" + expression2[2]) || expression2[2].equals("~" + expression1[0])){
                        return true;
                    }
                    if (expression1[2].equals("~" + expression2[0]) || expression2[0].equals("~" + expression1[2]))
                        return true;
                    if (expression1[2].equals("~" + expression2[2]) || expression1[2].equals("~" + expression1[2]))
                        return true;
                }
            }
            return false;
        }
        @Override
        public Expression apply(Expression exp1, Expression exp2) {
            String[] ex1 = exp1.getSplittedExpression();
            String[] ex2 = exp2.getSplittedExpression();
            String result;
            if (ex1[0].equals("~" + ex2[0]) || ex2[0].equals("~" + ex1[0]))
                result = ex1[2] + " v " + ex2[2];
            else if (ex1[0].equals("~" + ex2[2]) || ex2[2].equals("~" + ex1[0]))
                result = ex1[2] + " v " + ex2[0];
            else if (ex1[2].equals("~" + ex2[0]) || ex2[0].equals("~" + ex1[2]))
                result = ex1[0] + " v " + ex2[2];
            else
                result = ex1[0] + " v " + ex2[0];
            return new MyExpression(result, "Resolution");
        }
    }



    