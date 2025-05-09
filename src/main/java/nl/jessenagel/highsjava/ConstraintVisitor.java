package nl.jessenagel.highsjava;

public interface ConstraintVisitor extends NumExprVisitor {
    void visit(HiGHSConstraint constraint);
    void visit(HiGHSNumExpr expr);
    void visit(HiGHSIntExpr expr);
    void visit(HiGHSIntVar expr);
    void visit(HiGHSNumVar expr);
}
