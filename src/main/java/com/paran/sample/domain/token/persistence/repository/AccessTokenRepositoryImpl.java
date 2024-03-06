package com.paran.sample.domain.token.persistence.repository;

import com.paran.sample.domain.token.persistence.entity.AccessToken;
import com.paran.sample.domain.token.persistence.entity.QAccessToken;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccessTokenRepositoryImpl implements AccessTokenRepositoryCustom {
    private final JPAQueryFactory query;

    QAccessToken accessToken = QAccessToken.accessToken;

    @Override
    public List<AccessToken> findAccessTokensByUserIdx(Long appUserIdx) {
        return query.selectFrom(accessToken)
                .where(
                        eqAppUserIdx(appUserIdx),
                        validTokenOnly()
                )
                .fetch();
    }

    private BooleanExpression eqAppUserIdx(Long appUserIdx) {
        return accessToken.user.appUserIdx.eq(appUserIdx);
    }

    private BooleanExpression validTokenOnly() {
        return accessToken.expired.isFalse()
                .and(accessToken.revoked.isFalse());
    }

}
