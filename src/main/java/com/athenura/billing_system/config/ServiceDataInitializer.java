// package com.athenura.billing_system.config;

// import com.athenura.billing_system.service.entity.ServiceEntity;
// import com.athenura.billing_system.service.repository.ServiceRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;

// @Component
// @RequiredArgsConstructor
// public class ServiceDataInitializer implements CommandLineRunner {

//     private final ServiceRepository serviceRepository;

//     @Override
//     public void run(String... args) {

//         if (serviceRepository.count() == 0) {

//             serviceRepository.save(
//                     ServiceEntity.builder()
//                             .serviceName("Custom Software Development")
//                             .description("Tailored enterprise solutions")
//                             .basePrice(50000.0)
//                             .gstPercentage(18.0)
//                             .active(true)
//                             .build()
//             );

//             serviceRepository.save(
//                     ServiceEntity.builder()
//                             .serviceName("Website Development")
//                             .description("Corporate & dynamic websites")
//                             .basePrice(30000.0)
//                             .gstPercentage(18.0)
//                             .active(true)
//                             .build()
//             );

//             serviceRepository.save(
//                     ServiceEntity.builder()
//                             .serviceName("AI & Machine Learning")
//                             .description("AI automation & analytics")
//                             .basePrice(80000.0)
//                             .gstPercentage(18.0)
//                             .active(true)
//                             .build()
//             );

//             serviceRepository.save(
//                     ServiceEntity.builder()
//                             .serviceName("Cloud Solutions & DevOps")
//                             .description("Cloud infra & CI/CD pipelines")
//                             .basePrice(60000.0)
//                             .gstPercentage(18.0)
//                             .active(true)
//                             .build()
//             );

//             serviceRepository.save(
//                     ServiceEntity.builder()
//                             .serviceName("UI/UX Design")
//                             .description("Product UI & UX design")
//                             .basePrice(20000.0)
//                             .gstPercentage(18.0)
//                             .active(true)
//                             .build()
//             );

//             serviceRepository.save(
//                     ServiceEntity.builder()
//                             .serviceName("IT Consulting")
//                             .description("Strategic technology consulting")
//                             .basePrice(40000.0)
//                             .gstPercentage(18.0)
//                             .active(true)
//                             .build()
//             );
//         }
//     }
// }

