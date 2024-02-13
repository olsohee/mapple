package mapple.mapple.review.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.friend.entity.QFriend;
import mapple.mapple.friend.entity.RequestStatus;
import mapple.mapple.review.entity.QReview;
import mapple.mapple.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static mapple.mapple.friend.entity.QFriend.friend;
import static mapple.mapple.review.entity.QReview.review;
import static mapple.mapple.user.entity.QUser.user;

public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Review> findReviewsPage(String keyword, Long userId, Pageable pageable) {
        JPAQuery<Review> query = queryFactory
                .selectFrom(review)
                .join(review.user, user).fetchJoin()
                .leftJoin(friend).on(friend.fromUser.id.eq(userId))
                .where(
                        accessible(friend, review, userId),
                        containKeyword(review, keyword));

        setOrder(query, review, pageable);
        QueryResults<Review> results = query.fetchResults();
        return new PageImpl(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public Page<Review> findFriendReviewsPage(Long userId, Pageable pageable) {
        JPAQuery<Review> query = queryFactory
                .selectFrom(review)
                .join(review.user, user).fetchJoin()
                .leftJoin(friend).on(friend.fromUser.id.eq(userId))
                .where(
                        isAccessibleFriendReview(friend, review, userId)
                );

        setOrder(query, review, pageable);
        QueryResults<Review> results = query.fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private Predicate accessible(QFriend friend, QReview review, Long userId) {
        return review.publicStatus.eq(PublicStatus.PUBLIC) // 전체 공개이거나
                .or(review.user.id.eq(userId)) // 유저 자신의 리뷰이거나
                .or(isAccessibleFriendReview(friend, review, userId)); // 유저 친구의 리뷰이거나
    }

    private Predicate isAccessibleFriendReview(QFriend friend, QReview review, Long userId) {
        return review.user.id.eq(friend.toUser.id) // 유저 친구 리뷰글이면서
                .and(friend.requestStatus.eq(RequestStatus.ACCEPT)) // 친구 상태가 ACCEPT 이면서
                .and(review.publicStatus.ne(PublicStatus.PRIVATE)); // 비공개가 아닌 경우
    }

    private Predicate containKeyword(QReview review, String keyword) {
        return keyword != null ? review.placeName.contains(keyword) : null;
    }

    private void setOrder(JPAQuery<Review> query, QReview review, Pageable pageable) {
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(review.getType(), review.getMetadata());

            query
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                            pathBuilder.get(o.getProperty())));
        }
    }
}
