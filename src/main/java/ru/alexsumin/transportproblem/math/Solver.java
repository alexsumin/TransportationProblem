package ru.alexsumin.transportproblem.math;

import java.util.ArrayList;
import java.util.List;


public class Solver {


    List shopNeeds = new ArrayList();
    List storageStock = new ArrayList();
    List costTable = new ArrayList();
    MathCore core;


    public void setShopNeeds(List shopNeeds) {
        this.shopNeeds = shopNeeds;
    }

    public void setStorageStock(List storageStock) {
        this.storageStock = storageStock;
    }

    public void setCostTable(List costTable) {
        this.costTable = costTable;
    }

    public int getCost() {
        core = new MathCore(shopNeeds, storageStock, costTable);
        core.createBasicRoutes();
        return core.getCurrentCost();
    }

    public int[][] calcNW() {
        core = new MathCore(shopNeeds, storageStock, costTable);
        return core.createBasicRoutes();

    }

    public List solveTask(int maxIterations) {
        List result = new ArrayList();
        MathCore core = new MathCore(shopNeeds, storageStock, costTable);

        /*Make our task closed*/
        if (core.makeClosed()) {
            //System.out.println("Транспортная задача закрыта изначально");
        } else {
            //System.out.println("Транспортная задача закрыта в процессе решения");
        }

        core.createBasicRoutes();

        core.getCurrentCost();

        core.checkDegeneracy();

        for (int i = 0; i < maxIterations; i++) {
            core.checkDegeneracy();
            core.calcPotintials();
            core.getStockPotentials();
            core.getOrdersPotentials();
            core.calcDeltas();
            core.getMinimalDelta();

            if (core.getMinimalDelta() >= 0) {

                result.add(core.getCurrentCost());
                result.add(core.getCurrentRoutes());
                result.add(true);
                return result;
            }

            List<int[]> cycle = core.getCycle(core.getMinimalDeltaCoords());
            core.redistribute();
            core.getCurrentCost();


        }

        result.add(core.getCurrentCost());
        result.add(core.getCurrentRoutes());
        result.add(false);
        return result;
    }

}
