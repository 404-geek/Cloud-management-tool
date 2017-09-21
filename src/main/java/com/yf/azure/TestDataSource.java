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
import com.yf.utils.Billing;
import com.yf.utils.Databases;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestDataSource extends AbstractDataSource {
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
		p.add(RateCard());

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
				String zone = new String(TestDataSource.this.loadBlob("zone"));
				cm.add(new ColumnMetaData("VmID", DataType.TEXT));
				cm.add(new ColumnMetaData("Resource Type", DataType.TEXT));
				cm.add(new ColumnMetaData("OS Type", DataType.TEXT));
				cm.add(new ColumnMetaData("VM Size", DataType.TEXT));
				cm.add(new ColumnMetaData("Location", DataType.TEXT));
				cm.add(new ColumnMetaData("Timestamp(UTC)", DataType.TIMESTAMP));
				cm.add(new ColumnMetaData("Timestamp(" + zone + ")", DataType.TIMESTAMP));
				cm.add(new ColumnMetaData("Percentage CPU", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Network In", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Network Out", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Read Bytes", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Write Bytes", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Read Operations", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Write Operations", DataType.NUMERIC));

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
				String zone = new String(TestDataSource.this.loadBlob("zone"));
				String VMachine = VirtualMachine.getLiveDetails(token);
				JsonElement je = new JsonParser().parse(VMachine);
				JsonArray ja = je.getAsJsonArray();
				saveBlob("VMACHINELIVE", VMachine.getBytes());
				String nodeData = new String(TestDataSource.this.loadBlob("VMACHINELIVE"));

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
						} else if (((ColumnMetaData) columns.get(j)).getColumnName()
								.equals("Timestamp(" + zone + ")")) {
							val = tt.read("$.[" + i + "].['Timestamp']");
							String timestamp = val.toString();
							DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
							utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

							Date date = null;
							try {
								date = utcFormat.parse(timestamp);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							pstFormat.setTimeZone(TimeZone.getTimeZone(zone));
							Timestamp ts = Timestamp.valueOf(pstFormat.format(date));
							data[i][j] = ts;
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Percentage CPU")) {
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

				cm.add(new ColumnMetaData("id", DataType.TEXT));
				cm.add(new ColumnMetaData("name", DataType.TEXT));
				cm.add(new ColumnMetaData("type", DataType.TEXT));
				cm.add(new ColumnMetaData("location", DataType.TEXT));
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
				JsonArray ja = jo.getAsJsonArray("value");
				TestDataSource.this.saveBlob("INVENTORY", Inventory.getBytes());
				String nodeData = new String(TestDataSource.this.loadBlob("INVENTORY"));

				Configuration conf = Configuration.defaultConfiguration()
						.addOptions(new Option[] { Option.DEFAULT_PATH_LEAF_TO_NULL })
						.addOptions(new Option[] { Option.SUPPRESS_EXCEPTIONS });
				DocumentContext tt = JsonPath.using(conf).parse(nodeData);

				Object[][] data = (Object[][]) null;
				data = new Object[ja.size()][columns.size()];

				Object val = null;
				for (int i = 0; i < ja.size(); i++) {
					for (int j = 0; j < columns.size(); j++) {
						if (((ColumnMetaData) columns.get(j)).getColumnName().equals("id")) {
							val = tt.read("$.['value'].[" + i + "].['id']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("name")) {
							val = tt.read("$.['value'].[" + i + "].['name']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("type")) {
							val = tt.read("$.['value'].[" + i + "].['type']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("location")) {
							val = tt.read("$.['value'].[" + i + "].['location']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Resource Group String")) {
							val = tt.read("$.['value'].[" + i + "].['id']");
							data[i][j] = val.toString();
							String str = val.toString();
							Pattern pat = Pattern.compile("resourceGroups/(.*?)/providers");
							Matcher m = pat.matcher(str);
							while (m.find()) {
								data[i][j] = m.group(1);
							}
						}
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
				return fm;
			}

			public String getDataSetName() {
				return "virtualMachine";
			}

			public ArrayList<ColumnMetaData> getColumns() {
				ArrayList<ColumnMetaData> cm = new ArrayList();
				String zone = new String(TestDataSource.this.loadBlob("zone"));
				cm.add(new ColumnMetaData("VmID", DataType.TEXT));
				cm.add(new ColumnMetaData("Resource Type", DataType.TEXT));
				cm.add(new ColumnMetaData("OS Type", DataType.TEXT));
				cm.add(new ColumnMetaData("VM Size", DataType.TEXT));
				cm.add(new ColumnMetaData("Location", DataType.TEXT));
				cm.add(new ColumnMetaData("Timestamp(UTC)", DataType.TIMESTAMP));
				cm.add(new ColumnMetaData("Timestamp(" + zone + ")", DataType.TIMESTAMP));
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
				String token = new String(TestDataSource.this.loadBlob("accessToken"));
				String zone = new String(TestDataSource.this.loadBlob("zone"));
				new VirtualMachine();
				String VMachine = VirtualMachine.getDetails(token);
				JsonElement je = new JsonParser().parse(VMachine);
				JsonArray ja = je.getAsJsonArray();
				TestDataSource.this.saveBlob("VMACHINE", VMachine.getBytes());
				String nodeData = new String(TestDataSource.this.loadBlob("VMACHINE"));

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
						} else if (((ColumnMetaData) columns.get(j)).getColumnName()
								.equals("Timestamp(" + zone + ")")) {
							val = tt.read("$.[" + i + "].['Timestamp']");
							String timestamp = val.toString();
							DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
							utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

							Date date = null;
							try {
								date = utcFormat.parse(timestamp);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							pstFormat.setTimeZone(TimeZone.getTimeZone(zone));
							Timestamp ts = Timestamp.valueOf(pstFormat.format(date));
							data[i][j] = ts;
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Percentage CPU")) {
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

				cm.add(new ColumnMetaData("Resource ID", DataType.TEXT));
				cm.add(new ColumnMetaData("Resource Group", DataType.TEXT));
				cm.add(new ColumnMetaData("Database Name", DataType.TEXT));
				cm.add(new ColumnMetaData("Timestamp", DataType.TIMESTAMP));
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
				String token = new String(loadBlob("accessToken"));
				String database = Databases.getDetails(token);
				JsonElement je = new JsonParser().parse(database);
				JsonArray ja = je.getAsJsonArray();
				TestDataSource.this.saveBlob("DATA", database.getBytes());
				String nodeData = new String(TestDataSource.this.loadBlob("DATA"));

				Configuration conf = Configuration.defaultConfiguration()
						.addOptions(new Option[] { Option.DEFAULT_PATH_LEAF_TO_NULL })
						.addOptions(new Option[] { Option.SUPPRESS_EXCEPTIONS });
				DocumentContext tt = JsonPath.using(conf).parse(nodeData);

				Object[][] data = (Object[][]) null;
				data = new Object[ja.size()][columns.size()];

				Object val = null;
				for (int i = 0; i < ja.size(); i++) {
					for (int j = 0; j < columns.size(); j++) {
						if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Resource ID")) {
							val = tt.read("$.[" + i + "].['Resource ID']");
							data[i][j] = val.toString();
						}
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
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Timestamp")) {
							val = tt.read("$.[" + i + "].['Timestamp']");
							String timestamp = val.toString();
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
							format.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
							try {
								Date parsedDate = format.parse(timestamp);
								Timestamp timeStampDate = new Timestamp(parsedDate.getTime());
								data[i][j] = timeStampDate;
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
				}
				return data;
			}
		};
		return simpleDataSet;
	}

	private AbstractDataSet RateCard() {
		AbstractDataSet simpleDataSet = new AbstractDataSet() {
			public ArrayList<FilterMetaData> getFilters() {
				ArrayList<FilterMetaData> fm = new ArrayList();
				return fm;
			}

			public String getDataSetName() {
				return "RateCard";
			}

			public ArrayList<ColumnMetaData> getColumns() {
				ArrayList<ColumnMetaData> cm = new ArrayList();
				String zone = new String(TestDataSource.this.loadBlob("zone"));
				cm.add(new ColumnMetaData("Currency", DataType.TEXT));
				cm.add(new ColumnMetaData("Tax", DataType.TEXT));
				cm.add(new ColumnMetaData("Locale", DataType.TEXT));
				//cm.add(new ColumnMetaData("EffectiveDate(UTC)", DataType.TIMESTAMP));
				cm.add(new ColumnMetaData("IncludedQuantity", DataType.INTEGER));
				cm.add(new ColumnMetaData("MeterCategory", DataType.TEXT));
				//cm.add(new ColumnMetaData("EffectiveDate(" + zone + ")", DataType.TIMESTAMP));
				cm.add(new ColumnMetaData("MeterRegion", DataType.TEXT));
				cm.add(new ColumnMetaData("MeterStatus", DataType.TEXT));
				cm.add(new ColumnMetaData("MeterSubCategory", DataType.TEXT));
				cm.add(new ColumnMetaData("MeterTags", DataType.TEXT));
				cm.add(new ColumnMetaData("MeterRates", DataType.TEXT));

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
				String zone = new String(TestDataSource.this.loadBlob("zone"));
				String RCard = Billing.getCard(token);
				JsonElement je = new JsonParser().parse(RCard);
				JsonArray ja = je.getAsJsonArray();
				saveBlob("RCARD", RCard.getBytes());
				String nodeData = new String(TestDataSource.this.loadBlob("RCARD"));

				Configuration conf = Configuration.defaultConfiguration()
						.addOptions(new Option[] { Option.DEFAULT_PATH_LEAF_TO_NULL })
						.addOptions(new Option[] { Option.SUPPRESS_EXCEPTIONS });
				DocumentContext tt = JsonPath.using(conf).parse(nodeData);

				Object[][] data = (Object[][]) null;
				data = new Object[ja.size()][columns.size()];

				Object val = null;
				for (int i = 0; i < ja.size(); i++) {
					for (int j = 0; j < columns.size(); j++) {
						if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Currency")) {
							val = tt.read("$.[" + i + "].['Currency']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Tax")) {
							val = tt.read("$.[" + i + "].['Tax']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Locale")) {
							val = tt.read("$.[" + i + "].['Locale']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("VM Size")) {
							val = tt.read("$.[" + i + "].['VMSize']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("Location")) {
							val = tt.read("$.[" + i + "].['Location']");
							data[i][j] = val.toString();
						} /*else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("EffectiveDate(UTC)")) {
							val = tt.read("$.[" + i + "].['EffectiveDate']");
							String timestamp = val.toString();
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
							try {
								Date parsedDate = format.parse(timestamp);
								Timestamp timeStampDate = new Timestamp(parsedDate.getTime());
								data[i][j] = timeStampDate;
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}*/ /*else if (((ColumnMetaData) columns.get(j)).getColumnName()
								.equals("EffectiveDate(" + zone + ")")) {
							val = tt.read("$.[" + i + "].['EffectiveDate']");
							String timestamp = val.toString();
							DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
							utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

							Date date = null;
							try {
								date = utcFormat.parse(timestamp);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							pstFormat.setTimeZone(TimeZone.getTimeZone(zone));
							Timestamp ts = Timestamp.valueOf(pstFormat.format(date));
							data[i][j] = ts;
						} */else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("IncludedQuantity")) {
							val = tt.read("$.[" + i + "].['IncludedQuantity']");
							data[i][j] = Integer.parseInt(val.toString());
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("MeterCategory")) {
							val = tt.read("$.[" + i + "].['MeterCategory']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("MeterRegion")) {
							val = tt.read("$.[" + i + "].['MeterRegion']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("MeterStatus")) {
							val = tt.read("$.[" + i + "].['MeterStatus']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("MeterSubCategory")) {
							val = tt.read("$.[" + i + "].['MeterSubCategory']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("MeterTags")) {
							val = tt.read("$.[" + i + "].['MeterTags']");
							data[i][j] = val.toString();
						} else if (((ColumnMetaData) columns.get(j)).getColumnName().equals("MeterRates")) {
							val = tt.read("$.[" + i + "].['MeterRates']");
							data[i][j] = val.toString();
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
			String zone = (String) getAttribute("SELECTOR");
			JsonElement je = new JsonParser().parse(AzureAuth.getResponse(authCode));
			try {
				new AzureAuth();
				if (AzureAuth.authCheck(authCode) == 200) {
					JsonObject jo = je.getAsJsonObject();
					String accessToken = jo.get("access_token").getAsString();
					String refreshToken = jo.get("refresh_token").getAsString();
					saveBlob("accessToken", accessToken.getBytes());
					saveBlob("refreshToken", refreshToken.getBytes());
					saveBlob("zone", zone.getBytes());
				}
				if (AzureAuth.authCheck(authCode) != 200) {
					String ref = new String(loadBlob("refreshToken"));
					String accessToken = Refresher.refreshToken(ref);
					saveBlob("accessToken", accessToken.getBytes());
					saveBlob("refreshToken", ref.getBytes());
					saveBlob("zone", zone.getBytes());
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

		String aToken = "aaa";
		saveBlob("ACCESS_TOKEN", aToken.getBytes());
		aToken = new String(loadBlob("ACCESS_TOKEN"));

		saveBlob("LASTRUN", new Date(System.currentTimeMillis()).toLocaleString().getBytes());

		return true;
	}
}
