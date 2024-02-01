
package democloud.demo_0_1;

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
 * Job: Demo Purpose: Demo<br>
 * Description: Blend Customers details with emails and check email for valid
 * pattern. Valid records are pushed into DW. Invalid records are re-routed to
 * Data Stewardship for remedial actions. <br>
 * 
 * @author Tan, Eric
 * @version 8.0.1.20231222_1430-patch
 * @status
 */
public class Demo implements TalendJob {
	static {
		System.setProperty("TalendJob.log", "Demo.log");
	}

	private static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(Demo.class);

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
	private final String jobName = "Demo";
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
			"_d2VcgIK_Ee6PMYwZwGxOMQ", "0.1");
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
					Demo.this.exception = e;
				}
			}
			if (!(e instanceof TalendException)) {
				try {
					for (java.lang.reflect.Method m : this.getClass().getEnclosingClass().getMethods()) {
						if (m.getName().compareTo(currentComponent + "_error") == 0) {
							m.invoke(Demo.this, new Object[] { e, currentComponent, globalMap });
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

	public void tPatternCheck_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

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

	public void tLogRow_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBInput_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDataStewardshipTaskOutput_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBInput_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tSendMail_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tSendMail_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tFileInputDelimited_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBInput_1_onSubJobError(exception, errorComponent, globalMap);
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

	public void tSendMail_1_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void talendJobLog_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public static class row4Struct implements routines.system.IPersistableRow<row4Struct> {
		final static byte[] commonByteArrayLock_DEMOCLOUD_Demo = new byte[0];
		static byte[] commonByteArray_DEMOCLOUD_Demo = new byte[0];

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
				if (length > commonByteArray_DEMOCLOUD_Demo.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_Demo.length == 0) {
						commonByteArray_DEMOCLOUD_Demo = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_Demo = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DEMOCLOUD_Demo, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_Demo, 0, length, utf8Charset);
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
				if (length > commonByteArray_DEMOCLOUD_Demo.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_Demo.length == 0) {
						commonByteArray_DEMOCLOUD_Demo = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_Demo = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DEMOCLOUD_Demo, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_Demo, 0, length, utf8Charset);
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

			synchronized (commonByteArrayLock_DEMOCLOUD_Demo) {

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

			synchronized (commonByteArrayLock_DEMOCLOUD_Demo) {

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
		public int compareTo(row4Struct other) {

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

	public static class row3Struct implements routines.system.IPersistableRow<row3Struct> {
		final static byte[] commonByteArrayLock_DEMOCLOUD_Demo = new byte[0];
		static byte[] commonByteArray_DEMOCLOUD_Demo = new byte[0];

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
				if (length > commonByteArray_DEMOCLOUD_Demo.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_Demo.length == 0) {
						commonByteArray_DEMOCLOUD_Demo = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_Demo = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DEMOCLOUD_Demo, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_Demo, 0, length, utf8Charset);
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
				if (length > commonByteArray_DEMOCLOUD_Demo.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_Demo.length == 0) {
						commonByteArray_DEMOCLOUD_Demo = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_Demo = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DEMOCLOUD_Demo, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_Demo, 0, length, utf8Charset);
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

			synchronized (commonByteArrayLock_DEMOCLOUD_Demo) {

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

			synchronized (commonByteArrayLock_DEMOCLOUD_Demo) {

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
		public int compareTo(row3Struct other) {

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

	public static class row5Struct implements routines.system.IPersistableRow<row5Struct> {
		final static byte[] commonByteArrayLock_DEMOCLOUD_Demo = new byte[0];
		static byte[] commonByteArray_DEMOCLOUD_Demo = new byte[0];

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
				if (length > commonByteArray_DEMOCLOUD_Demo.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_Demo.length == 0) {
						commonByteArray_DEMOCLOUD_Demo = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_Demo = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DEMOCLOUD_Demo, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_Demo, 0, length, utf8Charset);
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
				if (length > commonByteArray_DEMOCLOUD_Demo.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_Demo.length == 0) {
						commonByteArray_DEMOCLOUD_Demo = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_Demo = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DEMOCLOUD_Demo, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_Demo, 0, length, utf8Charset);
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

			synchronized (commonByteArrayLock_DEMOCLOUD_Demo) {

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

			synchronized (commonByteArrayLock_DEMOCLOUD_Demo) {

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
		public int compareTo(row5Struct other) {

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

	public static class CustomersOutStruct implements routines.system.IPersistableRow<CustomersOutStruct> {
		final static byte[] commonByteArrayLock_DEMOCLOUD_Demo = new byte[0];
		static byte[] commonByteArray_DEMOCLOUD_Demo = new byte[0];

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
				if (length > commonByteArray_DEMOCLOUD_Demo.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_Demo.length == 0) {
						commonByteArray_DEMOCLOUD_Demo = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_Demo = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DEMOCLOUD_Demo, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_Demo, 0, length, utf8Charset);
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
				if (length > commonByteArray_DEMOCLOUD_Demo.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_Demo.length == 0) {
						commonByteArray_DEMOCLOUD_Demo = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_Demo = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DEMOCLOUD_Demo, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_Demo, 0, length, utf8Charset);
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

			synchronized (commonByteArrayLock_DEMOCLOUD_Demo) {

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

			synchronized (commonByteArrayLock_DEMOCLOUD_Demo) {

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
		final static byte[] commonByteArrayLock_DEMOCLOUD_Demo = new byte[0];
		static byte[] commonByteArray_DEMOCLOUD_Demo = new byte[0];

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
				if (length > commonByteArray_DEMOCLOUD_Demo.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_Demo.length == 0) {
						commonByteArray_DEMOCLOUD_Demo = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_Demo = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DEMOCLOUD_Demo, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_Demo, 0, length, utf8Charset);
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
				if (length > commonByteArray_DEMOCLOUD_Demo.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_Demo.length == 0) {
						commonByteArray_DEMOCLOUD_Demo = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_Demo = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DEMOCLOUD_Demo, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_Demo, 0, length, utf8Charset);
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

			synchronized (commonByteArrayLock_DEMOCLOUD_Demo) {

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

			synchronized (commonByteArrayLock_DEMOCLOUD_Demo) {

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
		final static byte[] commonByteArrayLock_DEMOCLOUD_Demo = new byte[0];
		static byte[] commonByteArray_DEMOCLOUD_Demo = new byte[0];

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
				if (length > commonByteArray_DEMOCLOUD_Demo.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_Demo.length == 0) {
						commonByteArray_DEMOCLOUD_Demo = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_Demo = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DEMOCLOUD_Demo, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_Demo, 0, length, utf8Charset);
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
				if (length > commonByteArray_DEMOCLOUD_Demo.length) {
					if (length < 1024 && commonByteArray_DEMOCLOUD_Demo.length == 0) {
						commonByteArray_DEMOCLOUD_Demo = new byte[1024];
					} else {
						commonByteArray_DEMOCLOUD_Demo = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DEMOCLOUD_Demo, 0, length);
				strReturn = new String(commonByteArray_DEMOCLOUD_Demo, 0, length, utf8Charset);
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

			synchronized (commonByteArrayLock_DEMOCLOUD_Demo) {

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

			synchronized (commonByteArrayLock_DEMOCLOUD_Demo) {

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
		org.slf4j.MDC.put("_subJobPid", "JTZzUO_" + subJobPidCounter.getAndIncrement());

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
				row3Struct row3 = new row3Struct();
				row3Struct row4 = row3;
				row5Struct row5 = new row5Struct();

				/**
				 * [tLogRow_1 begin ] start
				 */

				ok_Hash.put("tLogRow_1", false);
				start_Hash.put("tLogRow_1", System.currentTimeMillis());

				currentComponent = "tLogRow_1";

				cLabel = "OutputConsole";

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row4");

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

					int[] colLengths = new int[14];

					public void addRow(String[] row) {

						for (int i = 0; i < 14; i++) {
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
						for (k = 0; k < (totals + 13 - name.length()) / 2; k++) {
							sb.append(' ');
						}
						sb.append(name);
						for (int i = 0; i < totals + 13 - name.length() - k; i++) {
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

						// last column
						for (int i = 0; i < colLengths[13] - fillChars[1].length() + 1; i++) {
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
						"MaritalStatus", "Salary", "Address", "City", "State", "Zip", "Phone", "Email", });
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

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row3");

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
							log4jParamters_tFileOutputDelimited_1.append("APPEND" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("INCLUDEHEADER" + " = " + "true");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("COMPRESS" + " = " + "false");
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
							log4jParamters_tFileOutputDelimited_1.append("FILE_EXIST_EXCEPTION" + " = " + "false");
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

				java.io.File fileToDelete_tFileOutputDelimited_1 = new java.io.File(fileName_tFileOutputDelimited_1);
				if (fileToDelete_tFileOutputDelimited_1.exists()) {
					fileToDelete_tFileOutputDelimited_1.delete();
				}
				outtFileOutputDelimited_1 = new java.io.BufferedWriter(new java.io.OutputStreamWriter(
						new java.io.FileOutputStream(fileName_tFileOutputDelimited_1, false), "ISO-8859-15"));
				resourceMap.put("out_tFileOutputDelimited_1", outtFileOutputDelimited_1);
				if (filetFileOutputDelimited_1.length() == 0) {
					outtFileOutputDelimited_1.write("Id");
					outtFileOutputDelimited_1.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("First_Name");
					outtFileOutputDelimited_1.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("Last_Name");
					outtFileOutputDelimited_1.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("Gender");
					outtFileOutputDelimited_1.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("Age");
					outtFileOutputDelimited_1.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("Occupation");
					outtFileOutputDelimited_1.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("MaritalStatus");
					outtFileOutputDelimited_1.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("Salary");
					outtFileOutputDelimited_1.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("Address");
					outtFileOutputDelimited_1.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("City");
					outtFileOutputDelimited_1.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("State");
					outtFileOutputDelimited_1.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("Zip");
					outtFileOutputDelimited_1.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("Phone");
					outtFileOutputDelimited_1.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("Email");
					outtFileOutputDelimited_1.write(OUT_DELIM_ROWSEP_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.flush();
				}

				resourceMap.put("nb_line_tFileOutputDelimited_1", nb_line_tFileOutputDelimited_1);

				/**
				 * [tFileOutputDelimited_1 begin ] stop
				 */

				/**
				 * [tDataStewardshipTaskOutput_1 begin ] start
				 */

				ok_Hash.put("tDataStewardshipTaskOutput_1", false);
				start_Hash.put("tDataStewardshipTaskOutput_1", System.currentTimeMillis());

				currentComponent = "tDataStewardshipTaskOutput_1";

				cLabel = "InvalidCustomers";

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row5");

				int tos_count_tDataStewardshipTaskOutput_1 = 0;

				if (enableLogStash) {
					talendJobLog.addCM("tDataStewardshipTaskOutput_1", "InvalidCustomers",
							"tDataStewardshipTaskOutput");
					talendJobLogProcess(globalMap);
				}

				boolean doesNodeBelongToRequest_tDataStewardshipTaskOutput_1 = 0 == 0;
				@SuppressWarnings("unchecked")
				java.util.Map<String, Object> restRequest_tDataStewardshipTaskOutput_1 = (java.util.Map<String, Object>) globalMap
						.get("restRequest");
				String currentTRestRequestOperation_tDataStewardshipTaskOutput_1 = (String) (restRequest_tDataStewardshipTaskOutput_1 != null
						? restRequest_tDataStewardshipTaskOutput_1.get("OPERATION")
						: null);

				org.talend.components.api.component.ComponentDefinition def_tDataStewardshipTaskOutput_1 = new org.talend.components.datastewardship.tdatastewardshiptaskoutput.TDataStewardshipTaskOutputDefinition();

				org.talend.components.api.component.runtime.Writer writer_tDataStewardshipTaskOutput_1 = null;
				org.talend.components.api.component.runtime.Reader reader_tDataStewardshipTaskOutput_1 = null;

				org.talend.components.datastewardship.tdatastewardshiptaskoutput.TDataStewardshipTaskOutputProperties props_tDataStewardshipTaskOutput_1 = (org.talend.components.datastewardship.tdatastewardshiptaskoutput.TDataStewardshipTaskOutputProperties) def_tDataStewardshipTaskOutput_1
						.createRuntimeProperties();
				props_tDataStewardshipTaskOutput_1.setValue("batchSize", 50);

				props_tDataStewardshipTaskOutput_1.setValue("campaignName", "c42f03ad5f6882cf57f9164faf263b04a");

				props_tDataStewardshipTaskOutput_1.setValue("campaignLabel", "Demo Campaign");

				props_tDataStewardshipTaskOutput_1.setValue("campaignType",
						org.talend.components.datastewardship.common.CampaignType.RESOLUTION);

				props_tDataStewardshipTaskOutput_1.setValue("laxSchema", false);

				props_tDataStewardshipTaskOutput_1.setValue("taskPriority", "NO_PRIORITY");

				props_tDataStewardshipTaskOutput_1.setValue("taskTags", "");

				props_tDataStewardshipTaskOutput_1.setValue("taskState", "New");

				props_tDataStewardshipTaskOutput_1.setValue("taskAssignee", "No Assignee");

				java.util.List<Object> tDataStewardshipTaskOutput_1_taskCommentsTable_fieldName = new java.util.ArrayList<Object>();

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldName.add("Id");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldName.add("First_Name");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldName.add("Last_Name");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldName.add("Gender");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldName.add("Age");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldName.add("Occupation");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldName.add("MaritalStatus");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldName.add("Salary");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldName.add("Address");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldName.add("City");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldName.add("State");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldName.add("Zip");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldName.add("Phone");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldName.add("Email");

				((org.talend.daikon.properties.Properties) props_tDataStewardshipTaskOutput_1.taskCommentsTable)
						.setValue("fieldName", tDataStewardshipTaskOutput_1_taskCommentsTable_fieldName);

				java.util.List<Object> tDataStewardshipTaskOutput_1_taskCommentsTable_fieldComment = new java.util.ArrayList<Object>();

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldComment.add("");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldComment.add("");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldComment.add("");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldComment.add("");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldComment.add("");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldComment.add("");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldComment.add("");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldComment.add("");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldComment.add("");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldComment.add("");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldComment.add("");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldComment.add("");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldComment.add("");

				tDataStewardshipTaskOutput_1_taskCommentsTable_fieldComment.add("");

				((org.talend.daikon.properties.Properties) props_tDataStewardshipTaskOutput_1.taskCommentsTable)
						.setValue("fieldComment", tDataStewardshipTaskOutput_1_taskCommentsTable_fieldComment);

				java.util.List<Object> tDataStewardshipTaskOutput_1_httpConfig_keys = new java.util.ArrayList<Object>();

				((org.talend.daikon.properties.Properties) props_tDataStewardshipTaskOutput_1.httpConfig)
						.setValue("keys", tDataStewardshipTaskOutput_1_httpConfig_keys);

				java.util.List<Object> tDataStewardshipTaskOutput_1_httpConfig_values = new java.util.ArrayList<Object>();

				((org.talend.daikon.properties.Properties) props_tDataStewardshipTaskOutput_1.httpConfig)
						.setValue("values", tDataStewardshipTaskOutput_1_httpConfig_values);

				class SchemaSettingTool_tDataStewardshipTaskOutput_1_1_fisrt {

					String getSchemaValue() {

						StringBuilder s = new StringBuilder();

						a("{\"type\":\"record\",", s);

						a("\"name\":\"tDataStewardshipTaskOutput_1\",\"fields\":[{", s);

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

						a("\"name\":\"TDS_DUE_DATE\",\"type\":[\"long\",\"null\"],\"di.table.comment\":\"\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"TDS_DUE_DATE\",\"talend.field.dbColumnName\":\"TDS_DUE_DATE\",\"di.column.talendType\":\"id_Long\",\"di.column.isNullable\":\"true\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"TDS_DUE_DATE\",\"di.column.relatedEntity\":\"\"},{",
								s);

						a("\"name\":\"TDS_EXTERNAL_ID\",\"type\":[\"string\",\"null\"],\"di.table.comment\":\"\",\"talend.isLocked\":\"true\",\"AVRO_TECHNICAL_KEY\":\"TDS_EXTERNAL_ID\",\"talend.field.dbColumnName\":\"TDS_EXTERNAL_ID\",\"di.column.talendType\":\"id_String\",\"di.column.isNullable\":\"true\",\"talend.field.pattern\":\"\",\"di.column.relationshipType\":\"\",\"di.table.label\":\"TDS_EXTERNAL_ID\",\"di.column.relatedEntity\":\"\"}],\"di.table.name\":\"tDataStewardshipTaskOutput_1\",\"di.table.label\":\"tDataStewardshipTaskOutput_1\"}",
								s);

						return s.toString();

					}

					void a(String part, StringBuilder strB) {
						strB.append(part);
					}

				}

				SchemaSettingTool_tDataStewardshipTaskOutput_1_1_fisrt sst_tDataStewardshipTaskOutput_1_1_fisrt = new SchemaSettingTool_tDataStewardshipTaskOutput_1_1_fisrt();

				props_tDataStewardshipTaskOutput_1.schema.setValue("schema", new org.apache.avro.Schema.Parser()
						.setValidateDefaults(false).parse(sst_tDataStewardshipTaskOutput_1_1_fisrt.getSchemaValue()));

				props_tDataStewardshipTaskOutput_1.connection.setValue("url",
						"https://tds.ap.cloud.talend.com/data-stewardship");

				props_tDataStewardshipTaskOutput_1.connection.setValue("username", "eric.tan@nfr.siftag.com");

				props_tDataStewardshipTaskOutput_1.connection.setValue("password",
						routines.system.PasswordEncryptUtil.decryptPassword(
								"enc:routine.encryption.key.v1:zf3zOhOp5rrChU/TRU28woFajJgJOf/ZmirvZhJkFfvB35Ajmh5GeA=="));

				props_tDataStewardshipTaskOutput_1.connection.refConnection.setValue("referenceDefinitionName",
						"datastewardship");

				if (org.talend.components.api.properties.ComponentReferenceProperties.ReferenceType.COMPONENT_INSTANCE == props_tDataStewardshipTaskOutput_1.connection.refConnection.referenceType
						.getValue()) {
					final String referencedComponentInstanceId_tDataStewardshipTaskOutput_1 = props_tDataStewardshipTaskOutput_1.connection.refConnection.componentInstanceId
							.getStringValue();
					if (referencedComponentInstanceId_tDataStewardshipTaskOutput_1 != null) {
						org.talend.daikon.properties.Properties referencedComponentProperties_tDataStewardshipTaskOutput_1 = (org.talend.daikon.properties.Properties) globalMap
								.get(referencedComponentInstanceId_tDataStewardshipTaskOutput_1
										+ "_COMPONENT_RUNTIME_PROPERTIES");
						props_tDataStewardshipTaskOutput_1.connection.refConnection
								.setReference(referencedComponentProperties_tDataStewardshipTaskOutput_1);
					}
				}
				globalMap.put("tDataStewardshipTaskOutput_1_COMPONENT_RUNTIME_PROPERTIES",
						props_tDataStewardshipTaskOutput_1);
				globalMap.putIfAbsent("TALEND_PRODUCT_VERSION", "8.0");
				globalMap.put("TALEND_COMPONENTS_VERSION", "0.37.27");
				java.net.URL mappings_url_tDataStewardshipTaskOutput_1 = this.getClass().getResource("/xmlMappings");
				globalMap.put("tDataStewardshipTaskOutput_1_MAPPINGS_URL", mappings_url_tDataStewardshipTaskOutput_1);

				org.talend.components.api.container.RuntimeContainer container_tDataStewardshipTaskOutput_1 = new org.talend.components.api.container.RuntimeContainer() {
					public Object getComponentData(String componentId, String key) {
						return globalMap.get(componentId + "_" + key);
					}

					public void setComponentData(String componentId, String key, Object data) {
						globalMap.put(componentId + "_" + key, data);
					}

					public String getCurrentComponentId() {
						return "tDataStewardshipTaskOutput_1";
					}

					public Object getGlobalData(String key) {
						return globalMap.get(key);
					}
				};

				int nb_line_tDataStewardshipTaskOutput_1 = 0;

				org.talend.components.api.component.ConnectorTopology topology_tDataStewardshipTaskOutput_1 = null;
				topology_tDataStewardshipTaskOutput_1 = org.talend.components.api.component.ConnectorTopology.INCOMING;

				org.talend.daikon.runtime.RuntimeInfo runtime_info_tDataStewardshipTaskOutput_1 = def_tDataStewardshipTaskOutput_1
						.getRuntimeInfo(org.talend.components.api.component.runtime.ExecutionEngine.DI,
								props_tDataStewardshipTaskOutput_1, topology_tDataStewardshipTaskOutput_1);
				java.util.Set<org.talend.components.api.component.ConnectorTopology> supported_connector_topologies_tDataStewardshipTaskOutput_1 = def_tDataStewardshipTaskOutput_1
						.getSupportedConnectorTopologies();

				org.talend.components.api.component.runtime.RuntimableRuntime componentRuntime_tDataStewardshipTaskOutput_1 = (org.talend.components.api.component.runtime.RuntimableRuntime) (Class
						.forName(runtime_info_tDataStewardshipTaskOutput_1.getRuntimeClassName()).newInstance());
				org.talend.daikon.properties.ValidationResult initVr_tDataStewardshipTaskOutput_1 = componentRuntime_tDataStewardshipTaskOutput_1
						.initialize(container_tDataStewardshipTaskOutput_1, props_tDataStewardshipTaskOutput_1);

				if (initVr_tDataStewardshipTaskOutput_1
						.getStatus() == org.talend.daikon.properties.ValidationResult.Result.ERROR) {
					throw new RuntimeException(initVr_tDataStewardshipTaskOutput_1.getMessage());
				}

				if (componentRuntime_tDataStewardshipTaskOutput_1 instanceof org.talend.components.api.component.runtime.ComponentDriverInitialization) {
					org.talend.components.api.component.runtime.ComponentDriverInitialization compDriverInitialization_tDataStewardshipTaskOutput_1 = (org.talend.components.api.component.runtime.ComponentDriverInitialization) componentRuntime_tDataStewardshipTaskOutput_1;
					compDriverInitialization_tDataStewardshipTaskOutput_1
							.runAtDriver(container_tDataStewardshipTaskOutput_1);
				}

				org.talend.components.api.component.runtime.SourceOrSink sourceOrSink_tDataStewardshipTaskOutput_1 = null;
				if (componentRuntime_tDataStewardshipTaskOutput_1 instanceof org.talend.components.api.component.runtime.SourceOrSink) {
					sourceOrSink_tDataStewardshipTaskOutput_1 = (org.talend.components.api.component.runtime.SourceOrSink) componentRuntime_tDataStewardshipTaskOutput_1;
					if (doesNodeBelongToRequest_tDataStewardshipTaskOutput_1) {
						org.talend.daikon.properties.ValidationResult vr_tDataStewardshipTaskOutput_1 = sourceOrSink_tDataStewardshipTaskOutput_1
								.validate(container_tDataStewardshipTaskOutput_1);
						if (vr_tDataStewardshipTaskOutput_1
								.getStatus() == org.talend.daikon.properties.ValidationResult.Result.ERROR) {
							throw new RuntimeException(vr_tDataStewardshipTaskOutput_1.getMessage());
						}
					}
				}

				org.talend.codegen.enforcer.IncomingSchemaEnforcer incomingEnforcer_tDataStewardshipTaskOutput_1 = null;
				if (sourceOrSink_tDataStewardshipTaskOutput_1 instanceof org.talend.components.api.component.runtime.Sink) {
					org.talend.components.api.component.runtime.Sink sink_tDataStewardshipTaskOutput_1 = (org.talend.components.api.component.runtime.Sink) sourceOrSink_tDataStewardshipTaskOutput_1;
					org.talend.components.api.component.runtime.WriteOperation writeOperation_tDataStewardshipTaskOutput_1 = sink_tDataStewardshipTaskOutput_1
							.createWriteOperation();
					if (doesNodeBelongToRequest_tDataStewardshipTaskOutput_1) {
						writeOperation_tDataStewardshipTaskOutput_1.initialize(container_tDataStewardshipTaskOutput_1);
					}
					writer_tDataStewardshipTaskOutput_1 = writeOperation_tDataStewardshipTaskOutput_1
							.createWriter(container_tDataStewardshipTaskOutput_1);
					if (doesNodeBelongToRequest_tDataStewardshipTaskOutput_1) {
						writer_tDataStewardshipTaskOutput_1.open("tDataStewardshipTaskOutput_1");
					}

					resourceMap.put("writer_tDataStewardshipTaskOutput_1", writer_tDataStewardshipTaskOutput_1);
				} // end of "sourceOrSink_tDataStewardshipTaskOutput_1 instanceof ...Sink"
				org.talend.components.api.component.Connector c_tDataStewardshipTaskOutput_1 = null;
				for (org.talend.components.api.component.Connector currentConnector : props_tDataStewardshipTaskOutput_1
						.getAvailableConnectors(null, false)) {
					if (currentConnector.getName().equals("MAIN")) {
						c_tDataStewardshipTaskOutput_1 = currentConnector;
						break;
					}
				}
				org.apache.avro.Schema designSchema_tDataStewardshipTaskOutput_1 = props_tDataStewardshipTaskOutput_1
						.getSchema(c_tDataStewardshipTaskOutput_1, false);
				incomingEnforcer_tDataStewardshipTaskOutput_1 = new org.talend.codegen.enforcer.IncomingSchemaEnforcer(
						designSchema_tDataStewardshipTaskOutput_1);

				java.lang.Iterable<?> outgoingMainRecordsList_tDataStewardshipTaskOutput_1 = new java.util.ArrayList<Object>();
				java.util.Iterator outgoingMainRecordsIt_tDataStewardshipTaskOutput_1 = null;

				/**
				 * [tDataStewardshipTaskOutput_1 begin ] stop
				 */

				/**
				 * [tPatternCheck_1 begin ] start
				 */

				ok_Hash.put("tPatternCheck_1", false);
				start_Hash.put("tPatternCheck_1", System.currentTimeMillis());

				currentComponent = "tPatternCheck_1";

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "CustomersOut");

				int tos_count_tPatternCheck_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tPatternCheck_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tPatternCheck_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tPatternCheck_1 = new StringBuilder();
							log4jParamters_tPatternCheck_1.append("Parameters:");
							log4jParamters_tPatternCheck_1.append("COLUMN2CHECK" + " = " + "Email");
							log4jParamters_tPatternCheck_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tPatternCheck_1 - " + (log4jParamters_tPatternCheck_1));
						}
					}
					new BytesLimit65535_tPatternCheck_1().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tPatternCheck_1", "tPatternCheck_1", "tPatternCheck");
					talendJobLogProcess(globalMap);
				}

				String stringpattern_tPatternCheck_1 = "^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";

				boolean result_tPatternCheck_1 = false;
				int nb_line_tPatternCheck_1 = 0;
				int nb_line_ok_tPatternCheck_1 = 0;
				int nb_line_reject_tPatternCheck_1 = 0;
				java.util.regex.Pattern pattern_tPatternCheck_1 = null;
				try {
					pattern_tPatternCheck_1 = java.util.regex.Pattern.compile(stringpattern_tPatternCheck_1);
				} catch (java.util.regex.PatternSyntaxException pe_tPatternCheck_1) {
					System.err.println("Error in component tPatternCheck: the pattern defined contains errors");
					throw pe_tPatternCheck_1;
				}

				/**
				 * [tPatternCheck_1 begin ] stop
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

				cLabel = "CustomersDetails";

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
									"enc:routine.encryption.key.v1:4m4H4kfPG8TSEzgF+WSOQZ5sugQpyXakQsl3mmKxmyBSR1pw4A==")
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
					talendJobLog.addCM("tDBInput_1", "CustomersDetails", "tMysqlInput");
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
						"enc:routine.encryption.key.v1:yhdwhx+0edVE9wxFrMsa31ttrfBuqUSGMg8rmnicZmRoUo5vDQ==");

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

						cLabel = "CustomersDetails";

						tos_count_tDBInput_1++;

						/**
						 * [tDBInput_1 main ] stop
						 */

						/**
						 * [tDBInput_1 process_data_begin ] start
						 */

						currentComponent = "tDBInput_1";

						cLabel = "CustomersDetails";

						/**
						 * [tDBInput_1 process_data_begin ] stop
						 */

						/**
						 * [tMap_1 main ] start
						 */

						currentComponent = "tMap_1";

						if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

								, "row1", "tDBInput_1", "CustomersDetails", "tMysqlInput", "tMap_1", "tMap_1", "tMap"

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
							 * [tPatternCheck_1 main ] start
							 */

							currentComponent = "tPatternCheck_1";

							if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

									, "CustomersOut", "tMap_1", "tMap_1", "tMap", "tPatternCheck_1", "tPatternCheck_1",
									"tPatternCheck"

							)) {
								talendJobLogProcess(globalMap);
							}

							if (log.isTraceEnabled()) {
								log.trace("CustomersOut - " + (CustomersOut == null ? "" : CustomersOut.toLogString()));
							}

							row5 = null;
							row3 = null;

							try {
								java.util.regex.Matcher matcher_tPatternCheck_1 = pattern_tPatternCheck_1
										.matcher(CustomersOut.Email);
								result_tPatternCheck_1 = matcher_tPatternCheck_1.find();
							} catch (NullPointerException ne) {
								result_tPatternCheck_1 = false;
							}

							if (!result_tPatternCheck_1) {
								if (row5 == null) {
									row5 = new row5Struct();
								}
								row5.Id = CustomersOut.Id;
								row5.First_Name = CustomersOut.First_Name;
								row5.Last_Name = CustomersOut.Last_Name;
								row5.Gender = CustomersOut.Gender;
								row5.Age = CustomersOut.Age;
								row5.Occupation = CustomersOut.Occupation;
								row5.MaritalStatus = CustomersOut.MaritalStatus;
								row5.Salary = CustomersOut.Salary;
								row5.Address = CustomersOut.Address;
								row5.City = CustomersOut.City;
								row5.State = CustomersOut.State;
								row5.Zip = CustomersOut.Zip;
								row5.Phone = CustomersOut.Phone;
								row5.Email = CustomersOut.Email;
								nb_line_reject_tPatternCheck_1++;

							} else {
								if (row3 == null) {
									row3 = new row3Struct();
								}
								row3.Id = CustomersOut.Id;
								row3.First_Name = CustomersOut.First_Name;
								row3.Last_Name = CustomersOut.Last_Name;
								row3.Gender = CustomersOut.Gender;
								row3.Age = CustomersOut.Age;
								row3.Occupation = CustomersOut.Occupation;
								row3.MaritalStatus = CustomersOut.MaritalStatus;
								row3.Salary = CustomersOut.Salary;
								row3.Address = CustomersOut.Address;
								row3.City = CustomersOut.City;
								row3.State = CustomersOut.State;
								row3.Zip = CustomersOut.Zip;
								row3.Phone = CustomersOut.Phone;
								row3.Email = CustomersOut.Email;
								nb_line_ok_tPatternCheck_1++;
							}
							nb_line_tPatternCheck_1++;

							tos_count_tPatternCheck_1++;

							/**
							 * [tPatternCheck_1 main ] stop
							 */

							/**
							 * [tPatternCheck_1 process_data_begin ] start
							 */

							currentComponent = "tPatternCheck_1";

							/**
							 * [tPatternCheck_1 process_data_begin ] stop
							 */
// Start of branch "row3"
							if (row3 != null) {

								/**
								 * [tFileOutputDelimited_1 main ] start
								 */

								currentComponent = "tFileOutputDelimited_1";

								cLabel = "ValidCustomers";

								if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

										, "row3", "tPatternCheck_1", "tPatternCheck_1", "tPatternCheck",
										"tFileOutputDelimited_1", "ValidCustomers", "tFileOutputDelimited"

								)) {
									talendJobLogProcess(globalMap);
								}

								if (log.isTraceEnabled()) {
									log.trace("row3 - " + (row3 == null ? "" : row3.toLogString()));
								}

								StringBuilder sb_tFileOutputDelimited_1 = new StringBuilder();
								if (row3.Id != null) {
									sb_tFileOutputDelimited_1.append(row3.Id);
								}
								sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
								if (row3.First_Name != null) {
									sb_tFileOutputDelimited_1.append(row3.First_Name);
								}
								sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
								if (row3.Last_Name != null) {
									sb_tFileOutputDelimited_1.append(row3.Last_Name);
								}
								sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
								if (row3.Gender != null) {
									sb_tFileOutputDelimited_1.append(row3.Gender);
								}
								sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
								if (row3.Age != null) {
									sb_tFileOutputDelimited_1.append(row3.Age);
								}
								sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
								if (row3.Occupation != null) {
									sb_tFileOutputDelimited_1.append(row3.Occupation);
								}
								sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
								if (row3.MaritalStatus != null) {
									sb_tFileOutputDelimited_1.append(row3.MaritalStatus);
								}
								sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
								if (row3.Salary != null) {
									sb_tFileOutputDelimited_1.append(row3.Salary);
								}
								sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
								if (row3.Address != null) {
									sb_tFileOutputDelimited_1.append(row3.Address);
								}
								sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
								if (row3.City != null) {
									sb_tFileOutputDelimited_1.append(row3.City);
								}
								sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
								if (row3.State != null) {
									sb_tFileOutputDelimited_1.append(row3.State);
								}
								sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
								if (row3.Zip != null) {
									sb_tFileOutputDelimited_1.append(row3.Zip);
								}
								sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
								if (row3.Phone != null) {
									sb_tFileOutputDelimited_1.append(row3.Phone);
								}
								sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
								if (row3.Email != null) {
									sb_tFileOutputDelimited_1.append(row3.Email);
								}
								sb_tFileOutputDelimited_1.append(OUT_DELIM_ROWSEP_tFileOutputDelimited_1);

								nb_line_tFileOutputDelimited_1++;
								resourceMap.put("nb_line_tFileOutputDelimited_1", nb_line_tFileOutputDelimited_1);

								outtFileOutputDelimited_1.write(sb_tFileOutputDelimited_1.toString());
								log.debug("tFileOutputDelimited_1 - Writing the record "
										+ nb_line_tFileOutputDelimited_1 + ".");

								row4 = row3;

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

										, "row4", "tFileOutputDelimited_1", "ValidCustomers", "tFileOutputDelimited",
										"tLogRow_1", "OutputConsole", "tLogRow"

								)) {
									talendJobLogProcess(globalMap);
								}

								if (log.isTraceEnabled()) {
									log.trace("row4 - " + (row4 == null ? "" : row4.toLogString()));
								}

///////////////////////		

								String[] row_tLogRow_1 = new String[14];

								if (row4.Id != null) { //
									row_tLogRow_1[0] = String.valueOf(row4.Id);

								} //

								if (row4.First_Name != null) { //
									row_tLogRow_1[1] = String.valueOf(row4.First_Name);

								} //

								if (row4.Last_Name != null) { //
									row_tLogRow_1[2] = String.valueOf(row4.Last_Name);

								} //

								if (row4.Gender != null) { //
									row_tLogRow_1[3] = String.valueOf(row4.Gender);

								} //

								if (row4.Age != null) { //
									row_tLogRow_1[4] = String.valueOf(row4.Age);

								} //

								if (row4.Occupation != null) { //
									row_tLogRow_1[5] = String.valueOf(row4.Occupation);

								} //

								if (row4.MaritalStatus != null) { //
									row_tLogRow_1[6] = String.valueOf(row4.MaritalStatus);

								} //

								if (row4.Salary != null) { //
									row_tLogRow_1[7] = String.valueOf(row4.Salary);

								} //

								if (row4.Address != null) { //
									row_tLogRow_1[8] = String.valueOf(row4.Address);

								} //

								if (row4.City != null) { //
									row_tLogRow_1[9] = String.valueOf(row4.City);

								} //

								if (row4.State != null) { //
									row_tLogRow_1[10] = String.valueOf(row4.State);

								} //

								if (row4.Zip != null) { //
									row_tLogRow_1[11] = String.valueOf(row4.Zip);

								} //

								if (row4.Phone != null) { //
									row_tLogRow_1[12] = String.valueOf(row4.Phone);

								} //

								if (row4.Email != null) { //
									row_tLogRow_1[13] = String.valueOf(row4.Email);

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

							} // End of branch "row3"

// Start of branch "row5"
							if (row5 != null) {

								/**
								 * [tDataStewardshipTaskOutput_1 main ] start
								 */

								currentComponent = "tDataStewardshipTaskOutput_1";

								cLabel = "InvalidCustomers";

								if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

										, "row5", "tPatternCheck_1", "tPatternCheck_1", "tPatternCheck",
										"tDataStewardshipTaskOutput_1", "InvalidCustomers", "tDataStewardshipTaskOutput"

								)) {
									talendJobLogProcess(globalMap);
								}

								if (log.isTraceEnabled()) {
									log.trace("row5 - " + (row5 == null ? "" : row5.toLogString()));
								}

								boolean shouldCreateRuntimeSchemaForIncomingNode = false;
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getDesignSchema()
												.getField("Id") == null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.addIncomingNodeField("Id",
											((Object) row5.Id).getClass().getCanonicalName());
									shouldCreateRuntimeSchemaForIncomingNode = true;
								}
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getDesignSchema()
												.getField("First_Name") == null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.addIncomingNodeField("First_Name",
											((Object) row5.First_Name).getClass().getCanonicalName());
									shouldCreateRuntimeSchemaForIncomingNode = true;
								}
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getDesignSchema()
												.getField("Last_Name") == null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.addIncomingNodeField("Last_Name",
											((Object) row5.Last_Name).getClass().getCanonicalName());
									shouldCreateRuntimeSchemaForIncomingNode = true;
								}
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getDesignSchema()
												.getField("Gender") == null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.addIncomingNodeField("Gender",
											((Object) row5.Gender).getClass().getCanonicalName());
									shouldCreateRuntimeSchemaForIncomingNode = true;
								}
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getDesignSchema()
												.getField("Age") == null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.addIncomingNodeField("Age",
											((Object) row5.Age).getClass().getCanonicalName());
									shouldCreateRuntimeSchemaForIncomingNode = true;
								}
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getDesignSchema()
												.getField("Occupation") == null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.addIncomingNodeField("Occupation",
											((Object) row5.Occupation).getClass().getCanonicalName());
									shouldCreateRuntimeSchemaForIncomingNode = true;
								}
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getDesignSchema()
												.getField("MaritalStatus") == null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.addIncomingNodeField("MaritalStatus",
											((Object) row5.MaritalStatus).getClass().getCanonicalName());
									shouldCreateRuntimeSchemaForIncomingNode = true;
								}
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getDesignSchema()
												.getField("Salary") == null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.addIncomingNodeField("Salary",
											((Object) row5.Salary).getClass().getCanonicalName());
									shouldCreateRuntimeSchemaForIncomingNode = true;
								}
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getDesignSchema()
												.getField("Address") == null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.addIncomingNodeField("Address",
											((Object) row5.Address).getClass().getCanonicalName());
									shouldCreateRuntimeSchemaForIncomingNode = true;
								}
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getDesignSchema()
												.getField("City") == null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.addIncomingNodeField("City",
											((Object) row5.City).getClass().getCanonicalName());
									shouldCreateRuntimeSchemaForIncomingNode = true;
								}
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getDesignSchema()
												.getField("State") == null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.addIncomingNodeField("State",
											((Object) row5.State).getClass().getCanonicalName());
									shouldCreateRuntimeSchemaForIncomingNode = true;
								}
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getDesignSchema()
												.getField("Zip") == null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.addIncomingNodeField("Zip",
											((Object) row5.Zip).getClass().getCanonicalName());
									shouldCreateRuntimeSchemaForIncomingNode = true;
								}
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getDesignSchema()
												.getField("Phone") == null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.addIncomingNodeField("Phone",
											((Object) row5.Phone).getClass().getCanonicalName());
									shouldCreateRuntimeSchemaForIncomingNode = true;
								}
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getDesignSchema()
												.getField("Email") == null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.addIncomingNodeField("Email",
											((Object) row5.Email).getClass().getCanonicalName());
									shouldCreateRuntimeSchemaForIncomingNode = true;
								}
								if (shouldCreateRuntimeSchemaForIncomingNode
										&& incomingEnforcer_tDataStewardshipTaskOutput_1 != null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.createRuntimeSchema();
								}
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.createNewRecord();
								}
								// skip the put action if the input column doesn't appear in component runtime
								// schema
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getRuntimeSchema()
												.getField("Id") != null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.put("Id", row5.Id);
								}
								// skip the put action if the input column doesn't appear in component runtime
								// schema
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getRuntimeSchema()
												.getField("First_Name") != null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.put("First_Name", row5.First_Name);
								}
								// skip the put action if the input column doesn't appear in component runtime
								// schema
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getRuntimeSchema()
												.getField("Last_Name") != null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.put("Last_Name", row5.Last_Name);
								}
								// skip the put action if the input column doesn't appear in component runtime
								// schema
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getRuntimeSchema()
												.getField("Gender") != null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.put("Gender", row5.Gender);
								}
								// skip the put action if the input column doesn't appear in component runtime
								// schema
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getRuntimeSchema()
												.getField("Age") != null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.put("Age", row5.Age);
								}
								// skip the put action if the input column doesn't appear in component runtime
								// schema
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getRuntimeSchema()
												.getField("Occupation") != null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.put("Occupation", row5.Occupation);
								}
								// skip the put action if the input column doesn't appear in component runtime
								// schema
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getRuntimeSchema()
												.getField("MaritalStatus") != null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.put("MaritalStatus",
											row5.MaritalStatus);
								}
								// skip the put action if the input column doesn't appear in component runtime
								// schema
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getRuntimeSchema()
												.getField("Salary") != null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.put("Salary", row5.Salary);
								}
								// skip the put action if the input column doesn't appear in component runtime
								// schema
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getRuntimeSchema()
												.getField("Address") != null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.put("Address", row5.Address);
								}
								// skip the put action if the input column doesn't appear in component runtime
								// schema
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getRuntimeSchema()
												.getField("City") != null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.put("City", row5.City);
								}
								// skip the put action if the input column doesn't appear in component runtime
								// schema
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getRuntimeSchema()
												.getField("State") != null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.put("State", row5.State);
								}
								// skip the put action if the input column doesn't appear in component runtime
								// schema
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getRuntimeSchema()
												.getField("Zip") != null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.put("Zip", row5.Zip);
								}
								// skip the put action if the input column doesn't appear in component runtime
								// schema
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getRuntimeSchema()
												.getField("Phone") != null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.put("Phone", row5.Phone);
								}
								// skip the put action if the input column doesn't appear in component runtime
								// schema
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null
										&& incomingEnforcer_tDataStewardshipTaskOutput_1.getRuntimeSchema()
												.getField("Email") != null) {
									incomingEnforcer_tDataStewardshipTaskOutput_1.put("Email", row5.Email);
								}

								org.apache.avro.generic.IndexedRecord data_tDataStewardshipTaskOutput_1 = null;
								if (incomingEnforcer_tDataStewardshipTaskOutput_1 != null) {
									data_tDataStewardshipTaskOutput_1 = incomingEnforcer_tDataStewardshipTaskOutput_1
											.getCurrentRecord();
								}

								if (writer_tDataStewardshipTaskOutput_1 != null
										&& data_tDataStewardshipTaskOutput_1 != null) {
									writer_tDataStewardshipTaskOutput_1.write(data_tDataStewardshipTaskOutput_1);
								}

								nb_line_tDataStewardshipTaskOutput_1++;

								tos_count_tDataStewardshipTaskOutput_1++;

								/**
								 * [tDataStewardshipTaskOutput_1 main ] stop
								 */

								/**
								 * [tDataStewardshipTaskOutput_1 process_data_begin ] start
								 */

								currentComponent = "tDataStewardshipTaskOutput_1";

								cLabel = "InvalidCustomers";

								/**
								 * [tDataStewardshipTaskOutput_1 process_data_begin ] stop
								 */

								/**
								 * [tDataStewardshipTaskOutput_1 process_data_end ] start
								 */

								currentComponent = "tDataStewardshipTaskOutput_1";

								cLabel = "InvalidCustomers";

								/**
								 * [tDataStewardshipTaskOutput_1 process_data_end ] stop
								 */

							} // End of branch "row5"

							/**
							 * [tPatternCheck_1 process_data_end ] start
							 */

							currentComponent = "tPatternCheck_1";

							/**
							 * [tPatternCheck_1 process_data_end ] stop
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

						cLabel = "CustomersDetails";

						/**
						 * [tDBInput_1 process_data_end ] stop
						 */

						/**
						 * [tDBInput_1 end ] start
						 */

						currentComponent = "tDBInput_1";

						cLabel = "CustomersDetails";

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
						"tDBInput_1", "CustomersDetails", "tMysqlInput", "tMap_1", "tMap_1", "tMap", "output")) {
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
				 * [tPatternCheck_1 end ] start
				 */

				currentComponent = "tPatternCheck_1";

				globalMap.put("tPatternCheck_1_NB_LINE", nb_line_tPatternCheck_1);
				globalMap.put("tPatternCheck_1_NB_LINE_OK", nb_line_ok_tPatternCheck_1);
				globalMap.put("tPatternCheck_1_NB_LINE_REJECT", nb_line_reject_tPatternCheck_1);

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "CustomersOut", 2, 0,
						"tMap_1", "tMap_1", "tMap", "tPatternCheck_1", "tPatternCheck_1", "tPatternCheck", "output")) {
					talendJobLogProcess(globalMap);
				}

				if (log.isDebugEnabled())
					log.debug("tPatternCheck_1 - " + ("Done."));

				ok_Hash.put("tPatternCheck_1", true);
				end_Hash.put("tPatternCheck_1", System.currentTimeMillis());

				/**
				 * [tPatternCheck_1 end ] stop
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

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row3", 2, 0,
						"tPatternCheck_1", "tPatternCheck_1", "tPatternCheck", "tFileOutputDelimited_1",
						"ValidCustomers", "tFileOutputDelimited", "output")) {
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

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row4", 2, 0,
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

				/**
				 * [tDataStewardshipTaskOutput_1 end ] start
				 */

				currentComponent = "tDataStewardshipTaskOutput_1";

				cLabel = "InvalidCustomers";

// end of generic

				resourceMap.put("finish_tDataStewardshipTaskOutput_1", Boolean.TRUE);

				java.util.Map<String, Object> resultMap_tDataStewardshipTaskOutput_1 = null;
				if (writer_tDataStewardshipTaskOutput_1 != null) {
					org.talend.components.api.component.runtime.Result resultObject_tDataStewardshipTaskOutput_1 = (org.talend.components.api.component.runtime.Result) writer_tDataStewardshipTaskOutput_1
							.close();
					resultMap_tDataStewardshipTaskOutput_1 = writer_tDataStewardshipTaskOutput_1.getWriteOperation()
							.finalize(
									java.util.Arrays.<org.talend.components.api.component.runtime.Result>asList(
											resultObject_tDataStewardshipTaskOutput_1),
									container_tDataStewardshipTaskOutput_1);
				}
				if (resultMap_tDataStewardshipTaskOutput_1 != null) {
					for (java.util.Map.Entry<String, Object> entry_tDataStewardshipTaskOutput_1 : resultMap_tDataStewardshipTaskOutput_1
							.entrySet()) {
						switch (entry_tDataStewardshipTaskOutput_1.getKey()) {
						case org.talend.components.api.component.ComponentDefinition.RETURN_ERROR_MESSAGE:
							container_tDataStewardshipTaskOutput_1.setComponentData("tDataStewardshipTaskOutput_1",
									"ERROR_MESSAGE", entry_tDataStewardshipTaskOutput_1.getValue());
							break;
						case org.talend.components.api.component.ComponentDefinition.RETURN_TOTAL_RECORD_COUNT:
							container_tDataStewardshipTaskOutput_1.setComponentData("tDataStewardshipTaskOutput_1",
									"NB_LINE", entry_tDataStewardshipTaskOutput_1.getValue());
							break;
						case org.talend.components.api.component.ComponentDefinition.RETURN_SUCCESS_RECORD_COUNT:
							container_tDataStewardshipTaskOutput_1.setComponentData("tDataStewardshipTaskOutput_1",
									"NB_SUCCESS", entry_tDataStewardshipTaskOutput_1.getValue());
							break;
						case org.talend.components.api.component.ComponentDefinition.RETURN_REJECT_RECORD_COUNT:
							container_tDataStewardshipTaskOutput_1.setComponentData("tDataStewardshipTaskOutput_1",
									"NB_REJECT", entry_tDataStewardshipTaskOutput_1.getValue());
							break;
						default:
							StringBuilder studio_key_tDataStewardshipTaskOutput_1 = new StringBuilder();
							for (int i_tDataStewardshipTaskOutput_1 = 0; i_tDataStewardshipTaskOutput_1 < entry_tDataStewardshipTaskOutput_1
									.getKey().length(); i_tDataStewardshipTaskOutput_1++) {
								char ch_tDataStewardshipTaskOutput_1 = entry_tDataStewardshipTaskOutput_1.getKey()
										.charAt(i_tDataStewardshipTaskOutput_1);
								if (Character.isUpperCase(ch_tDataStewardshipTaskOutput_1)
										&& i_tDataStewardshipTaskOutput_1 > 0) {
									studio_key_tDataStewardshipTaskOutput_1.append('_');
								}
								studio_key_tDataStewardshipTaskOutput_1.append(ch_tDataStewardshipTaskOutput_1);
							}
							container_tDataStewardshipTaskOutput_1.setComponentData(
									"tDataStewardshipTaskOutput_1", studio_key_tDataStewardshipTaskOutput_1.toString()
											.toUpperCase(java.util.Locale.ENGLISH),
									entry_tDataStewardshipTaskOutput_1.getValue());
							break;
						}
					}
				}

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row5", 2, 0,
						"tPatternCheck_1", "tPatternCheck_1", "tPatternCheck", "tDataStewardshipTaskOutput_1",
						"InvalidCustomers", "tDataStewardshipTaskOutput", "output")) {
					talendJobLogProcess(globalMap);
				}

				ok_Hash.put("tDataStewardshipTaskOutput_1", true);
				end_Hash.put("tDataStewardshipTaskOutput_1", System.currentTimeMillis());

				if (execStat) {
					runStat.updateStatOnConnection("OnComponentOk1", 0, "ok");
				}
				tSendMail_1Process(globalMap);

				/**
				 * [tDataStewardshipTaskOutput_1 end ] stop
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

				cLabel = "CustomersDetails";

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
				 * [tPatternCheck_1 finally ] start
				 */

				currentComponent = "tPatternCheck_1";

				/**
				 * [tPatternCheck_1 finally ] stop
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

				/**
				 * [tDataStewardshipTaskOutput_1 finally ] start
				 */

				currentComponent = "tDataStewardshipTaskOutput_1";

				cLabel = "InvalidCustomers";

// finally of generic

				if (resourceMap.get("finish_tDataStewardshipTaskOutput_1") == null) {
					if (resourceMap.get("writer_tDataStewardshipTaskOutput_1") != null) {
						try {
							((org.talend.components.api.component.runtime.Writer) resourceMap
									.get("writer_tDataStewardshipTaskOutput_1")).close();
						} catch (java.io.IOException e_tDataStewardshipTaskOutput_1) {
							String errorMessage_tDataStewardshipTaskOutput_1 = "failed to release the resource in tDataStewardshipTaskOutput_1 :"
									+ e_tDataStewardshipTaskOutput_1.getMessage();
							System.err.println(errorMessage_tDataStewardshipTaskOutput_1);
						}
					}
				}

				/**
				 * [tDataStewardshipTaskOutput_1 finally ] stop
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

	public void tSendMail_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tSendMail_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdcInfo.forEach(org.slf4j.MDC::put);
		org.slf4j.MDC.put("_subJobName", "tSendMail_1");
		org.slf4j.MDC.put("_subJobPid", "rOv4tF_" + subJobPidCounter.getAndIncrement());

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
				 * [tSendMail_1 begin ] start
				 */

				ok_Hash.put("tSendMail_1", false);
				start_Hash.put("tSendMail_1", System.currentTimeMillis());

				currentComponent = "tSendMail_1";

				cLabel = "NotifyDataSteward";

				int tos_count_tSendMail_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tSendMail_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tSendMail_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tSendMail_1 = new StringBuilder();
							log4jParamters_tSendMail_1.append("Parameters:");
							log4jParamters_tSendMail_1.append("TO" + " = " + "\"tancse@gmail.com\"");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("FROM" + " = " + "\"eric.tan@sift-ag.com\"");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("NEED_PERSONAL_NAME" + " = " + "false");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("CC" + " = " + "");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("BCC" + " = " + "");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1
									.append("SUBJECT" + " = " + "\"Talend Open Studio notification\"");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("MESSAGE" + " = "
									+ "\"Hello Eric, the job Demo 0.1 encountered a data quality issue.  There are unassigned tasks in Demo Campaign that require your attention.\"");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("CHECK_ATTACHMENT" + " = " + "false");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("ATTACHMENTS" + " = " + "[]");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("HEADERS" + " = " + "[]");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("SMTP_HOST" + " = " + "\"smtp.gmail.com\"");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("SMTP_PORT" + " = " + "465");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("SSL" + " = " + "true");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("STARTTLS" + " = " + "false");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("IMPORTANCE" + " = " + "Normal");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("AUTH_MODE" + " = " + "BASIC");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("AUTH_USERNAME" + " = " + "\"eric.tan@sift-ag.com\"");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("AUTH_PASSWORD" + " = " + String.valueOf(
									"enc:routine.encryption.key.v1:I0apQE5F7PEN2WRXF5i9y3GrQhmJgkylzCpxsm77ETy2Jz+HRg==")
									.substring(0, 4) + "...");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("DIE_ON_ERROR" + " = " + "false");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("TEXT_SUBTYPE" + " = " + "plain");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("ENCODING" + " = " + "\"ISO-8859-15\"");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("SET_LOCALHOST" + " = " + "false");
							log4jParamters_tSendMail_1.append(" | ");
							log4jParamters_tSendMail_1.append("CONFIGS" + " = " + "[]");
							log4jParamters_tSendMail_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tSendMail_1 - " + (log4jParamters_tSendMail_1));
						}
					}
					new BytesLimit65535_tSendMail_1().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tSendMail_1", "NotifyDataSteward", "tSendMail");
					talendJobLogProcess(globalMap);
				}

				/**
				 * [tSendMail_1 begin ] stop
				 */

				/**
				 * [tSendMail_1 main ] start
				 */

				currentComponent = "tSendMail_1";

				cLabel = "NotifyDataSteward";

				String smtpHost_tSendMail_1 = "smtp.gmail.com";
				String smtpPort_tSendMail_1 = "465";
				String from_tSendMail_1 = ("eric.tan@sift-ag.com");
				String to_tSendMail_1 = ("tancse@gmail.com").replace(";", ",");
				String cc_tSendMail_1 = (("") == null || "".equals("")) ? null : ("").replace(";", ",");
				String bcc_tSendMail_1 = (("") == null || "".equals("")) ? null : ("").replace(";", ",");
				String subject_tSendMail_1 = ("Talend Open Studio notification");

				java.util.List<java.util.Map<String, String>> headers_tSendMail_1 = new java.util.ArrayList<java.util.Map<String, String>>();
				java.util.List<String> attachments_tSendMail_1 = new java.util.ArrayList<String>();
				java.util.List<String> contentTransferEncoding_tSendMail_1 = new java.util.ArrayList<String>();

				String message_tSendMail_1 = (("Hello Eric, the job Demo 0.1 encountered a data quality issue.\nThere are unassigned tasks in Demo Campaign that requir"
						+ "e your attention.") == null
						|| "".equals(
								"Hello Eric, the job Demo 0.1 encountered a data quality issue.\nThere are unassigned tasks in Demo Campaign that requir"
										+ "e your attention.")) ? "\"\""
												: ("Hello Eric, the job Demo 0.1 encountered a data quality issue.\nThere are unassigned tasks in Demo Campaign that requir"
														+ "e your attention.");
				java.util.Properties props_tSendMail_1 = System.getProperties();
				props_tSendMail_1.put("mail.smtp.host", smtpHost_tSendMail_1);
				props_tSendMail_1.put("mail.smtp.port", smtpPort_tSendMail_1);

				props_tSendMail_1.put("mail.mime.encodefilename", "true");
				props_tSendMail_1.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props_tSendMail_1.put("mail.smtp.socketFactory.fallback", "false");
				props_tSendMail_1.put("mail.smtp.socketFactory.port", smtpPort_tSendMail_1);
				try {

					log.info("tSendMail_1 - Connection attempt to '" + smtpHost_tSendMail_1 + "'.");

					props_tSendMail_1.put("mail.smtp.auth", "true");
					javax.mail.Session session_tSendMail_1 = javax.mail.Session.getInstance(props_tSendMail_1,
							new javax.mail.Authenticator() {
								protected javax.mail.PasswordAuthentication getPasswordAuthentication() {

									final String decryptedPassword_tSendMail_1 = routines.system.PasswordEncryptUtil
											.decryptPassword(
													"enc:routine.encryption.key.v1:bygxABFjZbhWOVJKtLixfFKHGzUP//ya3I3U+k8cHrFhp57rHA==");

									return new javax.mail.PasswordAuthentication("eric.tan@sift-ag.com",
											decryptedPassword_tSendMail_1);
								}
							});

					log.info("tSendMail_1 - Connection to '" + smtpHost_tSendMail_1 + "' has succeeded.");

					javax.mail.Message msg_tSendMail_1 = new javax.mail.internet.MimeMessage(session_tSendMail_1);
					msg_tSendMail_1.setFrom(new javax.mail.internet.InternetAddress(from_tSendMail_1, null));
					msg_tSendMail_1.setRecipients(javax.mail.Message.RecipientType.TO,
							javax.mail.internet.InternetAddress.parse(to_tSendMail_1, false));
					if (cc_tSendMail_1 != null)
						msg_tSendMail_1.setRecipients(javax.mail.Message.RecipientType.CC,
								javax.mail.internet.InternetAddress.parse(cc_tSendMail_1, false));
					if (bcc_tSendMail_1 != null)
						msg_tSendMail_1.setRecipients(javax.mail.Message.RecipientType.BCC,
								javax.mail.internet.InternetAddress.parse(bcc_tSendMail_1, false));
					msg_tSendMail_1.setSubject(subject_tSendMail_1);

					for (int i_tSendMail_1 = 0; i_tSendMail_1 < headers_tSendMail_1.size(); i_tSendMail_1++) {
						java.util.Map<String, String> header_tSendMail_1 = headers_tSendMail_1.get(i_tSendMail_1);
						msg_tSendMail_1.setHeader(header_tSendMail_1.get("KEY"), header_tSendMail_1.get("VALUE"));
					}
					msg_tSendMail_1.setSentDate(new Date());
					msg_tSendMail_1.setHeader("X-Priority", "3"); // High->1 Normal->3 Low->5
					javax.mail.Multipart mp_tSendMail_1 = new javax.mail.internet.MimeMultipart();
					javax.mail.internet.MimeBodyPart mbpText_tSendMail_1 = new javax.mail.internet.MimeBodyPart();
					mbpText_tSendMail_1.setText(message_tSendMail_1, "ISO-8859-15", "plain");
					mp_tSendMail_1.addBodyPart(mbpText_tSendMail_1);

					javax.mail.internet.MimeBodyPart mbpFile_tSendMail_1 = null;

					for (int i_tSendMail_1 = 0; i_tSendMail_1 < attachments_tSendMail_1.size(); i_tSendMail_1++) {
						String filename_tSendMail_1 = attachments_tSendMail_1.get(i_tSendMail_1);
						javax.activation.FileDataSource fds_tSendMail_1 = null;
						java.io.File file_tSendMail_1 = new java.io.File(filename_tSendMail_1);

						if (!file_tSendMail_1.exists()) {
							continue;
						}

						if (file_tSendMail_1.isDirectory()) {
							java.io.File[] subFiles_tSendMail_1 = file_tSendMail_1.listFiles();
							for (java.io.File subFile_tSendMail_1 : subFiles_tSendMail_1) {
								if (subFile_tSendMail_1.isFile()) {
									fds_tSendMail_1 = new javax.activation.FileDataSource(
											subFile_tSendMail_1.getAbsolutePath());
									mbpFile_tSendMail_1 = new javax.mail.internet.MimeBodyPart();
									mbpFile_tSendMail_1
											.setDataHandler(new javax.activation.DataHandler(fds_tSendMail_1));
									mbpFile_tSendMail_1.setFileName(
											javax.mail.internet.MimeUtility.encodeText(fds_tSendMail_1.getName()));
									if (contentTransferEncoding_tSendMail_1.get(i_tSendMail_1)
											.equalsIgnoreCase("base64")) {
										mbpFile_tSendMail_1.setHeader("Content-Transfer-Encoding", "base64");
									}
									mp_tSendMail_1.addBodyPart(mbpFile_tSendMail_1);
								}
							}
						} else {
							mbpFile_tSendMail_1 = new javax.mail.internet.MimeBodyPart();
							fds_tSendMail_1 = new javax.activation.FileDataSource(filename_tSendMail_1);
							mbpFile_tSendMail_1.setDataHandler(new javax.activation.DataHandler(fds_tSendMail_1));
							mbpFile_tSendMail_1
									.setFileName(javax.mail.internet.MimeUtility.encodeText(fds_tSendMail_1.getName()));
							if (contentTransferEncoding_tSendMail_1.get(i_tSendMail_1).equalsIgnoreCase("base64")) {
								mbpFile_tSendMail_1.setHeader("Content-Transfer-Encoding", "base64");
							}
							mp_tSendMail_1.addBodyPart(mbpFile_tSendMail_1);
						}
					}
					// -- set the content --
					msg_tSendMail_1.setContent(mp_tSendMail_1);
					// add handlers for main MIME types
					javax.activation.MailcapCommandMap mc_tSendMail_1 = (javax.activation.MailcapCommandMap) javax.activation.CommandMap
							.getDefaultCommandMap();
					mc_tSendMail_1.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
					mc_tSendMail_1.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
					mc_tSendMail_1.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
					mc_tSendMail_1
							.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
					mc_tSendMail_1
							.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
					javax.activation.CommandMap.setDefaultCommandMap(mc_tSendMail_1);
					// add com.sun.mail.handlers to job imports / depenencies (TESB-27110)
					com.sun.mail.handlers.text_plain text_plain_h_tSendMail_1 = null;
					// -- Send the message --
					javax.mail.Transport.send(msg_tSendMail_1);
				} catch (java.lang.Exception e) {
					globalMap.put("tSendMail_1_ERROR_MESSAGE", e.getMessage());

					log.error("tSendMail_1 - " + e.toString());

					System.err.println(e.toString());

				} finally {
					props_tSendMail_1.remove("mail.smtp.host");
					props_tSendMail_1.remove("mail.smtp.port");

					props_tSendMail_1.remove("mail.mime.encodefilename");

					props_tSendMail_1.remove("mail.smtp.socketFactory.class");
					props_tSendMail_1.remove("mail.smtp.socketFactory.fallback");
					props_tSendMail_1.remove("mail.smtp.socketFactory.port");

					props_tSendMail_1.remove("mail.smtp.auth");
				}

				tos_count_tSendMail_1++;

				/**
				 * [tSendMail_1 main ] stop
				 */

				/**
				 * [tSendMail_1 process_data_begin ] start
				 */

				currentComponent = "tSendMail_1";

				cLabel = "NotifyDataSteward";

				/**
				 * [tSendMail_1 process_data_begin ] stop
				 */

				/**
				 * [tSendMail_1 process_data_end ] start
				 */

				currentComponent = "tSendMail_1";

				cLabel = "NotifyDataSteward";

				/**
				 * [tSendMail_1 process_data_end ] stop
				 */

				/**
				 * [tSendMail_1 end ] start
				 */

				currentComponent = "tSendMail_1";

				cLabel = "NotifyDataSteward";

				if (log.isDebugEnabled())
					log.debug("tSendMail_1 - " + ("Done."));

				ok_Hash.put("tSendMail_1", true);
				end_Hash.put("tSendMail_1", System.currentTimeMillis());

				/**
				 * [tSendMail_1 end ] stop
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
				 * [tSendMail_1 finally ] start
				 */

				currentComponent = "tSendMail_1";

				cLabel = "NotifyDataSteward";

				/**
				 * [tSendMail_1 finally ] stop
				 */
			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tSendMail_1_SUBPROCESS_STATE", 1);
	}

	public static class row2Struct implements routines.system.IPersistableComparableLookupRow<row2Struct> {
		final static byte[] commonByteArrayLock_DEMOCLOUD_Demo = new byte[0];
		static byte[] commonByteArray_DEMOCLOUD_Demo = new byte[0];
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

			synchronized (commonByteArrayLock_DEMOCLOUD_Demo) {

				try {

					int length = 0;

					this.Id = readInteger(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readKeysData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_DEMOCLOUD_Demo) {

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
		org.slf4j.MDC.put("_subJobPid", "zwOBjK_" + subJobPidCounter.getAndIncrement());

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

	public void talendJobLogProcess(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("talendJobLog_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdcInfo.forEach(org.slf4j.MDC::put);
		org.slf4j.MDC.put("_subJobName", "talendJobLog");
		org.slf4j.MDC.put("_subJobPid", "7pNoDO_" + subJobPidCounter.getAndIncrement());

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
		final Demo DemoClass = new Demo();

		int exitCode = DemoClass.runJobInTOS(args);
		if (exitCode == 0) {
			log.info("TalendJob: 'Demo' - Done.");
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
		log.info("TalendJob: 'Demo' - Start.");

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
		org.slf4j.MDC.put("_jobRepositoryId", "_d2VcgIK_Ee6PMYwZwGxOMQ");
		org.slf4j.MDC.put("_compiledAtTimestamp", "2024-02-01T03:22:54.391714700Z");

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
			java.io.InputStream inContext = Demo.class.getClassLoader()
					.getResourceAsStream("democloud/demo_0_1/contexts/" + contextStr + ".properties");
			if (inContext == null) {
				inContext = Demo.class.getClassLoader()
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
		log.info("TalendJob: 'Demo' - Started.");
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
			tDBInput_1Process(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (TalendException e_tDBInput_1) {
			globalMap.put("tDBInput_1_SUBPROCESS_STATE", -1);

			e_tDBInput_1.printStackTrace();

		}

		this.globalResumeTicket = true;// to run tPostJob

		end = System.currentTimeMillis();

		if (watch) {
			System.out.println((end - startTime) + " milliseconds");
		}

		endUsedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		if (false) {
			System.out.println((endUsedMemory - startUsedMemory) + " bytes memory increase when running : Demo");
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
		log.info("TalendJob: 'Demo' - Finished - status: " + status + " returnCode: " + returnCode);

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
 * 322259 characters generated by Talend Cloud Data Fabric on the 1 February
 * 2024 at 11:22:54 AM SGT
 ************************************************************************************************/