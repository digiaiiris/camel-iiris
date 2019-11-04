package com.digia.camel.component.iiris;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.apache.camel.builder.SimpleBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The iiris producer. Uses simple language to evaluate values from uri.
 * Puts values into MDC fields which are used in log4j configuration.
 */
public class IirisProducer extends DefaultProducer {
	
    private static final Logger LOG = LoggerFactory.getLogger(IirisProducer.class);

    public static final String MDC_AUTHOR = "iirisAuthor";
    public static final String MDC_CORRELATION_ID = "iirisCorrelationId";
    public static final String MDC_DESTINATION = "iirisDestination";
    public static final String MDC_EXTERNAL_ID = "iirisExternalId";
    public static final String MDC_ID = "iirisId";
    public static final String MDC_INFO = "iirisInfo";
    public static final String MDC_INTEGRATION_NAME = "iirisIntegrationName";
    public static final String MDC_PAYLOAD_ORIGINAL_FILE_NAME = "iirisPayloadOriginalFileName";
    public static final String MDC_SERVER_ID = "iirisServerId";
    public static final String MDC_SOURCE = "iirisSource";
    public static final String MDC_STATE_LABEL = "iirisStateLabel";
    
    public static final String[] ALL_MDC_FIELDS = new String[]{
    		MDC_AUTHOR,
    		MDC_CORRELATION_ID,
    		MDC_DESTINATION,
    		MDC_EXTERNAL_ID,
    		MDC_ID,
    		MDC_INFO,
    		MDC_INTEGRATION_NAME,
    		MDC_PAYLOAD_ORIGINAL_FILE_NAME,
    		MDC_SERVER_ID,
    		MDC_SOURCE,
    		MDC_STATE_LABEL
    		};
    public static final String[] MDC_ID_FIELDS = new String[]{
    		MDC_CORRELATION_ID,
    		MDC_ID
    		};
    
    private IirisEndpoint endpoint;

    public IirisProducer(IirisEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
    	setMdc(exchange, MDC_AUTHOR, endpoint.getAuthor());
    	setMdc(exchange, MDC_CORRELATION_ID, endpoint.getCorrelationId());
    	setMdc(exchange, MDC_DESTINATION, endpoint.getDestination());
    	setMdc(exchange, MDC_EXTERNAL_ID, endpoint.getExternalId());
        setMdc(exchange, MDC_ID, endpoint.getId());
        setMdc(exchange, MDC_INFO, endpoint.getInfo());
        setMdc(exchange, MDC_INTEGRATION_NAME, endpoint.getIntegrationName());
        setMdc(exchange, MDC_PAYLOAD_ORIGINAL_FILE_NAME, endpoint.getPayloadOriginalFileName());
        setMdc(exchange, MDC_SERVER_ID, endpoint.getServerId());
        setMdc(exchange, MDC_SOURCE, endpoint.getSource());
        setMdc(exchange, MDC_STATE_LABEL, endpoint.getStateLabel());

        String message = evaluateSimple(exchange, endpoint.getInfo());
        if (message == null) {
        	LOG.info("");
        } else {
        	LOG.info(message);
        }
        clearFields(false);
    }
    
    /**
     * Id fields can be left to MDC to get those logged from other loggers, for example org.slf4j.Logger from a bean.
     * 
     * @param alsoIdFields if id fields must be also cleared
     */
    protected void clearFields(boolean alsoIdFields) {
    	List<String> allFields = Arrays.asList(ALL_MDC_FIELDS);
    	List<String> idFields = Arrays.asList(MDC_ID_FIELDS);
    	for (String field : allFields) {
    		if (!idFields.contains(field) || alsoIdFields) {
    			MDC.remove(field);
    		}
    	}
    }
    
    protected String evaluateSimple(Exchange exchange, String value) {
    	if (value != null) {
    		SimpleBuilder simpleBuilder = SimpleBuilder.simple(value);
    		
            return simpleBuilder.evaluate(exchange, String.class);
    	} else {
    		return value;
    	}
    }
    
    protected void setMdc(Exchange exchange, String mdcFieldName, String valueFromEndpoint) {
    	// value from route component
    	String value = valueFromEndpoint; 
    	if (value == null) {
    		// value from exchange property
    		value = exchange.getProperty(mdcFieldName, String.class);
    		if (value == null) {
    			// value from system property
    			value = System.getProperty(mdcFieldName);
    			if (value == null) {
    				// value from system environment
    				Map<String,String> env = System.getenv();
    				value = env.get(mdcFieldName);
    			}
    		}
    	}
    	
    	if (value != null) {
    		MDC.put(mdcFieldName, evaluateSimple(exchange, value));
    	}
    }
}
