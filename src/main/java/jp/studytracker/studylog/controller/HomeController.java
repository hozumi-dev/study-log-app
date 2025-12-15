package jp.studytracker.studylog.controller;

import java.time.LocalDate;
import java.util.List;

import jp.studytracker.studylog.entity.StudyLog;
import jp.studytracker.studylog.repository.StudyLogRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class HomeController {
    
    private final StudyLogRepository repo;

    public HomeController(StudyLogRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/")
    public String home(Model model) {
        LocalDate today = LocalDate.now();

        Integer totalSeconds = repo.sumSecondsByDate(today);
        String totalHms = formatHms(totalSeconds);
        List<StudyLog> todayLogs = repo.findByStudyDate(today);

        List<String> subjects = repo.findDistinctSubjects();
        model.addAttribute("subjects", subjects);

        StudyLog form = new StudyLog();
        form.setStudyDate(today);

        model.addAttribute("today", today);
        model.addAttribute("totalSeconds", totalSeconds);
        model.addAttribute("totalHms", totalHms);
        model.addAttribute("todayLogs", todayLogs);
        model.addAttribute("studyLog", form);

        return "home";
    }

    @PostMapping("/home/log")
    public String saveFromHome(
            @Valid @ModelAttribute("studyLog") StudyLog studyLog,
            BindingResult bindingResult,
            Model model) {

        LocalDate today = LocalDate.now();
        
        if (studyLog.getStudyDate() == null) {
            studyLog.setStudyDate(LocalDate.now());
        }

        if(bindingResult.hasErrors()){
            Integer totalSeconds = repo.sumSecondsByDate(today);
            String totalHms = formatHms(totalSeconds);
            List<StudyLog> todayLogs = repo.findByStudyDate(today);
            List<String> subjects = repo.findDistinctSubjects();

            model.addAttribute("today", today);
            model.addAttribute("totalSeconds", totalSeconds);
            model.addAttribute("totalHms", totalHms);
            model.addAttribute("todayLogs", todayLogs);
            model.addAttribute("subjects", subjects);

            return "home";
        }

        if(studyLog.getSeconds() == null) {
            Integer minutes = studyLog.getMinutes();
            studyLog.setSeconds(minutes == null ? 0 : minutes * 60);
        }

        repo.save(studyLog);   
        return "redirect:/";
    }

    private static String formatHms(Integer totalSeconds) {
        int sec = (totalSeconds == null) ? 0 : totalSeconds;
        int hours = sec / 3600;
        int minutes = (sec % 3600) / 60;
        int seconds = sec % 60;
        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }
}
