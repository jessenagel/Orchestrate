package nl.jessenagel.orchestrate;
/**
 * This interface represents a linear constraint
 */
public interface Constraint extends Fragment, NumExpr, IntExpr{

    /**
     * Accepts a visitor to perform operations on the constraint.
     *
     * @param visitor The visitor to accept.
     */
    void accept(ConstraintVisitor visitor);
}
