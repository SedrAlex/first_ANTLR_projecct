package app;

import antlr.ExprLexer;
import antlr.ExprParser;
import expression.AntlrToProgram;
import expression.ExpressionProcessor;
import expression.Program;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

public class ExpressionApp {
    public static void main(String[] args) {
        if(args.length != 1){
            System.err.print("Usage : file name");
        }
        else{
            String fileName = args[0];
            ExprParser parser  = getParser(fileName);
            //tell antlr to build a parser tree
            //parse from the start symbol 'prog'
            ParseTree antlrAST = parser.prog();
            //Create a visitor for converting the parse tree into  Program/ExpressionObject, the main purpose for creating the visit class is by converting from a parsetree into objectmodel
            AntlrToProgram progVisitor = new AntlrToProgram();
            Program prog = progVisitor.visit(antlrAST);

            if(progVisitor.semanticErrors.isEmpty()) {
                ExpressionProcessor ep = new ExpressionProcessor(prog.expressions);
                for (String evaluation : ep.getEvaluationResults()) {
                    System.out.println(evaluation);
                }
            }
            else {
                  for(String err :progVisitor.semanticErrors){
                      System.out.println(err);
                }
            }
        }
    }
    /*
    *Here the types of parser and lexer  are specific to the grammer name Expr.g4
    * */
    private   static  ExprParser getParser(String fileName) {
        ExprParser parser = null;

        try {
            CharStream input = CharStreams.fromFileName(fileName);
            ExprLexer lexer = new ExprLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            parser = new ExprParser(tokens);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return parser;
    }
}