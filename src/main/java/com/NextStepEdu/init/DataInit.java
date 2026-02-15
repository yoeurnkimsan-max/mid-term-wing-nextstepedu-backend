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
        // 0) ROLES
        // ===============================
        if (roleRepository.count() == 0) {
            RoleModel user = new RoleModel();
            user.setName("USER");

            RoleModel admin = new RoleModel();
            admin.setName("ADMIN");

            roleRepository.saveAll(List.of(user, admin));
            System.out.println("✅ Roles initialized: USER, ADMIN");
        } else {
            // Ensure USER role exists
            RoleModel userRole = roleRepository.findRoleUser();
            if (userRole == null) {
                RoleModel user = new RoleModel();
                user.setName("USER");
                roleRepository.save(user);
                System.out.println("✅ Role created: USER");
            }

            // Ensure ADMIN role exists
            RoleModel adminRole = roleRepository.findRoleAdmin();
            if (adminRole == null) {
                RoleModel admin = new RoleModel();
                admin.setName("ADMIN");
                roleRepository.save(admin);
                System.out.println("✅ Role created: ADMIN");
            }

            System.out.println("ℹ️ Roles already exist in database (ensured USER/ADMIN)");
        }


        // ===============================
        // 1) UNIVERSITIES
        // ===============================
        if (universityRepository.count() == 0) {

            UniversityModel uni1 = new UniversityModel();
            uni1.setName("Harvard University");
            uni1.setSlug("harvard");
            uni1.setCountry("USA");
            uni1.setCity("Cambridge");
            uni1.setStatus("ACTIVE");
            uni1.setCreatedAt(LocalDateTime.now());
            uni1.setUpdatedAt(LocalDateTime.now());

            UniversityModel uni2 = new UniversityModel();
            uni2.setName("Oxford University");
            uni2.setSlug("oxford");
            uni2.setCountry("UK");
            uni2.setCity("Oxford");
            uni2.setStatus("ACTIVE");
            uni2.setCreatedAt(LocalDateTime.now());
            uni2.setUpdatedAt(LocalDateTime.now());

            universityRepository.saveAll(List.of(uni1, uni2));
            System.out.println("✅ Universities initialized");
        } else {
            System.out.println("ℹ️ Universities already exist in database");
        }

        // Fetch universities (needed for relations) - FIXED (find-or-create)
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
                    System.out.println("✅ University created (missing): harvard");
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
                    System.out.println("✅ University created (missing): oxford");
                    return universityRepository.save(u);
                });


        // ===============================
        // 2) FACULTIES
        // ===============================
        if (facultyRepository.count() == 0) {

            FacultyModel faculty1 = new FacultyModel();
            faculty1.setName("Engineering Faculty");
            faculty1.setDescription("Engineering and Technology programs");
            faculty1.setUniversity(harvard);

            FacultyModel faculty2 = new FacultyModel();
            faculty2.setName("Business Faculty");
            faculty2.setDescription("Business and Management programs");
            faculty2.setUniversity(oxford);

            facultyRepository.saveAll(List.of(faculty1, faculty2));
            System.out.println("✅ Faculties initialized");
        } else {
            System.out.println("ℹ️ Faculties already exist in database");
        }

        // Fetch faculties - FIXED (find-or-create)
        FacultyModel engineering = facultyRepository.findByName("Engineering Faculty")
                .orElseGet(() -> {
                    FacultyModel f = new FacultyModel();
                    f.setName("Engineering Faculty");
                    f.setDescription("Engineering and Technology programs");
                    f.setUniversity(harvard);
                    System.out.println("✅ Faculty created (missing): Engineering Faculty");
                    return facultyRepository.save(f);
                });

        FacultyModel business = facultyRepository.findByName("Business Faculty")
                .orElseGet(() -> {
                    FacultyModel f = new FacultyModel();
                    f.setName("Business Faculty");
                    f.setDescription("Business and Management programs");
                    f.setUniversity(oxford);
                    System.out.println("✅ Faculty created (missing): Business Faculty");
                    return facultyRepository.save(f);
                });


        // ===============================
        // 3) PROGRAMS
        // ===============================
        if (programRepository.count() == 0) {

            ProgramModel program1 = new ProgramModel();
            program1.setName("Computer Science MSc");
            program1.setDescription("Master program in Computer Science");
            program1.setDegreeLevel(2);
            program1.setExamRequired(true);
            program1.setTuitionFeeAmount(20000.0);
            program1.setCurrency("USD");
            program1.setStudyPeriodMonths(24);
            program1.setUniversity(harvard);
            program1.setFaculty(engineering);

            ProgramModel program2 = new ProgramModel();
            program2.setName("MBA Business Administration");
            program2.setDescription("MBA program for future leaders");
            program2.setDegreeLevel(2);
            program2.setExamRequired(false);
            program2.setTuitionFeeAmount(25000.0);
            program2.setCurrency("GBP");
            program2.setStudyPeriodMonths(18);
            program2.setUniversity(oxford);
            program2.setFaculty(business);

            programRepository.saveAll(List.of(program1, program2));
            System.out.println("✅ Programs initialized");
        } else {
            System.out.println("ℹ️ Programs already exist in database");
        }

        // Fetch programs - FIXED (find-or-create)
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
                    System.out.println("✅ Program created (missing): Computer Science MSc");
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
                    System.out.println("✅ Program created (missing): MBA Business Administration");
                    return programRepository.save(p);
                });


        // ===============================
        // 4) SCHOLARSHIPS
        // ===============================
        if (scholarshipRepository.count() == 0) {

            ScholarshipModel scholarship1 = new ScholarshipModel();
            scholarship1.setName("Harvard Excellence Scholarship");
            scholarship1.setSlug("harvard-excellence");
            scholarship1.setDescription("Full scholarship for outstanding students");
            scholarship1.setLevel(1);
            scholarship1.setStatus("OPEN");
            scholarship1.setDeadline(LocalDateTime.now().plusMonths(3));
            scholarship1.setProgram(csProgram);
            scholarship1.setUniversity(harvard);
            scholarship1.setCreatedAt(LocalDateTime.now());
            scholarship1.setUpdatedAt(LocalDateTime.now());

            ScholarshipModel scholarship2 = new ScholarshipModel();
            scholarship2.setName("Oxford Leadership Scholarship");
            scholarship2.setSlug("oxford-leadership");
            scholarship2.setDescription("Scholarship for MBA leadership students");
            scholarship2.setLevel(2);
            scholarship2.setStatus("OPEN");
            scholarship2.setDeadline(LocalDateTime.now().plusMonths(4));
            scholarship2.setProgram(mbaProgram);
            scholarship2.setUniversity(oxford);
            scholarship2.setCreatedAt(LocalDateTime.now());
            scholarship2.setUpdatedAt(LocalDateTime.now());

            scholarshipRepository.saveAll(List.of(scholarship1, scholarship2));
            System.out.println("✅ Scholarships initialized");
        } else {
            System.out.println("ℹ️ Scholarships already exist in database");
        }

        // Fetch scholarship - FIXED (find-or-create)
        ScholarshipModel harvardScholarship =
                scholarshipRepository.findBySlug("harvard-excellence")
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
                            System.out.println("✅ Scholarship created (missing): harvard-excellence");
                            return scholarshipRepository.save(s);
                        });


        // ===============================
        // 5) UNIVERSITY CONTACTS
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
        // 6) SCHOLARSHIP CONTACTS
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

        System.out.println("🎉 All demo data initialized successfully!");
    }
}
