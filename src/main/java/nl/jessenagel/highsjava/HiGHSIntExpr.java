package nl.jessenagel.highsjava;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * Represents an integer expression in the HiGHS model.
 * An integer expression consists of a name, a list of integer variables,
 * a list of integer coefficients, and a constant term.
 */
public class HiGHSIntExpr implements IntExpr {
    /**
     * The name of the integer expression.
     */
    String name;

    /**
     * The list of integer variables in the expression.
     */
    LinkedHashMap<IntVar, Integer> variablesAndCoefficients;

    /**
     * The constant term in the integer expression.
     */
    Integer constant;

    /**
     * Gets the name of the integer expression.
     *
     * @return The name of the expression.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the integer expression.
     *
     * @param name The new name of the expression.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Constructs a new HiGHSIntExpr with no variables, coefficients, or constant.
     * The name is generated automatically.
     */
    HiGHSIntExpr() {
        this.name = "IntExpr_" + HiGHSCounter.getNextVarCounter();
        this.variablesAndCoefficients = new LinkedHashMap<>();
        this.constant = 0;
    }

    /**
     * Constructs a new HiGHSIntExpr by copying an existing integer expression.
     *
     * @param expr The integer expression to copy.
     * @throws HiGHSException If the expression type is invalid.
     */
    HiGHSIntExpr(IntExpr expr) {
        this.name = expr.getName() + "_" + HiGHSCounter.getNextVarCounter();
        expr.accept(new HiGHSIntExprVisitor(this));
//        if (expr instanceof HiGHSIntExpr expr_cast) {
//            this.coefficients = new ArrayList<>(expr_cast.coefficients);
//            this.variables = new ArrayList<>(expr_cast.variables);
//            this.constant = expr_cast.constant;
//        }
//        if (expr instanceof HiGHSNumExpr expr_cast) {
//            throw new HiGHSException("Invalid expression type, converting NumExpr to IntExpr is not defined: " + expr.getClass() + ", " + expr_cast.getClass());
//        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(" ");
        for(Entry<IntVar, Integer> entry : variablesAndCoefficients.entrySet()) {
            if (entry.getValue() < 0) {
                result.append("- ");
            } else {
                result.append("+ ");
            }
            result.append(Math.abs(entry.getValue())).append(" ").append(entry.getKey().getName()).append(" ");
        }
        result.append("+ ").append(constant);
        return result.toString();
    }


    @Override
    public void accept(IntExprVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void accept(NumExprVisitor visitor) {
        visitor.visit(this);
    }
}