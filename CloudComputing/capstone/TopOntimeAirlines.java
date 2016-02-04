import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;

// >>> Don't Change
public class TopOntimeAirlines extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new TopOntimeAirlines(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = this.getConf();
        FileSystem fs = FileSystem.get(conf);
        Path tmpPath = new Path("/data/tmp");
        fs.delete(tmpPath, true);

        Job jobA = Job.getInstance(conf, "Delay Count");
        jobA.setOutputKeyClass(Text.class);
        jobA.setOutputValueClass(IntWritable.class);

        jobA.setMapperClass(DelayCountMap.class);
        jobA.setReducerClass(DelayCountReduce.class);

        FileInputFormat.setInputPaths(jobA, new Path(args[0]));
        FileOutputFormat.setOutputPath(jobA, tmpPath);

        jobA.setJarByClass(TopOntimeAirlines.class);
        jobA.waitForCompletion(true);

        Job jobB = Job.getInstance(conf, "Top Ontime Airlines");
        jobB.setOutputKeyClass(Text.class);
        jobB.setOutputValueClass(IntWritable.class);

        jobB.setMapOutputKeyClass(NullWritable.class);
        jobB.setMapOutputValueClass(TextArrayWritable.class);

        jobB.setMapperClass(TopOntimeAirlinesMap.class);
        jobB.setReducerClass(TopOntimeAirlinesReduce.class);
        jobB.setNumReduceTasks(1);

        FileInputFormat.setInputPaths(jobB, tmpPath);
        FileOutputFormat.setOutputPath(jobB, new Path(args[1]));

        jobB.setInputFormatClass(KeyValueTextInputFormat.class);
        jobB.setOutputFormatClass(TextOutputFormat.class);

        jobB.setJarByClass(TopOntimeAirlines.class);
        return jobB.waitForCompletion(true) ? 0 : 1;
    }

    public static String readHDFSFile(String path, Configuration conf) throws IOException{
        Path pt=new Path(path);
        FileSystem fs = FileSystem.get(pt.toUri(), conf);
        FSDataInputStream file = fs.open(pt);
        BufferedReader buffIn=new BufferedReader(new InputStreamReader(file));

        StringBuilder everything = new StringBuilder();
        String line;
        while( (line = buffIn.readLine()) != null) {
            everything.append(line);
            everything.append("\n");
        }
        return everything.toString();
    }

    public static class TextArrayWritable extends ArrayWritable {
        public TextArrayWritable() {
            super(Text.class);
        }

        public TextArrayWritable(String[] strings) {
            super(Text.class);
            Text[] texts = new Text[strings.length];
            for (int i = 0; i < strings.length; i++) {
                texts[i] = new Text(strings[i]);
            }
            set(texts);
        }
    }
// <<< Don't Change

    public static class DelayCountMap extends Mapper<Object, Text, Text, IntWritable> {
        String delimiters = ",";

        @Override
        protected void setup(Context context) throws IOException,InterruptedException {

            Configuration conf = context.getConfiguration();
        }


        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            // TODO
            String line = value.toString();
            StringTokenizer tokenizer = new StringTokenizer(line, this.delimiters);
            int index = 0;
            String airLineID = "";
            while (tokenizer.hasMoreTokens()) {
                String nextToken = tokenizer.nextToken().trim().toLowerCase();
                index++;
                // 2: AirlineID; 12: ArrDelay; 13: Cancelled
                if (index == 2) {
                    if (nextToken.compareToIgnoreCase("AirlineID") == 0) {
                        // Header
                        break;
                    }
                    airLineID = nextToken;
                }
                if (index == 12) {
                    Double count = Double.parseDouble(nextToken);
                    if (count > 15) {
                        // > 15 min
                        context.write(new Text(airLineID), new IntWritable(1));
                        break;
                    }
                }
                if (index == 13 ) {
                    Double count = Double.parseDouble(nextToken);
                    if (count >= 1) {
                        // cancelled
                        context.write(new Text(airLineID), new IntWritable(1));
                        break;
                    }
                }
            }
        }
    }

    public static class DelayCountReduce extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            // TODO
            int sum =0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            context.write(key, new IntWritable(sum));
        }
    }

    public static class TopOntimeAirlinesMap extends Mapper<Text, Text, NullWritable, TextArrayWritable> {
        Integer N;
        // TODO
        private TreeSet<Pair<Integer, String>> countToTitleMap = new TreeSet<Pair<Integer, String>>();
        
        @Override
        protected void setup(Context context) throws IOException,InterruptedException {
            Configuration conf = context.getConfiguration();
            this.N = conf.getInt("N", 10);
        }

        @Override
        public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            // TODO
            Integer count = Integer.parseInt(value.toString());
            String title = key.toString();
            
            countToTitleMap.add(new Pair<Integer, String>(count, title));
            if (countToTitleMap.size() > this.N) {
                countToTitleMap.remove(countToTitleMap.last());
            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            // TODO
            for (Pair<Integer, String> item : countToTitleMap) {
                String[] strings = {item.second, item.first.toString()};
                TextArrayWritable val = new TextArrayWritable(strings);
                context.write(NullWritable.get(), val);
            }
        }
    }

    public static class TopOntimeAirlinesReduce extends Reducer<NullWritable, TextArrayWritable, Text, IntWritable> {
        Integer N;
        // TODO
        private TreeSet<Pair<Integer, String>> countToTitleMap = new TreeSet<Pair<Integer, String>>();
        
        @Override
        protected void setup(Context context) throws IOException,InterruptedException {
            Configuration conf = context.getConfiguration();
            this.N = conf.getInt("N", 10);
        }

        @Override
        public void reduce(NullWritable key, Iterable<TextArrayWritable> values, Context context) throws IOException, InterruptedException {
            // TODO
            for (TextArrayWritable val : values) {
                Text[] pair = (Text[]) val.toArray();
                
                String title = pair[0].toString();
                Integer count = Integer.parseInt(pair[1].toString());
                
                countToTitleMap.add(new Pair<Integer, String>(count, title));
                if (countToTitleMap.size() > this.N) {
                    countToTitleMap.remove(countToTitleMap.last());
                }
            }

            for (Pair<Integer, String> item : countToTitleMap) {
                Text title = new Text(item.second);
                IntWritable value = new IntWritable(item.first);
                context.write(title, value);
            }
        }
    }

}

// >>> Don't Change
class Pair<A extends Comparable<? super A>,
        B extends Comparable<? super B>>
        implements Comparable<Pair<A, B>> {

    public final A first;
    public final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public static <A extends Comparable<? super A>,
            B extends Comparable<? super B>>
    Pair<A, B> of(A first, B second) {
        return new Pair<A, B>(first, second);
    }

    @Override
    public int compareTo(Pair<A, B> o) {
        int cmp = o == null ? 1 : (this.first).compareTo(o.first);
        return cmp == 0 ? (this.second).compareTo(o.second) : cmp;
    }

    @Override
    public int hashCode() {
        return 31 * hashcode(first) + hashcode(second);
    }

    private static int hashcode(Object o) {
        return o == null ? 0 : o.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair))
            return false;
        if (this == obj)
            return true;
        return equal(first, ((Pair<?, ?>) obj).first)
                && equal(second, ((Pair<?, ?>) obj).second);
    }

    private boolean equal(Object o1, Object o2) {
        return o1 == o2 || (o1 != null && o1.equals(o2));
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ')';
    }
}
// <<< Don't Change