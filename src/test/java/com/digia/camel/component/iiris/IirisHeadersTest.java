package com.digia.camel.component.iiris;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.junit.Assert;
import org.junit.Test;

public class IirisHeadersTest {

	@Test
	public void setFromPropertiesTest() {
		String id = "123e4567-e89b-12d3-a456-426655440000";
		
		CamelContext ctx = new DefaultCamelContext();
		Exchange exchange = new DefaultExchange(ctx);
		Message message = new DefaultMessage(ctx);
		IirisHeaders ih = new IirisHeaders();
		exchange.setIn(message);
		
		Assert.assertNull(message.getHeader(IirisProducer.MDC_ID));
		exchange.setProperty(IirisProducer.MDC_ID, id);
		ih.setFromProperties(exchange);
		Assert.assertEquals(id, message.getHeader(IirisProducer.MDC_ID));
	}
	
	@Test
	public void setToPropertiesTest() {
		String id = "123e4567-e89b-12d3-a456-426655440000";
		
		CamelContext ctx = new DefaultCamelContext();
		Exchange exchange = new DefaultExchange(ctx);
		Message message = new DefaultMessage(ctx);
		IirisHeaders ih = new IirisHeaders();
		exchange.setIn(message);
		
		Assert.assertNull(exchange.getProperty(IirisProducer.MDC_ID));
		message.setHeader(IirisProducer.MDC_ID, id);
		ih.setToProperties(exchange);
		Assert.assertEquals(id, exchange.getProperty(IirisProducer.MDC_ID));
	}
}
