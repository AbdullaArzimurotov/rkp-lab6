package ru.kafpin.school;

import jakarta.persistence.*;

@Entity
@Table(name = "schools")
class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 100)
    String name;
    long id() { return id; }
    String name() { return name; }
}
