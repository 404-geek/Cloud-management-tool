package com.yf.azure;

import java.util.HashMap;


import com.hof.mi.interfaces.UserInputParameters.Parameter;
import com.hof.pool.DBType;
import com.hof.pool.JDBCMetaData;
import com.hof.util.UtilString;
import com.yf.utils.AzureAuth;

public class TestSourceMetaData extends JDBCMetaData {
ArrayList<String> list=Timezone.getTimeZone();
	public TestSourceMetaData() {

		super();

		sourceName = "azure";
		sourceCode = "TEST_DATA_SOURCE";
		driverName = "com.yf.azure.TestDataSource";
		sourceType = DBType.THIRD_PARTY;
	}

	public void initialiseParameters() {
		super.initialiseParameters();

		/*
		 * In this function need to define the list of parameters that are
		 * required to be filled to establish a connection to the webservice.
		 */
		Parameter pq = new Parameter("URL", "Generate Authentication Code", "Click here to generate Authcode", TYPE_UNKNOWN,
				DISPLAY_URLBUTTON, null, true);
		pq.addOption("BUTTONTEXT", "Authorize Azure");
		pq.addOption("BUTTONURL",
				"https://login.microsoftonline.com/common/OAuth2/Authorize?client_id=b3144fe1-6338-43a0-8918-c05291f51170&response_mode=query&response_type=code&redirect_uri=https://tpconnect.yellowfin.bi/getToken.jsp&resource=https://management.core.windows.net/&domain_hint=consumers&prompt=consent&state=123456");
		/*
		 * In this function need to define the list of parameters that are
		 * required for the authentication
		 */
		Parameter pq1 = new Parameter("SELECTCOR", "TimeZone Selector", "Click here to select TimeZone", TYPE_UNKNOWN,
				DISPLAY_URLBUTTON, null, true 
			for (String id : list) {
			pq1.addOption(id);
		}
					      
		
        
  
	}
	

	public String buttonPressed(String buttonName) throws Exception {
		/*
		 * In this function you should define the actions that should be
		 * performed in case if some button was pressed. String buttonName
		 * contains the ID of the button that was pressed
		 */
		if (buttonName.equals("ValidateToken")) {
			String code = null;
			code = (String) getParameterValue("CODE");
			if (code == null || code.length() < 1) {
				return "Provide Authentication Code";
			}
			 
			
			if(AzureAuth.authCheck(code)!=200){
				
				return "Error TM: Invalid Authentication";
			}
		}	 

		return null;
	}

	@Override
	public byte[] getDatasourceIcon() {
		/* This function should return Base-64 encrypted version of icon file */
		String str = "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";
		return str.getBytes();
	}

	@Override
	public String getDatasourceShortDescription() {
		return "Azure";
	}

	@Override
	public String getDatasourceLongDescription() {
		return "Azure";
	}
}
