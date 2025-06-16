package nl.jessenagel.orchestrate;

import org.junit.jupiter.api.Test;

import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Test class for the Orchestrate library.
 * This class contains unit tests for creating, exporting, and solving linear programming models.
 */
class OrchestrateTest {

    /**
     * Test for exporting a linear programming model to a file.
     * This test creates variables, constraints, and an objective function,
     * and then exports the model to a file named "test.lp".
     */
    @Test
    void exportModel() {
        Orchestrate orchestrate = new Orchestrate();
        // Declare variables x1, x2, x3, x4
        NumVar x1 = orchestrate.numVar(0, 40, "x1");
        NumVar x2 = orchestrate.numVar("x2");
        NumVar x3 = orchestrate.numVar("x3");
        IntVar x4 = orchestrate.intVar(2, 3, "x4");


        // Declare constraints
        NumExpr lhs1 = orchestrate.constant(0);
        lhs1 = orchestrate.sum(lhs1, orchestrate.prod(-1, x1));
        lhs1 = orchestrate.sum(lhs1, x2);
        lhs1 = orchestrate.sum(lhs1, x3);
        lhs1 = orchestrate.sum(lhs1, orchestrate.prod(10, x4));
        orchestrate.addLe(lhs1, orchestrate.constant(20)).setName("c1");

        NumExpr lhs2 = orchestrate.constant(0);
        lhs2 = orchestrate.sum(lhs2, x1);
        lhs2 = orchestrate.sum(lhs2, orchestrate.prod(-3, x2));
        lhs2 = orchestrate.sum(lhs2, x3);
        orchestrate.addLe(lhs2, orchestrate.constant(30));

        NumExpr lsh3 = orchestrate.constant(0);
        lsh3 = orchestrate.sum(lsh3, x2);
        lsh3 = orchestrate.sum(lsh3, orchestrate.prod(-3.5, x4));
        orchestrate.addLe(lsh3, orchestrate.constant(30));

        // Add objective function
        NumExpr objective = orchestrate.constant(0);
        objective = orchestrate.sum(objective, x1);
        objective = orchestrate.sum(objective, orchestrate.prod(2, x2));
        objective = orchestrate.sum(objective, orchestrate.prod(3, x3));
        objective = orchestrate.sum(objective, x4);
        orchestrate.addMaximize(objective);
        // Export the model to a file
        orchestrate.exportModel("test.lp");
    }

    /**
     * Test for solving a linear programming model.
     * This test creates variables, constraints, and an objective function,
     * solves the model, and verifies the solution values and objective value.
     */
    @Test
    void solveLP() {
        Orchestrate orchestrate = new Orchestrate();
        // Declare variables x and y
        NumVar x = orchestrate.numVar("x");
        NumVar y = orchestrate.numVar("y");

        // Declare constraints
        NumExpr lhs1 = orchestrate.constant(0);
        lhs1 = orchestrate.sum(lhs1, x);
        lhs1 = orchestrate.sum(lhs1, orchestrate.prod(2, y));
        orchestrate.addLe(lhs1, orchestrate.constant(14)).setName("c1");

        NumExpr lhs2 = orchestrate.constant(0);
        lhs2 = orchestrate.sum(lhs2, orchestrate.prod(3, x));
        lhs2 = orchestrate.sum(lhs2, orchestrate.prod(-1, y));
        orchestrate.addGe(lhs2, orchestrate.constant(0));

        NumExpr lsh3 = orchestrate.constant(0);
        lsh3 = orchestrate.sum(lsh3, x);
        lsh3 = orchestrate.sum(lsh3, orchestrate.prod(-1, y));
        orchestrate.addLe(lsh3, orchestrate.constant(2));

        // Add objective function
        NumExpr objective = orchestrate.constant(0);
        objective = orchestrate.sum(objective, orchestrate.prod(3, x));
        objective = orchestrate.sum(objective, orchestrate.prod(4, y));
        orchestrate.addMaximize(objective);
        // Export the model to a file
        orchestrate.exportModel("test.lp");

        // Solve the model
        orchestrate.solve();
        assertEquals(Orchestrate.Status.Optimal, orchestrate.getStatus());
        // Check the solution
        assertEquals(6.0, orchestrate.getValue(x), 0.01);
        assertEquals(4.0, orchestrate.getValue(y), 0.01);
        assertEquals(34.0, orchestrate.getObjValue(), 0.01);
    }


    @Test
    void solveLPWithNegativeFirstArgument(){
        Orchestrate orchestrate = new Orchestrate();
        // Declare variables x and y
        NumVar x = orchestrate.numVar("x");
        NumVar y = orchestrate.numVar("y");

        // Declare constraints
        NumExpr lhs1 = orchestrate.constant(0);
        lhs1 = orchestrate.sum(lhs1, x);
        lhs1 = orchestrate.sum(lhs1, orchestrate.prod(2, y));
        orchestrate.addLe(lhs1, orchestrate.constant(14)).setName("c1");

        NumExpr lhs2 = orchestrate.constant(0);
        lhs2 = orchestrate.sum(lhs2, orchestrate.prod(3, x));
        lhs2 = orchestrate.sum(lhs2, orchestrate.prod(-1, y));
        orchestrate.addGe(lhs2, orchestrate.constant(0));

        NumExpr lsh3 = orchestrate.constant(0);
        lsh3 = orchestrate.sum(lsh3, x);
        lsh3 = orchestrate.sum(lsh3, orchestrate.prod(-1, y));
        orchestrate.addLe(lsh3, orchestrate.constant(2));

        // Add objective function
        NumExpr objective = orchestrate.constant(0);
        objective = orchestrate.sum(objective, orchestrate.prod(-3, x));
        objective = orchestrate.sum(objective, orchestrate.prod(-4, y));
        orchestrate.addMinimize(objective);
        // Export the model to a file
        orchestrate.exportModel("test.lp");

        // Solve the model
        orchestrate.solve();
        assertEquals(Orchestrate.Status.Optimal, orchestrate.getStatus());
        // Check the solution
        assertEquals(6.0, orchestrate.getValue(x), 0.01);
        assertEquals(4.0, orchestrate.getValue(y), 0.01);
        assertEquals(-34.0, orchestrate.getObjValue(), 0.01);
    }
    /**
     * Test for solving an integer programming model.
     * This test creates integer variables, constraints, and an objective function,
     * solves the model, and verifies the solution values and objective value.
     */
    @Test
    void solveIP() {
        Orchestrate orchestrate = new Orchestrate();
        // Declare integer variables x and yf
        IntVar x = orchestrate.intVar("x");
        IntVar y = orchestrate.boolVar("y");

        // Declare constraints
        NumExpr lhs1 = orchestrate.constant(0);
        lhs1 = orchestrate.sum(lhs1, x);
        lhs1 = orchestrate.sum(lhs1, orchestrate.prod(2, y));
        orchestrate.addLe(lhs1, orchestrate.constant(14)).setName("c1");

        NumExpr lhs2 = orchestrate.constant(0);
        lhs2 = orchestrate.sum(lhs2, orchestrate.prod(3, x));
        lhs2 = orchestrate.sum(lhs2, orchestrate.prod(-1, y));
        orchestrate.addGe(lhs2, orchestrate.constant(0));

        NumExpr lsh3 = orchestrate.constant(0);
        lsh3 = orchestrate.sum(lsh3, x);
        lsh3 = orchestrate.sum(lsh3, orchestrate.prod(-1, y));
        Constraint constraint = orchestrate.addLe(lsh3, orchestrate.constant(2));

        orchestrate.rebalanceConstraint(constraint);

        // Add objective function
        NumExpr objective = orchestrate.constant(0);
        objective = orchestrate.sum(objective, x);
        objective = orchestrate.sum(objective, y);
        objective = orchestrate.sum(objective, 5);
        orchestrate.addMaximize(objective);

        // Export the model to a file
        orchestrate.exportModel("test.lp");

        // Solve the model
        orchestrate.solve();
        // Get the solution
        assertEquals(3.0, orchestrate.getValue(x), 0.01);
        assertEquals(1.0, orchestrate.getValue(y), 0.01);
    }

    /**
     * Test for solving a mixed-integer programming model with a constant in the objective function.
     * This test creates integer and continuous variables, constraints, and an objective function,
     * solves the model, and verifies the solution values and objective value.
     */
    @Test
    void solveMIPWithConstant() {
        Orchestrate orchestrate = new Orchestrate();
        // Declare integer and continuous variables
        IntVar x = orchestrate.intVar("x");
        NumVar y = orchestrate.numVar("y");

        // Declare constraints
        NumExpr lhs1 = orchestrate.constant(0);
        lhs1 = orchestrate.sum(lhs1, x);
        lhs1 = orchestrate.sum(lhs1, orchestrate.prod(2, y));
        orchestrate.addLe(lhs1, orchestrate.constant(14)).setName("c1");

        NumExpr lhs2 = orchestrate.constant(0);
        lhs2 = orchestrate.sum(lhs2, orchestrate.prod(3, x));
        lhs2 = orchestrate.sum(lhs2, orchestrate.prod(-1, y));
        orchestrate.addGe(lhs2, orchestrate.constant(0));

        NumExpr lsh3 = orchestrate.constant(0);
        lsh3 = orchestrate.sum(lsh3, x);
        lsh3 = orchestrate.sum(lsh3, orchestrate.prod(-1, y));
        orchestrate.addLe(lsh3, orchestrate.constant(2));

        // Add objective function with a constant
        NumExpr objective = orchestrate.constant(0);
        objective = orchestrate.sum(objective, x);
        objective = orchestrate.sum(objective, y);
        objective = orchestrate.sum(objective, 5);
        orchestrate.addMaximize(objective);

        // Solve the model
        orchestrate.solve();
        // Get the solution
        assertEquals(6.0, orchestrate.getValue(x), 0.01);
        assertEquals(4.0, orchestrate.getValue(y), 0.01);
    }

    @Test
    void solveLargeLP(){
        Logger logger = LoggerFactory.getLogger(OrchestrateTest.class.getName());

        int numVars = 1000;
        int numConstraints = 1000;
        boolean integer = false;
        long seed = 1;
        Orchestrate orchestrate = new Orchestrate();
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
            vars[i] = orchestrate.numVar( 0, 100,"var_" + i);
        }
        // Step 2: Add constraints A * x <= b, with known feasible point
        for (int i = 0; i < numConstraints; i++) {
            if(i %10 == 0) {
                logger.info("Adding constraint " + i + " of " + numConstraints);
            }
            NumExpr lhs = orchestrate.constant(0);
            double dotProduct = 0.0;

            for (int j = 0; j < numVars; j++) {
                double coeff = integer ? rand.nextInt(11) - 5 : -5 + 10 * rand.nextDouble(); // [-5, 5)
                NumExpr term = orchestrate.prod(coeff, vars[j]);
                lhs = orchestrate.sum(lhs, term);
                dotProduct += coeff * xFeasible[j];
            }

            double delta = 1 + 9 * rand.nextDouble(); // Ensures feasibility
            double rhs = dotProduct + delta;
            orchestrate.addLe(lhs, orchestrate.constant(rhs));
        }

        // Step 3: Define objective function
        NumExpr obj = null;
        for (int i = 0; i < numVars; i++) {
            double coeff = integer ? rand.nextInt(21) - 10 : -10 + 20 * rand.nextDouble(); // [-10, 10)
            NumExpr term = orchestrate.prod(coeff, vars[i]);
            obj = (obj == null) ? term : orchestrate.sum(obj, term);
        }

        orchestrate.addMinimize(obj); // This will depend on the solver’s API (min/max etc.)
        orchestrate.solve();
        assertEquals(Orchestrate.Status.Optimal, orchestrate.getStatus());
    }

    @Test
    void solveLargeLPUsingFile(){
        Logger logger = LoggerFactory.getLogger(OrchestrateTest.class.getName());

        int numVars = 1000;
        int numConstraints = 1000;
        boolean integer = false;
        long seed = 1;
        Orchestrate orchestrate = new Orchestrate();
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
            vars[i] = orchestrate.numVar( 0, 100,"var_" + i);
        }
        // Step 2: Add constraints A * x <= b, with known feasible point
        for (int i = 0; i < numConstraints; i++) {
            if(i %10 == 0) {
                logger.info("Adding constraint " + i + " of " + numConstraints);
            }
            NumExpr lhs = orchestrate.constant(0);
            double dotProduct = 0.0;

            for (int j = 0; j < numVars; j++) {
                double coeff = integer ? rand.nextInt(11) - 5 : -5 + 10 * rand.nextDouble(); // [-5, 5)
                NumExpr term = orchestrate.prod(coeff, vars[j]);
                lhs = orchestrate.sum(lhs, term);
                dotProduct += coeff * xFeasible[j];
            }

            double delta = 1 + 9 * rand.nextDouble(); // Ensures feasibility
            double rhs = dotProduct + delta;
            orchestrate.addLe(lhs, orchestrate.constant(rhs));
        }

        // Step 3: Define objective function
        NumExpr obj = null;
        for (int i = 0; i < numVars; i++) {
            double coeff = integer ? rand.nextInt(21) - 10 : -10 + 20 * rand.nextDouble(); // [-10, 10)
            NumExpr term = orchestrate.prod(coeff, vars[i]);
            obj = (obj == null) ? term : orchestrate.sum(obj, term);
        }

        orchestrate.addMinimize(obj); // This will depend on the solver’s API (min/max etc.)
        orchestrate.solveByExportingFile();
        assertEquals(Orchestrate.Status.Optimal, orchestrate.getStatus());
    }
    @Test
    void addEq() {
        Orchestrate orchestrate = new Orchestrate();
        // Declare variables x and y
        NumVar x = orchestrate.numVar("x");
        NumVar y = orchestrate.numVar("y");
        // Declare numerical expression
        NumExpr lhs = orchestrate.constant(0);
        lhs = orchestrate.sum(lhs, x);
        lhs = orchestrate.sum(lhs, y);
        // Add equality constraint
        Constraint constraint = orchestrate.addEq(lhs, 10);
        OrchConstraint hConstraint = new OrchConstraint(constraint);
        // Check the constraint type
        OrchNumExpr lhsExpr = new OrchNumExpr(hConstraint.lhs);
        OrchNumExpr rhsExpr = new OrchNumExpr(hConstraint.rhs);

        assertEquals(0, lhsExpr.constant);
        assertEquals(10, rhsExpr.constant);
        assertEquals(2, lhsExpr.numberOfVariables);
        assertEquals(0, rhsExpr.numberOfVariables);
        assertEquals(ConstraintType.Eq, hConstraint.type);
    }

    @Test
    void testAddEq() {
    }

    @Test
    void addLe() {
        Orchestrate orchestrate = new Orchestrate();
        // Declare variables x and y
        NumVar x = orchestrate.numVar("x");
        NumVar y = orchestrate.numVar("y");
        // Declare numerical expression
        NumExpr lhs = orchestrate.constant(0);
        lhs = orchestrate.sum(lhs, x);
        lhs = orchestrate.sum(lhs, y);
        // Add equality constraint
        Constraint constraint = orchestrate.addLe(lhs, orchestrate.constant(10));
        OrchConstraint hConstraint = new OrchConstraint(constraint);
        // Check the constraint type
        OrchNumExpr lhsExpr = new OrchNumExpr(hConstraint.lhs);
        OrchNumExpr rhsExpr = new OrchNumExpr(hConstraint.rhs);

        assertEquals(0, lhsExpr.constant);
        assertEquals(10, rhsExpr.constant);
        assertEquals(2, lhsExpr.numberOfVariables);
        assertEquals(0, rhsExpr.numberOfVariables);
        assertEquals(ConstraintType.Le, hConstraint.type);
    }

    @Test
    void addGe() {
        Orchestrate orchestrate = new Orchestrate();
        // Declare variables x and y
        NumVar x = orchestrate.numVar("x");
        NumVar y = orchestrate.numVar("y");
        // Declare numerical expression
        NumExpr lhs = orchestrate.constant(0);
        lhs = orchestrate.sum(lhs, x);
        lhs = orchestrate.sum(lhs, y);
        // Add equality constraint
        Constraint constraint = orchestrate.addGe(lhs, orchestrate.constant(10));
        OrchConstraint hConstraint = new OrchConstraint(constraint);
        // Check the constraint type
        OrchNumExpr lhsExpr = new OrchNumExpr(hConstraint.lhs);
        OrchNumExpr rhsExpr = new OrchNumExpr(hConstraint.rhs);

        assertEquals(0, lhsExpr.constant);
        assertEquals(10, rhsExpr.constant);
        assertEquals(2, lhsExpr.numberOfVariables);
        assertEquals(0, rhsExpr.numberOfVariables);
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
        Orchestrate orchestrate = new Orchestrate();
        // Declare variables x and y
        NumVar x = orchestrate.numVar("x");
        NumVar y = orchestrate.numVar("y");
        // Declare numerical expression
        NumExpr lhs1 = orchestrate.constant(0);
        lhs1 = orchestrate.sum(lhs1, x);
        lhs1 = orchestrate.sum(1, orchestrate.prod(-1, lhs1));
        orchestrate.addEq(lhs1, 1);
        NumExpr obj = orchestrate.constant(0);
        obj = orchestrate.sum(obj,x);
        obj = orchestrate.sum(obj,y);
        obj = orchestrate.sum(obj, 5);
        orchestrate.addMinimize(obj);
        orchestrate.exportModel("test.lp");

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


}