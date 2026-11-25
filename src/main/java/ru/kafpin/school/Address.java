package ru.kafpin.school;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "postal_code", nullable = false, length = 6)
    private String postalCode;

    @Column(name = "country", nullable = false, length = 80)
    private String country;

    @Column(name = "region", nullable = false, length = 80)
    private String region;

    @Column(name = "district", nullable = false, length = 80)
    private String district;

    @Column(name = "city", nullable = false, length = 80)
    private String city;

    @Column(name = "street", nullable = false, length = 80)
    private String street;

    @Column(name = "house", nullable = false, length = 20)
    private String house;

    @Column(name = "apartment", nullable = true, length = 20)
    private String apartment;

    protected Address() {
    }

    Address(AddressData data) {
        apply(data);
    }

    void apply(AddressData data) {
        postalCode = data.postalCode();
        country = data.country();
        region = data.region();
        district = data.district();
        city = data.city();
        street = data.street();
        house = data.house();
        apartment = data.apartment();
    }

    AddressData toData() {
        return new AddressData(postalCode, country, region, district, city, street, house, apartment);
    }
}
