#!/bin/sh
cd `dirname $0`
ROOT_PATH=`pwd`
java -Dtalend.component.manager.m2.repository=$ROOT_PATH/../lib -Xms256M -Xmx1024M -Dfile.encoding=UTF-8 -cp .:$ROOT_PATH:$ROOT_PATH/../lib/routines.jar:$ROOT_PATH/../lib/log4j-slf4j-impl-2.17.1.jar:$ROOT_PATH/../lib/log4j-api-2.17.1.jar:$ROOT_PATH/../lib/log4j-core-2.17.1.jar:$ROOT_PATH/../lib/pax-url-aether-2.6.12.jar:$ROOT_PATH/../lib/json-smart-2.4.11.jar:$ROOT_PATH/../lib/javax.annotation-api-1.3.jar:$ROOT_PATH/../lib/jackson-annotations-2.14.3.jar:$ROOT_PATH/../lib/maven-resolver-spi-1.8.2.jar:$ROOT_PATH/../lib/talend-httputil-1.0.6.jar:$ROOT_PATH/../lib/commons-validator-1.6.jar:$ROOT_PATH/../lib/httpclient-4.5.13.jar:$ROOT_PATH/../lib/pax-url-aether-support-2.6.12.jar:$ROOT_PATH/../lib/commons-logging-1.2.jar:$ROOT_PATH/../lib/commons-digester-1.8.1.jar:$ROOT_PATH/../lib/javax.inject-1.jar:$ROOT_PATH/../lib/job-audit-1.5.jar:$ROOT_PATH/../lib/jackson-mapper-asl-1.9.16-TALEND.jar:$ROOT_PATH/../lib/maven-resolver-api-1.8.2.jar:$ROOT_PATH/../lib/checker-qual-3.5.0.jar:$ROOT_PATH/../lib/talend_file_enhanced-1.3.jar:$ROOT_PATH/../lib/commons-codec-1.14.jar:$ROOT_PATH/../lib/commons-configuration2-2.9.0.jar:$ROOT_PATH/../lib/components-dataprep-0.37.25.jar:$ROOT_PATH/../lib/ezmorph-1.0.6.jar:$ROOT_PATH/../lib/auto-service-1.0-rc2.jar:$ROOT_PATH/../lib/jackson-core-2.14.3.jar:$ROOT_PATH/../lib/auto-common-0.3.jar:$ROOT_PATH/../lib/failureaccess-1.0.1.jar:$ROOT_PATH/../lib/maven-resolver-impl-1.8.2.jar:$ROOT_PATH/../lib/avro-1.11.2.jar:$ROOT_PATH/../lib/talend-codegen-utils.jar:$ROOT_PATH/../lib/maven-resolver-util-1.8.2.jar:$ROOT_PATH/../lib/commons-lang3-3.10.jar:$ROOT_PATH/../lib/commons-beanutils-1.9.4.jar:$ROOT_PATH/../lib/json-io-4.13.1-TALEND.jar:$ROOT_PATH/../lib/jsr305-1.3.9.jar:$ROOT_PATH/../lib/commons-compress-1.23.0.jar:$ROOT_PATH/../lib/lombok-1.18.24.jar:$ROOT_PATH/../lib/audit-common-1.16.1.jar:$ROOT_PATH/../lib/maven-resolver-named-locks-1.8.2.jar:$ROOT_PATH/../lib/httpcore-4.4.13.jar:$ROOT_PATH/../lib/commons-io-2.8.0.jar:$ROOT_PATH/../lib/components-common-0.37.25.jar:$ROOT_PATH/../lib/error_prone_annotations-2.3.4.jar:$ROOT_PATH/../lib/jackson-core-asl-1.9.16-TALEND.jar:$ROOT_PATH/../lib/resty-0.3.2.jar:$ROOT_PATH/../lib/jboss-marshalling-2.0.12.Final.jar:$ROOT_PATH/../lib/jackson-databind-2.14.3.jar:$ROOT_PATH/../lib/org.talend.dataquality.parser.jar:$ROOT_PATH/../lib/components-api-0.37.25.jar:$ROOT_PATH/../lib/j2objc-annotations-1.3.jar:$ROOT_PATH/../lib/fluent-hc-4.5.5.jar:$ROOT_PATH/../lib/slf4j-api-1.7.34.jar:$ROOT_PATH/../lib/org.osgi.service.component.annotations-1.3.0.jar:$ROOT_PATH/../lib/daikon-exception-7.1.16.jar:$ROOT_PATH/../lib/dom4j-2.1.3.jar:$ROOT_PATH/../lib/accessors-smart-2.4.11.jar:$ROOT_PATH/../lib/commons-text-1.10.0.jar:$ROOT_PATH/../lib/crypto-utils-7.1.16.jar:$ROOT_PATH/../lib/jsoup-1.15.3.jar:$ROOT_PATH/../lib/antlr-runtime-3.5.2.jar:$ROOT_PATH/../lib/commons-lang-2.6.jar:$ROOT_PATH/../lib/audit-log4j2-1.16.1.jar:$ROOT_PATH/../lib/commons-collections-3.2.2.jar:$ROOT_PATH/../lib/guava-32.1.2-jre.jar:$ROOT_PATH/../lib/json-lib-2.4.6-talend.jar:$ROOT_PATH/../lib/listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar:$ROOT_PATH/../lib/logging-event-layout-1.16.1.jar:$ROOT_PATH/../lib/joda-time-2.8.2.jar:$ROOT_PATH/../lib/asm-9.5.jar:$ROOT_PATH/../lib/daikon-7.1.16.jar:$ROOT_PATH/../lib/talendcsv-1.1.0.jar:$ROOT_PATH/../lib/javax.servlet-api-3.1.0.jar:$ROOT_PATH/dataprep_0_1.jar: democloud.dataprep_0_1.DataPrep "$@"
