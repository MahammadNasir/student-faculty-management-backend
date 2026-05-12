package com.university.sfms.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "submissions",
        uniqueConstraints = @UniqueConstraint(name = "uk_submission_assignment_student", columnNames = {"assignment_id", "student_id"}))
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assignment_id", nullable = false, foreignKey = @ForeignKey(name = "fk_submissions_assignment"))
    private Assignment assignment;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false, foreignKey = @ForeignKey(name = "fk_submissions_student"))
    private User student;
    @Column(nullable = false, length = 500)
    private String fileUrl;
    @Column(nullable = false, updatable = false)
    private Instant submittedAt = Instant.now();

    public Long getId() { return id; }
    public Assignment getAssignment() { return assignment; }
    public void setAssignment(Assignment assignment) { this.assignment = assignment; }
    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public Instant getSubmittedAt() { return submittedAt; }
}
