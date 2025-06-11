package users.subscriptions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import users.subscriptions.model.Subscriptions;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionsRepository extends JpaRepository<Subscriptions, Long> {
    @Query("SELECT s FROM Subscriptions s WHERE s.type = :type")
    Optional<Subscriptions> findByType(@Param("type") String type);

    @Query(value = "select * from subscriptions s where s.subscribers_counter > 0", nativeQuery = true)
    Optional<List<Subscriptions>> getAllUsedSubscriptions();

    @Query(value = "select * from subscriptions s where s.subscribers_counter IN " +
            "(SELECT DISTINCT s2.subscribers_counter FROM subscriptions s2 " +
            "ORDER BY s2.subscribers_counter DESC limit 3) limit 3", nativeQuery = true)
    Optional<List<Subscriptions>> getTop3Subs();
}