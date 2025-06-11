package nl.jessenagel.orchestrate;

public interface NumExprVisitor {
    void visit(OrchNumExpr expr);
    void visit(OrchIntExpr expr);
    void visit(OrchIntVar expr);
    void visit(OrchNumVar expr);
    void visit(OrchConstraint orchConstraint);
    void visit(OrchSumExpr orchSumExpr);
    void visit(OrchProdExpr orchProdExpr);
}
