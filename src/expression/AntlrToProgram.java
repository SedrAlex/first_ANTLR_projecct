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
          AntlrToExpression exprVisitor = new AntlrToExpression()

         return  prog;
    }
}
