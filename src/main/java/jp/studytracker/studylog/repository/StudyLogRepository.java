package jp.studytracker.studylog.repository;

import java.time.LocalDate;
import java.util.List;

import jp.studytracker.studylog.entity.StudyLog;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface StudyLogRepository extends JpaRepository<StudyLog, Long> {

    List<StudyLog> findByStudyDate(LocalDate studyDate);
    
    @Query("select coalesce(sum(s.minutes), 0) from StudyLog s where s.studyDate = :date")
    Integer sumMinutesByDate(@Param("date") LocalDate date);
}
