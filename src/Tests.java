
public class Tests {
public static void main(String args[])
		{
		int n=111;
		    String number = String.valueOf(n);
		    System.out.println("n "+number);
		    char[] digits = number.toCharArray();
		    int a=0; 
		    for(int i=0;i<digits.length;i++)
		     {
		    	System.out.println("a pre "+digits[i]);
		         a=a+((int)digits[i]- 48);
		     }
		    System.out.println(a);
		}
}
