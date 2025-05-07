package users.subscriptions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import users.subscriptions.model.Subscriptions;

@Repository
public interface SubscriptionsRepository extends JpaRepository<Subscriptions, Long> {
}
