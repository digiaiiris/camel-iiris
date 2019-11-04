package com.digia.camel.component.iiris;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a iiris endpoint.
 */
@UriEndpoint(firstVersion = "1.0", scheme = "iiris", title = "iiris", syntax="iiris:stateLabel", producerOnly = true,
             consumerClass = IirisComponent.class, label = "custom")
public class IirisEndpoint extends DefaultEndpoint {
    
    // New, Processing, Warning, Failure, Done
    @UriPath @Metadata(required = "true")
    private String stateLabel;
    
    @UriParam
    private String author;
    @UriParam
    private String correlationId;
    @UriParam
    private String destination;
    @UriParam
    private String externalId;
    @UriParam
    private String id;
    @UriParam
    private String info;
    @UriParam
    private String integrationName;
    @UriParam
    private String payloadOriginalFileName;
    @UriParam
    private String serverId;
    @UriParam
    private String source;

    public IirisEndpoint() {}

    public IirisEndpoint(String uri, String remaining, IirisComponent component) {
        super(uri, component);
        this.stateLabel = remaining;
    }

    public Producer createProducer() throws Exception {
        return new IirisProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        throw new UnsupportedOperationException("Cannot consume from an IirisEndpoint: " + getEndpointUri());
    }

    public boolean isSingleton() {
        return true;
    }

    /**
     * Id combining log messages into a flow. 
     * 
     * @param option
     */
    public void setId(String option) {
        this.id = option;
    }

    public String getId() {
        return id;
    }

    /**
     * Describes the processing state. Possible values are:
     * New
     * Processing
     * Warning
     * Failure
     * Done
     * 
     * @param option
     */
    public void setStateLabel(String option) {
        this.stateLabel = option;
    }

    public String getStateLabel() {
        return stateLabel;
    }

    /**
     * Server on which the flow is running. Usually this is set from environment.
     * 
     * @param option
     */
    public void setServerId(String option) {
        this.serverId = option;
    }

    public String getServerId() {
        return serverId;
    }
    
    /**
     * Some external id for example order id.
     * 
     * @param option
     */
    public void setExternalId(String option) {
        this.externalId = option;
    }

    public String getExternalId() {
        return externalId;
    }
    
    /**
     * Reference to other integration flow. For example parent flow.
     * 
     * @param option
     */
    public void setCorrelationId(String option) {
        this.correlationId = option;
    }

    public String getCorrelationId() {
        return correlationId;
    }
    
    /**
     * Organization developing the integration.
     * 
     * @param option
     */
    public void setAuthor(String option) {
        this.author = option;
    }

    public String getAuthor() {
        return author;
    }
    
    /**
     * Info, the message.
     * 
     * @param option
     */
    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
    
    /**
     * Source system id.
     * 
     * @param option
     */
    public void setSource(String option) {
        this.source = option;
    }

    public String getSource() {
        return source;
    }
    
    /**
     * Destination system id.
     * 
     * @param option
     */
    public void setDestination(String option) {
        this.destination = option;
    }

    public String getDestination() {
        return destination;
    }
    
    /**
     * Integration name.
     * 
     * @param option
     */
    public void setIntegrationName(String option) {
        this.integrationName = option;
    }

    public String getIntegrationName() {
        return integrationName;
    }
    
    /**
     * Original file name.
     * 
     * @param option
     */
    public void setPayloadOriginalFileName(String option) {
        this.payloadOriginalFileName = option;
    }

    public String getPayloadOriginalFileName() {
        return payloadOriginalFileName;
    }
}
