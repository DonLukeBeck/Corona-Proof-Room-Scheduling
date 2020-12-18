package nl.tudelft.sem.rooms;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer>,
        JpaSpecificationExecutor<Room> {
    Optional<Room> findByName(String name);

    List<Room> findAll();
}