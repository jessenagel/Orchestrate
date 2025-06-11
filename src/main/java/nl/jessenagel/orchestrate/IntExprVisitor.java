package nl.jessenagel.orchestrate;

public interface IntExprVisitor extends NumExprVisitor {
    void visit(OrchIntExpr expr);
    void visit(OrchIntVar expr);

}
