package ru.itis.springrabbitmqstudy;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.concurrent.TimeUnit;

@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final ImageDownloadingReceiver imageDownloadingReceiver;

    public Runner(ImageDownloadingReceiver imageDownloadingReceiver, RabbitTemplate rabbitTemplate) {
        this.imageDownloadingReceiver = imageDownloadingReceiver;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Sending message...");
        produceImages();
        imageDownloadingReceiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    }

    private void produceImages() {
        try {
            File file = new File("src/images.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String currentFile;
            while ((currentFile = reader.readLine()) != null) {
                rabbitTemplate.convertAndSend(
                        SpringRabbitMqStudyApplication.topicExchangeName,
                        SpringRabbitMqStudyApplication.routingKey.replace("#", "image"),
                        currentFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
