package com.quantal.quantalmicroservicetemplate.config.shared;

import com.quantal.basecomponents.objectmapper.NullSkippingOrikaBeanMapper;
import com.quantal.basecomponents.objectmapper.OrikaBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dman on 12/04/2017.
 */

@Configuration
public class SharedConfig {

    @Bean
    public NullSkippingOrikaBeanMapper nullSkippingOrikaBeanMapper() {
        return new NullSkippingOrikaBeanMapper();
    }

    @Bean
    public OrikaBeanMapper orikaBeanMapper() {
        return new OrikaBeanMapper();
    }

}
