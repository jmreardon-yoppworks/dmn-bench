# DMN Benchmarks

Some benchmarking code to assess KIE DMN performance.

To run list filtering benches, use:
```bash
Jmh/run -i 10 -wi 10 -f1 -t1 ".*DmnListFilterBench"
```


## Last Run Results

On 9.44.0.Final, with lists of 10 elements:

This benchmark demonstrates the performance of various list filter expressions. Expressions that use names outside the list item's root context have high overhead.

```
[info] Benchmark                               Mode  Cnt    Score   Error   Units
[info] DmnListFilterBench.evaluate1Function   thrpt   10   15.985 ± 0.441  ops/ms
[info] DmnListFilterBench.evaluate2Functions  thrpt   10    8.790 ± 0.189  ops/ms
[info] DmnListFilterBench.evaluateBool        thrpt   10  127.600 ± 1.373  ops/ms
[info] DmnListFilterBench.evaluateBoolItem    thrpt   10   10.472 ± 0.192  ops/ms
```
