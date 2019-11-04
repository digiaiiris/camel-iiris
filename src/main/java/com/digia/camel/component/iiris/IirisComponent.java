package com.digia.camel.component.iiris;

import java.util.Map;

import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link IirisEndpoint}.
 */
public class IirisComponent extends DefaultComponent {
	
    //Each hit to producer uri ends here. Remaining hold parameter between : and ?, the rest of parameters are stored to map<key,value>
    protected IirisEndpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        IirisEndpoint endpoint = new IirisEndpoint(uri, remaining, this);
        setProperties(endpoint, parameters);
        endpoint.setStateLabel(remaining);
        
        return endpoint;
    }
}
