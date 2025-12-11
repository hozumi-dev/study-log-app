package jp.studytracker.studylog.controller;

import java.util.List;
import jp.studytracker.studylog.entity.StudyLog;
import jp.studytracker.studylog.repository.StudyLogRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudyLogApiController {

    private final StudyLogRepository studyLogRepository;

    public StudyLogApiController(StudyLogRepository studyLogRepository) {
        this.studyLogRepository = studyLogRepository;
    }

    @GetMapping("/api/studylogs")
    public List<StudyLog> getAll() {
        return studyLogRepository.findAll();
    }
}
