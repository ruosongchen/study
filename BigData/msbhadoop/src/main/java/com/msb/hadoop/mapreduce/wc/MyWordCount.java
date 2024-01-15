package com.msb.hadoop.mapreduce.wc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.GenericWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;

public class MyWordCount {
    //bin/hadoop command [genericOptions] [commandOptions]
    //  hadoop jar xxx -D ooxx=ooxx inpath outpath

    //args：2类参数 genericOptions commandOptions
    //人有复杂度：自己分析args数组
    public static void main(String[] args) throws Exception {
        //解析XML配置文件
        Configuration conf = new Configuration(true);

        GenericOptionsParser parser = new GenericOptionsParser(conf, args);//工具类帮我们-D等等属性直接set到conf里，会留下commandOptions
        String[] othargs = parser.getRemainingArgs();

        //让框架知道是windows异构平台运行
        conf.set("mapreduce.app-submission.cross-platform", "true");

        //System.out.println("mapreduce.framework.name:" + conf.get("mapreduce.framework.name"));
        //conf.set("mapreduce.framework.name","local");
        //System.out.println("mapreduce.framework.name:" + conf.get("mapreduce.framework.name"));

        //mapreduce抽象类
        Job job = Job.getInstance(conf);

        //人为干预
        //job.setInputFormatClass(xxoo.class);
        //job.setSortComparatorClass(qwe.class); //自定义比较器
        //默认minSize=1
        //FileInputFormat.setMinInputSplitSize(job,2222);

        job.setJar("D:\\Dev\\bigdata\\msbhadoop\\target\\hadoop-hdfs-1.0-SNAPSHOT.jar");

        job.setJarByClass(MyWordCount.class);
        job.setJobName("mashibing");



        //Path infile = new Path("/data/wc/input");
        Path infile = new Path(othargs[0]);
        TextInputFormat.addInputPath(job,infile);

        //Path outfile = new Path("/data/wc/output");
        Path outfile = new Path(othargs[1]);
        if(outfile.getFileSystem(conf).exists(outfile))  outfile.getFileSystem(conf).delete(outfile,true);
        TextOutputFormat.setOutputPath(job,outfile);

        job.setMapperClass(MyMapper.class);
        //map到reduce数据传递
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setReducerClass(MyReducer.class);

        //job.setNumReduceTasks(2);
        // Submit the job, then poll for progress until the job is complete
        job.waitForCompletion(true);



    }
}
