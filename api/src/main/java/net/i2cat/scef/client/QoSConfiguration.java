package net.i2cat.scef.client;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Component
@ConfigurationProperties(prefix = "pcf.qos")
@Data
public class QoSConfiguration {

    private Map<String, String> references;
    private Map<String, String> qosE;
    private Map<String, String> qosS;
    private Map<String, String> qosM;
    private Map<String, String> qosL;
}

