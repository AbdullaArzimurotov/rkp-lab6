package ru.kafpin.school;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SchoolboyFilter(
        @Size(max = 80) String surname,
        @Size(max = 80) String city,
        @Positive Long schoolId,
        @Positive Long classId) {
}
