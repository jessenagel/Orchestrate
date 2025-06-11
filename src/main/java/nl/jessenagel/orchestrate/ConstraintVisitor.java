package nl.jessenagel.orchestrate;

public interface ConstraintVisitor extends NumExprVisitor {
    void visit(OrchConstraint constraint);
    void visit(OrchIntExpr expr);
    void visit(OrchIntVar expr);
    void visit(OrchNumVar expr);
}
