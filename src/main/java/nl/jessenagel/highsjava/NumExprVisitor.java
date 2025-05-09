package nl.jessenagel.highsjava;

public interface NumExprVisitor {
    void visit(HiGHSNumExpr expr);
    void visit(HiGHSIntExpr expr);
    void visit(HiGHSIntVar expr);
    void visit(HiGHSNumVar expr);
}
