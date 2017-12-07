package com.xwtec.tools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching
@EnableTransactionManagement
public class WebToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebToolsApplication.class, args);
    }

    /*
        @Bean
        public FilterRegistrationBean filterRegistrationBean() {
            FilterRegistrationBean registrationBean = new FilterRegistrationBean();
            HTTPBasicAuthorizeAttribute httpBasicFilter = new HTTPBasicAuthorizeAttribute();
            registrationBean.setFilter(httpBasicFilter);
            List<String> urlPatterns = new ArrayList<String>();
            urlPatterns.add("/user/*");
            registrationBean.setUrlPatterns(urlPatterns);
            return registrationBean;
        }*/
   /* @Bean
    public FilterRegistrationBean jwtFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        HTTPBearerAuthorizeAttribute httpBearerFilter = new HTTPBearerAuthorizeAttribute();
        registrationBean.setFilter(httpBearerFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/user/getusers");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }*/
}
