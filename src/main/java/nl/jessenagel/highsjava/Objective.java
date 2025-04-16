package nl.jessenagel.highsjava;

/**
 * Represents an objective in the HiGHS Java library.
 * An objective is a specialized fragment that consists of a constant term
 * and a numerical expression.
 */
public interface Objective extends Fragment {

    /**
     * Gets the constant term of the objective.
     *
     * @return The constant term as a double.
     */
    double getConstant();

    /**
     * Gets the numerical expression of the objective.
     *
     * @return The numerical expression as a {@link NumExpr}.
     */
    NumExpr getExpr();
}