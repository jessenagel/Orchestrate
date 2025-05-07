package nl.jessenagel.highsjava;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HiGHS is the class used to create LP models to be solved in the HiGHS solver
 */
public class HiGHS implements Modeler {

    private String name;

    private final List<Constraint> constraints;
    private final List<NumVar> variables;
    private final Map<NumVar, Double> solutionValues;
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
        //TODO: Empty constraint
        return null;
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
        return null;
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
        return null;
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
        if (e1.getClass() == HiGHSIntExpr.class && e2.getClass() == HiGHSIntExpr.class) {
            HiGHSIntExpr lhs = (HiGHSIntExpr) e1;
            HiGHSIntExpr rhs = (HiGHSIntExpr) e2;
            HiGHSIntExpr sum = new HiGHSIntExpr();
            sum.variables.addAll(lhs.variables);
            sum.variables.addAll(rhs.variables);
            sum.coefficients.addAll(lhs.coefficients);
            sum.coefficients.addAll(rhs.coefficients);
            return sum;
        }
        return null;
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
        return null;
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
        return null;
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
        NumExpr lhs = e1;
        NumExpr rhs = e2;
        if (e1 instanceof NumVar) {
            HiGHSNumExpr expr = new HiGHSNumExpr();
            expr.variables.add((NumVar) e1);
            expr.coefficients.add(1.0);
            lhs = expr;
        }
        if (e2 instanceof NumVar) {
            HiGHSNumExpr expr = new HiGHSNumExpr();
            expr.variables.add((NumVar) e2);
            expr.coefficients.add(1.0);
            rhs = expr;
        }
        if (lhs instanceof HiGHSNumExpr lhs_cast && rhs instanceof HiGHSNumExpr rhs_cast) {
            HiGHSNumExpr sum = new HiGHSNumExpr(lhs_cast);
            sum.variables.addAll(rhs_cast.variables);
            sum.coefficients.addAll(rhs_cast.coefficients);
            return sum;
        } else if (lhs instanceof HiGHSIntExpr lhs_cast && rhs instanceof HiGHSIntExpr rhs_cast) {
            HiGHSIntExpr sum = new HiGHSIntExpr();
            sum.variables.addAll(lhs_cast.variables);
            sum.variables.addAll(rhs_cast.variables);
            sum.coefficients.addAll(lhs_cast.coefficients);
            sum.coefficients.addAll(rhs_cast.coefficients);
            return sum;
        } else if (lhs instanceof HiGHSIntExpr lhs_cast && rhs instanceof HiGHSNumExpr rhs_cast) {
            HiGHSNumExpr sum = new HiGHSNumExpr(lhs_cast);
            sum.variables.addAll(rhs_cast.variables);
            sum.coefficients.addAll(rhs_cast.coefficients);
            return sum;
        } else if (lhs instanceof HiGHSNumExpr lhs_cast && rhs instanceof HiGHSIntExpr rhs_cast) {
            HiGHSNumExpr sum = new HiGHSNumExpr(lhs_cast);
            sum.variables.addAll(rhs_cast.variables);
            for (int i = 0; i < rhs_cast.variables.size(); i++) {
                sum.coefficients.add(rhs_cast.coefficients.get(i).doubleValue());
            }
            return sum;
        }
        throw new HiGHSException("Invalid expression types for sum: " + e1.getClass() + ", " + e2.getClass());
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
            if (this.objective.expr instanceof HiGHSNumExpr expr_cast) {
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
            } else if (this.objective.expr instanceof HiGHSIntExpr expr_cast) {
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
            } else {
                throw new HiGHSException("Invalid expression type for objective: " + this.objective.expr.getClass());
            }
            fileWriter.write(this.objective.getConstant() + "\n");
            //Write the constraints
            fileWriter.write("Subject To\n");
            for (Constraint constraint : constraints) {
                if (constraint instanceof HiGHSConstraint constraint_cast) {
                    fileWriter.write(constraint_cast.getName() + ": ");
                    if (constraint_cast.lhs instanceof HiGHSNumExpr expr_cast) {
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
                    } else if (constraint_cast.lhs instanceof HiGHSIntExpr expr_cast) {
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
                    } else {
                        throw new HiGHSException("Invalid expression type for constraint: " + constraint_cast.lhs.getClass());
                    }
                    if (constraint_cast.type == ConstraintType.Eq) {
                        fileWriter.write("= ");
                    } else if (constraint_cast.type == ConstraintType.Le) {
                        fileWriter.write("<= ");
                    } else if (constraint_cast.type == ConstraintType.Ge) {
                        fileWriter.write(">= ");
                    } else {
                        throw new HiGHSException("Invalid constraint type: " + constraint_cast.type);
                    }
                    //Write the right hand side
                    if (constraint_cast.rhs instanceof HiGHSNumExpr expr_cast) {
                        for (int i = 0; i < expr_cast.coefficients.size(); i++) {
                            if (expr_cast.coefficients.get(i) < 0) {
                                fileWriter.write("- ");
                            } else {
                                fileWriter.write("+ ");
                            }
                            fileWriter.write(Math.abs(expr_cast.coefficients.get(i)) + " " + expr_cast.variables.get(i).getName() + " ");
                        }
                    } else if (constraint_cast.rhs instanceof HiGHSIntExpr expr_cast) {
                        for (int i = 0; i < expr_cast.coefficients.size(); i++) {
                            if (expr_cast.coefficients.get(i) < 0) {
                                fileWriter.write("- ");
                            } else {
                                fileWriter.write("+ ");
                            }
                            fileWriter.write(Math.abs(expr_cast.coefficients.get(i)) + " " + expr_cast.variables.get(i).getName() + " ");
                        }
                    } else {
                        throw new HiGHSException("Invalid expression type for constraint: " + constraint_cast.rhs.getClass());

                    }
                    if (constraint_cast.rhs instanceof HiGHSNumExpr expr_cast) {

                        fileWriter.write(expr_cast.constant + "\n");
                    } else if (constraint_cast.rhs instanceof HiGHSIntExpr expr_cast) {
                        fileWriter.write(expr_cast.constant + "\n");
                    } else {
                        throw new HiGHSException("Invalid expression type for constraint: " + constraint_cast.rhs.getClass());
                    }
                }
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
        HiGHSIntExpr expr = new HiGHSIntExpr();
        expr.constant = i;
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
        HiGHSNumExpr expr = new HiGHSNumExpr(numExpr);
        expr.coefficients.replaceAll(v -> v * i);
        return expr;
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
        HiGHSNumExpr expr = new HiGHSNumExpr(numVar);
        expr.coefficients.replaceAll(v -> v * d);
        return expr;
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
        // Write to file and call the solver
        exportModel("out.lp");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("highs", "--model_file", "out.lp", "--solution_file", "out.sol");
            processBuilder.redirectOutput(new File("out.txt"));
            processBuilder.redirectError(new File("error.txt"));
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Represents a constraint in the HiGHS model.
     * A constraint consists of a left-hand side (lhs) expression, a right-hand side (rhs) expression,
     * and a constraint type (e.g., equality, less-than-or-equal-to, greater-than-or-equal-to).
     */
    private class HiGHSConstraint implements Constraint {
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
         * Constructs a new HiGHSConstraint with the specified lhs, rhs, and type.
         *
         * @param lhs  The left-hand side numerical expression.
         * @param rhs  The right-hand side numerical expression.
         * @param type The type of the constraint.
         */
        HiGHSConstraint(NumExpr lhs, NumExpr rhs, ConstraintType type) {
            this.lhs = lhs;
            this.rhs = rhs;
            this.type = type;
            this.name = "Constraint_" + constraintCounter++;
        }

        /**
         * Constructs a new HiGHSConstraint with a default name.
         */
        HiGHSConstraint() {
            this.name = "Constraint_" + constraintCounter++;
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
    }

    /**
     * Represents a numerical variable in the HiGHS model.
     * A numerical variable has a name, lower bound, and upper bound.
     */
    private class HiGHSNumVar implements NumVar {
        /**
         * The name of the numerical variable.
         */
        String name;

        /**
         * The lower bound of the numerical variable.
         */
        double lb;

        /**
         * The upper bound of the numerical variable.
         */
        double ub;

        /**
         * Constructs a new HiGHSNumVar with default bounds and a generated name.
         * The lower bound is set to 0, and the upper bound is set to Double.MAX_VALUE.
         */
        HiGHSNumVar() {
            this.name = "NumVar_" + varCounter++;
            this.lb = 0;
            this.ub = Double.MAX_VALUE;
        }

        /**
         * Gets the lower bound of the numerical variable.
         *
         * @return The lower bound of the variable.
         */
        @Override
        public double getLB() {
            return this.lb;
        }

        /**
         * Gets the upper bound of the numerical variable.
         *
         * @return The upper bound of the variable.
         */
        @Override
        public double getUB() {
            return this.ub;
        }

        /**
         * Gets the type of the numerical variable.
         *
         * @return The type of the variable, or null if not specified.
         */
        @Override
        public NumVarType getType() {
            return null;
        }

        /**
         * Gets the name of the numerical variable.
         *
         * @return The name of the variable.
         */
        @Override
        public String getName() {
            return name;
        }

        /**
         * Sets the lower bound of the numerical variable.
         *
         * @param lb The new lower bound of the variable.
         */
        @Override
        public void setLB(double lb) {
            this.lb = lb;
        }

        /**
         * Sets the upper bound of the numerical variable.
         *
         * @param ub The new upper bound of the variable.
         */
        @Override
        public void setUB(double ub) {
            this.ub = ub;
        }

        /**
         * Sets the name of the numerical variable.
         *
         * @param name The new name of the variable.
         */
        @Override
        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * Represents an integer variable in the HiGHS model.
     * An integer variable has a name, minimum bound, maximum bound, and a type.
     */
    private class HiGHSIntVar implements IntVar {
        /**
         * The name of the integer variable.
         */
        String name;

        /**
         * The maximum bound of the integer variable.
         */
        int max;

        /**
         * The minimum bound of the integer variable.
         */
        int min;

        /**
         * The type of the variable, which is set to integer.
         */
        NumVarType type = NumVarType.Int;

        /**
         * Constructs a new HiGHSIntVar with default bounds and a generated name.
         * The minimum bound is set to 0, and the maximum bound is set to Integer.MAX_VALUE.
         */
        public HiGHSIntVar() {
            this.name = "IntVar_" + varCounter++;
            this.max = Integer.MAX_VALUE;
            this.min = 0;
        }

        /**
         * Constructs a new HiGHSIntVar with specified minimum and maximum bounds.
         *
         * @param min The minimum bound of the integer variable.
         * @param max The maximum bound of the integer variable.
         */
        public HiGHSIntVar(int min, int max) {
            this.name = "IntVar_" + varCounter++;
            this.max = max;
            this.min = min;
        }

        /**
         * Constructs a new HiGHSIntVar with a specified maximum bound.
         * The minimum bound is set to 0.
         *
         * @param max The maximum bound of the integer variable.
         */
        public HiGHSIntVar(int max) {
            this.name = "IntVar_" + varCounter++;
            this.max = max;
            this.min = 0;
        }

        /**
         * Gets the maximum bound of the integer variable.
         *
         * @return The maximum bound of the variable.
         */
        @Override
        public int getMax() {
            return max;
        }

        /**
         * Gets the minimum bound of the integer variable.
         *
         * @return The minimum bound of the variable.
         */
        @Override
        public int getMin() {
            return min;
        }

        /**
         * Sets the maximum bound of the integer variable.
         *
         * @param max The new maximum bound of the variable.
         * @return The updated maximum bound.
         */
        @Override
        public int setMax(int max) {
            this.max = max;
            return max;
        }

        /**
         * Sets the minimum bound of the integer variable.
         *
         * @param min The new minimum bound of the variable.
         * @return The updated minimum bound.
         */
        @Override
        public int setMin(int min) {
            this.min = min;
            return min;
        }

        /**
         * Gets the lower bound of the integer variable as a double.
         *
         * @return The lower bound of the variable.
         */
        @Override
        public double getLB() {
            return min;
        }

        /**
         * Gets the upper bound of the integer variable as a double.
         *
         * @return The upper bound of the variable.
         */
        @Override
        public double getUB() {
            return max;
        }

        /**
         * Gets the type of the integer variable.
         *
         * @return The type of the variable, or null if not specified.
         */
        @Override
        public NumVarType getType() {
            return null;
        }

        /**
         * Gets the name of the integer variable.
         *
         * @return The name of the variable.
         */
        @Override
        public String getName() {
            return name;
        }

        /**
         * Sets the lower bound of the integer variable. This method is not implemented.
         *
         * @param lb The new lower bound of the variable.
         */
        @Override
        public void setLB(double lb) {
            // Not implemented
        }

        /**
         * Sets the upper bound of the integer variable. This method is not implemented.
         *
         * @param ub The new upper bound of the variable.
         */
        @Override
        public void setUB(double ub) {
            // Not implemented
        }

        /**
         * Sets the name of the integer variable.
         *
         * @param name The new name of the variable.
         */
        @Override
        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * Represents a boolean variable in the HiGHS model.
     * A boolean variable is a specialized integer variable with bounds [0, 1].
     */
    private class HiGHSBoolVar extends HiGHSIntVar {
        /**
         * The type of the variable, which is set to boolean.
         */
        NumVarType type = NumVarType.Bool;

        /**
         * Constructs a new HiGHSBoolVar with default bounds [0, 1] and a generated name.
         */
        public HiGHSBoolVar() {
            this.name = "BoolVar_" + varCounter++;
            this.max = 1;
            this.min = 0;
        }
    }

    /**
     * Represents a numerical expression in the HiGHS model.
     * A numerical expression consists of variables, coefficients, and a constant term.
     */
    private class HiGHSNumExpr implements NumExpr {
        /**
         * The name of the numerical expression.
         */
        String name;

        /**
         * The list of variables in the numerical expression.
         */
        List<NumVar> variables;

        /**
         * The list of coefficients corresponding to the variables.
         */
        List<Double> coefficients;

        /**
         * The constant term in the numerical expression.
         */
        Double constant;

        /**
         * Constructs a new HiGHSNumExpr by copying an existing numerical expression.
         *
         * @param expr The numerical expression to copy.
         * @throws HiGHSException If the expression type is invalid.
         */
        public HiGHSNumExpr(NumExpr expr) {
            name = expr.getName() + "_" + varCounter++;
            if (expr instanceof HiGHSNumExpr expr_cast) {
                this.coefficients = new ArrayList<>(expr_cast.coefficients);
                this.variables = new ArrayList<>(expr_cast.variables);
                this.constant = expr_cast.constant;
            } else if (expr instanceof HiGHSIntExpr expr_cast) {
                this.coefficients = new ArrayList<>();
                for (Integer value : expr_cast.coefficients) {
                    this.coefficients.add(value.doubleValue());
                }
                this.variables = new ArrayList<>(expr_cast.variables);
                this.constant = (double) expr_cast.constant;
            } else if (expr instanceof HiGHSIntVar expr_cast) {
                this.coefficients = new ArrayList<>();
                this.coefficients.add(1.0);
                this.variables = new ArrayList<>();
                this.variables.add(expr_cast);
                this.constant = 0.0;
            } else if (expr instanceof HiGHSNumVar expr_cast) {
                this.coefficients = new ArrayList<>();
                this.coefficients.add(1.0);
                this.variables = new ArrayList<>();
                this.variables.add(expr_cast);
                this.constant = 0.0;
            } else if (expr instanceof HiGHSBoolVar expr_cast) {
                this.coefficients = new ArrayList<>();
                this.coefficients.add(1.0);
                this.variables = new ArrayList<>();
                this.variables.add(expr_cast);
                this.constant = 0.0;
            } else {
                throw new HiGHSException("Invalid expression type: " + expr.getClass());
            }
        }

        /**
         * Constructs a new HiGHSNumExpr for a single variable with a coefficient of 1.
         *
         * @param var The variable to include in the expression.
         */
        public HiGHSNumExpr(NumVar var) {
            this.name = "NumExpr_" + varCounter++;
            this.variables = new ArrayList<>();
            this.coefficients = new ArrayList<>();
            this.constant = 0.0;
            this.variables.add(var);
            this.coefficients.add(1.0);
        }

        /**
         * Gets the name of the numerical expression.
         *
         * @return The name of the expression.
         */
        @Override
        public String getName() {
            return name;
        }

        /**
         * Sets the name of the numerical expression.
         *
         * @param name The new name of the expression.
         */
        @Override
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Constructs a new HiGHSNumExpr with no variables, coefficients, or constant.
         */
        HiGHSNumExpr() {
            this.name = "NumExpr_" + varCounter++;
            this.variables = new ArrayList<>();
            this.coefficients = new ArrayList<>();
            this.constant = 0.0;
        }
    }

    /**
     * Represents an integer expression in the HiGHS model.
     * An integer expression consists of a name, a list of integer variables,
     * a list of integer coefficients, and a constant term.
     */
    private class HiGHSIntExpr implements IntExpr {
        /**
         * The name of the integer expression.
         */
        String name;

        /**
         * The list of integer variables in the expression.
         */
        List<IntVar> variables;

        /**
         * The list of integer coefficients corresponding to the variables.
         */
        List<Integer> coefficients;

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
            this.name = "IntExpr_" + varCounter++;
            this.variables = new ArrayList<>();
            this.coefficients = new ArrayList<>();
            this.constant = 0;
        }

        /**
         * Constructs a new HiGHSIntExpr by copying an existing integer expression.
         *
         * @param expr The integer expression to copy.
         * @throws HiGHSException If the expression type is invalid.
         */
        HiGHSIntExpr(IntExpr expr) {
            this.name = expr.getName() + "_" + varCounter++;
            if (expr instanceof HiGHSIntExpr expr_cast) {
                this.coefficients = new ArrayList<>(expr_cast.coefficients);
                this.variables = new ArrayList<>(expr_cast.variables);
                this.constant = expr_cast.constant;
            }
            if (expr instanceof HiGHSNumExpr expr_cast) {
                throw new HiGHSException("Invalid expression type, converting NumExpr to IntExpr is not defined: " + expr.getClass() + ", " + expr_cast.getClass());
            }
        }
    }

    /**
     * Represents an objective in the HiGHS model.
     * An objective consists of a numerical expression, a sense (maximize or minimize), and a name.
     */
    private class HiGHSObjective implements Objective {
        /**
         * The name of the objective.
         */
        String name;

        /**
         * The numerical expression representing the objective.
         */
        NumExpr expr;

        /**
         * The sense of the objective (maximize or minimize).
         */
        ObjectiveSense sense;

        /**
         * Constructs a new HiGHSObjective with the specified expression and sense.
         * The name is generated automatically.
         *
         * @param expr  The numerical expression representing the objective.
         * @param sense The sense of the objective (maximize or minimize).
         */
        public HiGHSObjective(NumExpr expr, ObjectiveSense sense) {
            this.expr = expr;
            this.sense = sense;
            this.name = "Objective_" + varCounter++;
        }

        /**
         * Gets the constant term of the objective's numerical expression.
         *
         * @return The constant term of the expression.
         * @throws HiGHSException If the expression type is invalid.
         */
        @Override
        public double getConstant() {
            if (expr instanceof HiGHSNumExpr expr_cast) {
                return expr_cast.constant;
            }
            if (expr instanceof HiGHSIntExpr expr_cast) {
                return expr_cast.constant.doubleValue();
            }
            throw new HiGHSException("Invalid expression type for constant: " + expr.getClass());
        }

        /**
         * Gets the numerical expression of the objective.
         *
         * @return The numerical expression.
         */
        @Override
        public NumExpr getExpr() {
            return expr;
        }

        /**
         * Gets the name of the objective.
         *
         * @return The name of the objective.
         */
        @Override
        public String getName() {
            return name;
        }

        /**
         * Sets the name of the objective.
         *
         * @param name The new name of the objective.
         */
        @Override
        public void setName(String name) {
            this.name = name;
        }
    }
}
