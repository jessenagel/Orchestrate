package nl.jessenagel.orchestrate;

public class OrchIntExprVisitor implements IntExprVisitor {
    private final OrchIntExpr target;

    public OrchIntExprVisitor(OrchIntExpr target) {
        this.target = target;
    }


    @Override
    public void visit(OrchNumExpr expr) {
        throw new OrchException("Cannot visit OrchNumExpr in OrchIntExprVisitor");
    }

    @Override
    public void visit(OrchIntExpr expr) {
        target.variables =expr.variables.clone();
        target.coefficients = expr.coefficients.clone();
        target.constant = expr.constant;
        target.numberOfVariables = expr.numberOfVariables;
    }

    @Override
    public void visit(OrchIntVar expr) {
        target.variables = new int[1];
        target.coefficients = new double[1];
        target.variables[0] = expr.getIndex();
        target.coefficients[0] = 1.0;
        target.constant = 0;
        target.numberOfVariables = 1;
    }

    @Override
    public void visit(OrchNumVar expr) {
        throw new OrchException("Cannot visit OrchNumExpr in OrchIntExprVisitor");

    }

    @Override
    public void visit(OrchConstraint orchConstraint) {
        throw new OrchException("Cannot visit OrchNumExpr in OrchIntExprVisitor");
    }

    @Override
    public void visit(OrchSumExpr orchSumExpr) {
        throw new OrchException("Cannot visit OrchSumExpr in OrchIntExprVisitor");

    }

}
