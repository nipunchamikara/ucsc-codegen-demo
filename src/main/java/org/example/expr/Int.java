package org.example.expr;

/**
 * Integer expression.
 */
public class Int extends Expr {
    int value;

    /**
     * Construct an integer expression.
     * @param value integer value
     */
    public Int(int value) {
        this.value = value;
    }

    /**
     * Get the integer value.
     * @return integer value
     */
    public int getValue() {
        return value;
    }
}
