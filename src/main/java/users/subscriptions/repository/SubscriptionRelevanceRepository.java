package users.subscriptions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import users.subscriptions.model.SubscriptionRelevance;

@Repository
public interface SubscriptionRelevanceRepository extends JpaRepository<SubscriptionRelevance, Long> {
}