package com.digia.camel.component.iiris;

import org.apache.camel.Exchange;

/**
 * Utility class for setting and getting Camel message headers and Exchange properties.
 */
public class IirisHeaders {
	
	/**
	 * Sets Iiris message headers based on Exchange properties. 
	 * 
	 * @param exchange
	 */
	public void setFromProperties(Exchange exchange) {
		for (String field : IirisProducer.ALL_MDC_FIELDS) {
			String value = (String)exchange.getProperty(field);
			if (value != null) {
				exchange.getIn().setHeader(field, value);
			}
		}
	}
	
	/**
	 * Sets Iiris message properties based on message headers.
	 * Overrides existing properties.
	 * 
	 * @param exchange
	 */
	public void setToProperties(Exchange exchange) {
		for (String field : IirisProducer.ALL_MDC_FIELDS) {
			String value = (String)exchange.getIn().getHeader(field);
			if (value != null) {
				exchange.setProperty(field, value);
			}
		}
	}
}
