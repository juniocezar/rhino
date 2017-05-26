package org.mozilla.javascript;

import org.mozilla.javascript.ast.*;

import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.Token;

import java.io.*; 
import java.util.Scanner;
import java.util.List;

public class SimpleParser {    
    static int identifiers = 1;
    static Node b;

    public static void  main(String[] args) throws FileNotFoundException, java.io.IOException {
        AstRoot astRoot;
        CompilerEnvirons environment = new CompilerEnvirons();
        environment.setRecordingComments(true);
        environment.setRecordingLocalJsDocComments(true);

        // Replace the string by the file path.
        // or you can use an argument as file input
        File scriptFile = new File("/home/grad/ccomp/12/juniocezar/test.js"); 
        try {
            FileReader fr = new FileReader(scriptFile); 
            Parser p = new Parser(environment);
            astRoot = p.parse(fr, null, 1);
        } catch (FileNotFoundException ex) {
            return;
        }

        // Implementing the visit interface
        // may be done as well in a secondary class
        // This method visit every node in a 'aleatory way'
        // All nodes are seen as members os a linked list and
        // function visit iterates over this list
        astRoot.visit(new NodeVisitor() {
            @Override
            public boolean visit(AstNode node) {
                int nodeType = node.getType();
                if(nodeType == Token.FUNCTION) {
                    handleFunction(node);
                }                
                //processNode(node);
                return true; //process children
            }
        });


        // another way to iterate over the AST is by getting each node
        // identify it by its Token type and the process it individually
        // this way we get the astRoot, check if it hasChildren(). get the
        // first child, getFirstChild, for each child we can get its children
        // for each child we can get its siblings with the next property.
        // each AstNode has its speciallized functions to get its members
        // like getName, getParams, getBody for a FunctionNode


        System.out.println("\n\n === Instrumented code ===\n" + astRoot.toSource());
    }

    private static void processNode(AstNode node) {
        int nodeType  = node.getType();
        System.out.println(nodeType + " |" + node.shortName() + " | " + node.toSource(0));
    }


    private static void handleFunction(AstNode fNode) {
        
        String fName = ((FunctionNode)fNode).getName();
        List<AstNode> params = ((FunctionNode)fNode).getParams();
        AstNode body = ((FunctionNode)fNode).getBody();

        //System.out.println("\nFunction Name: " + fName);
        for (AstNode node : params) {
            String argName = ((Name)node).getIdentifier();
            //System.out.println("\nArgument: " + argName);
            if(specialize(node)) {
                StringLiteral spec = new StringLiteral();
                spec.setValue("specialize " + argName);
                spec.setQuoteCharacter('\"');

                ExpressionStatement expr = new ExpressionStatement(spec);

                // System.out.println("specialized -> " + spec.getValue());                

                body.addChildToFront(expr);

                // System.out.println("fNode -> " + fNode.toSource());

            }
        }
    }

    private static boolean specialize(AstNode node) {
        // Some logic here to decide either
        // to specialize or not
        return true; 
    }    

}























































