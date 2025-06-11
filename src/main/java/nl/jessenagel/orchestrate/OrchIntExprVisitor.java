package nl.jessenagel.orchestrate;

import java.util.LinkedHashMap;

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
        target.variablesAndCoefficients = new LinkedHashMap<>(expr.variablesAndCoefficients);
        target.constant = expr.constant;
    }

    @Override
    public void visit(OrchIntVar expr) {
        target.variablesAndCoefficients = new LinkedHashMap<>();
        target.variablesAndCoefficients.put(expr, 1);
        target.constant = 0;
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

    @Override
    public void visit(OrchProdExpr orchProdExpr) {
        throw new OrchException("Cannot visit OrchProdExpr in OrchIntExprVisitor");
    }
}
