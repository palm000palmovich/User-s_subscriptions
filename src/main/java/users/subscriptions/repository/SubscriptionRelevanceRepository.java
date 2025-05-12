package users.subscriptions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import users.subscriptions.model.SubscriptionRelevance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRelevanceRepository extends JpaRepository<SubscriptionRelevance, Long> {
    @Query(value = "SELECT * FROM subscription_relevance sr WHERE sr.user_id = :userId AND sr.sub_id = :subId", nativeQuery = true)
    Optional<SubscriptionRelevance> findByUserIdAndSubId(@Param("userId") Long userId, @Param("subId") Long subId);

    @Query(value = "SELECT * FROM subscription_relevance sr WHERE sr.end_time < :currentTime", nativeQuery = true)
    List<SubscriptionRelevance> findByEndTimeBefore(@Param("currentTime") LocalDateTime curTime);

}