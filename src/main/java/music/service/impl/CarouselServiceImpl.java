package music.service.impl;

import music.service.CarouselService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {

    private Logger logger = LoggerFactory.getLogger(CarouselServiceImpl.class);

    private List<String> images = new ArrayList<>();

    public CarouselServiceImpl(@Value("${carousel.images}") List<String> images) {
        this.images.addAll(images);
    }

    @PostConstruct
    public void AfterInitialize() {
        if (this.images.size() < 3) {
            throw new IllegalArgumentException("Not enough images");
        }
    }

    @Override
    public String firstImage() {
        return this.images.get(0);
    }

    @Override
    public String secondImage() {
        return this.images.get(1);
    }

    @Override
    public String thirdImage() {
        return this.images.get(2);
    }

    @Scheduled(cron = "${carousel.refresh-cron}")
    public void shuffle() {
        logger.info("SHUFFLING images..");
        Collections.shuffle(images);
    }
}
