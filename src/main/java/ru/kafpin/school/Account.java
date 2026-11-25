package ru.kafpin.school;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
class Account {
    @Id
    @Column(length = 80)
    private String username;

    @Column(nullable = false, length = 60)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Role role;

    protected Account() {
    }

    Account(String username, String passwordHash, Role role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    String username() { return username; }
    String passwordHash() { return passwordHash; }
    Role role() { return role; }

    enum Role { USER, ADMIN }
}
