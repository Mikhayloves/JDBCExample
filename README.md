# Домашнее задание №14
_______________________________
## Тема задания:
Разработать продвинутый кэш, который помнит о кэшированных данных после перезапуска приложения. 
```java
@interface Cachable {
Class value() ;
}

class Calculator {
@Cachable(H2DB.class) 
public List fibonachi (int i) {
// алгоритм
}
```
_______________________________
Что было реализованно:
В коде был реализован прокси-обработчик кэша для вычислений чисел Фибоначчи. 
Класс [**`FibbonachiCacheProxyHandler`**](https://github.com/Mikhayloves/JDBCExample/blob/main/src/main/java/org/example/cacheProxy/FibbonachiCacheProxyHandler.java) используется для кэширования результатов вычислений чисел Фибоначчи с использованием аннотации @Cachable.

Вот подробное объяснение, что делает данный код:

1. Конструктор FibbonachiCacheProxyHandler:

   ```java
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
   ```

   - Конструктор принимает два параметра: target, представляющий целевой объект, и dao, представляющий объект для взаимодействия с базой данных (DAO).
   - Инициализируются переменные dao, target и cache.
   - Загружаются данные из базы данных и сортируются.
   - Создаются и кэшируются числа Фибоначчи из загруженных данных.
   - Обновляется значение lastArgument, указывающее на последний закэшированный аргумент.

2. Метод cacheResult:

   ```java
   private void cacheResult(List<Integer> fibonachi) {
       List<CalcModel> models = new ArrayList<>();
       for (int i = lastArgument + 1; i < fibonachi.size(); i++) {
           models.add(new CalcModel(i, fibonachi.get(i)));
           cache.put(i, fibonachi.subList(0, i + 1));
       }
       lastArgument = fibonachi.size() - 1;
       dao.saveAll(models);
   }
   ```

   - Принимает список чисел Фибоначчи и кэширует их результаты.
   - Создает новые экземпляры CalcModel и сохраняет их в базе данных.
   - Обновляет кэш и значение lastArgument.

3. Метод invoke:

   ```java
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
  ```

   - Этот метод вызывается при каждом вызове метода на прокси-объекте.
   - Проверяет, присутствует ли аннотация @Cachable на методе. Если нет, просто вызывает метод на целевом объекте.
   - Если запрашиваемый аргумент уже находится в кэше, возвращает закэшированный результат.
   - В противном случае вызывает метод на целевом объекте, кэширует результат и возвращает его.

Данный код эффективно кэширует результаты вычислений чисел Фибоначчи, чтобы улучшить производительность за счет повторного использования уже вычисленных значений.
