package nl.jessenagel.highsjava;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HiGHS implements Modeler{

    private String name;

    private final List<Constraint> constraints;
    private final List<NumVar> variables;
    private final Map<NumVar, Double> solutionValues;
    private HiGHSObjective objective;
    private int varCounter = 0;
    private int constraintCounter = 0;
    private double objectiveValue;
    public HiGHS(){
        this.name = "HIHGS";
        this.constraints = new ArrayList<>();
        this.variables = new ArrayList<>();
        this.solutionValues = new HashMap<>();

    }
    @Override
    public Constraint addEq(NumExpr lhs, NumExpr rhs) {
        // TODO Auto-generated method stub
        return null;
    }

    public Constraint addEq(NumExpr lhs, int i) {
        HiGHSConstraint constraint = new HiGHSConstraint(lhs, constant(i), ConstraintType.Eq);
        constraints.add(constraint);
        return constraint;
    }

    @Override
    public Constraint addLe(NumExpr lhs, NumExpr rhs) {
        HiGHSConstraint constraint = new HiGHSConstraint(lhs, rhs, ConstraintType.Le);
        constraints.add(constraint);
        return constraint;
    }

    @Override
    public Constraint addGe(NumExpr lhs, NumExpr rhs) {
        HiGHSConstraint constraint = new HiGHSConstraint(lhs, rhs, ConstraintType.Ge);
        constraints.add(constraint);
        return constraint;
    }

    @Override
    public IntExpr sum(int v, IntExpr e) {
        return null;
    }

    @Override
    public IntExpr sum(IntExpr e, int v) {
        return null;
    }

    @Override
    public IntExpr sum(IntExpr e1, IntExpr e2) {
        if(e1.getClass()== HiGHSIntExpr.class && e2.getClass()== HiGHSIntExpr.class){
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

    @Override
    public NumExpr sum(double v, NumExpr e) {
        return null;
    }

    @Override
    public NumExpr sum(NumExpr e, double v) {
        return null;
    }

    @Override
    public NumExpr sum(NumExpr e1, NumExpr e2) {
        NumExpr lhs = e1;
        NumExpr rhs = e2;
        if(e1 instanceof NumVar){
            HiGHSNumExpr expr = new HiGHSNumExpr();
            expr.variables.add((NumVar) e1);
            expr.coefficients.add(1.0);
            lhs = expr;
        }
        if(e2 instanceof NumVar){
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


    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public Fragment add(Fragment fragment) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Fragment[] add(Fragment[] fragment) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Fragment remove(Fragment fragment) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Fragment[] remove(Fragment[] fragment) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
    Writes the active model to a .lp file.
    @param name the name of the file to write to
     **/
    public void exportModel(String name){
        //Create new file
        File file = new File(name);
        try {
            FileWriter fileWriter = new FileWriter(file);
            if(this.objective.sense == ObjectiveSense.Minimize) {
                fileWriter.write("Minimize\n");
            }else if(this.objective.sense == ObjectiveSense.Maximize) {
                fileWriter.write("Maximize\n");
            }else{
                throw new HiGHSException("Invalid objective sense: " + this.objective.sense);
            }
            //Write the objective function
            if(this.objective.expr instanceof HiGHSNumExpr expr_cast) {
                for (int i = 0; i < expr_cast.coefficients.size(); i++) {
                    if(i !=0) {

                        if (expr_cast.coefficients.get(i) < 0) {
                            fileWriter.write("- ");
                        } else {
                            fileWriter.write("+ ");
                        }
                    }
                    fileWriter.write(Math.abs(expr_cast.coefficients.get(i)) + " " + expr_cast.variables.get(i).getName() + " ");
                }
            }else if(this.objective.expr instanceof HiGHSIntExpr expr_cast) {
                for (int i = 0; i < expr_cast.coefficients.size(); i++) {
                    if(i !=0) {
                        if (expr_cast.coefficients.get(i) < 0) {
                            fileWriter.write("- ");
                        } else {
                            fileWriter.write("+ ");
                        }
                    }
                    fileWriter.write(Math.abs(expr_cast.coefficients.get(i)) + " " + expr_cast.variables.get(i).getName() + " ");
                }
            }else{
                throw new HiGHSException("Invalid expression type for objective: " + this.objective.expr.getClass());
            }
            fileWriter.write(this.objective.getConstant() + "\n");
            //Write the constraints
            fileWriter.write("Subject To\n");
            for (Constraint constraint : constraints) {
                if(constraint instanceof HiGHSConstraint constraint_cast) {
                    fileWriter.write(constraint_cast.getName() + ": ");
                    if (constraint_cast.lhs instanceof HiGHSNumExpr expr_cast) {
                        for (int i = 0; i < expr_cast.coefficients.size(); i++) {
                            if(i !=0) {
                                if (expr_cast.coefficients.get(i) < 0) {
                                    fileWriter.write("- ");
                                } else {
                                    fileWriter.write("+ ");
                                }
                            }
                            fileWriter.write(Math.abs(expr_cast.coefficients.get(i)) + " " + expr_cast.variables.get(i).getName() + " ");
                        }
                    }else if(constraint_cast.lhs instanceof HiGHSIntExpr expr_cast) {
                        for (int i = 0; i < expr_cast.coefficients.size(); i++) {
                            if(i !=0) {
                                if (expr_cast.coefficients.get(i) < 0) {
                                    fileWriter.write("- ");
                                } else {
                                    fileWriter.write("+ ");
                                }
                            }
                            fileWriter.write(Math.abs(expr_cast.coefficients.get(i)) + " " + expr_cast.variables.get(i).getName() + " ");
                        }
                    }else{
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
                            if(expr_cast.coefficients.get(i) < 0){
                                fileWriter.write("- ");
                            }else{
                                fileWriter.write("+ ");
                            }
                            fileWriter.write(Math.abs(expr_cast.coefficients.get(i)) + " " + expr_cast.variables.get(i).getName() + " ");
                        }
                    }else if(constraint_cast.rhs instanceof HiGHSIntExpr expr_cast) {
                        for (int i = 0; i < expr_cast.coefficients.size(); i++) {
                            if(expr_cast.coefficients.get(i) < 0){
                                fileWriter.write("- ");
                            }else{
                                fileWriter.write("+ ");
                            }
                            fileWriter.write(Math.abs(expr_cast.coefficients.get(i)) + " " + expr_cast.variables.get(i).getName() + " ");
                        }
                    }else {
                        throw new HiGHSException("Invalid expression type for constraint: " + constraint_cast.rhs.getClass());

                    }
                    if (constraint_cast.rhs instanceof HiGHSNumExpr expr_cast) {

                        fileWriter.write(expr_cast.constant + "\n");
                    }else if(constraint_cast.rhs instanceof HiGHSIntExpr expr_cast) {
                        fileWriter.write(expr_cast.constant + "\n");
                    }else{
                        throw new HiGHSException("Invalid expression type for constraint: " + constraint_cast.rhs.getClass());
                    }
                    }
            }
            //Write the bounds
            fileWriter.write("Bounds\n");
            for (NumVar variable : variables) {
                if(variable instanceof HiGHSNumVar var_cast) {
                    if(var_cast.getLB() != 0 && var_cast.getUB() < Integer.MAX_VALUE){
                        fileWriter.write(var_cast.getLB() + " <= " + var_cast.getName() + " <= " + var_cast.getUB() + "\n");
                    }else if(var_cast.getLB() != 0 ) {
                        fileWriter.write(var_cast.getLB() + " <= " + var_cast.getName() + "\n");
                    }else if(var_cast.getUB() != Double.MAX_VALUE && var_cast.getUB() != Integer.MAX_VALUE) {
                        fileWriter.write(var_cast.getName() + " <= " + var_cast.getUB() + "\n");
                    }
                }else if(variable instanceof HiGHSIntVar var_cast) {

                    if(var_cast.getMin() != 0 && var_cast.getMax() < Integer.MAX_VALUE){
                        fileWriter.write(var_cast.getMin() + " <= " + var_cast.getName() + " <= " + var_cast.getMax() + "\n");
                    }else if(var_cast.getMin() != 0) {
                        fileWriter.write(var_cast.getMin() + " <= " + var_cast.getName() + "\n");
                    }else if(var_cast.getMax() != Integer.MAX_VALUE) {
                        fileWriter.write(var_cast.getName() + " <= " + var_cast.getMax() + "\n");
                    }
                }else{
                    throw new HiGHSException("Invalid variable type: " + variable.getClass());
                }
            }
            //Write the variable type section
            fileWriter.write("Generals\n");
            for (NumVar variable : variables) {
                if(variable instanceof HiGHSIntVar var_cast) {
                    fileWriter.write(var_cast.getName() + "\n");
                }
            }
            //Write the binary variable section
            fileWriter.write("Binaries\n");
            for (NumVar variable : variables) {
                if(variable instanceof HiGHSBoolVar var_cast) {
                    fileWriter.write(var_cast.getName() + "\n");
                }
            }
            fileWriter.write("End\n");
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void importSol(String fileName){
        //Read a sol file produced by HiGHS and populate the solution value hashmap
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

    public IntVar boolVar() {
        HiGHSBoolVar var = new HiGHSBoolVar();
        variables.add(var);
        return var;
    }
    public IntVar boolVar(String name) {
        HiGHSBoolVar var = new HiGHSBoolVar();
        var.setName(name);
        variables.add(var);
        return var;
    }

    public IntVar intVar() {
        HiGHSIntVar var = new HiGHSIntVar();
        variables.add(var);
        return var;
    }
    public IntVar intVar(String name) {
        HiGHSIntVar var = new HiGHSIntVar();
        var.setName(name);
        variables.add(var);
        return var;
    }
    public IntVar intVar(int min, int max) {
        HiGHSIntVar var = new HiGHSIntVar();
        var.setMin(min);
        var.setMax(max);
        variables.add(var);
        return var;
    }
    public IntVar intVar(int min, int max, String name) {
        HiGHSIntVar var = new HiGHSIntVar();
        var.setMin(min);
        var.setMax(max);
        var.setName(name);
        variables.add(var);
        return var;
    }


    public NumExpr constant(int i) {
        HiGHSIntExpr expr = new HiGHSIntExpr();
        expr.constant = i;
        return expr;
    }
    public NumExpr constant(double d) {
        HiGHSNumExpr expr = new HiGHSNumExpr();
        expr.constant = d;
        return expr;
    }

    public NumExpr prod(int i, NumExpr numExpr) {
        HiGHSNumExpr expr = new HiGHSNumExpr(numExpr);
        expr.coefficients.replaceAll(v -> v * i);
        return expr;
    }
    public IntExpr prod(int i, IntExpr numVar) {
        HiGHSIntExpr expr = new HiGHSIntExpr(numVar);
        expr.coefficients.replaceAll(v -> v * i);
        return expr;
    }


    public NumExpr prod(double d, IntExpr numVar) {
        HiGHSNumExpr expr = new HiGHSNumExpr(numVar);
        expr.coefficients.replaceAll(v -> v * d);
        return expr;
    }

    public NumExpr prod(double d, NumExpr numVar) {
        HiGHSNumExpr expr = new HiGHSNumExpr(numVar);
        expr.coefficients.replaceAll(v -> v * d);
        return expr;
    }

    public Objective addMaximize(NumExpr objective) {
        this.objective = new HiGHSObjective(objective, ObjectiveSense.Maximize);
        return this.objective;
    }
    public Objective addMaximize(NumExpr objective, String name) {
        this.objective = new HiGHSObjective(objective, ObjectiveSense.Maximize);
        this.objective.setName(name);
        return this.objective;
    }
    public Objective addMinimize(NumExpr objective) {
        this.objective = new HiGHSObjective(objective, ObjectiveSense.Minimize);
        return this.objective;
    }
    public Objective addMinimize(NumExpr objective, String name) {
        this.objective = new HiGHSObjective(objective, ObjectiveSense.Minimize);
        this.objective.setName(name);
        return this.objective;
    }
    public Objective addObjective(ObjectiveSense sense, NumExpr objective) {
        this.objective = new HiGHSObjective(objective, sense);
        return this.objective;
    }
    public Objective addObjective(ObjectiveSense sense, NumExpr objective, String name) {
        this.objective = new HiGHSObjective(objective, sense);
        this.objective.setName(name);
        return this.objective;
    }

    public int getValue(IntVar var){
        return (int) Math.round(solutionValues.get(var));
    }

    public double getValue(NumVar var){
        return solutionValues.get(var);
    }

    public double getObjValue(){
        return objectiveValue;
    }
    public NumVar numVar(int lb, int ub, String name) {
        HiGHSNumVar var = new HiGHSNumVar();
        var.setLB(lb);
        var.setUB(ub);
        var.setName(name);
        variables.add(var);
        return var;
    }

    public NumVar numVar(String name) {
        HiGHSNumVar var = new HiGHSNumVar();
        var.setName(name);
        variables.add(var);
        return var;
    }

    public void solve() {
        //Write to file and call the solver
        exportModel("out.lp");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("highs", "--model_file" , "out.lp", "--solution_file", "out.sol");
            processBuilder.redirectOutput(new File("out.txt"));
            processBuilder.redirectError(new File("error.txt"));
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private class HiGHSConstraint implements Constraint{
        String name;
        NumExpr lhs;
        NumExpr rhs;
        ConstraintType type;
        HiGHSConstraint(NumExpr lhs, NumExpr rhs, ConstraintType type){
            this.lhs = lhs;
            this.rhs = rhs;
            this.type = type;
            this.name = "Constraint_" + constraintCounter++;
        }
        HiGHSConstraint(){
            this.name = "Constraint_" + constraintCounter++;
        }
        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }
    }

    private class HiGHSNumVar implements NumVar{
        String name;
        double lb;
        double ub;
        HiGHSNumVar(){
            this.name = "NumVar_" + varCounter++;
            this.lb =0;
            this.ub = Double.MAX_VALUE;
        }
        @Override
        public double getLB() {
            return this.lb;
        }

        @Override
        public double getUB() {
            return this.ub;
        }

        @Override
        public NumVarType getType() {
            return null;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setLB(double lb) {
            this.lb = lb;
        }

        @Override
        public void setUB(double ub) {
            this.ub=ub;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }
    }

    private class HiGHSIntVar implements IntVar{
        String name;
        int max;
        int min;
        NumVarType type = NumVarType.Int;

        public HiGHSIntVar(){
            this.name = "IntVar_" + varCounter++;
            this.max = Integer.MAX_VALUE;
            this.min = 0;
        }
        public HiGHSIntVar(int min, int max){
            this.name = "IntVar_" + varCounter++;
            this.max = max;
            this.min = min;
        }
        public HiGHSIntVar(int max){
            this.name = "IntVar_" + varCounter++;
            this.max = max;
            this.min = 0;
        }

        @Override
        public int getMax() {
            return max;
        }

        @Override
        public int getMin() {
            return min;
        }

        @Override
        public int setMax(int max) {
            this.max = max;
            return max;
        }

        @Override
        public int setMin(int min) {
            this.min = min;
            return min;
        }

        @Override
        public double getLB() {
            return min;
        }

        @Override
        public double getUB() {
            return max;
        }

        @Override
        public NumVarType getType() {
            return null;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setLB(double lb) {

        }

        @Override
        public void setUB(double ub) {

        }

        @Override
        public void setName(String name) {
            this.name = name;
        }
    }

    private class HiGHSBoolVar extends HiGHSIntVar{
        NumVarType type = NumVarType.Bool;

        public HiGHSBoolVar(){
            this.name = "BoolVar_" + varCounter++;
            this.max = 1;
            this.min = 0;
        }

    }

    private class HiGHSNumExpr implements NumExpr{
        String name;
        List<NumVar> variables;
        List<Double> coefficients;
        Double constant;

        public HiGHSNumExpr(NumExpr expr) {
            name = expr.getName()+ "_" + varCounter++;
            if(expr instanceof HiGHSNumExpr expr_cast) {
                this.coefficients = new ArrayList<>(expr_cast.coefficients);
                this.variables = new ArrayList<>(expr_cast.variables);
                this.constant = expr_cast.constant;
            }
            else if(expr instanceof HiGHSIntExpr expr_cast){
                this.coefficients = new ArrayList<>();
                for (Integer value : expr_cast.coefficients) {
                        this.coefficients.add(value.doubleValue());
                    }
                this.variables = new ArrayList<>(expr_cast.variables);
                this.constant = (double) expr_cast.constant;
            }
            else if(expr instanceof HiGHSIntVar expr_cast){
                this.coefficients = new ArrayList<>();
                this.coefficients.add(1.0);
                this.variables = new ArrayList<>();
                this.variables.add(expr_cast);
                this.constant = 0.0;
            }
            else if(expr instanceof HiGHSNumVar expr_cast){
                this.coefficients = new ArrayList<>();
                this.coefficients.add(1.0);
                this.variables = new ArrayList<>();
                this.variables.add(expr_cast);
                this.constant = 0.0;
            }
            else if(expr instanceof HiGHSBoolVar expr_cast){
                this.coefficients = new ArrayList<>();
                this.coefficients.add(1.0);
                this.variables = new ArrayList<>();
                this.variables.add(expr_cast);
                this.constant = 0.0;
            }
            else{
                throw new HiGHSException("Invalid expression type: " + expr.getClass());
            }
        }

        public HiGHSNumExpr(NumVar var){
            this.name = "NumExpr_" + varCounter++;
            this.variables = new ArrayList<>();
            this.coefficients = new ArrayList<>();
            this.constant = 0.0;
            this.variables.add(var);
            this.coefficients.add(1.0);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }

        HiGHSNumExpr(){
            this.name = "NumExpr_" + varCounter++;
            this.variables = new ArrayList<>();
            this.coefficients = new ArrayList<>();
            this.constant = 0.0;
        }
    }
    private class HiGHSIntExpr implements IntExpr{
        String name;
        List<IntVar> variables;
        List<Integer> coefficients;
        Integer constant;
        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }

        HiGHSIntExpr(){
            this.name = "IntExpr_" + varCounter++;
            this.variables = new ArrayList<>();
            this.coefficients = new ArrayList<>();
            this.constant = 0;
        }
        HiGHSIntExpr(IntExpr expr){
            this.name = expr.getName()+ "_" +varCounter++;
            if(expr instanceof HiGHSIntExpr expr_cast) {
                this.coefficients = new ArrayList<>(expr_cast.coefficients);
                this.variables = new ArrayList<>(expr_cast.variables);
                this.constant = expr_cast.constant;
            }
            if(expr instanceof HiGHSNumExpr expr_cast){
                throw new HiGHSException("Invalid expression type, converting NumExpr to IntExpr is not defined: " + expr.getClass() + ", " + expr_cast.getClass());
            }
        }
    }

    private class HiGHSObjective implements Objective{
        String name;
        NumExpr expr;
        ObjectiveSense sense;

        public HiGHSObjective(NumExpr expr, ObjectiveSense sense) {
            this.expr = expr;
            this.sense = sense;
            this.name = "Objective_" + varCounter++;
        }

        @Override
        public double getConstant() {
            if(expr instanceof HiGHSNumExpr expr_cast) {
                return expr_cast.constant;
            }
            if(expr instanceof HiGHSIntExpr expr_cast) {
                return expr_cast.constant.doubleValue();
            }
            throw new HiGHSException("Invalid expression type for constant: " + expr.getClass());
        }

        @Override
        public NumExpr getExpr() {
            return expr;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }
    }
}
