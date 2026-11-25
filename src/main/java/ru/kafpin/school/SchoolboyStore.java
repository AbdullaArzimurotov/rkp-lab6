package ru.kafpin.school;

import java.util.List;
import java.util.Optional;

public interface SchoolboyStore {
    List<SchoolboyView> search(SchoolboyFilter filter);
    Optional<SchoolboyView> find(long id);
    List<SchoolInfo> schools();
    List<ClassInfo> classes();
    Optional<ClassInfo> findClass(long id);
    SchoolboyView create(SchoolboyData data);

}
