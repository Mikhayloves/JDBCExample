package org.example;


import org.example.cacheProxy.FibbonachiCacheProxy;
import org.example.calculator.Calculator;
import org.example.calculator.CalculatorImpl;
import org.example.dao.CalcModelDao;
import org.example.dao.JdbcCalcModelDao;
import org.example.dao.JdbcCalcModelMapper;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        new JdbcCalcModelDao(new JdbcCalcModelMapper()).clearAll();
        Calculator calculator = FibbonachiCacheProxy.cached(new CalculatorImpl(),
                new JdbcCalcModelDao(new JdbcCalcModelMapper()));
        calculator.fibonachi(40);
        calculator.fibonachi(30);
        calculator.fibonachi(43);

    }
}