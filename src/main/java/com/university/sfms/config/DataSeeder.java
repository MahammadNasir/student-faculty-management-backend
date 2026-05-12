//package com.university.sfms.config;
//
//import com.university.sfms.model.Role;
//import com.university.sfms.model.RoleName;
//import com.university.sfms.model.User;
//import com.university.sfms.repository.RoleRepository;
//import com.university.sfms.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import java.util.Arrays;
//
//@Component
//public class DataSeeder implements CommandLineRunner {
//    private final RoleRepository roles;
//    private final UserRepository users;
//    private final PasswordEncoder encoder;
////    @Value("${app.superadmin.user-id}") 
//    private String userId;
////    @Value("${app.superadmin.name}") 
//    private String name;
////    @Value("${app.superadmin.email}") 
//    private String email;
////    @Value("${app.superadmin.password}") 
//    private String password;
//
//    public DataSeeder(RoleRepository roles, UserRepository users, PasswordEncoder encoder) {
//        this.roles = roles;
//        this.users = users;
//        this.encoder = encoder;
//    }
//
//    @Override
//    @Transactional
//    public void run(String... args) {
//        Arrays.stream(RoleName.values()).forEach(role -> roles.findByName(role).orElseGet(() -> roles.save(new Role(role))));
//        if (!users.existsByEmail(email)) {
//            User admin = new User();
//            admin.setUserId(userId);
//            admin.setName(name);
//            admin.setEmail(email);
//            admin.setPassword(encoder.encode(password));
//            admin.setRole(roles.findByName(RoleName.SUPERADMIN).orElseThrow());
//            users.save(admin);
//        }
//    }
//}
