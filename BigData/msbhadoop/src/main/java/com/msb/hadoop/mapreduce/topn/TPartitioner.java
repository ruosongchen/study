package com.msb.hadoop.mapreduce.topn;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class TPartitioner extends Partitioner<TKey, IntWritable> {

    @Override
    public int getPartition(TKey key, IntWritable value, int numPartitions) {
        //1.不能太复杂
        //按 年、月分区 -> 分区 > 分组   按年分区！！！！！
        //分区器潜台词：满足 相同的key获得相同的分区号就可以！

        return key.getYear() % numPartitions;   //数据倾斜
    }
}
