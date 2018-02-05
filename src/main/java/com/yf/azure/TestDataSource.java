package com.yf.azure;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hof.mi.thirdparty.interfaces.AbstractDataSet;
import com.hof.mi.thirdparty.interfaces.AbstractDataSource;
import com.hof.mi.thirdparty.interfaces.ColumnMetaData;
import com.hof.mi.thirdparty.interfaces.DataType;
import com.hof.mi.thirdparty.interfaces.FilterData;
import com.hof.mi.thirdparty.interfaces.FilterMetaData;
import com.hof.mi.thirdparty.interfaces.ScheduleDefinition;
import com.hof.mi.thirdparty.interfaces.ScheduleDefinition.FrequencyTypeCode;
import com.hof.mi.thirdparty.interfaces.ThirdPartyException;
import com.hof.pool.JDBCMetaData;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.Predicate;
import com.yf.utils.AzureAuth;
import com.yf.utils.Databases;
import com.yf.utils.NewBilling;
import com.yf.utils.Refresher;
import com.yf.utils.Resources;
import com.yf.utils.VirtualMachine;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestDataSource extends AbstractDataSource {

	static Logger LOGGER = Logger.getLogger(AbstractDataSource.class.getName());

	// static String ref =
	// "AQABAAAAAABHh4kmS_aKT5XrjzxRAtHzSSkfsTHPh0x2caD5R3YHGBEd6CVwoO_sN9uHqwau4rR8Y42xAX4yIV_LjSPrnyfR0An5ABGlH99TmJNuvX65UE3j_s5zq6M-zmJqW00fXvH1KBlEqoE-2COvfWmgejXZtSAd5IMhmcgh5fZUBMgGoqCwyQXAHjKyhbTKZRB2zmDVUgyltKndTq_Oc_1vEInm9KB-zPGM4INV755X7O0dl26z7hu51EQ5dOXrWwGtnjAGiKH6vO4TDmX0uE0RAYUAxAnhqExXywZl5dnkN-SfrK2iQqF0W2Bj0bu2AKKXB7O3pxutf3OK9zJnX7jF3n1G5ajsiaQ1rtdAJeQPTEhTv2fBTmEbOOCpO9hQfyD9kdInTK1WgYVSdaEmkGhBrOkI2_kJWnySyCG0rI3rg8FcLqknNN_uqMAoEJmwbhryWNHFQETCTP7azgIxlthVWnl6OGK0qBWSgbdfMSaSsgxOUB8-2UTN6Rkmjpf7aZXbmgKpMFEN2vvBaaqLB-d_nKl6OfTjIjWd39KyrlLewNV3LE2qhuANZ24s5IeeOafFJ7HKSHvykLhp7L138gyRXrm7fsBkM7Dgt-kGYujjVIKGTl_NRQp4oHt9b0DKZhMTR7rgheC9pTHrDG1RL-y4sHIXA99p3HGT8JJC6YV4Y0zVXmKhr5vDdzpXKt6deelOnjtvtK3piij4NU6uOZlh0AxyPO7A218es4AbGeiqlQckBlKFB328vk9vfhQP-I4ChdmQtai29xByuPgIUJ3T2mqdO2JJy0Rit8Fxp8Lqq997FSAA";
	public String getDataSourceName() {
		return "Azure";
	}

	public ScheduleDefinition getScheduleDefinition() {
		return new ScheduleDefinition(ScheduleDefinition.FrequencyTypeCode.MINUTES, null, Integer.valueOf(5));
	}

	public ArrayList<AbstractDataSet> getDataSets() {
		ArrayList<AbstractDataSet> p = new ArrayList();

		p.add(inventory());
		p.add(virtualMachine());
		p.add(database());
		p.add(virtualMachineLive());
		p.add(Billing());
		p.add(databaseLive());
		p.add(RBilling());

		return p;
	}

	private AbstractDataSet virtualMachineLive() {
		AbstractDataSet simpleDataSet = new AbstractDataSet() {
			public ArrayList<FilterMetaData> getFilters() {
				ArrayList<FilterMetaData> fm = new ArrayList();
				return fm;
			}

			public String getDataSetName() {
				return "VirtualMachine Live";
			}

			public ArrayList<ColumnMetaData> getColumns() {
				ArrayList<ColumnMetaData> cm = new ArrayList();
				LOGGER.log(Level.INFO, "get Columns begins");
				// String zone = new
				// String(TestDataSource.this.loadBlob("zone"));
				cm.add(new ColumnMetaData("VmID", DataType.TEXT));
				cm.add(new ColumnMetaData("Resource Type", DataType.TEXT));
				cm.add(new ColumnMetaData("OS Type", DataType.TEXT));
				cm.add(new ColumnMetaData("VM Size", DataType.TEXT));
				cm.add(new ColumnMetaData("Location", DataType.TEXT));
				cm.add(new ColumnMetaData("Status", DataType.TEXT));
				cm.add(new ColumnMetaData("Name", DataType.TEXT));
				cm.add(new ColumnMetaData("Timestamp(UTC)", DataType.TIMESTAMP));
				// cm.add(new ColumnMetaData("Timestamp(" + zone + ")",
				// DataType.TIMESTAMP));
				cm.add(new ColumnMetaData("Percentage CPU", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Network In", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Network Out", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Read Bytes", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Write Bytes", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Read Operations", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Write Operations", DataType.NUMERIC));
				LOGGER.log(Level.INFO, "get Columns ends");
				return cm;
			}

			public boolean getAllowsDuplicateColumns() {
				return false;
			}

			public boolean getAllowsAggregateColumns() {
				return false;
			}

			public Object[][] execute(List<ColumnMetaData> columns, List<FilterData> filters) {
				if (TestDataSource.this.loadBlob("LASTRUN") == null) {
					throw new ThirdPartyException("Database is not yet populated");
				}
				String nodeData = new String(TestDataSource.this.loadBlob("VMACHINEL"));
				JsonElement je = new JsonParser().parse(nodeData);
				JsonArray ja = je.getAsJsonArray();
				

				Configuration conf = Configuration.defaultConfiguration()
						.addOptions(new Option[] { Option.DEFAULT_PATH_LEAF_TO_NULL })
						.addOptions(new Option[] { Option.SUPPRESS_EXCEPTIONS });
				DocumentContext tt = JsonPath.using(conf).parse(nodeData);

				Object[][] data = (Object[][]) null;
				data = new Object[ja.size()][columns.size()];

				Object val = null;
				for (int i = 0; i < ja.size(); i++) {
					for (int j = 0; j < columns.size(); j++) {
						if (((ColumnMetaData) columns.get(j)).getColumnName().equals("VmID")) {
							val = tt.read("$.[" + i + "].['VmID']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Resource Type")) {
							val = tt.read("$.[" + i + "].['Resource Type']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("OS Type")) {
							val = tt.read("$.[" + i + "].['OS type']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("VM Size")) {
							val = tt.read("$.[" + i + "].['VMSize']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Location")) {
							val = tt.read("$.[" + i + "].['Location']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Status")) {
							val = tt.read("$.[" + i + "].['Status']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Name")) {
							val = tt.read("$.[" + i + "].['Name']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Timestamp(UTC)")) {
							val = tt.read("$.[" + i + "].['Timestamp']");
							String timestamp = val.toString();
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
							try {
								Date parsedDate = format.parse(timestamp);
								Timestamp timeStampDate = new Timestamp(parsedDate.getTime());
								data[i][j] = timeStampDate;
							} catch (ParseException e) {
								e.printStackTrace();
							}
						} /*
							 * else if (((ColumnMetaData)
							 * columns.get(j)).getColumnName()
							 * .equals("Timestamp(" + zone + ")")) { val =
							 * tt.read("$.[" + i + "].['Timestamp']"); String
							 * timestamp = val.toString(); DateFormat utcFormat
							 * = new
							 * SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
							 * utcFormat.setTimeZone(TimeZone.getTimeZone("UTC")
							 * );
							 * 
							 * Date date = null; try { date =
							 * utcFormat.parse(timestamp); } catch
							 * (ParseException e) { e.printStackTrace(); }
							 * DateFormat pstFormat = new
							 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							 * pstFormat.setTimeZone(TimeZone.getTimeZone(zone))
							 * ; Timestamp ts =
							 * Timestamp.valueOf(pstFormat.format(date));
							 * data[i][j] = ts; }
							 */ else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Percentage CPU")) {
							val = tt.read("$.[" + i + "].['Percentage CPU']");
							data[i][j] = new BigDecimal(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Network In")) {
							val = tt.read("$.[" + i + "].['NetworkIN']");
							data[i][j] = new BigDecimal(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Network Out")) {
							val = tt.read("$.[" + i + "].['NetworkOut']");
							data[i][j] = new BigDecimal(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Disk Read Bytes")) {
							val = tt.read("$.[" + i + "].['Disk Read Bytes']");
							data[i][j] = new BigDecimal(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Disk Write Bytes")) {
							val = tt.read("$.[" + i + "].['Disk Write Bytes']");
							data[i][j] = new BigDecimal(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Disk Read Operations")) {
							val = tt.read("$.[" + i + "].['Disk Read Operation']");
							data[i][j] = new BigDecimal(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Disk Write Operations")) {
							val = tt.read("$.[" + i + "].['Disk Write Operation']");
							data[i][j] = new BigDecimal(val.toString());
						}
					}
				}
				return data;
			}
		};
		return simpleDataSet;
	}

	private AbstractDataSet inventory() {
		AbstractDataSet simpleDataSet = new AbstractDataSet() {
			public ArrayList<FilterMetaData> getFilters() {
				ArrayList<FilterMetaData> fm = new ArrayList();
				return fm;
			}

			public String getDataSetName() {
				return "Inventory";
			}

			public ArrayList<ColumnMetaData> getColumns() {
				ArrayList<ColumnMetaData> cm = new ArrayList();

				// cm.add(new ColumnMetaData("Subscription ID", DataType.TEXT));
				cm.add(new ColumnMetaData("Name", DataType.TEXT));
				cm.add(new ColumnMetaData("Type", DataType.TEXT));
				cm.add(new ColumnMetaData("Location", DataType.TEXT));
				cm.add(new ColumnMetaData("Resource Group String", DataType.TEXT));

				return cm;
			}

			public boolean getAllowsDuplicateColumns() {
				return false;
			}

			public boolean getAllowsAggregateColumns() {
				return false;
			}

			public Object[][] execute(List<ColumnMetaData> columns, List<FilterData> filters) {
				if (TestDataSource.this.loadBlob("LASTRUN") == null) {
					throw new ThirdPartyException("Database is not yet populated");
				}
				String token = new String(TestDataSource.this.loadBlob("accessToken"));
				new Resources();
				String Inventory = Resources.getResources(token);
				JsonElement je = new JsonParser().parse(Inventory);
				JsonObject jo = je.getAsJsonObject();
				TestDataSource.this.saveBlob("INVENTORY", Inventory.getBytes());
				String nodeData = new String(TestDataSource.this.loadBlob("INVENTORY"));

				Configuration conf = Configuration.defaultConfiguration()
						.addOptions(new Option[] { Option.DEFAULT_PATH_LEAF_TO_NULL })
						.addOptions(new Option[] { Option.SUPPRESS_EXCEPTIONS });
				DocumentContext tt = JsonPath.using(conf).parse(nodeData);

				Object[][] data = (Object[][]) null;
				int y = 0, x = 0;
				for (int k = 1; k <= jo.size(); k++) {
					JsonArray ja = jo.getAsJsonArray("" + k + "");
					y = ja.size();
					x = y + x;
				}
				data = new Object[x][columns.size()];

				Object val = null;
				int p = 0, i;
				for (int k = 1; k <= jo.size(); k++) {
					JsonArray ja = jo.getAsJsonArray("" + k + "");
					for (i = 0; i < ja.size(); i++) {
						for (int j = 0; j < columns.size(); j++) {
							/*
							 * if (((ColumnMetaData)
							 * columns.get(j)).getColumnName().
							 * equals("Subscription ID")) { val =
							 * tt.read("$.['value'].[" + i + "].['id']");
							 * data[i][j] = val.toString(); }
							 */if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Name")) {
								val = tt.read("$.['" + k + "'].[" + i + "].['name']");
								data[p][j] = val.toString();
							} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Type")) {
								val = tt.read("$.['" + k + "'].[" + i + "].['type']");
								String str = val.toString().substring(val.toString().lastIndexOf("/") + 1);
								String output = str.substring(0, 1).toUpperCase() + str.substring(1);
								data[p][j] = output;
							} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Location")) {
								val = tt.read("$.['" + k + "'].[" + i + "].['location']");
								data[p][j] = val.toString();
							} else if (((ColumnMetaData) columns.get(j)).getColumnName()
									.equals("Resource Group String")) {
								val = tt.read("$.['" + k + "'].[" + i + "].['id']");
								data[p][j] = val.toString();
								String str = val.toString();
								Pattern pat = Pattern.compile("resourceGroups/(.*?)/providers");
								Matcher m = pat.matcher(str);
								while (m.find()) {
									data[p][j] = m.group(1);
								}
							}
						}
						p++;
					}
				}
				return data;
			}
		};
		return simpleDataSet;
	}

	private AbstractDataSet virtualMachine() {
		AbstractDataSet simpleDataSet = new AbstractDataSet() {
			public ArrayList<FilterMetaData> getFilters() {
				ArrayList<FilterMetaData> fm = new ArrayList();
				boolean usefortransform = false;
				
				try {

					usefortransform = isTransformationContext();

				} catch (NoSuchMethodError e) {


				}
				if (usefortransform)
				{
                          
				}
				return fm;
			}

			public String getDataSetName() {
				return "virtualMachine";
			}

			public ArrayList<ColumnMetaData> getColumns() {
				ArrayList<ColumnMetaData> cm = new ArrayList();
				// String zone = new
				// String(TestDataSource.this.loadBlob("zone"));
				cm.add(new ColumnMetaData("VmID", DataType.TEXT));
				cm.add(new ColumnMetaData("Resource Type", DataType.TEXT));
				cm.add(new ColumnMetaData("OS Type", DataType.TEXT));
				cm.add(new ColumnMetaData("VM Size", DataType.TEXT));
				cm.add(new ColumnMetaData("Location", DataType.TEXT));
				cm.add(new ColumnMetaData("Name", DataType.TEXT));
				cm.add(new ColumnMetaData("Timestamp(UTC)", DataType.TIMESTAMP));
				// cm.add(new ColumnMetaData("Timestamp(" + zone + ")",
				// DataType.TIMESTAMP));
				cm.add(new ColumnMetaData("Percentage CPU", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Network In", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Network Out", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Read Bytes", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Write Bytes", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Read Operations", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Write Operations", DataType.NUMERIC));

				return cm;
			}

			public boolean getAllowsAggregateColumns() {
				return false;
			}

			public boolean getAllowsDuplicateColumns() {
				return false;
			}

			public Object[][] execute(List<ColumnMetaData> columns, List<FilterData> filters) {
				if (TestDataSource.this.loadBlob("LASTRUN") == null) {
					throw new ThirdPartyException("Database is not yet populated");
				}
				String nodeData = new String(TestDataSource.this.loadBlob("VMACHINE"));
				JsonElement je = new JsonParser().parse(nodeData);
				JsonArray ja = je.getAsJsonArray();

				Configuration conf = Configuration.defaultConfiguration()
						.addOptions(new Option[] { Option.DEFAULT_PATH_LEAF_TO_NULL })
						.addOptions(new Option[] { Option.SUPPRESS_EXCEPTIONS });
				DocumentContext tt = JsonPath.using(conf).parse(nodeData);

				Object[][] data = (Object[][]) null;
				data = new Object[ja.size()][columns.size()];

				Object val = null;
				for (int i = 0; i < ja.size(); i++) {
					for (int j = 0; j < columns.size(); j++) {
						if (((ColumnMetaData) columns.get(j)).getColumnName().equals("VmID")) {
							val = tt.read("$.[" + i + "].['VmID']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Resource Type")) {
							val = tt.read("$.[" + i + "].['Resource Type']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("OS Type")) {
							val = tt.read("$.[" + i + "].['OS type']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("VM Size")) {
							val = tt.read("$.[" + i + "].['VMSize']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Location")) {
							val = tt.read("$.[" + i + "].['Location']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Name")) {
							val = tt.read("$.[" + i + "].['Name']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Timestamp(UTC)")) {
							val = tt.read("$.[" + i + "].['Timestamp']");
							String timestamp = val.toString();
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
							try {
								Date parsedDate = format.parse(timestamp);
								Timestamp timeStampDate = new Timestamp(parsedDate.getTime());
								data[i][j] = timeStampDate;
							} catch (ParseException e) {
								e.printStackTrace();
							}
						} /*
							 * else if (((ColumnMetaData)
							 * columns.get(j)).getColumnName()
							 * .equals("Timestamp(" + zone + ")")) { val =
							 * tt.read("$.[" + i + "].['Timestamp']"); String
							 * timestamp = val.toString(); DateFormat utcFormat
							 * = new
							 * SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
							 * utcFormat.setTimeZone(TimeZone.getTimeZone("UTC")
							 * );
							 * 
							 * Date date = null; try { date =
							 * utcFormat.parse(timestamp); } catch
							 * (ParseException e) { e.printStackTrace(); }
							 * DateFormat pstFormat = new
							 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							 * pstFormat.setTimeZone(TimeZone.getTimeZone(zone))
							 * ; Timestamp ts =
							 * Timestamp.valueOf(pstFormat.format(date));
							 * data[i][j] = ts; }
							 */ else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Percentage CPU")) {
							val = tt.read("$.[" + i + "].['Percentage CPU']");
							data[i][j] = new BigDecimal(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Network In")) {
							val = tt.read("$.[" + i + "].['NetworkIN']");
							data[i][j] = new BigDecimal(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Network Out")) {
							val = tt.read("$.[" + i + "].['NetworkOut']");
							data[i][j] = new BigDecimal(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Disk Read Bytes")) {
							val = tt.read("$.[" + i + "].['Disk Read Bytes']");
							data[i][j] = new BigDecimal(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Disk Write Bytes")) {
							val = tt.read("$.[" + i + "].['Disk Write Bytes']");
							data[i][j] = new BigDecimal(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Disk Read Operations")) {
							val = tt.read("$.[" + i + "].['Disk Read Operation']");
							data[i][j] = new BigDecimal(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Disk Write Operations")) {
							val = tt.read("$.[" + i + "].['Disk Write Operation']");
							data[i][j] = new BigDecimal(val.toString());
						}
					}
				}
				return data;
			}
		};
		return simpleDataSet;
	}


	private AbstractDataSet database() {
		AbstractDataSet simpleDataSet = new AbstractDataSet() {
			public ArrayList<FilterMetaData> getFilters() {
				ArrayList<FilterMetaData> fm = new ArrayList();
				return fm;
			}

			public String getDataSetName() {
				return "databases";
			}

			public ArrayList<ColumnMetaData> getColumns() {
				ArrayList<ColumnMetaData> cm = new ArrayList();
				// String zone = new
				// String(TestDataSource.this.loadBlob("zone"));
				// cm.add(new ColumnMetaData("Resource ID", DataType.TEXT));
				cm.add(new ColumnMetaData("Resource Group", DataType.TEXT));
				cm.add(new ColumnMetaData("Database Name", DataType.TEXT));
				cm.add(new ColumnMetaData("Timestamp(UTC)", DataType.TIMESTAMP));
				// cm.add(new ColumnMetaData("Timestamp(" + zone + ")",
				// DataType.TIMESTAMP));
				cm.add(new ColumnMetaData("Percentage DTU", DataType.NUMERIC));
				cm.add(new ColumnMetaData("CPU percentage", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Log IO percentage", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Data IO percentage", DataType.NUMERIC));

				return cm;
			}

			public boolean getAllowsAggregateColumns() {
				return false;
			}

			public boolean getAllowsDuplicateColumns() {
				return false;
			}

			public Object[][] execute(List<ColumnMetaData> columns, List<FilterData> filters) {
				if (TestDataSource.this.loadBlob("LASTRUN") == null) {
					throw new ThirdPartyException("Database is not yet populated");
				}
				String nodeData = new String(TestDataSource.this.loadBlob("DATA"));
				JsonElement je = new JsonParser().parse(nodeData);
				JsonArray ja = je.getAsJsonArray();

				Configuration conf = Configuration.defaultConfiguration()
						.addOptions(new Option[] { Option.DEFAULT_PATH_LEAF_TO_NULL })
						.addOptions(new Option[] { Option.SUPPRESS_EXCEPTIONS });
				DocumentContext tt = JsonPath.using(conf).parse(nodeData);

				Object[][] data = (Object[][]) null;
				data = new Object[ja.size()][columns.size()];

				Object val = null;
				for (int i = 0; i < ja.size(); i++) {
					for (int j = 0; j < columns.size(); j++) {
						/*
						 * if (((ColumnMetaData)
						 * columns.get(j)).getColumnName().equals("Resource ID"
						 * )) { val = tt.read("$.[" + i + "].['Resource ID']");
						 * data[i][j] = val.toString(); }
						 */
						if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Resource Group")) {
							val = tt.read("$.[" + i + "].['Resource Group']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Percentage DTU")) {
							val = tt.read("$.[" + i + "].['DTU Percentage']");
							data[i][j] = new Float(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Database Name")) {
							val = tt.read("$.[" + i + "].['Resource ID']");
							String str = val.toString();
							data[i][j] = str.substring(str.lastIndexOf("/") + 1);
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("CPU percentage")) {
							val = tt.read("$.[" + i + "].['CPU percentage']");
							data[i][j] = new Float(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Log IO percentage")) {
							val = tt.read("$.[" + i + "].['Log IO percentage']");
							data[i][j] = new Float(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Data IO percentage")) {
							val = tt.read("$.[" + i + "].['Data IO percentage']");
							data[i][j] = new Float(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Timestamp(UTC)")) {
							val = tt.read("$.[" + i + "].['Timestamp']");
							String timestamp = val.toString();
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
							try {
								Date parsedDate = format.parse(timestamp);
								Timestamp timeStampDate = new Timestamp(parsedDate.getTime());
								data[i][j] = timeStampDate;
							} catch (ParseException e) {
								e.printStackTrace();
							}
						} /*
							 * else if (((ColumnMetaData)
							 * columns.get(j)).getColumnName()
							 * .equals("Timestamp(" + zone + ")")) { val =
							 * tt.read("$.[" + i + "].['Timestamp']"); String
							 * timestamp = val.toString(); DateFormat utcFormat
							 * = new
							 * SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
							 * utcFormat.setTimeZone(TimeZone.getTimeZone("UTC")
							 * ); Date date = null; try { date =
							 * utcFormat.parse(timestamp); } catch
							 * (ParseException e) { e.printStackTrace(); }
							 * DateFormat pstFormat = new
							 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							 * pstFormat.setTimeZone(TimeZone.getTimeZone(zone))
							 * ; Timestamp ts =
							 * Timestamp.valueOf(pstFormat.format(date));
							 * data[i][j] = ts; }
							 */
					}
				}
				return data;
			}
		};
		return simpleDataSet;
	}

	private AbstractDataSet databaseLive() {
		AbstractDataSet simpleDataSet = new AbstractDataSet() {
			public ArrayList<FilterMetaData> getFilters() {
				ArrayList<FilterMetaData> fm = new ArrayList();
				return fm;
			}

			public String getDataSetName() {
				return "Databases Live";
			}

			public ArrayList<ColumnMetaData> getColumns() {
				ArrayList<ColumnMetaData> cm = new ArrayList();
				// String zone = new
				// String(TestDataSource.this.loadBlob("zone"));
				// cm.add(new ColumnMetaData("Resource ID", DataType.TEXT));
				cm.add(new ColumnMetaData("Resource Group", DataType.TEXT));
				cm.add(new ColumnMetaData("Database Name", DataType.TEXT));
				cm.add(new ColumnMetaData("Status", DataType.TEXT));
				cm.add(new ColumnMetaData("Timestamp(UTC)", DataType.TIMESTAMP));
				// cm.add(new ColumnMetaData("Timestamp(" + zone + ")",
				// DataType.TIMESTAMP));
				cm.add(new ColumnMetaData("Percentage DTU", DataType.NUMERIC));
				cm.add(new ColumnMetaData("CPU percentage", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Log IO percentage", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Data IO percentage", DataType.NUMERIC));

				return cm;
			}

			public boolean getAllowsAggregateColumns() {
				return false;
			}

			public boolean getAllowsDuplicateColumns() {
				return false;
			}

			public Object[][] execute(List<ColumnMetaData> columns, List<FilterData> filters) {
				if (TestDataSource.this.loadBlob("LASTRUN") == null) {
					throw new ThirdPartyException("Database is not yet populated");
				}
				String nodeData = new String(TestDataSource.this.loadBlob("DATAL"));
				JsonElement je = new JsonParser().parse(nodeData);
				JsonArray ja = je.getAsJsonArray();
				Configuration conf = Configuration.defaultConfiguration()
						.addOptions(new Option[] { Option.DEFAULT_PATH_LEAF_TO_NULL })
						.addOptions(new Option[] { Option.SUPPRESS_EXCEPTIONS });
				DocumentContext tt = JsonPath.using(conf).parse(nodeData);

				Object[][] data = (Object[][]) null;
				data = new Object[ja.size()][columns.size()];

				Object val = null;
				for (int i = 0; i < ja.size(); i++) {
					for (int j = 0; j < columns.size(); j++) {
						/*
						 * if (((ColumnMetaData)
						 * columns.get(j)).getColumnName().equals("Resource ID"
						 * )) { val = tt.read("$.[" + i + "].['Resource ID']");
						 * data[i][j] = val.toString(); }
						 */
						if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Resource Group")) {
							val = tt.read("$.[" + i + "].['Resource Group']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Percentage DTU")) {
							val = tt.read("$.[" + i + "].['DTU Percentage']");
							data[i][j] = new Float(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Database Name")) {
							val = tt.read("$.[" + i + "].['Resource ID']");
							String str = val.toString();
							data[i][j] = str.substring(str.lastIndexOf("/") + 1);
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Status")) {
							val = tt.read("$.[" + i + "].['Status']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("CPU percentage")) {
							val = tt.read("$.[" + i + "].['CPU percentage']");
							data[i][j] = new Float(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Log IO percentage")) {
							val = tt.read("$.[" + i + "].['Log IO percentage']");
							data[i][j] = new Float(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Data IO percentage")) {
							val = tt.read("$.[" + i + "].['Data IO percentage']");
							data[i][j] = new Float(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Timestamp(UTC)")) {
							val = tt.read("$.[" + i + "].['Timestamp']");
							String timestamp = val.toString();
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
							try {
								Date parsedDate = format.parse(timestamp);
								Timestamp timeStampDate = new Timestamp(parsedDate.getTime());
								data[i][j] = timeStampDate;
							} catch (ParseException e) {
								e.printStackTrace();
							}
						} /*
							 * else if (((ColumnMetaData)
							 * columns.get(j)).getColumnName()
							 * .equals("Timestamp(" + zone + ")")) { val =
							 * tt.read("$.[" + i + "].['Timestamp']"); String
							 * timestamp = val.toString(); DateFormat utcFormat
							 * = new
							 * SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
							 * utcFormat.setTimeZone(TimeZone.getTimeZone("UTC")
							 * ); Date date = null; try { date =
							 * utcFormat.parse(timestamp); } catch
							 * (ParseException e) { e.printStackTrace(); }
							 * DateFormat pstFormat = new
							 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							 * pstFormat.setTimeZone(TimeZone.getTimeZone(zone))
							 * ; Timestamp ts =
							 * Timestamp.valueOf(pstFormat.format(date));
							 * data[i][j] = ts; }
							 */
					}
				}
				return data;
			}
		};
		return simpleDataSet;
	}

	private AbstractDataSet Billing() {
		AbstractDataSet simpleDataSet = new AbstractDataSet() {
			public ArrayList<FilterMetaData> getFilters() {
				ArrayList<FilterMetaData> fm = new ArrayList();
				return fm;
			}

			public String getDataSetName() {
				return "Billing";
			}

			public ArrayList<ColumnMetaData> getColumns() {
				ArrayList<ColumnMetaData> cm = new ArrayList();
				cm.add(new ColumnMetaData("Reported Start Time", DataType.DATE));
				cm.add(new ColumnMetaData("Reported End Time", DataType.DATE));
				cm.add(new ColumnMetaData("Subscription ID", DataType.TEXT));
				cm.add(new ColumnMetaData("Currency", DataType.TEXT));
				cm.add(new ColumnMetaData("Bill", DataType.NUMERIC));

				return cm;
			}

			public boolean getAllowsDuplicateColumns() {
				return false;
			}

			public boolean getAllowsAggregateColumns() {
				return false;
			}

			public Object[][] execute(List<ColumnMetaData> columns, List<FilterData> filters) {
				if (TestDataSource.this.loadBlob("LASTRUN") == null) {
					throw new ThirdPartyException("Database is not yet populated");
				}

				String nodeData = new String(TestDataSource.this.loadBlob("BILL"));
				JsonElement je = new JsonParser().parse(nodeData);
				JsonArray ja = je.getAsJsonArray();

				Configuration conf = Configuration.defaultConfiguration()
						.addOptions(new Option[] { Option.DEFAULT_PATH_LEAF_TO_NULL })
						.addOptions(new Option[] { Option.SUPPRESS_EXCEPTIONS });
				DocumentContext tt = JsonPath.using(conf).parse(nodeData);

				Object[][] data = (Object[][]) null;
				data = new Object[ja.size()][columns.size()];

				Object val = null;
				for (int i = 0; i < ja.size(); i++) {
					for (int j = 0; j < columns.size(); j++) {
						if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Reported Start Time")) {
							val = tt.read("$.[" + i + "].['ReportedStartedTime']");
							SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
							Date parsed = null;
							try {
								parsed = sdf.parse(val.toString());
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							java.sql.Date date = new java.sql.Date(parsed.getTime());
							data[i][j] = date;
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Reported End Time")) {
							val = tt.read("$.[" + i + "].['ReportedEndTime']");
							SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
							Date parsed = null;
							try {
								parsed = sdf.parse(val.toString());
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							java.sql.Date date = new java.sql.Date(parsed.getTime());
							data[i][j] = date;
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Bill")) {
							val = tt.read("$.[" + i + "].['Bill']");
							data[i][j] = new BigDecimal(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Subscription ID")) {
							val = tt.read("$.[" + i + "].['Subscription Id']");
							String str = val.toString().substring(val.toString().lastIndexOf("/") + 1);
							data[i][j] = str;
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Currency")) {
							val = tt.read("$.[" + i + "].['Currency']");
							data[i][j] = val.toString();
						}
					}
				}
				return data;
			}
		};
		return simpleDataSet;
	}

	private AbstractDataSet RBilling() {
		AbstractDataSet simpleDataSet = new AbstractDataSet() {
			public ArrayList<FilterMetaData> getFilters() {
				ArrayList<FilterMetaData> fm = new ArrayList();
				return fm;
			}

			public String getDataSetName() {
				return "Resource Billing";
			}

			public ArrayList<ColumnMetaData> getColumns() {
				String currency = new String(TestDataSource.this.loadBlob("currency"));
				ArrayList<ColumnMetaData> cm = new ArrayList();
				cm.add(new ColumnMetaData("Subscription Id", DataType.TEXT));
				cm.add(new ColumnMetaData("Resource Name", DataType.TEXT));
				cm.add(new ColumnMetaData("Resource GUID", DataType.TEXT));
				cm.add(new ColumnMetaData("Consumed Units", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Billable Units", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Pre-Tax Cost(" + currency + ")", DataType.NUMERIC));

				return cm;
			}

			public boolean getAllowsDuplicateColumns() {
				return false;
			}

			public boolean getAllowsAggregateColumns() {
				return false;
			}

			public Object[][] execute(List<ColumnMetaData> columns, List<FilterData> filters) {
				if (TestDataSource.this.loadBlob("LASTRUN") == null) {
					throw new ThirdPartyException("Database is not yet populated");
				}
				String currency = new String(TestDataSource.this.loadBlob("currency"));
				String nodeData = new String(TestDataSource.this.loadBlob("RBILL"));
				JsonElement je = new JsonParser().parse(nodeData);
				JsonArray ja = je.getAsJsonArray();

				Configuration conf = Configuration.defaultConfiguration()
						.addOptions(new Option[] { Option.DEFAULT_PATH_LEAF_TO_NULL })
						.addOptions(new Option[] { Option.SUPPRESS_EXCEPTIONS });
				DocumentContext tt = JsonPath.using(conf).parse(nodeData);

				Object[][] data = (Object[][]) null;
				data = new Object[ja.size()][columns.size()];

				Object val = null;
				for (int i = 0; i < ja.size(); i++) {
					for (int j = 0; j < columns.size(); j++) {
						if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Subscription Id")) {
							val = tt.read("$.[" + i + "].['Subscription Id']");
							String str = val.toString().substring(val.toString().lastIndexOf("/") + 1);
							data[i][j] = str;
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Resource Name")) {
							val = tt.read("$.[" + i + "].['Resource Name']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Resource GUID")) {
							val = tt.read("$.[" + i + "].['Id']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Consumed Units")) {
							val = tt.read("$.[" + i + "].['Consumed Units']");
							data[i][j] = new BigDecimal(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Billable Units")) {
							val = tt.read("$.[" + i + "].['Billable Units']");
							data[i][j] = new BigDecimal(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName()
								.equals("Pre-Tax Cost(" + currency + ")")) {
							val = tt.read("$.[" + i + "].['Pre-Tax Cost']");
							data[i][j] = new BigDecimal(val.toString());
						}
					}
				}
				return data;
			}
		};
		return simpleDataSet;
	}

	public JDBCMetaData getDataSourceMetaData() {
		return new TestSourceMetaData();
	}

	public boolean authenticate() {
		return true;
	}

	public void disconnect() {
	}

	public Map<String, Object> testConnection() {
		Map<String, Object> p = new HashMap();
		try {
			String authCode = (String) getAttribute("CODE");
			// String zone = (String) getAttribute("SELECTOR");
			String currency = (String) getAttribute("SELECTOR1");
			String months = (String) getAttribute("MONTHS");
			String Locale = (String) getAttribute("SELECTOR2");
			String Region = (String) getAttribute("SELECTOR3");
			String Offer = (String) getAttribute("SELECTOR5");
			JsonElement je = new JsonParser().parse(AzureAuth.getResponse(authCode));
			try {
				new AzureAuth();
				if (AzureAuth.authCheck(authCode) == 200) {
					JsonObject jo = je.getAsJsonObject();
					String accessToken = jo.get("access_token").getAsString();
					String refreshToken = jo.get("refresh_token").getAsString();
					saveBlob("accessToken", accessToken.getBytes());
					saveBlob("refreshToken", refreshToken.getBytes());
					saveBlob("currency", currency.getBytes());
					// saveBlob("zone", zone.getBytes());
					saveBlob("months", months.getBytes());
					saveBlob("Locale", Locale.getBytes());
					saveBlob("Region", Region.getBytes());
					saveBlob("Offer", Offer.getBytes());
					if (saveBlob("accessToken", accessToken.getBytes()) && saveBlob("months", months.getBytes())) {
						autoRun();
					}
				}
				if (AzureAuth.authCheck(authCode) != 200) {
					String ref = new String(TestDataSource.this.loadBlob("refreshToken"));
					String accessToken = Refresher.accessToken(ref);
					saveBlob("accessToken", accessToken.getBytes());
					saveBlob("refreshToken", ref.getBytes());
					saveBlob("currency", currency.getBytes());
					// saveBlob("zone", zone.getBytes());
					saveBlob("months", months.getBytes());
					saveBlob("Locale", Locale.getBytes());
					saveBlob("Region", Region.getBytes());
					saveBlob("Offer", Offer.getBytes());
				}

			} catch (Exception e) {
				p.put("ERROR", "Invalid Authentication Code");
			}
		} catch (Exception e) {
			p.put("ERROR", "Please Enter the Authentication Code");
		}
		/*
		 * Map<String,Object> p = new HashMap<String, Object>();
		 * 
		 * String authCode=(String)getAttribute("CODE"); JsonElement je = new
		 * JsonParser().parse(new AzureAuth().getResponse(authCode)); JsonObject
		 * jo = je.getAsJsonObject();
		 * 
		 * String accessToken = jo.get("access_token").getAsString(); String
		 * refreshToken = jo.get("refresh_token").getAsString();
		 * 
		 * if(new AzureAuth().authCheck(authCode)==200){
		 * 
		 * //System.out.
		 * println("RESSSSSSSSSSSSSSSSSSS PONSSSSSSSSSSSSSSEEEEEEEEEE");
		 * //System.out.println(accessToken);
		 * //System.out.println(refreshToken); saveBlob("accessToken",
		 * accessToken.getBytes()); saveBlob("refreshToken",
		 * refreshToken.getBytes()); } if(new AzureAuth().authCheck(authCode)
		 * !=200) { try { String refreshTok = new String(loadBlob("URL"));
		 * String accessTok = Refresher.refreshToken(refreshTok);
		 * saveBlob("accessToken", accessTok.getBytes());
		 * saveBlob("refreshToken", refreshToken.getBytes());
		 * 
		 * } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * }
		 * 
		 * 
		 * else{ p.put("ERROR DS", "Invalid Authentication Code"); }
		 */
		return p;
	}

	public boolean autoRun() {

		System.out.println("Auto running Test data source");
		
		boolean useonlyfortransform = Boolean.valueOf((String)getAttribute("USEFORTRANSFORMATIONS"));
		if (!useonlyfortransform){
			
			boolean onlylatest = Boolean.valueOf((String)getAttribute("ONLYLATEST"));
			
		}
		try {
			String bill = new String(TestDataSource.this.loadBlob("BILL"));
		} catch (Exception e) {
			// TODO: handle exception

			String token = new String(loadBlob("accessToken"));
			String months = new String(TestDataSource.this.loadBlob("months"));
			String currency = new String(TestDataSource.this.loadBlob("currency"));
			String locale = new String(TestDataSource.this.loadBlob("Locale"));
			String region = new String(TestDataSource.this.loadBlob("Region"));
			String offer = new String(TestDataSource.this.loadBlob("Offer"));
			String RBill = com.yf.utils.Billing.getRBilling(token, currency, locale, region, offer);
			String Bill = NewBilling.getBilling(token, months);
			String VMachineL = VirtualMachine.getLiveDetails(token);
			String VMachine = VirtualMachine.getDetails(token);
			String database = Databases.getDetails(token);
			String databaseL = Databases.getLiveDetails(token);
			saveBlob("BILL", Bill.getBytes());
			saveBlob("RBILL", RBill.getBytes());
			saveBlob("VMACHINE", VMachine.getBytes());
			saveBlob("VMACHINEL", VMachineL.getBytes());
            saveBlob("DATA", database.getBytes());
            saveBlob("DATAL", databaseL.getBytes());   
		}
		saveBlob("LASTRUN", new Date(System.currentTimeMillis()).toLocaleString().getBytes());

		return true;
	}
	
	public boolean isTransformationCompatible()
	{
		return true;
	}
}
