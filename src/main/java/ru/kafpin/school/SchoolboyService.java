package ru.kafpin.school;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
class SchoolboyService {
    private final SchoolboyStore store;

    SchoolboyService(SchoolboyStore store) {
        this.store = store;
    }

    List<SchoolboyView> search(SchoolboyFilter filter) {
        return store.search(filter);
    }

    Optional<SchoolboyView> find(long id) {
        return store.find(id);
    }

    List<SchoolInfo> schools() {
        return store.schools();
    }

    List<ClassInfo> classes() {
        return store.classes();
    }

    Optional<String> selectionError(Long schoolId, Long classId) {
        if (schoolId != null && schools().stream().noneMatch(school -> school.id() == schoolId)) {
            return Optional.of("Выберите существующую школу.");
        }
        if (classId == null) {
            return Optional.empty();
        }
        boolean valid = store.findClass(classId)
                .filter(selected -> schoolId == null || selected.schoolId() == schoolId)
                .isPresent();
        return valid ? Optional.empty() : Optional.of("Укажите существующий класс выбранной школы.");
    }

    @Transactional
    SchoolboyView create(SchoolboyData data) {
        return store.create(data);
    }
}
