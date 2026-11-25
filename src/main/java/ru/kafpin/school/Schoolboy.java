package ru.kafpin.school;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "schoolboys")
class Schoolboy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String surname;
    @Column(nullable = false, length = 80)
    private String name;
    @Column(length = 80)
    private String patronymic;
    @Column(nullable = false, length = 16)
    private String gender;
    @Column(nullable = false, length = 80)
    private String nationality;
    @Column(nullable = false, precision = 4, scale = 1)
    private BigDecimal height;
    @Column(nullable = false, precision = 4, scale = 1)
    private BigDecimal weight;
    @Column(nullable = false)
    private LocalDate birthDate;
    @Column(nullable = false, length = 22)
    private String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", unique = true)
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;

    protected Schoolboy() {
    }

    Schoolboy(SchoolboyData data, SchoolClass selectedClass) {
        address = new Address(data.address());
        apply(data, selectedClass);
    }

    void apply(SchoolboyData data, SchoolClass selectedClass) {
        surname = data.surname();
        name = data.name();
        patronymic = data.patronymic();
        gender = data.gender();
        nationality = data.nationality();
        height = data.height();
        weight = data.weight();
        birthDate = data.birthDate();
        phoneNumber = data.phoneNumber();
        address.apply(data.address());
        schoolClass = selectedClass;
    }

    SchoolboyView toView() {
        var data = new SchoolboyData(surname, name, patronymic, gender, nationality, height,
                weight, birthDate, phoneNumber, address.toData(), schoolClass.schoolId(), schoolClass.id());
        return new SchoolboyView(id, data, schoolClass.schoolName(), schoolClass.name());
    }
}
