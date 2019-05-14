# 用户行为 bloomfilter

* 通过配置来控制初始化bloomfilter的预存数量和个数
* 每个ActionBloomFilterGroup中包含两个group，一组存放当前月的数据，一组存放上个月的数据
* 定时任务-将所有内存中的bloomfilter持久化到文件中，启动的时候也从文件中加载
* 定时任务-每个月1日创建一个新的group组，将当前group中bloomfilter移动到上个月的group中


当前测试结果：10亿key/group，0.001 误差率，会占用4G的内存，考虑bloomfilter的swap过程，应该会占用6G左右。

* bak:
    还需要考虑服务其他部分的内存;网上有评测，guava的bloomfilter所占的内存会在写入值之后发生变化，需要对其测试