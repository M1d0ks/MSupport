import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchTest {
public static void main(String args[])
{
	
	String searchTerm="US-";
	
	if((searchTerm.substring(0,3).equals("US-"))||(searchTerm.substring(0,4).equals("REF-")))
	{
		System.out.println("Simple search");
	}else{
		System.out.println("no");
	}
	}
}
