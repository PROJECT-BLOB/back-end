package com.codeit.blob.post.repository;

import com.codeit.blob.city.domain.City;
import com.codeit.blob.city.domain.Country;
import com.codeit.blob.post.domain.Category;
import com.codeit.blob.post.domain.Post;
import com.codeit.blob.post.domain.Subcategory;
import com.codeit.blob.post.request.FeedFilter;
import com.codeit.blob.post.request.MapFilter;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.codeit.blob.post.domain.QPost.post;
import static com.codeit.blob.post.domain.QPostReport.postReport;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Post> getFeed(City city, FeedFilter filters, Pageable pageable){
        BooleanExpression predicate = getFeedFilterPredicate(city, filters);
        List<Post> content = jpaQueryFactory.selectFrom(post)
                .where(predicate)
                .orderBy(getOrderBy(filters.getSortBy()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory.select(post.count()).from(post)
                .where(predicate)
                .fetchOne();
        total = total == null ? 0 : total;

        return new PageImpl<>(content, pageable, total);
    }

    public List<Post> getMap(MapFilter filters){
        BooleanExpression predicate = getMapFilterPredicate(filters);
        return jpaQueryFactory.selectFrom(post)
                .where(predicate)
                .fetch();
    }

    public Page<Post> getMapSidebar(MapFilter filters, Pageable pageable, String sortBy){
        BooleanExpression predicate = getMapFilterPredicate(filters);
        List<Post> content = jpaQueryFactory.selectFrom(post)
                .where(predicate)
                .orderBy(getOrderBy(sortBy))
                .fetch();

        Long total = jpaQueryFactory.select(post.count()).from(post)
                .where(predicate)
                .fetchOne();
        total = total == null ? 0 : total;

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression getFeedFilterPredicate(City city, FeedFilter filters){
        BooleanExpression predicate = post.city.eq(city);

        if (filters.getCategories() != null && !filters.getCategories().isEmpty()) {
            predicate = predicate.and(getCategoryFilterPredicate(filters.getCategories()));
        }

        if (filters.getStartDate() != null) {
            predicate = predicate.and(post.createdDate.goe(filters.getStartDate().atStartOfDay()));
        }

        if (filters.getEndDate() != null) {
            predicate = predicate.and(post.createdDate.loe(filters.getEndDate().atTime(23,59,59)));
        }

        if (filters.isHasImage()) {
            predicate = predicate.and(post.postImages.isNotEmpty());
        }

        if (filters.isHasLocation()) {
            predicate = predicate.and(post.coordinate.isNotNull());
        }

        if (filters.getMinLikes() > 0) {
            predicate = predicate.and(post.likes.size().goe(filters.getMinLikes()));
        }

        if (filters.getKeyword() != null) {
            predicate = predicate.and(
                    post.title.like("%" + filters.getKeyword() + "%")
                            .or(post.content.like("%" + filters.getKeyword() + "%"))
            );
        }

        return predicate;
    }

    private BooleanExpression getMapFilterPredicate(MapFilter filters){
        BooleanExpression predicate = post.coordinate.isNotNull()
                .and(post.coordinate.lat.loe(filters.getMaxLat()))
                .and(post.coordinate.lat.goe(filters.getMinLat()))
                .and(post.coordinate.lng.loe(filters.getMaxLng()))
                .and(post.coordinate.lng.goe(filters.getMinLng()));

        NumberExpression<Long> minutesToShow = post.likes.size().longValue()
                .multiply(60).add(60 * 24 * 7);
        NumberExpression<Long> timeDifference = Expressions.numberTemplate(Long.class,
                "timestampdiff(SECOND, {0}, now())", post.createdDate).divide(60);
        predicate = predicate.and(timeDifference.loe(minutesToShow));

        if (filters.getCategories() != null && !filters.getCategories().isEmpty()) {
            predicate = predicate.and(getCategoryFilterPredicate(filters.getCategories()));
        }

        return predicate;
    }

    private BooleanExpression getCategoryFilterPredicate(List<String> categories){
        BooleanExpression predicate = Expressions.FALSE;
        for (String categoryAndSubcategory : categories) {
            String[] parts = categoryAndSubcategory.split(":");
            if (parts.length == 1){
                Category category = Category.getInstance(parts[0]);
                predicate = predicate.or(post.category.eq(category));
            } else if (parts.length == 2) {
                Category category = Category.getInstance(parts[0]);
                Subcategory subcategory = Subcategory.getInstance(parts[1]);
                predicate = predicate.or(post.category.eq(category).and(post.subcategory.eq(subcategory)));
            }
        }
        return predicate;
    }

    private OrderSpecifier<?>[] getOrderBy(String sortBy) {
        switch (sortBy) {
            case "hot": // ref: hacker news ranking algorithms
                NumberExpression<Long> timeDifference = Expressions
                        .numberTemplate(Long.class, "timestampdiff(SECOND, {0}, now())", post.createdDate)
                        .divide(3600 * 24).add(2);

                // time diff in days, gravity is 2
                NumberExpression<Double> denominator = timeDifference.doubleValue()
                        .multiply(timeDifference);

                NumberExpression<Double> score = post.likes.size().doubleValue()
                        .subtract(1)
                        .divide(denominator);

                OrderSpecifier<Double> hotOrderSpecifier = score.desc();
                return new OrderSpecifier[]{hotOrderSpecifier, post.createdDate.desc()};

            case "likes":
                return new OrderSpecifier[]{post.likes.size().desc(), post.createdDate.desc()};
            case "views":
                return new OrderSpecifier[]{post.views.desc(), post.createdDate.desc()};
            default: // recent
                return new OrderSpecifier[]{post.createdDate.desc()};
        }
    }

    public Page<Post> getReportedPosts(Pageable pageable, int minReport){
        List<Post> content = jpaQueryFactory.selectFrom(post)
                .distinct()
                .leftJoin(post.reports, postReport)
                .where(post.reports.size().goe(minReport))
                .orderBy(post.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory.select(post.count()).from(post)
                .distinct()
                .leftJoin(post.reports, postReport)
                .where(post.reports.size().goe(minReport))
                .fetchOne();
        total = total == null ? 0 : total;

        return new PageImpl<>(content, pageable, total);
    }
}
