package com.university.sfms.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "marks",
        uniqueConstraints = @UniqueConstraint(name = "uk_marks_student_subject_exam", columnNames = {"student_id", "subject_id", "exam_type"}))
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false, foreignKey = @ForeignKey(name = "fk_marks_student"))
    private User student;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false, foreignKey = @ForeignKey(name = "fk_marks_subject"))
    private Subject subject;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "faculty_id", nullable = false, foreignKey = @ForeignKey(name = "fk_marks_faculty"))
    private User faculty;
    @Column(nullable = false, length = 40)
    private String examType;
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal score;
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal maxScore;

    public Long getId() { return id; }
    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }
    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }
    public User getFaculty() { return faculty; }
    public void setFaculty(User faculty) { this.faculty = faculty; }
    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }
    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }
    public BigDecimal getMaxScore() { return maxScore; }
    public void setMaxScore(BigDecimal maxScore) { this.maxScore = maxScore; }
}
