package jp.studytracker.studylog.controller;

import java.util.List;
import java.time.LocalDate;

import jp.studytracker.studylog.entity.StudyLog;
import jp.studytracker.studylog.repository.StudyLogRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/studylogs")
public class StudyLogController {

    private final StudyLogRepository repo;

    public StudyLogController(StudyLogRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String list(Model model) {
        List<StudyLog> logs = repo.findAll();
        model.addAttribute("logs", logs);
        return "studylog/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        StudyLog log = new StudyLog();
        log.setStudyDate(LocalDate.now());
        model.addAttribute("studyLog", log);
        return "studylog/form";
    }

    @PostMapping
    public String create(@ModelAttribute("studyLog") StudyLog studyLog) {
        if (studyLog.getStudyDate() == null) {
            studyLog.setStudyDate(LocalDate.now());
        }
        repo.save(studyLog);
        return "redirect:/studylogs";
    }
}
