package expression;

import antlr.ExprBaseVisitor;
import antlr.ExprParser;

import java.util.ArrayList;
import java.util.List;

public class AntlrToProgram extends ExprBaseVisitor<Program> {

    public List<String> semanticErrors; // to be accessed by the main application programm
    @Override
    public Program visitProgramm(ExprParser.ProgrammContext ctx) {
         Program prog = new Program();

          semanticErrors  = new ArrayList<>();
          // helper visitor for transforming each subtree into an Expression object
          AntlrToExpression exprVisitor = new AntlrToExpression(semanticErrors);
          // We want to visit each subtree because we want to count the number of children
          for(int i = 0; i < ctx.getChildCount(); i++) {
               if(i == ctx.getChildCount() - 1){
                   /* last child of the start symbol prig is EOF     */
                   //Do not visit this child and never attempt to convert it to an expression object
               }
               else{
                  prog.addExpression(exprVisitor.visit(ctx.getChild(i)));

               }
          }

         return  prog;
    }
}
