import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;

public class getContentType {
public static void main(String args[]) throws DfException, IOException
{

	DFCHelper dfcHelper = new DFCHelper();
	String docbaseLoginId = "dmadmin-p";
	String docbasePassword = "merlin";
	String docbaseName = "midocsp";
	System.out.println("ReferenceUtil: main: enter");
	IDfSession dfSession = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);
	
	String resQuery="select r_object_id from dm_sysobject where r_object_id='0902b720802ea313'";
	
	IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
	
	while(resColl.next())
	{
		String object_id = resColl.getString("r_object_id");
		IDfSysObject document = (IDfSysObject)dfSession.getObject(new DfId(object_id));
		
		String sContentType = document.getContentType();
		
		System.out.println("Content Type ==== "+sContentType);
	}
		
}
}
