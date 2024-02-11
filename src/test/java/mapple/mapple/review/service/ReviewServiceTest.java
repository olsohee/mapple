package mapple.mapple.review.service;

import mapple.mapple.review.entity.Review;
import mapple.mapple.review.repository.ReviewRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@Transactional
class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;
    @Autowired
    ReviewRepository reviewRepository;

    @Test
    @DisplayName("동시에 같은 사용자가 좋아요를 눌러도 좋아요 수는 0~1개만 추가되어야 한다.")
    void likeBySameUser() throws InterruptedException {
        long reviewId = 1;
        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                reviewService.like(reviewId, "user1@naver.com");
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();

        Review review = reviewRepository.findById(reviewId).get();
        Assertions.assertThat(review.getLikes().size()).isLessThanOrEqualTo(1);
    }

    @Test
    @DisplayName("동시에 여러 사용자가 좋아요를 눌러도 사용자 수만큼 좋아요 수가 늘어야 한다.")
    void likeByUsers() throws InterruptedException {
        long reviewId = 1;
        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            String identifier = "user" + (i + 1) + "@naver.com";
            executorService.execute(() -> {
                reviewService.like(reviewId, identifier);
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();

        Review review = reviewRepository.findById(reviewId).get();
        Assertions.assertThat(review.getLikes().size()).isEqualTo(numberOfThreads);
    }
}
