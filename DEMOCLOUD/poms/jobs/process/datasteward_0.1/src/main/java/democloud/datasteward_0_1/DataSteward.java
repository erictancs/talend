
package democloud.datasteward_0_1;

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
 * Job: DataSteward Purpose: Demo<br>
 * Description: Receive validated records from Data Stewardship and append to DW
 * <br>
 * 
 * @author Tan, Eric
 * @version 8.0.1.20231222_1430-patch
 * @status
 */
public class DataSteward implements TalendJob {
	static {
		System.setProperty("TalendJob.log", "DataSteward.log");
	}

	private static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
			.getLogger(DataSteward.class);

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
	private final String jobName = "DataSteward";
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
			"_UXy2wILFEe6PMYwZwGxOMQ", "0.1");
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
					DataSteward.this.exception = e;
				}
			}
			if (!(e instanceof TalendException)) {
				try {
					for (java.lang.reflect.Method m : this.getClass().getEnclosingClass().getMethods()) {
						if (m.getName().compareTo(currentComponent + "_error") == 0) {
							m.invoke(DataSteward.this, new Object[] { e, currentComponent, globalMap });
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

	public void tDataStewardshipTaskInput_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDataStewardshipTaskInput_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tFileOutputDelimited_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDataStewardshipTaskInput_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tLogRow_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDataStewardshipTaskInput_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void talendJobLog_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		talendJobLog_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDataStewardshipTaskInput_1_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void talendJobLog_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public static class row2Struct implements routines.system.IPersistableRow<row2Struct> {
		final static byte[] commonByteArrayLock_DEMOCLOUD_DataSteward = new byte[0];
		static byte[] commonByteArray_DEMOCLOUD_DataSteward = new byte[0];

		public int Id;

		public int getId() {
			return this.Id;
		}

		public Boolean IdIsNullable() {
			return false;
		}

		public Boolean IdIsKey() {
			return false;
		}

		public Integer IdLength() {
			return null;
		}

		public Integer IdPrecision() {
			return null;
		}

		public String IdDefault() {

			return null;

		}

		public String IdComment() {

			return "integer";

		}

		public String IdPattern() {

			return "";

		}

		public String IdOriginalDbColumnName() {

			return "Id";

		}

		public String First_Name;

		public String getFirst_Name() {
			return this.First_Name;
		}

		public Boolean First_NameIsNullable() {
			return false;
		}

		public Boolean First_NameIsKey() {
			return false;
		}

		public Integer First_NameLength() {
			return null;
		}

		public Integer First_NamePrecision() {
			return null;
		}

		public String First_NameDefault() {

			return null;

		}

		public String First_NameComment() {

			return "text";

		}

		public String First_NamePattern() {

			return "";

		}

		public String First_NameOriginalDbColumnName() {

			return "First_Name";

		}

		public String Last_Name;

		public String getLast_Name() {
			return this.Last_Name;
		}

		public Boolean Last_NameIsNullable() {
			return false;
		}

		public Boolean Last_NameIsKey() {
			return false;
		}

		public Integer Last_NameLength() {
			return null;
		}

		public Integer Last_NamePrecision() {
			return null;
		}

		public String Last_NameDefault() {

			return null;

		}

		public String Last_NameComment() {

			return "text";

		}

		public String Last_NamePattern() {

			return "";

		}

		public String Last_NameOriginalDbColumnName() {

			return "Last_Name";

		}

		public String Gender;

		public String getGender() {
			return this.Gender;
		}

		public Boolean GenderIsNullable() {
			return false;
		}

		public Boolean GenderIsKey() {
			return false;
		}

		public Integer GenderLength() {
			return null;
		}

		public Integer GenderPrecision() {
			return null;
		}

		public String GenderDefault() {

			return null;

		}

		public String GenderComment() {

			return "text";

		}

		public String GenderPattern() {

			return "";

		}

		public String GenderOriginalDbColumnName() {

			return "Gender";

		}

		public String Age;

		public String getAge() {
			return this.Age;
		}

		public Boolean AgeIsNullable() {
			return false;
		}

		public Boolean AgeIsKey() {
			return false;
		}

		public Integer AgeLength() {
			return null;
		}

		public Integer AgePrecision() {
			return null;
		}

		public String AgeDefault() {

			return null;

		}

		public String AgeComment() {

			return "text";

		}

		public String AgePattern() {

			return "";

		}

		public String AgeOriginalDbColumnName() {

			return "Age";

		}

		public String Occupation;

		public String getOccupation() {
			return this.Occupation;
		}

		public Boolean OccupationIsNullable() {
			return false;
		}

		public Boolean OccupationIsKey() {
			return false;
		}

		public Integer OccupationLength() {
			return null;
		}

		public Integer OccupationPrecision() {
			return null;
		}

		public String OccupationDefault() {

			return null;

		}

		public String OccupationComment() {

			return "text";

		}

		public String OccupationPattern() {

			return "";

		}

		public String OccupationOriginalDbColumnName() {

			return "Occupation";

		}

		public String MaritalStatus;

		public String getMaritalStatus() {
			return this.MaritalStatus;
		}

		public Boolean MaritalStatusIsNullable() {
			return false;
		}

		public Boolean MaritalStatusIsKey() {
			return false;
		}

		public Integer MaritalStatusLength() {
			return null;
		}

		public Integer MaritalStatusPrecision() {
			return null;
		}

		public String MaritalStatusDefault() {

			return null;

		}

		public String MaritalStatusComment() {

			return "text";

		}

		public String MaritalStatusPattern() {

			return "";

		}

		public String MaritalStatusOriginalDbColumnName() {

			return "MaritalStatus";

		}

		public String Salary;

		public String getSalary() {
			return this.Salary;
		}

		public Boolean SalaryIsNullable() {
			return false;
		}

		public Boolean SalaryIsKey() {
			return false;
		}

		public Integer SalaryLength() {
			return null;
		}

		public Integer SalaryPrecision() {
			return null;
		}

		public String SalaryDefault() {

			return null;

		}

		public String SalaryComment() {

			return "text";

		}

		public String SalaryPattern() {

			return "";

		}

		public String SalaryOriginalDbColumnName() {

			return "Salary";

		}

		public String Address;

		public String getAddress() {
			return this.Address;
		}

		public Boolean AddressIsNullable() {
			return false;
		}

		public Boolean AddressIsKey() {
			return false;
		}

		public Integer AddressLength() {
			return null;
		}

		public Integer AddressPrecision() {
			return null;
		}

		public String AddressDefault() {

			return null;

		}

		public String AddressComment() {

			return "text";

		}

		public String AddressPattern() {

			return "";

		}

		public String AddressOriginalDbColumnName() {

			return "Address";

		}

		public String City;

		public String getCity() {
			return this.City;
		}

		public Boolean CityIsNullable() {
			return false;
		}

		public Boolean CityIsKey() {
			return false;
		}

		public Integer CityLength() {
			return null;
		}

		public Integer CityPrecision() {
			return null;
		}

		public String CityDefault() {

			return null;

		}

		public String CityComment() {

			return "text";

		}

		public String CityPattern() {

			return "";

		}

		public String CityOriginalDbColumnName() {

			return "City";

		}

		public String State;

		public String getState() {
			return this.State;
		}

		public Boolean StateIsNullable() {
			return false;
		}

		public Boolean StateIsKey() {
			return false;
		}

		public Integer StateLength() {
			return null;
		}

		public Integer StatePrecision() {
			return null;
		}

		public String StateDefault() {

			return null;

		}

		public String StateComment() {

			return "text";

		}

		public String StatePattern() {

			return "";

		}

		public String StateOriginalDbColumnName() {

			return "State";

		}

		public int Zip;

		public int getZip() {
			return this.Zip;
		}

		public Boolean ZipIsNullable() {
			return false;
		}

		public Boolean ZipIsKey() {
			return false;
		}

		public Integer ZipLength() {
			return null;
		}

		public Integer ZipPrecision() {
			return null;
		}

		public String ZipDefault() {

			return null;

		}

		public String ZipComment() {

			return "integer";

		}

		public String ZipPattern() {

			return "";

		}

		public String ZipOriginalDbColumnName() {

			return "Zip";

		}

		public String Phone;

		public String getPhone() {
			return this.Phone;
		}

		public Boolean PhoneIsNullable() {
			return false;
		}

		public Boolean PhoneIsKey() {
			return false;
		}

		public Integer PhoneLength() {
			return null;
		}

		public Integer PhonePrecision() {
			return null;
		}

		public String PhoneDefault() {

			return null;

		}

		public String PhoneComment() {

			return "text";

		}

		public String PhonePattern() {

			return "";

		}

		public String PhoneOriginalDbColumnName() {

			return "Phone";

		}

		public String Email;

		public String getEmail() {
			return this.Email;
		}

		public Boolean EmailIsNullable() {
			return false;
		}

		public Boolean EmailIsKey() {
			return false;
		}

		public Integer EmailLength() {
			return null;
		}

		public Integer EmailPrecision() {
			return null;
		}

		public String EmailDefault() {

			return null;

		}

		public String EmailComment() {

			return "text";

		}

		public String EmailPattern() {

			return "";

		}

		public String EmailOriginalDbColumnName() {

			return "Email";

		}

		public String TDS_ID;

		public String getTDS_ID() {
			return this.TDS_ID;
		}

		public Boolean TDS_IDIsNullable() {
			return false;
		}

		public Boolean TDS_IDIsKey() {
			return false;
		}

		public Integer TDS_IDLength() {
			return null;
		}

		public Integer TDS_IDPrecision() {
			return null;
		}

		public String TDS_IDDefault() {

			return null;

		}

		public String TDS_IDComment() {

			return "";

		}

		public String TDS_IDPattern() {

			return "";

		}

		public String TDS_IDOriginalDbColumnName() {

			return "TDS_ID";

		}

		public String TDS_STATE;

		public String getTDS_STATE() {
			return this.TDS_STATE;
		}

		public Boolean TDS_STATEIsNullable() {
			return false;
		}

		public Boolean TDS_STATEIsKey() {
			return false;
		}

		public Integer TDS_STATELength() {
			return null;
		}

		public Integer TDS_STATEPrecision() {
			return null;
		}

		public String TDS_STATEDefault() {

			return null;

		}

		public String TDS_STATEComment() {

			return "";

		}

		public String TDS_STATEPattern() {

			return "";

		}

		public String TDS_STATEOriginalDbColumnName() {

			return "TDS_STATE";

		}

		public String TDS_ASSIGNEE;

		public String getTDS_ASSIGNEE() {
			return this.TDS_ASSIGNEE;
		}

		public Boolean TDS_ASSIGNEEIsNullable() {
			return true;
		}

		public Boolean TDS_ASSIGNEEIsKey() {
			return false;
		}

		public Integer TDS_ASSIGNEELength() {
			return null;
		}

		public Integer TDS_ASSIGNEEPrecision() {
			return null;
		}

		public String TDS_ASSIGNEEDefault() {

			return null;

		}

		public String TDS_ASSIGNEEComment() {

			return "";

		}

		public String TDS_ASSIGNEEPattern() {

			return "";

		}

		public String TDS_ASSIGNEEOriginalDbColumnName() {

			return "TDS_ASSIGNEE";

		}

		public long TDS_CREATION;

		public long getTDS_CREATION() {
			return this.TDS_CREATION;
		}

		public Boolean TDS_CREATIONIsNullable() {
			return false;
		}

		public Boolean TDS_CREATIONIsKey() {
			return false;
		}

		public Integer TDS_CREATIONLength() {
			return null;
		}

		public Integer TDS_CREATIONPrecision() {
			return null;
		}

		public String TDS_CREATIONDefault() {

			return null;

		}

		public String TDS_CREATIONComment() {

			return "";

		}

		public String TDS_CREATIONPattern() {

			return "";

		}

		public String TDS_CREATIONOriginalDbColumnName() {

			return "TDS_CREATION";

		}

		public long TDS_LAST_UPDATE;

		public long getTDS_LAST_UPDATE() {
			return this.TDS_LAST_UPDATE;
		}

		public Boolean TDS_LAST_UPDATEIsNullable() {
			return false;
		}

		public Boolean TDS_LAST_UPDATEIsKey() {
			return false;
		}

		public Integer TDS_LAST_UPDATELength() {
			return null;
		}

		public Integer TDS_LAST_UPDATEPrecision() {
			return null;
		}

		public String TDS_LAST_UPDATEDefault() {

			return null;

		}

		public String TDS_LAST_UPDATEComment() {

			return "";

		}

		public String TDS_LAST_UPDATEPattern() {

			return "";

		}

		public String TDS_LAST_UPDATEOriginalDbColumnName() {

			return "TDS_LAST_UPDATE";

		}

		public String TDS_LAST_UPDATED_BY;

		public String getTDS_LAST_UPDATED_BY() {
			return this.TDS_LAST_UPDATED_BY;
		}

		public Boolean TDS_LAST_UPDATED_BYIsNullable() {
			return true;
		}

		public Boolean TDS_LAST_UPDATED_BYIsKey() {
			return false;
		}

		public Integer TDS_LAST_UPDATED_BYLength() {
			return null;
		}

		public Integer TDS_LAST_UPDATED_BYPrecision() {
			return null;
		}

		public String TDS_LAST_UPDATED_BYDefault() {

			return null;

		}

		public String TDS_LAST_UPDATED_BYComment() {

			return "";

		}

		public String TDS_LAST_UPDATED_BYPattern() {

			return "";

		}

		public String TDS_LAST_UPDATED_BYOriginalDbColumnName() {

			return "TDS_LAST_UPDATED_BY";

		}

		public Integer TDS_PRIORITY;

		public Integer getTDS_PRIORITY() {
			return this.TDS_PRIORITY;
		}

		public Boolean TDS_PRIORITYIsNullable() {
			return true;
		}

		public Boolean TDS_PRIORITYIsKey() {
			return false;
		}

		public Integer TDS_PRIORITYLength() {
			return null;
		}

		public Integer TDS_PRIORITYPrecision() {
			return null;
		}

		public String TDS_PRIORITYDefault() {

			return null;

		}

		public String TDS_PRIORITYComment() {

			return "";

		}

		public String TDS_PRIORITYPattern() {

			return "";

		}

		public String TDS_PRIORITYOriginalDbColumnName() {

			return "TDS_PRIORITY";

		}

		public String TDS_TAGS;

		public String getTDS_TAGS() {
			return this.TDS_TAGS;
		}

		public Boolean TDS_TAGSIsNullable() {
			return true;
		}

		public Boolean TDS_TAGSIsKey() {
			return false;
		}

		public Integer TDS_TAGSLength() {
			return null;
		}

		public Integer TDS_TAGSPrecision() {
			return null;
		}

		public String TDS_TAGSDefault() {

			return null;

		}

		public String TDS_TAGSComment() {

			return "";

		}

		public String TDS_TAGSPattern() {

			return "";

		}

		public String TDS_TAGSOriginalDbColumnName() {

			return "TDS_TAGS";

		}

		public Long TDS_DUE_DATE;

		public Long getTDS_DUE_DATE() {
			return this.TDS_DUE_DATE;
		}

		public Boolean TDS_DUE_DATEIsNullable() {
			return true;
		}

		public Boolean TDS_DUE_DATEIsKey() {
			return false;
		}

		public Integer TDS_DUE_DATELength() {
			return null;
		}

		public Integer TDS_DUE_DATEPrecision() {
			return null;
		}

		public String TDS_DUE_DATEDefault() {

			return null;

		}

		public String TDS_DUE_DATEComment() {

			return "";

		}

		public String TDS_DUE_DATEPattern() {

			return "";

		}

		public String TDS_DUE_DATEOriginalDbColumnName() {

			return "TDS_DUE_DATE";

		}

		public String TDS_EXTERNAL_ID;

		public String getTDS_EXTERNAL_ID() {
			return this.TDS_EXTERNAL_ID;
		}

		public Boolean TDS_EXTERNAL_IDIsNullable() {
			return true;
		}

		public Boolean TDS_EXTERNAL_IDIsKey() {
			return false;
		}

		public Integer TDS_EXTERNAL_IDLength() {
			return null;
		}

		public Integer TDS_EXTERNAL_IDPrecision() {
			return null;
		}

		public String TDS_EXTERNAL_IDDefault() {

			return null;

		}

		public String TDS_EXTERNAL_IDComment() {

			return "";

		}

		public String TDS_EXTERNAL_IDPattern() {

			return "";

		}

		public String TDS_EXTERNAL_IDOriginalDbColumnName() {

			return "TDS_EXTERNAL_ID";

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DEMOCLOUD_DataSteward.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_DataSteward.length == 0) {
						commonByteArray_DEMOCLOUD_DataSteward = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_DataSteward = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DEMOCLOUD_DataSteward, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_DataSteward, 0, length, utf8Charset);
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
				if (length > commonByteArray_DEMOCLOUD_DataSteward.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_DataSteward.length == 0) {
						commonByteArray_DEMOCLOUD_DataSteward = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_DataSteward = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DEMOCLOUD_DataSteward, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_DataSteward, 0, length, utf8Charset);
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

		private Integer readInteger(ObjectInputStream dis) throws IOException {
			Integer intReturn;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				intReturn = null;
			} else {
				intReturn = dis.readInt();
			}
			return intReturn;
		}

		private Integer readInteger(org.jboss.marshalling.Unmarshaller dis) throws IOException {
			Integer intReturn;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				intReturn = null;
			} else {
				intReturn = dis.readInt();
			}
			return intReturn;
		}

		private void writeInteger(Integer intNum, ObjectOutputStream dos) throws IOException {
			if (intNum == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeInt(intNum);
			}
		}

		private void writeInteger(Integer intNum, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (intNum == null) {
				marshaller.writeByte(-1);
			} else {
				marshaller.writeByte(0);
				marshaller.writeInt(intNum);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_DEMOCLOUD_DataSteward) {

				try {

					int length = 0;

					this.Id = dis.readInt();

					this.First_Name = readString(dis);

					this.Last_Name = readString(dis);

					this.Gender = readString(dis);

					this.Age = readString(dis);

					this.Occupation = readString(dis);

					this.MaritalStatus = readString(dis);

					this.Salary = readString(dis);

					this.Address = readString(dis);

					this.City = readString(dis);

					this.State = readString(dis);

					this.Zip = dis.readInt();

					this.Phone = readString(dis);

					this.Email = readString(dis);

					this.TDS_ID = readString(dis);

					this.TDS_STATE = readString(dis);

					this.TDS_ASSIGNEE = readString(dis);

					this.TDS_CREATION = dis.readLong();

					this.TDS_LAST_UPDATE = dis.readLong();

					this.TDS_LAST_UPDATED_BY = readString(dis);

					this.TDS_PRIORITY = readInteger(dis);

					this.TDS_TAGS = readString(dis);

					length = dis.readByte();
					if (length == -1) {
						this.TDS_DUE_DATE = null;
					} else {
						this.TDS_DUE_DATE = dis.readLong();
					}

					this.TDS_EXTERNAL_ID = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_DEMOCLOUD_DataSteward) {

				try {

					int length = 0;

					this.Id = dis.readInt();

					this.First_Name = readString(dis);

					this.Last_Name = readString(dis);

					this.Gender = readString(dis);

					this.Age = readString(dis);

					this.Occupation = readString(dis);

					this.MaritalStatus = readString(dis);

					this.Salary = readString(dis);

					this.Address = readString(dis);

					this.City = readString(dis);

					this.State = readString(dis);

					this.Zip = dis.readInt();

					this.Phone = readString(dis);

					this.Email = readString(dis);

					this.TDS_ID = readString(dis);

					this.TDS_STATE = readString(dis);

					this.TDS_ASSIGNEE = readString(dis);

					this.TDS_CREATION = dis.readLong();

					this.TDS_LAST_UPDATE = dis.readLong();

					this.TDS_LAST_UPDATED_BY = readString(dis);

					this.TDS_PRIORITY = readInteger(dis);

					this.TDS_TAGS = readString(dis);

					length = dis.readByte();
					if (length == -1) {
						this.TDS_DUE_DATE = null;
					} else {
						this.TDS_DUE_DATE = dis.readLong();
					}

					this.TDS_EXTERNAL_ID = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// int

				dos.writeInt(this.Id);

				// String

				writeString(this.First_Name, dos);

				// String

				writeString(this.Last_Name, dos);

				// String

				writeString(this.Gender, dos);

				// String

				writeString(this.Age, dos);

				// String

				writeString(this.Occupation, dos);

				// String

				writeString(this.MaritalStatus, dos);

				// String

				writeString(this.Salary, dos);

				// String

				writeString(this.Address, dos);

				// String

				writeString(this.City, dos);

				// String

				writeString(this.State, dos);

				// int

				dos.writeInt(this.Zip);

				// String

				writeString(this.Phone, dos);

				// String

				writeString(this.Email, dos);

				// String

				writeString(this.TDS_ID, dos);

				// String

				writeString(this.TDS_STATE, dos);

				// String

				writeString(this.TDS_ASSIGNEE, dos);

				// long

				dos.writeLong(this.TDS_CREATION);

				// long

				dos.writeLong(this.TDS_LAST_UPDATE);

				// String

				writeString(this.TDS_LAST_UPDATED_BY, dos);

				// Integer

				writeInteger(this.TDS_PRIORITY, dos);

				// String

				writeString(this.TDS_TAGS, dos);

				// Long

				if (this.TDS_DUE_DATE == null) {
					dos.writeByte(-1);
				} else {
					dos.writeByte(0);
					dos.writeLong(this.TDS_DUE_DATE);
				}

				// String

				writeString(this.TDS_EXTERNAL_ID, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// int

				dos.writeInt(this.Id);

				// String

				writeString(this.First_Name, dos);

				// String

				writeString(this.Last_Name, dos);

				// String

				writeString(this.Gender, dos);

				// String

				writeString(this.Age, dos);

				// String

				writeString(this.Occupation, dos);

				// String

				writeString(this.MaritalStatus, dos);

				// String

				writeString(this.Salary, dos);

				// String

				writeString(this.Address, dos);

				// String

				writeString(this.City, dos);

				// String

				writeString(this.State, dos);

				// int

				dos.writeInt(this.Zip);

				// String

				writeString(this.Phone, dos);

				// String

				writeString(this.Email, dos);

				// String

				writeString(this.TDS_ID, dos);

				// String

				writeString(this.TDS_STATE, dos);

				// String

				writeString(this.TDS_ASSIGNEE, dos);

				// long

				dos.writeLong(this.TDS_CREATION);

				// long

				dos.writeLong(this.TDS_LAST_UPDATE);

				// String

				writeString(this.TDS_LAST_UPDATED_BY, dos);

				// Integer

				writeInteger(this.TDS_PRIORITY, dos);

				// String

				writeString(this.TDS_TAGS, dos);

				// Long

				if (this.TDS_DUE_DATE == null) {
					dos.writeByte(-1);
				} else {
					dos.writeByte(0);
					dos.writeLong(this.TDS_DUE_DATE);
				}

				// String

				writeString(this.TDS_EXTERNAL_ID, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("Id=" + String.valueOf(Id));
			sb.append(",First_Name=" + First_Name);
			sb.append(",Last_Name=" + Last_Name);
			sb.append(",Gender=" + Gender);
			sb.append(",Age=" + Age);
			sb.append(",Occupation=" + Occupation);
			sb.append(",MaritalStatus=" + MaritalStatus);
			sb.append(",Salary=" + Salary);
			sb.append(",Address=" + Address);
			sb.append(",City=" + City);
			sb.append(",State=" + State);
			sb.append(",Zip=" + String.valueOf(Zip));
			sb.append(",Phone=" + Phone);
			sb.append(",Email=" + Email);
			sb.append(",TDS_ID=" + TDS_ID);
			sb.append(",TDS_STATE=" + TDS_STATE);
			sb.append(",TDS_ASSIGNEE=" + TDS_ASSIGNEE);
			sb.append(",TDS_CREATION=" + String.valueOf(TDS_CREATION));
			sb.append(",TDS_LAST_UPDATE=" + String.valueOf(TDS_LAST_UPDATE));
			sb.append(",TDS_LAST_UPDATED_BY=" + TDS_LAST_UPDATED_BY);
			sb.append(",TDS_PRIORITY=" + String.valueOf(TDS_PRIORITY));
			sb.append(",TDS_TAGS=" + TDS_TAGS);
			sb.append(",TDS_DUE_DATE=" + String.valueOf(TDS_DUE_DATE));
			sb.append(",TDS_EXTERNAL_ID=" + TDS_EXTERNAL_ID);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			sb.append(Id);

			sb.append("|");

			if (First_Name == null) {
				sb.append("<null>");
			} else {
				sb.append(First_Name);
			}

			sb.append("|");

			if (Last_Name == null) {
				sb.append("<null>");
			} else {
				sb.append(Last_Name);
			}

			sb.append("|");

			if (Gender == null) {
				sb.append("<null>");
			} else {
				sb.append(Gender);
			}

			sb.append("|");

			if (Age == null) {
				sb.append("<null>");
			} else {
				sb.append(Age);
			}

			sb.append("|");

			if (Occupation == null) {
				sb.append("<null>");
			} else {
				sb.append(Occupation);
			}

			sb.append("|");

			if (MaritalStatus == null) {
				sb.append("<null>");
			} else {
				sb.append(MaritalStatus);
			}

			sb.append("|");

			if (Salary == null) {
				sb.append("<null>");
			} else {
				sb.append(Salary);
			}

			sb.append("|");

			if (Address == null) {
				sb.append("<null>");
			} else {
				sb.append(Address);
			}

			sb.append("|");

			if (City == null) {
				sb.append("<null>");
			} else {
				sb.append(City);
			}

			sb.append("|");

			if (State == null) {
				sb.append("<null>");
			} else {
				sb.append(State);
			}

			sb.append("|");

			sb.append(Zip);

			sb.append("|");

			if (Phone == null) {
				sb.append("<null>");
			} else {
				sb.append(Phone);
			}

			sb.append("|");

			if (Email == null) {
				sb.append("<null>");
			} else {
				sb.append(Email);
			}

			sb.append("|");

			if (TDS_ID == null) {
				sb.append("<null>");
			} else {
				sb.append(TDS_ID);
			}

			sb.append("|");

			if (TDS_STATE == null) {
				sb.append("<null>");
			} else {
				sb.append(TDS_STATE);
			}

			sb.append("|");

			if (TDS_ASSIGNEE == null) {
				sb.append("<null>");
			} else {
				sb.append(TDS_ASSIGNEE);
			}

			sb.append("|");

			sb.append(TDS_CREATION);

			sb.append("|");

			sb.append(TDS_LAST_UPDATE);

			sb.append("|");

			if (TDS_LAST_UPDATED_BY == null) {
				sb.append("<null>");
			} else {
				sb.append(TDS_LAST_UPDATED_BY);
			}

			sb.append("|");

			if (TDS_PRIORITY == null) {
				sb.append("<null>");
			} else {
				sb.append(TDS_PRIORITY);
			}

			sb.append("|");

			if (TDS_TAGS == null) {
				sb.append("<null>");
			} else {
				sb.append(TDS_TAGS);
			}

			sb.append("|");

			if (TDS_DUE_DATE == null) {
				sb.append("<null>");
			} else {
				sb.append(TDS_DUE_DATE);
			}

			sb.append("|");

			if (TDS_EXTERNAL_ID == null) {
				sb.append("<null>");
			} else {
				sb.append(TDS_EXTERNAL_ID);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row2Struct other) {

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

	public static class row1Struct implements routines.system.IPersistableRow<row1Struct> {
		final static byte[] commonByteArrayLock_DEMOCLOUD_DataSteward = new byte[0];
		static byte[] commonByteArray_DEMOCLOUD_DataSteward = new byte[0];

		public int Id;

		public int getId() {
			return this.Id;
		}

		public Boolean IdIsNullable() {
			return false;
		}

		public Boolean IdIsKey() {
			return false;
		}

		public Integer IdLength() {
			return null;
		}

		public Integer IdPrecision() {
			return null;
		}

		public String IdDefault() {

			return null;

		}

		public String IdComment() {

			return "integer";

		}

		public String IdPattern() {

			return "";

		}

		public String IdOriginalDbColumnName() {

			return "Id";

		}

		public String First_Name;

		public String getFirst_Name() {
			return this.First_Name;
		}

		public Boolean First_NameIsNullable() {
			return false;
		}

		public Boolean First_NameIsKey() {
			return false;
		}

		public Integer First_NameLength() {
			return null;
		}

		public Integer First_NamePrecision() {
			return null;
		}

		public String First_NameDefault() {

			return null;

		}

		public String First_NameComment() {

			return "text";

		}

		public String First_NamePattern() {

			return "";

		}

		public String First_NameOriginalDbColumnName() {

			return "First_Name";

		}

		public String Last_Name;

		public String getLast_Name() {
			return this.Last_Name;
		}

		public Boolean Last_NameIsNullable() {
			return false;
		}

		public Boolean Last_NameIsKey() {
			return false;
		}

		public Integer Last_NameLength() {
			return null;
		}

		public Integer Last_NamePrecision() {
			return null;
		}

		public String Last_NameDefault() {

			return null;

		}

		public String Last_NameComment() {

			return "text";

		}

		public String Last_NamePattern() {

			return "";

		}

		public String Last_NameOriginalDbColumnName() {

			return "Last_Name";

		}

		public String Gender;

		public String getGender() {
			return this.Gender;
		}

		public Boolean GenderIsNullable() {
			return false;
		}

		public Boolean GenderIsKey() {
			return false;
		}

		public Integer GenderLength() {
			return null;
		}

		public Integer GenderPrecision() {
			return null;
		}

		public String GenderDefault() {

			return null;

		}

		public String GenderComment() {

			return "text";

		}

		public String GenderPattern() {

			return "";

		}

		public String GenderOriginalDbColumnName() {

			return "Gender";

		}

		public String Age;

		public String getAge() {
			return this.Age;
		}

		public Boolean AgeIsNullable() {
			return false;
		}

		public Boolean AgeIsKey() {
			return false;
		}

		public Integer AgeLength() {
			return null;
		}

		public Integer AgePrecision() {
			return null;
		}

		public String AgeDefault() {

			return null;

		}

		public String AgeComment() {

			return "text";

		}

		public String AgePattern() {

			return "";

		}

		public String AgeOriginalDbColumnName() {

			return "Age";

		}

		public String Occupation;

		public String getOccupation() {
			return this.Occupation;
		}

		public Boolean OccupationIsNullable() {
			return false;
		}

		public Boolean OccupationIsKey() {
			return false;
		}

		public Integer OccupationLength() {
			return null;
		}

		public Integer OccupationPrecision() {
			return null;
		}

		public String OccupationDefault() {

			return null;

		}

		public String OccupationComment() {

			return "text";

		}

		public String OccupationPattern() {

			return "";

		}

		public String OccupationOriginalDbColumnName() {

			return "Occupation";

		}

		public String MaritalStatus;

		public String getMaritalStatus() {
			return this.MaritalStatus;
		}

		public Boolean MaritalStatusIsNullable() {
			return false;
		}

		public Boolean MaritalStatusIsKey() {
			return false;
		}

		public Integer MaritalStatusLength() {
			return null;
		}

		public Integer MaritalStatusPrecision() {
			return null;
		}

		public String MaritalStatusDefault() {

			return null;

		}

		public String MaritalStatusComment() {

			return "text";

		}

		public String MaritalStatusPattern() {

			return "";

		}

		public String MaritalStatusOriginalDbColumnName() {

			return "MaritalStatus";

		}

		public String Salary;

		public String getSalary() {
			return this.Salary;
		}

		public Boolean SalaryIsNullable() {
			return false;
		}

		public Boolean SalaryIsKey() {
			return false;
		}

		public Integer SalaryLength() {
			return null;
		}

		public Integer SalaryPrecision() {
			return null;
		}

		public String SalaryDefault() {

			return null;

		}

		public String SalaryComment() {

			return "text";

		}

		public String SalaryPattern() {

			return "";

		}

		public String SalaryOriginalDbColumnName() {

			return "Salary";

		}

		public String Address;

		public String getAddress() {
			return this.Address;
		}

		public Boolean AddressIsNullable() {
			return false;
		}

		public Boolean AddressIsKey() {
			return false;
		}

		public Integer AddressLength() {
			return null;
		}

		public Integer AddressPrecision() {
			return null;
		}

		public String AddressDefault() {

			return null;

		}

		public String AddressComment() {

			return "text";

		}

		public String AddressPattern() {

			return "";

		}

		public String AddressOriginalDbColumnName() {

			return "Address";

		}

		public String City;

		public String getCity() {
			return this.City;
		}

		public Boolean CityIsNullable() {
			return false;
		}

		public Boolean CityIsKey() {
			return false;
		}

		public Integer CityLength() {
			return null;
		}

		public Integer CityPrecision() {
			return null;
		}

		public String CityDefault() {

			return null;

		}

		public String CityComment() {

			return "text";

		}

		public String CityPattern() {

			return "";

		}

		public String CityOriginalDbColumnName() {

			return "City";

		}

		public String State;

		public String getState() {
			return this.State;
		}

		public Boolean StateIsNullable() {
			return false;
		}

		public Boolean StateIsKey() {
			return false;
		}

		public Integer StateLength() {
			return null;
		}

		public Integer StatePrecision() {
			return null;
		}

		public String StateDefault() {

			return null;

		}

		public String StateComment() {

			return "text";

		}

		public String StatePattern() {

			return "";

		}

		public String StateOriginalDbColumnName() {

			return "State";

		}

		public int Zip;

		public int getZip() {
			return this.Zip;
		}

		public Boolean ZipIsNullable() {
			return false;
		}

		public Boolean ZipIsKey() {
			return false;
		}

		public Integer ZipLength() {
			return null;
		}

		public Integer ZipPrecision() {
			return null;
		}

		public String ZipDefault() {

			return null;

		}

		public String ZipComment() {

			return "integer";

		}

		public String ZipPattern() {

			return "";

		}

		public String ZipOriginalDbColumnName() {

			return "Zip";

		}

		public String Phone;

		public String getPhone() {
			return this.Phone;
		}

		public Boolean PhoneIsNullable() {
			return false;
		}

		public Boolean PhoneIsKey() {
			return false;
		}

		public Integer PhoneLength() {
			return null;
		}

		public Integer PhonePrecision() {
			return null;
		}

		public String PhoneDefault() {

			return null;

		}

		public String PhoneComment() {

			return "text";

		}

		public String PhonePattern() {

			return "";

		}

		public String PhoneOriginalDbColumnName() {

			return "Phone";

		}

		public String Email;

		public String getEmail() {
			return this.Email;
		}

		public Boolean EmailIsNullable() {
			return false;
		}

		public Boolean EmailIsKey() {
			return false;
		}

		public Integer EmailLength() {
			return null;
		}

		public Integer EmailPrecision() {
			return null;
		}

		public String EmailDefault() {

			return null;

		}

		public String EmailComment() {

			return "text";

		}

		public String EmailPattern() {

			return "";

		}

		public String EmailOriginalDbColumnName() {

			return "Email";

		}

		public String TDS_ID;

		public String getTDS_ID() {
			return this.TDS_ID;
		}

		public Boolean TDS_IDIsNullable() {
			return false;
		}

		public Boolean TDS_IDIsKey() {
			return false;
		}

		public Integer TDS_IDLength() {
			return null;
		}

		public Integer TDS_IDPrecision() {
			return null;
		}

		public String TDS_IDDefault() {

			return null;

		}

		public String TDS_IDComment() {

			return "";

		}

		public String TDS_IDPattern() {

			return "";

		}

		public String TDS_IDOriginalDbColumnName() {

			return "TDS_ID";

		}

		public String TDS_STATE;

		public String getTDS_STATE() {
			return this.TDS_STATE;
		}

		public Boolean TDS_STATEIsNullable() {
			return false;
		}

		public Boolean TDS_STATEIsKey() {
			return false;
		}

		public Integer TDS_STATELength() {
			return null;
		}

		public Integer TDS_STATEPrecision() {
			return null;
		}

		public String TDS_STATEDefault() {

			return null;

		}

		public String TDS_STATEComment() {

			return "";

		}

		public String TDS_STATEPattern() {

			return "";

		}

		public String TDS_STATEOriginalDbColumnName() {

			return "TDS_STATE";

		}

		public String TDS_ASSIGNEE;

		public String getTDS_ASSIGNEE() {
			return this.TDS_ASSIGNEE;
		}

		public Boolean TDS_ASSIGNEEIsNullable() {
			return true;
		}

		public Boolean TDS_ASSIGNEEIsKey() {
			return false;
		}

		public Integer TDS_ASSIGNEELength() {
			return null;
		}

		public Integer TDS_ASSIGNEEPrecision() {
			return null;
		}

		public String TDS_ASSIGNEEDefault() {

			return null;

		}

		public String TDS_ASSIGNEEComment() {

			return "";

		}

		public String TDS_ASSIGNEEPattern() {

			return "";

		}

		public String TDS_ASSIGNEEOriginalDbColumnName() {

			return "TDS_ASSIGNEE";

		}

		public long TDS_CREATION;

		public long getTDS_CREATION() {
			return this.TDS_CREATION;
		}

		public Boolean TDS_CREATIONIsNullable() {
			return false;
		}

		public Boolean TDS_CREATIONIsKey() {
			return false;
		}

		public Integer TDS_CREATIONLength() {
			return null;
		}

		public Integer TDS_CREATIONPrecision() {
			return null;
		}

		public String TDS_CREATIONDefault() {

			return null;

		}

		public String TDS_CREATIONComment() {

			return "";

		}

		public String TDS_CREATIONPattern() {

			return "";

		}

		public String TDS_CREATIONOriginalDbColumnName() {

			return "TDS_CREATION";

		}

		public long TDS_LAST_UPDATE;

		public long getTDS_LAST_UPDATE() {
			return this.TDS_LAST_UPDATE;
		}

		public Boolean TDS_LAST_UPDATEIsNullable() {
			return false;
		}

		public Boolean TDS_LAST_UPDATEIsKey() {
			return false;
		}

		public Integer TDS_LAST_UPDATELength() {
			return null;
		}

		public Integer TDS_LAST_UPDATEPrecision() {
			return null;
		}

		public String TDS_LAST_UPDATEDefault() {

			return null;

		}

		public String TDS_LAST_UPDATEComment() {

			return "";

		}

		public String TDS_LAST_UPDATEPattern() {

			return "";

		}

		public String TDS_LAST_UPDATEOriginalDbColumnName() {

			return "TDS_LAST_UPDATE";

		}

		public String TDS_LAST_UPDATED_BY;

		public String getTDS_LAST_UPDATED_BY() {
			return this.TDS_LAST_UPDATED_BY;
		}

		public Boolean TDS_LAST_UPDATED_BYIsNullable() {
			return true;
		}

		public Boolean TDS_LAST_UPDATED_BYIsKey() {
			return false;
		}

		public Integer TDS_LAST_UPDATED_BYLength() {
			return null;
		}

		public Integer TDS_LAST_UPDATED_BYPrecision() {
			return null;
		}

		public String TDS_LAST_UPDATED_BYDefault() {

			return null;

		}

		public String TDS_LAST_UPDATED_BYComment() {

			return "";

		}

		public String TDS_LAST_UPDATED_BYPattern() {

			return "";

		}

		public String TDS_LAST_UPDATED_BYOriginalDbColumnName() {

			return "TDS_LAST_UPDATED_BY";

		}

		public Integer TDS_PRIORITY;

		public Integer getTDS_PRIORITY() {
			return this.TDS_PRIORITY;
		}

		public Boolean TDS_PRIORITYIsNullable() {
			return true;
		}

		public Boolean TDS_PRIORITYIsKey() {
			return false;
		}

		public Integer TDS_PRIORITYLength() {
			return null;
		}

		public Integer TDS_PRIORITYPrecision() {
			return null;
		}

		public String TDS_PRIORITYDefault() {

			return null;

		}

		public String TDS_PRIORITYComment() {

			return "";

		}

		public String TDS_PRIORITYPattern() {

			return "";

		}

		public String TDS_PRIORITYOriginalDbColumnName() {

			return "TDS_PRIORITY";

		}

		public String TDS_TAGS;

		public String getTDS_TAGS() {
			return this.TDS_TAGS;
		}

		public Boolean TDS_TAGSIsNullable() {
			return true;
		}

		public Boolean TDS_TAGSIsKey() {
			return false;
		}

		public Integer TDS_TAGSLength() {
			return null;
		}

		public Integer TDS_TAGSPrecision() {
			return null;
		}

		public String TDS_TAGSDefault() {

			return null;

		}

		public String TDS_TAGSComment() {

			return "";

		}

		public String TDS_TAGSPattern() {

			return "";

		}

		public String TDS_TAGSOriginalDbColumnName() {

			return "TDS_TAGS";

		}

		public Long TDS_DUE_DATE;

		public Long getTDS_DUE_DATE() {
			return this.TDS_DUE_DATE;
		}

		public Boolean TDS_DUE_DATEIsNullable() {
			return true;
		}

		public Boolean TDS_DUE_DATEIsKey() {
			return false;
		}

		public Integer TDS_DUE_DATELength() {
			return null;
		}

		public Integer TDS_DUE_DATEPrecision() {
			return null;
		}

		public String TDS_DUE_DATEDefault() {

			return null;

		}

		public String TDS_DUE_DATEComment() {

			return "";

		}

		public String TDS_DUE_DATEPattern() {

			return "";

		}

		public String TDS_DUE_DATEOriginalDbColumnName() {

			return "TDS_DUE_DATE";

		}

		public String TDS_EXTERNAL_ID;

		public String getTDS_EXTERNAL_ID() {
			return this.TDS_EXTERNAL_ID;
		}

		public Boolean TDS_EXTERNAL_IDIsNullable() {
			return true;
		}

		public Boolean TDS_EXTERNAL_IDIsKey() {
			return false;
		}

		public Integer TDS_EXTERNAL_IDLength() {
			return null;
		}

		public Integer TDS_EXTERNAL_IDPrecision() {
			return null;
		}

		public String TDS_EXTERNAL_IDDefault() {

			return null;

		}

		public String TDS_EXTERNAL_IDComment() {

			return "";

		}

		public String TDS_EXTERNAL_IDPattern() {

			return "";

		}

		public String TDS_EXTERNAL_IDOriginalDbColumnName() {

			return "TDS_EXTERNAL_ID";

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DEMOCLOUD_DataSteward.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_DataSteward.length == 0) {
						commonByteArray_DEMOCLOUD_DataSteward = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_DataSteward = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DEMOCLOUD_DataSteward, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_DataSteward, 0, length, utf8Charset);
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
				if (length > commonByteArray_DEMOCLOUD_DataSteward.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_DataSteward.length == 0) {
						commonByteArray_DEMOCLOUD_DataSteward = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_DataSteward = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DEMOCLOUD_DataSteward, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_DataSteward, 0, length, utf8Charset);
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

		private Integer readInteger(ObjectInputStream dis) throws IOException {
			Integer intReturn;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				intReturn = null;
			} else {
				intReturn = dis.readInt();
			}
			return intReturn;
		}

		private Integer readInteger(org.jboss.marshalling.Unmarshaller dis) throws IOException {
			Integer intReturn;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				intReturn = null;
			} else {
				intReturn = dis.readInt();
			}
			return intReturn;
		}

		private void writeInteger(Integer intNum, ObjectOutputStream dos) throws IOException {
			if (intNum == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeInt(intNum);
			}
		}

		private void writeInteger(Integer intNum, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (intNum == null) {
				marshaller.writeByte(-1);
			} else {
				marshaller.writeByte(0);
				marshaller.writeInt(intNum);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_DEMOCLOUD_DataSteward) {

				try {

					int length = 0;

					this.Id = dis.readInt();

					this.First_Name = readString(dis);

					this.Last_Name = readString(dis);

					this.Gender = readString(dis);

					this.Age = readString(dis);

					this.Occupation = readString(dis);

					this.MaritalStatus = readString(dis);

					this.Salary = readString(dis);

					this.Address = readString(dis);

					this.City = readString(dis);

					this.State = readString(dis);

					this.Zip = dis.readInt();

					this.Phone = readString(dis);

					this.Email = readString(dis);

					this.TDS_ID = readString(dis);

					this.TDS_STATE = readString(dis);

					this.TDS_ASSIGNEE = readString(dis);

					this.TDS_CREATION = dis.readLong();

					this.TDS_LAST_UPDATE = dis.readLong();

					this.TDS_LAST_UPDATED_BY = readString(dis);

					this.TDS_PRIORITY = readInteger(dis);

					this.TDS_TAGS = readString(dis);

					length = dis.readByte();
					if (length == -1) {
						this.TDS_DUE_DATE = null;
					} else {
						this.TDS_DUE_DATE = dis.readLong();
					}

					this.TDS_EXTERNAL_ID = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_DEMOCLOUD_DataSteward) {

				try {

					int length = 0;

					this.Id = dis.readInt();

					this.First_Name = readString(dis);

					this.Last_Name = readString(dis);

					this.Gender = readString(dis);

					this.Age = readString(dis);

					this.Occupation = readString(dis);

					this.MaritalStatus = readString(dis);

					this.Salary = readString(dis);

					this.Address = readString(dis);

					this.City = readString(dis);

					this.State = readString(dis);

					this.Zip = dis.readInt();

					this.Phone = readString(dis);

					this.Email = readString(dis);

					this.TDS_ID = readString(dis);

					this.TDS_STATE = readString(dis);

					this.TDS_ASSIGNEE = readString(dis);

					this.TDS_CREATION = dis.readLong();

					this.TDS_LAST_UPDATE = dis.readLong();

					this.TDS_LAST_UPDATED_BY = readString(dis);

					this.TDS_PRIORITY = readInteger(dis);

					this.TDS_TAGS = readString(dis);

					length = dis.readByte();
					if (length == -1) {
						this.TDS_DUE_DATE = null;
					} else {
						this.TDS_DUE_DATE = dis.readLong();
					}

					this.TDS_EXTERNAL_ID = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// int

				dos.writeInt(this.Id);

				// String

				writeString(this.First_Name, dos);

				// String

				writeString(this.Last_Name, dos);

				// String

				writeString(this.Gender, dos);

				// String

				writeString(this.Age, dos);

				// String

				writeString(this.Occupation, dos);

				// String

				writeString(this.MaritalStatus, dos);

				// String

				writeString(this.Salary, dos);

				// String

				writeString(this.Address, dos);

				// String

				writeString(this.City, dos);

				// String

				writeString(this.State, dos);

				// int

				dos.writeInt(this.Zip);

				// String

				writeString(this.Phone, dos);

				// String

				writeString(this.Email, dos);

				// String

				writeString(this.TDS_ID, dos);

				// String

				writeString(this.TDS_STATE, dos);

				// String

				writeString(this.TDS_ASSIGNEE, dos);

				// long

				dos.writeLong(this.TDS_CREATION);

				// long

				dos.writeLong(this.TDS_LAST_UPDATE);

				// String

				writeString(this.TDS_LAST_UPDATED_BY, dos);

				// Integer

				writeInteger(this.TDS_PRIORITY, dos);

				// String

				writeString(this.TDS_TAGS, dos);

				// Long

				if (this.TDS_DUE_DATE == null) {
					dos.writeByte(-1);
				} else {
					dos.writeByte(0);
					dos.writeLong(this.TDS_DUE_DATE);
				}

				// String

				writeString(this.TDS_EXTERNAL_ID, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// int

				dos.writeInt(this.Id);

				// String

				writeString(this.First_Name, dos);

				// String

				writeString(this.Last_Name, dos);

				// String

				writeString(this.Gender, dos);

				// String

				writeString(this.Age, dos);

				// String

				writeString(this.Occupation, dos);

				// String

				writeString(this.MaritalStatus, dos);

				// String

				writeString(this.Salary, dos);

				// String

				writeString(this.Address, dos);

				// String

				writeString(this.City, dos);

				// String

				writeString(this.State, dos);

				// int

				dos.writeInt(this.Zip);

				// String

				writeString(this.Phone, dos);

				// String

				writeString(this.Email, dos);

				// String

				writeString(this.TDS_ID, dos);

				// String

				writeString(this.TDS_STATE, dos);

				// String

				writeString(this.TDS_ASSIGNEE, dos);

				// long

				dos.writeLong(this.TDS_CREATION);

				// long

				dos.writeLong(this.TDS_LAST_UPDATE);

				// String

				writeString(this.TDS_LAST_UPDATED_BY, dos);

				// Integer

				writeInteger(this.TDS_PRIORITY, dos);

				// String

				writeString(this.TDS_TAGS, dos);

				// Long

				if (this.TDS_DUE_DATE == null) {
					dos.writeByte(-1);
				} else {
					dos.writeByte(0);
					dos.writeLong(this.TDS_DUE_DATE);
				}

				// String

				writeString(this.TDS_EXTERNAL_ID, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("Id=" + String.valueOf(Id));
			sb.append(",First_Name=" + First_Name);
			sb.append(",Last_Name=" + Last_Name);
			sb.append(",Gender=" + Gender);
			sb.append(",Age=" + Age);
			sb.append(",Occupation=" + Occupation);
			sb.append(",MaritalStatus=" + MaritalStatus);
			sb.append(",Salary=" + Salary);
			sb.append(",Address=" + Address);
			sb.append(",City=" + City);
			sb.append(",State=" + State);
			sb.append(",Zip=" + String.valueOf(Zip));
			sb.append(",Phone=" + Phone);
			sb.append(",Email=" + Email);
			sb.append(",TDS_ID=" + TDS_ID);
			sb.append(",TDS_STATE=" + TDS_STATE);
			sb.append(",TDS_ASSIGNEE=" + TDS_ASSIGNEE);
			sb.append(",TDS_CREATION=" + String.valueOf(TDS_CREATION));
			sb.append(",TDS_LAST_UPDATE=" + String.valueOf(TDS_LAST_UPDATE));
			sb.append(",TDS_LAST_UPDATED_BY=" + TDS_LAST_UPDATED_BY);
			sb.append(",TDS_PRIORITY=" + String.valueOf(TDS_PRIORITY));
			sb.append(",TDS_TAGS=" + TDS_TAGS);
			sb.append(",TDS_DUE_DATE=" + String.valueOf(TDS_DUE_DATE));
			sb.append(",TDS_EXTERNAL_ID=" + TDS_EXTERNAL_ID);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			sb.append(Id);

			sb.append("|");

			if (First_Name == null) {
				sb.append("<null>");
			} else {
				sb.append(First_Name);
			}

			sb.append("|");

			if (Last_Name == null) {
				sb.append("<null>");
			} else {
				sb.append(Last_Name);
			}

			sb.append("|");

			if (Gender == null) {
				sb.append("<null>");
			} else {
				sb.append(Gender);
			}

			sb.append("|");

			if (Age == null) {
				sb.append("<null>");
			} else {
				sb.append(Age);
			}

			sb.append("|");

			if (Occupation == null) {
				sb.append("<null>");
			} else {
				sb.append(Occupation);
			}

			sb.append("|");

			if (MaritalStatus == null) {
				sb.append("<null>");
			} else {
				sb.append(MaritalStatus);
			}

			sb.append("|");

			if (Salary == null) {
				sb.append("<null>");
			} else {
				sb.append(Salary);
			}

			sb.append("|");

			if (Address == null) {
				sb.append("<null>");
			} else {
				sb.append(Address);
			}

			sb.append("|");

			if (City == null) {
				sb.append("<null>");
			} else {
				sb.append(City);
			}

			sb.append("|");

			if (State == null) {
				sb.append("<null>");
			} else {
				sb.append(State);
			}

			sb.append("|");

			sb.append(Zip);

			sb.append("|");

			if (Phone == null) {
				sb.append("<null>");
			} else {
				sb.append(Phone);
			}

			sb.append("|");

			if (Email == null) {
				sb.append("<null>");
			} else {
				sb.append(Email);
			}

			sb.append("|");

			if (TDS_ID == null) {
				sb.append("<null>");
			} else {
				sb.append(TDS_ID);
			}

			sb.append("|");

			if (TDS_STATE == null) {
				sb.append("<null>");
			} else {
				sb.append(TDS_STATE);
			}

			sb.append("|");

			if (TDS_ASSIGNEE == null) {
				sb.append("<null>");
			} else {
				sb.append(TDS_ASSIGNEE);
			}

			sb.append("|");

			sb.append(TDS_CREATION);

			sb.append("|");

			sb.append(TDS_LAST_UPDATE);

			sb.append("|");

			if (TDS_LAST_UPDATED_BY == null) {
				sb.append("<null>");
			} else {
				sb.append(TDS_LAST_UPDATED_BY);
			}

			sb.append("|");

			if (TDS_PRIORITY == null) {
				sb.append("<null>");
			} else {
				sb.append(TDS_PRIORITY);
			}

			sb.append("|");

			if (TDS_TAGS == null) {
				sb.append("<null>");
			} else {
				sb.append(TDS_TAGS);
			}

			sb.append("|");

			if (TDS_DUE_DATE == null) {
				sb.append("<null>");
			} else {
				sb.append(TDS_DUE_DATE);
			}

			sb.append("|");

			if (TDS_EXTERNAL_ID == null) {
				sb.append("<null>");
			} else {
				sb.append(TDS_EXTERNAL_ID);
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

	public void tDataStewardshipTaskInput_1Process(final java.util.Map<String, Object> globalMap)
			throws TalendException {
		globalMap.put("tDataStewardshipTaskInput_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdcInfo.forEach(org.slf4j.MDC::put);
		org.slf4j.MDC.put("_subJobName", "tDataStewardshipTaskInput_1");
		org.slf4j.MDC.put("_subJobPid", "8Sv9uQ_" + subJobPidCounter.getAndIncrement());

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
				row1Struct row2 = row1;

				/**
				 * [tLogRow_1 begin ] start
				 */

				ok_Hash.put("tLogRow_1", false);
				start_Hash.put("tLogRow_1", System.currentTimeMillis());

				currentComponent = "tLogRow_1";

				cLabel = "OutputConsole";

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row2");

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
					talendJobLog.addCM("tLogRow_1", "OutputConsole", "tLogRow");
					talendJobLogProcess(globalMap);
				}

				///////////////////////

				class Util_tLogRow_1 {

					String[] des_top = { ".", ".", "-", "+" };

					String[] des_head = { "|=", "=|", "-", "+" };

					String[] des_bottom = { "'", "'", "-", "+" };

					String name = "";

					java.util.List<String[]> list = new java.util.ArrayList<String[]>();

					int[] colLengths = new int[24];

					public void addRow(String[] row) {

						for (int i = 0; i < 24; i++) {
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
						for (k = 0; k < (totals + 23 - name.length()) / 2; k++) {
							sb.append(' ');
						}
						sb.append(name);
						for (int i = 0; i < totals + 23 - name.length() - k; i++) {
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

							sbformat.append("|%5$-");
							sbformat.append(colLengths[4]);
							sbformat.append("s");

							sbformat.append("|%6$-");
							sbformat.append(colLengths[5]);
							sbformat.append("s");

							sbformat.append("|%7$-");
							sbformat.append(colLengths[6]);
							sbformat.append("s");

							sbformat.append("|%8$-");
							sbformat.append(colLengths[7]);
							sbformat.append("s");

							sbformat.append("|%9$-");
							sbformat.append(colLengths[8]);
							sbformat.append("s");

							sbformat.append("|%10$-");
							sbformat.append(colLengths[9]);
							sbformat.append("s");

							sbformat.append("|%11$-");
							sbformat.append(colLengths[10]);
							sbformat.append("s");

							sbformat.append("|%12$-");
							sbformat.append(colLengths[11]);
							sbformat.append("s");

							sbformat.append("|%13$-");
							sbformat.append(colLengths[12]);
							sbformat.append("s");

							sbformat.append("|%14$-");
							sbformat.append(colLengths[13]);
							sbformat.append("s");

							sbformat.append("|%15$-");
							sbformat.append(colLengths[14]);
							sbformat.append("s");

							sbformat.append("|%16$-");
							sbformat.append(colLengths[15]);
							sbformat.append("s");

							sbformat.append("|%17$-");
							sbformat.append(colLengths[16]);
							sbformat.append("s");

							sbformat.append("|%18$-");
							sbformat.append(colLengths[17]);
							sbformat.append("s");

							sbformat.append("|%19$-");
							sbformat.append(colLengths[18]);
							sbformat.append("s");

							sbformat.append("|%20$-");
							sbformat.append(colLengths[19]);
							sbformat.append("s");

							sbformat.append("|%21$-");
							sbformat.append(colLengths[20]);
							sbformat.append("s");

							sbformat.append("|%22$-");
							sbformat.append(colLengths[21]);
							sbformat.append("s");

							sbformat.append("|%23$-");
							sbformat.append(colLengths[22]);
							sbformat.append("s");

							sbformat.append("|%24$-");
							sbformat.append(colLengths[23]);
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
						for (int i = 0; i < colLengths[3] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[4] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[5] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[6] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[7] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[8] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[9] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[10] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[11] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[12] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[13] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[14] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[15] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[16] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[17] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[18] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[19] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[20] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[21] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[22] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);

						// last column
						for (int i = 0; i < colLengths[23] - fillChars[1].length() + 1; i++) {
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
				util_tLogRow_1.setTableName("OutputConsole");
				util_tLogRow_1.addRow(new String[] { "Id", "First_Name", "Last_Name", "Gender", "Age", "Occupation",
						"MaritalStatus", "Salary", "Address", "City", "State", "Zip", "Phone", "Email", "TDS_ID",
						"TDS_STATE", "TDS_ASSIGNEE", "TDS_CREATION", "TDS_LAST_UPDATE", "TDS_LAST_UPDATED_BY",
						"TDS_PRIORITY", "TDS_TAGS", "TDS_DUE_DATE", "TDS_EXTERNAL_ID", });
				StringBuilder strBuffer_tLogRow_1 = null;
				int nb_line_tLogRow_1 = 0;
///////////////////////    			

				/**
				 * [tLogRow_1 begin ] stop
				 */

				/**
				 * [tFileOutputDelimited_1 begin ] start
				 */

				ok_Hash.put("tFileOutputDelimited_1", false);
				start_Hash.put("tFileOutputDelimited_1", System.currentTimeMillis());

				currentComponent = "tFileOutputDelimited_1";

				cLabel = "ValidCustomers";

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row1");

				int tos_count_tFileOutputDelimited_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tFileOutputDelimited_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tFileOutputDelimited_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tFileOutputDelimited_1 = new StringBuilder();
							log4jParamters_tFileOutputDelimited_1.append("Parameters:");
							log4jParamters_tFileOutputDelimited_1.append("USESTREAM" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append(
									"FILENAME" + " = " + "\"C:/SIFT/Developer/Talend/Data/Demo/CustomersCleaned.csv\"");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("ROWSEPARATOR" + " = " + "\"\\n\"");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("FIELDSEPARATOR" + " = " + "\";\"");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("APPEND" + " = " + "true");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("INCLUDEHEADER" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("ADVANCED_SEPARATOR" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("CSV_OPTION" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("CREATE" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("SPLIT" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("FLUSHONROW" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("ROW_MODE" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("ENCODING" + " = " + "\"ISO-8859-15\"");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("DELETE_EMPTYFILE" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tFileOutputDelimited_1 - " + (log4jParamters_tFileOutputDelimited_1));
						}
					}
					new BytesLimit65535_tFileOutputDelimited_1().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tFileOutputDelimited_1", "ValidCustomers", "tFileOutputDelimited");
					talendJobLogProcess(globalMap);
				}

				String fileName_tFileOutputDelimited_1 = "";
				fileName_tFileOutputDelimited_1 = (new java.io.File(
						"C:/SIFT/Developer/Talend/Data/Demo/CustomersCleaned.csv")).getAbsolutePath().replace("\\",
								"/");
				String fullName_tFileOutputDelimited_1 = null;
				String extension_tFileOutputDelimited_1 = null;
				String directory_tFileOutputDelimited_1 = null;
				if ((fileName_tFileOutputDelimited_1.indexOf("/") != -1)) {
					if (fileName_tFileOutputDelimited_1.lastIndexOf(".") < fileName_tFileOutputDelimited_1
							.lastIndexOf("/")) {
						fullName_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1;
						extension_tFileOutputDelimited_1 = "";
					} else {
						fullName_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1.substring(0,
								fileName_tFileOutputDelimited_1.lastIndexOf("."));
						extension_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1
								.substring(fileName_tFileOutputDelimited_1.lastIndexOf("."));
					}
					directory_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1.substring(0,
							fileName_tFileOutputDelimited_1.lastIndexOf("/"));
				} else {
					if (fileName_tFileOutputDelimited_1.lastIndexOf(".") != -1) {
						fullName_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1.substring(0,
								fileName_tFileOutputDelimited_1.lastIndexOf("."));
						extension_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1
								.substring(fileName_tFileOutputDelimited_1.lastIndexOf("."));
					} else {
						fullName_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1;
						extension_tFileOutputDelimited_1 = "";
					}
					directory_tFileOutputDelimited_1 = "";
				}
				boolean isFileGenerated_tFileOutputDelimited_1 = true;
				java.io.File filetFileOutputDelimited_1 = new java.io.File(fileName_tFileOutputDelimited_1);
				globalMap.put("tFileOutputDelimited_1_FILE_NAME", fileName_tFileOutputDelimited_1);
				if (filetFileOutputDelimited_1.exists()) {
					isFileGenerated_tFileOutputDelimited_1 = false;
				}
				int nb_line_tFileOutputDelimited_1 = 0;
				int splitedFileNo_tFileOutputDelimited_1 = 0;
				int currentRow_tFileOutputDelimited_1 = 0;

				final String OUT_DELIM_tFileOutputDelimited_1 = /** Start field tFileOutputDelimited_1:FIELDSEPARATOR */
						";"/** End field tFileOutputDelimited_1:FIELDSEPARATOR */
				;

				final String OUT_DELIM_ROWSEP_tFileOutputDelimited_1 = /**
																		 * Start field
																		 * tFileOutputDelimited_1:ROWSEPARATOR
																		 */
						"\n"/** End field tFileOutputDelimited_1:ROWSEPARATOR */
				;

				// routines.system.Row
				java.io.Writer outtFileOutputDelimited_1 = null;

				outtFileOutputDelimited_1 = new java.io.BufferedWriter(new java.io.OutputStreamWriter(
						new java.io.FileOutputStream(fileName_tFileOutputDelimited_1, true), "ISO-8859-15"));
				resourceMap.put("out_tFileOutputDelimited_1", outtFileOutputDelimited_1);

				resourceMap.put("nb_line_tFileOutputDelimited_1", nb_line_tFileOutputDelimited_1);

				/**
				 * [tFileOutputDelimited_1 begin ] stop
				 */

				/**
				 * [tDataStewardshipTaskInput_1 begin ] start
				 */

				ok_Hash.put("tDataStewardshipTaskInput_1", false);
				start_Hash.put("tDataStewardshipTaskInput_1", System.currentTimeMillis());

				currentComponent = "tDataStewardshipTaskInput_1";

				cLabel = "ValidatedRecords";

				int tos_count_tDataStewardshipTaskInput_1 = 0;

				if (enableLogStash) {
					talendJobLog.addCM("tDataStewardshipTaskInput_1", "ValidatedRecords", "tDataStewardshipTaskInput");
					talendJobLogProcess(globalMap);
				}

				boolean doesNodeBelongToRequest_tDataStewardshipTaskInput_1 = 0 == 0;
				@SuppressWarnings("unchecked")
				java.util.Map<String, Object> restRequest_tDataStewardshipTaskInput_1 = (java.util.Map<String, Object>) globalMap
						.get("restRequest");
				String currentTRestRequestOperation_tDataStewardshipTaskInput_1 = (String) (restRequest_tDataStewardshipTaskInput_1 != null
						? restRequest_tDataStewardshipTaskInput_1.get("OPERATION")
						: null);

				org.talend.components.api.component.ComponentDefinition def_tDataStewardshipTaskInput_1 = new org.talend.components.datastewardship.tdatastewardshiptaskinput.TDataStewardshipTaskInputDefinition();

				org.talend.components.api.component.runtime.Writer writer_tDataStewardshipTaskInput_1 = null;
				org.talend.components.api.component.runtime.Reader reader_tDataStewardshipTaskInput_1 = null;

				org.talend.components.datastewardship.tdatastewardshiptaskinput.TDataStewardshipTaskInputProperties props_tDataStewardshipTaskInput_1 = (org.talend.components.datastewardship.tdatastewardshiptaskinput.TDataStewardshipTaskInputProperties) def_tDataStewardshipTaskInput_1
						.createRuntimeProperties();
				props_tDataStewardshipTaskInput_1.setValue("batchSize", 50);

				props_tDataStewardshipTaskInput_1.setValue("consumeTasks", false);

				props_tDataStewardshipTaskInput_1.setValue("paginationWithID", false);

				props_tDataStewardshipTaskInput_1.setValue("retryOnTimeout", 2);

				props_tDataStewardshipTaskInput_1.setValue("searchQuery", "");

				props_tDataStewardshipTaskInput_1.setValue("campaignName", "c42f03ad5f6882cf57f9164faf263b04a");

				props_tDataStewardshipTaskInput_1.setValue("campaignLabel", "Demo Campaign");

				props_tDataStewardshipTaskInput_1.setValue("campaignType",
						org.talend.components.datastewardship.common.CampaignType.RESOLUTION);

				props_tDataStewardshipTaskInput_1.setValue("laxSchema", false);

				props_tDataStewardshipTaskInput_1.setValue("taskPriority", "ANY_PRIORITY");

				props_tDataStewardshipTaskInput_1.setValue("taskTags", "");

				props_tDataStewardshipTaskInput_1.setValue("taskState", "Resolved");

				props_tDataStewardshipTaskInput_1.setValue("taskAssignee", "Any Assignee");

				java.util.List<Object> tDataStewardshipTaskInput_1_httpConfig_keys = new java.util.ArrayList<Object>();

				((org.talend.daikon.properties.Properties) props_tDataStewardshipTaskInput_1.httpConfig)
						.setValue("keys", tDataStewardshipTaskInput_1_httpConfig_keys);

				java.util.List<Object> tDataStewardshipTaskInput_1_httpConfig_values = new java.util.ArrayList<Object>();

				((org.talend.daikon.properties.Properties) props_tDataStewardshipTaskInput_1.httpConfig)
						.setValue("values", tDataStewardshipTaskInput_1_httpConfig_values);

				class SchemaSettingTool_tDataStewardshipTaskInput_1_1_fisrt {

					String getSchemaValue() {

						StringBuilder s = new StringBuilder();

						a("{\"type\":\"record\",", s);

						a("\"name\":\"MAIN\",\"fields\":[{", s);

						a("\"name\":\"Id\",\"type\":\"int\",\"di.table.comment\":\"integer\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"Id\",\"talend.field.dbColumnName\":\"Id\",\"di.column.talendType\":\"id_Integer\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"Id\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"First_Name\",\"type\":\"string\",\"di.table.comment\":\"text\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"First_Name\",\"talend.field.dbColumnName\":\"First_Name\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"First_Name\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"Last_Name\",\"type\":\"string\",\"di.table.comment\":\"text\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"Last_Name\",\"talend.field.dbColumnName\":\"Last_Name\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"Last_Name\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"Gender\",\"type\":\"string\",\"di.table.comment\":\"text\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"Gender\",\"talend.field.dbColumnName\":\"Gender\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"Gender\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"Age\",\"type\":\"string\",\"di.table.comment\":\"text\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"Age\",\"talend.field.dbColumnName\":\"Age\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"Age\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"Occupation\",\"type\":\"string\",\"di.table.comment\":\"text\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"Occupation\",\"talend.field.dbColumnName\":\"Occupation\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"Occupation\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"MaritalStatus\",\"type\":\"string\",\"di.table.comment\":\"text\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"MaritalStatus\",\"talend.field.dbColumnName\":\"MaritalStatus\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"MaritalStatus\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"Salary\",\"type\":\"string\",\"di.table.comment\":\"text\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"Salary\",\"talend.field.dbColumnName\":\"Salary\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"Salary\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"Address\",\"type\":\"string\",\"di.table.comment\":\"text\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"Address\",\"talend.field.dbColumnName\":\"Address\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"Address\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"City\",\"type\":\"string\",\"di.table.comment\":\"text\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"City\",\"talend.field.dbColumnName\":\"City\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"City\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"State\",\"type\":\"string\",\"di.table.comment\":\"text\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"State\",\"talend.field.dbColumnName\":\"State\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"State\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"Zip\",\"type\":\"int\",\"di.table.comment\":\"integer\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"Zip\",\"talend.field.dbColumnName\":\"Zip\",\"di.column.talendType\":\"id_Integer\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"Zip\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"Phone\",\"type\":\"string\",\"di.table.comment\":\"text\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"Phone\",\"talend.field.dbColumnName\":\"Phone\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"Phone\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"Email\",\"type\":\"string\",\"di.table.comment\":\"text\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"Email\",\"talend.field.dbColumnName\":\"Email\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"Email\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"TDS_ID\",\"type\":\"string\",\"di.table.comment\":\"\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"TDS_ID\",\"talend.field.dbColumnName\":\"TDS_ID\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"TDS_ID\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"TDS_STATE\",\"type\":\"string\",\"di.table.comment\":\"\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"TDS_STATE\",\"talend.field.dbColumnName\":\"TDS_STATE\",\"di.column.talendType\":\"id_String\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"TDS_STATE\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"TDS_ASSIGNEE\",\"type\":[\"string\",\"null\"],\"di.table.comment\":\"\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"TDS_ASSIGNEE\",\"talend.field.dbColumnName\":\"TDS_ASSIGNEE\",\"di.column.talendType\":\"id_String\",\"di.column.isNullable\":\"true\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"TDS_ASSIGNEE\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"TDS_CREATION\",\"type\":\"long\",\"di.table.comment\":\"\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"TDS_CREATION\",\"talend.field.dbColumnName\":\"TDS_CREATION\",\"di.column.talendType\":\"id_Long\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"TDS_CREATION\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"TDS_LAST_UPDATE\",\"type\":\"long\",\"di.table.comment\":\"\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"TDS_LAST_UPDATE\",\"talend.field.dbColumnName\":\"TDS_LAST_UPDATE\",\"di.column.talendType\":\"id_Long\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"TDS_LAST_UPDATE\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"TDS_LAST_UPDATED_BY\",\"type\":[\"string\",\"null\"],\"di.table.comment\":\"\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"TDS_LAST_UPDATED_BY\",\"talend.field.dbColumnName\":\"TDS_LAST_UPDATED_BY\",\"di.column.talendType\":\"id_String\",\"di.column.isNullable\":\"true\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"TDS_LAST_UPDATED_BY\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"TDS_PRIORITY\",\"type\":[\"int\",\"null\"],\"di.table.comment\":\"\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"TDS_PRIORITY\",\"talend.field.dbColumnName\":\"TDS_PRIORITY\",\"di.column.talendType\":\"id_Integer\",\"di.column.isNullable\":\"true\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"TDS_PRIORITY\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"TDS_TAGS\",\"type\":[\"string\",\"null\"],\"di.table.comment\":\"\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"TDS_TAGS\",\"talend.field.dbColumnName\":\"TDS_TAGS\",\"di.column.talendType\":\"id_String\",\"di.column.isNullable\":\"true\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"TDS_TAGS\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"TDS_DUE_DATE\",\"type\":[\"long\",\"null\"],\"di.table.comment\":\"\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"TDS_DUE_DATE\",\"talend.field.dbColumnName\":\"TDS_DUE_DATE\",\"di.column.talendType\":\"id_Long\",\"di.column.isNullable\":\"true\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"TDS_DUE_DATE\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"TDS_EXTERNAL_ID\",\"type\":[\"string\",\"null\"],\"di.table.comment\":\"\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"TDS_EXTERNAL_ID\",\"talend.field.dbColumnName\":\"TDS_EXTERNAL_ID\",\"di.column.talendType\":\"id_String\",\"di.column.isNullable\":\"true\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"TDS_EXTERNAL_ID\",\"di.column.relatedEntity\":\"\"}],\"di.table.name\":\"MAIN\",\"di.table.label\":\"MAIN\"}",
								s);

						return s.toString();

					}

					void a(String part, StringBuilder strB) {
						strB.append(part);
					}

				}

				SchemaSettingTool_tDataStewardshipTaskInput_1_1_fisrt sst_tDataStewardshipTaskInput_1_1_fisrt = new SchemaSettingTool_tDataStewardshipTaskInput_1_1_fisrt();

				props_tDataStewardshipTaskInput_1.schema.setValue("schema", new org.apache.avro.Schema.Parser()
						.setValidateDefaults(false).parse(sst_tDataStewardshipTaskInput_1_1_fisrt.getSchemaValue()));

				props_tDataStewardshipTaskInput_1.connection.setValue("url",
						"https://tds.ap.cloud.talend.com/data-stewardship");

				props_tDataStewardshipTaskInput_1.connection.setValue("username", "eric.tan@nfr.siftag.com");

				props_tDataStewardshipTaskInput_1.connection.setValue("password",
						routines.system.PasswordEncryptUtil.decryptPassword(
								"enc:routine.encryption.key.v1:3fp/nM3wXNMW5CQyh+8Y0oz5epRKLyufzYQW4bWykajI3bb0jRgtPw=="));

				props_tDataStewardshipTaskInput_1.connection.refConnection.setValue("referenceDefinitionName",
						"datastewardship");

				if (org.talend.components.api.properties.ComponentReferenceProperties.ReferenceType.COMPONENT_INSTANCE == props_tDataStewardshipTaskInput_1.connection.refConnection.referenceType
						.getValue()) {
					final String referencedComponentInstanceId_tDataStewardshipTaskInput_1 = props_tDataStewardshipTaskInput_1.connection.refConnection.componentInstanceId
							.getStringValue();
					if (referencedComponentInstanceId_tDataStewardshipTaskInput_1 != null) {
						org.talend.daikon.properties.Properties referencedComponentProperties_tDataStewardshipTaskInput_1 = (org.talend.daikon.properties.Properties) globalMap
								.get(referencedComponentInstanceId_tDataStewardshipTaskInput_1
										+ "_COMPONENT_RUNTIME_PROPERTIES");
						props_tDataStewardshipTaskInput_1.connection.refConnection
								.setReference(referencedComponentProperties_tDataStewardshipTaskInput_1);
					}
				}
				globalMap.put("tDataStewardshipTaskInput_1_COMPONENT_RUNTIME_PROPERTIES",
						props_tDataStewardshipTaskInput_1);
				globalMap.putIfAbsent("TALEND_PRODUCT_VERSION", "8.0");
				globalMap.put("TALEND_COMPONENTS_VERSION", "0.37.27");
				java.net.URL mappings_url_tDataStewardshipTaskInput_1 = this.getClass().getResource("/xmlMappings");
				globalMap.put("tDataStewardshipTaskInput_1_MAPPINGS_URL", mappings_url_tDataStewardshipTaskInput_1);

				org.talend.components.api.container.RuntimeContainer container_tDataStewardshipTaskInput_1 = new org.talend.components.api.container.RuntimeContainer() {
					public Object getComponentData(String componentId, String key) {
						return globalMap.get(componentId + "_" + key);
					}

					public void setComponentData(String componentId, String key, Object data) {
						globalMap.put(componentId + "_" + key, data);
					}

					public String getCurrentComponentId() {
						return "tDataStewardshipTaskInput_1";
					}

					public Object getGlobalData(String key) {
						return globalMap.get(key);
					}
				};

				int nb_line_tDataStewardshipTaskInput_1 = 0;

				org.talend.components.api.component.ConnectorTopology topology_tDataStewardshipTaskInput_1 = null;
				topology_tDataStewardshipTaskInput_1 = org.talend.components.api.component.ConnectorTopology.OUTGOING;

				org.talend.daikon.runtime.RuntimeInfo runtime_info_tDataStewardshipTaskInput_1 = def_tDataStewardshipTaskInput_1
						.getRuntimeInfo(org.talend.components.api.component.runtime.ExecutionEngine.DI,
								props_tDataStewardshipTaskInput_1, topology_tDataStewardshipTaskInput_1);
				java.util.Set<org.talend.components.api.component.ConnectorTopology> supported_connector_topologies_tDataStewardshipTaskInput_1 = def_tDataStewardshipTaskInput_1
						.getSupportedConnectorTopologies();

				org.talend.components.api.component.runtime.RuntimableRuntime componentRuntime_tDataStewardshipTaskInput_1 = (org.talend.components.api.component.runtime.RuntimableRuntime) (Class
						.forName(runtime_info_tDataStewardshipTaskInput_1.getRuntimeClassName()).newInstance());
				org.talend.daikon.properties.ValidationResult initVr_tDataStewardshipTaskInput_1 = componentRuntime_tDataStewardshipTaskInput_1
						.initialize(container_tDataStewardshipTaskInput_1, props_tDataStewardshipTaskInput_1);

				if (initVr_tDataStewardshipTaskInput_1
						.getStatus() == org.talend.daikon.properties.ValidationResult.Result.ERROR) {
					throw new RuntimeException(initVr_tDataStewardshipTaskInput_1.getMessage());
				}

				if (componentRuntime_tDataStewardshipTaskInput_1 instanceof org.talend.components.api.component.runtime.ComponentDriverInitialization) {
					org.talend.components.api.component.runtime.ComponentDriverInitialization compDriverInitialization_tDataStewardshipTaskInput_1 = (org.talend.components.api.component.runtime.ComponentDriverInitialization) componentRuntime_tDataStewardshipTaskInput_1;
					compDriverInitialization_tDataStewardshipTaskInput_1
							.runAtDriver(container_tDataStewardshipTaskInput_1);
				}

				org.talend.components.api.component.runtime.SourceOrSink sourceOrSink_tDataStewardshipTaskInput_1 = null;
				if (componentRuntime_tDataStewardshipTaskInput_1 instanceof org.talend.components.api.component.runtime.SourceOrSink) {
					sourceOrSink_tDataStewardshipTaskInput_1 = (org.talend.components.api.component.runtime.SourceOrSink) componentRuntime_tDataStewardshipTaskInput_1;
					if (doesNodeBelongToRequest_tDataStewardshipTaskInput_1) {
						org.talend.daikon.properties.ValidationResult vr_tDataStewardshipTaskInput_1 = sourceOrSink_tDataStewardshipTaskInput_1
								.validate(container_tDataStewardshipTaskInput_1);
						if (vr_tDataStewardshipTaskInput_1
								.getStatus() == org.talend.daikon.properties.ValidationResult.Result.ERROR) {
							throw new RuntimeException(vr_tDataStewardshipTaskInput_1.getMessage());
						}
					}
				}

				if (sourceOrSink_tDataStewardshipTaskInput_1 instanceof org.talend.components.api.component.runtime.Source) {
					org.talend.components.api.component.runtime.Source source_tDataStewardshipTaskInput_1 = (org.talend.components.api.component.runtime.Source) sourceOrSink_tDataStewardshipTaskInput_1;
					reader_tDataStewardshipTaskInput_1 = source_tDataStewardshipTaskInput_1
							.createReader(container_tDataStewardshipTaskInput_1);
					reader_tDataStewardshipTaskInput_1 = new org.talend.codegen.flowvariables.runtime.FlowVariablesReader(
							reader_tDataStewardshipTaskInput_1, container_tDataStewardshipTaskInput_1);

					boolean multi_output_is_allowed_tDataStewardshipTaskInput_1 = false;
					org.talend.components.api.component.Connector c_tDataStewardshipTaskInput_1 = null;
					for (org.talend.components.api.component.Connector currentConnector : props_tDataStewardshipTaskInput_1
							.getAvailableConnectors(null, true)) {
						if (currentConnector.getName().equals("MAIN")) {
							c_tDataStewardshipTaskInput_1 = currentConnector;
						}

						if (currentConnector.getName().equals("REJECT")) {// it's better to move the code to javajet
							multi_output_is_allowed_tDataStewardshipTaskInput_1 = true;
						}
					}
					org.apache.avro.Schema schema_tDataStewardshipTaskInput_1 = props_tDataStewardshipTaskInput_1
							.getSchema(c_tDataStewardshipTaskInput_1, true);

					org.talend.codegen.enforcer.OutgoingSchemaEnforcer outgoingEnforcer_tDataStewardshipTaskInput_1 = org.talend.codegen.enforcer.EnforcerCreator
							.createOutgoingEnforcer(schema_tDataStewardshipTaskInput_1, false);

					// Create a reusable factory that converts the output of the reader to an
					// IndexedRecord.
					org.talend.daikon.avro.converter.IndexedRecordConverter<Object, ? extends org.apache.avro.generic.IndexedRecord> factory_tDataStewardshipTaskInput_1 = null;

					// Iterate through the incoming data.
					boolean available_tDataStewardshipTaskInput_1 = reader_tDataStewardshipTaskInput_1.start();

					resourceMap.put("reader_tDataStewardshipTaskInput_1", reader_tDataStewardshipTaskInput_1);

					for (; available_tDataStewardshipTaskInput_1; available_tDataStewardshipTaskInput_1 = reader_tDataStewardshipTaskInput_1
							.advance()) {
						nb_line_tDataStewardshipTaskInput_1++;

						if (multi_output_is_allowed_tDataStewardshipTaskInput_1) {

							row1 = null;

						}

						try {
							Object data_tDataStewardshipTaskInput_1 = reader_tDataStewardshipTaskInput_1.getCurrent();

							if (multi_output_is_allowed_tDataStewardshipTaskInput_1) {
								row1 = new row1Struct();
							}

							// Construct the factory once when the first data arrives.
							if (factory_tDataStewardshipTaskInput_1 == null) {
								factory_tDataStewardshipTaskInput_1 = (org.talend.daikon.avro.converter.IndexedRecordConverter<Object, ? extends org.apache.avro.generic.IndexedRecord>) new org.talend.daikon.avro.AvroRegistry()
										.createIndexedRecordConverter(data_tDataStewardshipTaskInput_1.getClass());
							}

							// Enforce the outgoing schema on the input.
							outgoingEnforcer_tDataStewardshipTaskInput_1.setWrapped(factory_tDataStewardshipTaskInput_1
									.convertToAvro(data_tDataStewardshipTaskInput_1));
							Object columnValue_0_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(0);
							if (columnValue_0_tDataStewardshipTaskInput_1 == null) {
								row1.Id = 0;
							} else {
								row1.Id = (int) (columnValue_0_tDataStewardshipTaskInput_1);
							}
							Object columnValue_1_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(1);
							row1.First_Name = (String) (columnValue_1_tDataStewardshipTaskInput_1);
							Object columnValue_2_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(2);
							row1.Last_Name = (String) (columnValue_2_tDataStewardshipTaskInput_1);
							Object columnValue_3_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(3);
							row1.Gender = (String) (columnValue_3_tDataStewardshipTaskInput_1);
							Object columnValue_4_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(4);
							row1.Age = (String) (columnValue_4_tDataStewardshipTaskInput_1);
							Object columnValue_5_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(5);
							row1.Occupation = (String) (columnValue_5_tDataStewardshipTaskInput_1);
							Object columnValue_6_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(6);
							row1.MaritalStatus = (String) (columnValue_6_tDataStewardshipTaskInput_1);
							Object columnValue_7_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(7);
							row1.Salary = (String) (columnValue_7_tDataStewardshipTaskInput_1);
							Object columnValue_8_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(8);
							row1.Address = (String) (columnValue_8_tDataStewardshipTaskInput_1);
							Object columnValue_9_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(9);
							row1.City = (String) (columnValue_9_tDataStewardshipTaskInput_1);
							Object columnValue_10_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(10);
							row1.State = (String) (columnValue_10_tDataStewardshipTaskInput_1);
							Object columnValue_11_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(11);
							if (columnValue_11_tDataStewardshipTaskInput_1 == null) {
								row1.Zip = 0;
							} else {
								row1.Zip = (int) (columnValue_11_tDataStewardshipTaskInput_1);
							}
							Object columnValue_12_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(12);
							row1.Phone = (String) (columnValue_12_tDataStewardshipTaskInput_1);
							Object columnValue_13_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(13);
							row1.Email = (String) (columnValue_13_tDataStewardshipTaskInput_1);
							Object columnValue_14_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(14);
							row1.TDS_ID = (String) (columnValue_14_tDataStewardshipTaskInput_1);
							Object columnValue_15_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(15);
							row1.TDS_STATE = (String) (columnValue_15_tDataStewardshipTaskInput_1);
							Object columnValue_16_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(16);
							row1.TDS_ASSIGNEE = (String) (columnValue_16_tDataStewardshipTaskInput_1);
							Object columnValue_17_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(17);
							if (columnValue_17_tDataStewardshipTaskInput_1 == null) {
								row1.TDS_CREATION = 0;
							} else {
								row1.TDS_CREATION = (long) (columnValue_17_tDataStewardshipTaskInput_1);
							}
							Object columnValue_18_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(18);
							if (columnValue_18_tDataStewardshipTaskInput_1 == null) {
								row1.TDS_LAST_UPDATE = 0;
							} else {
								row1.TDS_LAST_UPDATE = (long) (columnValue_18_tDataStewardshipTaskInput_1);
							}
							Object columnValue_19_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(19);
							row1.TDS_LAST_UPDATED_BY = (String) (columnValue_19_tDataStewardshipTaskInput_1);
							Object columnValue_20_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(20);
							row1.TDS_PRIORITY = (Integer) (columnValue_20_tDataStewardshipTaskInput_1);
							Object columnValue_21_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(21);
							row1.TDS_TAGS = (String) (columnValue_21_tDataStewardshipTaskInput_1);
							Object columnValue_22_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(22);
							row1.TDS_DUE_DATE = (Long) (columnValue_22_tDataStewardshipTaskInput_1);
							Object columnValue_23_tDataStewardshipTaskInput_1 = outgoingEnforcer_tDataStewardshipTaskInput_1
									.get(23);
							row1.TDS_EXTERNAL_ID = (String) (columnValue_23_tDataStewardshipTaskInput_1);
						} catch (org.talend.components.api.exception.DataRejectException e_tDataStewardshipTaskInput_1) {
							java.util.Map<String, Object> info_tDataStewardshipTaskInput_1 = e_tDataStewardshipTaskInput_1
									.getRejectInfo();

							// TODO use a method instead of getting method by the special key
							// "error/errorMessage"
							Object errorMessage_tDataStewardshipTaskInput_1 = null;
							if (info_tDataStewardshipTaskInput_1.containsKey("error")) {
								errorMessage_tDataStewardshipTaskInput_1 = info_tDataStewardshipTaskInput_1
										.get("error");
							} else if (info_tDataStewardshipTaskInput_1.containsKey("errorMessage")) {
								errorMessage_tDataStewardshipTaskInput_1 = info_tDataStewardshipTaskInput_1
										.get("errorMessage");
							} else {
								errorMessage_tDataStewardshipTaskInput_1 = "Rejected but error message missing";
							}
							errorMessage_tDataStewardshipTaskInput_1 = "Row " + nb_line_tDataStewardshipTaskInput_1
									+ ": " + errorMessage_tDataStewardshipTaskInput_1;
							System.err.println(errorMessage_tDataStewardshipTaskInput_1);

							// If the record is reject, the main line record should put NULL
							row1 = null;

						} // end of catch

						java.lang.Iterable<?> outgoingMainRecordsList_tDataStewardshipTaskInput_1 = new java.util.ArrayList<Object>();
						java.util.Iterator outgoingMainRecordsIt_tDataStewardshipTaskInput_1 = null;

						/**
						 * [tDataStewardshipTaskInput_1 begin ] stop
						 */

						/**
						 * [tDataStewardshipTaskInput_1 main ] start
						 */

						currentComponent = "tDataStewardshipTaskInput_1";

						cLabel = "ValidatedRecords";

						tos_count_tDataStewardshipTaskInput_1++;

						/**
						 * [tDataStewardshipTaskInput_1 main ] stop
						 */

						/**
						 * [tDataStewardshipTaskInput_1 process_data_begin ] start
						 */

						currentComponent = "tDataStewardshipTaskInput_1";

						cLabel = "ValidatedRecords";

						/**
						 * [tDataStewardshipTaskInput_1 process_data_begin ] stop
						 */

						/**
						 * [tFileOutputDelimited_1 main ] start
						 */

						currentComponent = "tFileOutputDelimited_1";

						cLabel = "ValidCustomers";

						if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

								, "row1", "tDataStewardshipTaskInput_1", "ValidatedRecords",
								"tDataStewardshipTaskInput", "tFileOutputDelimited_1", "ValidCustomers",
								"tFileOutputDelimited"

						)) {
							talendJobLogProcess(globalMap);
						}

						if (log.isTraceEnabled()) {
							log.trace("row1 - " + (row1 == null ? "" : row1.toLogString()));
						}

						StringBuilder sb_tFileOutputDelimited_1 = new StringBuilder();
						sb_tFileOutputDelimited_1.append(row1.Id);
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.First_Name != null) {
							sb_tFileOutputDelimited_1.append(row1.First_Name);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.Last_Name != null) {
							sb_tFileOutputDelimited_1.append(row1.Last_Name);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.Gender != null) {
							sb_tFileOutputDelimited_1.append(row1.Gender);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.Age != null) {
							sb_tFileOutputDelimited_1.append(row1.Age);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.Occupation != null) {
							sb_tFileOutputDelimited_1.append(row1.Occupation);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.MaritalStatus != null) {
							sb_tFileOutputDelimited_1.append(row1.MaritalStatus);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.Salary != null) {
							sb_tFileOutputDelimited_1.append(row1.Salary);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.Address != null) {
							sb_tFileOutputDelimited_1.append(row1.Address);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.City != null) {
							sb_tFileOutputDelimited_1.append(row1.City);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.State != null) {
							sb_tFileOutputDelimited_1.append(row1.State);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						sb_tFileOutputDelimited_1.append(row1.Zip);
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.Phone != null) {
							sb_tFileOutputDelimited_1.append(row1.Phone);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.Email != null) {
							sb_tFileOutputDelimited_1.append(row1.Email);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.TDS_ID != null) {
							sb_tFileOutputDelimited_1.append(row1.TDS_ID);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.TDS_STATE != null) {
							sb_tFileOutputDelimited_1.append(row1.TDS_STATE);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.TDS_ASSIGNEE != null) {
							sb_tFileOutputDelimited_1.append(row1.TDS_ASSIGNEE);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						sb_tFileOutputDelimited_1.append(row1.TDS_CREATION);
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						sb_tFileOutputDelimited_1.append(row1.TDS_LAST_UPDATE);
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.TDS_LAST_UPDATED_BY != null) {
							sb_tFileOutputDelimited_1.append(row1.TDS_LAST_UPDATED_BY);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.TDS_PRIORITY != null) {
							sb_tFileOutputDelimited_1.append(row1.TDS_PRIORITY);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.TDS_TAGS != null) {
							sb_tFileOutputDelimited_1.append(row1.TDS_TAGS);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.TDS_DUE_DATE != null) {
							sb_tFileOutputDelimited_1.append(row1.TDS_DUE_DATE);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
						if (row1.TDS_EXTERNAL_ID != null) {
							sb_tFileOutputDelimited_1.append(row1.TDS_EXTERNAL_ID);
						}
						sb_tFileOutputDelimited_1.append(OUT_DELIM_ROWSEP_tFileOutputDelimited_1);

						nb_line_tFileOutputDelimited_1++;
						resourceMap.put("nb_line_tFileOutputDelimited_1", nb_line_tFileOutputDelimited_1);

						outtFileOutputDelimited_1.write(sb_tFileOutputDelimited_1.toString());
						log.debug(
								"tFileOutputDelimited_1 - Writing the record " + nb_line_tFileOutputDelimited_1 + ".");

						row2 = row1;

						tos_count_tFileOutputDelimited_1++;

						/**
						 * [tFileOutputDelimited_1 main ] stop
						 */

						/**
						 * [tFileOutputDelimited_1 process_data_begin ] start
						 */

						currentComponent = "tFileOutputDelimited_1";

						cLabel = "ValidCustomers";

						/**
						 * [tFileOutputDelimited_1 process_data_begin ] stop
						 */

						/**
						 * [tLogRow_1 main ] start
						 */

						currentComponent = "tLogRow_1";

						cLabel = "OutputConsole";

						if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

								, "row2", "tFileOutputDelimited_1", "ValidCustomers", "tFileOutputDelimited",
								"tLogRow_1", "OutputConsole", "tLogRow"

						)) {
							talendJobLogProcess(globalMap);
						}

						if (log.isTraceEnabled()) {
							log.trace("row2 - " + (row2 == null ? "" : row2.toLogString()));
						}

///////////////////////		

						String[] row_tLogRow_1 = new String[24];

						row_tLogRow_1[0] = String.valueOf(row2.Id);

						if (row2.First_Name != null) { //
							row_tLogRow_1[1] = String.valueOf(row2.First_Name);

						} //

						if (row2.Last_Name != null) { //
							row_tLogRow_1[2] = String.valueOf(row2.Last_Name);

						} //

						if (row2.Gender != null) { //
							row_tLogRow_1[3] = String.valueOf(row2.Gender);

						} //

						if (row2.Age != null) { //
							row_tLogRow_1[4] = String.valueOf(row2.Age);

						} //

						if (row2.Occupation != null) { //
							row_tLogRow_1[5] = String.valueOf(row2.Occupation);

						} //

						if (row2.MaritalStatus != null) { //
							row_tLogRow_1[6] = String.valueOf(row2.MaritalStatus);

						} //

						if (row2.Salary != null) { //
							row_tLogRow_1[7] = String.valueOf(row2.Salary);

						} //

						if (row2.Address != null) { //
							row_tLogRow_1[8] = String.valueOf(row2.Address);

						} //

						if (row2.City != null) { //
							row_tLogRow_1[9] = String.valueOf(row2.City);

						} //

						if (row2.State != null) { //
							row_tLogRow_1[10] = String.valueOf(row2.State);

						} //

						row_tLogRow_1[11] = String.valueOf(row2.Zip);

						if (row2.Phone != null) { //
							row_tLogRow_1[12] = String.valueOf(row2.Phone);

						} //

						if (row2.Email != null) { //
							row_tLogRow_1[13] = String.valueOf(row2.Email);

						} //

						if (row2.TDS_ID != null) { //
							row_tLogRow_1[14] = String.valueOf(row2.TDS_ID);

						} //

						if (row2.TDS_STATE != null) { //
							row_tLogRow_1[15] = String.valueOf(row2.TDS_STATE);

						} //

						if (row2.TDS_ASSIGNEE != null) { //
							row_tLogRow_1[16] = String.valueOf(row2.TDS_ASSIGNEE);

						} //

						row_tLogRow_1[17] = String.valueOf(row2.TDS_CREATION);

						row_tLogRow_1[18] = String.valueOf(row2.TDS_LAST_UPDATE);

						if (row2.TDS_LAST_UPDATED_BY != null) { //
							row_tLogRow_1[19] = String.valueOf(row2.TDS_LAST_UPDATED_BY);

						} //

						if (row2.TDS_PRIORITY != null) { //
							row_tLogRow_1[20] = String.valueOf(row2.TDS_PRIORITY);

						} //

						if (row2.TDS_TAGS != null) { //
							row_tLogRow_1[21] = String.valueOf(row2.TDS_TAGS);

						} //

						if (row2.TDS_DUE_DATE != null) { //
							row_tLogRow_1[22] = String.valueOf(row2.TDS_DUE_DATE);

						} //

						if (row2.TDS_EXTERNAL_ID != null) { //
							row_tLogRow_1[23] = String.valueOf(row2.TDS_EXTERNAL_ID);

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

						cLabel = "OutputConsole";

						/**
						 * [tLogRow_1 process_data_begin ] stop
						 */

						/**
						 * [tLogRow_1 process_data_end ] start
						 */

						currentComponent = "tLogRow_1";

						cLabel = "OutputConsole";

						/**
						 * [tLogRow_1 process_data_end ] stop
						 */

						/**
						 * [tFileOutputDelimited_1 process_data_end ] start
						 */

						currentComponent = "tFileOutputDelimited_1";

						cLabel = "ValidCustomers";

						/**
						 * [tFileOutputDelimited_1 process_data_end ] stop
						 */

						/**
						 * [tDataStewardshipTaskInput_1 process_data_end ] start
						 */

						currentComponent = "tDataStewardshipTaskInput_1";

						cLabel = "ValidatedRecords";

						/**
						 * [tDataStewardshipTaskInput_1 process_data_end ] stop
						 */

						/**
						 * [tDataStewardshipTaskInput_1 end ] start
						 */

						currentComponent = "tDataStewardshipTaskInput_1";

						cLabel = "ValidatedRecords";

// end of generic

						resourceMap.put("finish_tDataStewardshipTaskInput_1", Boolean.TRUE);

					} // while
				} // end of "if (sourceOrSink_tDataStewardshipTaskInput_1 instanceof ...Source)"
				java.util.Map<String, Object> resultMap_tDataStewardshipTaskInput_1 = null;
				if (reader_tDataStewardshipTaskInput_1 != null) {
					reader_tDataStewardshipTaskInput_1.close();
					resultMap_tDataStewardshipTaskInput_1 = reader_tDataStewardshipTaskInput_1.getReturnValues();
				}
				if (resultMap_tDataStewardshipTaskInput_1 != null) {
					for (java.util.Map.Entry<String, Object> entry_tDataStewardshipTaskInput_1 : resultMap_tDataStewardshipTaskInput_1
							.entrySet()) {
						switch (entry_tDataStewardshipTaskInput_1.getKey()) {
						case org.talend.components.api.component.ComponentDefinition.RETURN_ERROR_MESSAGE:
							container_tDataStewardshipTaskInput_1.setComponentData("tDataStewardshipTaskInput_1",
									"ERROR_MESSAGE", entry_tDataStewardshipTaskInput_1.getValue());
							break;
						case org.talend.components.api.component.ComponentDefinition.RETURN_TOTAL_RECORD_COUNT:
							container_tDataStewardshipTaskInput_1.setComponentData("tDataStewardshipTaskInput_1",
									"NB_LINE", entry_tDataStewardshipTaskInput_1.getValue());
							break;
						case org.talend.components.api.component.ComponentDefinition.RETURN_SUCCESS_RECORD_COUNT:
							container_tDataStewardshipTaskInput_1.setComponentData("tDataStewardshipTaskInput_1",
									"NB_SUCCESS", entry_tDataStewardshipTaskInput_1.getValue());
							break;
						case org.talend.components.api.component.ComponentDefinition.RETURN_REJECT_RECORD_COUNT:
							container_tDataStewardshipTaskInput_1.setComponentData("tDataStewardshipTaskInput_1",
									"NB_REJECT", entry_tDataStewardshipTaskInput_1.getValue());
							break;
						default:
							StringBuilder studio_key_tDataStewardshipTaskInput_1 = new StringBuilder();
							for (int i_tDataStewardshipTaskInput_1 = 0; i_tDataStewardshipTaskInput_1 < entry_tDataStewardshipTaskInput_1
									.getKey().length(); i_tDataStewardshipTaskInput_1++) {
								char ch_tDataStewardshipTaskInput_1 = entry_tDataStewardshipTaskInput_1.getKey()
										.charAt(i_tDataStewardshipTaskInput_1);
								if (Character.isUpperCase(ch_tDataStewardshipTaskInput_1)
										&& i_tDataStewardshipTaskInput_1 > 0) {
									studio_key_tDataStewardshipTaskInput_1.append('_');
								}
								studio_key_tDataStewardshipTaskInput_1.append(ch_tDataStewardshipTaskInput_1);
							}
							container_tDataStewardshipTaskInput_1.setComponentData(
									"tDataStewardshipTaskInput_1", studio_key_tDataStewardshipTaskInput_1.toString()
											.toUpperCase(java.util.Locale.ENGLISH),
									entry_tDataStewardshipTaskInput_1.getValue());
							break;
						}
					}
				}

				ok_Hash.put("tDataStewardshipTaskInput_1", true);
				end_Hash.put("tDataStewardshipTaskInput_1", System.currentTimeMillis());

				/**
				 * [tDataStewardshipTaskInput_1 end ] stop
				 */

				/**
				 * [tFileOutputDelimited_1 end ] start
				 */

				currentComponent = "tFileOutputDelimited_1";

				cLabel = "ValidCustomers";

				if (outtFileOutputDelimited_1 != null) {
					outtFileOutputDelimited_1.flush();
					outtFileOutputDelimited_1.close();
				}

				globalMap.put("tFileOutputDelimited_1_NB_LINE", nb_line_tFileOutputDelimited_1);
				globalMap.put("tFileOutputDelimited_1_FILE_NAME", fileName_tFileOutputDelimited_1);

				resourceMap.put("finish_tFileOutputDelimited_1", true);

				log.debug("tFileOutputDelimited_1 - Written records count: " + nb_line_tFileOutputDelimited_1 + " .");

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row1", 2, 0,
						"tDataStewardshipTaskInput_1", "ValidatedRecords", "tDataStewardshipTaskInput",
						"tFileOutputDelimited_1", "ValidCustomers", "tFileOutputDelimited", "output")) {
					talendJobLogProcess(globalMap);
				}

				if (log.isDebugEnabled())
					log.debug("tFileOutputDelimited_1 - " + ("Done."));

				ok_Hash.put("tFileOutputDelimited_1", true);
				end_Hash.put("tFileOutputDelimited_1", System.currentTimeMillis());

				/**
				 * [tFileOutputDelimited_1 end ] stop
				 */

				/**
				 * [tLogRow_1 end ] start
				 */

				currentComponent = "tLogRow_1";

				cLabel = "OutputConsole";

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

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row2", 2, 0,
						"tFileOutputDelimited_1", "ValidCustomers", "tFileOutputDelimited", "tLogRow_1",
						"OutputConsole", "tLogRow", "output")) {
					talendJobLogProcess(globalMap);
				}

				if (log.isDebugEnabled())
					log.debug("tLogRow_1 - " + ("Done."));

				ok_Hash.put("tLogRow_1", true);
				end_Hash.put("tLogRow_1", System.currentTimeMillis());

				/**
				 * [tLogRow_1 end ] stop
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
				 * [tDataStewardshipTaskInput_1 finally ] start
				 */

				currentComponent = "tDataStewardshipTaskInput_1";

				cLabel = "ValidatedRecords";

// finally of generic

				if (resourceMap.get("finish_tDataStewardshipTaskInput_1") == null) {
					if (resourceMap.get("reader_tDataStewardshipTaskInput_1") != null) {
						try {
							((org.talend.components.api.component.runtime.Reader) resourceMap
									.get("reader_tDataStewardshipTaskInput_1")).close();
						} catch (java.io.IOException e_tDataStewardshipTaskInput_1) {
							String errorMessage_tDataStewardshipTaskInput_1 = "failed to release the resource in tDataStewardshipTaskInput_1 :"
									+ e_tDataStewardshipTaskInput_1.getMessage();
							System.err.println(errorMessage_tDataStewardshipTaskInput_1);
						}
					}
				}

				/**
				 * [tDataStewardshipTaskInput_1 finally ] stop
				 */

				/**
				 * [tFileOutputDelimited_1 finally ] start
				 */

				currentComponent = "tFileOutputDelimited_1";

				cLabel = "ValidCustomers";

				if (resourceMap.get("finish_tFileOutputDelimited_1") == null) {

					java.io.Writer outtFileOutputDelimited_1 = (java.io.Writer) resourceMap
							.get("out_tFileOutputDelimited_1");
					if (outtFileOutputDelimited_1 != null) {
						outtFileOutputDelimited_1.flush();
						outtFileOutputDelimited_1.close();
					}

				}

				/**
				 * [tFileOutputDelimited_1 finally ] stop
				 */

				/**
				 * [tLogRow_1 finally ] start
				 */

				currentComponent = "tLogRow_1";

				cLabel = "OutputConsole";

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

		globalMap.put("tDataStewardshipTaskInput_1_SUBPROCESS_STATE", 1);
	}

	public void talendJobLogProcess(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("talendJobLog_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdcInfo.forEach(org.slf4j.MDC::put);
		org.slf4j.MDC.put("_subJobName", "talendJobLog");
		org.slf4j.MDC.put("_subJobPid", "equb1P_" + subJobPidCounter.getAndIncrement());

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

	private final org.talend.components.common.runtime.SharedConnectionsPool connectionPool = new org.talend.components.common.runtime.SharedConnectionsPool() {
		public java.sql.Connection getDBConnection(String dbDriver, String url, String userName, String password,
				String dbConnectionName) throws ClassNotFoundException, java.sql.SQLException {
			return SharedDBConnection.getDBConnection(dbDriver, url, userName, password, dbConnectionName);
		}

		public java.sql.Connection getDBConnection(String dbDriver, String url, String dbConnectionName)
				throws ClassNotFoundException, java.sql.SQLException {
			return SharedDBConnection.getDBConnection(dbDriver, url, dbConnectionName);
		}
	};

	private static final String GLOBAL_CONNECTION_POOL_KEY = "GLOBAL_CONNECTION_POOL";

	{
		globalMap.put(GLOBAL_CONNECTION_POOL_KEY, connectionPool);
	}

	private final static java.util.Properties jobInfo = new java.util.Properties();
	private final static java.util.Map<String, String> mdcInfo = new java.util.HashMap<>();
	private final static java.util.concurrent.atomic.AtomicLong subJobPidCounter = new java.util.concurrent.atomic.AtomicLong();

	public static void main(String[] args) {
		final DataSteward DataStewardClass = new DataSteward();

		int exitCode = DataStewardClass.runJobInTOS(args);
		if (exitCode == 0) {
			log.info("TalendJob: 'DataSteward' - Done.");
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
		log.info("TalendJob: 'DataSteward' - Start.");

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
		org.slf4j.MDC.put("_jobRepositoryId", "_UXy2wILFEe6PMYwZwGxOMQ");
		org.slf4j.MDC.put("_compiledAtTimestamp", "2024-02-01T07:40:49.500746800Z");

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
			java.io.InputStream inContext = DataSteward.class.getClassLoader()
					.getResourceAsStream("democloud/datasteward_0_1/contexts/" + contextStr + ".properties");
			if (inContext == null) {
				inContext = DataSteward.class.getClassLoader()
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
		log.info("TalendJob: 'DataSteward' - Started.");
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
			tDataStewardshipTaskInput_1Process(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (TalendException e_tDataStewardshipTaskInput_1) {
			globalMap.put("tDataStewardshipTaskInput_1_SUBPROCESS_STATE", -1);

			e_tDataStewardshipTaskInput_1.printStackTrace();

		}

		this.globalResumeTicket = true;// to run tPostJob

		end = System.currentTimeMillis();

		if (watch) {
			System.out.println((end - startTime) + " milliseconds");
		}

		endUsedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		if (false) {
			System.out.println((endUsedMemory - startUsedMemory) + " bytes memory increase when running : DataSteward");
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
		log.info("TalendJob: 'DataSteward' - Finished - status: " + status + " returnCode: " + returnCode);

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
 * 192113 characters generated by Talend Cloud Data Fabric on the 1 February
 * 2024 at 3:40:49 PM SGT
 ************************************************************************************************/