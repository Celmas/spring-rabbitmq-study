package ru.itis.springrabbitmqstudy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;

@Component
public class ImageDownloadingReceiver {

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String imageUrl) {
        System.out.println("Start  load image " + imageUrl);
        try {
            URL url = new URL(imageUrl);
            String fileName = url.getFile();
            BufferedImage image = ImageIO.read(url);
            File output = new File("downloaded/" + UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf(".")));
            ImageIO.write(image, "jpg", output);
            System.out.println("Finish load image " + imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}