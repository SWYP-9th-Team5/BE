package swyp.team5.greening.common.config;

import java.time.LocalDate;
import java.util.function.Supplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfig {

    @Bean("nowLocalDateSupplier")
    public Supplier<LocalDate> nowLocalDateSupplier() {
        return LocalDate::now;
    }

}
