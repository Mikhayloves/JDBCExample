package org.example.calculator;

import org.example.cacheProxy.Cachable;

import java.util.List;

public interface Calculator {
    @Cachable
    List<Integer> fibonachi (int i);
}
