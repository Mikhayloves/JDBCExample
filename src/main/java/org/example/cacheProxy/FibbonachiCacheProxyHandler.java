package org.example.cacheProxy;

import org.example.dao.CalcModelDao;
import org.example.model.CalcModel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FibbonachiCacheProxyHandler implements InvocationHandler {
    private Map<Integer, List<Integer>> cache;
    private final CalcModelDao dao;
    private int lastArgument;
    private Object target;


    public FibbonachiCacheProxyHandler(Object target, CalcModelDao dao) {
        this.dao = dao;
        this.target = target;
        List<CalcModel> models = dao.findAllSortedByArgument();
        List<Integer> fibonachi = new ArrayList<>();
        cache = new HashMap<>();
        for (int i = 0; i < models.size(); i++) {
            fibonachi.add(models.get(i).getResult());
            cache.put(i, new ArrayList<>(fibonachi));
        }
        lastArgument = models.size() - 1;
    }

    private void cacheResult(List<Integer> fibonachi) {
        List<CalcModel> models = new ArrayList<>();
        for (int i = lastArgument + 1; i < fibonachi.size(); i++) {
            models.add(new CalcModel(i, fibonachi.get(i)));
            cache.put(i, fibonachi.subList(0, i + 1));
        }
        lastArgument = fibonachi.size() - 1;
        dao.saveAll(models);
    }


    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!method.isAnnotationPresent(Cachable.class)) {
            return method.invoke(target, args);
        }
        if (cache.containsKey((int) args[0])) {
            return cache.get((int) args[0]);
        }
        List<Integer> result = (List<Integer>) method.invoke(target, args);
        cacheResult(result);
        return result;
    }
}
