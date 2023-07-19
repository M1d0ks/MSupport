
import java.util.HashMap;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfWorkflow;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;
import com.emc.d2.api.D2Session;
import com.emc.d2.api.methods.D2CoreMethod;
import com.emc.d2.api.methods.D2Method;



public class Refauthor_cleanup {
public static void main(String args[]) throws DfException
{
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "midocsp";
	String docbasePassword = "m1d0csp74";
	String docbaseName = "midocs_prod";
	System.out.println("ReferenceUtil: main: enter");
	IDfSession session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	 
	//String proName="'Aimovig'";
	updateAuthors(session);
}

private static void updateAuthors(IDfSession session) throws DfException {

	//String query="select r_object_id,object_name from mi_reference(all) where reference_type!=0 and any rauthors like '%#%'";
	String query="select r_object_id,object_name from mi_reference(all) where r_object_id='0902e9e9801eb96f'";
	
	System.out.println("Query "+query);

	IDfCollection col = DFCHelper.executeQuery(session, query);
	int count=0; 
	while(col.next())
	{
		String object_id=col.getString("r_object_id");
		IDfSysObject document = (IDfSysObject)session.getObject(new DfId(object_id));
		System.out.println("Checking document "+document.getString("object_name")+" count "+count);
		/*String authors = document.getAllRepeatingStrings("rauthors",",");
		System.out.println("Authors "+authors);
		String[] sauthors;
		if(authors.contains("###")){
			sauthors = authors.split("###");
		
		for(int i=0;i<sauthors.length;i++){
			document.setRepeatingString("rauthors", i, sauthors[i]);
		}
		if(document!=null && document.getString("r_immutable_flag")!=null&& document.getBoolean("r_immutable_flag")){
			System.out.println("Is immutable");
			document.setString("r_immutable_flag","False");
			document.save();
			}
		if(!document.isCheckedOut())
		{
		document.save();
		}
		}*/
		//String objCounter = document.getString("obj_name_counter");
		//System.out.println("Object counter "+objCounter);
		//objCounter = objCounter.replaceAll("[a-zA-Z]","");
		//System.out.println("Object counter "+objCounter);
		if(document!=null && document.getString("r_immutable_flag")!=null&& document.getBoolean("r_immutable_flag")){
			System.out.println("Is immutable");
			document.setString("r_immutable_flag","False");
			document.save();
			}
		if(document.isCheckedOut())
		{
		System.out.println("Canceled Checkout");
	    document.cancelCheckout();
		document.save();
		}
		count++;
	 }
	}

}