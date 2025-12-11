package jp.studytracker.studylog.repository;

import jp.studytracker.studylog.entity.StudyLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyLogRepository extends JpaRepository<StudyLog, Long>{
    
}
