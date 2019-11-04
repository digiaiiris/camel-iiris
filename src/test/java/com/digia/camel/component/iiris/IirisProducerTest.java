package com.digia.camel.component.iiris;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class IirisProducerTest {

	private static Logger log = LoggerFactory.getLogger(IirisProducerTest.class);

	@Test
	public void evaluateSimpleTextTest() throws Exception {
		CamelContext ctx = new DefaultCamelContext();
		Exchange exchange = new DefaultExchange(ctx);
		Message message = new DefaultMessage(ctx);
		exchange.setIn(message);
		IirisProducer producer = new IirisProducer(new IirisEndpoint());
		String value = "staticValue";
		String result = producer.evaluateSimple(exchange, value);
		Assert.assertEquals("staticValue", result);
	}

	@Test
	public void evaluateSimpleBodyTest() throws Exception {
		CamelContext ctx = new DefaultCamelContext();
		Exchange exchange = new DefaultExchange(ctx);
		Message message = new DefaultMessage(ctx);
		message.setBody("{\"id\": \"1\"}");
		exchange.setIn(message);
		IirisProducer producer = new IirisProducer(new IirisEndpoint());
		String value = "${body}";
		String result = producer.evaluateSimple(exchange, value);
		Assert.assertEquals("{\"id\": \"1\"}", result);
	}

	@Test
	public void evaluateSimpleHeaderTest() throws Exception {
		CamelContext ctx = new DefaultCamelContext();
		Exchange exchange = new DefaultExchange(ctx);
		Message message = new DefaultMessage(ctx);
		message.setHeader("someHeader", "value1");
		exchange.setIn(message);
		IirisProducer producer = new IirisProducer(new IirisEndpoint());
		String value = "Header: ${headers.someHeader}";
		String result = producer.evaluateSimple(exchange, value);
		Assert.assertEquals("Header: value1", result);
	}

	@Test
	public void evaluateSimpleNullTest() throws Exception {
		CamelContext ctx = new DefaultCamelContext();
		Exchange exchange = new DefaultExchange(ctx);
		Message message = new DefaultMessage(ctx);
		exchange.setIn(message);
		IirisProducer producer = new IirisProducer(new IirisEndpoint());
		String result = producer.evaluateSimple(exchange, null);
		Assert.assertEquals(null, result);
	}

	@Test
	public void setMdcOnlyEndpointValueTest() {
		MDC.clear();
		CamelContext ctx = new DefaultCamelContext();
		Exchange exchange = new DefaultExchange(ctx);
		Message message = new DefaultMessage(ctx);
		exchange.setIn(message);
		IirisProducer producer = new IirisProducer(new IirisEndpoint());
		producer.setMdc(exchange, "mdcField", "value1");
		String mdcValue = MDC.get("mdcField");
		Assert.assertEquals("value1", mdcValue);
	}

	@Test
	public void setMdcOnlyExchangePropertyValueTest() {
		MDC.clear();
		CamelContext ctx = new DefaultCamelContext();
		Exchange exchange = new DefaultExchange(ctx);
		exchange.setProperty("mdcField", "value2");
		IirisProducer producer = new IirisProducer(new IirisEndpoint());
		producer.setMdc(exchange, "mdcField", null);
		String mdcValue = MDC.get("mdcField");
		Assert.assertEquals("value2", mdcValue);
	}

	@Test
	public void setMdcEndpointValueWithOtherTest() {
		MDC.clear();
		CamelContext ctx = new DefaultCamelContext();
		Exchange exchange = new DefaultExchange(ctx);
		exchange.setProperty("mdcField", "value2");
		System.setProperty("mdcField", "value3");
		IirisProducer producer = new IirisProducer(new IirisEndpoint());
		producer.setMdc(exchange, "mdcField", "value1");
		String mdcValue = MDC.get("mdcField");
		Assert.assertEquals("value1", mdcValue);
	}

	@Test
	public void processTest() throws Exception {
		MDC.clear();
		CamelContext ctx = new DefaultCamelContext();
		Exchange exchange = new DefaultExchange(ctx);

		String author = "author1";
		String correlationId = "321";
		String destination = "dest1";
		String externalId = "123";
		String id = "23a8baa6-5006-11e9-8647-d663bd873d93";
		String info = "This is a message.";
		String integrationName = "Payments";
		String payloadOriginalFileName = "payments.json";
		String serverId = "node1";
		String source = "src1";
		String stateLabel = "New";

		IirisEndpoint endpoint = new IirisEndpoint();
		endpoint.setAuthor(author);
		endpoint.setCorrelationId(correlationId);
		endpoint.setDestination(destination);
		endpoint.setExternalId(externalId);
		endpoint.setId(id);
		endpoint.setInfo(info);
		endpoint.setIntegrationName(integrationName);
		endpoint.setPayloadOriginalFileName(payloadOriginalFileName);
		endpoint.setServerId(serverId);
		endpoint.setSource(source);
		endpoint.setStateLabel(stateLabel);

		IirisProducer producer = new IirisProducer(endpoint);
		producer.process(exchange);

		Assert.assertNull(MDC.get(IirisProducer.MDC_AUTHOR));
		Assert.assertEquals(correlationId, MDC.get(IirisProducer.MDC_CORRELATION_ID)); // process clears all but id fields
		Assert.assertNull(MDC.get(IirisProducer.MDC_DESTINATION));
		Assert.assertNull(MDC.get(IirisProducer.MDC_EXTERNAL_ID));
		Assert.assertEquals(id, MDC.get(IirisProducer.MDC_ID)); // process clears all but id fields
		Assert.assertNull(MDC.get(IirisProducer.MDC_INFO));
		Assert.assertNull(MDC.get(IirisProducer.MDC_INTEGRATION_NAME));
		Assert.assertNull(MDC.get(IirisProducer.MDC_PAYLOAD_ORIGINAL_FILE_NAME));
		Assert.assertNull(MDC.get(IirisProducer.MDC_SERVER_ID));
		Assert.assertNull(MDC.get(IirisProducer.MDC_SOURCE));
		Assert.assertNull(MDC.get(IirisProducer.MDC_STATE_LABEL));
	}
}
