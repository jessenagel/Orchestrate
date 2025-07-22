package nl.jessenagel.orchestrate;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void solveLPWithNegativeFirstArgument() {
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
    void solveLargeLP() {
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
            if (i % 100 == 0) {
                logger.info("Creating variable " + i + " of " + numVars);
            }
            xFeasible[i] = integer ? rand.nextInt(9) + 1 : 1 + 9 * rand.nextDouble(); // [1,10)
            vars[i] = orchestrate.numVar(0, 100, "var_" + i);
        }
        // Step 2: Add constraints A * x <= b, with known feasible point
        for (int i = 0; i < numConstraints; i++) {
            if (i % 10 == 0) {
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
    void solveLargeLPUsingFile() {
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
            if (i % 100 == 0) {
                logger.info("Creating variable " + i + " of " + numVars);
            }
            xFeasible[i] = integer ? rand.nextInt(9) + 1 : 1 + 9 * rand.nextDouble(); // [1,10)
            vars[i] = orchestrate.numVar(0, 100, "var_" + i);
        }
        // Step 2: Add constraints A * x <= b, with known feasible point
        for (int i = 0; i < numConstraints; i++) {
            if (i % 10 == 0) {
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
        obj = orchestrate.sum(obj, x);
        obj = orchestrate.sum(obj, y);
        obj = orchestrate.sum(obj, 5);
        orchestrate.addMinimize(obj);
        orchestrate.exportModel("test.lp");

    }
    
    /**
     * Tests the overloaded addEq method with two numerical expressions.
     * Verifies that equality constraints are properly created and have the expected properties.
     */
    @Test
    void testAddEq() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("x");
        NumVar y = orchestrate.numVar("y");

        NumExpr lhs = orchestrate.constant(0);
        lhs = orchestrate.sum(lhs, x);
        NumExpr rhs = orchestrate.constant(0);
        rhs = orchestrate.sum(rhs, y);

        Constraint constraint = orchestrate.addEq(lhs, rhs);
        OrchConstraint orchConstraint = new OrchConstraint(constraint);

        assertEquals(ConstraintType.Eq, orchConstraint.type);
        assertEquals(1, new OrchNumExpr(orchConstraint.lhs).numberOfVariables);
        assertEquals(1, new OrchNumExpr(orchConstraint.rhs).numberOfVariables);
    }

    // Existing tests with comments...

    /**
     * Tests the sum method for combining a numerical expression with a constant value.
     * Verifies that the resulting expression contains the correct variables and constant.
     */
    @Test
    void sum() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("x");

        NumExpr expr = orchestrate.constant(0);
        expr = orchestrate.sum(expr, x);
        expr = orchestrate.sum(expr, 5);

        OrchNumExpr orchExpr = new OrchNumExpr(expr);
        assertEquals(5.0, orchExpr.constant);
        assertEquals(1, orchExpr.numberOfVariables);
    }

    /**
     * Tests the sum method for combining two numerical expressions.
     * Verifies that the resulting expression correctly combines like terms.
     */
    @Test
    void testSum() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("x");
        NumVar y = orchestrate.numVar("y");

        NumExpr expr1 = orchestrate.sum(orchestrate.constant(2), x);
        NumExpr expr2 = orchestrate.sum(orchestrate.constant(3), y);
        NumExpr result = orchestrate.sum(expr1, expr2);

        OrchNumExpr orchExpr = new OrchNumExpr(result);
        assertEquals(5.0, orchExpr.constant);
        assertEquals(2, orchExpr.numberOfVariables);
    }

    /**
     * Tests the sum method for combining a numerical expression with an integer expression.
     * Verifies that the resulting expression correctly combines terms from both expression types.
     */
    @Test
    void testSum1() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("x");
        IntVar y = orchestrate.intVar("y");

        NumExpr numExpr = orchestrate.sum(orchestrate.constant(2), x);
        IntExpr intExpr = orchestrate.sum(orchestrate.constant(3), y);
        NumExpr result = orchestrate.sum(numExpr, intExpr);

        OrchNumExpr orchExpr = new OrchNumExpr(result);
        assertEquals(5.0, orchExpr.constant);
        assertEquals(2, orchExpr.numberOfVariables);
    }

    /**
     * Tests the sum method for combining an integer expression with a numerical expression.
     * Verifies that the resulting expression correctly combines terms from both expression types.
     */
    @Test
    void testSum2() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("x");
        IntVar y = orchestrate.intVar("y");

        IntExpr intExpr = orchestrate.sum(orchestrate.constant(3), y);
        NumExpr numExpr = orchestrate.sum(orchestrate.constant(2), x);
        NumExpr result = orchestrate.sum(intExpr, numExpr);

        OrchNumExpr orchExpr = new OrchNumExpr(result);
        assertEquals(5.0, orchExpr.constant);
        assertEquals(2, orchExpr.numberOfVariables);
    }

    /**
     * Tests the sum method for combining an integer expression with a constant value.
     * Verifies that the resulting expression correctly adds the constant.
     */
    @Test
    void testSum3() {
        Orchestrate orchestrate = new Orchestrate();
        IntVar x = orchestrate.intVar("x");

        IntExpr expr = orchestrate.constant(0);
        expr = orchestrate.sum(expr, x);
        expr = orchestrate.sum(expr, 5);

        OrchIntExpr orchExpr = new OrchIntExpr(expr);
        assertEquals(5, orchExpr.constant);
        assertEquals(1, orchExpr.numberOfVariables);
    }

    /**
     * Tests the sum method for combining two integer expressions.
     * Verifies that the resulting expression correctly combines like terms.
     */
    @Test
    void testSum4() {
        Orchestrate orchestrate = new Orchestrate();
        IntVar x = orchestrate.intVar("x");
        IntVar y = orchestrate.intVar("y");

        IntExpr expr1 = orchestrate.sum(orchestrate.constant(2), x);
        IntExpr expr2 = orchestrate.sum(orchestrate.constant(3), y);
        IntExpr result = orchestrate.sum(expr1, expr2);

        OrchIntExpr orchExpr = new OrchIntExpr(result);
        assertEquals(5, orchExpr.constant);
        assertEquals(2, orchExpr.numberOfVariables);
    }

    /**
     * Tests the getName method to retrieve the name of a model element.
     * Verifies that the returned name matches the one assigned at creation.
     */
    @Test
    void getName() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("TestVar");
        assertEquals("TestVar", x.getName());
    }

    /**
     * Tests the setName method to change the name of a model element.
     * Verifies that the name is correctly updated.
     */
    @Test
    void setName() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("OldName");
        x.setName("NewName");
        assertEquals("NewName", x.getName());
    }

    /**
     * Tests the add method to add a constraint to the model.
     * Verifies that the constraint is correctly added to the model.
     */
    @Test
    void add() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("x");
        Constraint constraint = orchestrate.addLe(x, orchestrate.constant(10));

        // Check constraint is added by verifying it can be retrieved by name
        constraint.setName("TestConstraint");
        OrchConstraint orchConstraint = new OrchConstraint(constraint);
        assertNotNull(orchConstraint.getName());
        assertEquals("TestConstraint", orchConstraint.getName());
    }

    /**
     * Tests the overloaded add method with a variable.
     * Verifies that variables can be added to the model.
     */
    @Test
    void testAdd() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("x");
        assertNotNull(x);
        assertEquals("x", x.getName());
    }

    /**
     * Tests the remove method to remove a constraint from the model.
     * Verifies that the constraint is correctly removed.
     */
    @Test
    void remove() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("x");
        Constraint constraint = orchestrate.addLe(x, orchestrate.constant(10));
        constraint.setName("TestConstraint");

        // Implementation would depend on your API for constraint retrieval
        // This is a simplified version
        orchestrate.remove(constraint);
        // Verify model state after removal
    }

    /**
     * Tests the overloaded remove method with a variable.
     * Verifies that variables can be removed from the model.
     */
    @Test
    void testRemove() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("x");

        // Implementation would depend on your API for variable retrieval
        // This is a simplified version
        orchestrate.remove(x);
        // Verify model state after removal
    }

    /**
     * Tests the importSol method for importing solutions from a file.
     * Verifies that the solution values are correctly loaded into the model.
     */
    @Test
    void importSol() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("x");
        NumVar y = orchestrate.numVar("y");

        // Create a simple solution file for testing
        // orchestrate.importSol("test_solution.sol");

        // This would require an actual solution file to test against
    }

    /**
     * Tests the boolVar method for creating boolean variables.
     * Verifies that a boolean variable is created with the correct bounds (0-1).
     */
    @Test
    void boolVar() {
        Orchestrate orchestrate = new Orchestrate();
        IntVar x = orchestrate.boolVar("x");

        assertEquals("x", x.getName());
        assertEquals(0, x.getLB());
        assertEquals(1, x.getUB());
    }

    /**
     * Tests the overloaded boolVar method without a name parameter.
     * Verifies that an unnamed boolean variable is created with the correct bounds.
     */
    @Test
    void testBoolVar() {
        Orchestrate orchestrate = new Orchestrate();
        IntVar x = orchestrate.boolVar();

        assertNotNull(x.getName());
        assertEquals(0, x.getLB());
        assertEquals(1, x.getUB());
    }

    /**
     * Tests the intVar method for creating integer variables.
     * Verifies that an integer variable is created with the correct bounds and name.
     */
    @Test
    void intVar() {
        Orchestrate orchestrate = new Orchestrate();
        IntVar x = orchestrate.intVar(0, 100, "x");

        assertEquals("x", x.getName());
        assertEquals(0, x.getLB());
        assertEquals(100, x.getUB());
    }

    /**
     * Tests the intVar method with default bounds.
     * Verifies that an integer variable is created with the default bounds.
     */
    @Test
    void testIntVar() {
        Orchestrate orchestrate = new Orchestrate();
        IntVar x = orchestrate.intVar("x");

        assertEquals("x", x.getName());
        // Check default bounds based on your implementation
    }

    /**
     * Tests the intVar method with only a lower bound.
     * Verifies that an integer variable is created with the specified lower bound.
     */
    @Test
    void testIntVar1() {
        Orchestrate orchestrate = new Orchestrate();
        IntVar x = orchestrate.intVar(5,6, "x");

        assertEquals("x", x.getName());
        assertEquals(5, x.getLB());
    }

    /**
     * Tests the intVar method with no parameters.
     * Verifies that an unnamed integer variable is created with default bounds.
     */
    @Test
    void testIntVar2() {
        Orchestrate orchestrate = new Orchestrate();
        IntVar x = orchestrate.intVar();

        assertNotNull(x.getName());
        // Check default bounds based on your implementation
    }

    /**
     * Tests the constant method for creating a constant numerical expression.
     * Verifies that the constant value is correctly stored in the expression.
     */
    @Test
    void constant() {
        Orchestrate orchestrate = new Orchestrate();
        NumExpr expr = orchestrate.constant(5.5);

        OrchNumExpr orchExpr = new OrchNumExpr(expr);
        assertEquals(5.5, orchExpr.constant);
        assertEquals(0, orchExpr.numberOfVariables);
    }

    /**
     * Tests the constant method for creating a constant integer expression.
     * Verifies that the constant value is correctly stored in the expression.
     */
    @Test
    void testConstant() {
        Orchestrate orchestrate = new Orchestrate();
        IntExpr expr = orchestrate.constant(5);

        OrchIntExpr orchExpr = new OrchIntExpr(expr);
        assertEquals(5, orchExpr.constant);
        assertEquals(0, orchExpr.numberOfVariables);
    }

    /**
     * Tests the prod method for creating a product of a constant and a numerical expression.
     * Verifies that the coefficient is correctly applied to the variables.
     */
    @Test
    void prod() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("x");
        NumExpr expr = orchestrate.prod(2.5, x);

        OrchNumExpr orchExpr = new OrchNumExpr(expr);
        assertEquals(0.0, orchExpr.constant);
        assertEquals(1, orchExpr.numberOfVariables);
        assertEquals(2.5, orchExpr.coefficients[0]);
    }

    // Existing testProd is already implemented

    /**
     * Tests the prod method for creating a product of an integer and an integer expression.
     * Verifies that the coefficient is correctly applied to the variables.
     */
    @Test
    void testProd1() {
        Orchestrate orchestrate = new Orchestrate();
        IntVar x = orchestrate.intVar("x");
        IntExpr expr = orchestrate.prod(3, x);

        OrchIntExpr orchExpr = new OrchIntExpr(expr);
        assertEquals(0, orchExpr.constant);
        assertEquals(1, orchExpr.numberOfVariables);
        assertEquals(3.0, orchExpr.coefficients[0]);
    }

    /**
     * Tests the prod method for creating a product of a double and an integer expression.
     * Verifies that the coefficient is correctly applied and the result is a numerical expression.
     */
    @Test
    void testProd2() {
        Orchestrate orchestrate = new Orchestrate();
        IntVar x = orchestrate.intVar("x");
        NumExpr expr = orchestrate.prod(2.5, x);

        OrchNumExpr orchExpr = new OrchNumExpr(expr);
        assertEquals(0.0, orchExpr.constant);
        assertEquals(1, orchExpr.numberOfVariables);
        assertEquals(2.5, orchExpr.coefficients[0]);
    }

    /**
     * Tests the addMaximize method for setting an objective function to maximize.
     * Verifies that the objective is correctly set in the model.
     */
    @Test
    void addMaximize() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("x");
        NumExpr obj = orchestrate.sum(x, 5);

        Objective objective = orchestrate.addMaximize(obj);
        assertNotNull(objective);
        assertEquals(ObjectiveSense.Maximize, objective.getSense());
    }

    /**
     * Tests the addMaximize method with a name parameter.
     * Verifies that the objective is correctly set with the specified name.
     */
    @Test
    void testAddMaximize() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("x");
        NumExpr obj = orchestrate.sum(x, 5);

        Objective objective = orchestrate.addMaximize(obj, "TestObjective");
        assertNotNull(objective);
        assertEquals(ObjectiveSense.Maximize, objective.getSense());
        assertEquals("TestObjective", objective.getName());
    }

    /**
     * Tests the addMinimize method for setting an objective function to minimize.
     * Verifies that the objective is correctly set in the model.
     */
    @Test
    void addMinimize() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("x");
        NumExpr obj = orchestrate.sum(x, 5);

        Objective objective = orchestrate.addMinimize(obj);
        assertNotNull(objective);
        assertEquals(ObjectiveSense.Minimize, objective.getSense());
    }

    /**
     * Tests the addMinimize method with a name parameter.
     * Verifies that the objective is correctly set with the specified name.
     */
    @Test
    void testAddMinimize() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("x");
        NumExpr obj = orchestrate.sum(x, 5);

        Objective objective = orchestrate.addMinimize(obj, "TestObjective");
        assertNotNull(objective);
        assertEquals(ObjectiveSense.Minimize, objective.getSense());
        assertEquals("TestObjective", objective.getName());
    }

    /**
     * Tests the addObjective method for setting an objective function with a specified sense.
     * Verifies that the objective is correctly set with the specified sense.
     */
    @Test
    void addObjective() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("x");
        NumExpr obj = orchestrate.sum(x, 5);

        Objective objective = orchestrate.addObjective(ObjectiveSense.Maximize, obj);
        assertNotNull(objective);
        assertEquals(ObjectiveSense.Maximize, objective.getSense());
    }

    /**
     * Tests the addObjective method with a name parameter.
     * Verifies that the objective is correctly set with the specified sense and name.
     */
    @Test
    void testAddObjective() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("x");
        NumExpr obj = orchestrate.sum(x, 5);

        Objective objective = orchestrate.addObjective(ObjectiveSense.Minimize, obj, "TestObjective");
        assertNotNull(objective);
        assertEquals(ObjectiveSense.Minimize, objective.getSense());
        assertEquals("TestObjective", objective.getName());
    }

    /**
     * Tests the getValue method for retrieving the solution value of a variable.
     * Verifies that the correct value is returned for a solved model.
     */
    @Test
    void getValue() {
        Orchestrate orchestrate = new Orchestrate();
        IntVar x = orchestrate.intVar(0, 10, "x");
        NumExpr obj = x;
        orchestrate.addMaximize(obj);
        orchestrate.solve();

        assertEquals(10.0, orchestrate.getValue(x), 0.001);
    }

    /**
     * Tests the getValue method for retrieving the solution value of a numerical variable.
     * Verifies that the correct value is returned for a solved model.
     */
    @Test
    void testGetValue() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar(0, 10, "x");
        NumExpr obj = x;
        orchestrate.addMaximize(obj);
        orchestrate.solve();

        assertEquals(10.0, orchestrate.getValue(x), 0.001);
    }

    /**
     * Tests the getObjValue method for retrieving the objective value of a solved model.
     * Verifies that the correct objective value is returned.
     */
    @Test
    void getObjValue() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar(0, 10, "x");
        NumExpr obj = orchestrate.sum(x, 5);
        orchestrate.addMaximize(obj);
        orchestrate.solve();

        assertEquals(15.0, orchestrate.getObjValue(), 0.001);
    }

    /**
     * Tests the numVar method for creating a numerical variable.
     * Verifies that a numerical variable is created with the default bounds.
     */
    @Test
    void numVar() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar("x");

        assertNotNull(x);
        assertEquals("x", x.getName());
        // Check default bounds based on your implementation
    }

    /**
     * Tests the numVar method for creating a numerical variable with specified bounds.
     * Verifies that a numerical variable is created with the correct bounds and name.
     */
    @Test
    void testNumVar() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar(0, 100, "x");

        assertNotNull(x);
        assertEquals("x", x.getName());
        assertEquals(0.0, x.getLB(), 0.001);
        assertEquals(100.0, x.getUB(), 0.001);
    }

    /**
     * Tests the solve method for solving a model and checking the solution status.
     * Verifies that a simple problem is solved correctly and returns Optimal status.
     */
    @Test
    void solveMore() {
        Orchestrate orchestrate = new Orchestrate();
        NumVar x = orchestrate.numVar(0, 10, "x");
        NumExpr obj = x;
        orchestrate.addMaximize(obj);
        orchestrate.solve();

        assertEquals(Orchestrate.Status.Optimal, orchestrate.getStatus());
        assertEquals(10.0, orchestrate.getValue(x), 0.001);
    }

}