package com.psm;

import com.psm.utils.CustomDeserialization;
import com.ververica.cdc.connectors.postgres.PostgreSQLSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.nio.file.Paths;
import java.util.Properties;

@Slf4j
public class FlinkCDCProducer {
	public static void main(String[] args) throws Exception {
		Properties properties =new Properties();
		//这里设置全量还是增量该属性是 never不执行初始一致性快照，但是会同步后续数据库的更改记录 默认是全量+增量 initial
		properties.setProperty("snapshot.mode","initial");
		//获取配置文件信息
		SourceFunction<String> sourceFunction = PostgreSQLSource.<String>builder()
				.hostname("localhost")
				.port(5432)
				.database("psm")
				.schemaList("public")
				.tableList("public.tb_users", "public.tb_3d_models")
				.username("replicator")
				.password("XiaoDaoZei990508*")
				.decodingPluginName("pgoutput")
				.debeziumProperties(properties)
				.deserializer(new CustomDeserialization())
				.build();

		Configuration config = new Configuration();
		// 启动还原点end
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(config);
		env.enableCheckpointing(5000);
		env.getCheckpointConfig().setCheckpointTimeout(10000L);
		env.getCheckpointConfig().setCheckpointStorage("file:///" + Paths.get("..", "cdcCheckpoints").toAbsolutePath().toString());
		env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
		env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);//限制最大checkpoint并发

		DataStreamSource<String> stringDataStreamSource = env.addSource(sourceFunction);

		// 创建 Kafka 生产者
		Properties kafkaProps = new Properties();
		kafkaProps.setProperty("bootstrap.servers", "localhost:9092");
		kafkaProps.setProperty("topic", "ESCDC");

		FlinkKafkaProducer<String> kafkaProducer = new FlinkKafkaProducer<>(
				kafkaProps.getProperty("topic"),
				new SimpleStringSchema(),
				kafkaProps
		);

		// 将数据流发送到 Kafka
		stringDataStreamSource.addSink(kafkaProducer);

		stringDataStreamSource.print();
		env.execute("flinkPgCDCProducer");
	}
}

