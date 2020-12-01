package nl.tudelft.sem.rooms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private int roomId;

    @Column(name = "name")
    private String name;

    @Column(name = "capacity")
    private int capacity;

    /**
     * Returns the id of the room.
     *
     * @return room id of room entity
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * Sets the id of the room.
     *
     * @param roomId of room entity
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    /**
     * Gets the name variable of the room.
     *
     * @return name of of room entity
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name variable of the room.
     *
     * @param name of room entity
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the capacity of the room.
     *
     * @return capacity of room entity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the capacity of the room.
     *
     * @param capacity of room entity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
