package uk.me.eastmans.multi.dbevents;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;

@ComponentScan(basePackages = "uk.me.eastmans.multi")
@EntityScan(basePackages ="uk.me.eastmans.multi.em")
@EnableJpaRepositories("uk.me.eastmans.multi.em")
@SpringBootApplication
public class DbChangeEventProcessor {

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    public static void main(String[] args) {
        SpringApplication.run(DbChangeEventProcessor.class, args);
    }

    @Bean
    public CommandLineRunner CommandLineRunnerBean() {
        return (args) -> {
            MessageListenerContainer listenerContainer =
                    kafkaListenerEndpointRegistry.getListenerContainer("dbEventConsumer");
            listenerContainer.start();
        };
    }

    DbChangeEventProcessor() {
    }

}