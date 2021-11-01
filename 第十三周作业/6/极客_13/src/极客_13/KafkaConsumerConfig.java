package ����_13;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
 
import java.util.HashMap;
import java.util.Map;
 


public class KafkaConsumerConfig {
 

    private KafkaConsumerProps kafkaConsumerProps;
    
    private static final String GROUP0_ID = "group0";
    private static final String GROUP1_ID = "group1";
 

    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory0() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory0());
        //��Ӧtopic������
        factory.setConcurrency(3);
        //����Ϊ�������ѣ�ÿ������������Kafka���ò���������ConsumerConfig.MAX_POLL_RECORDS_CONFIG
        factory.setBatchListener(true);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }
 
    public ConsumerFactory<String, String> consumerFactory0() {
        Map<String, Object> map = consumerConfigs();
        map.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP0_ID);
        return new DefaultKafkaConsumerFactory<>(map);
    }
 

    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory1() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory1());
        //��Ӧtopic������
        factory.setConcurrency(3);
        //����Ϊ�������ѣ�ÿ������������Kafka���ò���������ConsumerConfig.MAX_POLL_RECORDS_CONFIG
        factory.setBatchListener(true);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }
 
    public ConsumerFactory<String, String> consumerFactory1() {
        Map<String, Object> map = consumerConfigs();
        map.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP1_ID);
        return new DefaultKafkaConsumerFactory<>(map);
    }
 
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>(16);
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerProps.getBootstrapServers());
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaConsumerProps.getEnableAutoCommit());
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, kafkaConsumerProps.getAutoCommitInterval());
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerProps.getDefaultGroupId());
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerProps.getAutoOffsetReset());
        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConsumerProps.getMaxPollRecords());
        return propsMap;
    }
 
}
