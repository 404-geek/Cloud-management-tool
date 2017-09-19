package com.yf.azure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.yf.azure.TestSourceMetaData;
import com.yf.utils.AzureAuth;
import com.yf.utils.Refresher;
import com.yf.utils.Resources;
import com.yf.utils.VirtualMachine;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hof.mi.thirdparty.interfaces.AbstractDataSet;
import com.hof.mi.thirdparty.interfaces.AbstractDataSource;
import com.hof.mi.thirdparty.interfaces.AggregationType;
import com.hof.mi.thirdparty.interfaces.ColumnMetaData;
import com.hof.mi.thirdparty.interfaces.DataType;
import com.hof.mi.thirdparty.interfaces.FilterData;
import com.hof.mi.thirdparty.interfaces.FilterOperator;
import com.hof.mi.thirdparty.interfaces.FilterMetaData;
import com.hof.mi.thirdparty.interfaces.ScheduleDefinition;
import com.hof.mi.thirdparty.interfaces.ThirdPartyException;
import com.hof.mi.thirdparty.interfaces.ScheduleDefinition.FrequencyTypeCode;
import com.hof.pool.JDBCMetaData;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

public class TestDataSource extends AbstractDataSource {

	public String getDataSourceName() {

		return "azure";

	}

	public ScheduleDefinition getScheduleDefinition() {
		/*
		 * In this function define the frequency with which Yellowfin should
		 * execute the autorun function
		 */
		return new ScheduleDefinition(FrequencyTypeCode.MINUTES, null, 5);
	};

	public ArrayList<AbstractDataSet> getDataSets() {

		/*
		 * In this function define the list of datasets available for this
		 * connector.
		 */

		ArrayList<AbstractDataSet> p = new ArrayList<AbstractDataSet>();

		/* Each dataset is an AbstractDataSet. */
		p.add(inventory());
		p.add(virtualMachine());
		p.add(virtualMachineLive());

		return p;

	}

	private AbstractDataSet inventory() {
		AbstractDataSet simpleDataSet = new AbstractDataSet() {

			public ArrayList<FilterMetaData> getFilters() {
				/* In this function define the list of available filters */
				ArrayList<FilterMetaData> fm = new ArrayList<FilterMetaData>();
				return fm;

			}

			public String getDataSetName() {
				/* Here define the dataset name */
				return "inventory";

			}

			public ArrayList<ColumnMetaData> getColumns() {

				/*
				 * In this function define the list of columns available in the
				 * dataset
				 */
				ArrayList<ColumnMetaData> cm = new ArrayList<ColumnMetaData>();

				cm.add(new ColumnMetaData("id", DataType.TEXT));
				cm.add(new ColumnMetaData("name", DataType.TEXT));
				cm.add(new ColumnMetaData("type", DataType.TEXT));
				cm.add(new ColumnMetaData("location", DataType.TEXT));
				cm.add(new ColumnMetaData("Resource Group String", DataType.TEXT));

				return cm;
			}

			@Override
			public boolean getAllowsDuplicateColumns() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean getAllowsAggregateColumns() {
				// TODO Auto-generated method stub
				return false;
			}

			public Object[][] execute(List<ColumnMetaData> columns, List<FilterData> filters) {

				if (TestDataSource.this.loadBlob("LASTRUN") == null) {
					throw new ThirdPartyException("Database is not yet populated");
				}
				/*
				 * This is the main function that should return a dataset
				 * according to user's preferences The dataset is represented by
				 * an object matrix. List<ColumnMetaData> columns contains the
				 * list of columns that the user has selected. List<FilterData>
				 * filters contains the filters and their values that the user
				 * has selected
				 */

				int arrSize = 0;
				String token = new String(loadBlob("accessToken"));
				String Inventory = new Resources().getResources(token);
				JsonElement je = new JsonParser().parse(Inventory);
				JsonObject jo = je.getAsJsonObject();
				JsonArray ja = jo.getAsJsonArray("value");
				arrSize = ja.size();
				saveBlob("INVENTORY", Inventory.getBytes());
				String nodeData = new String(loadBlob("INVENTORY"));
				Configuration conf = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
						.addOptions(Option.SUPPRESS_EXCEPTIONS);
				DocumentContext tt = JsonPath.using(conf).parse(nodeData);

				Object[][] data = null;
				data = new Object[arrSize][columns.size()];
				int i, j;
				Object val = null;
				for (i = 0; i < arrSize; i++) {
					for (j = 0; j < columns.size(); j++) {
						if (columns.get(j).getColumnName().equals("id")) {
							val = tt.read("$.['value'].[" + i + "].['id']");
							data[i][j] = val.toString();
						} else if (columns.get(j).getColumnName().equals("name")) {
							val = tt.read("$.['value'].[" + i + "].['name']");
							data[i][j] = val.toString();
						}

						else if (columns.get(j).getColumnName().equals("type")) {
							val = tt.read("$.['value'].[" + i + "].['type']");
							data[i][j] = val.toString();
						}

						else if (columns.get(j).getColumnName().equals("location")) {
							val = tt.read("$.['value'].[" + i + "].['location']");
							data[i][j] = val.toString();
						} else if (columns.get(j).getColumnName().equals("Resource Group String")) {
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

	private AbstractDataSet virtualMachineLive() {
		AbstractDataSet simpleDataSet = new AbstractDataSet() {

			public ArrayList<FilterMetaData> getFilters() {
				/* In this function define the list of available filters */
				ArrayList<FilterMetaData> fm = new ArrayList<FilterMetaData>();
				return fm;

			}

			public String getDataSetName() {
				/* Here define the dataset name */
				return "virtualMachineLive";

			}

			public ArrayList<ColumnMetaData> getColumns() {

				/*
				 * In this function define the list of columns available in the
				 * dataset
				 */
				ArrayList<ColumnMetaData> cm = new ArrayList<ColumnMetaData>();

				cm.add(new ColumnMetaData("Vm ID", DataType.TEXT));
				cm.add(new ColumnMetaData("Resource Type", DataType.TEXT));
				cm.add(new ColumnMetaData("OS Type", DataType.TEXT));
				cm.add(new ColumnMetaData("Vm Type", DataType.TEXT));
				cm.add(new ColumnMetaData("Location", DataType.TEXT));
				cm.add(new ColumnMetaData("TimeStamp", DataType.TIMESTAMP));
				cm.add(new ColumnMetaData("Percentage CPU", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Network IN", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Network Out", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Read", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Write", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Read Operations", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Write Operations", DataType.NUMERIC));


				return cm;
			}

			@Override
			public boolean getAllowsDuplicateColumns() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean getAllowsAggregateColumns() {
				// TODO Auto-generated method stub
				return false;
			}

			public Object[][] execute(List<ColumnMetaData> columns, List<FilterData> filters) {

				if (TestDataSource.this.loadBlob("LASTRUN") == null) {
					throw new ThirdPartyException("Database is not yet populated");
				}
				/*
				 * This is the main function that should return a dataset
				 * according to user's preferences The dataset is represented by
				 * an object matrix. List<ColumnMetaData> columns contains the
				 * list of columns that the user has selected. List<FilterData>
				 * filters contains the filters and their values that the user
				 * has selected
				 */

				int arrSize = 0;
				String token = new String(loadBlob("accessToken"));
				String Inventory = new Resources().getResources(token);
				JsonElement je = new JsonParser().parse(Inventory);
				JsonObject jo = je.getAsJsonObject();
				JsonArray ja = jo.getAsJsonArray("value");
				arrSize = ja.size();
				saveBlob("INVENTORY", Inventory.getBytes());
				String nodeData = new String(loadBlob("INVENTORY"));
				Configuration conf = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
						.addOptions(Option.SUPPRESS_EXCEPTIONS);
				DocumentContext tt = JsonPath.using(conf).parse(nodeData);

				Object[][] data = null;
				data = new Object[arrSize][columns.size()];
				int i, j;
				Object val = null;
				for (i = 0; i < arrSize; i++) {
					for (j = 0; j < columns.size(); j++) {
						if (columns.get(j).getColumnName().equals("Vm ID")) {
							val = tt.read("$.['vmdetails'].[" + i + "].['vmID']");
							data[i][j] = val.toString();
						} else if (columns.get(j).getColumnName().equals("Resource Type")) {

							val = tt.read("$.['vmdetails'].[" + i + "].['Resource Type']");
							data[i][j] = val.toString();
						}

						else if (columns.get(j).getColumnName().equals("OS Type")) {
							val = tt.read("$.['vmdetails'].[" + i + "].['OS Type']");
							data[i][j] = val.toString();
						}

						else if (columns.get(j).getColumnName().equals("Vm Type")) {
							val = tt.read("$.['vmdetails'].[" + i + "].['VM Size']");
							data[i][j] = val.toString();
						} else if (columns.get(j).getColumnName().equals("Location")) {
							val = tt.read("$.['vmdetails'].[" + i + "].['Location']");
							data[i][j] = val.toString();

						} else if (columns.get(j).getColumnName().equals("TimeStamp")) {
							val = tt.read("$.['vmdetails'].[" + i + "].['TimeStamp']");
							String time = val.toString();

						}
						else if (columns.get(j).getColumnName().equals("TimeStamp"))
						{
							val = tt.read("$.['vmdetails'].["+i+"].['TimeStamp']");
							String inpt = val.toString();
							SimpleDateFormat sdfgmt = new SimpleDateFormat("YYYY-MM-DD'T'HH:MM:SS'Z'");
        						sdfgmt.setTimeZone(TimeZone.getTimeZone("UTC"));

        						SimpleDateFormat sdfmad = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        						sdfmad.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
       							System.out.println("Asia/Kolkata:  " + sdfmad.format(sdfgmt.parse(inpt)));
							
						}
					}
				}

				return data;
			}

		};

		return simpleDataSet;
	}

	private AbstractDataSet virtualMachine() {
		// TODO Auto-generated method stub
		AbstractDataSet simpleDataSet = new AbstractDataSet() {
			public ArrayList<FilterMetaData> getFilters() {
				/* In this function define the list of available filters */
				ArrayList<FilterMetaData> fm = new ArrayList<FilterMetaData>();
				return fm;

			}

			public String getDataSetName() {
				/* Here define the dataset name */
				return "virtualMachine";

			}

			public ArrayList<ColumnMetaData> getColumns() {

				/*
				 * In this function define the list of columns available in the
				 * dataset
				 */
				ArrayList<ColumnMetaData> cm = new ArrayList<ColumnMetaData>();

				cm.add(new ColumnMetaData("Vm ID", DataType.TEXT));
				cm.add(new ColumnMetaData("Resource Type", DataType.TEXT));
				cm.add(new ColumnMetaData("OS Type", DataType.TEXT));
				cm.add(new ColumnMetaData("Vm Type", DataType.TEXT));
				cm.add(new ColumnMetaData("Location", DataType.TEXT));
				cm.add(new ColumnMetaData("TimeStamp", DataType.TIMESTAMP));
				cm.add(new ColumnMetaData("Percentage CPU", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Network IN", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Network Out", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Read", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Write", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Read Operations", DataType.NUMERIC));
				cm.add(new ColumnMetaData("Disk Write Operations", DataType.NUMERIC));

				return cm;
			}

			@Override
			public boolean getAllowsAggregateColumns() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean getAllowsDuplicateColumns() {
				// TODO Auto-generated method stub
				return false;
			}

			public Object[][] execute(List<ColumnMetaData> columns, List<FilterData> filters) {
				// TODO Auto-generated method stub
				if (TestDataSource.this.loadBlob("LASTRUN") == null) {
					throw new ThirdPartyException("Database is not yet populated");
				}
				/*
				 * This is the main function that should return a dataset
				 * according to user's preferences The dataset is represented by
				 * an object matrix. List<ColumnMetaData> columns contains the
				 * list of columns that the user has selected. List<FilterData>
				 * filters contains the filters and their values that the user
				 * has selected
				 */

				int arrSize, arrs = 0;
				String token = new String(loadBlob("accessToken"));
				new VirtualMachine();
				String VMachine = VirtualMachine.getDetails(token);
				// String VMetrics = VirtualMachine.getvm(token);
				JsonElement je = new JsonParser().parse(VMachine);
				JsonObject jo = je.getAsJsonObject();
				JsonArray ja = jo.getAsJsonArray("vmdetails");
				arrSize = ja.size();
				saveBlob("VMACHINE", VMachine.getBytes());
				String nodeData = new String(loadBlob("VMACHINE"));
				Configuration conf = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
						.addOptions(Option.SUPPRESS_EXCEPTIONS);
				DocumentContext tt = JsonPath.using(conf).parse(nodeData);

				Object[][] data = null;
				data = new Object[arrSize][columns.size()];
				int i, j;
				Object val = null;
				for (i = 0; i < arrSize; i++) {
					for (j = 0; j < columns.size(); j++) {
						if (columns.get(j).getColumnName().equals("Vm ID")) {
							val = tt.read("$.['vmdetails'].[" + i + "].['vmID']");
							data[i][j] = val.toString();
						} else if (columns.get(j).getColumnName().equals("Resource Type")) {

							val = tt.read("$.['vmdetails'].[" + i + "].['Resource Type']");
							data[i][j] = val.toString();
						}

						else if (columns.get(j).getColumnName().equals("OS Type")) {
							val = tt.read("$.['vmdetails'].[" + i + "].['OS Type']");
							data[i][j] = val.toString();
						}

						else if (columns.get(j).getColumnName().equals("Vm Type")) {
							val = tt.read("$.['vmdetails'].[" + i + "].['VM Size']");
							data[i][j] = val.toString();
						} else if (columns.get(j).getColumnName().equals("Location")) {
							val = tt.read("$.['vmdetails'].[" + i + "].['Location']");
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

		/*
		 * In this function you should define the actions that the connector
		 * should perform if the user clicks the 'Test Connection' button. If
		 * you want to tell Yellowfin that the connection was not successful
		 * then the Map that you return should contain a value with key "ERROR"
		 */
		Map<String, Object> p = new HashMap<String, Object>();
		try {
			String authCode = (String) getAttribute("CODE");
			JsonElement je = new JsonParser().parse(authCode);
			try {
				if (AzureAuth.authCheck(authCode) == 200) {
					JsonObject jo = je.getAsJsonObject();
					String accessToken = jo.get("access_token").getAsString();
					String refreshToken = jo.get("refresh_token").getAsString();
					saveBlob("accessToken", accessToken.getBytes());
					saveBlob("refreshToken", refreshToken.getBytes());
				}
				if (AzureAuth.authCheck(authCode) != 200) {
					String refreshTok = new String(loadBlob("refreshToken"));
					String accessTok = Refresher.refreshToken(refreshTok);
					saveBlob("accessToken", accessTok.getBytes());
					saveBlob("refreshToken", refreshTok.getBytes());
				}
			} catch (Exception e) {
				p.put("ERROR DS", "Invalid Authentication Code");
			}
		} catch (Exception e) {
			p.put("ERROR DS", "Please Enter Authentication Code");
		}
		return p;
	}

	// String authCode=(String)getAttribute("CODE");
	// JsonElement je = new JsonParser().parse(new
	// AzureAuth().getResponse(authCode));
	// JsonObject jo = je.getAsJsonObject();

	// String accessToken = jo.get("access_token").getAsString();
	// String refreshToken = jo.get("refresh_token").getAsString();

	// if(new AzureAuth().authCheck(authCode)==200){

	// //System.out.println("RESSSSSSSSSSSSSSSSSSS
	// PONSSSSSSSSSSSSSSEEEEEEEEEE");
	// //System.out.println(accessToken);
	// //System.out.println(refreshToken);
	// saveBlob("accessToken", accessToken.getBytes());
	// saveBlob("refreshToken", refreshToken.getBytes());
	// }
	// if(new AzureAuth().authCheck(authCode) !=200) {
	// try {
	// String refreshTok = new String(loadBlob("URL"));
	// String accessTok = Refresher.refreshToken(refreshTok);
	// saveBlob("accessToken", accessTok.getBytes());
	// saveBlob("refreshToken", refreshToken.getBytes());

	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }

	// }

	// else{
	// p.put("ERROR DS", "Invalid Authentication Code");
	// }
	public boolean autoRun() {
		/*
		 * This function is being automatically called by Yellowfin with a
		 * frequency defined in ScheduleDefinition() function. It can be helpful
		 * if you need to run a background job for the connector (for example
		 * update tokens)
		 */
		System.out.println("Auto running Test data source");

		/*
		 * saveBlob(String key, byte[] value) and loadBlob(key) functions allow
		 * to store and retrieve data from database at given keys
		 */
		String aToken = "aaa";
		saveBlob("ACCESS_TOKEN", aToken.getBytes());
		aToken = new String(loadBlob("ACCESS_TOKEN"));

		saveBlob("LASTRUN", new Date(System.currentTimeMillis()).toLocaleString().getBytes());

		return true;
	}

}
