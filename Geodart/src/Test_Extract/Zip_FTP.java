package Test_Extract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;


public class Zip_FTP {
public static void main(String args[]) throws JSchException, SftpException
{

	DateFormat df = new SimpleDateFormat("yyyyMMdd");
	Calendar cal1 = Calendar.getInstance();
	cal1.add(Calendar.DATE, 1);
	String fDate=df.format(cal1.getTime());
	System.out.println("Filename time  "+fDate);

	
	String srcFiles[]={"/u01/home/dmadmin-np/Geoda3rt/MIDOC_DOCHISTORY_"+fDate+".txt","/u01/home/dmadmin-np/Geoda3rt//MIDOC_DOCUMENT_"+fDate+".txt","/u01/home/dmadmin-np/Geoda3rt/MIDOC_PRODUCT_"+fDate+".txt","/u01/home/dmadmin-np/Geoda3rt/MIDOC_RELATIONSHIP_"+fDate+".txt"};
	
	
	byte[] buffer = new byte[1024]; 

	try{

		FileOutputStream fos = new FileOutputStream("/u01/home/dmadmin-np/Geoda3rt/Prod_jars/MIDOC_"+fDate+".zip");
		ZipOutputStream zos = new ZipOutputStream(fos);
		
		for(int i=0;i<srcFiles.length;i++)
		{
		
		File srcFile = new File(srcFiles[i]);
		FileInputStream in = new FileInputStream(srcFile);
		System.out.println("Zipping this document "+srcFile.getName());
		ZipEntry ze= new ZipEntry(srcFile.getName());
		zos.putNextEntry(ze);
		
		int len;
		while ((len = in.read(buffer)) > 0) {
			
			zos.write(buffer, 0, len);
		}
		
		zos.closeEntry();
		//remember close it
		
		in.close();
		System.out.println("Done");
		}
		zos.close();
	}catch(Exception e)
	{
		System.out.println(e);
	}
	
	String path = "SOME/PATH";

	
	
    String sftpUser="sftp_midoc";
    
    // production 
    String sftpHost="PHUSEH-SP220074";
    //QA String sftpHost="PHUSEH-ST220038";
    
    String localFilePath="/u01/home/dmadmin-np/Geoda3rt/Prod_jars/MIDOC_"+fDate+".zip";
    String remoteFilePath="/opt/data/sftp_midoc/MIDOC_"+fDate+".zip";		
    
    final File file = new File(localFilePath);
	file.setReadable(true, false);
	file.setExecutable(true, false);
	file.setWritable(true, false);

    System.out.println("Initiating SFTP transfer");
	sendSftp(sftpUser,sftpHost,localFilePath,remoteFilePath);
}

private static void sendSftp(String sftpUser, String sftpHost, String localFilePath, String remoteFilePath) throws JSchException, SftpException {
	JSch jsch = new JSch();
	Session session = null;
	ChannelSftp c = null;
	String privateKeyPath="/u01/home/dmadmin-np/.ssh/id_rsa";
	
	jsch.addIdentity(privateKeyPath);
	session = jsch.getSession(sftpUser, sftpHost);
	
	java.util.Properties config = new java.util.Properties();
	config.put("StrictHostKeyChecking", "no");
	session.setConfig(config);
	session.connect();
	
	 Channel channel = session.openChannel("sftp");
     channel.setInputStream(System.in);
     channel.setOutputStream(System.out);
     channel.connect();
     c = (ChannelSftp) channel;
     try{
    	 System.out.println("Putting the extract ");
     c.put(localFilePath, remoteFilePath);
     System.out.println("Done ");
     }catch(SftpException e)
     {
    	 System.out.println("Exception encountered while uploading "+e);
     }
     }
}
