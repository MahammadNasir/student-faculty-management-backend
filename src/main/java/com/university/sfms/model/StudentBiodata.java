package com.university.sfms.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "student_biodata")
public class StudentBiodata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_index", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_student_biodata_user"))
    private User user;

    @Column(nullable = false, length = 120)
    private String fatherName;
    @Column(nullable = false, length = 120)
    private String motherName;
    private LocalDate dob;
    @Column(length = 20)
    private String gender;
    @Column(length = 500)
    private String address;
    @Column(length = 30)
    private String phone;
    @Column(length = 30)
    private String emergencyContact;
    @Column(length = 10)
    private String bloodGroup;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_student_biodata_department"))
    private Department department;
    @Column(name = "academic_year")
    private Integer year;
    @Column(length = 10)
    private String section;
    @Column(length = 500)
    private String imageUrl;

    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }
    public String getMotherName() { return motherName; }
    public void setMotherName(String motherName) { this.motherName = motherName; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
