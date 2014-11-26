package meme;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tank
 * @date:26 Nov 2014 11:46:01
 * @description:
 * @version :1.0
 */
public class TestMain {
	
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date= sdf.parse("2009-09-15 00:00:00");
		
		System.out.println(date.getTime());
	}

}
