package org.example.expr;

/**
 * Function call expression.
 */
public class FnCall extends Expr {
    String name;
    Expr[] args;

    /**
     * Construct a function call expression.
     * @param name function name
     * @param args function arguments
     */
    public FnCall(String name, Expr[] args) {
        this.name = name;
        this.args = args;
    }


    public String getName() {
        return name;
    }
    public Expr[] getArgs() {
        return args;
    }
    public int argLength() {
        return args.length;
    }
}
