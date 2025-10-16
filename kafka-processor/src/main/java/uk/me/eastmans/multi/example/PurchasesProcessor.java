package uk.me.eastmans.multi.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.LoggingProducerListener;

@SpringBootApplication
@ComponentScan(basePackages = "uk.me.eastmans.multi")
public class PurchasesProcessor {

    private final Producer producer;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    @Autowired
    private LoggingProducerListener kafkaProducerListener;

    public static void main(String[] args) {
        System.err.println( "Running with args " + args.length);
        SpringApplication.run(PurchasesProcessor.class, args);
        //SpringApplication application = new SpringApplication(KafkaProcessor.class);
        //application.setWebApplicationType(WebApplicationType.NONE);
        //System.err.println( "Running with args " + args);
        //application.run(args);
    }

    @Bean
    public CommandLineRunner CommandLineRunnerBean() {
        System.err.println( "In CommandLineRunnerBean with args " );
        return (args) -> {
            for (String arg : args) {
                System.err.println("Argument: " + arg);
                switch (arg) {
                    case "--producer":
                        this.producer.sendMessage("awalther", "t-shirts");
                        this.producer.sendMessage("htanaka", "t-shirts");
                        this.producer.sendMessage("htanaka", "batteries");
                        this.producer.sendMessage("eabara", "t-shirts");
                        this.producer.sendMessage("htanaka", "t-shirts");
                        this.producer.sendMessage("jsmith", "book");
                        this.producer.sendMessage("awalther", "t-shirts");
                        this.producer.sendMessage("jsmith", "batteries");
                        this.producer.sendMessage("jsmith", "gift card");
                        this.producer.sendMessage("eabara", "t-shirts");
                        break;
                    case "--consumer":
                        MessageListenerContainer listenerContainer =
                                kafkaListenerEndpointRegistry.getListenerContainer("myConsumer");
                        listenerContainer.start();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Autowired
    PurchasesProcessor(Producer producer) {
        System.err.println("************************" + producer.toString());
        this.producer = producer;
    }

}
