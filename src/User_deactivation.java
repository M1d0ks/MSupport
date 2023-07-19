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


public class User_deactivation {
	public static void main(String args[]) throws DfException, IOException
	{


		DFCHelper dfcHelper = new DFCHelper();
		String docbaseLoginId = "dmadmin-p";
		String docbasePassword = "merlin";
		String docbaseName = "midocsp";
		System.out.println("ReferenceUtil: main: enter");
		IDfSession dfSession = dfcHelper.createSession(docbaseLoginId, docbasePassword, docbaseName);

		String resQuery = "select * from dm_user where user_name in ('poploja1','flemith1','clarkju5','jonesed1','jamilah2','william2','warnime1','meyermak','hearndi2','reeseal1','friedmib','jhaveli1','micilel1','francel2','awanfa1','phamje2','mortoma1','bodlave1','willita3','murphni1','millemij','singhaka','dewelma1','yooji5','takerja1','4-Apr','beckehe2','jaitlni1','tomliea1','schooch2','obriesh2','urakr1','falcoda1','solipra1','hsube2','oduolfu1','gazdost1','feltosa1','visagda1','mannitr1','mooreki5','shahprh','anderky5','fichtbr2','nelsosh2','naikka5','wangir3','pushkna1','alonslo3','baguldr1','gabrion2','kimjek','nelsoma9','lindeco2','wortzge1','paintme1','chamblo1','crossjo3','shahru6','choppbe1','rizvini1','bermuja2','chugbad1','patelak9','doshiso1','blaneer1','wongke1','radoval2','branvad1','bruinjo4','caldeke3','cookas2','aldapmi1','curtiwi2','harmojo2','loweni1','morenlo2','nortoka3','patelpaq','ryceran1','sattlca1','schonda2','smithmel','sonidh1','winteta3','sadakra1','horrasa1','cargeli1','bandabi1','spearje2','patilja9','paulan8','kumarla8','brownerc','vaidych2','cecchco1','chandra7','sawakdi1','kadamra8','wilkich4','reynosa4','rossje2','gogulvi1','ensanme1','srivane6','patilke5','aryaas3','matheni4','mckeoal1','kharamr1','eddenmi1','sharms4c','waltome2','jadhase2','frailom1','reddyya2','mohitna2','klotcje1','rajre2','dashle1','niazifa4','ankamsa1','kleinkr3')";
		IDfCollection resColl = DFCHelper.executeQuery(dfSession, resQuery);
		int i=1;
		while(resColl.next())
		{
				String objId=resColl.getString("r_object_id");
				System.out.println(objId);

				IDfUser user=(IDfUser) dfSession.getObjectByQualification
						("dm_user where r_object_id = '"+objId+"'");

				String userName=user.getString("user_name");
				System.out.println("User "+userName+" Deactivated");
				user.setString("user_state", "3");
				user.save();
				i++;
				System.out.println(userName);
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