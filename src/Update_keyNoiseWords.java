import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Test_Extract.DFCHelper;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;


public class Update_keyNoiseWords {
	public static void main(String args[]) throws DfException, IOException
	{		
		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "dmadmin-p";
		String docbasePassword = "merlin";
		String docbaseName = "midocsp";
		System.out.println("ReferenceUtil: main: enter");
		IDfSession dfSession = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);

		String sDirectoryPathIn ="C:/Geodart/Keywords_Noisewords.xlsx";
		FileInputStream fileIn = new FileInputStream(new File(sDirectoryPathIn));
		Workbook workbook = new XSSFWorkbook(fileIn);

		Sheet datatypeSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = datatypeSheet.iterator();

	//Workbook workbook1 = new SXSSFWorkbook(1000);
		Sheet sheet = workbook.getSheetAt(0);

		int i=0;
			while (iterator.hasNext()) {
			try{
				
				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();
			
				Cell objN=currentRow.getCell(0);
				String objName=objN.getStringCellValue();
				
				Cell keyword=currentRow.getCell(4);
				String skeyWord=keyword.getStringCellValue();
				
				Cell noiseword=currentRow.getCell(6);
				String sNoiseWord=noiseword.getStringCellValue();

				System.out.println("****** Object_name *********"+objN+" ***Keyword*** "+skeyWord+" *****Noiseword**** "+sNoiseWord+" Number "+i);

				IDfSysObject doc = (IDfSysObject)dfSession.getObjectByQualification("mi_response where response_type='1' and object_name='"+objN+"'");
				String[] a = skeyWord.split(",");
				//System.out.println("keywords a == "+a+" Length == "+a.length);
				for(int x=0;x<a.length;x++){
					//System.out.println("keywords a == "+a[x]);
					doc.appendString("emedical_keywords", a[x]);
				}
				doc.setString("emedical_noise_words", sNoiseWord);
				doc.save();
				System.out.println("Saved id === "+doc);
				i++;
			}catch(Exception e)
			{
				System.out.println(e); 
			}
		}

	}
}