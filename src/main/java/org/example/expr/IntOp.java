package org.example.expr;

/**
 * Integer operation expression.
 */
public class IntOp extends Expr {
    Expr left;
    Expr right;
    String op;

    /**
     * Construct an integer operation expression.
     * @param left left operand
     * @param right right operand
     * @param op operator (one of "+", "-", "*", "/")
     */
    public IntOp(Expr left, Expr right, String op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    /**
     * Get the left operand.
     * @return left operand
     */
    public Expr getLeft() {
        return left;
    }

    /**
     * Get the right operand.
     * @return right operand
     */
    public Expr getRight() {
        return right;
    }

    /**
     * Get the operator.
     * @return operator
     */
    public String getOp() {
        return op;
    }
}
