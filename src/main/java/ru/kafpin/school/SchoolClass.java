package ru.kafpin.school;

import jakarta.persistence.*;

@Entity
@Table(name = "school_classes", uniqueConstraints = @UniqueConstraint(columnNames = {"school_id", "name"}))
class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_id")
    School school;

    @Column(nullable = false, length = 16)
    String name;
    long id() { return id; }
    long schoolId() { return school.id(); }
    String schoolName() { return school.name(); }
    String name() { return name; }
}
