package com.msb.hadoop.mapreduce.wc;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class MyMapper extends Mapper<Object, Text,Text,IntWritable> {

    //hadoop框架中，它是一个分布式、数据、序列化、反序列化
    //hadoop有自己一套可以序列化、反序列化
    //或者自己开发类型必须，实现序列化。反序列化接口，实现比较器
    //排序 -》 这个世界有2种顺序 8、11, 字典序和数值序列
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
    //hello hadoop 1
    //hello hadoop 2
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken());
        context.write(word, one);
      }
    }

}
