import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Iterator;

import javax.mail.Session;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfGroup;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfUser;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;


public class User_UAM {
	public static void main(String args[]) throws DfException, IOException
	{

		String sDirectoryPath ="C:/Geodart/ONC_MIDOCS_UL_001_v1 0_Account Revalidation_Q2_2017_JC.xlsx";
		FileInputStream filein = new FileInputStream(new File(sDirectoryPath));
		Workbook workbook = new XSSFWorkbook(filein);
		ArrayList<String> users=new ArrayList<String>();

		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "dmadmin-np";
		String docbasePassword = "merlin";
		String docbaseName = "midocsq";
		System.out.println("ReferenceUtil: main: enter");
		IDfSession dfSession = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);

		/*Sheet firstSheet = workbook.getSheetAt(1);
		int count=0;
		for(int i=0;i<firstSheet.getLastRowNum();i++)
		{
			try{
				Cell val1=firstSheet.getRow(i).getCell(6);
				//System.out.println(count+" "+val1.getStringCellValue());
				if((val1.getStringCellValue()).equals("Remove"))
				{
					Cell val=firstSheet.getRow(i).getCell(1);
				//	System.out.println(count+" "+val.getStringCellValue());
					users.add(val.getStringCellValue());
					count++;
				}
			}catch(Exception e)
			{
				System.out.println(e);
			}
		}
		String a;
		a=getdelimited(users);
		System.out.println(a);
		 */
		String resQuery = "select r_object_id from dm_user where user_name not in ('dmadmin-np','Merlin') and user_state='0'";
		IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
		int i=0;
		while(resColl.next())
		{
			if(i<1)
			{
				String objId=resColl.getString("r_object_id");
				System.out.println(objId);

				IDfUser user=(IDfUser) dfSession.getObjectByQualification
						("dm_user where r_object_id = '"+objId+"'");

				String userName=user.getString("user_name");
				user.setString("user_state", "2");
				user.save();
				i++;
				System.out.println(userName);
			}else{
				break;
			}
		}
		resColl.close();
	}

	private static String getdelimited(ArrayList<String> users) {
		String x="("+"'"+null+"'";
		for(int i=0;i<users.size();i++)
		{
			x=x+","+"'"+users.get(i)+"'";
		}
		x=x+")";
		return x;
	}
}