package org.example.expr;

/**
 * Variable expression.
 */
public class Var extends Expr {
    String name;

    /**
     * Construct a variable expression.
     * @param name variable name
     */
    public Var (String name) {
        this.name = name;
    }

    /**
     * Get the variable name.
     * @return variable name
     */
    public String getName() {
        return name;
    }
}
