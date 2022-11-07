/**
 *
 */
package org.poc.bloodbank;

import org.poc.bloodbank.property.IntegrationConfigProperties;
import org.poc.bloodbank.property.ServicecConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Sankha
 *
 */

@EnableConfigurationProperties({IntegrationConfigProperties.class, ServicecConfigProperties.class})
//@EnableAsync
@EnableDiscoveryClient
@SpringBootApplication
public class BloodBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(BloodBankApplication.class, args);
    }
}
