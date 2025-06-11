package nl.jessenagel.orchestrate;

/**
 * Represents a constraint in the Orchestrate model.
 * A constraint consists of a left-hand side (lhs) expression, a right-hand side (rhs) expression,
 * and a constraint type (e.g., equality, less-than-or-equal-to, greater-than-or-equal-to).
 */
public class OrchConstraint implements Constraint {
    /**
     * The name of the constraint.
     */
    String name;

    /**
     * The left-hand side numerical expression of the constraint.
     */
    NumExpr lhs;

    /**
     * The right-hand side numerical expression of the constraint.
     */
    NumExpr rhs;

    /**
     * The type of the constraint (e.g., equality, inequality).
     */
    ConstraintType type;

    /**
     * Constructs a new OrchConstraint with the specified lhs, rhs, and type.
     *
     * @param lhs  The left-hand side numerical expression.
     * @param rhs  The right-hand side numerical expression.
     * @param type The type of the constraint.
     */
    OrchConstraint(NumExpr lhs, NumExpr rhs, ConstraintType type) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.type = type;
        this.name = "Constraint_" + OrchCounter.getNextConstraintCounter();
    }

    /**
     * Constructs a new OrchConstraint with a default name.
     */
    OrchConstraint() {
        this.name = "Constraint_" + OrchCounter.getNextConstraintCounter();
    }

    public OrchConstraint(Constraint constraint) {
        this.name = constraint.getName() + "_" + OrchCounter.getNextConstraintCounter();
        constraint.accept(new OrchConstraintVisitor(this));
    }

    /**
     * Gets the name of the constraint.
     *
     * @return The name of the constraint.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the constraint.
     *
     * @param name The new name of the constraint.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String result = name + ": ";
        result = result + lhs.toString() + " ";
        if (type == ConstraintType.Eq) {
            result = result + "= ";
        } else if (type == ConstraintType.Le) {
            result = result + "<= ";
        } else if (type == ConstraintType.Ge) {
            result = result + ">= ";
        }
        result = result + rhs.toString();
        return result;
    }

    @Override
    public void accept(NumExprVisitor visitor) {
        if (lhs != null) {
            lhs.accept(visitor);
        }
        if (rhs != null) {
            rhs.accept(visitor);
        }
        visitor.visit(this);
    }

    @Override
    public void accept(IntExprVisitor visitor) {
        if (lhs != null) {
            lhs.accept(visitor);
        }
        if (rhs != null) {
            rhs.accept(visitor);
        }
        visitor.visit(this);
    }

    @Override
    public void accept(ConstraintVisitor visitor) {
        visitor.visit(this);
    }
}
