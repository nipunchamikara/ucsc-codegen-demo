package org.example.stmt;

import org.example.expr.Expr;

/**
 * Return statement
 */
public class ReturnStmt extends Stmt {
    Expr expr;

    /**
     * @param expr expression to return
     */
    public ReturnStmt(Expr expr) {
        this.expr = expr;
    }

    /**
     * @return expression to return
     */
    public Expr getExpr() {
        return expr;
    }
}
