package nl.jessenagel.highsjava;

public interface Modeler extends Fragment, Model{
    Constraint addEq(NumExpr lhs, NumExpr rhs);
    Constraint addLe(NumExpr lhs, NumExpr rhs);
    Constraint addGe(NumExpr lhs, NumExpr rhs);
    IntExpr sum(int v, IntExpr e);
    IntExpr sum( IntExpr e, int v);
    IntExpr sum(IntExpr e1, IntExpr e2);
    NumExpr sum(double v, NumExpr e);
    NumExpr sum( NumExpr e, double v);
    NumExpr sum(NumExpr e1, NumExpr e2);

}
