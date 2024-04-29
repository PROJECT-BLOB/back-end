package com.codeit.blob.post.repository;

import com.codeit.blob.city.domain.City;
import com.codeit.blob.city.domain.Country;
import com.codeit.blob.post.domain.Category;
import com.codeit.blob.post.domain.Post;
import com.codeit.blob.post.domain.Subcategory;
import com.codeit.blob.post.request.FeedFilter;
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

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Post> getFeed(Country country, City city, FeedFilter filters, Pageable pageable){
        BooleanExpression predicate = getFeedFilterPredicate(country, city, filters);
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

    private BooleanExpression getFeedFilterPredicate(Country country, City city, FeedFilter filters){
        BooleanExpression predicate = Expressions.TRUE;

        if(filters.getCity() == null){
            predicate = predicate.and(post.city.country.eq(country));
        } else {
            predicate = predicate.and(post.city.eq(city));
        }

        if (filters.getCategories() != null && !filters.getCategories().isEmpty()) {
            BooleanExpression categoryPredicate = Expressions.FALSE;
            for (String categoryAndSubcategory : filters.getCategories()) {
                String[] parts = categoryAndSubcategory.split(":");
                if (parts.length == 1){
                    Category category = Category.getInstance(parts[0]);
                    categoryPredicate = categoryPredicate.or(post.category.eq(category));
                } else if (parts.length == 2) {
                    Category category = Category.getInstance(parts[0]);
                    Subcategory subcategory = Subcategory.getInstance(parts[1]);
                    categoryPredicate = categoryPredicate.or(post.category.eq(category).and(post.subcategory.eq(subcategory)));
                }
            }
            predicate = predicate.and(categoryPredicate);
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

        return predicate;
    }

    private OrderSpecifier<?>[] getOrderBy(String sortBy) {
        switch (sortBy) {
            case "hot":
                NumberExpression<Long> timeDifference = Expressions
                        .numberTemplate(Long.class, "timestampdiff(SECOND, {0}, now())", post.createdDate);

                // penalty older posts by subtracting week diff with weight
                NumberExpression<Double> scoreExpression = post.likes.size().doubleValue()
                        .subtract(timeDifference.divide(3600 * 24 * 7).multiply(100));

                OrderSpecifier<Double> hotOrderSpecifier = scoreExpression.desc();
                return new OrderSpecifier[]{hotOrderSpecifier, post.createdDate.desc()};

            case "likes":
                return new OrderSpecifier[]{post.likes.size().desc(), post.createdDate.desc()};
            case "views":
                return new OrderSpecifier[]{post.views.desc(), post.createdDate.desc()};
            default: // recent
                return new OrderSpecifier[]{post.createdDate.desc()};
        }
    }
}