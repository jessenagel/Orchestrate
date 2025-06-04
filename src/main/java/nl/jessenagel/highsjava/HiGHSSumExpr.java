package nl.jessenagel.highsjava;

import java.util.ArrayList;
import java.util.List;

public class HiGHSSumExpr implements NumExpr {
    String name;
    List<NumExpr> exprs;

    public HiGHSSumExpr(String name, List<NumExpr> exprs) {
        this.name = name;
        this.exprs = exprs;
    }
    public HiGHSSumExpr(String name) {
        this.name = name;
        this.exprs =new ArrayList<>();
    }

    public HiGHSSumExpr() {
        this.name = "SumExpr_" + HiGHSCounter.getNextVarCounter();
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
}
