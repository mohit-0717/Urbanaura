package com.urbanaura.urbanaura.config;

import com.urbanaura.urbanaura.service.DataBackfillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BackfillBootstrapConfig {

    private static final Logger log = LoggerFactory.getLogger(BackfillBootstrapConfig.class);

    @Bean
    public CommandLineRunner runBackfillOnStartup(
            DataBackfillService dataBackfillService,
            @Value("${urbanaura.backfill.on-startup:true}") boolean backfillOnStartup) {
        return args -> {
            if (!backfillOnStartup) {
                log.info("Startup backfill disabled via urbanaura.backfill.on-startup=false");
                return;
            }
            DataBackfillService.BackfillSummary summary = dataBackfillService.backfillMissingData();
            log.info("Startup backfill finished. propertiesScanned={}, smartCreated={}, smartUpdated={}, quickCreated={}, quickUpdated={}, aqiSeeded={}",
                    summary.propertiesScanned(),
                    summary.smartMetricsCreated(),
                    summary.smartMetricsUpdated(),
                    summary.quickCommerceCreated(),
                    summary.quickCommerceUpdated(),
                    summary.aqiSeeded());
        };
    }
}
