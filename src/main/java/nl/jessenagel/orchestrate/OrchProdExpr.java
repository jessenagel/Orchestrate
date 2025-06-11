package nl.jessenagel.orchestrate;

import java.util.ArrayList;
import java.util.List;

public class OrchProdExpr implements NumExpr {
    String name;
    List<NumExpr> exprs;

    public OrchProdExpr(List<NumExpr> exprs) {
        this.exprs = exprs;
        this.name = "ProdExpr_" + OrchCounter.getNextVarCounter();
    }
    public OrchProdExpr(String name, List<NumExpr> exprs) {
        this.name = name;
        this.exprs = exprs;
    }
    public OrchProdExpr(String name) {
        this.name = name;
        this.exprs =new ArrayList<>();

    }
    public OrchProdExpr() {
        this.name = "ProdExpr_" + OrchCounter.getNextVarCounter();
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
