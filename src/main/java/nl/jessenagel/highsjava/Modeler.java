package nl.jessenagel.highsjava;

    /**
     * This interface defines the methods a Modeler class should implement.
     * A Modeler is responsible for creating and managing constraints and expressions
     * in a linear programming model.
     */
    public interface Modeler extends Fragment, Model {

        /**
         * Adds an equality constraint between two numerical expressions.
         *
         * @param lhs The left-hand side numerical expression.
         * @param rhs The right-hand side numerical expression.
         * @return The created equality constraint.
         */
        Constraint addEq(NumExpr lhs, NumExpr rhs);

        /**
         * Adds a less-than-or-equal-to constraint between two numerical expressions.
         *
         * @param lhs The left-hand side numerical expression.
         * @param rhs The right-hand side numerical expression.
         * @return The created less-than-or-equal-to constraint.
         */
        Constraint addLe(NumExpr lhs, NumExpr rhs);

        /**
         * Adds a greater-than-or-equal-to constraint between two numerical expressions.
         *
         * @param lhs The left-hand side numerical expression.
         * @param rhs The right-hand side numerical expression.
         * @return The created greater-than-or-equal-to constraint.
         */
        Constraint addGe(NumExpr lhs, NumExpr rhs);

        /**
         * Sums an integer value and an integer expression.
         *
         * @param v The integer value to add.
         * @param e The integer expression to add.
         * @return The resulting integer expression.
         */
        IntExpr sum(int v, IntExpr e);

        /**
         * Sums an integer expression and an integer value.
         *
         * @param e The integer expression to add.
         * @param v The integer value to add.
         * @return The resulting integer expression.
         */
        IntExpr sum(IntExpr e, int v);

        /**
         * Sums two integer expressions.
         *
         * @param e1 The first integer expression to add.
         * @param e2 The second integer expression to add.
         * @return The resulting integer expression.
         */
        IntExpr sum(IntExpr e1, IntExpr e2);

        /**
         * Sums a double value and a numerical expression.
         *
         * @param v The double value to add.
         * @param e The numerical expression to add.
         * @return The resulting numerical expression.
         */
        NumExpr sum(double v, NumExpr e);

        /**
         * Sums a numerical expression and a double value.
         *
         * @param e The numerical expression to add.
         * @param v The double value to add.
         * @return The resulting numerical expression.
         */
        NumExpr sum(NumExpr e, double v);

        /**
         * Sums two numerical expressions.
         *
         * @param e1 The first numerical expression to add.
         * @param e2 The second numerical expression to add.
         * @return The resulting numerical expression.
         */
        NumExpr sum(NumExpr e1, NumExpr e2);
    }