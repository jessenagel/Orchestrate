package nl.jessenagel.highsjava;

import java.util.ArrayList;
import java.util.List;

public class HiGHSProdExpr implements NumExpr {
    String name;
    List<NumExpr> exprs;

    public HiGHSProdExpr(List<NumExpr> exprs) {
        this.exprs = exprs;
        this.name = "ProdExpr_" + HiGHSCounter.getNextVarCounter();
    }
    public HiGHSProdExpr(String name, List<NumExpr> exprs) {
        this.name = name;
        this.exprs = exprs;
    }
    public HiGHSProdExpr(String name) {
        this.name = name;
        this.exprs =new ArrayList<>();

    }
    public HiGHSProdExpr() {
        this.name = "ProdExpr_" + HiGHSCounter.getNextVarCounter();
        this.exprs = new ArrayList<>();
    }

    @Override
    public void accept(NumExprVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
