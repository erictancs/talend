%~d0
cd %~dp0
java -Dtalend.component.manager.m2.repository="%cd%/../lib" -Xms256M -Xmx1024M -cp .;../lib/routines.jar;../lib/log4j-slf4j-impl-2.17.1.jar;../lib/log4j-api-2.17.1.jar;../lib/log4j-core-2.17.1.jar;../lib/log4j-1.2-api-2.17.1.jar;../lib/activation.jar;../lib/commons-collections-3.2.2.jar;../lib/jakarta.mail-1.6.7.jar;../lib/pax-url-aether-2.6.12.jar;../lib/fluent-hc-4.5.13.jar;../lib/json-smart-2.4.11.jar;../lib/javax.annotation-api-1.3.jar;../lib/jackson-annotations-2.14.3.jar;../lib/maven-resolver-spi-1.8.2.jar;../lib/pax-url-aether-support-2.6.12.jar;../lib/commons-logging-1.2.jar;../lib/httpclient-4.5.13.jar;../lib/javax.inject-1.jar;../lib/job-audit-1.5.jar;../lib/jackson-mapper-asl-1.9.16-TALEND.jar;../lib/maven-resolver-api-1.8.2.jar;../lib/checker-qual-3.5.0.jar;../lib/talend_file_enhanced-1.3.jar;../lib/commons-codec-1.14.jar;../lib/commons-configuration2-2.9.0.jar;../lib/auto-service-1.0-rc2.jar;../lib/jackson-core-2.14.3.jar;../lib/auto-common-0.3.jar;../lib/failureaccess-1.0.1.jar;../lib/components-datastewardship-0.37.25.jar;../lib/maven-resolver-impl-1.8.2.jar;../lib/avro-1.11.2.jar;../lib/talend-codegen-utils.jar;../lib/maven-resolver-util-1.8.2.jar;../lib/commons-lang3-3.10.jar;../lib/commons-beanutils-1.9.4.jar;../lib/json-io-4.13.1-TALEND.jar;../lib/jsr305-1.3.9.jar;../lib/commons-compress-1.23.0.jar;../lib/lombok-1.18.24.jar;../lib/trove-1.0.2.jar;../lib/audit-common-1.16.1.jar;../lib/jboss-marshalling-river-2.0.12.Final.jar;../lib/maven-resolver-named-locks-1.8.2.jar;../lib/components-common-0.37.25.jar;../lib/error_prone_annotations-2.3.4.jar;../lib/jackson-core-asl-1.9.16-TALEND.jar;../lib/jboss-marshalling-2.0.12.Final.jar;../lib/org.talend.dataquality.parser.jar;../lib/components-api-0.37.25.jar;../lib/j2objc-annotations-1.3.jar;../lib/slf4j-api-1.7.34.jar;../lib/org.osgi.service.component.annotations-1.3.0.jar;../lib/daikon-exception-7.1.16.jar;../lib/dom4j-2.1.3.jar;../lib/commons-lang-2.6.jar;../lib/accessors-smart-2.4.11.jar;../lib/commons-text-1.10.0.jar;../lib/crypto-utils-7.1.16.jar;../lib/antlr-runtime-3.5.2.jar;../lib/audit-log4j2-1.16.1.jar;../lib/guava-32.1.2-jre.jar;../lib/listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;../lib/logging-event-layout-1.16.1.jar;../lib/joda-time-2.8.2.jar;../lib/asm-9.5.jar;../lib/daikon-7.1.16.jar;../lib/httpcore-4.4.13.jar;../lib/advancedPersistentLookupLib.jar;../lib/talendcsv-1.1.0.jar;../lib/mysql-connector-j-8.0.33.jar;../lib/datastewardship-client-0.37.25.jar;../lib/javax.servlet-api-3.1.0.jar;../lib/jackson-databind-2.14.3.jar;demo_0_1.jar; democloud.demo_0_1.Demo --context=Default %*
