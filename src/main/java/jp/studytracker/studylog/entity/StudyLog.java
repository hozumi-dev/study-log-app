package jp.studytracker.studylog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.persistence.Column;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "study_log")
@Getter
@Setter
public class StudyLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 勉強した日付
    private LocalDate studyDate;

    // 科目・分野
    @NotBlank(message = "科目を選択してください")
    private String subject;

    // 勉強時間（分）
    private Integer minutes;

    // 一言メモ・内容
    private String memo;

    @Column(name = "seconds")
    @Min(0)
    private Integer seconds;

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }
}
