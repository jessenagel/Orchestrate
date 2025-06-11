package nl.jessenagel.orchestrate;

/**
 * Represents a numerical expression in the Orchestrate Java library.
 * This interface extends the {@link Fragment} interface, and is the
 * basis of all Numerical components of the model.
 */
public interface NumExpr extends Fragment {

    /**
     * Accepts a visitor to perform operations on this numerical expression.
     *
     * @param visitor The visitor to accept.
     */
    void accept(NumExprVisitor visitor);

}