package users.subscriptions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import users.subscriptions.model.Subscriptions;

import java.util.Optional;

@Repository
public interface SubscriptionsRepository extends JpaRepository<Subscriptions, Long> {
    @Query("SELECT s FROM Subscriptions s WHERE s.type = :type")
    Optional<Subscriptions> findByType(@Param("type") String type);
}
