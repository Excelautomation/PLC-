package dk.aau.sw402F15.Rewriter;

import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikkel on 22-04-2015.
 */
public class ASTSimplify extends DepthFirstAdapter {

    @Override
    public void outAForStatement(AForStatement node) {
        List<PStatement> statementList = new ArrayList<PStatement>();
        List<PStatement> statements = new ArrayList<PStatement>();

        statements.add(node.getStatement());
        for (PExpr expr : node.getIterator())
            statements.add(new AExprStatement(expr));

        for (PExpr expr : node.getInitilizer())
            statements.add(new AExprStatement(expr));
        statementList.add(new AWhileStatement(node.getCondition() == null ? new ATrueExpr() : node.getCondition(), new AScopeStatement(statements)));

        Node newNode = new AScopeStatement(statementList);

        // Apply newnode
        newNode.apply(this);

        // Replace node
        node.replaceBy(newNode);
    }

    @Override
    public void caseAModExpr(AModExpr node) {
        // 3 % 2 = std.mod(3, 2) // STDLIB
        List<PExpr> argumentList = new ArrayList<PExpr>();
        argumentList.add(node.getLeft());
        argumentList.add(node.getRight());

        Node newNode =
                new AMemberExpr(
                        new AFunctionCallExpr(new TIdentifier("std"), new ArrayList<Object>()),
                        new AFunctionCallExpr(new TIdentifier("mod"), argumentList
                        )
                );

        // Apply newnode
        newNode.apply(this);

        // Replace node
        node.replaceBy(newNode);
    }

    @Override
    public void caseACompoundAddExpr(ACompoundAddExpr node) {
        Node newNode = new AAssignmentExpr(
                (PExpr)node.getLeft().clone(),
                new AAddExpr((PExpr)node.getLeft().clone(), (PExpr)node.getRight().clone())
        );

        // Apply newnode
        newNode.apply(this);

        // Replace node
        node.replaceBy(newNode);
    }

    @Override
    public void caseACompoundSubExpr(ACompoundSubExpr node) {
        Node newNode = new AAssignmentExpr(
                (PExpr)node.getLeft().clone(),
                new ASubExpr((PExpr)node.getLeft().clone(), (PExpr)node.getRight().clone())
        );

        // Apply newnode
        newNode.apply(this);

        // Replace node
        node.replaceBy(newNode);
    }

    @Override
    public void caseACompoundMultExpr(ACompoundMultExpr node) {
        Node newNode = new AAssignmentExpr(
                (PExpr)node.getLeft().clone(),
                new AMultiExpr((PExpr)node.getLeft().clone(), (PExpr)node.getRight().clone())
        );

        // Apply newnode
        newNode.apply(this);

        // Replace node
        node.replaceBy(newNode);
    }

    @Override
    public void caseACompoundDivExpr(ACompoundDivExpr node) {
        Node newNode = new AAssignmentExpr(
                (PExpr)node.getLeft().clone(),
                new ADivExpr((PExpr)node.getLeft().clone(), (PExpr)node.getRight().clone())
        );

        // Apply newnode
        newNode.apply(this);

        // Replace node
        node.replaceBy(newNode);
    }

    @Override
    public void caseACompoundModExpr(ACompoundModExpr node) {
        Node newNode = new AAssignmentExpr(
                (PExpr)node.getLeft().clone(),
                new AModExpr((PExpr)node.getLeft().clone(), (PExpr)node.getRight().clone())
        );

        // Apply newnode
        newNode.apply(this);

        // Replace node
        node.replaceBy(newNode);
    }

    @Override
    public void caseATimerTypeSpecifier(ATimerTypeSpecifier node) {
        Node newNode = new AStructTypeSpecifier(new TIdentifier("stdtimer"));

        // Apply newnode (don't do anything but maybe in inherited class)
        newNode.apply(this);

        // Replace node
        node.replaceBy(newNode);
    }
}