package expression;

import antlr.ExprBaseVisitor;
import antlr.ExprParser;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import  java.util.List;
public class AntlrToExpression extends ExprBaseVisitor<Expression> {
    /*
     *Given that all visit_* methods are called in a top-down fashion,
     * we can be sure the order in which we add declared variables in the 'vars' is identical to how
     * they are declared in the input program.
     */
    private List<String> vars; // stores all the variables declared in the progrram so far
    private List<String> semanticErrors; // 1. duplicate declaration  2. reference to undeclared variable

    //Note that semantic errors are different from syntax errors


    // This is really cruical because it won't compile if we did not handle this nullpointerexception
    public AntlrToExpression(List<String> semanticErrors){
        vars = new ArrayList<>(); // Assign this to new empty list
        this.semanticErrors = semanticErrors;
    }
    @Override
    public Expression visitDeclaration(ExprParser.DeclarationContext ctx) {
        //ID is a method generated to correspond to the token ID in the source grammer
         Token idToken = ctx.ID().getSymbol();// it is used to take  the token object, equivlivent to: ctx.getChild(0.getSymbol()
         int line = idToken.getLine();// to take the line of a particiular token
         int column = idToken.getCharPositionInLine() + 1;// whatever the position is it is starting from zero to 1
         String id = ctx.getChild(0).getText();
         //Maintaining the vars list for semantics error reporting
         if (vars.contains(id)){
             semanticErrors.add("Errors : variable" + id + "already declared ( " + line + "," + column + ")");// to check if we duplicate the declaration of the variable
    }
         else{
              vars.add(id);
         }
         String type = ctx.getChild(2).getText();
         // in declaration we need to check the type of the variable if it is integer, string and it is mostly the second child
         int value = Integer.parseInt(ctx.NUM().getText());// here we get the value and we parse and change it to int
         return new VariableDeclaration(id, type, value);

}
    @Override
    public Expression visitMultiplication(ExprParser.MultiplicationContext ctx) {
          Expression left = visit(ctx.getChild(0)); //recursively visit the left subtree of the current  Multiplication node we inherit it from the ExpressionVisitor
          Expression right = visit(ctx.getChild(2)); //index one actually is the multiplication sign
         return new Multiplication(left,right);
    }

    @Override
    public Expression visitAddition(ExprParser.AdditionContext ctx) {
        Expression left = visit(ctx.getChild(0)); //recursively visit the left subtree of the current  Addition node
        Expression right = visit(ctx.getChild(2));
        return new Multiplication(left,right);    }

    @Override
    public Expression visitVariable(ExprParser.VariableContext ctx) {
        Token idToken = ctx.ID().getSymbol();
        int line = idToken.getLine();
        int column = idToken.getCharPositionInLine() + 1;

        String id = ctx.getChild(0).getText();

        if (!vars.contains(id)){
            semanticErrors.add("Errors : variable" + id + "not declared ( " + line + "," + column + ")");
        }
        return  new Variable(id);
    }
// expression maybe nested any levels you would like so it should be for estimating level as you would like that is recursive
    @Override
    public Expression visitNumber(ExprParser.NumberContext ctx) {
    String numText = ctx.getChild(0).getText(); // we want to get the child of this node expression to get a string of this expression
    int num = Integer.parseInt(numText); // we want to convert this to number
    return new Number(num);// the return type should be expression and NUMBER is a type of expression
    }
}
