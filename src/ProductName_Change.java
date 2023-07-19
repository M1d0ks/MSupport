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



public class ProductName_Change {
public static void main(String args[]) throws DfException
{
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "dmadmin-p";
	String docbasePassword = "merlin";
	String docbaseName = "midocsp";
	System.out.println("ReferenceUtil: main: enter");
	IDfSession session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	
	String oldName="Adalimumab";
	String newName="Hyrimoz";
	
	System.out.println("Adding products");
	//addProduct(session,newName,oldName);
	deleteProduct(session,oldName);
	
}

private static void addProduct(IDfSession session, String newName, String oldName) throws DfException {
	//String query="Select r_object_id from mi_referenc where any product_name='"+oldName+"'";
	String query="Select r_object_id from mi_response where any product_name='"+oldName+"'";
	
	System.out.println("Query "+query);

	IDfCollection col = DFCHelper.executeQuery(session, query);
	int count=0;
	while(col.next())
	{
		System.out.println("Number of document "+count);
		String object_id=col.getString("r_object_id");
		count++;
	
		IDfSysObject document = (IDfSysObject)session.getObject(new DfId(object_id));
    
		if(!document.isCheckedOut())
		{
		String objName=document.getString("object_name");
		System.out.println("Adding product to the document "+objName);
		
		document.appendString("product_name", newName);
		document.save();
		}
	}
}

private static void deleteProduct(IDfSession session, String oldName) throws DfException {
	
	String query="Select r_object_id from mi_response where any product_name='"+oldName+"' ";
	//String query="Select r_object_id from mi_reference where any product_name='"+oldName+"' ";
	
	System.out.println("Query "+query);
	IDfCollection col = DFCHelper.executeQuery(session, query);
	
	while(col.next())
	{
		String object_id=col.getString("r_object_id");
		IDfSysObject document = (IDfSysObject)session.getObject(new DfId(object_id));

		String product_names=document.getAllRepeatingStrings("product_name", ",");
		System.out.println("Product_name "+product_names);
		String[] products=product_names.split(",");
		
		String objName=document.getString("object_name");
		System.out.println("Adding product to the document "+objName);
		
		for(int i=0;i<products.length;i++)
		{
			System.out.println(products[i]);
			if(!document.isCheckedOut())
			{
			if((products[i]).equals(oldName))
			{
				document.remove("product_name", i);
				document.save();
			}
			}
		}
		
	}
}
}