package org.example.expr;

/**
 * Integer comparison expression.
 */
public class IntCmp extends Expr {
    Expr left;
    Expr right;
    String op;

    /**
     * Construct an integer comparison expression.
     * @param left left operand
     * @param right right operand
     * @param op comparison operator (only "==" supported)
     */
    public IntCmp(Expr left, Expr right, String op) {
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
     * Get the comparison operator.
     * @return comparison operator
     */
    public String getOp() {
        return op;
    }
}
