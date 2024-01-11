
package democloud.file_s3_0_1;

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
 * Job: File_S3 Purpose: <br>
 * Description: <br>
 * 
 * @author Tan, Eric
 * @version 8.0.1.20231222_1430-patch
 * @status
 */
public class File_S3 implements TalendJob {
	static {
		System.setProperty("TalendJob.log", "File_S3.log");
	}

	private static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(File_S3.class);

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

			if (param_file_path != null) {

				this.setProperty("param_file_path", param_file_path.toString());

			}

		}

		// if the stored or passed value is "<TALEND_NULL>" string, it mean null
		public String getStringValue(String key) {
			String origin_value = this.getProperty(key);
			if (NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY.equals(origin_value)) {
				return null;
			}
			return origin_value;
		}

		public String param_file_path;

		public String getParam_file_path() {
			return this.param_file_path;
		}
	}

	protected ContextProperties context = new ContextProperties(); // will be instanciated by MS.

	public ContextProperties getContext() {
		return this.context;
	}

	private final String jobVersion = "0.1";
	private final String jobName = "File_S3";
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
			"_bi3FQIREEe6K79Ugdc6UFA", "0.1");
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
					File_S3.this.exception = e;
				}
			}
			if (!(e instanceof TalendException)) {
				try {
					for (java.lang.reflect.Method m : this.getClass().getEnclosingClass().getMethods()) {
						if (m.getName().compareTo(currentComponent + "_error") == 0) {
							m.invoke(File_S3.this, new Object[] { e, currentComponent, globalMap });
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

	public void tDBInput_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBInput_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tMap_1_error(Exception exception, String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBInput_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tFileOutputDelimited_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBInput_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tFileInputDelimited_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBInput_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tPrejob_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tPrejob_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tS3Connection_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tS3Connection_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tPostjob_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tPostjob_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tS3Put_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tS3Put_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tAdvancedHash_row2_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBInput_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void talendJobLog_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		talendJobLog_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDBInput_1_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tPrejob_1_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tS3Connection_1_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tPostjob_1_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tS3Put_1_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void talendJobLog_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public static class CustomersOutStruct implements routines.system.IPersistableRow<CustomersOutStruct> {
		final static byte[] commonByteArrayLock_DEMOCLOUD_File_S3 = new byte[0];
		static byte[] commonByteArray_DEMOCLOUD_File_S3 = new byte[0];

		public Integer Id;

		public Integer getId() {
			return this.Id;
		}

		public Boolean IdIsNullable() {
			return true;
		}

		public Boolean IdIsKey() {
			return false;
		}

		public Integer IdLength() {
			return 10;
		}

		public Integer IdPrecision() {
			return 0;
		}

		public String IdDefault() {

			return null;

		}

		public String IdComment() {

			return "";

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
			return true;
		}

		public Boolean First_NameIsKey() {
			return false;
		}

		public Integer First_NameLength() {
			return 100;
		}

		public Integer First_NamePrecision() {
			return 0;
		}

		public String First_NameDefault() {

			return null;

		}

		public String First_NameComment() {

			return "";

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
			return true;
		}

		public Boolean Last_NameIsKey() {
			return false;
		}

		public Integer Last_NameLength() {
			return 100;
		}

		public Integer Last_NamePrecision() {
			return 0;
		}

		public String Last_NameDefault() {

			return null;

		}

		public String Last_NameComment() {

			return "";

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
			return true;
		}

		public Boolean GenderIsKey() {
			return false;
		}

		public Integer GenderLength() {
			return 10;
		}

		public Integer GenderPrecision() {
			return 0;
		}

		public String GenderDefault() {

			return null;

		}

		public String GenderComment() {

			return "";

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
			return true;
		}

		public Boolean AgeIsKey() {
			return false;
		}

		public Integer AgeLength() {
			return 100;
		}

		public Integer AgePrecision() {
			return 0;
		}

		public String AgeDefault() {

			return null;

		}

		public String AgeComment() {

			return "";

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
			return true;
		}

		public Boolean OccupationIsKey() {
			return false;
		}

		public Integer OccupationLength() {
			return 100;
		}

		public Integer OccupationPrecision() {
			return 0;
		}

		public String OccupationDefault() {

			return null;

		}

		public String OccupationComment() {

			return "";

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
			return true;
		}

		public Boolean MaritalStatusIsKey() {
			return false;
		}

		public Integer MaritalStatusLength() {
			return 100;
		}

		public Integer MaritalStatusPrecision() {
			return 0;
		}

		public String MaritalStatusDefault() {

			return null;

		}

		public String MaritalStatusComment() {

			return "";

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
			return true;
		}

		public Boolean SalaryIsKey() {
			return false;
		}

		public Integer SalaryLength() {
			return 100;
		}

		public Integer SalaryPrecision() {
			return 0;
		}

		public String SalaryDefault() {

			return null;

		}

		public String SalaryComment() {

			return "";

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
			return true;
		}

		public Boolean AddressIsKey() {
			return false;
		}

		public Integer AddressLength() {
			return 100;
		}

		public Integer AddressPrecision() {
			return 0;
		}

		public String AddressDefault() {

			return null;

		}

		public String AddressComment() {

			return "";

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
			return true;
		}

		public Boolean CityIsKey() {
			return false;
		}

		public Integer CityLength() {
			return 100;
		}

		public Integer CityPrecision() {
			return 0;
		}

		public String CityDefault() {

			return null;

		}

		public String CityComment() {

			return "";

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
			return true;
		}

		public Boolean StateIsKey() {
			return false;
		}

		public Integer StateLength() {
			return 100;
		}

		public Integer StatePrecision() {
			return 0;
		}

		public String StateDefault() {

			return null;

		}

		public String StateComment() {

			return "";

		}

		public String StatePattern() {

			return "";

		}

		public String StateOriginalDbColumnName() {

			return "State";

		}

		public String Zip;

		public String getZip() {
			return this.Zip;
		}

		public Boolean ZipIsNullable() {
			return true;
		}

		public Boolean ZipIsKey() {
			return false;
		}

		public Integer ZipLength() {
			return 100;
		}

		public Integer ZipPrecision() {
			return 0;
		}

		public String ZipDefault() {

			return null;

		}

		public String ZipComment() {

			return "";

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
			return true;
		}

		public Boolean PhoneIsKey() {
			return false;
		}

		public Integer PhoneLength() {
			return 100;
		}

		public Integer PhonePrecision() {
			return 0;
		}

		public String PhoneDefault() {

			return null;

		}

		public String PhoneComment() {

			return "";

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
			return true;
		}

		public Boolean EmailIsKey() {
			return false;
		}

		public Integer EmailLength() {
			return 29;
		}

		public Integer EmailPrecision() {
			return 0;
		}

		public String EmailDefault() {

			return null;

		}

		public String EmailComment() {

			return "";

		}

		public String EmailPattern() {

			return "dd-MM-yyyy";

		}

		public String EmailOriginalDbColumnName() {

			return "Email";

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

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DEMOCLOUD_File_S3.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_File_S3.length == 0) {
						commonByteArray_DEMOCLOUD_File_S3 = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_File_S3 = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DEMOCLOUD_File_S3, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_File_S3, 0, length, utf8Charset);
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
				if (length > commonByteArray_DEMOCLOUD_File_S3.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_File_S3.length == 0) {
						commonByteArray_DEMOCLOUD_File_S3 = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_File_S3 = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DEMOCLOUD_File_S3, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_File_S3, 0, length, utf8Charset);
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

			synchronized (commonByteArrayLock_DEMOCLOUD_File_S3) {

				try {

					int length = 0;

					this.Id = readInteger(dis);

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

					this.Zip = readString(dis);

					this.Phone = readString(dis);

					this.Email = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_DEMOCLOUD_File_S3) {

				try {

					int length = 0;

					this.Id = readInteger(dis);

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

					this.Zip = readString(dis);

					this.Phone = readString(dis);

					this.Email = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// Integer

				writeInteger(this.Id, dos);

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

				// String

				writeString(this.Zip, dos);

				// String

				writeString(this.Phone, dos);

				// String

				writeString(this.Email, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// Integer

				writeInteger(this.Id, dos);

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

				// String

				writeString(this.Zip, dos);

				// String

				writeString(this.Phone, dos);

				// String

				writeString(this.Email, dos);

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
			sb.append(",Zip=" + Zip);
			sb.append(",Phone=" + Phone);
			sb.append(",Email=" + Email);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (Id == null) {
				sb.append("<null>");
			} else {
				sb.append(Id);
			}

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

			if (Zip == null) {
				sb.append("<null>");
			} else {
				sb.append(Zip);
			}

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

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(CustomersOutStruct other) {

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
		final static byte[] commonByteArrayLock_DEMOCLOUD_File_S3 = new byte[0];
		static byte[] commonByteArray_DEMOCLOUD_File_S3 = new byte[0];

		public Integer Id;

		public Integer getId() {
			return this.Id;
		}

		public Boolean IdIsNullable() {
			return true;
		}

		public Boolean IdIsKey() {
			return false;
		}

		public Integer IdLength() {
			return 10;
		}

		public Integer IdPrecision() {
			return 0;
		}

		public String IdDefault() {

			return null;

		}

		public String IdComment() {

			return "";

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
			return true;
		}

		public Boolean First_NameIsKey() {
			return false;
		}

		public Integer First_NameLength() {
			return 100;
		}

		public Integer First_NamePrecision() {
			return 0;
		}

		public String First_NameDefault() {

			return null;

		}

		public String First_NameComment() {

			return "";

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
			return true;
		}

		public Boolean Last_NameIsKey() {
			return false;
		}

		public Integer Last_NameLength() {
			return 100;
		}

		public Integer Last_NamePrecision() {
			return 0;
		}

		public String Last_NameDefault() {

			return null;

		}

		public String Last_NameComment() {

			return "";

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
			return true;
		}

		public Boolean GenderIsKey() {
			return false;
		}

		public Integer GenderLength() {
			return 10;
		}

		public Integer GenderPrecision() {
			return 0;
		}

		public String GenderDefault() {

			return null;

		}

		public String GenderComment() {

			return "";

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
			return true;
		}

		public Boolean AgeIsKey() {
			return false;
		}

		public Integer AgeLength() {
			return 100;
		}

		public Integer AgePrecision() {
			return 0;
		}

		public String AgeDefault() {

			return null;

		}

		public String AgeComment() {

			return "";

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
			return true;
		}

		public Boolean OccupationIsKey() {
			return false;
		}

		public Integer OccupationLength() {
			return 100;
		}

		public Integer OccupationPrecision() {
			return 0;
		}

		public String OccupationDefault() {

			return null;

		}

		public String OccupationComment() {

			return "";

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
			return true;
		}

		public Boolean MaritalStatusIsKey() {
			return false;
		}

		public Integer MaritalStatusLength() {
			return 100;
		}

		public Integer MaritalStatusPrecision() {
			return 0;
		}

		public String MaritalStatusDefault() {

			return null;

		}

		public String MaritalStatusComment() {

			return "";

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
			return true;
		}

		public Boolean SalaryIsKey() {
			return false;
		}

		public Integer SalaryLength() {
			return 100;
		}

		public Integer SalaryPrecision() {
			return 0;
		}

		public String SalaryDefault() {

			return null;

		}

		public String SalaryComment() {

			return "";

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
			return true;
		}

		public Boolean AddressIsKey() {
			return false;
		}

		public Integer AddressLength() {
			return 100;
		}

		public Integer AddressPrecision() {
			return 0;
		}

		public String AddressDefault() {

			return null;

		}

		public String AddressComment() {

			return "";

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
			return true;
		}

		public Boolean CityIsKey() {
			return false;
		}

		public Integer CityLength() {
			return 100;
		}

		public Integer CityPrecision() {
			return 0;
		}

		public String CityDefault() {

			return null;

		}

		public String CityComment() {

			return "";

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
			return true;
		}

		public Boolean StateIsKey() {
			return false;
		}

		public Integer StateLength() {
			return 100;
		}

		public Integer StatePrecision() {
			return 0;
		}

		public String StateDefault() {

			return null;

		}

		public String StateComment() {

			return "";

		}

		public String StatePattern() {

			return "";

		}

		public String StateOriginalDbColumnName() {

			return "State";

		}

		public String Zip;

		public String getZip() {
			return this.Zip;
		}

		public Boolean ZipIsNullable() {
			return true;
		}

		public Boolean ZipIsKey() {
			return false;
		}

		public Integer ZipLength() {
			return 100;
		}

		public Integer ZipPrecision() {
			return 0;
		}

		public String ZipDefault() {

			return null;

		}

		public String ZipComment() {

			return "";

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
			return true;
		}

		public Boolean PhoneIsKey() {
			return false;
		}

		public Integer PhoneLength() {
			return 100;
		}

		public Integer PhonePrecision() {
			return 0;
		}

		public String PhoneDefault() {

			return null;

		}

		public String PhoneComment() {

			return "";

		}

		public String PhonePattern() {

			return "";

		}

		public String PhoneOriginalDbColumnName() {

			return "Phone";

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

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DEMOCLOUD_File_S3.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_File_S3.length == 0) {
						commonByteArray_DEMOCLOUD_File_S3 = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_File_S3 = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DEMOCLOUD_File_S3, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_File_S3, 0, length, utf8Charset);
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
				if (length > commonByteArray_DEMOCLOUD_File_S3.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_File_S3.length == 0) {
						commonByteArray_DEMOCLOUD_File_S3 = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_File_S3 = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DEMOCLOUD_File_S3, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_File_S3, 0, length, utf8Charset);
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

			synchronized (commonByteArrayLock_DEMOCLOUD_File_S3) {

				try {

					int length = 0;

					this.Id = readInteger(dis);

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

					this.Zip = readString(dis);

					this.Phone = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_DEMOCLOUD_File_S3) {

				try {

					int length = 0;

					this.Id = readInteger(dis);

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

					this.Zip = readString(dis);

					this.Phone = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// Integer

				writeInteger(this.Id, dos);

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

				// String

				writeString(this.Zip, dos);

				// String

				writeString(this.Phone, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// Integer

				writeInteger(this.Id, dos);

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

				// String

				writeString(this.Zip, dos);

				// String

				writeString(this.Phone, dos);

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
			sb.append(",Zip=" + Zip);
			sb.append(",Phone=" + Phone);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (Id == null) {
				sb.append("<null>");
			} else {
				sb.append(Id);
			}

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

			if (Zip == null) {
				sb.append("<null>");
			} else {
				sb.append(Zip);
			}

			sb.append("|");

			if (Phone == null) {
				sb.append("<null>");
			} else {
				sb.append(Phone);
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

	public static class after_tDBInput_1Struct implements routines.system.IPersistableRow<after_tDBInput_1Struct> {
		final static byte[] commonByteArrayLock_DEMOCLOUD_File_S3 = new byte[0];
		static byte[] commonByteArray_DEMOCLOUD_File_S3 = new byte[0];

		public Integer Id;

		public Integer getId() {
			return this.Id;
		}

		public Boolean IdIsNullable() {
			return true;
		}

		public Boolean IdIsKey() {
			return false;
		}

		public Integer IdLength() {
			return 10;
		}

		public Integer IdPrecision() {
			return 0;
		}

		public String IdDefault() {

			return null;

		}

		public String IdComment() {

			return "";

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
			return true;
		}

		public Boolean First_NameIsKey() {
			return false;
		}

		public Integer First_NameLength() {
			return 100;
		}

		public Integer First_NamePrecision() {
			return 0;
		}

		public String First_NameDefault() {

			return null;

		}

		public String First_NameComment() {

			return "";

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
			return true;
		}

		public Boolean Last_NameIsKey() {
			return false;
		}

		public Integer Last_NameLength() {
			return 100;
		}

		public Integer Last_NamePrecision() {
			return 0;
		}

		public String Last_NameDefault() {

			return null;

		}

		public String Last_NameComment() {

			return "";

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
			return true;
		}

		public Boolean GenderIsKey() {
			return false;
		}

		public Integer GenderLength() {
			return 10;
		}

		public Integer GenderPrecision() {
			return 0;
		}

		public String GenderDefault() {

			return null;

		}

		public String GenderComment() {

			return "";

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
			return true;
		}

		public Boolean AgeIsKey() {
			return false;
		}

		public Integer AgeLength() {
			return 100;
		}

		public Integer AgePrecision() {
			return 0;
		}

		public String AgeDefault() {

			return null;

		}

		public String AgeComment() {

			return "";

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
			return true;
		}

		public Boolean OccupationIsKey() {
			return false;
		}

		public Integer OccupationLength() {
			return 100;
		}

		public Integer OccupationPrecision() {
			return 0;
		}

		public String OccupationDefault() {

			return null;

		}

		public String OccupationComment() {

			return "";

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
			return true;
		}

		public Boolean MaritalStatusIsKey() {
			return false;
		}

		public Integer MaritalStatusLength() {
			return 100;
		}

		public Integer MaritalStatusPrecision() {
			return 0;
		}

		public String MaritalStatusDefault() {

			return null;

		}

		public String MaritalStatusComment() {

			return "";

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
			return true;
		}

		public Boolean SalaryIsKey() {
			return false;
		}

		public Integer SalaryLength() {
			return 100;
		}

		public Integer SalaryPrecision() {
			return 0;
		}

		public String SalaryDefault() {

			return null;

		}

		public String SalaryComment() {

			return "";

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
			return true;
		}

		public Boolean AddressIsKey() {
			return false;
		}

		public Integer AddressLength() {
			return 100;
		}

		public Integer AddressPrecision() {
			return 0;
		}

		public String AddressDefault() {

			return null;

		}

		public String AddressComment() {

			return "";

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
			return true;
		}

		public Boolean CityIsKey() {
			return false;
		}

		public Integer CityLength() {
			return 100;
		}

		public Integer CityPrecision() {
			return 0;
		}

		public String CityDefault() {

			return null;

		}

		public String CityComment() {

			return "";

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
			return true;
		}

		public Boolean StateIsKey() {
			return false;
		}

		public Integer StateLength() {
			return 100;
		}

		public Integer StatePrecision() {
			return 0;
		}

		public String StateDefault() {

			return null;

		}

		public String StateComment() {

			return "";

		}

		public String StatePattern() {

			return "";

		}

		public String StateOriginalDbColumnName() {

			return "State";

		}

		public String Zip;

		public String getZip() {
			return this.Zip;
		}

		public Boolean ZipIsNullable() {
			return true;
		}

		public Boolean ZipIsKey() {
			return false;
		}

		public Integer ZipLength() {
			return 100;
		}

		public Integer ZipPrecision() {
			return 0;
		}

		public String ZipDefault() {

			return null;

		}

		public String ZipComment() {

			return "";

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
			return true;
		}

		public Boolean PhoneIsKey() {
			return false;
		}

		public Integer PhoneLength() {
			return 100;
		}

		public Integer PhonePrecision() {
			return 0;
		}

		public String PhoneDefault() {

			return null;

		}

		public String PhoneComment() {

			return "";

		}

		public String PhonePattern() {

			return "";

		}

		public String PhoneOriginalDbColumnName() {

			return "Phone";

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

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DEMOCLOUD_File_S3.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_File_S3.length == 0) {
						commonByteArray_DEMOCLOUD_File_S3 = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_File_S3 = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DEMOCLOUD_File_S3, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_File_S3, 0, length, utf8Charset);
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
				if (length > commonByteArray_DEMOCLOUD_File_S3.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_File_S3.length == 0) {
						commonByteArray_DEMOCLOUD_File_S3 = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_File_S3 = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DEMOCLOUD_File_S3, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_File_S3, 0, length, utf8Charset);
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

			synchronized (commonByteArrayLock_DEMOCLOUD_File_S3) {

				try {

					int length = 0;

					this.Id = readInteger(dis);

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

					this.Zip = readString(dis);

					this.Phone = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_DEMOCLOUD_File_S3) {

				try {

					int length = 0;

					this.Id = readInteger(dis);

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

					this.Zip = readString(dis);

					this.Phone = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// Integer

				writeInteger(this.Id, dos);

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

				// String

				writeString(this.Zip, dos);

				// String

				writeString(this.Phone, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// Integer

				writeInteger(this.Id, dos);

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

				// String

				writeString(this.Zip, dos);

				// String

				writeString(this.Phone, dos);

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
			sb.append(",Zip=" + Zip);
			sb.append(",Phone=" + Phone);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (Id == null) {
				sb.append("<null>");
			} else {
				sb.append(Id);
			}

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

			if (Zip == null) {
				sb.append("<null>");
			} else {
				sb.append(Zip);
			}

			sb.append("|");

			if (Phone == null) {
				sb.append("<null>");
			} else {
				sb.append(Phone);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(after_tDBInput_1Struct other) {

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

	public void tDBInput_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tDBInput_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdcInfo.forEach(org.slf4j.MDC::put);
		org.slf4j.MDC.put("_subJobName", "tDBInput_1");
		org.slf4j.MDC.put("_subJobPid", "AMJYYR_" + subJobPidCounter.getAndIncrement());

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

				tFileInputDelimited_1Process(globalMap);

				row1Struct row1 = new row1Struct();
				CustomersOutStruct CustomersOut = new CustomersOutStruct();

				/**
				 * [tFileOutputDelimited_1 begin ] start
				 */

				ok_Hash.put("tFileOutputDelimited_1", false);
				start_Hash.put("tFileOutputDelimited_1", System.currentTimeMillis());

				currentComponent = "tFileOutputDelimited_1";

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "CustomersOut");

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
									"FILENAME" + " = " + "context.param_file_path + \"/\" + jobName + \"/out.csv\"");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("ROWSEPARATOR" + " = " + "\"\\n\"");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("FIELDSEPARATOR" + " = " + "\";\"");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("APPEND" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("INCLUDEHEADER" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("COMPRESS" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("ADVANCED_SEPARATOR" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("CSV_OPTION" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("CREATE" + " = " + "true");
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
							log4jParamters_tFileOutputDelimited_1.append("FILE_EXIST_EXCEPTION" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tFileOutputDelimited_1 - " + (log4jParamters_tFileOutputDelimited_1));
						}
					}
					new BytesLimit65535_tFileOutputDelimited_1().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tFileOutputDelimited_1", "tFileOutputDelimited_1", "tFileOutputDelimited");
					talendJobLogProcess(globalMap);
				}

				String fileName_tFileOutputDelimited_1 = "";
				fileName_tFileOutputDelimited_1 = (new java.io.File(
						context.param_file_path + "/" + jobName + "/out.csv")).getAbsolutePath().replace("\\", "/");
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

				// create directory only if not exists
				if (directory_tFileOutputDelimited_1 != null && directory_tFileOutputDelimited_1.trim().length() != 0) {
					java.io.File dir_tFileOutputDelimited_1 = new java.io.File(directory_tFileOutputDelimited_1);
					if (!dir_tFileOutputDelimited_1.exists()) {
						log.info("tFileOutputDelimited_1 - Creating directory '"
								+ dir_tFileOutputDelimited_1.getCanonicalPath() + "'.");
						dir_tFileOutputDelimited_1.mkdirs();
						log.info("tFileOutputDelimited_1 - The directory '"
								+ dir_tFileOutputDelimited_1.getCanonicalPath() + "' has been created successfully.");
					}
				}

				// routines.system.Row
				java.io.Writer outtFileOutputDelimited_1 = null;

				java.io.File fileToDelete_tFileOutputDelimited_1 = new java.io.File(fileName_tFileOutputDelimited_1);
				if (fileToDelete_tFileOutputDelimited_1.exists()) {
					fileToDelete_tFileOutputDelimited_1.delete();
				}
				outtFileOutputDelimited_1 = new java.io.BufferedWriter(new java.io.OutputStreamWriter(
						new java.io.FileOutputStream(fileName_tFileOutputDelimited_1, false), "ISO-8859-15"));
				resourceMap.put("out_tFileOutputDelimited_1", outtFileOutputDelimited_1);

				resourceMap.put("nb_line_tFileOutputDelimited_1", nb_line_tFileOutputDelimited_1);

				/**
				 * [tFileOutputDelimited_1 begin ] stop
				 */

				/**
				 * [tMap_1 begin ] start
				 */

				ok_Hash.put("tMap_1", false);
				start_Hash.put("tMap_1", System.currentTimeMillis());

				currentComponent = "tMap_1";

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row1");

				int tos_count_tMap_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tMap_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tMap_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tMap_1 = new StringBuilder();
							log4jParamters_tMap_1.append("Parameters:");
							log4jParamters_tMap_1.append("LINK_STYLE" + " = " + "AUTO");
							log4jParamters_tMap_1.append(" | ");
							log4jParamters_tMap_1.append("TEMPORARY_DATA_DIRECTORY" + " = " + "");
							log4jParamters_tMap_1.append(" | ");
							log4jParamters_tMap_1.append("ROWS_BUFFER_SIZE" + " = " + "2000000");
							log4jParamters_tMap_1.append(" | ");
							log4jParamters_tMap_1.append("CHANGE_HASH_AND_EQUALS_FOR_BIGDECIMAL" + " = " + "true");
							log4jParamters_tMap_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tMap_1 - " + (log4jParamters_tMap_1));
						}
					}
					new BytesLimit65535_tMap_1().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tMap_1", "tMap_1", "tMap");
					talendJobLogProcess(globalMap);
				}

// ###############################
// # Lookup's keys initialization
				int count_row1_tMap_1 = 0;

				int count_row2_tMap_1 = 0;

				org.talend.designer.components.lookup.memory.AdvancedMemoryLookup<row2Struct> tHash_Lookup_row2 = (org.talend.designer.components.lookup.memory.AdvancedMemoryLookup<row2Struct>) ((org.talend.designer.components.lookup.memory.AdvancedMemoryLookup<row2Struct>) globalMap
						.get("tHash_Lookup_row2"));

				row2Struct row2HashKey = new row2Struct();
				row2Struct row2Default = new row2Struct();
// ###############################        

// ###############################
// # Vars initialization
				class Var__tMap_1__Struct {
				}
				Var__tMap_1__Struct Var__tMap_1 = new Var__tMap_1__Struct();
// ###############################

// ###############################
// # Outputs initialization
				int count_CustomersOut_tMap_1 = 0;

				CustomersOutStruct CustomersOut_tmp = new CustomersOutStruct();
// ###############################

				/**
				 * [tMap_1 begin ] stop
				 */

				/**
				 * [tDBInput_1 begin ] start
				 */

				ok_Hash.put("tDBInput_1", false);
				start_Hash.put("tDBInput_1", System.currentTimeMillis());

				currentComponent = "tDBInput_1";

				cLabel = "\"customers\"";

				int tos_count_tDBInput_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tDBInput_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tDBInput_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tDBInput_1 = new StringBuilder();
							log4jParamters_tDBInput_1.append("Parameters:");
							log4jParamters_tDBInput_1.append("DB_VERSION" + " = " + "MYSQL_8");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("USE_EXISTING_CONNECTION" + " = " + "false");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("HOST" + " = " + "\"localhost\"");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("PORT" + " = " + "\"3306\"");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("DBNAME" + " = " + "\"talend\"");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("USER" + " = " + "\"root\"");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("PASS" + " = " + String.valueOf(
									"enc:routine.encryption.key.v1:MBcyMZqqGlRcdSmDiil1eZdpEjr3pL0ZI4TC6cFzkJov4eNdJA==")
									.substring(0, 4) + "...");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("TABLE" + " = " + "\"customers\"");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("QUERYSTORE" + " = " + "\"\"");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("QUERY" + " = "
									+ "\"SELECT    `customers`.`Id`,    `customers`.`First_Name`,    `customers`.`Last_Name`,    `customers`.`Gender`,    `customers`.`Age`,    `customers`.`Occupation`,    `customers`.`MaritalStatus`,    `customers`.`Salary`,    `customers`.`Address`,    `customers`.`City`,    `customers`.`State`,    `customers`.`Zip`,    `customers`.`Phone` FROM `customers`\"");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("SPECIFY_DATASOURCE_ALIAS" + " = " + "false");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("PROPERTIES" + " = "
									+ "\"noDatetimeStringSync=true&enabledTLSProtocols=TLSv1.2,TLSv1.1,TLSv1\"");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("ENABLE_STREAM" + " = " + "false");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("TRIM_ALL_COLUMN" + " = " + "false");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("TRIM_COLUMN" + " = " + "[{TRIM=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("Id") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN="
									+ ("First_Name") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN=" + ("Last_Name")
									+ "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN=" + ("Gender") + "}, {TRIM="
									+ ("false") + ", SCHEMA_COLUMN=" + ("Age") + "}, {TRIM=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("Occupation") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN="
									+ ("MaritalStatus") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN=" + ("Salary")
									+ "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN=" + ("Address") + "}, {TRIM="
									+ ("false") + ", SCHEMA_COLUMN=" + ("City") + "}, {TRIM=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("State") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN="
									+ ("Zip") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN=" + ("Phone") + "}]");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("UNIFIED_COMPONENTS" + " = " + "tMysqlInput");
							log4jParamters_tDBInput_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tDBInput_1 - " + (log4jParamters_tDBInput_1));
						}
					}
					new BytesLimit65535_tDBInput_1().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tDBInput_1", "\"customers\"", "tMysqlInput");
					talendJobLogProcess(globalMap);
				}

				java.util.Calendar calendar_tDBInput_1 = java.util.Calendar.getInstance();
				calendar_tDBInput_1.set(0, 0, 0, 0, 0, 0);
				java.util.Date year0_tDBInput_1 = calendar_tDBInput_1.getTime();
				int nb_line_tDBInput_1 = 0;
				java.sql.Connection conn_tDBInput_1 = null;
				String driverClass_tDBInput_1 = "com.mysql.cj.jdbc.Driver";
				java.lang.Class jdbcclazz_tDBInput_1 = java.lang.Class.forName(driverClass_tDBInput_1);
				String dbUser_tDBInput_1 = "root";

				final String decryptedPassword_tDBInput_1 = routines.system.PasswordEncryptUtil.decryptPassword(
						"enc:routine.encryption.key.v1:cc71lGOFC8I8+wLUwvpzKm+8Yeq8Fri8okuuECUEgFKErmdt0w==");

				String dbPwd_tDBInput_1 = decryptedPassword_tDBInput_1;

				String properties_tDBInput_1 = "noDatetimeStringSync=true&enabledTLSProtocols=TLSv1.2,TLSv1.1,TLSv1";
				if (properties_tDBInput_1 == null || properties_tDBInput_1.trim().length() == 0) {
					properties_tDBInput_1 = "";
				}
				String url_tDBInput_1 = "jdbc:mysql://" + "localhost" + ":" + "3306" + "/" + "talend" + "?"
						+ properties_tDBInput_1;

				log.debug("tDBInput_1 - Driver ClassName: " + driverClass_tDBInput_1 + ".");

				log.debug("tDBInput_1 - Connection attempt to '" + url_tDBInput_1 + "' with the username '"
						+ dbUser_tDBInput_1 + "'.");

				conn_tDBInput_1 = java.sql.DriverManager.getConnection(url_tDBInput_1, dbUser_tDBInput_1,
						dbPwd_tDBInput_1);
				log.debug("tDBInput_1 - Connection to '" + url_tDBInput_1 + "' has succeeded.");

				java.sql.Statement stmt_tDBInput_1 = conn_tDBInput_1.createStatement();

				String dbquery_tDBInput_1 = "SELECT \n  `customers`.`Id`, \n  `customers`.`First_Name`, \n  `customers`.`Last_Name`, \n  `customers`.`Gender`, \n  `custo"
						+ "mers`.`Age`, \n  `customers`.`Occupation`, \n  `customers`.`MaritalStatus`, \n  `customers`.`Salary`, \n  `customers`.`Addre"
						+ "ss`, \n  `customers`.`City`, \n  `customers`.`State`, \n  `customers`.`Zip`, \n  `customers`.`Phone`\nFROM `customers`";

				log.debug("tDBInput_1 - Executing the query: '" + dbquery_tDBInput_1 + "'.");

				globalMap.put("tDBInput_1_QUERY", dbquery_tDBInput_1);

				java.sql.ResultSet rs_tDBInput_1 = null;

				try {
					rs_tDBInput_1 = stmt_tDBInput_1.executeQuery(dbquery_tDBInput_1);
					java.sql.ResultSetMetaData rsmd_tDBInput_1 = rs_tDBInput_1.getMetaData();
					int colQtyInRs_tDBInput_1 = rsmd_tDBInput_1.getColumnCount();

					String tmpContent_tDBInput_1 = null;

					log.debug("tDBInput_1 - Retrieving records from the database.");

					while (rs_tDBInput_1.next()) {
						nb_line_tDBInput_1++;

						if (colQtyInRs_tDBInput_1 < 1) {
							row1.Id = null;
						} else {

							row1.Id = rs_tDBInput_1.getInt(1);
							if (rs_tDBInput_1.wasNull()) {
								row1.Id = null;
							}
						}
						if (colQtyInRs_tDBInput_1 < 2) {
							row1.First_Name = null;
						} else {

							row1.First_Name = routines.system.JDBCUtil.getString(rs_tDBInput_1, 2, false);
						}
						if (colQtyInRs_tDBInput_1 < 3) {
							row1.Last_Name = null;
						} else {

							row1.Last_Name = routines.system.JDBCUtil.getString(rs_tDBInput_1, 3, false);
						}
						if (colQtyInRs_tDBInput_1 < 4) {
							row1.Gender = null;
						} else {

							row1.Gender = routines.system.JDBCUtil.getString(rs_tDBInput_1, 4, false);
						}
						if (colQtyInRs_tDBInput_1 < 5) {
							row1.Age = null;
						} else {

							row1.Age = routines.system.JDBCUtil.getString(rs_tDBInput_1, 5, false);
						}
						if (colQtyInRs_tDBInput_1 < 6) {
							row1.Occupation = null;
						} else {

							row1.Occupation = routines.system.JDBCUtil.getString(rs_tDBInput_1, 6, false);
						}
						if (colQtyInRs_tDBInput_1 < 7) {
							row1.MaritalStatus = null;
						} else {

							row1.MaritalStatus = routines.system.JDBCUtil.getString(rs_tDBInput_1, 7, false);
						}
						if (colQtyInRs_tDBInput_1 < 8) {
							row1.Salary = null;
						} else {

							row1.Salary = routines.system.JDBCUtil.getString(rs_tDBInput_1, 8, false);
						}
						if (colQtyInRs_tDBInput_1 < 9) {
							row1.Address = null;
						} else {

							row1.Address = routines.system.JDBCUtil.getString(rs_tDBInput_1, 9, false);
						}
						if (colQtyInRs_tDBInput_1 < 10) {
							row1.City = null;
						} else {

							row1.City = routines.system.JDBCUtil.getString(rs_tDBInput_1, 10, false);
						}
						if (colQtyInRs_tDBInput_1 < 11) {
							row1.State = null;
						} else {

							row1.State = routines.system.JDBCUtil.getString(rs_tDBInput_1, 11, false);
						}
						if (colQtyInRs_tDBInput_1 < 12) {
							row1.Zip = null;
						} else {

							row1.Zip = routines.system.JDBCUtil.getString(rs_tDBInput_1, 12, false);
						}
						if (colQtyInRs_tDBInput_1 < 13) {
							row1.Phone = null;
						} else {

							row1.Phone = routines.system.JDBCUtil.getString(rs_tDBInput_1, 13, false);
						}

						log.debug("tDBInput_1 - Retrieving the record " + nb_line_tDBInput_1 + ".");

						/**
						 * [tDBInput_1 begin ] stop
						 */

						/**
						 * [tDBInput_1 main ] start
						 */

						currentComponent = "tDBInput_1";

						cLabel = "\"customers\"";

						tos_count_tDBInput_1++;

						/**
						 * [tDBInput_1 main ] stop
						 */

						/**
						 * [tDBInput_1 process_data_begin ] start
						 */

						currentComponent = "tDBInput_1";

						cLabel = "\"customers\"";

						/**
						 * [tDBInput_1 process_data_begin ] stop
						 */

						/**
						 * [tMap_1 main ] start
						 */

						currentComponent = "tMap_1";

						if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

								, "row1", "tDBInput_1", "\"customers\"", "tMysqlInput", "tMap_1", "tMap_1", "tMap"

						)) {
							talendJobLogProcess(globalMap);
						}

						if (log.isTraceEnabled()) {
							log.trace("row1 - " + (row1 == null ? "" : row1.toLogString()));
						}

						boolean hasCasePrimitiveKeyWithNull_tMap_1 = false;

						row2Struct row2 = null;

						// ###############################
						// # Input tables (lookups)

						boolean rejectedInnerJoin_tMap_1 = false;
						boolean mainRowRejected_tMap_1 = false;

						///////////////////////////////////////////////
						// Starting Lookup Table "row2"
						///////////////////////////////////////////////

						boolean forceLooprow2 = false;

						row2Struct row2ObjectFromLookup = null;

						if (!rejectedInnerJoin_tMap_1) { // G_TM_M_020

							hasCasePrimitiveKeyWithNull_tMap_1 = false;

							row2HashKey.Id = row1.Id;

							row2HashKey.hashCodeDirty = true;

							tHash_Lookup_row2.lookup(row2HashKey);

						} // G_TM_M_020

						if (tHash_Lookup_row2 != null && tHash_Lookup_row2.getCount(row2HashKey) > 1) { // G 071

							// System.out.println("WARNING: UNIQUE MATCH is configured for the lookup 'row2'
							// and it contains more one result from keys : row2.Id = '" + row2HashKey.Id +
							// "'");
						} // G 071

						row2Struct fromLookup_row2 = null;
						row2 = row2Default;

						if (tHash_Lookup_row2 != null && tHash_Lookup_row2.hasNext()) { // G 099

							fromLookup_row2 = tHash_Lookup_row2.next();

						} // G 099

						if (fromLookup_row2 != null) {
							row2 = fromLookup_row2;
						}

						// ###############################
						{ // start of Var scope

							// ###############################
							// # Vars tables

							Var__tMap_1__Struct Var = Var__tMap_1;// ###############################
							// ###############################
							// # Output tables

							CustomersOut = null;

// # Output table : 'CustomersOut'
							count_CustomersOut_tMap_1++;

							CustomersOut_tmp.Id = row1.Id;
							CustomersOut_tmp.First_Name = row1.First_Name;
							CustomersOut_tmp.Last_Name = row1.Last_Name;
							CustomersOut_tmp.Gender = row1.Gender;
							CustomersOut_tmp.Age = row1.Age;
							CustomersOut_tmp.Occupation = row1.Occupation;
							CustomersOut_tmp.MaritalStatus = row1.MaritalStatus;
							CustomersOut_tmp.Salary = DataMasking.createMD5(row1.Salary);
							CustomersOut_tmp.Address = row1.Address;
							CustomersOut_tmp.City = row1.City;
							CustomersOut_tmp.State = row1.State;
							CustomersOut_tmp.Zip = row1.Zip;
							CustomersOut_tmp.Phone = row1.Phone;
							CustomersOut_tmp.Email = row2.Email;
							CustomersOut = CustomersOut_tmp;
							log.debug("tMap_1 - Outputting the record " + count_CustomersOut_tMap_1
									+ " of the output table 'CustomersOut'.");

// ###############################

						} // end of Var scope

						rejectedInnerJoin_tMap_1 = false;

						tos_count_tMap_1++;

						/**
						 * [tMap_1 main ] stop
						 */

						/**
						 * [tMap_1 process_data_begin ] start
						 */

						currentComponent = "tMap_1";

						/**
						 * [tMap_1 process_data_begin ] stop
						 */
// Start of branch "CustomersOut"
						if (CustomersOut != null) {

							/**
							 * [tFileOutputDelimited_1 main ] start
							 */

							currentComponent = "tFileOutputDelimited_1";

							if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

									, "CustomersOut", "tMap_1", "tMap_1", "tMap", "tFileOutputDelimited_1",
									"tFileOutputDelimited_1", "tFileOutputDelimited"

							)) {
								talendJobLogProcess(globalMap);
							}

							if (log.isTraceEnabled()) {
								log.trace("CustomersOut - " + (CustomersOut == null ? "" : CustomersOut.toLogString()));
							}

							StringBuilder sb_tFileOutputDelimited_1 = new StringBuilder();
							if (CustomersOut.Id != null) {
								sb_tFileOutputDelimited_1.append(CustomersOut.Id);
							}
							sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
							if (CustomersOut.First_Name != null) {
								sb_tFileOutputDelimited_1.append(CustomersOut.First_Name);
							}
							sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
							if (CustomersOut.Last_Name != null) {
								sb_tFileOutputDelimited_1.append(CustomersOut.Last_Name);
							}
							sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
							if (CustomersOut.Gender != null) {
								sb_tFileOutputDelimited_1.append(CustomersOut.Gender);
							}
							sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
							if (CustomersOut.Age != null) {
								sb_tFileOutputDelimited_1.append(CustomersOut.Age);
							}
							sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
							if (CustomersOut.Occupation != null) {
								sb_tFileOutputDelimited_1.append(CustomersOut.Occupation);
							}
							sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
							if (CustomersOut.MaritalStatus != null) {
								sb_tFileOutputDelimited_1.append(CustomersOut.MaritalStatus);
							}
							sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
							if (CustomersOut.Salary != null) {
								sb_tFileOutputDelimited_1.append(CustomersOut.Salary);
							}
							sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
							if (CustomersOut.Address != null) {
								sb_tFileOutputDelimited_1.append(CustomersOut.Address);
							}
							sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
							if (CustomersOut.City != null) {
								sb_tFileOutputDelimited_1.append(CustomersOut.City);
							}
							sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
							if (CustomersOut.State != null) {
								sb_tFileOutputDelimited_1.append(CustomersOut.State);
							}
							sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
							if (CustomersOut.Zip != null) {
								sb_tFileOutputDelimited_1.append(CustomersOut.Zip);
							}
							sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
							if (CustomersOut.Phone != null) {
								sb_tFileOutputDelimited_1.append(CustomersOut.Phone);
							}
							sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
							if (CustomersOut.Email != null) {
								sb_tFileOutputDelimited_1.append(CustomersOut.Email);
							}
							sb_tFileOutputDelimited_1.append(OUT_DELIM_ROWSEP_tFileOutputDelimited_1);

							nb_line_tFileOutputDelimited_1++;
							resourceMap.put("nb_line_tFileOutputDelimited_1", nb_line_tFileOutputDelimited_1);

							outtFileOutputDelimited_1.write(sb_tFileOutputDelimited_1.toString());
							log.debug("tFileOutputDelimited_1 - Writing the record " + nb_line_tFileOutputDelimited_1
									+ ".");

							tos_count_tFileOutputDelimited_1++;

							/**
							 * [tFileOutputDelimited_1 main ] stop
							 */

							/**
							 * [tFileOutputDelimited_1 process_data_begin ] start
							 */

							currentComponent = "tFileOutputDelimited_1";

							/**
							 * [tFileOutputDelimited_1 process_data_begin ] stop
							 */

							/**
							 * [tFileOutputDelimited_1 process_data_end ] start
							 */

							currentComponent = "tFileOutputDelimited_1";

							/**
							 * [tFileOutputDelimited_1 process_data_end ] stop
							 */

						} // End of branch "CustomersOut"

						/**
						 * [tMap_1 process_data_end ] start
						 */

						currentComponent = "tMap_1";

						/**
						 * [tMap_1 process_data_end ] stop
						 */

						/**
						 * [tDBInput_1 process_data_end ] start
						 */

						currentComponent = "tDBInput_1";

						cLabel = "\"customers\"";

						/**
						 * [tDBInput_1 process_data_end ] stop
						 */

						/**
						 * [tDBInput_1 end ] start
						 */

						currentComponent = "tDBInput_1";

						cLabel = "\"customers\"";

					}
				} finally {
					if (rs_tDBInput_1 != null) {
						rs_tDBInput_1.close();
					}
					if (stmt_tDBInput_1 != null) {
						stmt_tDBInput_1.close();
					}
					if (conn_tDBInput_1 != null && !conn_tDBInput_1.isClosed()) {

						log.debug("tDBInput_1 - Closing the connection to the database.");

						conn_tDBInput_1.close();

						if ("com.mysql.cj.jdbc.Driver".equals((String) globalMap.get("driverClass_"))
								&& routines.system.BundleUtils.inOSGi()) {
							Class.forName("com.mysql.cj.jdbc.AbandonedConnectionCleanupThread")
									.getMethod("checkedShutdown").invoke(null, (Object[]) null);
						}

						log.debug("tDBInput_1 - Connection to the database closed.");

					}

				}
				globalMap.put("tDBInput_1_NB_LINE", nb_line_tDBInput_1);
				log.debug("tDBInput_1 - Retrieved records count: " + nb_line_tDBInput_1 + " .");

				if (log.isDebugEnabled())
					log.debug("tDBInput_1 - " + ("Done."));

				ok_Hash.put("tDBInput_1", true);
				end_Hash.put("tDBInput_1", System.currentTimeMillis());

				/**
				 * [tDBInput_1 end ] stop
				 */

				/**
				 * [tMap_1 end ] start
				 */

				currentComponent = "tMap_1";

// ###############################
// # Lookup hashes releasing
				if (tHash_Lookup_row2 != null) {
					tHash_Lookup_row2.endGet();
				}
				globalMap.remove("tHash_Lookup_row2");

// ###############################      
				log.debug("tMap_1 - Written records count in the table 'CustomersOut': " + count_CustomersOut_tMap_1
						+ ".");

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row1", 2, 0,
						"tDBInput_1", "\"customers\"", "tMysqlInput", "tMap_1", "tMap_1", "tMap", "output")) {
					talendJobLogProcess(globalMap);
				}

				if (log.isDebugEnabled())
					log.debug("tMap_1 - " + ("Done."));

				ok_Hash.put("tMap_1", true);
				end_Hash.put("tMap_1", System.currentTimeMillis());

				/**
				 * [tMap_1 end ] stop
				 */

				/**
				 * [tFileOutputDelimited_1 end ] start
				 */

				currentComponent = "tFileOutputDelimited_1";

				if (outtFileOutputDelimited_1 != null) {
					outtFileOutputDelimited_1.flush();
					outtFileOutputDelimited_1.close();
				}

				globalMap.put("tFileOutputDelimited_1_NB_LINE", nb_line_tFileOutputDelimited_1);
				globalMap.put("tFileOutputDelimited_1_FILE_NAME", fileName_tFileOutputDelimited_1);

				resourceMap.put("finish_tFileOutputDelimited_1", true);

				log.debug("tFileOutputDelimited_1 - Written records count: " + nb_line_tFileOutputDelimited_1 + " .");

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "CustomersOut", 2, 0,
						"tMap_1", "tMap_1", "tMap", "tFileOutputDelimited_1", "tFileOutputDelimited_1",
						"tFileOutputDelimited", "output")) {
					talendJobLogProcess(globalMap);
				}

				if (log.isDebugEnabled())
					log.debug("tFileOutputDelimited_1 - " + ("Done."));

				ok_Hash.put("tFileOutputDelimited_1", true);
				end_Hash.put("tFileOutputDelimited_1", System.currentTimeMillis());

				/**
				 * [tFileOutputDelimited_1 end ] stop
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

			// free memory for "tMap_1"
			globalMap.remove("tHash_Lookup_row2");

			try {

				/**
				 * [tDBInput_1 finally ] start
				 */

				currentComponent = "tDBInput_1";

				cLabel = "\"customers\"";

				/**
				 * [tDBInput_1 finally ] stop
				 */

				/**
				 * [tMap_1 finally ] start
				 */

				currentComponent = "tMap_1";

				/**
				 * [tMap_1 finally ] stop
				 */

				/**
				 * [tFileOutputDelimited_1 finally ] start
				 */

				currentComponent = "tFileOutputDelimited_1";

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

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tDBInput_1_SUBPROCESS_STATE", 1);
	}

	public static class row2Struct implements routines.system.IPersistableComparableLookupRow<row2Struct> {
		final static byte[] commonByteArrayLock_DEMOCLOUD_File_S3 = new byte[0];
		static byte[] commonByteArray_DEMOCLOUD_File_S3 = new byte[0];
		protected static final int DEFAULT_HASHCODE = 1;
		protected static final int PRIME = 31;
		protected int hashCode = DEFAULT_HASHCODE;
		public boolean hashCodeDirty = true;

		public String loopKey;

		public Integer Id;

		public Integer getId() {
			return this.Id;
		}

		public Boolean IdIsNullable() {
			return true;
		}

		public Boolean IdIsKey() {
			return false;
		}

		public Integer IdLength() {
			return 2;
		}

		public Integer IdPrecision() {
			return 0;
		}

		public String IdDefault() {

			return null;

		}

		public String IdComment() {

			return "";

		}

		public String IdPattern() {

			return "dd-MM-yyyy";

		}

		public String IdOriginalDbColumnName() {

			return "Id";

		}

		public String Email;

		public String getEmail() {
			return this.Email;
		}

		public Boolean EmailIsNullable() {
			return true;
		}

		public Boolean EmailIsKey() {
			return false;
		}

		public Integer EmailLength() {
			return 29;
		}

		public Integer EmailPrecision() {
			return 0;
		}

		public String EmailDefault() {

			return null;

		}

		public String EmailComment() {

			return "";

		}

		public String EmailPattern() {

			return "dd-MM-yyyy";

		}

		public String EmailOriginalDbColumnName() {

			return "Email";

		}

		@Override
		public int hashCode() {
			if (this.hashCodeDirty) {
				final int prime = PRIME;
				int result = DEFAULT_HASHCODE;

				result = prime * result + ((this.Id == null) ? 0 : this.Id.hashCode());

				this.hashCode = result;
				this.hashCodeDirty = false;
			}
			return this.hashCode;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final row2Struct other = (row2Struct) obj;

			if (this.Id == null) {
				if (other.Id != null)
					return false;

			} else if (!this.Id.equals(other.Id))

				return false;

			return true;
		}

		public void copyDataTo(row2Struct other) {

			other.Id = this.Id;
			other.Email = this.Email;

		}

		public void copyKeysDataTo(row2Struct other) {

			other.Id = this.Id;

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

		private String readString(DataInputStream dis, ObjectInputStream ois) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				byte[] byteArray = new byte[length];
				dis.read(byteArray);
				strReturn = new String(byteArray, utf8Charset);
			}
			return strReturn;
		}

		private String readString(DataInputStream dis, org.jboss.marshalling.Unmarshaller unmarshaller)
				throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				byte[] byteArray = new byte[length];
				unmarshaller.read(byteArray);
				strReturn = new String(byteArray, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, DataOutputStream dos, org.jboss.marshalling.Marshaller marshaller)
				throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		private void writeString(String str, DataOutputStream dos, ObjectOutputStream oos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		public void readKeysData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_DEMOCLOUD_File_S3) {

				try {

					int length = 0;

					this.Id = readInteger(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readKeysData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_DEMOCLOUD_File_S3) {

				try {

					int length = 0;

					this.Id = readInteger(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeKeysData(ObjectOutputStream dos) {
			try {

				// Integer

				writeInteger(this.Id, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeKeysData(org.jboss.marshalling.Marshaller dos) {
			try {

				// Integer

				writeInteger(this.Id, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		/**
		 * Fill Values data by reading ObjectInputStream.
		 */
		public void readValuesData(DataInputStream dis, ObjectInputStream ois) {
			try {

				int length = 0;

				this.Email = readString(dis, ois);

			} catch (IOException e) {
				throw new RuntimeException(e);

			}

		}

		public void readValuesData(DataInputStream dis, org.jboss.marshalling.Unmarshaller objectIn) {
			try {
				int length = 0;

				this.Email = readString(dis, objectIn);

			} catch (IOException e) {
				throw new RuntimeException(e);

			}

		}

		/**
		 * Return a byte array which represents Values data.
		 */
		public void writeValuesData(DataOutputStream dos, ObjectOutputStream oos) {
			try {

				writeString(this.Email, dos, oos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeValuesData(DataOutputStream dos, org.jboss.marshalling.Marshaller objectOut) {
			try {

				writeString(this.Email, dos, objectOut);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		public boolean supportMarshaller() {
			return true;
		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("Id=" + String.valueOf(Id));
			sb.append(",Email=" + Email);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (Id == null) {
				sb.append("<null>");
			} else {
				sb.append(Id);
			}

			sb.append("|");

			if (Email == null) {
				sb.append("<null>");
			} else {
				sb.append(Email);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row2Struct other) {

			int returnValue = -1;

			returnValue = checkNullsAndCompare(this.Id, other.Id);
			if (returnValue != 0) {
				return returnValue;
			}

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

	public void tFileInputDelimited_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tFileInputDelimited_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdcInfo.forEach(org.slf4j.MDC::put);
		org.slf4j.MDC.put("_subJobName", "tFileInputDelimited_1");
		org.slf4j.MDC.put("_subJobPid", "skwIaw_" + subJobPidCounter.getAndIncrement());

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

				row2Struct row2 = new row2Struct();

				/**
				 * [tAdvancedHash_row2 begin ] start
				 */

				ok_Hash.put("tAdvancedHash_row2", false);
				start_Hash.put("tAdvancedHash_row2", System.currentTimeMillis());

				currentComponent = "tAdvancedHash_row2";

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row2");

				int tos_count_tAdvancedHash_row2 = 0;

				if (enableLogStash) {
					talendJobLog.addCM("tAdvancedHash_row2", "tAdvancedHash_row2", "tAdvancedHash");
					talendJobLogProcess(globalMap);
				}

				// connection name:row2
				// source node:tFileInputDelimited_1 - inputs:(after_tDBInput_1)
				// outputs:(row2,row2) | target node:tAdvancedHash_row2 - inputs:(row2)
				// outputs:()
				// linked node: tMap_1 - inputs:(row1,row2) outputs:(CustomersOut)

				org.talend.designer.components.lookup.common.ICommonLookup.MATCHING_MODE matchingModeEnum_row2 = org.talend.designer.components.lookup.common.ICommonLookup.MATCHING_MODE.UNIQUE_MATCH;

				org.talend.designer.components.lookup.memory.AdvancedMemoryLookup<row2Struct> tHash_Lookup_row2 = org.talend.designer.components.lookup.memory.AdvancedMemoryLookup
						.<row2Struct>getLookup(matchingModeEnum_row2);

				globalMap.put("tHash_Lookup_row2", tHash_Lookup_row2);

				/**
				 * [tAdvancedHash_row2 begin ] stop
				 */

				/**
				 * [tFileInputDelimited_1 begin ] start
				 */

				ok_Hash.put("tFileInputDelimited_1", false);
				start_Hash.put("tFileInputDelimited_1", System.currentTimeMillis());

				currentComponent = "tFileInputDelimited_1";

				cLabel = "CustomersEmails";

				int tos_count_tFileInputDelimited_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tFileInputDelimited_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tFileInputDelimited_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tFileInputDelimited_1 = new StringBuilder();
							log4jParamters_tFileInputDelimited_1.append("Parameters:");
							log4jParamters_tFileInputDelimited_1.append("USE_EXISTING_DYNAMIC" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append(
									"FILENAME" + " = " + "\"C:/SIFT/Developer/Talend/Data/Demo/Customers_Email.csv\"");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("CSV_OPTION" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("ROWSEPARATOR" + " = " + "\"\\n\"");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("FIELDSEPARATOR" + " = " + "\",\"");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("HEADER" + " = " + "1");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("FOOTER" + " = " + "0");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("LIMIT" + " = " + "");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("REMOVE_EMPTY_ROW" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("UNCOMPRESS" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("DIE_ON_ERROR" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("ADVANCED_SEPARATOR" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("RANDOM" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("TRIMALL" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1
									.append("TRIMSELECT" + " = " + "[{TRIM=" + ("false") + ", SCHEMA_COLUMN=" + ("Id")
											+ "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN=" + ("Email") + "}]");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("CHECK_FIELDS_NUM" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("CHECK_DATE" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("ENCODING" + " = " + "\"UTF-8\"");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("SPLITRECORD" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("ENABLE_DECODE" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("USE_HEADER_AS_IS" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tFileInputDelimited_1 - " + (log4jParamters_tFileInputDelimited_1));
						}
					}
					new BytesLimit65535_tFileInputDelimited_1().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tFileInputDelimited_1", "CustomersEmails", "tFileInputDelimited");
					talendJobLogProcess(globalMap);
				}

				final routines.system.RowState rowstate_tFileInputDelimited_1 = new routines.system.RowState();

				int nb_line_tFileInputDelimited_1 = 0;
				org.talend.fileprocess.FileInputDelimited fid_tFileInputDelimited_1 = null;
				int limit_tFileInputDelimited_1 = -1;
				try {

					Object filename_tFileInputDelimited_1 = "C:/SIFT/Developer/Talend/Data/Demo/Customers_Email.csv";
					if (filename_tFileInputDelimited_1 instanceof java.io.InputStream) {

						int footer_value_tFileInputDelimited_1 = 0, random_value_tFileInputDelimited_1 = -1;
						if (footer_value_tFileInputDelimited_1 > 0 || random_value_tFileInputDelimited_1 > 0) {
							throw new java.lang.Exception(
									"When the input source is a stream,footer and random shouldn't be bigger than 0.");
						}

					}
					try {
						fid_tFileInputDelimited_1 = new org.talend.fileprocess.FileInputDelimited(
								"C:/SIFT/Developer/Talend/Data/Demo/Customers_Email.csv", "UTF-8", ",", "\n", false, 1,
								0, limit_tFileInputDelimited_1, -1, false);
					} catch (java.lang.Exception e) {
						globalMap.put("tFileInputDelimited_1_ERROR_MESSAGE", e.getMessage());

						log.error("tFileInputDelimited_1 - " + e.getMessage());

						System.err.println(e.getMessage());

					}

					log.info("tFileInputDelimited_1 - Retrieving records from the datasource.");

					while (fid_tFileInputDelimited_1 != null && fid_tFileInputDelimited_1.nextRecord()) {
						rowstate_tFileInputDelimited_1.reset();

						row2 = null;

						row2 = null;

						boolean whetherReject_tFileInputDelimited_1 = false;
						row2 = new row2Struct();
						try {

							int columnIndexWithD_tFileInputDelimited_1 = 0;

							String temp = "";

							columnIndexWithD_tFileInputDelimited_1 = 0;

							temp = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);
							if (temp.length() > 0) {

								try {

									row2.Id = ParserUtils.parseTo_Integer(temp);

								} catch (java.lang.Exception ex_tFileInputDelimited_1) {
									globalMap.put("tFileInputDelimited_1_ERROR_MESSAGE",
											ex_tFileInputDelimited_1.getMessage());
									rowstate_tFileInputDelimited_1.setException(new RuntimeException(String.format(
											"Couldn't parse value for column '%s' in '%s', value is '%s'. Details: %s",
											"Id", "row2", temp, ex_tFileInputDelimited_1), ex_tFileInputDelimited_1));
								}

							} else {

								row2.Id = null;

							}

							columnIndexWithD_tFileInputDelimited_1 = 1;

							row2.Email = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							if (rowstate_tFileInputDelimited_1.getException() != null) {
								throw rowstate_tFileInputDelimited_1.getException();
							}

						} catch (java.lang.Exception e) {
							globalMap.put("tFileInputDelimited_1_ERROR_MESSAGE", e.getMessage());
							whetherReject_tFileInputDelimited_1 = true;

							log.error("tFileInputDelimited_1 - " + e.getMessage());

							System.err.println(e.getMessage());
							row2 = null;

						}

						log.debug("tFileInputDelimited_1 - Retrieving the record "
								+ fid_tFileInputDelimited_1.getRowNumber() + ".");

						/**
						 * [tFileInputDelimited_1 begin ] stop
						 */

						/**
						 * [tFileInputDelimited_1 main ] start
						 */

						currentComponent = "tFileInputDelimited_1";

						cLabel = "CustomersEmails";

						tos_count_tFileInputDelimited_1++;

						/**
						 * [tFileInputDelimited_1 main ] stop
						 */

						/**
						 * [tFileInputDelimited_1 process_data_begin ] start
						 */

						currentComponent = "tFileInputDelimited_1";

						cLabel = "CustomersEmails";

						/**
						 * [tFileInputDelimited_1 process_data_begin ] stop
						 */
// Start of branch "row2"
						if (row2 != null) {

							/**
							 * [tAdvancedHash_row2 main ] start
							 */

							currentComponent = "tAdvancedHash_row2";

							if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

									, "row2", "tFileInputDelimited_1", "CustomersEmails", "tFileInputDelimited",
									"tAdvancedHash_row2", "tAdvancedHash_row2", "tAdvancedHash"

							)) {
								talendJobLogProcess(globalMap);
							}

							if (log.isTraceEnabled()) {
								log.trace("row2 - " + (row2 == null ? "" : row2.toLogString()));
							}

							row2Struct row2_HashRow = new row2Struct();

							row2_HashRow.Id = row2.Id;

							row2_HashRow.Email = row2.Email;

							tHash_Lookup_row2.put(row2_HashRow);

							tos_count_tAdvancedHash_row2++;

							/**
							 * [tAdvancedHash_row2 main ] stop
							 */

							/**
							 * [tAdvancedHash_row2 process_data_begin ] start
							 */

							currentComponent = "tAdvancedHash_row2";

							/**
							 * [tAdvancedHash_row2 process_data_begin ] stop
							 */

							/**
							 * [tAdvancedHash_row2 process_data_end ] start
							 */

							currentComponent = "tAdvancedHash_row2";

							/**
							 * [tAdvancedHash_row2 process_data_end ] stop
							 */

						} // End of branch "row2"

						/**
						 * [tFileInputDelimited_1 process_data_end ] start
						 */

						currentComponent = "tFileInputDelimited_1";

						cLabel = "CustomersEmails";

						/**
						 * [tFileInputDelimited_1 process_data_end ] stop
						 */

						/**
						 * [tFileInputDelimited_1 end ] start
						 */

						currentComponent = "tFileInputDelimited_1";

						cLabel = "CustomersEmails";

					}
				} finally {
					if (!((Object) ("C:/SIFT/Developer/Talend/Data/Demo/Customers_Email.csv") instanceof java.io.InputStream)) {
						if (fid_tFileInputDelimited_1 != null) {
							fid_tFileInputDelimited_1.close();
						}
					}
					if (fid_tFileInputDelimited_1 != null) {
						globalMap.put("tFileInputDelimited_1_NB_LINE", fid_tFileInputDelimited_1.getRowNumber());

						log.info("tFileInputDelimited_1 - Retrieved records count: "
								+ fid_tFileInputDelimited_1.getRowNumber() + ".");

					}
				}

				if (log.isDebugEnabled())
					log.debug("tFileInputDelimited_1 - " + ("Done."));

				ok_Hash.put("tFileInputDelimited_1", true);
				end_Hash.put("tFileInputDelimited_1", System.currentTimeMillis());

				/**
				 * [tFileInputDelimited_1 end ] stop
				 */

				/**
				 * [tAdvancedHash_row2 end ] start
				 */

				currentComponent = "tAdvancedHash_row2";

				tHash_Lookup_row2.endPut();

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row2", 2, 0,
						"tFileInputDelimited_1", "CustomersEmails", "tFileInputDelimited", "tAdvancedHash_row2",
						"tAdvancedHash_row2", "tAdvancedHash", "output")) {
					talendJobLogProcess(globalMap);
				}

				ok_Hash.put("tAdvancedHash_row2", true);
				end_Hash.put("tAdvancedHash_row2", System.currentTimeMillis());

				/**
				 * [tAdvancedHash_row2 end ] stop
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
				 * [tFileInputDelimited_1 finally ] start
				 */

				currentComponent = "tFileInputDelimited_1";

				cLabel = "CustomersEmails";

				/**
				 * [tFileInputDelimited_1 finally ] stop
				 */

				/**
				 * [tAdvancedHash_row2 finally ] start
				 */

				currentComponent = "tAdvancedHash_row2";

				/**
				 * [tAdvancedHash_row2 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tFileInputDelimited_1_SUBPROCESS_STATE", 1);
	}

	public void tPrejob_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tPrejob_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdcInfo.forEach(org.slf4j.MDC::put);
		org.slf4j.MDC.put("_subJobName", "tPrejob_1");
		org.slf4j.MDC.put("_subJobPid", "brufH4_" + subJobPidCounter.getAndIncrement());

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
				 * [tPrejob_1 begin ] start
				 */

				ok_Hash.put("tPrejob_1", false);
				start_Hash.put("tPrejob_1", System.currentTimeMillis());

				currentComponent = "tPrejob_1";

				int tos_count_tPrejob_1 = 0;

				if (enableLogStash) {
					talendJobLog.addCM("tPrejob_1", "tPrejob_1", "tPrejob");
					talendJobLogProcess(globalMap);
				}

				/**
				 * [tPrejob_1 begin ] stop
				 */

				/**
				 * [tPrejob_1 main ] start
				 */

				currentComponent = "tPrejob_1";

				tos_count_tPrejob_1++;

				/**
				 * [tPrejob_1 main ] stop
				 */

				/**
				 * [tPrejob_1 process_data_begin ] start
				 */

				currentComponent = "tPrejob_1";

				/**
				 * [tPrejob_1 process_data_begin ] stop
				 */

				/**
				 * [tPrejob_1 process_data_end ] start
				 */

				currentComponent = "tPrejob_1";

				/**
				 * [tPrejob_1 process_data_end ] stop
				 */

				/**
				 * [tPrejob_1 end ] start
				 */

				currentComponent = "tPrejob_1";

				ok_Hash.put("tPrejob_1", true);
				end_Hash.put("tPrejob_1", System.currentTimeMillis());

				if (execStat) {
					runStat.updateStatOnConnection("OnComponentOk1", 0, "ok");
				}
				tS3Connection_1Process(globalMap);

				/**
				 * [tPrejob_1 end ] stop
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
				 * [tPrejob_1 finally ] start
				 */

				currentComponent = "tPrejob_1";

				/**
				 * [tPrejob_1 finally ] stop
				 */
			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tPrejob_1_SUBPROCESS_STATE", 1);
	}

	public void tS3Connection_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tS3Connection_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdcInfo.forEach(org.slf4j.MDC::put);
		org.slf4j.MDC.put("_subJobName", "tS3Connection_1");
		org.slf4j.MDC.put("_subJobPid", "aqCDQL_" + subJobPidCounter.getAndIncrement());

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
				 * [tS3Connection_1 begin ] start
				 */

				ok_Hash.put("tS3Connection_1", false);
				start_Hash.put("tS3Connection_1", System.currentTimeMillis());

				currentComponent = "tS3Connection_1";

				int tos_count_tS3Connection_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tS3Connection_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tS3Connection_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tS3Connection_1 = new StringBuilder();
							log4jParamters_tS3Connection_1.append("Parameters:");
							log4jParamters_tS3Connection_1.append("CREDENTIAL_PROVIDER" + " = " + "STATIC_CREDENTIALS");
							log4jParamters_tS3Connection_1.append(" | ");
							log4jParamters_tS3Connection_1.append("ACCESS_KEY" + " = " + "\"AKIA425JNRQCIW5KV35H\"");
							log4jParamters_tS3Connection_1.append(" | ");
							log4jParamters_tS3Connection_1.append("SECRET_KEY" + " = " + String.valueOf(
									"enc:routine.encryption.key.v1:yeaSlAh7K2A2I9AnYuKz8ZK/nUMq5PmtxF5/rdLtE+i1C395rRWBT4MA+4LVRaTDAqAUSRHZGMksReDETXArqv4uStA=")
									.substring(0, 4) + "...");
							log4jParamters_tS3Connection_1.append(" | ");
							log4jParamters_tS3Connection_1.append("ASSUME_ROLE" + " = " + "false");
							log4jParamters_tS3Connection_1.append(" | ");
							log4jParamters_tS3Connection_1.append("REGION" + " = " + "DEFAULT");
							log4jParamters_tS3Connection_1.append(" | ");
							log4jParamters_tS3Connection_1.append("ENCRYPT" + " = " + "false");
							log4jParamters_tS3Connection_1.append(" | ");
							log4jParamters_tS3Connection_1.append("USE_REGION_ENDPOINT" + " = " + "false");
							log4jParamters_tS3Connection_1.append(" | ");
							log4jParamters_tS3Connection_1.append("CONFIG_CLIENT" + " = " + "false");
							log4jParamters_tS3Connection_1.append(" | ");
							log4jParamters_tS3Connection_1.append("CHECK_ACCESSIBILITY" + " = " + "true");
							log4jParamters_tS3Connection_1.append(" | ");
							log4jParamters_tS3Connection_1.append("CHECK_METHOD" + " = " + "BY_ACCOUNT_OWNER");
							log4jParamters_tS3Connection_1.append(" | ");
							log4jParamters_tS3Connection_1.append("ENABLE_ACCELERATE" + " = " + "false");
							log4jParamters_tS3Connection_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tS3Connection_1 - " + (log4jParamters_tS3Connection_1));
						}
					}
					new BytesLimit65535_tS3Connection_1().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tS3Connection_1", "tS3Connection_1", "tS3Connection");
					talendJobLogProcess(globalMap);
				}

				log.info("tS3Connection_1 - Creating new connection.");

				final String decryptedPassword_tS3Connection_1 = routines.system.PasswordEncryptUtil.decryptPassword(
						"enc:routine.encryption.key.v1:glXtrsWl8q1Dv0TlVBJZY6lstSGDRXG4dVE+3jerdGRAvfMF6zVa6HmzvP1yC1F9KKNwhbTVDVk4V/PZmwM9Jyvlxng=");

				com.amazonaws.auth.AWSCredentials credentials_tS3Connection_1 = new com.amazonaws.auth.BasicAWSCredentials(
						"AKIA425JNRQCIW5KV35H", decryptedPassword_tS3Connection_1);
				com.amazonaws.auth.AWSCredentialsProvider credentialsProvider_tS3Connection_1 = new com.amazonaws.auth.AWSStaticCredentialsProvider(
						credentials_tS3Connection_1);

				com.amazonaws.ClientConfiguration cc_tS3Connection_1 = new com.amazonaws.ClientConfiguration();
				cc_tS3Connection_1.setUserAgent("APN/1.0 Talend/8.0 Studio/8.0 (Talend Cloud)");

				com.amazonaws.services.s3.AmazonS3ClientBuilder builder_tS3Connection_1 = com.amazonaws.services.s3.AmazonS3ClientBuilder
						.standard();

				final boolean useRegionEndpoint_tS3Connection_1 = false;
				final String regionEndpoint_tS3Connection_1 = "s3.amazonaws.com";
				final boolean enableAccelerateMode_tS3Connection_1 = false;
				final boolean enablePathStyleAccess_tS3Connection_1 = false;

				if (useRegionEndpoint_tS3Connection_1 && regionEndpoint_tS3Connection_1 != null
						&& !regionEndpoint_tS3Connection_1.isEmpty()) {
					builder_tS3Connection_1
							.withEndpointConfiguration(
									new com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration(
											regionEndpoint_tS3Connection_1, null))
							.withForceGlobalBucketAccessEnabled(true);
				} else {
					builder_tS3Connection_1.withRegion("us-east-1") // The first region to try your request against
							.withForceGlobalBucketAccessEnabled(true); // If a bucket is in a different region, try
																		// again in the correct region
				}

				if (enableAccelerateMode_tS3Connection_1) {
					builder_tS3Connection_1.withAccelerateModeEnabled(true);
				}

				builder_tS3Connection_1.withCredentials(credentialsProvider_tS3Connection_1)
						.withClientConfiguration(cc_tS3Connection_1);

				if (useRegionEndpoint_tS3Connection_1 && enablePathStyleAccess_tS3Connection_1) {
					builder_tS3Connection_1.enablePathStyleAccess();
				}

				com.amazonaws.services.s3.AmazonS3 conn_tS3Connection_1 = builder_tS3Connection_1.build();

				log.info("tS3Connection_1 - Creating new connection successfully.");

				// This method is just for test connection.
				conn_tS3Connection_1.getS3AccountOwner();

				globalMap.put("conn_" + "tS3Connection_1", conn_tS3Connection_1);

				/**
				 * [tS3Connection_1 begin ] stop
				 */

				/**
				 * [tS3Connection_1 main ] start
				 */

				currentComponent = "tS3Connection_1";

				tos_count_tS3Connection_1++;

				/**
				 * [tS3Connection_1 main ] stop
				 */

				/**
				 * [tS3Connection_1 process_data_begin ] start
				 */

				currentComponent = "tS3Connection_1";

				/**
				 * [tS3Connection_1 process_data_begin ] stop
				 */

				/**
				 * [tS3Connection_1 process_data_end ] start
				 */

				currentComponent = "tS3Connection_1";

				/**
				 * [tS3Connection_1 process_data_end ] stop
				 */

				/**
				 * [tS3Connection_1 end ] start
				 */

				currentComponent = "tS3Connection_1";

				if (log.isDebugEnabled())
					log.debug("tS3Connection_1 - " + ("Done."));

				ok_Hash.put("tS3Connection_1", true);
				end_Hash.put("tS3Connection_1", System.currentTimeMillis());

				/**
				 * [tS3Connection_1 end ] stop
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
				 * [tS3Connection_1 finally ] start
				 */

				currentComponent = "tS3Connection_1";

				/**
				 * [tS3Connection_1 finally ] stop
				 */
			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tS3Connection_1_SUBPROCESS_STATE", 1);
	}

	public void tPostjob_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tPostjob_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdcInfo.forEach(org.slf4j.MDC::put);
		org.slf4j.MDC.put("_subJobName", "tPostjob_1");
		org.slf4j.MDC.put("_subJobPid", "oM9OX0_" + subJobPidCounter.getAndIncrement());

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
				 * [tPostjob_1 begin ] start
				 */

				ok_Hash.put("tPostjob_1", false);
				start_Hash.put("tPostjob_1", System.currentTimeMillis());

				currentComponent = "tPostjob_1";

				int tos_count_tPostjob_1 = 0;

				if (enableLogStash) {
					talendJobLog.addCM("tPostjob_1", "tPostjob_1", "tPostjob");
					talendJobLogProcess(globalMap);
				}

				/**
				 * [tPostjob_1 begin ] stop
				 */

				/**
				 * [tPostjob_1 main ] start
				 */

				currentComponent = "tPostjob_1";

				tos_count_tPostjob_1++;

				/**
				 * [tPostjob_1 main ] stop
				 */

				/**
				 * [tPostjob_1 process_data_begin ] start
				 */

				currentComponent = "tPostjob_1";

				/**
				 * [tPostjob_1 process_data_begin ] stop
				 */

				/**
				 * [tPostjob_1 process_data_end ] start
				 */

				currentComponent = "tPostjob_1";

				/**
				 * [tPostjob_1 process_data_end ] stop
				 */

				/**
				 * [tPostjob_1 end ] start
				 */

				currentComponent = "tPostjob_1";

				ok_Hash.put("tPostjob_1", true);
				end_Hash.put("tPostjob_1", System.currentTimeMillis());

				if (execStat) {
					runStat.updateStatOnConnection("OnComponentOk2", 0, "ok");
				}
				tS3Put_1Process(globalMap);

				/**
				 * [tPostjob_1 end ] stop
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
				 * [tPostjob_1 finally ] start
				 */

				currentComponent = "tPostjob_1";

				/**
				 * [tPostjob_1 finally ] stop
				 */
			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tPostjob_1_SUBPROCESS_STATE", 1);
	}

	public void tS3Put_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tS3Put_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdcInfo.forEach(org.slf4j.MDC::put);
		org.slf4j.MDC.put("_subJobName", "tS3Put_1");
		org.slf4j.MDC.put("_subJobPid", "FyfLB9_" + subJobPidCounter.getAndIncrement());

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
				 * [tS3Put_1 begin ] start
				 */

				ok_Hash.put("tS3Put_1", false);
				start_Hash.put("tS3Put_1", System.currentTimeMillis());

				currentComponent = "tS3Put_1";

				int tos_count_tS3Put_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tS3Put_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tS3Put_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tS3Put_1 = new StringBuilder();
							log4jParamters_tS3Put_1.append("Parameters:");
							log4jParamters_tS3Put_1.append("USE_EXISTING_CONNECTION" + " = " + "true");
							log4jParamters_tS3Put_1.append(" | ");
							log4jParamters_tS3Put_1.append("CONNECTION" + " = " + "tS3Connection_1");
							log4jParamters_tS3Put_1.append(" | ");
							log4jParamters_tS3Put_1.append("BUCKET" + " = " + "\"sift-et-bucket\"");
							log4jParamters_tS3Put_1.append(" | ");
							log4jParamters_tS3Put_1.append("KEY" + " = " + "\"test/out.csv\"");
							log4jParamters_tS3Put_1.append(" | ");
							log4jParamters_tS3Put_1
									.append("FILE" + " = " + "\"C:/SIFT/Developer/Talend/Data/test/out.csv\"");
							log4jParamters_tS3Put_1.append(" | ");
							log4jParamters_tS3Put_1.append("ENABLE_SERVER_SIDE_ENCRYPTION" + " = " + "false");
							log4jParamters_tS3Put_1.append(" | ");
							log4jParamters_tS3Put_1.append("DIE_ON_ERROR" + " = " + "false");
							log4jParamters_tS3Put_1.append(" | ");
							log4jParamters_tS3Put_1.append("MULTIPART_THRESHOLD" + " = " + "5");
							log4jParamters_tS3Put_1.append(" | ");
							log4jParamters_tS3Put_1.append("PART_SIZE" + " = " + "5");
							log4jParamters_tS3Put_1.append(" | ");
							log4jParamters_tS3Put_1.append("ACCESS_CONTROL_LIST" + " = " + "false");
							log4jParamters_tS3Put_1.append(" | ");
							log4jParamters_tS3Put_1.append("CANNED_ACCESS_CONTROL_LIST" + " = " + "\"NONE\"");
							log4jParamters_tS3Put_1.append(" | ");
							log4jParamters_tS3Put_1.append("ENABLE_OBJECT_LOCK" + " = " + "false");
							log4jParamters_tS3Put_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tS3Put_1 - " + (log4jParamters_tS3Put_1));
						}
					}
					new BytesLimit65535_tS3Put_1().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tS3Put_1", "tS3Put_1", "tS3Put");
					talendJobLogProcess(globalMap);
				}

				String millisecTime_tS3Put_1 = null;

				com.amazonaws.services.s3.AmazonS3Client conn_tS3Put_1 = (com.amazonaws.services.s3.AmazonS3Client) globalMap
						.get("conn_tS3Connection_1");

				log.info("tS3Put_1 - Get an free connection from " + "tS3Connection_1" + ".");

				String key_tS3Put_1 = "test/out.csv";

				int partSizeInBytes_tS3Put_1 = 5 * 1024 * 1024;
				if (partSizeInBytes_tS3Put_1 < 5 << 20) {

					log.info("Set part size as 5MB, as it is the recommended minimun value.");

					partSizeInBytes_tS3Put_1 = 5 << 20;
				}

				Object fileOrStream_tS3Put_1 = "C:/SIFT/Developer/Talend/Data/test/out.csv";

				boolean useStream_tS3Put_1 = false;
				java.io.InputStream uploadStream_tS3Put_1 = null;

				com.amazonaws.services.s3.transfer.TransferManager tm_tS3Put_1 = null;

				try {

					log.info("tS3Put_1 - Uploading an object with key:" + key_tS3Put_1);

					if (fileOrStream_tS3Put_1 instanceof String) {
						useStream_tS3Put_1 = false;
					} else if (fileOrStream_tS3Put_1 instanceof java.io.InputStream) {
						useStream_tS3Put_1 = true;
					}

					com.amazonaws.services.s3.model.ObjectMetadata objectMetadata_tS3Put_1 = new com.amazonaws.services.s3.model.ObjectMetadata();

					if (!useStream_tS3Put_1) {
						java.io.File inputFile_tS3Put_1 = new java.io.File((String) fileOrStream_tS3Put_1);

						long multipart_upload_threshold_tS3Put_1 = 5 * 1024 * 1024;

						tm_tS3Put_1 = com.amazonaws.services.s3.transfer.TransferManagerBuilder.standard()
								.withMinimumUploadPartSize((long) partSizeInBytes_tS3Put_1)
								.withMultipartUploadThreshold(multipart_upload_threshold_tS3Put_1)
								.withS3Client(conn_tS3Put_1).build();

						com.amazonaws.services.s3.model.PutObjectRequest putRequest_tS3Put_1 = new com.amazonaws.services.s3.model.PutObjectRequest(
								"sift-et-bucket", key_tS3Put_1, inputFile_tS3Put_1)
										.withMetadata(objectMetadata_tS3Put_1);

						com.amazonaws.services.s3.transfer.Upload upload_tS3Put_1 = tm_tS3Put_1
								.upload(putRequest_tS3Put_1);

						upload_tS3Put_1.waitForCompletion();

					} else {
						java.io.InputStream sourceStream_tS3Put_1 = ((java.io.InputStream) fileOrStream_tS3Put_1);

						class S3StreamUtil {
							// read content to buffer as many as possible
							public int readFully(final java.io.InputStream input, final byte[] buffer)
									throws java.io.IOException {
								return readFully(input, buffer, 0, buffer.length);
							}

							public int readFully(final java.io.InputStream input, final byte[] buffer, final int offset,
									final int length) throws java.io.IOException {
								if (length < 0) {
									throw new java.lang.IllegalArgumentException(
											"Length must not be negative: " + length);
								}

								int remaining = length;
								while (remaining > 0) {
									final int location = length - remaining;
									final int count = input.read(buffer, offset + location, remaining);
									if (count == -1) {
										break;
									}
									remaining -= count;
								}
								return length - remaining;
							}
						}

						S3StreamUtil streamUtil_tS3Put_1 = new S3StreamUtil();
						byte[] buffer_tS3Put_1 = new byte[partSizeInBytes_tS3Put_1];
						long curPartSize_tS3Put_1 = streamUtil_tS3Put_1.readFully(sourceStream_tS3Put_1,
								buffer_tS3Put_1);

						boolean multiUpload_tS3Put_1 = curPartSize_tS3Put_1 == partSizeInBytes_tS3Put_1;

						if (!multiUpload_tS3Put_1) {
							objectMetadata_tS3Put_1.setContentLength(curPartSize_tS3Put_1);
							uploadStream_tS3Put_1 = new java.io.ByteArrayInputStream(buffer_tS3Put_1, 0,
									Long.valueOf(curPartSize_tS3Put_1).intValue());
							com.amazonaws.services.s3.model.PutObjectRequest putRequest_tS3Put_1 = new com.amazonaws.services.s3.model.PutObjectRequest(
									"sift-et-bucket", key_tS3Put_1, uploadStream_tS3Put_1, objectMetadata_tS3Put_1);

							conn_tS3Put_1.putObject(putRequest_tS3Put_1);
						} else {
							uploadStream_tS3Put_1 = new java.io.ByteArrayInputStream(buffer_tS3Put_1);
							java.util.List<com.amazonaws.services.s3.model.PartETag> partTags_tS3Put_1 = new java.util.ArrayList<com.amazonaws.services.s3.model.PartETag>();
							com.amazonaws.services.s3.model.InitiateMultipartUploadRequest putRequest_tS3Put_1 = new com.amazonaws.services.s3.model.InitiateMultipartUploadRequest(
									"sift-et-bucket", key_tS3Put_1, objectMetadata_tS3Put_1);

							com.amazonaws.services.s3.model.InitiateMultipartUploadResult initResponse_tS3Put_1 = conn_tS3Put_1
									.initiateMultipartUpload(putRequest_tS3Put_1);
							String uploadId_tS3Put_1 = initResponse_tS3Put_1.getUploadId();
							int partNumber_tS3Put_1 = 1;
							boolean streamHasNext_tS3Put_1 = true;
							byte[] probeAvailability_tS3Put_1 = new byte[1];
							try {
								while (streamHasNext_tS3Put_1) {
									com.amazonaws.services.s3.model.UploadPartRequest uploadRequest_tS3Put_1 = new com.amazonaws.services.s3.model.UploadPartRequest()
											.withBucketName("sift-et-bucket").withKey(key_tS3Put_1)
											.withUploadId(uploadId_tS3Put_1).withPartNumber(partNumber_tS3Put_1)
											.withPartSize(curPartSize_tS3Put_1);
									uploadRequest_tS3Put_1.setInputStream(uploadStream_tS3Put_1);
									streamHasNext_tS3Put_1 = (1 == streamUtil_tS3Put_1.readFully(sourceStream_tS3Put_1,
											probeAvailability_tS3Put_1));
									if (!streamHasNext_tS3Put_1) {
										uploadRequest_tS3Put_1.setLastPart(true);
									}

									partTags_tS3Put_1
											.add(conn_tS3Put_1.uploadPart(uploadRequest_tS3Put_1).getPartETag());
									partNumber_tS3Put_1++;

									if (uploadStream_tS3Put_1 != null) {
										uploadStream_tS3Put_1.close();
									}
									buffer_tS3Put_1 = new byte[partSizeInBytes_tS3Put_1];
									curPartSize_tS3Put_1 = 1 + streamUtil_tS3Put_1.readFully(sourceStream_tS3Put_1,
											buffer_tS3Put_1, 1, partSizeInBytes_tS3Put_1 - 1);
									buffer_tS3Put_1[0] = probeAvailability_tS3Put_1[0];
									probeAvailability_tS3Put_1 = new byte[1];
									uploadStream_tS3Put_1 = new java.io.ByteArrayInputStream(buffer_tS3Put_1);
								}

								com.amazonaws.services.s3.model.CompleteMultipartUploadRequest compRequest_tS3Put_1 = new com.amazonaws.services.s3.model.CompleteMultipartUploadRequest(
										"sift-et-bucket", key_tS3Put_1, uploadId_tS3Put_1, partTags_tS3Put_1);
								conn_tS3Put_1.completeMultipartUpload(compRequest_tS3Put_1);
							} catch (java.lang.Exception uploadException_tS3Put_1) {
								globalMap.put("tS3Put_1_ERROR_MESSAGE", uploadException_tS3Put_1.getMessage());
								conn_tS3Put_1.abortMultipartUpload(
										new com.amazonaws.services.s3.model.AbortMultipartUploadRequest(
												"sift-et-bucket", key_tS3Put_1, uploadId_tS3Put_1));
								throw uploadException_tS3Put_1;
							}
						}
					}

					log.info("tS3Put_1 - Upload the object successfully.");

				} catch (java.lang.Exception e_tS3Put_1) {
					globalMap.put("tS3Put_1_ERROR_MESSAGE", e_tS3Put_1.getMessage());

					log.error("tS3Put_1 - " + e_tS3Put_1.getMessage());

					System.err.println(e_tS3Put_1.getMessage());

				} finally {
					if (useStream_tS3Put_1 && uploadStream_tS3Put_1 != null) {
						uploadStream_tS3Put_1.close();
					}

					if (tm_tS3Put_1 != null) {
						tm_tS3Put_1.shutdownNow(false);
					}

				}

				/**
				 * [tS3Put_1 begin ] stop
				 */

				/**
				 * [tS3Put_1 main ] start
				 */

				currentComponent = "tS3Put_1";

				tos_count_tS3Put_1++;

				/**
				 * [tS3Put_1 main ] stop
				 */

				/**
				 * [tS3Put_1 process_data_begin ] start
				 */

				currentComponent = "tS3Put_1";

				/**
				 * [tS3Put_1 process_data_begin ] stop
				 */

				/**
				 * [tS3Put_1 process_data_end ] start
				 */

				currentComponent = "tS3Put_1";

				/**
				 * [tS3Put_1 process_data_end ] stop
				 */

				/**
				 * [tS3Put_1 end ] start
				 */

				currentComponent = "tS3Put_1";

				if (log.isDebugEnabled())
					log.debug("tS3Put_1 - " + ("Done."));

				ok_Hash.put("tS3Put_1", true);
				end_Hash.put("tS3Put_1", System.currentTimeMillis());

				/**
				 * [tS3Put_1 end ] stop
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
				 * [tS3Put_1 finally ] start
				 */

				currentComponent = "tS3Put_1";

				/**
				 * [tS3Put_1 finally ] stop
				 */
			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tS3Put_1_SUBPROCESS_STATE", 1);
	}

	public void talendJobLogProcess(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("talendJobLog_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdcInfo.forEach(org.slf4j.MDC::put);
		org.slf4j.MDC.put("_subJobName", "talendJobLog");
		org.slf4j.MDC.put("_subJobPid", "c2xNo2_" + subJobPidCounter.getAndIncrement());

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
		final File_S3 File_S3Class = new File_S3();

		int exitCode = File_S3Class.runJobInTOS(args);
		if (exitCode == 0) {
			log.info("TalendJob: 'File_S3' - Done.");
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
		log.info("TalendJob: 'File_S3' - Start.");

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
		org.slf4j.MDC.put("_jobRepositoryId", "_bi3FQIREEe6K79Ugdc6UFA");
		org.slf4j.MDC.put("_compiledAtTimestamp", "2024-01-11T02:41:52.188134200Z");

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
			java.io.InputStream inContext = File_S3.class.getClassLoader()
					.getResourceAsStream("democloud/file_s3_0_1/contexts/" + contextStr + ".properties");
			if (inContext == null) {
				inContext = File_S3.class.getClassLoader()
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
					context.setContextType("param_file_path", "id_String");
					if (context.getStringValue("param_file_path") == null) {
						context.param_file_path = null;
					} else {
						context.param_file_path = (String) context.getProperty("param_file_path");
					}
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
			if (parentContextMap.containsKey("param_file_path")) {
				context.param_file_path = (String) parentContextMap.get("param_file_path");
			}
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
		log.info("TalendJob: 'File_S3' - Started.");
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

		try {
			errorCode = null;
			tPrejob_1Process(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (TalendException e_tPrejob_1) {
			globalMap.put("tPrejob_1_SUBPROCESS_STATE", -1);

			e_tPrejob_1.printStackTrace();

		}

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
			tDBInput_1Process(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (TalendException e_tDBInput_1) {
			globalMap.put("tDBInput_1_SUBPROCESS_STATE", -1);

			e_tDBInput_1.printStackTrace();

		}

		this.globalResumeTicket = true;// to run tPostJob

		try {
			errorCode = null;
			tPostjob_1Process(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (TalendException e_tPostjob_1) {
			globalMap.put("tPostjob_1_SUBPROCESS_STATE", -1);

			e_tPostjob_1.printStackTrace();

		}

		end = System.currentTimeMillis();

		if (watch) {
			System.out.println((end - startTime) + " milliseconds");
		}

		endUsedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		if (false) {
			System.out.println((endUsedMemory - startUsedMemory) + " bytes memory increase when running : File_S3");
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
		log.info("TalendJob: 'File_S3' - Finished - status: " + status + " returnCode: " + returnCode);

		return returnCode;

	}

	// only for OSGi env
	public void destroy() {
		closeS3Connections();

	}

	private void closeS3Connections() {
		try {
			com.amazonaws.services.s3.AmazonS3Client conn_tS3Connection_1 = (com.amazonaws.services.s3.AmazonS3Client) globalMap
					.get("conn_tS3Connection_1");
			if (conn_tS3Connection_1 != null) {
				conn_tS3Connection_1.shutdown();
			}
		} catch (java.lang.Exception e) {
		}
	}

	private java.util.Map<String, Object> getSharedConnections4REST() {
		java.util.Map<String, Object> connections = new java.util.HashMap<String, Object>();

		connections.put("conn_tS3Connection_1", globalMap.get("conn_tS3Connection_1"));

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
 * 203104 characters generated by Talend Cloud Data Fabric on the 11 January
 * 2024 at 10:41:52 AM SGT
 ************************************************************************************************/