package org.example.func;

import org.example.expr.Var;
import org.example.stmt.Stmt;

/**
 * Function definition
 */
public class Func {
    String name;
    Var[] args;
    Stmt[] stmts;

    /**
     * Create a function
     * @param name  function name
     * @param args  function arguments
     * @param stmts function body
     */
    public Func(String name, Var[] args, Stmt[] stmts) {
        this.name = name;
        this.args = args;
        this.stmts = stmts;
    }

    /**
     * Get function body
     * @return function body
     */
    public Stmt[] getStmts() {
        return stmts;
    }

    /**
     * Get function name
     * @return function name
     */
    public String getName() {
        return name;
    }

    /**
     * Get function arguments
     * @return function arguments
     */
    public Var[] getArgs() {
        return args;
    }

    /**
     * Get number of function arguments
     * @return number of function arguments
     */
    public int argLength() {
        return args.length;
    }
}
