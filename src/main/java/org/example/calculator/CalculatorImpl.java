package org.example.calculator;

import java.util.ArrayList;
import java.util.List;

public class CalculatorImpl implements Calculator {
    @Override
    public List<Integer> fibonachi(int i) {
        if(i < 0 || i > 46){
            throw new IllegalArgumentException("Число должно быть больше 0 и меньше 46");
        }
        List<Integer> result = new ArrayList<>();
        result.add(0);
        if(i >= 1) {
            result.add(1);
        }
        for (int j = 2; j <= i; j++) {
            result.add(result.get(j - 1) + result.get(j - 2));
        }
        return result;
    }
}
