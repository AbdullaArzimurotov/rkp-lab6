package ru.kafpin.school;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
class JpaSchoolboyStore implements SchoolboyStore {
    private final EntityManager entityManager;

    JpaSchoolboyStore(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<SchoolboyView> search(SchoolboyFilter filter) {
        return entityManager.createQuery("""
                select p from Schoolboy p
                join fetch p.address a join fetch p.schoolClass c join fetch c.school s
                where (:surname = '' or locate(lower(:surname), lower(p.surname)) > 0)
                  and (:city = '' or locate(lower(:city), lower(a.city)) > 0)
                  and (:schoolId is null or s.id = :schoolId)
                  and (:classId is null or c.id = :classId)
                order by p.id
                """, Schoolboy.class)
                .setParameter("surname", filter.surname() == null ? "" : filter.surname().strip())
                .setParameter("city", filter.city() == null ? "" : filter.city().strip())
                .setParameter("schoolId", filter.schoolId()).setParameter("classId", filter.classId())
                .getResultList().stream().map(Schoolboy::toView).toList();
    }

    @Override
    public Optional<SchoolboyView> find(long id) {
        return Optional.ofNullable(entityManager.find(Schoolboy.class, id)).map(Schoolboy::toView);
    }

    @Override
    public List<SchoolInfo> schools() {
        return entityManager.createQuery("""
                select new ru.kafpin.school.SchoolInfo(s.id, s.name)
                from School s order by s.name
                """, SchoolInfo.class).getResultList();
    }

    @Override
    public List<ClassInfo> classes() {
        return entityManager.createQuery("""
                select new ru.kafpin.school.ClassInfo(c.id, s.id, c.name)
                from SchoolClass c join c.school s order by s.name, c.name
                """, ClassInfo.class).getResultList();
    }

    @Override
    public Optional<ClassInfo> findClass(long id) {
        return Optional.ofNullable(entityManager.find(SchoolClass.class, id))
                .map(selected -> new ClassInfo(selected.id(), selected.schoolId(), selected.name()));
    }

    @Override
    public SchoolboyView create(SchoolboyData data) {
        SchoolClass selected = entityManager.find(SchoolClass.class, data.classId());
        Schoolboy schoolboy = new Schoolboy(data, selected);
        entityManager.persist(schoolboy);
        return schoolboy.toView();
    }

}
