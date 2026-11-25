package ru.kafpin.school;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressData(
        @NotBlank @Pattern(regexp = "[0-9]{5,6}", message = "Укажите 5 или 6 цифр.") String postalCode,
        @NotBlank @Size(max = 80) String country,
        @NotBlank @Size(max = 80) String region,
        @NotBlank @Size(max = 80) String district,
        @NotBlank @Size(max = 80) String city,
        @NotBlank @Size(max = 80) String street,
        @NotBlank @Size(max = 20) String house,
        @Size(max = 20) String apartment) {

    public static AddressData empty() {
        return new AddressData(null, null, null, null, null, null, null, null);
    }
}
