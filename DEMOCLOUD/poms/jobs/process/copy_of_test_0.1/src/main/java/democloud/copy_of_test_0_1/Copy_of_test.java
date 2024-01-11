
package democloud.copy_of_test_0_1;

import routines.DataOperation;
import routines.TalendDataGenerator;
import routines.DataQuality;
import routines.Relational;
import routines.DataQualityDependencies;
import routines.Mathematical;
import routines.SQLike;
import routines.Numeric;
import routines.TalendStringUtil;
import routines.TalendString;
import routines.DQTechnical;
import routines.StringHandling;
import routines.DataMasking;
import routines.TalendDate;
import routines.DqStringHandling;
import routines.system.*;
import routines.system.api.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Comparator;

@SuppressWarnings("unused")

/**
 * Job: Copy_of_test Purpose: <br>
 * Description: <br>
 * 
 * @author Tan, Eric
 * @version 8.0.1.20231222_1430-patch
 * @status
 */
public class Copy_of_test implements TalendJob {
	static {
		System.setProperty("TalendJob.log", "Copy_of_test.log");
	}

	private static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
			.getLogger(Copy_of_test.class);

	protected static void logIgnoredError(String message, Throwable cause) {
		log.error(message, cause);

	}

	public final Object obj = new Object();

	// for transmiting parameters purpose
	private Object valueObject = null;

	public Object getValueObject() {
		return this.valueObject;
	}

	public void setValueObject(Object valueObject) {
		this.valueObject = valueObject;
	}

	private final static String defaultCharset = java.nio.charset.Charset.defaultCharset().name();

	private final static String utf8Charset = "UTF-8";

	// contains type for every context property
	public class PropertiesWithType extends java.util.Properties {
		private static final long serialVersionUID = 1L;
		private java.util.Map<String, String> propertyTypes = new java.util.HashMap<>();

		public PropertiesWithType(java.util.Properties properties) {
			super(properties);
		}

		public PropertiesWithType() {
			super();
		}

		public void setContextType(String key, String type) {
			propertyTypes.put(key, type);
		}

		public String getContextType(String key) {
			return propertyTypes.get(key);
		}
	}

	// create and load default properties
	private java.util.Properties defaultProps = new java.util.Properties();

	// create application properties with default
	public class ContextProperties extends PropertiesWithType {

		private static final long serialVersionUID = 1L;

		public ContextProperties(java.util.Properties properties) {
			super(properties);
		}

		public ContextProperties() {
			super();
		}

		public void synchronizeContext() {

		}

		// if the stored or passed value is "<TALEND_NULL>" string, it mean null
		public String getStringValue(String key) {
			String origin_value = this.getProperty(key);
			if (NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY.equals(origin_value)) {
				return null;
			}
			return origin_value;
		}

	}

	protected ContextProperties context = new ContextProperties(); // will be instanciated by MS.

	public ContextProperties getContext() {
		return this.context;
	}

	private final String jobVersion = "0.1";
	private final String jobName = "Copy_of_test";
	private final String projectName = "DEMOCLOUD";
	public Integer errorCode = null;
	private String currentComponent = "";

	private String cLabel = null;

	private final java.util.Map<String, Object> globalMap = new java.util.HashMap<String, Object>();
	private final static java.util.Map<String, Object> junitGlobalMap = new java.util.HashMap<String, Object>();

	private final java.util.Map<String, Long> start_Hash = new java.util.HashMap<String, Long>();
	private final java.util.Map<String, Long> end_Hash = new java.util.HashMap<String, Long>();
	private final java.util.Map<String, Boolean> ok_Hash = new java.util.HashMap<String, Boolean>();
	public final java.util.List<String[]> globalBuffer = new java.util.ArrayList<String[]>();

	private final JobStructureCatcherUtils talendJobLog = new JobStructureCatcherUtils(jobName,
			"_RS3GcKrjEe6UOOsCwsvcUA", "0.1");
	private org.talend.job.audit.JobAuditLogger auditLogger_talendJobLog = null;

	private RunStat runStat = new RunStat(talendJobLog, System.getProperty("audit.interval"));

	// OSGi DataSource
	private final static String KEY_DB_DATASOURCES = "KEY_DB_DATASOURCES";

	private final static String KEY_DB_DATASOURCES_RAW = "KEY_DB_DATASOURCES_RAW";

	public void setDataSources(java.util.Map<String, javax.sql.DataSource> dataSources) {
		java.util.Map<String, routines.system.TalendDataSource> talendDataSources = new java.util.HashMap<String, routines.system.TalendDataSource>();
		for (java.util.Map.Entry<String, javax.sql.DataSource> dataSourceEntry : dataSources.entrySet()) {
			talendDataSources.put(dataSourceEntry.getKey(),
					new routines.system.TalendDataSource(dataSourceEntry.getValue()));
		}
		globalMap.put(KEY_DB_DATASOURCES, talendDataSources);
		globalMap.put(KEY_DB_DATASOURCES_RAW, new java.util.HashMap<String, javax.sql.DataSource>(dataSources));
	}

	public void setDataSourceReferences(List serviceReferences) throws Exception {

		java.util.Map<String, routines.system.TalendDataSource> talendDataSources = new java.util.HashMap<String, routines.system.TalendDataSource>();
		java.util.Map<String, javax.sql.DataSource> dataSources = new java.util.HashMap<String, javax.sql.DataSource>();

		for (java.util.Map.Entry<String, javax.sql.DataSource> entry : BundleUtils
				.getServices(serviceReferences, javax.sql.DataSource.class).entrySet()) {
			dataSources.put(entry.getKey(), entry.getValue());
			talendDataSources.put(entry.getKey(), new routines.system.TalendDataSource(entry.getValue()));
		}

		globalMap.put(KEY_DB_DATASOURCES, talendDataSources);
		globalMap.put(KEY_DB_DATASOURCES_RAW, new java.util.HashMap<String, javax.sql.DataSource>(dataSources));
	}

	private final java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
	private final java.io.PrintStream errorMessagePS = new java.io.PrintStream(new java.io.BufferedOutputStream(baos));

	public String getExceptionStackTrace() {
		if ("failure".equals(this.getStatus())) {
			errorMessagePS.flush();
			return baos.toString();
		}
		return null;
	}

	private Exception exception;

	public Exception getException() {
		if ("failure".equals(this.getStatus())) {
			return this.exception;
		}
		return null;
	}

	private class TalendException extends Exception {

		private static final long serialVersionUID = 1L;

		private java.util.Map<String, Object> globalMap = null;
		private Exception e = null;

		private String currentComponent = null;
		private String cLabel = null;

		private String virtualComponentName = null;

		public void setVirtualComponentName(String virtualComponentName) {
			this.virtualComponentName = virtualComponentName;
		}

		private TalendException(Exception e, String errorComponent, final java.util.Map<String, Object> globalMap) {
			this.currentComponent = errorComponent;
			this.globalMap = globalMap;
			this.e = e;
		}

		private TalendException(Exception e, String errorComponent, String errorComponentLabel,
				final java.util.Map<String, Object> globalMap) {
			this(e, errorComponent, globalMap);
			this.cLabel = errorComponentLabel;
		}

		public Exception getException() {
			return this.e;
		}

		public String getCurrentComponent() {
			return this.currentComponent;
		}

		public String getExceptionCauseMessage(Exception e) {
			Throwable cause = e;
			String message = null;
			int i = 10;
			while (null != cause && 0 < i--) {
				message = cause.getMessage();
				if (null == message) {
					cause = cause.getCause();
				} else {
					break;
				}
			}
			if (null == message) {
				message = e.getClass().getName();
			}
			return message;
		}

		@Override
		public void printStackTrace() {
			if (!(e instanceof TalendException || e instanceof TDieException)) {
				if (virtualComponentName != null && currentComponent.indexOf(virtualComponentName + "_") == 0) {
					globalMap.put(virtualComponentName + "_ERROR_MESSAGE", getExceptionCauseMessage(e));
				}
				globalMap.put(currentComponent + "_ERROR_MESSAGE", getExceptionCauseMessage(e));
				System.err.println("Exception in component " + currentComponent + " (" + jobName + ")");
			}
			if (!(e instanceof TDieException)) {
				if (e instanceof TalendException) {
					e.printStackTrace();
				} else {
					e.printStackTrace();
					e.printStackTrace(errorMessagePS);
					Copy_of_test.this.exception = e;
				}
			}
			if (!(e instanceof TalendException)) {
				try {
					for (java.lang.reflect.Method m : this.getClass().getEnclosingClass().getMethods()) {
						if (m.getName().compareTo(currentComponent + "_error") == 0) {
							m.invoke(Copy_of_test.this, new Object[] { e, currentComponent, globalMap });
							break;
						}
					}

					if (!(e instanceof TDieException)) {
						if (enableLogStash) {
							talendJobLog.addJobExceptionMessage(currentComponent, cLabel, null, e);
							talendJobLogProcess(globalMap);
						}
					}
				} catch (Exception e) {
					this.e.printStackTrace();
				}
			}
		}
	}

	public void tPOP_2_error(Exception exception, String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tPOP_2_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tFileList_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileList_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tFileInputMail_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileList_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tLogRow_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileList_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void talendJobLog_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		talendJobLog_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tPOP_2_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tFileList_1_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void talendJobLog_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tPOP_2Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tPOP_2_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdcInfo.forEach(org.slf4j.MDC::put);
		org.slf4j.MDC.put("_subJobName", "tPOP_2");
		org.slf4j.MDC.put("_subJobPid", "xKwpT1_" + subJobPidCounter.getAndIncrement());

		String iterateId = "";

		String currentComponent = "";
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [tPOP_2 begin ] start
				 */

				ok_Hash.put("tPOP_2", false);
				start_Hash.put("tPOP_2", System.currentTimeMillis());

				currentComponent = "tPOP_2";

				int tos_count_tPOP_2 = 0;

				if (log.isDebugEnabled())
					log.debug("tPOP_2 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tPOP_2 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tPOP_2 = new StringBuilder();
							log4jParamters_tPOP_2.append("Parameters:");
							log4jParamters_tPOP_2.append("HOST" + " = " + "\"pop.gmail.com\"");
							log4jParamters_tPOP_2.append(" | ");
							log4jParamters_tPOP_2.append("PORT" + " = " + "995");
							log4jParamters_tPOP_2.append(" | ");
							log4jParamters_tPOP_2.append("AUTH_MODE" + " = " + "BASIC");
							log4jParamters_tPOP_2.append(" | ");
							log4jParamters_tPOP_2.append("USERNAME" + " = " + "\"tancse@gmail.com\"");
							log4jParamters_tPOP_2.append(" | ");
							log4jParamters_tPOP_2.append("PASSWORD" + " = " + String.valueOf(
									"enc:routine.encryption.key.v1:fVffuOin0D/xcd67iGYUth1oqx3s/gxtgI1y8Y5D1MIcrmeUWw==")
									.substring(0, 4) + "...");
							log4jParamters_tPOP_2.append(" | ");
							log4jParamters_tPOP_2
									.append("OUTPUT_DIRECTORY" + " = " + "\"C:/SIFT/Developer/Talend/Data/Test\"");
							log4jParamters_tPOP_2.append(" | ");
							log4jParamters_tPOP_2.append("FILENAME_PATTERN" + " = "
									+ "TalendDate.getDate(\"yyyyMMdd-hhmmss\") + \"_\" + java.util.UUID.randomUUID().toString().substring(0, 16) + \".mail\"");
							log4jParamters_tPOP_2.append(" | ");
							log4jParamters_tPOP_2.append("ALL_EMAILS" + " = " + "true");
							log4jParamters_tPOP_2.append(" | ");
							log4jParamters_tPOP_2.append("DELETE_FROM_SERVER" + " = " + "false");
							log4jParamters_tPOP_2.append(" | ");
							log4jParamters_tPOP_2.append("PROTOCOL" + " = " + "pop3");
							log4jParamters_tPOP_2.append(" | ");
							log4jParamters_tPOP_2.append("USE_SSL" + " = " + "true");
							log4jParamters_tPOP_2.append(" | ");
							log4jParamters_tPOP_2.append("ADVANCED_FILTER" + " = " + "[]");
							log4jParamters_tPOP_2.append(" | ");
							log4jParamters_tPOP_2.append("FILTER_RELATION" + " = " + "&&");
							log4jParamters_tPOP_2.append(" | ");
							log4jParamters_tPOP_2.append("CONFIGS" + " = " + "[]");
							log4jParamters_tPOP_2.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tPOP_2 - " + (log4jParamters_tPOP_2));
						}
					}
					new BytesLimit65535_tPOP_2().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tPOP_2", "tPOP_2", "tPOP");
					talendJobLogProcess(globalMap);
				}

				String server_tPOP_2 = "pop.gmail.com";
				String mbox_tPOP_2 = "INBOX";
				String user_tPOP_2 = "tancse@gmail.com";

				final String decryptedPassword_tPOP_2 = routines.system.PasswordEncryptUtil.decryptPassword(
						"enc:routine.encryption.key.v1:J/28SlJbjmk8aYwkrXAjIWH4a5FCV96j3wsrj6GlYigRm1Ncig==");

				String password_tPOP_2 = decryptedPassword_tPOP_2;
				int port_tPOP_2 = 995;
				int nb_email_tPOP_2 = 0;
				javax.mail.Folder folder_tPOP_2;
				javax.mail.Session session_tPOP_2;
				javax.mail.Store store_tPOP_2;
				javax.mail.URLName url_tPOP_2;
				java.util.Properties props_tPOP_2;

				try {
					props_tPOP_2 = System.getProperties();
				} catch (SecurityException sex) {
					globalMap.put("tPOP_2_ERROR_MESSAGE", sex.getMessage());

					log.warn("tPOP_2 - " + sex.getMessage());

					props_tPOP_2 = new java.util.Properties();
				}
				props_tPOP_2.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props_tPOP_2.setProperty("mail.pop3.socketFactory.fallback", "false");
				props_tPOP_2.setProperty("mail.pop3.socketFactory.port", port_tPOP_2 + "");
				session_tPOP_2 = javax.mail.Session.getInstance(props_tPOP_2, null);
				store_tPOP_2 = session_tPOP_2.getStore("pop3");

				log.info("tPOP_2 - Connection attempt to '" + server_tPOP_2 + "' as '" + user_tPOP_2 + "'.");

				store_tPOP_2.connect(server_tPOP_2, port_tPOP_2, user_tPOP_2, password_tPOP_2);

				log.info("tPOP_2 - Connection to '" + server_tPOP_2 + "' has succeeded.");

				folder_tPOP_2 = store_tPOP_2.getDefaultFolder();

				if (folder_tPOP_2 == null) {
					throw new RuntimeException("No default folder");
				}
				// its INBOX
				folder_tPOP_2 = folder_tPOP_2.getFolder(mbox_tPOP_2);

				if (folder_tPOP_2 == null) {
					throw new RuntimeException("No POP3 INBOX");
				}

				try {
					folder_tPOP_2.open(javax.mail.Folder.READ_WRITE);
				} catch (java.lang.Exception e) {
					globalMap.put("tPOP_2_ERROR_MESSAGE", e.getMessage());

					log.warn("tPOP_2 - " + e.getMessage());

					folder_tPOP_2.open(javax.mail.Folder.READ_ONLY);
				}

				javax.mail.Message[] msgs_tPOP_2 = folder_tPOP_2.getMessages();

				log.info("tPOP_2 - Retrieving mails from server.");

				for (int counter_tPOP_2 = 0; counter_tPOP_2 < msgs_tPOP_2.length; counter_tPOP_2++) {

					javax.mail.Message message_tPOP_2 = msgs_tPOP_2[counter_tPOP_2];

					try {
						boolean isMatch_tPOP_2 = true;

						String filename_tPOP_2 = TalendDate.getDate("yyyyMMdd-hhmmss") + "_"
								+ java.util.UUID.randomUUID().toString().substring(0, 16) + ".mail";
						java.io.File file_tPOP_2 = new java.io.File("C:/SIFT/Developer/Talend/Data/Test",
								filename_tPOP_2);
						try (java.io.OutputStream os_tPOP_2 = new java.io.BufferedOutputStream(
								new java.io.FileOutputStream(file_tPOP_2))) {
							message_tPOP_2.writeTo(os_tPOP_2);
						}

						nb_email_tPOP_2++;
						globalMap.put("tPOP_2_CURRENT_FILE", filename_tPOP_2);
						globalMap.put("tPOP_2_CURRENT_FILEPATH", file_tPOP_2.getAbsolutePath());

						/**
						 * [tPOP_2 begin ] stop
						 */

						/**
						 * [tPOP_2 main ] start
						 */

						currentComponent = "tPOP_2";

						tos_count_tPOP_2++;

						/**
						 * [tPOP_2 main ] stop
						 */

						/**
						 * [tPOP_2 process_data_begin ] start
						 */

						currentComponent = "tPOP_2";

						/**
						 * [tPOP_2 process_data_begin ] stop
						 */

						/**
						 * [tPOP_2 process_data_end ] start
						 */

						currentComponent = "tPOP_2";

						/**
						 * [tPOP_2 process_data_end ] stop
						 */

						/**
						 * [tPOP_2 end ] start
						 */

						currentComponent = "tPOP_2";

						log.debug("tPOP_2 - Retrieving the record " + nb_email_tPOP_2 + ".");

					} catch (javax.mail.MessageRemovedException mre) {
						globalMap.put("tPOP_2_ERROR_MESSAGE", mre.getMessage());

						log.warn("tPOP_2 - One mail fails to retrieve since it was removed");

						System.out.println("one mail fails to retrieve since it was removed");

					}
				}

				if (folder_tPOP_2 != null) {

					folder_tPOP_2.close(false);

				}

				if (store_tPOP_2 != null) {

					log.info("tPOP_2 - Closing the connection to the server.");

					store_tPOP_2.close();

					log.info("tPOP_2 - Connection to the server closed.");

				}
				globalMap.put("tPOP_2_NB_EMAIL", nb_email_tPOP_2);

				log.info("tPOP_2 - Retrived " + nb_email_tPOP_2 + " mails.");

				if (log.isDebugEnabled())
					log.debug("tPOP_2 - " + ("Done."));

				ok_Hash.put("tPOP_2", true);
				end_Hash.put("tPOP_2", System.currentTimeMillis());

				/**
				 * [tPOP_2 end ] stop
				 */
			} // end the resume

			if (resumeEntryMethodName == null || globalResumeTicket) {
				resumeUtil.addLog("CHECKPOINT", "CONNECTION:SUBJOB_OK:tPOP_2:OnSubjobOk", "",
						Thread.currentThread().getId() + "", "", "", "", "", "");
			}

			if (execStat) {
				runStat.updateStatOnConnection("OnSubjobOk1", 0, "ok");
			}

			tFileList_1Process(globalMap);

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tPOP_2 finally ] start
				 */

				currentComponent = "tPOP_2";

				/**
				 * [tPOP_2 finally ] stop
				 */
			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tPOP_2_SUBPROCESS_STATE", 1);
	}

	public static class row1Struct implements routines.system.IPersistableRow<row1Struct> {
		final static byte[] commonByteArrayLock_DEMOCLOUD_Copy_of_test = new byte[0];
		static byte[] commonByteArray_DEMOCLOUD_Copy_of_test = new byte[0];

		public String Date;

		public String getDate() {
			return this.Date;
		}

		public Boolean DateIsNullable() {
			return true;
		}

		public Boolean DateIsKey() {
			return false;
		}

		public Integer DateLength() {
			return null;
		}

		public Integer DatePrecision() {
			return null;
		}

		public String DateDefault() {

			return null;

		}

		public String DateComment() {

			return "";

		}

		public String DatePattern() {

			return "";

		}

		public String DateOriginalDbColumnName() {

			return "Date";

		}

		public String Author;

		public String getAuthor() {
			return this.Author;
		}

		public Boolean AuthorIsNullable() {
			return true;
		}

		public Boolean AuthorIsKey() {
			return false;
		}

		public Integer AuthorLength() {
			return null;
		}

		public Integer AuthorPrecision() {
			return null;
		}

		public String AuthorDefault() {

			return null;

		}

		public String AuthorComment() {

			return "";

		}

		public String AuthorPattern() {

			return "";

		}

		public String AuthorOriginalDbColumnName() {

			return "Author";

		}

		public String Object;

		public String getObject() {
			return this.Object;
		}

		public Boolean ObjectIsNullable() {
			return true;
		}

		public Boolean ObjectIsKey() {
			return false;
		}

		public Integer ObjectLength() {
			return null;
		}

		public Integer ObjectPrecision() {
			return null;
		}

		public String ObjectDefault() {

			return null;

		}

		public String ObjectComment() {

			return "";

		}

		public String ObjectPattern() {

			return "";

		}

		public String ObjectOriginalDbColumnName() {

			return "Object";

		}

		public String Status;

		public String getStatus() {
			return this.Status;
		}

		public Boolean StatusIsNullable() {
			return true;
		}

		public Boolean StatusIsKey() {
			return false;
		}

		public Integer StatusLength() {
			return null;
		}

		public Integer StatusPrecision() {
			return null;
		}

		public String StatusDefault() {

			return null;

		}

		public String StatusComment() {

			return "";

		}

		public String StatusPattern() {

			return "";

		}

		public String StatusOriginalDbColumnName() {

			return "Status";

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DEMOCLOUD_Copy_of_test.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_Copy_of_test.length == 0) {
						commonByteArray_DEMOCLOUD_Copy_of_test = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_Copy_of_test = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DEMOCLOUD_Copy_of_test, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_Copy_of_test, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DEMOCLOUD_Copy_of_test.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_Copy_of_test.length == 0) {
						commonByteArray_DEMOCLOUD_Copy_of_test = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_Copy_of_test = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DEMOCLOUD_Copy_of_test, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_Copy_of_test, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_DEMOCLOUD_Copy_of_test) {

				try {

					int length = 0;

					this.Date = readString(dis);

					this.Author = readString(dis);

					this.Object = readString(dis);

					this.Status = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_DEMOCLOUD_Copy_of_test) {

				try {

					int length = 0;

					this.Date = readString(dis);

					this.Author = readString(dis);

					this.Object = readString(dis);

					this.Status = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.Date, dos);

				// String

				writeString(this.Author, dos);

				// String

				writeString(this.Object, dos);

				// String

				writeString(this.Status, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// String

				writeString(this.Date, dos);

				// String

				writeString(this.Author, dos);

				// String

				writeString(this.Object, dos);

				// String

				writeString(this.Status, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("Date=" + Date);
			sb.append(",Author=" + Author);
			sb.append(",Object=" + Object);
			sb.append(",Status=" + Status);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (Date == null) {
				sb.append("<null>");
			} else {
				sb.append(Date);
			}

			sb.append("|");

			if (Author == null) {
				sb.append("<null>");
			} else {
				sb.append(Author);
			}

			sb.append("|");

			if (Object == null) {
				sb.append("<null>");
			} else {
				sb.append(Object);
			}

			sb.append("|");

			if (Status == null) {
				sb.append("<null>");
			} else {
				sb.append(Status);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row1Struct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(), object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public void tFileList_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tFileList_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdcInfo.forEach(org.slf4j.MDC::put);
		org.slf4j.MDC.put("_subJobName", "tFileList_1");
		org.slf4j.MDC.put("_subJobPid", "5Xeykl_" + subJobPidCounter.getAndIncrement());

		String iterateId = "";

		String currentComponent = "";
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				row1Struct row1 = new row1Struct();

				/**
				 * [tFileList_1 begin ] start
				 */

				int NB_ITERATE_tFileInputMail_1 = 0; // for statistics

				ok_Hash.put("tFileList_1", false);
				start_Hash.put("tFileList_1", System.currentTimeMillis());

				currentComponent = "tFileList_1";

				int tos_count_tFileList_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tFileList_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tFileList_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tFileList_1 = new StringBuilder();
							log4jParamters_tFileList_1.append("Parameters:");
							log4jParamters_tFileList_1
									.append("DIRECTORY" + " = " + "\"C:/SIFT/Developer/Talend/Data/Test\"");
							log4jParamters_tFileList_1.append(" | ");
							log4jParamters_tFileList_1.append("LIST_MODE" + " = " + "FILES");
							log4jParamters_tFileList_1.append(" | ");
							log4jParamters_tFileList_1.append("INCLUDSUBDIR" + " = " + "false");
							log4jParamters_tFileList_1.append(" | ");
							log4jParamters_tFileList_1.append("CASE_SENSITIVE" + " = " + "YES");
							log4jParamters_tFileList_1.append(" | ");
							log4jParamters_tFileList_1.append("ERROR" + " = " + "false");
							log4jParamters_tFileList_1.append(" | ");
							log4jParamters_tFileList_1.append("GLOBEXPRESSIONS" + " = " + "true");
							log4jParamters_tFileList_1.append(" | ");
							log4jParamters_tFileList_1.append("FILES" + " = " + "[{FILEMASK=" + ("\"*.mail\"") + "}]");
							log4jParamters_tFileList_1.append(" | ");
							log4jParamters_tFileList_1.append("ORDER_BY_NOTHING" + " = " + "true");
							log4jParamters_tFileList_1.append(" | ");
							log4jParamters_tFileList_1.append("ORDER_BY_FILENAME" + " = " + "false");
							log4jParamters_tFileList_1.append(" | ");
							log4jParamters_tFileList_1.append("ORDER_BY_FILESIZE" + " = " + "false");
							log4jParamters_tFileList_1.append(" | ");
							log4jParamters_tFileList_1.append("ORDER_BY_MODIFIEDDATE" + " = " + "false");
							log4jParamters_tFileList_1.append(" | ");
							log4jParamters_tFileList_1.append("ORDER_ACTION_ASC" + " = " + "true");
							log4jParamters_tFileList_1.append(" | ");
							log4jParamters_tFileList_1.append("ORDER_ACTION_DESC" + " = " + "false");
							log4jParamters_tFileList_1.append(" | ");
							log4jParamters_tFileList_1.append("IFEXCLUDE" + " = " + "false");
							log4jParamters_tFileList_1.append(" | ");
							log4jParamters_tFileList_1.append("FORMAT_FILEPATH_TO_SLASH" + " = " + "false");
							log4jParamters_tFileList_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tFileList_1 - " + (log4jParamters_tFileList_1));
						}
					}
					new BytesLimit65535_tFileList_1().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tFileList_1", "tFileList_1", "tFileList");
					talendJobLogProcess(globalMap);
				}

				final StringBuffer log4jSb_tFileList_1 = new StringBuffer();

				String directory_tFileList_1 = "C:/SIFT/Developer/Talend/Data/Test";
				final java.util.List<String> maskList_tFileList_1 = new java.util.ArrayList<String>();
				final java.util.List<java.util.regex.Pattern> patternList_tFileList_1 = new java.util.ArrayList<java.util.regex.Pattern>();
				maskList_tFileList_1.add("*.mail");
				for (final String filemask_tFileList_1 : maskList_tFileList_1) {
					String filemask_compile_tFileList_1 = filemask_tFileList_1;

					filemask_compile_tFileList_1 = org.apache.oro.text.GlobCompiler.globToPerl5(
							filemask_tFileList_1.toCharArray(), org.apache.oro.text.GlobCompiler.DEFAULT_MASK);

					java.util.regex.Pattern fileNamePattern_tFileList_1 = java.util.regex.Pattern
							.compile(filemask_compile_tFileList_1);
					patternList_tFileList_1.add(fileNamePattern_tFileList_1);
				}
				int NB_FILEtFileList_1 = 0;

				final boolean case_sensitive_tFileList_1 = true;

				log.info("tFileList_1 - Starting to search for matching entries.");

				final java.util.List<java.io.File> list_tFileList_1 = new java.util.ArrayList<java.io.File>();
				final java.util.Set<String> filePath_tFileList_1 = new java.util.HashSet<String>();
				java.io.File file_tFileList_1 = new java.io.File(directory_tFileList_1);

				file_tFileList_1.listFiles(new java.io.FilenameFilter() {
					public boolean accept(java.io.File dir, String name) {
						java.io.File file = new java.io.File(dir, name);
						if (!file.isDirectory()) {

							String fileName_tFileList_1 = file.getName();
							for (final java.util.regex.Pattern fileNamePattern_tFileList_1 : patternList_tFileList_1) {
								if (fileNamePattern_tFileList_1.matcher(fileName_tFileList_1).matches()) {
									if (!filePath_tFileList_1.contains(file.getAbsolutePath())) {
										list_tFileList_1.add(file);
										filePath_tFileList_1.add(file.getAbsolutePath());
									}
								}
							}
						}
						return true;
					}
				});
				java.util.Collections.sort(list_tFileList_1);

				log.info("tFileList_1 - Start to list files.");

				for (int i_tFileList_1 = 0; i_tFileList_1 < list_tFileList_1.size(); i_tFileList_1++) {
					java.io.File files_tFileList_1 = list_tFileList_1.get(i_tFileList_1);
					String fileName_tFileList_1 = files_tFileList_1.getName();

					String currentFileName_tFileList_1 = files_tFileList_1.getName();
					String currentFilePath_tFileList_1 = files_tFileList_1.getAbsolutePath();
					String currentFileDirectory_tFileList_1 = files_tFileList_1.getParent();
					String currentFileExtension_tFileList_1 = null;

					if (files_tFileList_1.getName().contains(".") && files_tFileList_1.isFile()) {
						currentFileExtension_tFileList_1 = files_tFileList_1.getName()
								.substring(files_tFileList_1.getName().lastIndexOf(".") + 1);
					} else {
						currentFileExtension_tFileList_1 = "";
					}

					NB_FILEtFileList_1++;
					globalMap.put("tFileList_1_CURRENT_FILE", currentFileName_tFileList_1);
					globalMap.put("tFileList_1_CURRENT_FILEPATH", currentFilePath_tFileList_1);
					globalMap.put("tFileList_1_CURRENT_FILEDIRECTORY", currentFileDirectory_tFileList_1);
					globalMap.put("tFileList_1_CURRENT_FILEEXTENSION", currentFileExtension_tFileList_1);
					globalMap.put("tFileList_1_NB_FILE", NB_FILEtFileList_1);

					log.info("tFileList_1 - Current file or directory path : " + currentFilePath_tFileList_1);

					/**
					 * [tFileList_1 begin ] stop
					 */

					/**
					 * [tFileList_1 main ] start
					 */

					currentComponent = "tFileList_1";

					tos_count_tFileList_1++;

					/**
					 * [tFileList_1 main ] stop
					 */

					/**
					 * [tFileList_1 process_data_begin ] start
					 */

					currentComponent = "tFileList_1";

					/**
					 * [tFileList_1 process_data_begin ] stop
					 */
					NB_ITERATE_tFileInputMail_1++;

					if (execStat) {
						runStat.updateStatOnConnection("row1", 3, 0);
					}

					if (execStat) {
						runStat.updateStatOnConnection("iterate1", 1, "exec" + NB_ITERATE_tFileInputMail_1);
						// Thread.sleep(1000);
					}

					/**
					 * [tLogRow_1 begin ] start
					 */

					ok_Hash.put("tLogRow_1", false);
					start_Hash.put("tLogRow_1", System.currentTimeMillis());

					currentComponent = "tLogRow_1";

					runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row1");

					int tos_count_tLogRow_1 = 0;

					if (log.isDebugEnabled())
						log.debug("tLogRow_1 - " + ("Start to work."));
					if (log.isDebugEnabled()) {
						class BytesLimit65535_tLogRow_1 {
							public void limitLog4jByte() throws Exception {
								StringBuilder log4jParamters_tLogRow_1 = new StringBuilder();
								log4jParamters_tLogRow_1.append("Parameters:");
								log4jParamters_tLogRow_1.append("BASIC_MODE" + " = " + "false");
								log4jParamters_tLogRow_1.append(" | ");
								log4jParamters_tLogRow_1.append("TABLE_PRINT" + " = " + "true");
								log4jParamters_tLogRow_1.append(" | ");
								log4jParamters_tLogRow_1.append("VERTICAL" + " = " + "false");
								log4jParamters_tLogRow_1.append(" | ");
								log4jParamters_tLogRow_1.append("PRINT_CONTENT_WITH_LOG4J" + " = " + "true");
								log4jParamters_tLogRow_1.append(" | ");
								if (log.isDebugEnabled())
									log.debug("tLogRow_1 - " + (log4jParamters_tLogRow_1));
							}
						}
						new BytesLimit65535_tLogRow_1().limitLog4jByte();
					}
					if (enableLogStash) {
						talendJobLog.addCM("tLogRow_1", "tLogRow_1", "tLogRow");
						talendJobLogProcess(globalMap);
					}

					///////////////////////

					class Util_tLogRow_1 {

						String[] des_top = { ".", ".", "-", "+" };

						String[] des_head = { "|=", "=|", "-", "+" };

						String[] des_bottom = { "'", "'", "-", "+" };

						String name = "";

						java.util.List<String[]> list = new java.util.ArrayList<String[]>();

						int[] colLengths = new int[4];

						public void addRow(String[] row) {

							for (int i = 0; i < 4; i++) {
								if (row[i] != null) {
									colLengths[i] = Math.max(colLengths[i], row[i].length());
								}
							}
							list.add(row);
						}

						public void setTableName(String name) {

							this.name = name;
						}

						public StringBuilder format() {

							StringBuilder sb = new StringBuilder();

							sb.append(print(des_top));

							int totals = 0;
							for (int i = 0; i < colLengths.length; i++) {
								totals = totals + colLengths[i];
							}

							// name
							sb.append("|");
							int k = 0;
							for (k = 0; k < (totals + 3 - name.length()) / 2; k++) {
								sb.append(' ');
							}
							sb.append(name);
							for (int i = 0; i < totals + 3 - name.length() - k; i++) {
								sb.append(' ');
							}
							sb.append("|\n");

							// head and rows
							sb.append(print(des_head));
							for (int i = 0; i < list.size(); i++) {

								String[] row = list.get(i);

								java.util.Formatter formatter = new java.util.Formatter(new StringBuilder());

								StringBuilder sbformat = new StringBuilder();
								sbformat.append("|%1$-");
								sbformat.append(colLengths[0]);
								sbformat.append("s");

								sbformat.append("|%2$-");
								sbformat.append(colLengths[1]);
								sbformat.append("s");

								sbformat.append("|%3$-");
								sbformat.append(colLengths[2]);
								sbformat.append("s");

								sbformat.append("|%4$-");
								sbformat.append(colLengths[3]);
								sbformat.append("s");

								sbformat.append("|\n");

								formatter.format(sbformat.toString(), (Object[]) row);

								sb.append(formatter.toString());
								if (i == 0)
									sb.append(print(des_head)); // print the head
							}

							// end
							sb.append(print(des_bottom));
							return sb;
						}

						private StringBuilder print(String[] fillChars) {
							StringBuilder sb = new StringBuilder();
							// first column
							sb.append(fillChars[0]);
							for (int i = 0; i < colLengths[0] - fillChars[0].length() + 1; i++) {
								sb.append(fillChars[2]);
							}
							sb.append(fillChars[3]);

							for (int i = 0; i < colLengths[1] - fillChars[3].length() + 1; i++) {
								sb.append(fillChars[2]);
							}
							sb.append(fillChars[3]);
							for (int i = 0; i < colLengths[2] - fillChars[3].length() + 1; i++) {
								sb.append(fillChars[2]);
							}
							sb.append(fillChars[3]);

							// last column
							for (int i = 0; i < colLengths[3] - fillChars[1].length() + 1; i++) {
								sb.append(fillChars[2]);
							}
							sb.append(fillChars[1]);
							sb.append("\n");
							return sb;
						}

						public boolean isTableEmpty() {
							if (list.size() > 1)
								return false;
							return true;
						}
					}
					Util_tLogRow_1 util_tLogRow_1 = new Util_tLogRow_1();
					util_tLogRow_1.setTableName("tLogRow_1");
					util_tLogRow_1.addRow(new String[] { "Date", "Author", "Object", "Status", });
					StringBuilder strBuffer_tLogRow_1 = null;
					int nb_line_tLogRow_1 = 0;
///////////////////////    			

					/**
					 * [tLogRow_1 begin ] stop
					 */

					/**
					 * [tFileInputMail_1 begin ] start
					 */

					ok_Hash.put("tFileInputMail_1", false);
					start_Hash.put("tFileInputMail_1", System.currentTimeMillis());

					currentComponent = "tFileInputMail_1";

					int tos_count_tFileInputMail_1 = 0;

					if (log.isDebugEnabled())
						log.debug("tFileInputMail_1 - " + ("Start to work."));
					if (log.isDebugEnabled()) {
						class BytesLimit65535_tFileInputMail_1 {
							public void limitLog4jByte() throws Exception {
								StringBuilder log4jParamters_tFileInputMail_1 = new StringBuilder();
								log4jParamters_tFileInputMail_1.append("Parameters:");
								log4jParamters_tFileInputMail_1.append("FILENAME" + " = "
										+ "((String)globalMap.get(\"tFileList_1_CURRENT_FILEPATH\"))");
								log4jParamters_tFileInputMail_1.append(" | ");
								log4jParamters_tFileInputMail_1.append("MAIL_TYPE" + " = " + "MIME");
								log4jParamters_tFileInputMail_1.append(" | ");
								log4jParamters_tFileInputMail_1.append("ATTACHMENT_PATH" + " = "
										+ "\"C:/SIFT/Developer/Talend/Data/Test/Attachments\"");
								log4jParamters_tFileInputMail_1.append(" | ");
								log4jParamters_tFileInputMail_1.append("MAIL_PARTS" + " = " + "[{MAIL_PART="
										+ ("\"Date\"") + ", MULTI_VALUE=" + ("false") + ", SCHEMA_COLUMN=" + ("Date")
										+ ", PART_SEPARATOR=" + ("\"\"") + "}, {MAIL_PART=" + ("\"From\"")
										+ ", MULTI_VALUE=" + ("false") + ", SCHEMA_COLUMN=" + ("Author")
										+ ", PART_SEPARATOR=" + ("\"\"") + "}, {MAIL_PART=" + ("\"Subject\"")
										+ ", MULTI_VALUE=" + ("false") + ", SCHEMA_COLUMN=" + ("Object")
										+ ", PART_SEPARATOR=" + ("\"\"") + "}, {MAIL_PART=" + ("\"Received\"")
										+ ", MULTI_VALUE=" + ("true") + ", SCHEMA_COLUMN=" + ("Status")
										+ ", PART_SEPARATOR=" + ("\"\"") + "}]");
								log4jParamters_tFileInputMail_1.append(" | ");
								log4jParamters_tFileInputMail_1.append("DIE_ON_ERROR" + " = " + "true");
								log4jParamters_tFileInputMail_1.append(" | ");
								if (log.isDebugEnabled())
									log.debug("tFileInputMail_1 - " + (log4jParamters_tFileInputMail_1));
							}
						}
						new BytesLimit65535_tFileInputMail_1().limitLog4jByte();
					}
					if (enableLogStash) {
						talendJobLog.addCM("tFileInputMail_1", "tFileInputMail_1", "tFileInputMail");
						talendJobLogProcess(globalMap);
					}

					if (!("C:/SIFT/Developer/Talend/Data/Test/Attachments").endsWith("/")) {
						globalMap.put("tFileInputMail_1_EXPORTED_FILE_PATH",
								"C:/SIFT/Developer/Talend/Data/Test/Attachments" + "/");
					} else {
						globalMap.put("tFileInputMail_1_EXPORTED_FILE_PATH",
								"C:/SIFT/Developer/Talend/Data/Test/Attachments");
					}

					// create output directory if not exists
					String path = "C:/SIFT/Developer/Talend/Data/Test/Attachments";
					if (!path.endsWith("/")) {
						path = path + "/";
					}
					java.io.File outputDir = new java.io.File(path);
					if (!outputDir.exists()) {
						outputDir.mkdirs();
					}

					boolean hasData_tFileInputMail_1 = false;

					String[] mailParts_tFileInputMail_1 = new String[] {

							"Date",

							"From",

							"Subject",

							"Received",

					};
					String[] mailChecked_tFileInputMail_1 = new String[] {

							"false",

							"false",

							"false",

							"true",

					};
					String[] mailSeparator_tFileInputMail_1 = new String[] {

							"",

							"",

							"",

							"",

					};
					java.io.FileInputStream fileInputtFileInputMail_1 = null;

					try {
						fileInputtFileInputMail_1 = new java.io.FileInputStream(
								((String) globalMap.get("tFileList_1_CURRENT_FILEPATH")));
						javax.mail.Session session_tFileInputMail_1 = javax.mail.Session
								.getInstance(System.getProperties(), null);
						javax.mail.internet.MimeMessage msg_tFileInputMail_1 = new javax.mail.internet.MimeMessage(
								session_tFileInputMail_1, fileInputtFileInputMail_1);
						java.util.List<String> list_tFileInputMail_1 = new java.util.ArrayList<String>();

						for (int i_tFileInputMail_1 = 0; i_tFileInputMail_1 < mailParts_tFileInputMail_1.length; i_tFileInputMail_1++) {
							String part_tFileInputMail_1 = mailParts_tFileInputMail_1[i_tFileInputMail_1];
							String sep_tFileInputMail_1 = mailSeparator_tFileInputMail_1[i_tFileInputMail_1];
							if (part_tFileInputMail_1.equalsIgnoreCase("body")) {
								boolean multiValueBody_tFileInputMail_1 = ("true")
										.equals(mailChecked_tFileInputMail_1[i_tFileInputMail_1]);
								if (msg_tFileInputMail_1.isMimeType("multipart/*")) {
									javax.mail.Multipart mptFileInputMail_1 = (javax.mail.Multipart) msg_tFileInputMail_1
											.getContent();
									StringBuilder body_tFileInputMail_1 = new StringBuilder();
									for (int i = 0; i < mptFileInputMail_1.getCount(); i++) {
										javax.mail.BodyPart mparttFileInputMail_1 = mptFileInputMail_1.getBodyPart(i);
										String dispositiontFileInputMail_1 = mparttFileInputMail_1.getDisposition();
										if (!((dispositiontFileInputMail_1 != null) && ((dispositiontFileInputMail_1
												.equals(javax.mail.Part.ATTACHMENT))
												|| (dispositiontFileInputMail_1.equals(javax.mail.Part.INLINE))))) {
											// the following extract the body part(text/plain + text/html)
											try {
												Object content_tFileInputMail_1 = mparttFileInputMail_1.getContent();
												if (content_tFileInputMail_1 instanceof javax.mail.internet.MimeMultipart) {
													javax.mail.internet.MimeMultipart mimeMultipart_tFileInputMail_1 = (javax.mail.internet.MimeMultipart) content_tFileInputMail_1;
													for (int j_tFileInputMail_1 = 0; j_tFileInputMail_1 < mimeMultipart_tFileInputMail_1
															.getCount(); j_tFileInputMail_1++) {
														javax.mail.BodyPart bodyPart_tFileInputMail_1 = mimeMultipart_tFileInputMail_1
																.getBodyPart(j_tFileInputMail_1);

														Object content_tFileInputMail_1_body = bodyPart_tFileInputMail_1
																.getContent();
														if (content_tFileInputMail_1_body instanceof javax.mail.internet.MimeMultipart) {
															javax.mail.internet.MimeMultipart mimeMultipart_tFileInputMail_1_body = (javax.mail.internet.MimeMultipart) content_tFileInputMail_1_body;
															for (int j_tFileInputMail_1_body = 0; j_tFileInputMail_1_body < mimeMultipart_tFileInputMail_1_body
																	.getCount(); j_tFileInputMail_1_body++) {
																javax.mail.BodyPart bodyPart_tFileInputMail_1_body = mimeMultipart_tFileInputMail_1_body
																		.getBodyPart(j_tFileInputMail_1_body);
																if (bodyPart_tFileInputMail_1_body
																		.isMimeType("text/*")) {
																	if (multiValueBody_tFileInputMail_1) {
																		body_tFileInputMail_1
																				.append(bodyPart_tFileInputMail_1_body
																						.getContent().toString())
																				.append(sep_tFileInputMail_1);
																	} else {
																		list_tFileInputMail_1
																				.add(bodyPart_tFileInputMail_1_body
																						.getContent().toString());
																	}
																} else {
																	System.out.println("Ignore the part "
																			+ bodyPart_tFileInputMail_1_body
																					.getContentType());
																}
															}
														} else {
															if (bodyPart_tFileInputMail_1.isMimeType("text/*")) {
																if (multiValueBody_tFileInputMail_1) {
																	body_tFileInputMail_1
																			.append(bodyPart_tFileInputMail_1
																					.getContent().toString())
																			.append(sep_tFileInputMail_1);
																} else {
																	list_tFileInputMail_1.add(bodyPart_tFileInputMail_1
																			.getContent().toString());
																}
															} else {
																System.out.println("Ignore the part "
																		+ bodyPart_tFileInputMail_1.getContentType());
															}
														}
													}
												} else {
													if (multiValueBody_tFileInputMail_1) {
														body_tFileInputMail_1
																.append(mparttFileInputMail_1.getContent().toString())
																.append(sep_tFileInputMail_1);
													} else {
														list_tFileInputMail_1
																.add(mparttFileInputMail_1.getContent().toString());
													}
												}
											} catch (java.io.UnsupportedEncodingException e) {
												globalMap.put("tFileInputMail_1_ERROR_MESSAGE", e.getMessage());
												java.io.ByteArrayOutputStream bao_tFileInputMail_1 = new java.io.ByteArrayOutputStream();
												mparttFileInputMail_1.writeTo(bao_tFileInputMail_1);
												if (multiValueBody_tFileInputMail_1) {
													body_tFileInputMail_1.append(bao_tFileInputMail_1.toString())
															.append(sep_tFileInputMail_1);
												} else {
													list_tFileInputMail_1.add(bao_tFileInputMail_1.toString());
												}

												log.warn("tFileInputMail_1 - " + bao_tFileInputMail_1.toString());

											}

										} else if (dispositiontFileInputMail_1 != null
												&& dispositiontFileInputMail_1.equals(javax.mail.Part.INLINE)) {
											if (multiValueBody_tFileInputMail_1) {
												body_tFileInputMail_1
														.append(mparttFileInputMail_1.getContent().toString())
														.append(sep_tFileInputMail_1);
											} else {
												list_tFileInputMail_1
														.add(mparttFileInputMail_1.getContent().toString());
											}
										}
									}
									if (multiValueBody_tFileInputMail_1) {
										list_tFileInputMail_1.add(body_tFileInputMail_1.toString());
									}
								} else {
									java.io.InputStream in_tFileInputMail_1 = msg_tFileInputMail_1.getInputStream();
									byte[] buffer_tFileInputMail_1 = new byte[1024];
									int length_tFileInputMail_1 = 0;
									java.io.ByteArrayOutputStream baos_tFileInputMail_1 = new java.io.ByteArrayOutputStream();
									while ((length_tFileInputMail_1 = in_tFileInputMail_1.read(buffer_tFileInputMail_1,
											0, 1024)) != -1) {
										baos_tFileInputMail_1.write(buffer_tFileInputMail_1, 0,
												length_tFileInputMail_1);
									}
									String contentType_tFileInputMail_1 = msg_tFileInputMail_1.getContentType();
									String charsetName_tFileInputMail_1 = "";
									if (contentType_tFileInputMail_1 != null
											&& contentType_tFileInputMail_1.trim().length() > 0) {
										javax.mail.internet.ContentType cy_tFileInputMail_1 = new javax.mail.internet.ContentType(
												contentType_tFileInputMail_1);
										charsetName_tFileInputMail_1 = cy_tFileInputMail_1.getParameter("charset");
									}
									if (charsetName_tFileInputMail_1 != null
											&& charsetName_tFileInputMail_1.length() > 0) {
										list_tFileInputMail_1
												.add(baos_tFileInputMail_1.toString(charsetName_tFileInputMail_1));
									} else {
										list_tFileInputMail_1.add(baos_tFileInputMail_1.toString());
									}
									in_tFileInputMail_1.close();
									baos_tFileInputMail_1.close();
								}
							} else if (part_tFileInputMail_1.equalsIgnoreCase("header")) {
								java.util.Enumeration em = msg_tFileInputMail_1.getAllHeaderLines();
								int em_count = 0;

								String tempStr_tFileInputMail_1 = "";

								while (em.hasMoreElements()) {
									tempStr_tFileInputMail_1 = tempStr_tFileInputMail_1 + (String) em.nextElement()
											+ sep_tFileInputMail_1;
								}
								list_tFileInputMail_1.add(tempStr_tFileInputMail_1 == null ? null
										: javax.mail.internet.MimeUtility.decodeText(tempStr_tFileInputMail_1));
							} else {
								if (("true").equals(mailChecked_tFileInputMail_1[i_tFileInputMail_1])) {
									String[] sa_tFileInputMail_1 = msg_tFileInputMail_1
											.getHeader(part_tFileInputMail_1);
									String tempStr_tFileInputMail_1 = "";
									if (sa_tFileInputMail_1 != null) {
										for (int i = 0; i < sa_tFileInputMail_1.length; i++) {
											tempStr_tFileInputMail_1 = tempStr_tFileInputMail_1 + sa_tFileInputMail_1[i]
													+ sep_tFileInputMail_1;
										}
									} else {
										tempStr_tFileInputMail_1 = null;
									}
									list_tFileInputMail_1.add(tempStr_tFileInputMail_1 == null ? null
											: javax.mail.internet.MimeUtility.decodeText(tempStr_tFileInputMail_1));
								} else {
									String content_tFileInputMail_1 = msg_tFileInputMail_1
											.getHeader(part_tFileInputMail_1, null);
									list_tFileInputMail_1.add(content_tFileInputMail_1 == null ? null
											: javax.mail.internet.MimeUtility.decodeText(content_tFileInputMail_1));
								}
							}
						}

						// attachment Deal
						class MessagePartProcessor {
							void saveAttachment(javax.mail.Part mpart)
									throws IOException, javax.mail.MessagingException {
								if (mpart.getFileName() != null && (mpart.getDisposition() == null
										|| (mpart.getDisposition().equalsIgnoreCase(javax.mail.Part.ATTACHMENT)
												|| mpart.getDisposition().equalsIgnoreCase(javax.mail.Part.INLINE)))) {
									String attachFileName = javax.mail.internet.MimeUtility
											.decodeText(mpart.getFileName());

									String path = "C:/SIFT/Developer/Talend/Data/Test/Attachments";
									if (!path.endsWith("/")) {
										path = path + "/";
									}

									java.io.File attachFile = getUniqueFileName(attachFileName, new java.io.File(path));
									log.info("tFileInputMail_1 - Extracting attachment: '" + attachFile.getName()
											+ "'.");
									try (java.io.BufferedOutputStream out = new java.io.BufferedOutputStream(
											new java.io.FileOutputStream(attachFile));
											java.io.BufferedInputStream in = new java.io.BufferedInputStream(
													mpart.getInputStream())) {
										byte[] buffer = new byte[8192];
										int bytesRead = 0;
										while ((bytesRead = in.read(buffer)) > 0) {
											out.write(buffer, 0, bytesRead);
										}
										out.flush();
									}
								}
							}

							private java.io.File getUniqueFileName(String fileName, java.io.File dir) {

								int num = 1;
								final String ext = getFileExtension(fileName);
								final String name = getFileName(fileName);
								java.io.File file = new java.io.File(dir, fileName);
								while (file.exists()) {
									num++;
									file = new java.io.File(dir, name + "_" + num + (ext != null ? ext : ""));
								}
								return file;
							}

							private String getFileExtension(final String path) {
								if (path != null && path.lastIndexOf('.') != -1) {
									return path.substring(path.lastIndexOf('.'));
								}
								return null;
							}

							private String getFileName(String fileName) {
								int lastIndex = fileName.lastIndexOf('.');
								return lastIndex < 0 ? fileName : fileName.substring(0, fileName.lastIndexOf('.'));
							}

							// recursively process body parts
							void processPart(javax.mail.Part part) throws javax.mail.MessagingException, IOException {
								if (part.isMimeType("multipart/*")) {
									javax.mail.Multipart multipartContent = (javax.mail.Multipart) part.getContent();
									for (int i = 0; i < multipartContent.getCount(); i++) {
										javax.mail.Part mpart = multipartContent.getBodyPart(i);
										saveAttachment(mpart);
										processPart(mpart);
									}
								}
							}
						}
						new MessagePartProcessor().processPart(msg_tFileInputMail_1);

						// for output

						if (0 < list_tFileInputMail_1.size() && list_tFileInputMail_1.get(0) != null) {

							row1.Date = (String) list_tFileInputMail_1.get(0);

						} else {

							row1.Date = null;
						}

						hasData_tFileInputMail_1 = true;

						if (1 < list_tFileInputMail_1.size() && list_tFileInputMail_1.get(1) != null) {

							row1.Author = (String) list_tFileInputMail_1.get(1);

						} else {

							row1.Author = null;
						}

						if (2 < list_tFileInputMail_1.size() && list_tFileInputMail_1.get(2) != null) {

							row1.Object = (String) list_tFileInputMail_1.get(2);

						} else {

							row1.Object = null;
						}

						if (3 < list_tFileInputMail_1.size() && list_tFileInputMail_1.get(3) != null) {

							row1.Status = (String) list_tFileInputMail_1.get(3);

						} else {

							row1.Status = null;
						}

					}

					finally {
						if (fileInputtFileInputMail_1 != null)
							fileInputtFileInputMail_1.close();
					}
					////////////////////////////
					if (hasData_tFileInputMail_1) {

						/**
						 * [tFileInputMail_1 begin ] stop
						 */

						/**
						 * [tFileInputMail_1 main ] start
						 */

						currentComponent = "tFileInputMail_1";

						tos_count_tFileInputMail_1++;

						/**
						 * [tFileInputMail_1 main ] stop
						 */

						/**
						 * [tFileInputMail_1 process_data_begin ] start
						 */

						currentComponent = "tFileInputMail_1";

						/**
						 * [tFileInputMail_1 process_data_begin ] stop
						 */

						/**
						 * [tLogRow_1 main ] start
						 */

						currentComponent = "tLogRow_1";

						if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

								, "row1", "tFileInputMail_1", "tFileInputMail_1", "tFileInputMail", "tLogRow_1",
								"tLogRow_1", "tLogRow"

						)) {
							talendJobLogProcess(globalMap);
						}

						if (log.isTraceEnabled()) {
							log.trace("row1 - " + (row1 == null ? "" : row1.toLogString()));
						}

///////////////////////		

						String[] row_tLogRow_1 = new String[4];

						if (row1.Date != null) { //
							row_tLogRow_1[0] = String.valueOf(row1.Date);

						} //

						if (row1.Author != null) { //
							row_tLogRow_1[1] = String.valueOf(row1.Author);

						} //

						if (row1.Object != null) { //
							row_tLogRow_1[2] = String.valueOf(row1.Object);

						} //

						if (row1.Status != null) { //
							row_tLogRow_1[3] = String.valueOf(row1.Status);

						} //

						util_tLogRow_1.addRow(row_tLogRow_1);
						nb_line_tLogRow_1++;
						log.info("tLogRow_1 - Content of row " + nb_line_tLogRow_1 + ": "
								+ TalendString.unionString("|", row_tLogRow_1));
//////

//////                    

///////////////////////    			

						tos_count_tLogRow_1++;

						/**
						 * [tLogRow_1 main ] stop
						 */

						/**
						 * [tLogRow_1 process_data_begin ] start
						 */

						currentComponent = "tLogRow_1";

						/**
						 * [tLogRow_1 process_data_begin ] stop
						 */

						/**
						 * [tLogRow_1 process_data_end ] start
						 */

						currentComponent = "tLogRow_1";

						/**
						 * [tLogRow_1 process_data_end ] stop
						 */

						/**
						 * [tFileInputMail_1 process_data_end ] start
						 */

						currentComponent = "tFileInputMail_1";

						/**
						 * [tFileInputMail_1 process_data_end ] stop
						 */

						/**
						 * [tFileInputMail_1 end ] start
						 */

						currentComponent = "tFileInputMail_1";

					}

					if (log.isDebugEnabled())
						log.debug("tFileInputMail_1 - " + ("Done."));

					ok_Hash.put("tFileInputMail_1", true);
					end_Hash.put("tFileInputMail_1", System.currentTimeMillis());

					/**
					 * [tFileInputMail_1 end ] stop
					 */

					/**
					 * [tLogRow_1 end ] start
					 */

					currentComponent = "tLogRow_1";

//////

					java.io.PrintStream consoleOut_tLogRow_1 = null;
					if (globalMap.get("tLogRow_CONSOLE") != null) {
						consoleOut_tLogRow_1 = (java.io.PrintStream) globalMap.get("tLogRow_CONSOLE");
					} else {
						consoleOut_tLogRow_1 = new java.io.PrintStream(new java.io.BufferedOutputStream(System.out));
						globalMap.put("tLogRow_CONSOLE", consoleOut_tLogRow_1);
					}

					consoleOut_tLogRow_1.println(util_tLogRow_1.format().toString());
					consoleOut_tLogRow_1.flush();
//////
					globalMap.put("tLogRow_1_NB_LINE", nb_line_tLogRow_1);
					if (log.isInfoEnabled())
						log.info("tLogRow_1 - " + ("Printed row count: ") + (nb_line_tLogRow_1) + ("."));

///////////////////////    			

					if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row1", 2, 0,
							"tFileInputMail_1", "tFileInputMail_1", "tFileInputMail", "tLogRow_1", "tLogRow_1",
							"tLogRow", "output")) {
						talendJobLogProcess(globalMap);
					}

					if (log.isDebugEnabled())
						log.debug("tLogRow_1 - " + ("Done."));

					ok_Hash.put("tLogRow_1", true);
					end_Hash.put("tLogRow_1", System.currentTimeMillis());

					/**
					 * [tLogRow_1 end ] stop
					 */

					if (execStat) {
						runStat.updateStatOnConnection("iterate1", 2, "exec" + NB_ITERATE_tFileInputMail_1);
					}

					/**
					 * [tFileList_1 process_data_end ] start
					 */

					currentComponent = "tFileList_1";

					/**
					 * [tFileList_1 process_data_end ] stop
					 */

					/**
					 * [tFileList_1 end ] start
					 */

					currentComponent = "tFileList_1";

				}
				globalMap.put("tFileList_1_NB_FILE", NB_FILEtFileList_1);

				log.info("tFileList_1 - File or directory count : " + NB_FILEtFileList_1);

				if (log.isDebugEnabled())
					log.debug("tFileList_1 - " + ("Done."));

				ok_Hash.put("tFileList_1", true);
				end_Hash.put("tFileList_1", System.currentTimeMillis());

				/**
				 * [tFileList_1 end ] stop
				 */
			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tFileList_1 finally ] start
				 */

				currentComponent = "tFileList_1";

				/**
				 * [tFileList_1 finally ] stop
				 */

				/**
				 * [tFileInputMail_1 finally ] start
				 */

				currentComponent = "tFileInputMail_1";

				/**
				 * [tFileInputMail_1 finally ] stop
				 */

				/**
				 * [tLogRow_1 finally ] start
				 */

				currentComponent = "tLogRow_1";

				/**
				 * [tLogRow_1 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tFileList_1_SUBPROCESS_STATE", 1);
	}

	public void talendJobLogProcess(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("talendJobLog_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdcInfo.forEach(org.slf4j.MDC::put);
		org.slf4j.MDC.put("_subJobName", "talendJobLog");
		org.slf4j.MDC.put("_subJobPid", "qnI9Gv_" + subJobPidCounter.getAndIncrement());

		String iterateId = "";

		String currentComponent = "";
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [talendJobLog begin ] start
				 */

				ok_Hash.put("talendJobLog", false);
				start_Hash.put("talendJobLog", System.currentTimeMillis());

				currentComponent = "talendJobLog";

				int tos_count_talendJobLog = 0;

				for (JobStructureCatcherUtils.JobStructureCatcherMessage jcm : talendJobLog.getMessages()) {
					org.talend.job.audit.JobContextBuilder builder_talendJobLog = org.talend.job.audit.JobContextBuilder
							.create().jobName(jcm.job_name).jobId(jcm.job_id).jobVersion(jcm.job_version)
							.custom("process_id", jcm.pid).custom("thread_id", jcm.tid).custom("pid", pid)
							.custom("father_pid", fatherPid).custom("root_pid", rootPid);
					org.talend.logging.audit.Context log_context_talendJobLog = null;

					if (jcm.log_type == JobStructureCatcherUtils.LogType.PERFORMANCE) {
						long timeMS = jcm.end_time - jcm.start_time;
						String duration = String.valueOf(timeMS);

						log_context_talendJobLog = builder_talendJobLog.sourceId(jcm.sourceId)
								.sourceLabel(jcm.sourceLabel).sourceConnectorType(jcm.sourceComponentName)
								.targetId(jcm.targetId).targetLabel(jcm.targetLabel)
								.targetConnectorType(jcm.targetComponentName).connectionName(jcm.current_connector)
								.rows(jcm.row_count).duration(duration).build();
						auditLogger_talendJobLog.flowExecution(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.JOBSTART) {
						log_context_talendJobLog = builder_talendJobLog.timestamp(jcm.moment).build();
						auditLogger_talendJobLog.jobstart(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.JOBEND) {
						long timeMS = jcm.end_time - jcm.start_time;
						String duration = String.valueOf(timeMS);

						log_context_talendJobLog = builder_talendJobLog.timestamp(jcm.moment).duration(duration)
								.status(jcm.status).build();
						auditLogger_talendJobLog.jobstop(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.RUNCOMPONENT) {
						log_context_talendJobLog = builder_talendJobLog.timestamp(jcm.moment)
								.connectorType(jcm.component_name).connectorId(jcm.component_id)
								.connectorLabel(jcm.component_label).build();
						auditLogger_talendJobLog.runcomponent(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.FLOWINPUT) {// log current component
																							// input line
						long timeMS = jcm.end_time - jcm.start_time;
						String duration = String.valueOf(timeMS);

						log_context_talendJobLog = builder_talendJobLog.connectorType(jcm.component_name)
								.connectorId(jcm.component_id).connectorLabel(jcm.component_label)
								.connectionName(jcm.current_connector).connectionType(jcm.current_connector_type)
								.rows(jcm.total_row_number).duration(duration).build();
						auditLogger_talendJobLog.flowInput(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.FLOWOUTPUT) {// log current component
																								// output/reject line
						long timeMS = jcm.end_time - jcm.start_time;
						String duration = String.valueOf(timeMS);

						log_context_talendJobLog = builder_talendJobLog.connectorType(jcm.component_name)
								.connectorId(jcm.component_id).connectorLabel(jcm.component_label)
								.connectionName(jcm.current_connector).connectionType(jcm.current_connector_type)
								.rows(jcm.total_row_number).duration(duration).build();
						auditLogger_talendJobLog.flowOutput(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.JOBERROR) {
						java.lang.Exception e_talendJobLog = jcm.exception;
						if (e_talendJobLog != null) {
							try (java.io.StringWriter sw_talendJobLog = new java.io.StringWriter();
									java.io.PrintWriter pw_talendJobLog = new java.io.PrintWriter(sw_talendJobLog)) {
								e_talendJobLog.printStackTrace(pw_talendJobLog);
								builder_talendJobLog.custom("stacktrace", sw_talendJobLog.getBuffer().substring(0,
										java.lang.Math.min(sw_talendJobLog.getBuffer().length(), 512)));
							}
						}

						if (jcm.extra_info != null) {
							builder_talendJobLog.connectorId(jcm.component_id).custom("extra_info", jcm.extra_info);
						}

						log_context_talendJobLog = builder_talendJobLog
								.connectorType(jcm.component_id.substring(0, jcm.component_id.lastIndexOf('_')))
								.connectorId(jcm.component_id)
								.connectorLabel(jcm.component_label == null ? jcm.component_id : jcm.component_label)
								.build();

						auditLogger_talendJobLog.exception(log_context_talendJobLog);
					}

				}

				/**
				 * [talendJobLog begin ] stop
				 */

				/**
				 * [talendJobLog main ] start
				 */

				currentComponent = "talendJobLog";

				tos_count_talendJobLog++;

				/**
				 * [talendJobLog main ] stop
				 */

				/**
				 * [talendJobLog process_data_begin ] start
				 */

				currentComponent = "talendJobLog";

				/**
				 * [talendJobLog process_data_begin ] stop
				 */

				/**
				 * [talendJobLog process_data_end ] start
				 */

				currentComponent = "talendJobLog";

				/**
				 * [talendJobLog process_data_end ] stop
				 */

				/**
				 * [talendJobLog end ] start
				 */

				currentComponent = "talendJobLog";

				ok_Hash.put("talendJobLog", true);
				end_Hash.put("talendJobLog", System.currentTimeMillis());

				/**
				 * [talendJobLog end ] stop
				 */
			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [talendJobLog finally ] start
				 */

				currentComponent = "talendJobLog";

				/**
				 * [talendJobLog finally ] stop
				 */
			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("talendJobLog_SUBPROCESS_STATE", 1);
	}

	public String resuming_logs_dir_path = null;
	public String resuming_checkpoint_path = null;
	public String parent_part_launcher = null;
	private String resumeEntryMethodName = null;
	private boolean globalResumeTicket = false;

	public boolean watch = false;
	// portStats is null, it means don't execute the statistics
	public Integer portStats = null;
	public int portTraces = 4334;
	public String clientHost;
	public String defaultClientHost = "localhost";
	public String contextStr = "Default";
	public boolean isDefaultContext = true;
	public String pid = "0";
	public String rootPid = null;
	public String fatherPid = null;
	public String fatherNode = null;
	public long startTime = 0;
	public boolean isChildJob = false;
	public String log4jLevel = "";

	private boolean enableLogStash;

	private boolean execStat = true;

	private ThreadLocal<java.util.Map<String, String>> threadLocal = new ThreadLocal<java.util.Map<String, String>>() {
		protected java.util.Map<String, String> initialValue() {
			java.util.Map<String, String> threadRunResultMap = new java.util.HashMap<String, String>();
			threadRunResultMap.put("errorCode", null);
			threadRunResultMap.put("status", "");
			return threadRunResultMap;
		};
	};

	protected PropertiesWithType context_param = new PropertiesWithType();
	public java.util.Map<String, Object> parentContextMap = new java.util.HashMap<String, Object>();

	public String status = "";

	private final static java.util.Properties jobInfo = new java.util.Properties();
	private final static java.util.Map<String, String> mdcInfo = new java.util.HashMap<>();
	private final static java.util.concurrent.atomic.AtomicLong subJobPidCounter = new java.util.concurrent.atomic.AtomicLong();

	public static void main(String[] args) {
		final Copy_of_test Copy_of_testClass = new Copy_of_test();

		int exitCode = Copy_of_testClass.runJobInTOS(args);
		if (exitCode == 0) {
			log.info("TalendJob: 'Copy_of_test' - Done.");
		}

		System.exit(exitCode);
	}

	private void getjobInfo() {
		final String TEMPLATE_PATH = "src/main/templates/jobInfo_template.properties";
		final String BUILD_PATH = "../jobInfo.properties";
		final String path = this.getClass().getResource("").getPath();
		if (path.lastIndexOf("target") > 0) {
			final java.io.File templateFile = new java.io.File(
					path.substring(0, path.lastIndexOf("target")).concat(TEMPLATE_PATH));
			if (templateFile.exists()) {
				readJobInfo(templateFile);
				return;
			}
		}
		readJobInfo(new java.io.File(BUILD_PATH));
	}

	private void readJobInfo(java.io.File jobInfoFile) {

		if (jobInfoFile.exists()) {
			try (java.io.InputStream is = new java.io.FileInputStream(jobInfoFile)) {
				jobInfo.load(is);
			} catch (IOException e) {

				log.debug("Read jobInfo.properties file fail: " + e.getMessage());

			}
		}
		log.info(String.format("Project name: %s\tJob name: %s\tGIT Commit ID: %s\tTalend Version: %s", projectName,
				jobName, jobInfo.getProperty("gitCommitId"), "8.0.1.20231222_1430-patch"));

	}

	public String[][] runJob(String[] args) {

		int exitCode = runJobInTOS(args);
		String[][] bufferValue = new String[][] { { Integer.toString(exitCode) } };

		return bufferValue;
	}

	public boolean hastBufferOutputComponent() {
		boolean hastBufferOutput = false;

		return hastBufferOutput;
	}

	public int runJobInTOS(String[] args) {
		// reset status
		status = "";

		String lastStr = "";
		for (String arg : args) {
			if (arg.equalsIgnoreCase("--context_param")) {
				lastStr = arg;
			} else if (lastStr.equals("")) {
				evalParam(arg);
			} else {
				evalParam(lastStr + " " + arg);
				lastStr = "";
			}
		}
		enableLogStash = "true".equalsIgnoreCase(System.getProperty("audit.enabled"));

		if (!"".equals(log4jLevel)) {

			if ("trace".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.TRACE);
			} else if ("debug".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.DEBUG);
			} else if ("info".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.INFO);
			} else if ("warn".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.WARN);
			} else if ("error".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.ERROR);
			} else if ("fatal".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.FATAL);
			} else if ("off".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.OFF);
			}
			org.apache.logging.log4j.core.config.Configurator
					.setLevel(org.apache.logging.log4j.LogManager.getRootLogger().getName(), log.getLevel());

		}

		getjobInfo();
		log.info("TalendJob: 'Copy_of_test' - Start.");

		java.util.Set<Object> jobInfoKeys = jobInfo.keySet();
		for (Object jobInfoKey : jobInfoKeys) {
			org.slf4j.MDC.put("_" + jobInfoKey.toString(), jobInfo.get(jobInfoKey).toString());
		}
		org.slf4j.MDC.put("_pid", pid);
		org.slf4j.MDC.put("_rootPid", rootPid);
		org.slf4j.MDC.put("_fatherPid", fatherPid);
		org.slf4j.MDC.put("_projectName", projectName);
		org.slf4j.MDC.put("_startTimestamp", java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC)
				.format(java.time.format.DateTimeFormatter.ISO_INSTANT));
		org.slf4j.MDC.put("_jobRepositoryId", "_RS3GcKrjEe6UOOsCwsvcUA");
		org.slf4j.MDC.put("_compiledAtTimestamp", "2024-01-11T03:00:38.602882400Z");

		java.lang.management.RuntimeMXBean mx = java.lang.management.ManagementFactory.getRuntimeMXBean();
		String[] mxNameTable = mx.getName().split("@"); //$NON-NLS-1$
		if (mxNameTable.length == 2) {
			org.slf4j.MDC.put("_systemPid", mxNameTable[0]);
		} else {
			org.slf4j.MDC.put("_systemPid", String.valueOf(java.lang.Thread.currentThread().getId()));
		}

		if (enableLogStash) {
			java.util.Properties properties_talendJobLog = new java.util.Properties();
			properties_talendJobLog.setProperty("root.logger", "audit");
			properties_talendJobLog.setProperty("encoding", "UTF-8");
			properties_talendJobLog.setProperty("application.name", "Talend Studio");
			properties_talendJobLog.setProperty("service.name", "Talend Studio Job");
			properties_talendJobLog.setProperty("instance.name", "Talend Studio Job Instance");
			properties_talendJobLog.setProperty("propagate.appender.exceptions", "none");
			properties_talendJobLog.setProperty("log.appender", "file");
			properties_talendJobLog.setProperty("appender.file.path", "audit.json");
			properties_talendJobLog.setProperty("appender.file.maxsize", "52428800");
			properties_talendJobLog.setProperty("appender.file.maxbackup", "20");
			properties_talendJobLog.setProperty("host", "false");

			System.getProperties().stringPropertyNames().stream().filter(it -> it.startsWith("audit.logger."))
					.forEach(key -> properties_talendJobLog.setProperty(key.substring("audit.logger.".length()),
							System.getProperty(key)));

			org.apache.logging.log4j.core.config.Configurator
					.setLevel(properties_talendJobLog.getProperty("root.logger"), org.apache.logging.log4j.Level.DEBUG);

			auditLogger_talendJobLog = org.talend.job.audit.JobEventAuditLoggerFactory
					.createJobAuditLogger(properties_talendJobLog);
		}

		if (clientHost == null) {
			clientHost = defaultClientHost;
		}

		if (pid == null || "0".equals(pid)) {
			pid = TalendString.getAsciiRandomString(6);
		}

		org.slf4j.MDC.put("_pid", pid);

		if (rootPid == null) {
			rootPid = pid;
		}

		org.slf4j.MDC.put("_rootPid", rootPid);

		if (fatherPid == null) {
			fatherPid = pid;
		} else {
			isChildJob = true;
		}
		org.slf4j.MDC.put("_fatherPid", fatherPid);

		if (portStats != null) {
			// portStats = -1; //for testing
			if (portStats < 0 || portStats > 65535) {
				// issue:10869, the portStats is invalid, so this client socket can't open
				System.err.println("The statistics socket port " + portStats + " is invalid.");
				execStat = false;
			}
		} else {
			execStat = false;
		}
		boolean inOSGi = routines.system.BundleUtils.inOSGi();

		try {
			java.util.Dictionary<String, Object> jobProperties = null;
			if (inOSGi) {
				jobProperties = routines.system.BundleUtils.getJobProperties(jobName);

				if (jobProperties != null && jobProperties.get("context") != null) {
					contextStr = (String) jobProperties.get("context");
				}
			}
			// call job/subjob with an existing context, like: --context=production. if
			// without this parameter, there will use the default context instead.
			java.io.InputStream inContext = Copy_of_test.class.getClassLoader()
					.getResourceAsStream("democloud/copy_of_test_0_1/contexts/" + contextStr + ".properties");
			if (inContext == null) {
				inContext = Copy_of_test.class.getClassLoader()
						.getResourceAsStream("config/contexts/" + contextStr + ".properties");
			}
			if (inContext != null) {
				try {
					// defaultProps is in order to keep the original context value
					if (context != null && context.isEmpty()) {
						defaultProps.load(inContext);
						if (inOSGi && jobProperties != null) {
							java.util.Enumeration<String> keys = jobProperties.keys();
							while (keys.hasMoreElements()) {
								String propKey = keys.nextElement();
								if (defaultProps.containsKey(propKey)) {
									defaultProps.put(propKey, (String) jobProperties.get(propKey));
								}
							}
						}
						context = new ContextProperties(defaultProps);
					}
				} finally {
					inContext.close();
				}
			} else if (!isDefaultContext) {
				// print info and job continue to run, for case: context_param is not empty.
				System.err.println("Could not find the context " + contextStr);
			}

			if (!context_param.isEmpty()) {
				context.putAll(context_param);
				// set types for params from parentJobs
				for (Object key : context_param.keySet()) {
					String context_key = key.toString();
					String context_type = context_param.getContextType(context_key);
					context.setContextType(context_key, context_type);

				}
			}
			class ContextProcessing {
				private void processContext_0() {
				}

				public void processAllContext() {
					processContext_0();
				}
			}

			new ContextProcessing().processAllContext();
		} catch (java.io.IOException ie) {
			System.err.println("Could not load context " + contextStr);
			ie.printStackTrace();
		}

		// get context value from parent directly
		if (parentContextMap != null && !parentContextMap.isEmpty()) {
		}

		// Resume: init the resumeUtil
		resumeEntryMethodName = ResumeUtil.getResumeEntryMethodName(resuming_checkpoint_path);
		resumeUtil = new ResumeUtil(resuming_logs_dir_path, isChildJob, rootPid);
		resumeUtil.initCommonInfo(pid, rootPid, fatherPid, projectName, jobName, contextStr, jobVersion);

		List<String> parametersToEncrypt = new java.util.ArrayList<String>();
		// Resume: jobStart
		resumeUtil.addLog("JOB_STARTED", "JOB:" + jobName, parent_part_launcher, Thread.currentThread().getId() + "",
				"", "", "", "", resumeUtil.convertToJsonText(context, ContextProperties.class, parametersToEncrypt));

		org.slf4j.MDC.put("_context", contextStr);
		log.info("TalendJob: 'Copy_of_test' - Started.");
		java.util.Optional.ofNullable(org.slf4j.MDC.getCopyOfContextMap()).ifPresent(mdcInfo::putAll);

		if (execStat) {
			try {
				runStat.openSocket(!isChildJob);
				runStat.setAllPID(rootPid, fatherPid, pid, jobName);
				runStat.startThreadStat(clientHost, portStats);
				runStat.updateStatOnJob(RunStat.JOBSTART, fatherNode);
			} catch (java.io.IOException ioException) {
				ioException.printStackTrace();
			}
		}

		java.util.concurrent.ConcurrentHashMap<Object, Object> concurrentHashMap = new java.util.concurrent.ConcurrentHashMap<Object, Object>();
		globalMap.put("concurrentHashMap", concurrentHashMap);

		long startUsedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long endUsedMemory = 0;
		long end = 0;

		startTime = System.currentTimeMillis();

		this.globalResumeTicket = true;// to run tPreJob

		if (enableLogStash) {
			talendJobLog.addJobStartMessage();
			try {
				talendJobLogProcess(globalMap);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}

		this.globalResumeTicket = false;// to run others jobs

		try {
			errorCode = null;
			tPOP_2Process(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (TalendException e_tPOP_2) {
			globalMap.put("tPOP_2_SUBPROCESS_STATE", -1);

			e_tPOP_2.printStackTrace();

		}

		this.globalResumeTicket = true;// to run tPostJob

		end = System.currentTimeMillis();

		if (watch) {
			System.out.println((end - startTime) + " milliseconds");
		}

		endUsedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		if (false) {
			System.out
					.println((endUsedMemory - startUsedMemory) + " bytes memory increase when running : Copy_of_test");
		}
		if (enableLogStash) {
			talendJobLog.addJobEndMessage(startTime, end, status);
			try {
				talendJobLogProcess(globalMap);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}

		if (execStat) {
			runStat.updateStatOnJob(RunStat.JOBEND, fatherNode);
			runStat.stopThreadStat();
		}
		int returnCode = 0;

		if (errorCode == null) {
			returnCode = status != null && status.equals("failure") ? 1 : 0;
		} else {
			returnCode = errorCode.intValue();
		}
		resumeUtil.addLog("JOB_ENDED", "JOB:" + jobName, parent_part_launcher, Thread.currentThread().getId() + "", "",
				"" + returnCode, "", "", "");
		resumeUtil.flush();

		org.slf4j.MDC.remove("_subJobName");
		org.slf4j.MDC.remove("_subJobPid");
		org.slf4j.MDC.remove("_systemPid");
		log.info("TalendJob: 'Copy_of_test' - Finished - status: " + status + " returnCode: " + returnCode);

		return returnCode;

	}

	// only for OSGi env
	public void destroy() {

	}

	private java.util.Map<String, Object> getSharedConnections4REST() {
		java.util.Map<String, Object> connections = new java.util.HashMap<String, Object>();

		return connections;
	}

	private void evalParam(String arg) {
		if (arg.startsWith("--resuming_logs_dir_path")) {
			resuming_logs_dir_path = arg.substring(25);
		} else if (arg.startsWith("--resuming_checkpoint_path")) {
			resuming_checkpoint_path = arg.substring(27);
		} else if (arg.startsWith("--parent_part_launcher")) {
			parent_part_launcher = arg.substring(23);
		} else if (arg.startsWith("--watch")) {
			watch = true;
		} else if (arg.startsWith("--stat_port=")) {
			String portStatsStr = arg.substring(12);
			if (portStatsStr != null && !portStatsStr.equals("null")) {
				portStats = Integer.parseInt(portStatsStr);
			}
		} else if (arg.startsWith("--trace_port=")) {
			portTraces = Integer.parseInt(arg.substring(13));
		} else if (arg.startsWith("--client_host=")) {
			clientHost = arg.substring(14);
		} else if (arg.startsWith("--context=")) {
			contextStr = arg.substring(10);
			isDefaultContext = false;
		} else if (arg.startsWith("--father_pid=")) {
			fatherPid = arg.substring(13);
		} else if (arg.startsWith("--root_pid=")) {
			rootPid = arg.substring(11);
		} else if (arg.startsWith("--father_node=")) {
			fatherNode = arg.substring(14);
		} else if (arg.startsWith("--pid=")) {
			pid = arg.substring(6);
		} else if (arg.startsWith("--context_type")) {
			String keyValue = arg.substring(15);
			int index = -1;
			if (keyValue != null && (index = keyValue.indexOf('=')) > -1) {
				if (fatherPid == null) {
					context_param.setContextType(keyValue.substring(0, index),
							replaceEscapeChars(keyValue.substring(index + 1)));
				} else { // the subjob won't escape the especial chars
					context_param.setContextType(keyValue.substring(0, index), keyValue.substring(index + 1));
				}

			}

		} else if (arg.startsWith("--context_param")) {
			String keyValue = arg.substring(16);
			int index = -1;
			if (keyValue != null && (index = keyValue.indexOf('=')) > -1) {
				if (fatherPid == null) {
					context_param.put(keyValue.substring(0, index), replaceEscapeChars(keyValue.substring(index + 1)));
				} else { // the subjob won't escape the especial chars
					context_param.put(keyValue.substring(0, index), keyValue.substring(index + 1));
				}
			}
		} else if (arg.startsWith("--context_file")) {
			String keyValue = arg.substring(15);
			String filePath = new String(java.util.Base64.getDecoder().decode(keyValue));
			java.nio.file.Path contextFile = java.nio.file.Paths.get(filePath);
			try (java.io.BufferedReader reader = java.nio.file.Files.newBufferedReader(contextFile)) {
				String line;
				while ((line = reader.readLine()) != null) {
					int index = -1;
					if ((index = line.indexOf('=')) > -1) {
						if (line.startsWith("--context_param")) {
							if ("id_Password".equals(context_param.getContextType(line.substring(16, index)))) {
								context_param.put(line.substring(16, index),
										routines.system.PasswordEncryptUtil.decryptPassword(line.substring(index + 1)));
							} else {
								context_param.put(line.substring(16, index), line.substring(index + 1));
							}
						} else {// --context_type
							context_param.setContextType(line.substring(15, index), line.substring(index + 1));
						}
					}
				}
			} catch (java.io.IOException e) {
				System.err.println("Could not load the context file: " + filePath);
				e.printStackTrace();
			}
		} else if (arg.startsWith("--log4jLevel=")) {
			log4jLevel = arg.substring(13);
		} else if (arg.startsWith("--audit.enabled") && arg.contains("=")) {// for trunjob call
			final int equal = arg.indexOf('=');
			final String key = arg.substring("--".length(), equal);
			System.setProperty(key, arg.substring(equal + 1));
		}
	}

	private static final String NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY = "<TALEND_NULL>";

	private final String[][] escapeChars = { { "\\\\", "\\" }, { "\\n", "\n" }, { "\\'", "\'" }, { "\\r", "\r" },
			{ "\\f", "\f" }, { "\\b", "\b" }, { "\\t", "\t" } };

	private String replaceEscapeChars(String keyValue) {

		if (keyValue == null || ("").equals(keyValue.trim())) {
			return keyValue;
		}

		StringBuilder result = new StringBuilder();
		int currIndex = 0;
		while (currIndex < keyValue.length()) {
			int index = -1;
			// judege if the left string includes escape chars
			for (String[] strArray : escapeChars) {
				index = keyValue.indexOf(strArray[0], currIndex);
				if (index >= 0) {

					result.append(keyValue.substring(currIndex, index + strArray[0].length()).replace(strArray[0],
							strArray[1]));
					currIndex = index + strArray[0].length();
					break;
				}
			}
			// if the left string doesn't include escape chars, append the left into the
			// result
			if (index < 0) {
				result.append(keyValue.substring(currIndex));
				currIndex = currIndex + keyValue.length();
			}
		}

		return result.toString();
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public String getStatus() {
		return status;
	}

	ResumeUtil resumeUtil = null;
}
/************************************************************************************************
 * 100606 characters generated by Talend Cloud Data Fabric on the 11 January
 * 2024 at 11:00:38 AM SGT
 ************************************************************************************************/