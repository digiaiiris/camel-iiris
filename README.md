# Camel Iiris Component

## Introduction

Logger component which logs Digia Iiris JSON format. Also, it logs miscellaneous java logging. It logs to system out which should be forwarded to Elastic index using proper tooling, for example Iiris component or plain Filebeat etc.

Features:
* Camel simple language can be used in fields.

## Usage

```
iiris:stateLabel[?options]
```

Valid stateLabel values are: **New**, **Processing**, **Warning**, **Failure**, **Done**. Failure and Done are the only finished statuses, so that message won't be considered being stuck.

| **Option**              | **Property Name (exchange, system, environment)** | **Mandatory** | **Description** |
| ----------------------- | ------------------------------------------------- | ------------- | --------------- |
| author                  | iirisAuthor                                       | false         | Author or contact information related to the integration in case of failures |
| correlationId           | iirisCorrelationId                                | false         | In case of hierarchical messages, this is the identifier (id) of the parent or preceding message |
| destination             | iirisDestination                                  | false         | The name of the message destination |
| externalId              | iirisExternalId                                   | false         | An external identifier (possibly contained in the message) that can be related eg. to order, project, or sales so that messages are easier to find |
| id                      | iirisId                                           | **true**      | Unique message identifier |
| info                    | iirisInfo                                         | false         | Additional information |
| integrationName         | iirisIntegrationName                              | **true**      | Unique name of the integration in concern |
| payloadOriginalFileName | iirisPayloadOriginalFileName                      | false         | Payload file name  (eg. file transferred via ftp) |
| serverId                | iirisServerId                                     | false         | Integration server name that handled the request |
| source                  | iirisSource                                       | false         | The name of  the message source |

Options are resolved in the order (first match applies):
1. option
2. exchange property
3. system property
4. environment

Log state:
```java
.to("iiris:New")
```

Log headers as info:
```java
.to("iiris:Processing?info=Headers: ${headers}")
```

Log from a bean:
```java
...
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
...
private static final Logger log = LoggerFactory.getLogger(Helloworld.class);
...
log.info("A message from bean.");
```

### Note

Other logging imports must be excluded avoid StackOverflowError. **Can this be refactored somehow**?
```xml
<dependency>
	<groupId>org.apache.camel</groupId>
	<artifactId>camel-spring-boot-starter</artifactId>
	<version>${camel.version}</version>
	<exclusions>
		<exclusion>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-logging</artifactId>
		</exclusion>
	</exclusions>
</dependency>
```
## Example Logs

### Iiris JSON Object

The example below is pretty printed here but is one liner at real use.
```
{
  "date": "2019-03-26T18:56:37.037Z",
  "type": "app-iiris",
  "version": 1,
  "id": "23a8baa6-5006-11e9-8647-d663bd873d93",
  "stateLabel": "New",
  "info": "This is a message.",
  "correlationId": "1028baa6-5006-11e9-8647-d663bd877364",
  "externalId": "123",
  "integrationName": "Payments",
  "source": "src1",
  "destination": "dest1",
  "payloadOriginalFilename": "payments.json",
  "appName": "helloworld-service",
  "appBuild": "BUILD-35",
  "appCommit": "0d01749c3568aab21f3f1a217cff24eab6fc5163",
  "env": "test",
  "serverId": "node1",
  "author": "author1"
}
```

### Miscellaneous JSON Object

The example below is pretty printed here but is one liner at real use.
```
{
  "date": "2019-03-26T18:10:44.044Z",
  "type": "app-misc",
  "version": 1,
  "logLevel": "error",
  "message": "error",
  "exception": "java.lang.Exception",
  "stackTrace": " java.lang.Exception: Nothing is working!\n\tat com.digia.camel.component.iiris.IirisProducerTest.evaluateSimpleTextTest(IirisProducerTest.java:31)\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.lang.reflect.Method.invoke(Method.java:498)\n\tat org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)\n\tat org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)\n\tat org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)\n\tat org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)\n\tat org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)\n\tat org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)\n\tat org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)\n\tat org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)\n\tat org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)\n\tat org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)\n\tat org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)\n\tat org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)\n\tat org.junit.runners.ParentRunner.run(ParentRunner.java:363)\n\tat org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference.run(JUnit4TestReference.java:89)\n\tat org.eclipse.jdt.internal.junit.runner.TestExecution.run(TestExecution.java:41)\n\tat org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:541)\n\tat org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:763)\n\tat org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:463)\n\tat org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:209)\n",
  "meta": {
    "logsAppIiris":
    {
      "id": "23a8baa6-5006-11e9-8647-d663bd873d93",
      "correlationId": "321"
    }
  },
  "appName": "helloworld-service",
  "appBuild": "BUILD-35",
  "appCommit": "0d01749c3568aab21f3f1a217cff24eab6fc5163",
  "env": "test"
}
```

## Development

Each version should be tagged:
```
git tag -a vX.Y.Z -m "X.Y.Z version"
git push --follow-tags
```

and pushed to maven repository:
```
mvn clean deploy
```

## Notes

Currently, does not implement optional Iiris fields: attachment, attachmentSizeBytes, payload.
