package practice;

import java.io.*;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
//import org.apache.hadoop.mapred.JobConfigurable;
import org.apache.hadoop.util.*;

               //Tool interface supports the handling of generic hadoop command-line options
public class WordCountV2 extends Configured implements Tool { 
   //Mapper interface maps input key/value pairs to a set of intermediate key/value pairs
	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable>{	
		private boolean caseSensitive = true;
		private String inputFile;
		
		//JobConf pass the Mapper implementations for the job via JobConfigurable.configure(JobConf) method 
		//and override it to initialize themselves
		public void configure(JobConf job){  // how applications can access configuration parameters in the configure method of the Mapper and Reducer implementations
			caseSensitive = job.getBoolean("wordcountv2.case.sensitive", true);
			inputFile = job.get("map.input.file");
			
			if(job.getBoolean("wordcountv2.skip.patterns",  false)){
				Path[] patternsFiles = new Path[0];
				try{
					//DistributedCache to cache files needed by applications
					patternsFiles = DistributedCache.getLocalCacheFiles(job);
				} catch (IOException ioe) {
					System.err.println("Caught exception while getting cached files: " + StringUtils.stringifyException(ioe));
				}
				
				for(Path patternsFile : patternsFiles){
					parseSkipFile(patternsFile);
				}
			}
		}
		
		private Set<String> patternsToSkip = new HashSet<String>();

		private void parseSkipFile(Path patternsFile){
			try{
				BufferedReader fis = new BufferedReader(new FileReader(patternsFile.toString()));
				String pattern = null;
				while( (pattern = fis.readLine()) != null ) {
					patternsToSkip.add(pattern);
				}
			} catch (IOException ioe){
				System.err.println("Caught exception while parsing the cached file '" + patternsFile + "' : " + StringUtils.stringifyException(ioe));
			}
		}
		
		//Counters represent global counters, defined either by the MapReduce framework or applications
		static enum Counters { INPUT_WORDS }

		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		private long numRecords = 0;
		
		//the framework then calls map for each key/value pair in the InputSplit for that task
		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
			String line = (caseSensitive) ? value.toString() : value.toString().toLowerCase();
			
			for(String pattern : patternsToSkip){
				line = line.replaceAll(pattern, "");
			}
				
			StringTokenizer tokenizer = new StringTokenizer(line);
			while(tokenizer.hasMoreTokens()){
				word.set(tokenizer.nextToken());
				//output pairs are collected with calls to OutputCollector.collect(WritableComparable, Writable)
				output.collect(word, one); 
				//Application use the Reporter to report progress, set application-level status messages and update Counters
				reporter.incrCounter(Counters.INPUT_WORDS, 1); 
			}
			
			if( (++numRecords % 100) == 0 ) {
				reporter.setStatus("Finished processing " + numRecords + " records " + "from the input file: " + inputFile);
			}
		}
		
	}

	//The framework then calls reduce(WritableComparable, Iterator, OutputCollector, Reporter) method for each <key, (list of values)> pair in the grouped inputs
	public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
		//reduce(WritableComparalb, Iterator, OutputCollector, Reporter) method is called for each <key, (list of values)> pair in the grouped inputs
		public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
			int sum = 0;
			while(values.hasNext()){
				sum += values.next().get();
			}
			//the output of the reduce task is typically written to the FileSystem via OutputCollector.collect(WritableComparable, Writable)
			output.collect(key, new IntWritable(sum));
		}
	}
	
	//Tool interface is the standard for any MapReduce tool or application
	//The application should delegate the handling of standard command-line options to GenericOptionsParser via ToolRunner.run(Tool, String[])
	public int run(String[] args) throws Exception { //the utility of the Tool interface and the GenericOptionsParser to handle generic Hadoop command-line options
		JobConf conf = new JobConf(getConf(), WordCountV2.class);
	    conf.setJobName("wordcountV2");
		
	    conf.setOutputKeyClass(Text.class);
	    conf.setOutputValueClass(IntWritable.class);
	    
	    conf.setMapperClass(Map.class);
	    conf.setCombinerClass(Reduce.class);
	    conf.setReducerClass(Reduce.class);
	    
	    conf.setInputFormat(TextInputFormat.class);
	    conf.setOutputFormat(TextOutputFormat.class);
	    
	    List<String> other_args = new ArrayList<String>();
	    for(int i = 0; i < args.length; ++i){
	    	if("-skip".equals(args[i])){
	    		//DistributedCache distributes read-only data needed by the jobs. 
	    		DistributedCache.addCacheFile(new Path(args[++i]).toUri(), conf);
	    		conf.setBoolean("wordcountv2.skip.patterns", true);
	    	} else {
	    		other_args.add(args[i]);
	    	}
	    }
	    
	    FileInputFormat.setInputPaths(conf, new Path(other_args.get(0)));
	    FileOutputFormat.setOutputPath(conf, new Path(other_args.get(1)));
	    
	    JobClient.runJob(conf); //submits the job and returns only after the job has completed
	    return 0;
	}
	
	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new WordCountV2(), args);
		System.exit(res);
	}
}

