package ru.kafpin.school;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SchoolboyData(
        @NotBlank @Size(max = 80) String surname,
        @NotBlank @Size(max = 80) String name,
        @Size(max = 80) String patronymic,
        @NotBlank @Pattern(regexp = "Мужской|Женский", message = "Выберите пол из списка.") String gender,
        @NotBlank @Size(max = 80) String nationality,
        @NotNull @DecimalMin("40") @DecimalMax("250") @Digits(integer = 3, fraction = 1, message = "Укажите не более одной цифры после запятой.") BigDecimal height,
        @NotNull @DecimalMin("2") @DecimalMax("300") @Digits(integer = 3, fraction = 1, message = "Укажите не более одной цифры после запятой.") BigDecimal weight,
        @NotNull @Past @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
        @NotBlank @Pattern(regexp = "[+0-9 ()-]{7,22}", message = "Проверьте формат телефона.") String phoneNumber,
        @NotNull @Valid AddressData address,
        @NotNull @Positive Long schoolId,
        @NotNull @Positive Long classId) {

    public SchoolboyData {
        // Пустой адрес нужен, чтобы форма могла повторно отобразить вложенные поля.
        if (address == null) {
            address = AddressData.empty();
        }
    }

    public static SchoolboyData empty() {
        return new SchoolboyData(null, null, null, null, null, null, null, null,
                null, AddressData.empty(), null, null);
    }
}
