package com.aacoin.dig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
//@EnableDiscoveryClient
public class SmsApplication {
    private static final Logger log = LoggerFactory.getLogger(SmsApplication.class);

//    @Bean
//    public FilterRegistrationBean simpleCORSFilter() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setDispatcherTypes(DispatcherType.REQUEST);
//        registration.setFilter(new ApiOriginFilter());
//        registration.setEnabled(true);
//        registration.addUrlPatterns("/*");
//        return registration;
//    }

    public static void main(String[] args) {
        Object[] obj = {SmsApplication.class};
        SpringApplication app = new SpringApplication(obj);
        ApplicationContext context = app.run(args);
        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        for (String activeProfile : activeProfiles) {
            log.info("spring boot使用环境为: " + activeProfile);
        }
    }
}
