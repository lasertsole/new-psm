package com.psm;

import com.ververica.cdc.connectors.postgres.PostgreSQLSource;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import java.util.Properties;

public class FlinkCDCApplication {
	public static void main(String[] args) throws Exception {

		String fileName = args[0];
		ParameterTool parameterTool = ParameterTool.fromPropertiesFile(fileName);
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.disableOperatorChaining();
		env.enableCheckpointing(5000L);
		//指定 CK 的一致性语义
		env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
		//设置任务关闭的时候保留最后一次 CK 数据
		env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
		// 指定从 CK 自动重启策略
		env.setRestartStrategy(RestartStrategies.fixedDelayRestart(Integer.MAX_VALUE, 2000L));
		//设置状态后端
		env.setStateBackend(new FsStateBackend("hdfs://ip:8020/../.."));
		//设置访问 HDFS 的用户名
		System.setProperty("HADOOP_USER_NAME", "hadoop");


		Properties properties = new Properties();
		properties.setProperty("snapshot.mode", "initial");
		properties.setProperty("debezium.slot.name", "pg_cdc");
		properties.setProperty("debezium.slot.drop.on.stop", "true");
		properties.setProperty("include.schema.changes", "true");

		SourceFunction<String> sourceFunction = PostgreSQLSource.<String>builder()
				.hostname("192.168.1.xxx")
				.port(5432)
				.database("databseName") // monitor postgres database
				.schemaList("schemaName")  // monitor inventory snachema
				.tableList("schemaName.table1,scheamName.tabl2,...") // monitor products table
				.username("userName")
				.password("password")
				.decodingPluginName("pgoutput")
				.deserializer(new CustomerDeserialization()) // converts SourceRecord to JSON String
				.debeziumProperties(properties)
				.build();

		DataStreamSource<String> pgDataStream =
				env
						.addSource(sourceFunction)
						.setParallelism(1); // use parallelism 1 for sink to keep message ordering

		// 设置kafka配置
		Properties kafkaProps = new Properties();
		kafkaProps.setProperty("bootstrap.servers","ip1:9092");
		kafkaProps.setProperty("transaction.max.timeout.ms","90000");
//         sink到kafka
		FlinkKafkaProducer flinkKafkaProducer = new FlinkKafkaProducer<>("topicName"), new SimpleStringSchema(), kafkaProps);
		pgDataStream.addSink(flinkKafkaProducer).name("sink2Kafka");

		env.execute("pg_cdc job");

	}
}

