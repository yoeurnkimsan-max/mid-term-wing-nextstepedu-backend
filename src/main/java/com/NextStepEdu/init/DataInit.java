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
             System.out.println("ℹ️ Roles already exist in database");
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

         // Fetch universities (needed for relations)
         UniversityModel harvard = universityRepository.findBySlug("harvard").orElseThrow();
         UniversityModel oxford = universityRepository.findBySlug("oxford").orElseThrow();


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

         FacultyModel engineering = facultyRepository.findByName("Engineering Faculty").orElseThrow();
         FacultyModel business = facultyRepository.findByName("Business Faculty").orElseThrow();


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

         ProgramModel csProgram = programRepository.findByName("Computer Science MSc").orElseThrow();
         ProgramModel mbaProgram = programRepository.findByName("MBA Business Administration").orElseThrow();


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

         ScholarshipModel harvardScholarship =
                 scholarshipRepository.findBySlug("harvard-excellence").orElseThrow();


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
