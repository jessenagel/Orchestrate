package nl.jessenagel.orchestrate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import nl.jessenagel.jhighs.*;
/**
 * Orchestrate is the class used to create LP models to be solved using an external olver
 */
public class Orchestrate implements Modeler {
    final Logger logger = LoggerFactory.getLogger(Orchestrate.class);
    private final List<Constraint> constraints;
    private final List<NumVar> variables;
    private final Map<NumVar, Integer> varToIndex;
    private final Map<NumVar, Double> solutionValues;
    private String name;
    private OrchObjective objective;
    private int varCounter = 0;
    private int constraintCounter = 0;
    private double objectiveValue;
    private Status status;

    /**
     * Constructer which creates a new Orchestrate object
     */
    public Orchestrate() {
        this.name = "Orchestrate";
        this.constraints = new ArrayList<>();
        this.variables = new ArrayList<>();
        this.solutionValues = new HashMap<>();
        this.status = Status.Unknown;
        this.varToIndex = new HashMap<>();
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
        OrchConstraint constraint = new OrchConstraint(lhs, rhs, ConstraintType.Eq);
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
        OrchConstraint constraint = new OrchConstraint(lhs, constant(i), ConstraintType.Eq);
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
        OrchConstraint constraint = new OrchConstraint(lhs, rhs, ConstraintType.Le);
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
        OrchConstraint constraint = new OrchConstraint(lhs, rhs, ConstraintType.Ge);
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
        OrchIntExpr sum = new OrchIntExpr(e);
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
        OrchIntExpr sum = new OrchIntExpr(e1);
        OrchIntExpr addition = new OrchIntExpr(e2);
        for (Entry<IntVar, Integer> entry : addition.variablesAndCoefficients.entrySet()) {
            IntVar variable = entry.getKey();
            int coefficient = entry.getValue();
            if (!sum.variablesAndCoefficients.containsKey(variable)) {
                sum.variablesAndCoefficients.put(variable, coefficient);
            } else {
                sum.variablesAndCoefficients.put(variable, sum.variablesAndCoefficients.get(variable) + coefficient);
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
        OrchNumExpr sum = new OrchNumExpr(e);
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
     * @throws OrchException If the expression types are invalid.
     */
    @Override
    public NumExpr sum(NumExpr e1, NumExpr e2) {
        OrchSumExpr sum = new OrchSumExpr();
        sum.exprs.add(e1);
        sum.exprs.add(e2);
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
                throw new OrchException("Invalid objective sense: " + this.objective.sense);
            }
            //Write the objective function
            OrchNumExpr expr_cast = new OrchNumExpr(this.objective.getExpr());
            boolean first = true;
            for (Entry<NumVar, Double> entry : expr_cast.variablesAndCoefficients.entrySet()) {
                if (entry.getValue() < 0) {

                    fileWriter.write("- ");

                } else {
                    if (!first) {
                        fileWriter.write("+ ");
                    }
                }

                fileWriter.write(Math.abs(entry.getValue()) + " " + entry.getKey().getName() + " ");
                first = false;
            }
            if (this.objective.getConstant() != 0) {
                if (this.objective.getConstant() > 0) {
                    fileWriter.write("+ " + this.objective.getConstant());
                } else {
                    fileWriter.write("- " + Math.abs(this.objective.getConstant()));
                }
            }
            fileWriter.write("\n");
            //Write the constraints
            fileWriter.write("Subject To\n");
            for (Constraint constraint : constraints) {
                constraint = rebalanceConstraint(constraint);
                OrchConstraint orchConstraint = new OrchConstraint(constraint);
                OrchNumExpr lhs_expr = new OrchNumExpr(orchConstraint.lhs);
                OrchNumExpr rhs_expr = new OrchNumExpr(orchConstraint.rhs);
                fileWriter.write(orchConstraint.getName() + ": ");
                first = true;
                for (Entry<NumVar, Double> entry : lhs_expr.variablesAndCoefficients.entrySet()) {
                    if (entry.getValue() < 0) {
                        fileWriter.write("- ");
                    } else {
                        if (!first) {
                            fileWriter.write("+ ");
                        }
                    }
                    fileWriter.write(Math.abs(entry.getValue()) + " " + entry.getKey().getName() + " ");
                    first = false;
                }

                if (lhs_expr.constant > 0) {
                    fileWriter.write("+ " + lhs_expr.constant + " ");
                } else if (lhs_expr.constant < 0) {
                    fileWriter.write("- " + Math.abs(lhs_expr.constant) + " ");
                }
                //Write the constraint type
                if (orchConstraint.type == ConstraintType.Eq) {
                    fileWriter.write("= ");
                } else if (orchConstraint.type == ConstraintType.Le) {
                    fileWriter.write("<= ");
                } else if (orchConstraint.type == ConstraintType.Ge) {
                    fileWriter.write(">= ");
                } else {
                    throw new OrchException("Invalid constraint type: " + orchConstraint.type);
                }
                //Write the right hand side
                first= true;
                for (Entry<NumVar, Double> entry : rhs_expr.variablesAndCoefficients.entrySet()) {
                    if (entry.getValue() < 0) {
                        fileWriter.write("- ");
                    } else {
                        if(!first) {
                            fileWriter.write("+ ");
                        }
                    }
                    fileWriter.write(Math.abs(entry.getValue()) + " " + entry.getKey().getName() + " ");
                    first = false;
                }

                if (rhs_expr.constant > 0) {
                    if (rhs_expr.variablesAndCoefficients.isEmpty()) {
                        fileWriter.write(rhs_expr.constant + " ");
                    } else {
                        fileWriter.write("+ " + rhs_expr.constant);
                    }
                } else if (rhs_expr.constant < 0) {
                    fileWriter.write("- " + Math.abs(rhs_expr.constant));
                } else if (rhs_expr.variablesAndCoefficients.isEmpty()) {
                    fileWriter.write(" 0 ");
                }
                fileWriter.write("\n");
            }

            //Write the bounds
            fileWriter.write("Bounds\n");
            for (NumVar variable : variables) {
                if (variable instanceof OrchNumVar var_cast) {
                    if (var_cast.getLB() != 0 && var_cast.getUB() < Integer.MAX_VALUE) {
                        fileWriter.write(var_cast.getLB() + " <= " + var_cast.getName() + " <= " + var_cast.getUB() + "\n");
                    } else if (var_cast.getLB() != 0) {
                        fileWriter.write(var_cast.getLB() + " <= " + var_cast.getName() + "\n");
                    } else if (var_cast.getUB() != Double.MAX_VALUE && var_cast.getUB() != Integer.MAX_VALUE) {
                        fileWriter.write(var_cast.getName() + " <= " + var_cast.getUB() + "\n");
                    }
                } else if (variable instanceof OrchIntVar var_cast) {

                    if (var_cast.getMin() != 0 && var_cast.getMax() < Integer.MAX_VALUE) {
                        fileWriter.write(var_cast.getMin() + " <= " + var_cast.getName() + " <= " + var_cast.getMax() + "\n");
                    } else if (var_cast.getMin() != 0) {
                        fileWriter.write(var_cast.getMin() + " <= " + var_cast.getName() + "\n");
                    } else if (var_cast.getMax() != Integer.MAX_VALUE) {
                        fileWriter.write(var_cast.getName() + " <= " + var_cast.getMax() + "\n");
                    }
                } else {
                    throw new OrchException("Invalid variable type: " + variable.getClass());
                }
            }
            //Write the variable type section
            fileWriter.write("Generals\n");
            for (NumVar variable : variables) {
                if (variable instanceof OrchBoolVar) {
                    continue;
                }
                if (variable instanceof OrchIntVar var_cast) {
                    fileWriter.write(var_cast.getName() + "\n");
                }
            }
            //Write the binary variable section
            fileWriter.write("Binaries\n");
            for (NumVar variable : variables) {
                if (variable instanceof OrchBoolVar var_cast) {
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
     * @throws OrchException If the solution file is not found or an error occurs while reading the file.
     */
    public void importSol(String fileName) {
        // Read a sol file produced by HiGHS and populate the solution value hashmap
        File file = new File(fileName);
        if (!file.exists()) {
            throw new OrchException("Solution file not found: " + fileName);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean inPrimalSection = false;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.equalsIgnoreCase("Infeasible")) {
                    this.status = Status.Infeasible;
                    throw new OrchException("The model is infeasible");
                }
                if (line.equalsIgnoreCase("Unbounded")) {
                    this.status = Status.Unbounded;
                    throw new OrchException("The model is unbounded");
                }
                if (line.equalsIgnoreCase("Optimal")) {
                    this.status = Status.Optimal;
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
            throw new OrchException("Error reading solution file: " + fileName, e);
        }
    }

    /**
     * Creates a new boolean variable.
     *
     * @return The created boolean variable.
     */
    public IntVar boolVar() {
        OrchBoolVar var = new OrchBoolVar();
        variables.add(var);
        varToIndex.put(var, varCounter);
        varCounter++;
        return var;
    }

    /**
     * Creates a new boolean variable with a specified name.
     *
     * @param name The name of the boolean variable.
     * @return The created boolean variable.
     */
    public IntVar boolVar(String name) {
        OrchBoolVar var = new OrchBoolVar();
        var.setName(name);
        variables.add(var);
        varToIndex.put(var, varCounter);
        varCounter++;
        return var;
    }

    /**
     * Creates a new integer variable.
     *
     * @return The created integer variable.
     */
    public IntVar intVar() {
        OrchIntVar var = new OrchIntVar();
        variables.add(var);
        varToIndex.put(var, varCounter);
        varCounter++;
        return var;
    }

    /**
     * Creates a new integer variable with a specified name.
     *
     * @param name The name of the integer variable.
     * @return The created integer variable.
     */
    public IntVar intVar(String name) {
        OrchIntVar var = new OrchIntVar();
        var.setName(name);
        variables.add(var);
        varToIndex.put(var, varCounter);
        varCounter++;
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
        OrchIntVar var = new OrchIntVar();
        var.setMin(min);
        var.setMax(max);
        variables.add(var);
        varToIndex.put(var, varCounter);
        varCounter++;
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
        OrchIntVar var = new OrchIntVar();
        var.setMin(min);
        var.setMax(max);
        var.setName(name);
        variables.add(var);
        varToIndex.put(var, varCounter);
        varCounter++;
        return var;
    }

    /**
     * Creates a constant numerical expression with an integer value.
     *
     * @param i The integer value of the constant.
     * @return The created constant numerical expression.
     */
    public NumExpr constant(int i) {
        OrchNumExpr expr = new OrchNumExpr();
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
        OrchNumExpr expr = new OrchNumExpr();
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
        OrchIntExpr expr = new OrchIntExpr(numVar);
        expr.variablesAndCoefficients.replaceAll((k, v) -> v * i);
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
        OrchNumExpr expr = new OrchNumExpr(numVar);
        expr.variablesAndCoefficients.replaceAll((k, v) -> v * d);
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
        this.objective = new OrchObjective(objective, ObjectiveSense.Maximize);
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
        this.objective = new OrchObjective(objective, ObjectiveSense.Maximize);
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
        this.objective = new OrchObjective(objective, ObjectiveSense.Minimize);
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
        this.objective = new OrchObjective(objective, ObjectiveSense.Minimize);
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
        this.objective = new OrchObjective(objective, sense);
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
        this.objective = new OrchObjective(objective, sense);
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
        OrchNumVar var = new OrchNumVar();
        var.setLB(lb);
        var.setUB(ub);
        var.setName(name);
        variables.add(var);
        varToIndex.put(var, varCounter);
        varCounter++;
        return var;
    }

    /**
     * Creates a new numerical variable with a specified name.
     *
     * @param name The name of the variable.
     * @return The created numerical variable.
     */
    public NumVar numVar(String name) {
        OrchNumVar var = new OrchNumVar();
        var.setName(name);
        variables.add(var);
        varToIndex.put(var, varCounter);
        varCounter++;
        return var;
    }

    /**
     * Solves the model by exporting it to a file and calling the HiGHS solver.
     *
     * @throws RuntimeException If an error occurs during the solving process.
     */
    public void solveByExportingFile() {
        String uniqueID = UUID.randomUUID().toString();
        // Write to file and call the solver
        exportModel("out-" + uniqueID + ".lp");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("highs", "--model_file", "out-" + uniqueID + ".lp", "--solution_file", "out-" + uniqueID + ".sol");
            File outFile = new File("out.txt");
            File errFile = new File("error.txt");
            Process process = processBuilder.start();
            try (BufferedReader stdOutReader = new BufferedReader(new InputStreamReader(process.getInputStream())); BufferedReader stdErrReader = new BufferedReader(new InputStreamReader(process.getErrorStream())); PrintWriter outWriter = new PrintWriter(outFile); PrintWriter errWriter = new PrintWriter(errFile)) {
                String line;
                while ((line = stdOutReader.readLine()) != null) {
                    outWriter.println(line);
                    logger.info("[STDOUT] {}", line);
                }

                while ((line = stdErrReader.readLine()) != null) {
                    errWriter.println(line);
                    logger.error("[STDERR] {}", line);
                }
            }
            // Log standard output
            int exitCode = process.waitFor();
            logger.info("HiGHS solver exited with code: {}", exitCode);
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
            if (!file.delete()) {
                throw new RuntimeException("Failed to delete the file: " + file.getName());
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Solves the model by calling the HiGHS solver directly through the JHighs library.
     *
     * @throws RuntimeException If an error occurs during the solving process.
     */
    public void solve() {
        HiGHS solver = new HiGHS();
        // Add the variables to the solver
        for (NumVar variable : variables) {
            solver.addVar(variable.getLB(), variable.getUB());
            if (variable instanceof OrchIntVar ) {
                solver.changeColIntegrality(varToIndex.get(variable),VarType.kInteger);
            }
        }
        // Add the constraints to the solver
        for (Constraint constraint : constraints) {
            OrchConstraint orchConstraint = new OrchConstraint(constraint);
            OrchNumExpr lhs_expr = new OrchNumExpr(orchConstraint.lhs);
            OrchNumExpr rhs_expr = new OrchNumExpr(orchConstraint.rhs);
            double[] coeffs = new double[lhs_expr.variablesAndCoefficients.size()];
            int[] vars = new int[lhs_expr.variablesAndCoefficients.size()];
            int i = 0;
            for (Entry<NumVar, Double> entry : lhs_expr.variablesAndCoefficients.entrySet()) {
                vars[i] = varToIndex.get(entry.getKey());
                coeffs[i] = entry.getValue();
                i++;
            }
            if(orchConstraint.type == ConstraintType.Eq) {
                solver.addConstraint(coeffs, vars, rhs_expr.constant, rhs_expr.constant);
            } else if(orchConstraint.type == ConstraintType.Le) {
                solver.addConstraint(coeffs, vars, Double.NEGATIVE_INFINITY, rhs_expr.constant);
            } else if(orchConstraint.type == ConstraintType.Ge) {
                solver.addConstraint(coeffs, vars, rhs_expr.constant, Double.POSITIVE_INFINITY);
            } else {
                throw new OrchException("Invalid constraint type: " + orchConstraint.type);
            }
        }
        // Set the objective function
        OrchNumExpr objectiveExpr = new OrchNumExpr(this.objective.getExpr());
        double[] objCoeffs = new double[objectiveExpr.variablesAndCoefficients.size()];
        int[] objVars = new int[objectiveExpr.variablesAndCoefficients.size()];
        int i = 0;
        //Todo: Check if this is correct
        for (Entry<NumVar, Double> entry : objectiveExpr.variablesAndCoefficients.entrySet()) {
            objVars[i] = varToIndex.get(entry.getKey());
            objCoeffs[i] = entry.getValue();
            i++;
        }
        solver.setObjectiveFunction(objCoeffs, objVars, this.objective.sense == ObjectiveSense.Minimize, objectiveExpr.constant);

        HighsStatus highsStatus = solver.solve();
        if (highsStatus == HighsStatus.kOk){
            this.status = Status.Optimal;
            // Import the solution values
            Solution solution = solver.getSolution();
            double[] values = solution.getVariableValues();
            for (int j = 0; j < values.length; j++) {
                NumVar variable = variables.get(j);
                this.solutionValues.put(variable, values[j]);
            }
            this.objectiveValue = solution.getObjectiveValue();
        } else if (highsStatus == HighsStatus.kError) {
            this.status = Status.Error;
            throw new OrchException("An error occurred while solving the model: " + highsStatus);
        }
    }
    /**
     * Rebalances the constraint by subtracting the right-hand side from the left-hand side.
     *
     * @param constraint The constraint to be rebalanced.
     * @return The rebalanced constraint with updated left-hand and right-hand sides.
     */
    public Constraint rebalanceConstraint(Constraint constraint) {
        OrchConstraint orchConstraint = new OrchConstraint(constraint);
        OrchNumExpr lhs_expr = new OrchNumExpr(orchConstraint.lhs);
        OrchNumExpr rhs_expr = new OrchNumExpr(orchConstraint.rhs);
        for (Entry<NumVar, Double> entry : rhs_expr.variablesAndCoefficients.entrySet()) {
            if (!lhs_expr.variablesAndCoefficients.containsKey(entry.getKey())) {
                lhs_expr.variablesAndCoefficients.put(entry.getKey(), -entry.getValue());
            } else {
                lhs_expr.variablesAndCoefficients.put(entry.getKey(), lhs_expr.variablesAndCoefficients.get(entry.getKey()) - entry.getValue());
            }
        }
        orchConstraint.lhs = lhs_expr;
        orchConstraint.rhs = constant(rhs_expr.constant - lhs_expr.constant);
        lhs_expr.constant = 0.0;
        return orchConstraint;
    }

    public Status getStatus() {
        return this.status;
    }

    /**
     * Enum representing the status of the optimization problem.
     */
    public enum Status {
        Bounded, Error, Feasible, Infeasible, InfeasibleOrUnbounded, Optimal, Unbounded, Unknown
    }


}
