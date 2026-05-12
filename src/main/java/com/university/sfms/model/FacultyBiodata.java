package com.university.sfms.model;

import jakarta.persistence.*;

@Entity
@Table(name = "faculty_biodata")
public class FacultyBiodata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_index", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_faculty_biodata_user"))
    private User user;

    @Column(nullable = false, length = 160)
    private String qualification;
    @Column(nullable = false)
    private Integer experience;
    @Column(nullable = false, length = 160)
    private String specialization;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_faculty_biodata_department"))
    private Department department;
    @Column(length = 500)
    private String imageUrl;

    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public Integer getExperience() { return experience; }
    public void setExperience(Integer experience) { this.experience = experience; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
