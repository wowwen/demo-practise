HyperLogLog 提供不精确的去重计数方案，虽然不精确但是也不是非常不
精确，标准误差是 0.81%，这样的精确度已经可以满足上面的 UV 统计需求了

HyperLogLog 除了上面的 pfadd 和 pfcount 之外，还提供了第三个指令 pfmerge，用于
将多个 pf 计数值累加在一起形成一个新的 pf 值。