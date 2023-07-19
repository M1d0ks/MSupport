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



public class UserGroupUpdates {
public static void main(String args[]) throws DfException, IOException
{
	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "midocsp";
	String docbasePassword = "m1d0csp74";
	String docbaseName = "midocs_prod";
	System.out.println("ReferenceUtil: main: enter");
	IDfSession session = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	 
	String sDirectoryPathIn ="C:/Midocs_Migration/mi_reviewers.xlsx";
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
		String groups=objN.getStringCellValue();
		
		Cell keyword=currentRow.getCell(1);
		String users=keyword.getStringCellValue();
		
		System.out.println("****** Object_name *********"+groups+" ***Keyword*** "+users);
		
		if(!groups.isEmpty() && groups!=null && !users.isEmpty() && users!=null )
		UpdateGroups(groups,users,session);
		
//	}catch(Exception ex){
//		System.out.println(ex);
//	}
	//String proName="'Aimovig'";
}
}

private static void UpdateGroups(String groups, String users, IDfSession session) throws DfException {
	// TODO Auto-generated method stub
	//String query="select r_object_id,object_name from mi_reference(all) where reference_type!=0 and any rauthors like '%#%'";
	String[] usersnames =users.split(",");
	
	for(int i=0;i<usersnames.length;i++){
		System.out.println("Retrieving user  "+usersnames[i]);
		IDfUser userDoc = (IDfUser)session.getObjectByQualification("dm_user where user_os_name=('"+ usersnames[i] + "')");
		if(userDoc!=null){
		System.out.println("User Retrieved "+userDoc.getString("user_name"));
		IDfGroup group= (IDfGroup)session.getGroup (groups);
			System.out.println(" Adding user Name "+usersnames[i]);
			group.addUser(userDoc.getString("user_name"));
			System.out.println(" Added");
			group.save();
			System.out.println("Saved");
		}
	}
}
}
//select r_object_id,users_names,group_name from dm_group where group_name in ('mi_onc_authors','mi_gen_authors','mi_gen_approvers','mi_onc_approvers')