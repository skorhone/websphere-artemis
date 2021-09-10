# websphere-artemis
This repository includes documentation and examples for integrating WebSphere Application Server Classic/Liberty with Apache ActiveMQ Artemis

*Warning*: A lot of time has passed since this project was created. Jboss logging and activemq-artemis have been patched in recent versions. While I have not tested, I would expect that commons-beansutils version has been replaced in WebSphere.

# WebSphere Application Server Classic Issues
Websphere Application Server Classic has severe classloading issues. This chapter describes how these issues have been dealt with

*Issue: getPackage() returns null in classes loaded inside a resource adapter*
 * Fix: LoggerProvider in jboss-logging has been patched (1 line)
 * Fix: ActiveMQRALogger in artemis-ra has been patched (1 line)

*Issue: apache-commons inside resource adapter breaks admin console (Why!)*
 * Fix: PropertyUtils in apache-commons has been patched to include required methods

# Building artifact
Dependencies have to be built before building the resource adapter

Procedure:
 1. Clone and build patched jboss-logging from https://github.com/skorhone/jboss-logging 3.3.1-NPE branch
 2. Clone and build patched artemis-ra from https://github.com/skorhone/activemq-artemis.git 2.4.0-NPE branch
 3. Clone and build patched apache-commons from https://github.com/skorhone/commons-beanutils.git 1.9.3-IBM branch
 4. Clone and build this resource adapter

# Configuring servers

## Websphere Liberty
WebSphere Liberty configuration is done using server.xml.

Procedure:
 1. Create a copy of .rar on your server
 2. Specify resource adapter in server.xml
 3. Create connection factory (if required) in server.xml
 4. Create activation specification (if required) in server.xml
  
### server.xml: Resource adapter
    <resourceAdapter autoStart="true" id="artemis" location="${shared.resource.dir}/artemis/websphere-artemis-rar-2.4.0.1.rar">
        <properties.artemis connectionParameters="host=localhost;port=61616"/>
    </resourceAdapter>
	
### server.xml: Connection factory 
    <connectionFactory id="ArtemisCF" jndiName="jms/myCF">
        <connectionManager agedTimeout="60m" connectionTimeout="30s" maxIdleTime="30m" maxPoolSize="10" reapTime="3m"/>
        <properties.artemis/>
    </connectionFactory>
    
### server.xml: Activation specification
    <jmsActivationSpec id="my-application-0.0.1-SNAPSHOT/ConsumerBean">
        <properties.artemis clientID="MyClient" destination="MyTopic" destinationType="javax.jms.Topic" shareSubscriptions="true" subscriptionDurability="Durable" subscriptionName="MySubscription" useJndi="false"/>
    </jmsActivationSpec>
    
### server.xml: Trace logging
    <logging traceSpecification="*=info:org.apache.activemq.artemis.*=all"/>


## WebSphere Application Server Classic
WebSphere Application Server configuration can be done using scripts or admin console.

Procedure:
 1. Install resource adapter using scripts / admin console
 2. Create an authentication alias (mandatory)
 3. Create a connection factory (if required)
 4. Create an activation specification (if required)
