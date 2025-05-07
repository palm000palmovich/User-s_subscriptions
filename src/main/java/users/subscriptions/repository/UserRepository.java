package users.subscriptions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import users.subscriptions.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
