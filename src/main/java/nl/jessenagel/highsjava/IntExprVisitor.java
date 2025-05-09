package nl.jessenagel.highsjava;

public interface IntExprVisitor extends NumExprVisitor {
    void visit(HiGHSIntExpr expr);
    void visit(HiGHSIntVar expr);

}
