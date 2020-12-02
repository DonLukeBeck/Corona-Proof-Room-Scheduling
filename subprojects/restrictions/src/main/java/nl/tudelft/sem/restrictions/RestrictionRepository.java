package nl.tudelft.sem.restrictions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestrictionRepository extends JpaRepository<Restriction, String>, JpaSpecificationExecutor<Restriction> {
    Optional<Restriction> getRestrictionByName(String name);
}
