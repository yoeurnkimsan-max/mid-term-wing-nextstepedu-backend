
package com.NextStepEdu.init;

import com.NextStepEdu.models.*;
import com.NextStepEdu.repositories.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final RoleRepository roleRepository;
    private final UniversityRepository universityRepository;
    private final FacultyRepository facultyRepository;
    private final ProgramRepository programRepository;
    private final ScholarshipRepository scholarshipRepository;
    private final UniversityContactRepository universityContactRepository;
    private final ScholarshipContactRepository scholarshipContactRepository;

    @PostConstruct
    void init() {

        // ===============================
        // 0) ROLES (UPSERT)
        // ===============================
        if (roleRepository.count() == 0) {
            RoleModel user = new RoleModel();
            user.setName("USER");

            RoleModel admin = new RoleModel();
            admin.setName("ADMIN");

            roleRepository.saveAll(List.of(user, admin));
            System.out.println("✅ Roles initialized: USER, ADMIN");
        } else {
            System.out.println("ℹ️ Roles already exist in database");
        }

        // ===============================
        // 1) UNIVERSITIES (UPSERT)
        // ===============================
        UniversityModel harvard = universityRepository.findBySlug("harvard")
                .orElseGet(() -> {
                    UniversityModel u = new UniversityModel();
                    u.setName("Harvard University");
                    u.setSlug("harvard");
                    u.setCountry("USA");
                    u.setCity("Cambridge");
                    u.setStatus("ACTIVE");
                    u.setCreatedAt(LocalDateTime.now());
                    u.setUpdatedAt(LocalDateTime.now());
                    return universityRepository.save(u);
                });

        UniversityModel oxford = universityRepository.findBySlug("oxford")
                .orElseGet(() -> {
                    UniversityModel u = new UniversityModel();
                    u.setName("Oxford University");
                    u.setSlug("oxford");
                    u.setCountry("UK");
                    u.setCity("Oxford");
                    u.setStatus("ACTIVE");
                    u.setCreatedAt(LocalDateTime.now());
                    u.setUpdatedAt(LocalDateTime.now());
                    return universityRepository.save(u);
                });

        // ===============================
        // 2) FACULTIES (UPSERT)
        // ===============================
        FacultyModel engineering = facultyRepository.findByName("Engineering Faculty")
                .orElseGet(() -> {
                    FacultyModel f = new FacultyModel();
                    f.setName("Engineering Faculty");
                    f.setDescription("Engineering and Technology programs");
                    f.setUniversity(harvard);
                    return facultyRepository.save(f);
                });

        FacultyModel business = facultyRepository.findByName("Business Faculty")
                .orElseGet(() -> {
                    FacultyModel f = new FacultyModel();
                    f.setName("Business Faculty");
                    f.setDescription("Business and Management programs");
                    f.setUniversity(oxford);
                    return facultyRepository.save(f);
                });

        // ===============================
        // 3) PROGRAMS (UPSERT)
        // ===============================
        ProgramModel csProgram = programRepository.findByName("Computer Science MSc")
                .orElseGet(() -> {
                    ProgramModel p = new ProgramModel();
                    p.setName("Computer Science MSc");
                    p.setDescription("Master program in Computer Science");
                    p.setDegreeLevel(2);
                    p.setExamRequired(true);
                    p.setTuitionFeeAmount(20000.0);
                    p.setCurrency("USD");
                    p.setStudyPeriodMonths(24);
                    p.setUniversity(harvard);
                    p.setFaculty(engineering);
                    return programRepository.save(p);
                });

        ProgramModel mbaProgram = programRepository.findByName("MBA Business Administration")
                .orElseGet(() -> {
                    ProgramModel p = new ProgramModel();
                    p.setName("MBA Business Administration");
                    p.setDescription("MBA program for future leaders");
                    p.setDegreeLevel(2);
                    p.setExamRequired(false);
                    p.setTuitionFeeAmount(25000.0);
                    p.setCurrency("GBP");
                    p.setStudyPeriodMonths(18);
                    p.setUniversity(oxford);
                    p.setFaculty(business);
                    return programRepository.save(p);
                });

        // ===============================
        // 4) SCHOLARSHIPS (UPSERT)
        // ===============================
        ScholarshipModel harvardScholarship = scholarshipRepository.findBySlug("harvard-excellence")
                .orElseGet(() -> {
                    ScholarshipModel s = new ScholarshipModel();
                    s.setName("Harvard Excellence Scholarship");
                    s.setSlug("harvard-excellence");
                    s.setDescription("Full scholarship for outstanding students");
                    s.setLevel(1);
                    s.setStatus("OPEN");
                    s.setDeadline(LocalDateTime.now().plusMonths(3));
                    s.setProgram(csProgram);
                    s.setUniversity(harvard);
                    s.setCreatedAt(LocalDateTime.now());
                    s.setUpdatedAt(LocalDateTime.now());
                    return scholarshipRepository.save(s);
                });

        ScholarshipModel oxfordScholarship = scholarshipRepository.findBySlug("oxford-leadership")
                .orElseGet(() -> {
                    ScholarshipModel s = new ScholarshipModel();
                    s.setName("Oxford Leadership Scholarship");
                    s.setSlug("oxford-leadership");
                    s.setDescription("Scholarship for MBA leadership students");
                    s.setLevel(2);
                    s.setStatus("OPEN");
                    s.setDeadline(LocalDateTime.now().plusMonths(4));
                    s.setProgram(mbaProgram);
                    s.setUniversity(oxford);
                    s.setCreatedAt(LocalDateTime.now());
                    s.setUpdatedAt(LocalDateTime.now());
                    return scholarshipRepository.save(s);
                });

        // ===============================
        // 5) UNIVERSITY CONTACTS (UPSERT by label + university)
        // (Simple: create only if table empty)
        // ===============================
        if (universityContactRepository.count() == 0) {
            UniversityContactModel contact1 = new UniversityContactModel();
            contact1.setLabel("Admissions Office");
            contact1.setEmail("admissions@harvard.edu");
            contact1.setPhone("+1-111-222-3333");
            contact1.setWebsiteUrl("https://www.harvard.edu");
            contact1.setUniversity(harvard);

            UniversityContactModel contact2 = new UniversityContactModel();
            contact2.setLabel("International Office");
            contact2.setEmail("international@ox.ac.uk");
            contact2.setPhone("+44-555-666-7777");
            contact2.setWebsiteUrl("https://www.ox.ac.uk");
            contact2.setUniversity(oxford);

            universityContactRepository.saveAll(List.of(contact1, contact2));
            System.out.println("✅ University Contacts initialized");
        } else {
            System.out.println("ℹ️ University Contacts already exist in database");
        }

        // ===============================
        // 6) SCHOLARSHIP CONTACTS (Simple create only if empty)
        // ===============================
        if (scholarshipContactRepository.count() == 0) {
            ScholarshipContactModel sc1 = new ScholarshipContactModel();
            sc1.setLabel("Scholarship Support Office");
            sc1.setEmail("scholarships@harvard.edu");
            sc1.setPhone("+1-999-888-7777");
            sc1.setWebsiteUrl("https://www.harvard.edu/scholarships");
            sc1.setScholarship(harvardScholarship);

            scholarshipContactRepository.save(sc1);
            System.out.println("✅ Scholarship Contacts initialized");
        } else {
            System.out.println("ℹ️ Scholarship Contacts already exist in database");
        }

        System.out.println("🎉 DataInit done (safe upsert mode)!");
    }
}

