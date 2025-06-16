package nl.jessenagel.orchestrate;

import java.util.ArrayList;
import java.util.List;

public class OrchSumExpr implements NumExpr, IntExpr{
    String name;
    List<NumExpr> exprs;

    public OrchSumExpr(String name, List<NumExpr> exprs) {
        this.name = name;
        this.exprs = exprs;
    }
    public OrchSumExpr(String name) {
        this.name = name;
        this.exprs =new ArrayList<>();
    }

    public OrchSumExpr() {
        this.name = "SumExpr_" + OrchCounter.getNextVarCounter();
        this.exprs = new ArrayList<>();
    }
    @Override
    public void accept(NumExprVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < exprs.size(); i++) {
            result.append(exprs.get(i).toString());
        }
        return result.toString();
    }

    @Override
    public void accept(IntExprVisitor visitor) {
        visitor.visit(this);
    }
}
