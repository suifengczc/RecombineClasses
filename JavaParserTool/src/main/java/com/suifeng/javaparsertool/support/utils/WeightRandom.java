package com.suifeng.javaparsertool.support.utils;

import java.util.ArrayList;

public class WeightRandom {
    private ArrayList<WeightUnit> data;
    private long sum = 0;

    public WeightRandom(ArrayList<WeightUnit> units) {
        data = units;
        initData();
    }

    private void initData() {
        for (WeightUnit unit : this.data) {
            sum += unit.weight;
            unit.sumWeight = sum;
        }
    }

    public WeightUnit getRandomUnit() {
        int length = data.size();
        float v = RandomUtil.randFloat();
        long value = (long) (v * sum) + 1L;

        int start = 0;
        int end = length - 1;
        int middle = (start + end) / 2;
        int rst = 0;
        while (end >= start) {
            if (middle == 0 || start == end) {
                rst = middle;
                break;
            }
            WeightUnit unitAfter = data.get(middle);
            WeightUnit unitBefore = data.get(middle - 1);
            if (unitAfter.sumWeight > value) {
                if (unitBefore.sumWeight < value) {
                    rst = middle;
                    break;
                }
                end = middle - 1;
                middle = (start + end) / 2;
            } else if (unitAfter.sumWeight < value) {
                start = middle + 1;
                middle = (start + end) / 2;
            } else {
                rst = middle;
                break;
            }
        }
        return data.get(rst);
    }

    public static void main(String[] args) {
        ArrayList<WeightUnit> data = new ArrayList<>();
        data.add(new WeightUnit("a", 1));
        data.add(new WeightUnit("b", 2));
        data.add(new WeightUnit("c", 7));
        WeightRandom weightRandom = new WeightRandom(data);
        int aCount = 0;
        int bCount = 0;
        int cCount = 0;
        for (int i = 0; i < 1000; i++) {
            WeightUnit randomUnit = weightRandom.getRandomUnit();
            String s = randomUnit.unit.toString();
            if ("a".equals(s)) {
                aCount++;
            } else if ("b".equals(s)) {
                bCount++;
            } else if ("c".equals(s)) {
                cCount++;
            }
        }
        System.out.println("a = " + aCount + " b = " + bCount + " c = " + cCount);

    }

}
