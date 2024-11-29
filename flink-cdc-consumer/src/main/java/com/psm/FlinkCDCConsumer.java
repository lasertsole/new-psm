package com.psm;

import com.psm.Sink.CustomSink;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
public class FlinkCDCConsumer {
	public static void main(String[] args) throws Exception {
		// 创建 Kafka 生产者
		Properties kafkaProps = new Properties();
		kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, "default-group");
		kafkaProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		kafkaProps.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		kafkaProps.setProperty("topic", "ESCDC");

		FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>(
				kafkaProps.getProperty("topic"),
				new SimpleStringSchema(),
				kafkaProps
		);

		// 设置 Watermark 策略（可选）
		kafkaConsumer.setStartFromEarliest(); // 从最早的偏移量开始消费

		// 创建 Flink 执行环境
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// 添加 Kafka 源
		DataStream<String> stream = env.addSource(kafkaConsumer);
		AllWindowedStream<String, TimeWindow> ws = stream
				.windowAll(ProcessingTimeSessionWindows.withGap(Time.seconds(3))); //会话滑动窗口,间隔3秒没数据会触发窗口计算
		DataStream<String[]> newStream = ws.process(new ProcessAllWindowFunction<String, String[], TimeWindow>() {
			@Override
			public void process(ProcessAllWindowFunction<String, String[], TimeWindow>.Context context, Iterable<String> iterable, Collector<String[]> collector) throws Exception {
				// 将 Iterable<String> 转换为 List<String>
				List<String> list = new ArrayList<>();
				iterable.forEach(list::add);

				// 将 List<String> 转换为 String[]
				String[] array = list.toArray(new String[0]);//不用List为了绕过反射时的包依赖检查
				collector.collect(array);
			}
		});	// 设置窗口大小为5秒
		newStream.addSink(new CustomSink());
		// 启动 Flink 任务
		env.execute("flinkPgCDCConsumer");
	}
}

