package jp.studytracker.studylog.controller;

import java.time.LocalDate;
import java.util.List;

import jp.studytracker.studylog.entity.StudyLog;
import jp.studytracker.studylog.repository.StudyLogRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class HomeController {
    
    private final StudyLogRepository repo;

    public HomeController(StudyLogRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/")
    public String home(Model model) {
        LocalDate today = LocalDate.now();

        Integer totalMinutes = repo.sumMinutesByDate(today);
        List<StudyLog> todayLogs = repo.findByStudyDate(today);

        StudyLog form = new StudyLog();
        form.setStudyDate(today);

        model.addAttribute("today", today);
        model.addAttribute("totalMinutes", todayLogs);
        model.addAttribute("todayLogs", todayLogs);
        model.addAttribute("studyLog", form);

        return "home";
    }

    @PostMapping("/home/log")
    public String saveFromHome(@ModelAttribute("studyLog") StudyLog studyLog) {
        if (studyLog.getStudyDate() == null) {
            studyLog.setStudyDate(LocalDate.now());
        }
        repo.save(studyLog);   
        return "redirect:/";
    }
    
    
}
