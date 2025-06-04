package nl.jessenagel.highsjava;

public interface NumExprVisitor {
    void visit(HiGHSNumExpr expr);
    void visit(HiGHSIntExpr expr);
    void visit(HiGHSIntVar expr);
    void visit(HiGHSNumVar expr);
    void visit(HiGHSConstraint hiGHSConstraint);
    void visit(HiGHSSumExpr hiGHSSumExpr);
    void visit(HiGHSProdExpr hiGHSProdExpr);
}
