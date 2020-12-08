package nl.tudelft.sem.identity.repository;

import nl.tudelft.sem.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT * FROM user WHERE netid = ?1 LIMIT 1", nativeQuery = true)
    User findByNetid(String netid);
}
