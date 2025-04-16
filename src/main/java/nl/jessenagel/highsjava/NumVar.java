package nl.jessenagel.highsjava;

public interface NumVar extends NumExpr  {
    double getLB();
    double getUB();
    NumVarType getType();
    String getName();
    void setLB(double lb);
    void setUB(double ub);
    void setName(String name);
}
