# websphere-artemis
This project includes documentation and examples for integrating Apache ActiveMQ to WebSphere Application Server Classic/Liberty

# Notes
Currently Artemis requires access to TransactionManager interface. Related code is located websphere-artemis-tx submodule. While accessing TransactionManager directly from ResourceAdapter is considered can be considered as a bad practice, the way that Artemis uses it is reasonably safe. ResourceAdapters in general should avoid use of TransactionManager and use TransactionSynchronizationRegistry provided in BootstrapContext.

# Websphere Liberty
WebSphere Liberty integration is done using server.xml. 

Procedure:
 1. Create a copy of .rar on your server
 2. Specify resource adapter in server.xml
 3. Create connection factory (if required) in server.xml
 4. Create activation specification (if required) in server.xml
  
## server.xml: Resource adapter
    <resourceAdapter autoStart="true" id="artemis" location="${shared.resource.dir}/artemis/websphere-artemis-rar-2.3.0.rar" />
	
## server.xml: Connection factory 
    <connectionFactory id="ArtemisCF" jndiName="jms/myCF">
        <connectionManager agedTimeout="60m" connectionTimeout="30s" maxIdleTime="30m" maxPoolSize="10" reapTime="3m"/>
        <properties.artemis/>
    </connectionFactory>
    
## server.xml: Activation specification
    <jmsActivationSpec id="my-application-0.0.1-SNAPSHOT/ConsumerBean">
        <properties.artemis clientID="MyClient" destination="MyTopic" destinationType="javax.jms.Topic" shareSubscriptions="true" subscriptionDurability="Durable" subscriptionName="MySubscription" useJndi="false"/>
    </jmsActivationSpec>
    
## server.xml: Trace logging
    <logging traceSpecification="*=info:org.apache.activemq.artemis.*=all"/>