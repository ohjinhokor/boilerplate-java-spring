package com.boilerplate.user.repository;

import com.boilerplate.common.repository.SearchRepository;
import com.boilerplate.user.dto.UserSearchDto;
import com.boilerplate.user.model.QUser;
import com.boilerplate.user.model.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserSearchRepository extends SearchRepository<User, UserSearchDto> {

	private final JPAQueryFactory queryFactory;
	private final QUser qUser = QUser.user;

	@Override
	protected JPAQuery<User> searchQuery(UserSearchDto search) {
		return queryFactory
			.selectFrom(qUser).where(
				eqId(search.getId()),
				containUsername(search.getUsername()),
				containName(search.getName()),
				containEmail(search.getEmail()),
				containPhoneNumber(search.getPhoneNumber())
			);
	}

	@Override
	protected JPAQuery<Long> totalCountQuery(UserSearchDto search) {
		var searchQuery = searchQuery(search);
		return searchQuery.select(qUser.count());
	}

	private BooleanExpression eqId(Long id) {
		return id == null ? null : qUser.id.eq(id);
	}

	private BooleanExpression containUsername(String name) {
		return name == null ? null : qUser.username.contains(name);
	}

	private BooleanExpression containName(String name) {
		return name == null ? null : qUser.name.contains(name);
	}

	private BooleanExpression containEmail(String email) {
		return email == null ? null : qUser.email.contains(email);
	}

	private BooleanExpression containPhoneNumber(String phoneNumber) {
		return phoneNumber == null ? null : qUser.phoneNumber.contains(phoneNumber);
	}
}