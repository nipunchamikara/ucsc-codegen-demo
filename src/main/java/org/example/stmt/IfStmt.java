package org.example.stmt;

import org.example.expr.Expr;

/**
 * IfStmt represents an if statement.
 */
public class IfStmt extends Stmt {
    Expr expr;
    Stmt[] stmts;
    Stmt[] elseStmts;

    /**
     * Constructor.
     * @param expr the expression to evaluate
     * @param stmts the statements to execute if the expression is true
     * @param elseStmts the statements to execute if the expression is false
     */
    public IfStmt(Expr expr, Stmt[] stmts, Stmt[] elseStmts) {
        this.expr = expr;
        this.stmts = stmts;
        this.elseStmts = elseStmts;
    }

    /**
     * Get the expression.
     * @return the expression
     */
    public Expr getExpr() {
        return expr;
    }

    /**
     * Get the statements to execute if the expression is true.
     * @return the statements to execute if the expression is true
     */
    public Stmt[] getStmts() {
        return stmts;
    }

    /**
     * Get the statements to execute if the expression is false.
     * @return the statements to execute if the expression is false
     */
    public Stmt[] getElseStmts() {
        return elseStmts;
    }
}
