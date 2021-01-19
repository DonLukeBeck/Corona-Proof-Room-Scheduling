package nl.tudelft.sem.courses.controller;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.tudelft.sem.courses.entity.Lecture;
import nl.tudelft.sem.courses.repository.LectureRepository;
import nl.tudelft.sem.shared.entity.BareLecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class LectureRetrievalController {

    @Autowired
    private transient LectureRepository lectureRepository;

    /**
     * Get endpoint to retrieve all lectures.
     *
     * @return A list of {@link BareLecture}s
     */
    @GetMapping("/getAllLectures")
    @ResponseBody
    public ResponseEntity<?> getAllLectures() {
        Stream<BareLecture> tt = lectureRepository.findAll().stream().map(l -> new BareLecture(l.getCourseId(),
                Instant.ofEpochMilli(l.getScheduledDate().getTime()).atZone(ZoneId.systemDefault())
                        .toLocalDate(), l.getDuration()));

        return ResponseEntity.ok(
                tt.filter(Objects::nonNull).collect(Collectors.toList()));
    }

    /**
     * Get endpoint to retrieve all lectures after a certain date.
     *
     * @param date the date for which to retrieve the lectures
     * @return A list of {@link BareLecture}s
     */
    @GetMapping("/getLecturesAfterDate")
    @ResponseBody
    public ResponseEntity<?> getLecturesAfterDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            return ResponseEntity.notFound().build();
        }
        Date sqlDate = Date.valueOf(date);
        Stream<BareLecture> tt = lectureRepository
                .findByScheduledDateAfter(sqlDate).stream().map(l -> new BareLecture(l.getCourseId(),
                Instant.ofEpochMilli(l.getScheduledDate().getTime()).atZone(ZoneId.systemDefault())
                        .toLocalDate(), l.getDuration()));
        return ResponseEntity.ok(tt.collect(Collectors.toList()));
    }
}
