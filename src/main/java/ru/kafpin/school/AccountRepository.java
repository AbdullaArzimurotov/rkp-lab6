package ru.kafpin.school;

import org.springframework.data.repository.CrudRepository;

interface AccountRepository extends CrudRepository<Account, String> {
}
