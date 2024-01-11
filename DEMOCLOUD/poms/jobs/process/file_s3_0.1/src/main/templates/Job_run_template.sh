#!/bin/sh
cd `dirname $0`
ROOT_PATH=`pwd`
java -Dtalend.component.manager.m2.repository=$ROOT_PATH/../lib -Xms256M -Xmx1024M -Dfile.encoding=UTF-8 -cp .:$ROOT_PATH:$ROOT_PATH/../lib/routines.jar:$ROOT_PATH/../lib/log4j-jcl-2.17.1.jar:$ROOT_PATH/../lib/log4j-slf4j-impl-2.17.1.jar:$ROOT_PATH/../lib/log4j-api-2.17.1.jar:$ROOT_PATH/../lib/log4j-core-2.17.1.jar:$ROOT_PATH/../lib/log4j-1.2-api-2.17.1.jar:$ROOT_PATH/../lib/commons-collections-3.2.2.jar:$ROOT_PATH/../lib/commons-logging-1.2.jar:$ROOT_PATH/../lib/aws-java-sdk-s3-1.12.315.jar:$ROOT_PATH/../lib/commons-codec-1.14.jar:$ROOT_PATH/../lib/jboss-marshalling-2.0.12.Final.jar:$ROOT_PATH/../lib/httpclient-4.5.13.jar:$ROOT_PATH/../lib/json-smart-2.4.11.jar:$ROOT_PATH/../lib/org.talend.dataquality.parser.jar:$ROOT_PATH/../lib/advancedPersistentLookupLib-1.5.jar:$ROOT_PATH/../lib/aws-java-sdk-core-1.12.315.jar:$ROOT_PATH/../lib/slf4j-api-1.7.34.jar:$ROOT_PATH/../lib/jackson-databind-2.14.3.jar:$ROOT_PATH/../lib/job-audit-1.5.jar:$ROOT_PATH/../lib/talend_file_enhanced-1.3.jar:$ROOT_PATH/../lib/dom4j-2.1.3.jar:$ROOT_PATH/../lib/jackson-core-2.14.3.jar:$ROOT_PATH/../lib/aws-java-sdk-kms-1.12.315.jar:$ROOT_PATH/../lib/accessors-smart-2.4.11.jar:$ROOT_PATH/../lib/crypto-utils-7.1.16.jar:$ROOT_PATH/../lib/joda-time-2.8.1.jar:$ROOT_PATH/../lib/audit-log4j2-1.16.1.jar:$ROOT_PATH/../lib/antlr-runtime-3.5.2.jar:$ROOT_PATH/../lib/logging-event-layout-1.16.1.jar:$ROOT_PATH/../lib/commons-lang3-3.10.jar:$ROOT_PATH/../lib/asm-9.5.jar:$ROOT_PATH/../lib/aws-java-sdk-sts-1.12.315.jar:$ROOT_PATH/../lib/talendcsv-1.1.0.jar:$ROOT_PATH/../lib/httpcore-4.4.13.jar:$ROOT_PATH/../lib/mysql-connector-j-8.0.33.jar:$ROOT_PATH/../lib/jmespath-java-1.12.315.jar:$ROOT_PATH/../lib/audit-common-1.16.1.jar:$ROOT_PATH/../lib/jackson-annotations-2.14.3.jar:$ROOT_PATH/../lib/jboss-marshalling-river-2.0.12.Final.jar:$ROOT_PATH/../lib/trove-1.0.2.jar:$ROOT_PATH/file_s3_0_1.jar: democloud.file_s3_0_1.File_S3 "$@"
