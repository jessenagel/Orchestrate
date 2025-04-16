package nl.jessenagel.highsjava;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HiGHSTest {

    @Test
    void exportModel() {
        HiGHS highs = new HiGHS();
        //Declare variables x y z
        NumVar x1 = highs.numVar( 0, 40,"x1");
        NumVar x2 = highs.numVar( "x2");
        NumVar x3 = highs.numVar( "x3");
        IntVar x4 = highs.intVar( 2, 3,"x4");

        System.out.println(x1.getName());
        //Declare constraints
        NumExpr lhs1 = highs.constant(0);
        lhs1 = highs.sum(lhs1, highs.prod(-1,x1));
        lhs1 = highs.sum(lhs1, x2);
        lhs1 = highs.sum(lhs1, x3);
        lhs1 = highs.sum(lhs1, highs.prod(10,x4));
        highs.addLe(lhs1,highs.constant(20)).setName("c1");

        NumExpr lhs2 = highs.constant(0);
        lhs2 = highs.sum(lhs2, x1);
        lhs2 = highs.sum(lhs2, highs.prod(-3,x2));
        lhs2 = highs.sum(lhs2, x3);
        highs.addLe(lhs2, highs.constant(30));

        NumExpr lsh3 = highs.constant(0);
        lsh3 = highs.sum(lsh3, x2);
        lsh3 = highs.sum(lsh3, highs.prod(-3.5,x4));
        highs.addLe(lsh3, highs.constant(30));
        //Add objective function
        NumExpr objective = highs.constant(0);
        objective = highs.sum(objective,x1);
        objective = highs.sum(objective, highs.prod(2,x2));
        objective = highs.sum(objective, highs.prod(3,x3));
        objective = highs.sum(objective, x4);
        highs.addMaximize(objective);
        highs.exportModel("test.lp");
    }

    @Test
    void solveLP(){
        HiGHS highs = new HiGHS();
        //Declare variables x y z
        NumVar x = highs.numVar( "x");
        NumVar y = highs.numVar( "y");


        //Declare constraints
        NumExpr lhs1 = highs.constant(0);
        lhs1 = highs.sum(lhs1, x);
        lhs1 = highs.sum(lhs1, highs.prod(2,y));
        highs.addLe(lhs1,highs.constant(14)).setName("c1");

        NumExpr lhs2 = highs.constant(0);
        lhs2 = highs.sum(lhs2, highs.prod(3,x));
        lhs2 = highs.sum(lhs2, highs.prod(-1,y));
        highs.addGe(lhs2, highs.constant(0));

        NumExpr lsh3 = highs.constant(0);
        lsh3 = highs.sum(lsh3, x);
        lsh3 = highs.sum(lsh3, highs.prod(-1,y));
        highs.addLe(lsh3, highs.constant(2));
        //Add objective function
        NumExpr objective = highs.constant(0);
        objective = highs.sum(objective, highs.prod(3,x));
        objective = highs.sum(objective, highs.prod(4,y));
        highs.addMaximize(objective);
        highs.exportModel("test.lp");
        //Solve the model
        highs.solve();
        //Get the solution
        highs.importSol("out.sol");
        //Check the solution
        assertEquals(6.0, highs.getValue(x), 0.01);
        assertEquals(4.0, highs.getValue(y), 0.01);
        assertEquals(34.0, highs.getObjValue(), 0.01);

    }
}