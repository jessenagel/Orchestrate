package nl.jessenagel.highsjava;

import org.junit.jupiter.api.Test;

import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the HiGHS library.
 * This class contains unit tests for creating, exporting, and solving linear programming models.
 */
class HiGHSTest {

    /**
     * Test for exporting a linear programming model to a file.
     * This test creates variables, constraints, and an objective function,
     * and then exports the model to a file named "test.lp".
     */
    @Test
    void exportModel() {
        HiGHS highs = new HiGHS();
        // Declare variables x1, x2, x3, x4
        NumVar x1 = highs.numVar(0, 40, "x1");
        NumVar x2 = highs.numVar("x2");
        NumVar x3 = highs.numVar("x3");
        IntVar x4 = highs.intVar(2, 3, "x4");


        // Declare constraints
        NumExpr lhs1 = highs.constant(0);
        lhs1 = highs.sum(lhs1, highs.prod(-1, x1));
        lhs1 = highs.sum(lhs1, x2);
        lhs1 = highs.sum(lhs1, x3);
        lhs1 = highs.sum(lhs1, highs.prod(10, x4));
        highs.addLe(lhs1, highs.constant(20)).setName("c1");

        NumExpr lhs2 = highs.constant(0);
        lhs2 = highs.sum(lhs2, x1);
        lhs2 = highs.sum(lhs2, highs.prod(-3, x2));
        lhs2 = highs.sum(lhs2, x3);
        highs.addLe(lhs2, highs.constant(30));

        NumExpr lsh3 = highs.constant(0);
        lsh3 = highs.sum(lsh3, x2);
        lsh3 = highs.sum(lsh3, highs.prod(-3.5, x4));
        highs.addLe(lsh3, highs.constant(30));

        // Add objective function
        NumExpr objective = highs.constant(0);
        objective = highs.sum(objective, x1);
        objective = highs.sum(objective, highs.prod(2, x2));
        objective = highs.sum(objective, highs.prod(3, x3));
        objective = highs.sum(objective, x4);
        highs.addMaximize(objective);
        // Export the model to a file
        highs.exportModel("test.lp");
    }

    /**
     * Test for solving a linear programming model.
     * This test creates variables, constraints, and an objective function,
     * solves the model, and verifies the solution values and objective value.
     */
    @Test
    void solveLP() {
        HiGHS highs = new HiGHS();
        // Declare variables x and y
        NumVar x = highs.numVar("x");
        NumVar y = highs.numVar("y");

        // Declare constraints
        NumExpr lhs1 = highs.constant(0);
        lhs1 = highs.sum(lhs1, x);
        lhs1 = highs.sum(lhs1, highs.prod(2, y));
        highs.addLe(lhs1, highs.constant(14)).setName("c1");

        NumExpr lhs2 = highs.constant(0);
        lhs2 = highs.sum(lhs2, highs.prod(3, x));
        lhs2 = highs.sum(lhs2, highs.prod(-1, y));
        highs.addGe(lhs2, highs.constant(0));

        NumExpr lsh3 = highs.constant(0);
        lsh3 = highs.sum(lsh3, x);
        lsh3 = highs.sum(lsh3, highs.prod(-1, y));
        highs.addLe(lsh3, highs.constant(2));

        // Add objective function
        NumExpr objective = highs.constant(0);
        objective = highs.sum(objective, highs.prod(3, x));
        objective = highs.sum(objective, highs.prod(4, y));
        highs.addMaximize(objective);
        // Export the model to a file
        highs.exportModel("test.lp");

        // Solve the model
        highs.solve();
        assertEquals(HiGHS.Status.Optimal, highs.getStatus());
        // Check the solution
        assertEquals(6.0, highs.getValue(x), 0.01);
        assertEquals(4.0, highs.getValue(y), 0.01);
        assertEquals(34.0, highs.getObjValue(), 0.01);
    }


    @Test
    void solveLPWithNegativeFirstArgument(){
        HiGHS highs = new HiGHS();
        // Declare variables x and y
        NumVar x = highs.numVar("x");
        NumVar y = highs.numVar("y");

        // Declare constraints
        NumExpr lhs1 = highs.constant(0);
        lhs1 = highs.sum(lhs1, x);
        lhs1 = highs.sum(lhs1, highs.prod(2, y));
        highs.addLe(lhs1, highs.constant(14)).setName("c1");

        NumExpr lhs2 = highs.constant(0);
        lhs2 = highs.sum(lhs2, highs.prod(3, x));
        lhs2 = highs.sum(lhs2, highs.prod(-1, y));
        highs.addGe(lhs2, highs.constant(0));

        NumExpr lsh3 = highs.constant(0);
        lsh3 = highs.sum(lsh3, x);
        lsh3 = highs.sum(lsh3, highs.prod(-1, y));
        highs.addLe(lsh3, highs.constant(2));

        // Add objective function
        NumExpr objective = highs.constant(0);
        objective = highs.sum(objective, highs.prod(-3, x));
        objective = highs.sum(objective, highs.prod(-4, y));
        highs.addMinimize(objective);
        // Export the model to a file
        highs.exportModel("test.lp");

        // Solve the model
        highs.solve();
        assertEquals(HiGHS.Status.Optimal, highs.getStatus());
        // Check the solution
        assertEquals(6.0, highs.getValue(x), 0.01);
        assertEquals(4.0, highs.getValue(y), 0.01);
        assertEquals(-34.0, highs.getObjValue(), 0.01);
    }
    /**
     * Test for solving an integer programming model.
     * This test creates integer variables, constraints, and an objective function,
     * solves the model, and verifies the solution values and objective value.
     */
    @Test
    void solveIP() {
        HiGHS highs = new HiGHS();
        // Declare integer variables x and yf
        IntVar x = highs.intVar("x");
        IntVar y = highs.boolVar("y");

        // Declare constraints
        NumExpr lhs1 = highs.constant(0);
        lhs1 = highs.sum(lhs1, x);
        lhs1 = highs.sum(lhs1, highs.prod(2, y));
        highs.addLe(lhs1, highs.constant(14)).setName("c1");

        NumExpr lhs2 = highs.constant(0);
        lhs2 = highs.sum(lhs2, highs.prod(3, x));
        lhs2 = highs.sum(lhs2, highs.prod(-1, y));
        highs.addGe(lhs2, highs.constant(0));

        NumExpr lsh3 = highs.constant(0);
        lsh3 = highs.sum(lsh3, x);
        lsh3 = highs.sum(lsh3, highs.prod(-1, y));
        Constraint constraint = highs.addLe(lsh3, highs.constant(2));

        highs.rebalanceConstraint(constraint);

        // Add objective function
        NumExpr objective = highs.constant(0);
        objective = highs.sum(objective, x);
        objective = highs.sum(objective, y);
        objective = highs.sum(objective, 5);
        highs.addMaximize(objective);

        // Export the model to a file
        highs.exportModel("test.lp");

        // Solve the model
        highs.solve();
        // Get the solution
        assertEquals(3.0, highs.getValue(x), 0.01);
        assertEquals(1.0, highs.getValue(y), 0.01);
    }

    /**
     * Test for solving a mixed-integer programming model with a constant in the objective function.
     * This test creates integer and continuous variables, constraints, and an objective function,
     * solves the model, and verifies the solution values and objective value.
     */
    @Test
    void solveMIPWithConstant() {
        HiGHS highs = new HiGHS();
        // Declare integer and continuous variables
        IntVar x = highs.intVar("x");
        NumVar y = highs.numVar("y");

        // Declare constraints
        NumExpr lhs1 = highs.constant(0);
        lhs1 = highs.sum(lhs1, x);
        lhs1 = highs.sum(lhs1, highs.prod(2, y));
        highs.addLe(lhs1, highs.constant(14)).setName("c1");

        NumExpr lhs2 = highs.constant(0);
        lhs2 = highs.sum(lhs2, highs.prod(3, x));
        lhs2 = highs.sum(lhs2, highs.prod(-1, y));
        highs.addGe(lhs2, highs.constant(0));

        NumExpr lsh3 = highs.constant(0);
        lsh3 = highs.sum(lsh3, x);
        lsh3 = highs.sum(lsh3, highs.prod(-1, y));
        highs.addLe(lsh3, highs.constant(2));

        // Add objective function with a constant
        NumExpr objective = highs.constant(0);
        objective = highs.sum(objective, x);
        objective = highs.sum(objective, y);
        objective = highs.sum(objective, 5);
        highs.addMaximize(objective);

        // Solve the model
        highs.solve();
        // Get the solution
        assertEquals(6.0, highs.getValue(x), 0.01);
        assertEquals(4.0, highs.getValue(y), 0.01);
    }

    @Test
    void solveLargeLP(){
        Logger logger = LoggerFactory.getLogger(HiGHSTest.class.getName());

        int numVars = 1000;
        int numConstraints = 1000;
        boolean integer = false;
        long seed = 1;
        HiGHS highs = new HiGHS();
        // 1. Generate known feasible point
        Random rand = new Random(seed);

        // Step 1: Create variables
        NumExpr[] vars = new NumExpr[numVars];
        double[] xFeasible = new double[numVars];

        for (int i = 0; i < numVars; i++) {
            if(i %100 == 0) {
                logger.info("Creating variable " + i + " of " + numVars);
            }
            xFeasible[i] = integer ? rand.nextInt(9) + 1 : 1 + 9 * rand.nextDouble(); // [1,10)
            vars[i] = highs.numVar( 0, 100,"var_" + i);
        }
        // Step 2: Add constraints A * x <= b, with known feasible point
        for (int i = 0; i < numConstraints; i++) {
            if(i %10 == 0) {
                logger.info("Adding constraint " + i + " of " + numConstraints);
            }
            NumExpr lhs = highs.constant(0);
            double dotProduct = 0.0;

            for (int j = 0; j < numVars; j++) {
                double coeff = integer ? rand.nextInt(11) - 5 : -5 + 10 * rand.nextDouble(); // [-5, 5)
                NumExpr term = highs.prod(coeff, vars[j]);
                lhs = (lhs == null) ? term : highs.sum(lhs, term);
                dotProduct += coeff * xFeasible[j];
            }

            double delta = 1 + 9 * rand.nextDouble(); // Ensures feasibility
            double rhs = dotProduct + delta;
            highs.addLe(lhs, highs.constant(rhs));
        }

        // Step 3: Define objective function
        NumExpr obj = null;
        for (int i = 0; i < numVars; i++) {
            double coeff = integer ? rand.nextInt(21) - 10 : -10 + 20 * rand.nextDouble(); // [-10, 10)
            NumExpr term = highs.prod(coeff, vars[i]);
            obj = (obj == null) ? term : highs.sum(obj, term);
        }

        highs.addMinimize(obj); // This will depend on the solver’s API (min/max etc.)
        highs.solve();
        assertEquals(HiGHS.Status.Optimal, highs.getStatus());
    }

    @Test
    void addEq() {
        HiGHS highs = new HiGHS();
        // Declare variables x and y
        NumVar x = highs.numVar("x");
        NumVar y = highs.numVar("y");
        // Declare numerical expression
        NumExpr lhs = highs.constant(0);
        lhs = highs.sum(lhs, x);
        lhs = highs.sum(lhs, y);
        // Add equality constraint
        Constraint constraint = highs.addEq(lhs, 10);
        HiGHSConstraint hConstraint = new HiGHSConstraint(constraint);
        // Check the constraint type
        HiGHSNumExpr lhsExpr = new HiGHSNumExpr(hConstraint.lhs);
        HiGHSNumExpr rhsExpr = new HiGHSNumExpr(hConstraint.rhs);

        assertEquals(0, lhsExpr.constant);
        assertEquals(10, rhsExpr.constant);
        assertEquals(2, lhsExpr.variablesAndCoefficients.size());
        assertEquals(0, rhsExpr.variablesAndCoefficients.size());
        assertEquals(ConstraintType.Eq, hConstraint.type);
    }

    @Test
    void testAddEq() {
    }

    @Test
    void addLe() {
        HiGHS highs = new HiGHS();
        // Declare variables x and y
        NumVar x = highs.numVar("x");
        NumVar y = highs.numVar("y");
        // Declare numerical expression
        NumExpr lhs = highs.constant(0);
        lhs = highs.sum(lhs, x);
        lhs = highs.sum(lhs, y);
        // Add equality constraint
        Constraint constraint = highs.addLe(lhs, highs.constant(10));
        HiGHSConstraint hConstraint = new HiGHSConstraint(constraint);
        // Check the constraint type
        HiGHSNumExpr lhsExpr = new HiGHSNumExpr(hConstraint.lhs);
        HiGHSNumExpr rhsExpr = new HiGHSNumExpr(hConstraint.rhs);

        assertEquals(0, lhsExpr.constant);
        assertEquals(10, rhsExpr.constant);
        assertEquals(2, lhsExpr.variablesAndCoefficients.size());
        assertEquals(0, rhsExpr.variablesAndCoefficients.size());
        assertEquals(ConstraintType.Le, hConstraint.type);
    }

    @Test
    void addGe() {
        HiGHS highs = new HiGHS();
        // Declare variables x and y
        NumVar x = highs.numVar("x");
        NumVar y = highs.numVar("y");
        // Declare numerical expression
        NumExpr lhs = highs.constant(0);
        lhs = highs.sum(lhs, x);
        lhs = highs.sum(lhs, y);
        // Add equality constraint
        Constraint constraint = highs.addGe(lhs, highs.constant(10));
        HiGHSConstraint hConstraint = new HiGHSConstraint(constraint);
        // Check the constraint type
        HiGHSNumExpr lhsExpr = new HiGHSNumExpr(hConstraint.lhs);
        HiGHSNumExpr rhsExpr = new HiGHSNumExpr(hConstraint.rhs);

        assertEquals(0, lhsExpr.constant);
        assertEquals(10, rhsExpr.constant);
        assertEquals(2, lhsExpr.variablesAndCoefficients.size());
        assertEquals(0, rhsExpr.variablesAndCoefficients.size());
        assertEquals(ConstraintType.Ge, hConstraint.type);
    }

    @Test
    void sum() {
    }

    @Test
    void testSum() {
    }

    @Test
    void testSum1() {
    }

    @Test
    void testSum2() {
    }

    @Test
    void testSum3() {
    }

    @Test
    void testSum4() {
    }

    @Test
    void getName() {
    }

    @Test
    void setName() {
    }

    @Test
    void add() {
    }

    @Test
    void testAdd() {
    }

    @Test
    void remove() {
    }

    @Test
    void testRemove() {
    }

    @Test
    void importSol() {
    }

    @Test
    void boolVar() {
    }

    @Test
    void testBoolVar() {
    }

    @Test
    void intVar() {
    }

    @Test
    void testIntVar() {
    }

    @Test
    void testIntVar1() {
    }

    @Test
    void testIntVar2() {
    }

    @Test
    void constant() {
    }

    @Test
    void testConstant() {
    }

    @Test
    void prod() {
    }

    @Test
    void testProd() {
        HiGHS highs = new HiGHS();
        // Declare variables x and y
        NumVar x = highs.numVar("x");
        NumVar y = highs.numVar("y");
        // Declare numerical expression
        NumExpr lhs1 = highs.constant(0);
        lhs1 = highs.sum(lhs1, x);
        lhs1 = highs.sum(1, highs.prod(-1, lhs1));
        highs.addEq(lhs1, 1);
        NumExpr obj = highs.constant(0);
        obj = highs.sum(obj,x);
        obj = highs.sum(obj,y);
        obj = highs.sum(obj, 5);
        highs.addMinimize(obj);
        highs.exportModel("test.lp");

    }

    @Test
    void testProd1() {
    }

    @Test
    void testProd2() {
    }

    @Test
    void addMaximize() {
    }

    @Test
    void testAddMaximize() {
    }

    @Test
    void addMinimize() {
    }

    @Test
    void testAddMinimize() {
    }

    @Test
    void addObjective() {
    }

    @Test
    void testAddObjective() {
    }

    @Test
    void getValue() {
    }

    @Test
    void testGetValue() {
    }

    @Test
    void getObjValue() {
    }

    @Test
    void numVar() {
    }

    @Test
    void testNumVar() {
    }

    @Test
    void solve() {
    }

    @Test
    void rebalanceConstraint() {
        HiGHS highs = new HiGHS();
        // Declare variables x and y
        NumVar x = highs.numVar("x");
        NumVar y = highs.numVar("y");
        // Declare numerical expression
        NumExpr lhs = highs.constant(0);
        lhs = highs.sum(lhs, y);
        NumExpr rhs = highs.constant(100);
        rhs = highs.sum(rhs, highs.prod(-100, x));
        Constraint constraint = highs.addLe(lhs, rhs);
        constraint = highs.rebalanceConstraint(constraint);

        HiGHSConstraint hConstraint = new HiGHSConstraint(constraint);
        // Check the constraint type
        HiGHSNumExpr lhsExpr = new HiGHSNumExpr(hConstraint.lhs);
        HiGHSNumExpr rhsExpr = new HiGHSNumExpr(hConstraint.rhs);
        assertEquals(" + 1.0 y + 100.0 x + 0.0", lhsExpr.toString());
        assertEquals(" + 100.0", rhsExpr.toString());
    }

}