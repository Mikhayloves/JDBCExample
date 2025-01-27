package org.example.cacheProxy;

import org.example.calculator.Calculator;
import org.example.dao.CalcModelDao;

import java.lang.reflect.Proxy;

public class FibbonachiCacheProxy {
    public static Calculator cached(Calculator calculator, CalcModelDao dao) {
        return (Calculator) Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class[]{Calculator.class},
                new FibbonachiCacheProxyHandler(calculator,dao));
    }
}
