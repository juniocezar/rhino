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

public class SimpleParser {    
    public static void  main(String[] args) throws FileNotFoundException, java.io.IOException {
        AstRoot astRoot;
        CompilerEnvirons environment = new CompilerEnvirons();
        environment.setRecordingComments(true);
        environment.setRecordingLocalJsDocComments(true);

        File scriptFile = new File("/home/juniocezar/test.js");
        try
        {
            FileReader fr = new FileReader(scriptFile); 
            Parser p = new Parser(environment);
            astRoot = p.parse(fr, null, 1);
        } catch (FileNotFoundException ex)  
        {
            return;
        }

        astRoot.visit(new NodeVisitor() {
            @Override
            public boolean visit(AstNode node) {
                int nodeType  = node.getType();
                //TODO: process the node based on node type
                System.out.println(nodeType + " |" + node.shortName() + " | " + node.toSource(0));
                if(nodeType == 41) {
                    Node nnode = new Node(41);
                    StringLiteral tst = new StringLiteral();
                    //StringLiteral tst = (StringLiteral)nnode;
                    tst.setValue("Boceta");
                    node.addChildBefore(nnode, node);
                    ((StringLiteral)node).setValue("Cole");
                }   
                return true; //process children
            }
        });

        System.out.println(astRoot.toSource(0));

    }
}
