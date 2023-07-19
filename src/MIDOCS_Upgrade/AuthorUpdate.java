package MIDOCS_Upgrade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfGroup;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfUser;
import com.documentum.fc.client.IDfWorkflow;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;
import com.emc.d2.api.D2Session;
import com.emc.d2.api.methods.D2CoreMethod;
import com.emc.d2.api.methods.D2Method;



public class AuthorUpdate {
public static void main(String args[]) throws DfException, IOException
{
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "midocsp";
	String docbasePassword = "m1d0csp74";
	String docbaseName = "midocs_prod";
	System.out.println("ReferenceUtil: main: enter");
	IDfSession session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	 
	String sDirectoryPathIn ="C:/Midocs_Migration/MidocsAuthors.xlsx";
	FileInputStream fileIn = new FileInputStream(new File(sDirectoryPathIn));
	Workbook workbook = new XSSFWorkbook(fileIn);

	Sheet datatypeSheet = workbook.getSheetAt(0);
	Iterator<Row> iterator = datatypeSheet.iterator();
	
	Sheet sheet = workbook.getSheetAt(0);
	
	int i=0;
	while (iterator.hasNext()) {
	//try{
		
		Row currentRow = iterator.next();
		Iterator<Cell> cellIterator = currentRow.iterator();
	 
		Cell objN=currentRow.getCell(0);
		String doc=objN.getStringCellValue();
		
		Cell user=currentRow.getCell(1);
		String users=user.getStringCellValue();

		System.out.println("****** doc ********* "+doc+" user "+users+" count "+i);
		
		if(!users.isEmpty() && users!=null  )
		UpdateDoc(doc,users,session);
		i++;
		
}
}


private static void UpdateDoc(String doc,String users, IDfSession session) throws DfException {
	// TODO Auto-generated method stub
	//String query="select r_object_id,object_name from mi_reference(all) where reference_type!=0 and any rauthors like '%#%'";

	System.out.println("Retrieving user  "+users);
	IDfSysObject resp = (IDfSysObject)session.getObjectByQualification("mi_response where prev_obj_id=('"+ doc + "')");
	String userName =getUserName(users,session);
		if(userName!=null && resp!=null &&!resp.isImmutable() && !resp.isCheckedOut()){
		System.out.println("User Retrieved "+userName);
			resp.setString("owner_name",userName);
			System.out.println(" Adding user Name "+users);
			resp.setString("group_name","mi_authors");
			resp.save();
			System.out.println("Saved");
		}
	
}


private static String getUserName(String users, IDfSession session) throws DfException {
	IDfUser userDoc = (IDfUser)session.getObjectByQualification("dm_user where user_os_name=('"+ users + "')");
	if(userDoc!=null){
	String username= userDoc.getString("user_name");
	return username;
	}else{
		return null;
	}
}

}
//select r_object_id,users_names,group_name from dm_group where group_name in ('mi_onc_authors','mi_gen_authors','mi_gen_approvers','mi_onc_approvers')