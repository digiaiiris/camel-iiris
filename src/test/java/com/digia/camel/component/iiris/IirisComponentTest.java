package com.digia.camel.component.iiris;

import org.apache.camel.*;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.digia.camel.component.iiris.IirisEndpoint;
import com.digia.camel.component.iiris.IirisProducer;

public class IirisComponentTest extends CamelTestSupport {
	
    private static final Logger LOG = LoggerFactory.getLogger(IirisComponentTest.class);

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Test
    // test route from:iiris which is illegal and should throw an exception
    public void testFromNotSupported() throws Exception {
        try {
            context.getRouteDefinitions().get(0).adviceWith(context, new AdviceWithRouteBuilder() {
                @Override
                public void configure() throws Exception {
                    replaceFromWith("iiris:incomponent");   // replacing from
                }
            });
            context.start();
            MockEndpoint mock = getMockEndpoint("mock:result");
            mock.expectedMinimumMessageCount(0);
            template.sendBody("iiris:incomponent", "true");
            assertMockEndpointsSatisfied();
            context.stop();
        } catch (Exception e) {
            LOG.info("Exception:" + e.toString());
        }
    }

    /*
    @Test
    public void testIiris() throws Exception {
        context.setUseMDCLogging(true);
        context.start();
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);
        template.sendBody("direct:start", "true");
        assertMockEndpointsSatisfied();
    }
    */
    
    @Test
    public void testOtherKeysNotClearedFromMdc1() {
    	MDC.put("someOtherKey", "value");
        IirisProducer test = new IirisProducer(new IirisEndpoint());
        test.clearFields(false);
        assertNotNull(MDC.get("someOtherKey"));
    }
    
    @Test
    public void testOtherKeysNotClearedFromMdc2() {
    	MDC.put("someOtherKey", "value");
        IirisProducer test = new IirisProducer(new IirisEndpoint());
        test.clearFields(true);
        assertNotNull(MDC.get("someOtherKey"));
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
    	
        return new RouteBuilder() {
        	
            public void configure() throws Exception {
                from("direct:start").routeId("route1")
                  .to("iiris:New?id=1&correlationId=${body}")
                  .setHeader("cool", simple("Body ${body}"))
                  .log(LoggingLevel.INFO, "Normal log works as well ${id}")
                  .to("mock:result");
            }
        };
    }
}
