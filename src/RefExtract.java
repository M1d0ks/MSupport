import Test_Extract.DFCHelper;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfLoginInfo;
import com.documentum.operations.IDfExportNode;
import com.documentum.operations.IDfExportOperation;


public class RefExtract {
	public static void main(String args[]) throws DfException
	{
		String sDirectoryPath ="C:\\Geodart\\Tasigna_7-10\\";
		//String sDirectoryPath ="/home/kucheja1/refextract/";
		IDfSession session=null;
		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "dmadmin-p";
		String docbasePassword = "merlin";
		String docbaseName = "midocsp";
		System.out.println("ReferenceUtil: main: enter");
		session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
		
		IDfSession dfSession = null;
	    IDfSessionManager dfSessionManager = null;
	    System.out.println("instantiating client x libraries");
	    IDfClientX clientx = new DfClientX();
	    System.out.println("client x initialized");
	    IDfClient client = clientx.getLocalClient();
	    System.out.println("df client initialized");
	    dfSessionManager = client.newSessionManager();
	    System.out.println("retrieved session manager");
	    IDfLoginInfo loginInfo = clientx.getLoginInfo();
	    loginInfo.setUser(docbaseLoginId);
	    loginInfo.setPassword(docbasePassword);
	    System.out.println("setting user identity for docbase session");
	    dfSessionManager.setIdentity(docbaseName, loginInfo);
	    dfSession = dfSessionManager.getSession(docbaseName);
	    System.out.println("docbase session retrieved");  
		
		/*String resQuery = "select r_object_id,object_name,title from mi_response where object_name in ('US-MIS-S166','US-ILA-S002'"
				+ "'US-MIS-S003327','US-OOP-S003129','US-OOP-S001646','US-MIS-S181','US-MIS-S174','US-EVE-QV001036','US-LBH-S001475','US-LBH-S018')";*/
	  //String resQuery = "select r_object_id,object_name from mi_response where response_type=1 and a_status='Retired' and any ta_name not in ('Oncology','Cardiovascular and Metabolic','NeuroScience and Psychiatry','NeuroScience','Multiple Sclerosis','Dermatology','Transplant') and r_object_id in (select parent_id from dm_relation)";
	   String resQuery = "select r_object_id,object_name from mi_response(all) where legacy_object_id in ('090153898043af46','0901538980382e7c','09015389803c179c')";
	   
	   IDfCollection resColl = DFCHelper.executeQuery(session, resQuery);
		//IDfExportOperation exportOperation = clientx.getExportOperation();      
		//exportOperation.setDestinationDirectory(sDirectoryPath);
		//IDfExportNode node = null;  
		 
		String objName = null,objId;
		int i=0;
		while(resColl.next())
		{
			try{
		objId=resColl.getString("r_object_id");
		objName=resColl.getString("object_name");
		
		//String objectId=getRendition(objId,session);
		//System.out.println("Retrieved rendition obj id "+objectId);
		
		IDfSysObject document=(IDfSysObject)session.getObject((IDfId) new DfId(objId));
		String ext=document.getContentType();
		
		//IDfId id = new DfId(objId);
		//document.setObjectName(id.getId()+"_"+document.getObjectName());
		//node= (IDfExportNode) exportOperation.add(document);
		//node.setFormat("pdf");
	
		//String title = document.getString("title");
		String a = "?,=,[,],[,+,&,|,!,(,),{,},^,~,*,?,�,:,-,�,�,�,@,�,�,?,/";
		String[] escape = a.split(",");
		//for (int y = 0; y < escape.length; y++) {
		//	if (title.contains(escape[y])) {
		//		title = title.replace(escape[y], " ");
		//	}
	//	}
		//US-MIS-S178 
		 System.out.println("Getting object_name "+document+" with extension "+ext+" Current "+i);
		 i++;
		// title=title.replaceAll("\"","\\\\\"");
		 //title = title.replace("\n", " ").replace("\r", " ");
		if(ext.equals("msw8")){
		document.getFile(sDirectoryPath+objName+"_"+objId+"."+"doc");
		}else{
			document.getFile(sDirectoryPath+objName+"_"+objId+"."+ext);
		}
		//document.getContentEx("Pdf",0);
		//getRendition(document.getString("r_object_id"),dfSession);
			}catch(Exception e)
			{
				System.out.println("Exception objName "+objName+" "+e);
			}
		}
		resColl.close();                                     
                             
	}
}