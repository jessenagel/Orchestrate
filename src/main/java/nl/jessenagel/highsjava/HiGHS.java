package nl.jessenagel.highsjava;

import java.io.*;
import java.util.*;

/**
 * HiGHS is the class used to create LP models to be solved in the HiGHS solver
 */
public class HiGHS implements Modeler {

    private final List<Constraint> constraints;
    private final List<NumVar> variables;
    private final Map<NumVar, Double> solutionValues;
    private String name;
    private HiGHSObjective objective;
    private int varCounter = 0;
    private int constraintCounter = 0;
    private double objectiveValue;

    /**
     * Constructer which creates a new HiGHS object
     */
    public HiGHS() {
        this.name = "HiGHS";
        this.constraints = new ArrayList<>();
        this.variables = new ArrayList<>();
        this.solutionValues = new HashMap<>();

    }

    /**
     * Adds an equality constraint to the model.
     *
     * @param lhs The left-hand side numerical expression.
     * @param rhs The right-hand side numerical expression.
     * @return The created equality constraint.
     */
    @Override
    public Constraint addEq(NumExpr lhs, NumExpr rhs) {
        HiGHSConstraint constraint = new HiGHSConstraint(lhs, rhs, ConstraintType.Eq);
        constraints.add(constraint);
        return constraint;
    }

    /**
     * Adds an equality constraint to the model with a constant on the right-hand side.
     *
     * @param lhs The left-hand side numerical expression.
     * @param i   The constant value on the right-hand side.
     * @return The created equality constraint.
     */
    public Constraint addEq(NumExpr lhs, int i) {
        HiGHSConstraint constraint = new HiGHSConstraint(lhs, constant(i), ConstraintType.Eq);
        constraints.add(constraint);
        return constraint;
    }

    /**
     * Adds a less-than-or-equal-to constraint to the model.
     *
     * @param lhs The left-hand side numerical expression.
     * @param rhs The right-hand side numerical expression.
     * @return The created less-than-or-equal-to constraint.
     */
    @Override
    public Constraint addLe(NumExpr lhs, NumExpr rhs) {
        HiGHSConstraint constraint = new HiGHSConstraint(lhs, rhs, ConstraintType.Le);
        constraints.add(constraint);
        return constraint;
    }

    /**
     * Adds a greater-than-or-equal-to constraint to the model.
     *
     * @param lhs The left-hand side numerical expression.
     * @param rhs The right-hand side numerical expression.
     * @return The created greater-than-or-equal-to constraint.
     */
    @Override
    public Constraint addGe(NumExpr lhs, NumExpr rhs) {
        HiGHSConstraint constraint = new HiGHSConstraint(lhs, rhs, ConstraintType.Ge);
        constraints.add(constraint);
        return constraint;
    }

    /**
     * Sums an integer value and an integer expression.
     *
     * @param v The integer value.
     * @param e The integer expression.
     * @return The resulting integer expression.
     */
    @Override
    public IntExpr sum(int v, IntExpr e) {
        HiGHSIntExpr sum = new HiGHSIntExpr(e);
        sum.constant += v;
        return sum;
    }

    /**
     * Sums an integer expression and an integer value.
     *
     * @param e The integer expression.
     * @param v The integer value.
     * @return The resulting integer expression.
     */
    @Override
    public IntExpr sum(IntExpr e, int v) {
        return sum(v, e);
    }

    /**
     * Sums two integer expressions.
     *
     * @param e1 The first integer expression.
     * @param e2 The second integer expression.
     * @return The resulting integer expression, or null if the input types are invalid.
     */
    @Override
    public IntExpr sum(IntExpr e1, IntExpr e2) {
        HiGHSIntExpr sum = new HiGHSIntExpr(e1);
        HiGHSIntExpr addition = new HiGHSIntExpr(e2);
        for (int i = 0; i < addition.variables.size(); i++) {
            if (!sum.variables.contains(addition.variables.get(i))) {
                sum.variables.add(addition.variables.get(i));
                sum.coefficients.add(addition.coefficients.get(i));
            } else {
                int index = sum.variables.indexOf(addition.variables.get(i));
                sum.coefficients.set(index, sum.coefficients.get(index) + addition.coefficients.get(i));
            }
        }
        sum.constant += addition.constant;
        return sum;
    }

    /**
     * Sums a numerical value and a numerical expression.
     *
     * @param v The numerical value.
     * @param e The numerical expression.
     * @return The resulting numerical expression.
     */
    @Override
    public NumExpr sum(double v, NumExpr e) {
        HiGHSNumExpr sum = new HiGHSNumExpr(e);
        sum.constant += v;
        return sum;
    }

    /**
     * Sums a numerical expression and a numerical value.
     *
     * @param e The numerical expression.
     * @param v The numerical value.
     * @return The resulting numerical expression.
     */
    @Override
    public NumExpr sum(NumExpr e, double v) {
        return this.sum(v, e);
    }

    /**
     * Sums two numerical expressions.
     *
     * @param e1 The first numerical expression.
     * @param e2 The second numerical expression.
     * @return The resulting numerical expression.
     * @throws HiGHSException If the expression types are invalid.
     */
    @Override
    public NumExpr sum(NumExpr e1, NumExpr e2) {
        HiGHSNumExpr sum = new HiGHSNumExpr(e1);

        HiGHSNumExpr addition = new HiGHSNumExpr(e2);

        for (int i = 0; i < addition.variables.size(); i++) {
            if (!sum.variables.contains(addition.variables.get(i))) {
                sum.variables.add(addition.variables.get(i));
                sum.coefficients.add(addition.coefficients.get(i));
            } else {
                int index = sum.variables.indexOf(addition.variables.get(i));
                sum.coefficients.set(index, sum.coefficients.get(index) + addition.coefficients.get(i));
            }
        }
        sum.constant += addition.constant;
        return sum;

    }

    /**
     * Gets the name of the model.
     *
     * @return The name of the model.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the model.
     *
     * @param name The new name of the model.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds a fragment to the model.
     *
     * @param fragment The fragment to add.
     * @return The added fragment.
     */
    @Override
    public Fragment add(Fragment fragment) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Adds multiple fragments to the model.
     *
     * @param fragment An array of fragments to add.
     * @return An array of added fragments.
     */
    @Override
    public Fragment[] add(Fragment[] fragment) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Removes a fragment from the model.
     *
     * @param fragment The fragment to remove.
     * @return The removed fragment.
     */
    @Override
    public Fragment remove(Fragment fragment) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Removes multiple fragments from the model.
     *
     * @param fragment An array of fragments to remove.
     * @return An array of removed fragments.
     */
    @Override
    public Fragment[] remove(Fragment[] fragment) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Writes the active model to a .lp file.
     *
     * @param name the name of the file to write to
     **/
    public void exportModel(String name) {
        //Create new file
        File file = new File(name);
        try {
            FileWriter fileWriter = new FileWriter(file);
            if (this.objective.sense == ObjectiveSense.Minimize) {
                fileWriter.write("Minimize\n");
            } else if (this.objective.sense == ObjectiveSense.Maximize) {
                fileWriter.write("Maximize\n");
            } else {
                throw new HiGHSException("Invalid objective sense: " + this.objective.sense);
            }
            //Write the objective function
            HiGHSNumExpr expr_cast = new HiGHSNumExpr(this.objective.getExpr());
            for (int i = 0; i < expr_cast.coefficients.size(); i++) {
                if (i != 0) {

                    if (expr_cast.coefficients.get(i) < 0) {
                        fileWriter.write("- ");
                    } else {
                        fileWriter.write("+ ");
                    }
                }
                fileWriter.write(Math.abs(expr_cast.coefficients.get(i)) + " " + expr_cast.variables.get(i).getName() + " ");
            }

            if (this.objective.getConstant() != 0) {
                if (this.objective.getConstant() > 0) {
                    fileWriter.write("+ " + this.objective.getConstant() );
                } else {
                    fileWriter.write("- " + Math.abs(this.objective.getConstant()) );
                }
            }
            fileWriter.write("\n");
            //Write the constraints
            fileWriter.write("Subject To\n");
            for (Constraint constraint : constraints) {
                constraint = rebalanceConstraint(constraint);
                HiGHSConstraint hiGHSConstraint = new HiGHSConstraint(constraint);
                HiGHSNumExpr lhs_expr = new HiGHSNumExpr(hiGHSConstraint.lhs);
                HiGHSNumExpr rhs_expr = new HiGHSNumExpr(hiGHSConstraint.rhs);
                fileWriter.write(hiGHSConstraint.getName() + ": ");
                for (int i = 0; i < lhs_expr.coefficients.size(); i++) {
                    if (i != 0) {
                        if (lhs_expr.coefficients.get(i) < 0) {
                            fileWriter.write("- ");
                        } else {
                            fileWriter.write("+ ");
                        }
                    }
                    fileWriter.write(Math.abs(lhs_expr.coefficients.get(i)) + " " + lhs_expr.variables.get(i).getName() + " ");
                }
                if (lhs_expr.constant > 0) {
                    fileWriter.write("+ " + lhs_expr.constant + " ");
                } else if (lhs_expr.constant < 0) {
                    fileWriter.write("- " + Math.abs(lhs_expr.constant) + " ");
                }
                //Write the constraint type
                if (hiGHSConstraint.type == ConstraintType.Eq) {
                    fileWriter.write("= ");
                } else if (hiGHSConstraint.type == ConstraintType.Le) {
                    fileWriter.write("<= ");
                } else if (hiGHSConstraint.type == ConstraintType.Ge) {
                    fileWriter.write(">= ");
                } else {
                    throw new HiGHSException("Invalid constraint type: " + hiGHSConstraint.type);
                }
                //Write the right hand side
                for (int i = 0; i < rhs_expr.coefficients.size(); i++) {
                    if (rhs_expr.coefficients.get(i) < 0) {
                        fileWriter.write("- ");
                    } else {
                        fileWriter.write("+ ");
                    }
                    fileWriter.write(Math.abs(rhs_expr.coefficients.get(i)) + " " + rhs_expr.variables.get(i).getName() + " ");
                }
                if (rhs_expr.constant > 0) {
                    fileWriter.write("+ " + rhs_expr.constant );
                } else if (rhs_expr.constant < 0) {
                    fileWriter.write("- " + Math.abs(rhs_expr.constant) );
                } else if (rhs_expr.coefficients.isEmpty()) {
                    fileWriter.write(" 0 ");
                }
                fileWriter.write("\n");
            }

            //Write the bounds
            fileWriter.write("Bounds\n");
            for (NumVar variable : variables) {
                if (variable instanceof HiGHSNumVar var_cast) {
                    if (var_cast.getLB() != 0 && var_cast.getUB() < Integer.MAX_VALUE) {
                        fileWriter.write(var_cast.getLB() + " <= " + var_cast.getName() + " <= " + var_cast.getUB() + "\n");
                    } else if (var_cast.getLB() != 0) {
                        fileWriter.write(var_cast.getLB() + " <= " + var_cast.getName() + "\n");
                    } else if (var_cast.getUB() != Double.MAX_VALUE && var_cast.getUB() != Integer.MAX_VALUE) {
                        fileWriter.write(var_cast.getName() + " <= " + var_cast.getUB() + "\n");
                    }
                } else if (variable instanceof HiGHSIntVar var_cast) {

                    if (var_cast.getMin() != 0 && var_cast.getMax() < Integer.MAX_VALUE) {
                        fileWriter.write(var_cast.getMin() + " <= " + var_cast.getName() + " <= " + var_cast.getMax() + "\n");
                    } else if (var_cast.getMin() != 0) {
                        fileWriter.write(var_cast.getMin() + " <= " + var_cast.getName() + "\n");
                    } else if (var_cast.getMax() != Integer.MAX_VALUE) {
                        fileWriter.write(var_cast.getName() + " <= " + var_cast.getMax() + "\n");
                    }
                } else {
                    throw new HiGHSException("Invalid variable type: " + variable.getClass());
                }
            }
            //Write the variable type section
            fileWriter.write("Generals\n");
            for (NumVar variable : variables) {
                if (variable instanceof HiGHSBoolVar) {
                    continue;
                }
                if (variable instanceof HiGHSIntVar var_cast) {
                    fileWriter.write(var_cast.getName() + "\n");
                }
            }
            //Write the binary variable section
            fileWriter.write("Binaries\n");
            for (NumVar variable : variables) {
                if (variable instanceof HiGHSBoolVar var_cast) {
                    fileWriter.write(var_cast.getName() + "\n");
                }
            }
            fileWriter.write("End\n");
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Imports a solution file produced by HiGHS and populates the solution value hashmap.
     *
     * @param fileName The name of the solution file to import.
     * @throws HiGHSException If the solution file is not found or an error occurs while reading the file.
     */
    public void importSol(String fileName) {
        // Read a sol file produced by HiGHS and populate the solution value hashmap
        File file = new File(fileName);
        if (!file.exists()) {
            throw new HiGHSException("Solution file not found: " + fileName);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean inPrimalSection = false;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.equalsIgnoreCase("Infeasible")) {
                    throw new HiGHSException("The model is infeasible");
                }
                // Check for the start of the "Primal solution values" section
                if (line.equalsIgnoreCase("# Primal solution values")) {
                    inPrimalSection = true;
                    continue;
                }

                // Exit the section when encountering another header or end of file
                if (inPrimalSection && line.startsWith("# Dual solution values")) {
                    inPrimalSection = false;
                }

                // Parse variable values in the "Primal solution values" section
                if (inPrimalSection && !line.isEmpty()) {
                    String[] parts = line.split("\\s+");
                    // If the line starts with Objective, read it and set the corresponding double
                    if (parts[0].equalsIgnoreCase("Objective")) {
                        objectiveValue = Double.parseDouble(parts[1]);
                        continue;
                    }
                    if (parts.length == 2) {
                        String variableName = parts[0];
                        double value = Double.parseDouble(parts[1]);
                        for (NumVar variable : variables) {
                            if (variable.getName().equals(variableName)) {

                                solutionValues.put(variable, value);
                                break;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new HiGHSException("Error reading solution file: " + fileName, e);
        }
    }

    /**
     * Creates a new boolean variable.
     *
     * @return The created boolean variable.
     */
    public IntVar boolVar() {
        HiGHSBoolVar var = new HiGHSBoolVar();
        variables.add(var);
        return var;
    }

    /**
     * Creates a new boolean variable with a specified name.
     *
     * @param name The name of the boolean variable.
     * @return The created boolean variable.
     */
    public IntVar boolVar(String name) {
        HiGHSBoolVar var = new HiGHSBoolVar();
        var.setName(name);
        variables.add(var);
        return var;
    }

    /**
     * Creates a new integer variable.
     *
     * @return The created integer variable.
     */
    public IntVar intVar() {
        HiGHSIntVar var = new HiGHSIntVar();
        variables.add(var);
        return var;
    }

    /**
     * Creates a new integer variable with a specified name.
     *
     * @param name The name of the integer variable.
     * @return The created integer variable.
     */
    public IntVar intVar(String name) {
        HiGHSIntVar var = new HiGHSIntVar();
        var.setName(name);
        variables.add(var);
        return var;
    }

    /**
     * Creates a new integer variable with specified minimum and maximum bounds.
     *
     * @param min The minimum bound of the integer variable.
     * @param max The maximum bound of the integer variable.
     * @return The created integer variable.
     */
    public IntVar intVar(int min, int max) {
        HiGHSIntVar var = new HiGHSIntVar();
        var.setMin(min);
        var.setMax(max);
        variables.add(var);
        return var;
    }

    /**
     * Creates a new integer variable with specified minimum and maximum bounds and a name.
     *
     * @param min  The minimum bound of the integer variable.
     * @param max  The maximum bound of the integer variable.
     * @param name The name of the integer variable.
     * @return The created integer variable.
     */
    public IntVar intVar(int min, int max, String name) {
        HiGHSIntVar var = new HiGHSIntVar();
        var.setMin(min);
        var.setMax(max);
        var.setName(name);
        variables.add(var);
        return var;
    }

    /**
     * Creates a constant numerical expression with an integer value.
     *
     * @param i The integer value of the constant.
     * @return The created constant numerical expression.
     */
    public NumExpr constant(int i) {
        HiGHSNumExpr expr = new HiGHSNumExpr();
        expr.constant = (double) i;
        return expr;
    }

    /**
     * Creates a constant numerical expression with a double value.
     *
     * @param d The double value of the constant.
     * @return The created constant numerical expression.
     */
    public NumExpr constant(double d) {
        HiGHSNumExpr expr = new HiGHSNumExpr();
        expr.constant = d;
        return expr;
    }

    /**
     * Multiplies an integer value with a numerical expression.
     *
     * @param i       The integer multiplier.
     * @param numExpr The numerical expression to be multiplied.
     * @return The resulting numerical expression.
     */
    public NumExpr prod(int i, NumExpr numExpr) {
        return prod((double) i, numExpr);
    }

    /**
     * Multiplies an integer value with an integer expression.
     *
     * @param i      The integer multiplier.
     * @param numVar The integer expression to be multiplied.
     * @return The resulting integer expression.
     */
    public IntExpr prod(int i, IntExpr numVar) {
        HiGHSIntExpr expr = new HiGHSIntExpr(numVar);
        expr.coefficients.replaceAll(v -> v * i);
        expr.constant = expr.constant * i;
        return expr;
    }

    /**
     * Multiplies a double value with an integer expression.
     *
     * @param d      The double multiplier.
     * @param numVar The integer expression to be multiplied.
     * @return The resulting numerical expression.
     */
    public NumExpr prod(double d, IntExpr numVar) {
        return prod(d, (NumExpr) numVar);
    }

    /**
     * Multiplies a double value with a numerical expression.
     *
     * @param d      The double multiplier.
     * @param numVar The numerical expression to be multiplied.
     * @return The resulting numerical expression.
     */
    public NumExpr prod(double d, NumExpr numVar) {
        HiGHSNumExpr expr = new HiGHSNumExpr(numVar);
        expr.coefficients.replaceAll(v -> v * d);
        expr.constant = expr.constant * d;
        return expr;
    }

    /**
     * Adds a maximization objective to the model.
     *
     * @param objective The numerical expression representing the objective.
     * @return The created maximization objective.
     */
    public Objective addMaximize(NumExpr objective) {
        this.objective = new HiGHSObjective(objective, ObjectiveSense.Maximize);
        return this.objective;
    }

    /**
     * Adds a maximization objective to the model with a specified name.
     *
     * @param objective The numerical expression representing the objective.
     * @param name      The name of the objective.
     * @return The created maximization objective.
     */
    public Objective addMaximize(NumExpr objective, String name) {
        this.objective = new HiGHSObjective(objective, ObjectiveSense.Maximize);
        this.objective.setName(name);
        return this.objective;
    }

    /**
     * Adds a minimization objective to the model.
     *
     * @param objective The numerical expression representing the objective.
     * @return The created minimization objective.
     */
    public Objective addMinimize(NumExpr objective) {
        this.objective = new HiGHSObjective(objective, ObjectiveSense.Minimize);
        return this.objective;
    }

    /**
     * Adds a minimization objective to the model with a specified name.
     *
     * @param objective The numerical expression representing the objective.
     * @param name      The name of the objective.
     * @return The created minimization objective.
     */
    public Objective addMinimize(NumExpr objective, String name) {
        this.objective = new HiGHSObjective(objective, ObjectiveSense.Minimize);
        this.objective.setName(name);
        return this.objective;
    }

    /**
     * Adds an objective to the model with a specified sense (maximize or minimize).
     *
     * @param sense     The sense of the objective (maximize or minimize).
     * @param objective The numerical expression representing the objective.
     * @return The created objective.
     */
    public Objective addObjective(ObjectiveSense sense, NumExpr objective) {
        this.objective = new HiGHSObjective(objective, sense);
        return this.objective;
    }

    /**
     * Adds an objective to the model with a specified sense (maximize or minimize) and name.
     *
     * @param sense     The sense of the objective (maximize or minimize).
     * @param objective The numerical expression representing the objective.
     * @param name      The name of the objective.
     * @return The created objective.
     */
    public Objective addObjective(ObjectiveSense sense, NumExpr objective, String name) {
        this.objective = new HiGHSObjective(objective, sense);
        this.objective.setName(name);
        return this.objective;
    }

    /**
     * Gets the value of an integer variable from the solution.
     *
     * @param var The integer variable.
     * @return The value of the variable as an integer.
     */
    public int getValue(IntVar var) {
        return (int) Math.round(solutionValues.get(var));
    }

    /**
     * Gets the value of a numerical variable from the solution.
     *
     * @param var The numerical variable.
     * @return The value of the variable as a double.
     */
    public double getValue(NumVar var) {
        return solutionValues.get(var);
    }

    /**
     * Gets the value of the objective function from the solution.
     *
     * @return The value of the objective function.
     */
    public double getObjValue() {
        return objectiveValue;
    }

    /**
     * Creates a new numerical variable with specified bounds and name.
     *
     * @param lb   The lower bound of the variable.
     * @param ub   The upper bound of the variable.
     * @param name The name of the variable.
     * @return The created numerical variable.
     */
    public NumVar numVar(int lb, int ub, String name) {
        HiGHSNumVar var = new HiGHSNumVar();
        var.setLB(lb);
        var.setUB(ub);
        var.setName(name);
        variables.add(var);
        return var;
    }

    /**
     * Creates a new numerical variable with a specified name.
     *
     * @param name The name of the variable.
     * @return The created numerical variable.
     */
    public NumVar numVar(String name) {
        HiGHSNumVar var = new HiGHSNumVar();
        var.setName(name);
        variables.add(var);
        return var;
    }

    /**
     * Solves the model by exporting it to a file and calling the HiGHS solver.
     *
     * @throws RuntimeException If an error occurs during the solving process.
     */
    public void solve() {
        String uniqueID = UUID.randomUUID().toString();
        // Write to file and call the solver
        exportModel("out-" + uniqueID + ".lp");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("highs", "--model_file", "out-" + uniqueID + ".lp" , "--solution_file", "out-" + uniqueID + ".sol");
            processBuilder.redirectOutput(new File("out.txt"));
            processBuilder.redirectError(new File("error.txt"));
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("HiGHS solver failed with exit code: " + exitCode);
            }
            importSol("out-" + uniqueID + ".sol");
            //Delete created files after reading
            File file = new File("out-" + uniqueID + ".lp");
            if (!file.delete()) {
                throw new RuntimeException("Failed to delete the file: " + file.getName());
            }
            file = new File("out-" + uniqueID + ".sol");
            if (!file.delete()){
                throw new RuntimeException("Failed to delete the file: " + file.getName());
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Rebalances the constraint by subtracting the right-hand side from the left-hand side.
     *
     * @param constraint The constraint to be rebalanced.
     * @return The rebalanced constraint with updated left-hand and right-hand sides.
     */
    public Constraint rebalanceConstraint(Constraint constraint) {
        HiGHSConstraint hiGHSConstraint = new HiGHSConstraint(constraint);
        HiGHSNumExpr lhs_expr = new HiGHSNumExpr(hiGHSConstraint.lhs);
        HiGHSNumExpr rhs_expr = new HiGHSNumExpr(hiGHSConstraint.rhs);

        for (int i = 0; i < rhs_expr.variables.size(); i++) {
            if (!lhs_expr.variables.contains(rhs_expr.variables.get(i))) {
                lhs_expr.variables.add(rhs_expr.variables.get(i));
                lhs_expr.coefficients.add(-rhs_expr.coefficients.get(i));
            } else {
                int index = lhs_expr.variables.indexOf(rhs_expr.variables.get(i));
                lhs_expr.coefficients.set(index, lhs_expr.coefficients.get(index) - rhs_expr.coefficients.get(i));
            }
        }
        hiGHSConstraint.lhs = lhs_expr;
        hiGHSConstraint.rhs = constant( rhs_expr.constant - lhs_expr.constant);
        lhs_expr.constant = 0.0;
        return hiGHSConstraint;
    }


}
