/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

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
        File scriptFile = new File("/home/junio/test.js"); 
        try {
            FileReader fr = new FileReader(scriptFile); 
            Parser p = new Parser(environment);
            astRoot = p.parse(fr, null, 1);
        } catch (FileNotFoundException ex) {
            return;
        }

        // Implementing the visit interface
        // may be done as well in a secondary class
        astRoot.visit(new NodeVisitor() {
            @Override
            public boolean visit(AstNode node) {
                int nodeType = node.getType();
                /*if(nodeType == Token.FUNCTION) {
                    handleFunction(node);
                }*/
                if(nodeType == Token.STRING) {
                    Node copy = new Node(Token.STRING);                    
                    copy.setNext(node.getNext());
                    node.setNext(copy);
                    //node.addChildToFront(tst);
                }
                //processNode(node);
                return true; //process children
            }
        });


        astRoot.visit(new NodeVisitor() {
            @Override
            public boolean visit(AstNode node) {                
                processNode(node);
                return true; //process children
            }
        });        

        System.out.println("\n\n === Instrumented code ===\n" + astRoot.toSource());
    }

    private static void processNode(AstNode node) {
        int nodeType  = node.getType();
        System.out.println(nodeType + " |" + node.shortName() + " | " + node.toSource(0));
    }


    private static void handleFunction(AstNode fNode) {
        
        String fName = ((FunctionNode)fNode).getName();
        List<AstNode> params = ((FunctionNode)fNode).getParams();

        System.out.println("\nFunction Name: " + fName);
        for (AstNode node : params) {
            String argName = ((Name)node).getIdentifier();
            System.out.println("\nArgument: " + argName);
            if(specialize(node)) {
                StringLiteral spec = new StringLiteral();
                spec.setValue("specialize " + argName);

                ExpressionStatement expr = new ExpressionStatement(spec);

                System.out.println("specialized -> " + spec.getValue());
                

                Node store = new Node(Token.EXPR_RESULT);


                
                fNode.addChildToBack(store);

                System.out.println("fNode -> " + fNode.toSource());

            }
        }
    }

    private static boolean specialize(AstNode node) {
        // Some logic here to decide either
        // to specialize or not
        return true; 
    }    

}























































