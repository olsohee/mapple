package mapple.mapple.review.repository;

import mapple.mapple.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {

    Page<Review> findReviewsPage(Long userId, Pageable pageable);

    Page<Review> findFriendReviewsPage(Long userId, Pageable pageable);
}
