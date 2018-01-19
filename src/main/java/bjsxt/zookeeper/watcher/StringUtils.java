package bjsxt.zookeeper.watcher;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.BASE64Encoder;

/**
 * ~{OnD?C{3F#:~}RenRenERP <br />
 * ~{@`C{3F#:~}StringUtils <br />
 * ~{44=(HK#:~}Administrator <br />
 * ~{18W"#:~} <br />
 * 
 * @version <br />
 */
public class StringUtils extends org.apache.commons.lang.StringUtils
{
	public static boolean isNullOrBlank(Object object)
	{
		if (object == null)
			return true;

		return trimToEmpty(String.valueOf(object)).length() == 0;
	}

	public static boolean isAnyBlank(String... string)
	{
		for (String s : string)
		{
			if (isBlank(s))
				return true;
		}

		return false;
	}

	public static boolean isAllBlank(String... string)
	{
		for (String s : string)
		{
			if (!isBlank(s))
				return false;
		}

		return true;
	}

	public static boolean equalsAny(String value, String... string)
	{
		for (String s : string)
		{
			if (equals(value, s))
				return true;
		}

		return false;
	}

	public static boolean equalsAny(String value, List<String> list)
	{
		for (String s : list)
		{
			if (equals(value, s))
				return true;
		}

		return false;
	}

	public static int notEmptyCount(String... string)
	{
		int count = 0;
		for (String s : string)
		{
			if (isNotEmpty(s))
				count++;
		}

		return count;
	}

	public static String substringLast(String string, int length)
	{
		if (isEmpty(string))
			return EMPTY;

		return substring(string, string.length() - length);
	}

	public static String upperCaseFirst(String string)
	{
		if (isEmpty(string))
			return EMPTY;

		char character = Character.toUpperCase(string.charAt(0));
		String substring = substringLast(string, string.length() - 1);
		return String.format("%s%s", character, substring);
	}

	public static String parse(Object object)
	{
		return parse(object, true);
	}

	public static String parse(Object object, boolean trim)
	{
		if (object == null)
			return EMPTY;

		return trim ? trimToEmpty(String.valueOf(object)) : String.valueOf(object);
	}
	
	public static boolean isLetterDigitOrChinese(String str) {
		  String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";
		  return str.matches(regex);
	}
	
	public static boolean is_number(String number) {
		if(number==null) return false;
	    return number.matches("[+-]?[1-9]+[0-9]*(\\.[0-9]+)?");    
	}
	
	public static boolean is_alpha(String alpha) {
		if(alpha==null) return false;
	    return alpha.matches("[a-zA-Z]+");    
	}
	
	public static boolean is_chinese(String chineseContent) {
		if(chineseContent==null) return false;
		return chineseContent.matches("[\u4e00-\u9fa5]");
	}
	
	public static String TransactSQLInjection(String str){
          return str.replaceAll(".*([';]+|(--)+).*", " ");
    }
	
	/**
	 * 用*号做掩码（用于身份证号码、电话号码）
	 * @Title: maskCode
	 * @Description: 
	 * @author zideng.he
	 * @date 2015-12-14 下午12:41:33
	 * @param no 待处理的字符串
	 * @param prefix_num 前缀prefix_num个字符保持不变
	 * @param stuffix_num 后缀stuffix_num个字符保持不变
	 * @return    
	 * @return String
	 */
	public static String maskCode(String no,int prefix_num,int stuffix_num) {
		if(isBlank(no))
			return "";
		if(no.length()-prefix_num-stuffix_num<=0)
			return no;
		String replacement = "******************************";
		replacement = replacement.substring(0, no.length()-prefix_num-stuffix_num);
		return no.substring(0,prefix_num)+replacement+no.substring(no.length()-stuffix_num);
	}
	
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 验证车牌号码
	 * @Title: isVehicleNumber
	 * @Description: 车牌正确返回true,否则返回false
	 * @author weifa.Yang 
	 * @date 2016-11-28 下午03:47:01
	 * @param vehicleNumber
	 * @return boolean
	 *
	 */
	public static boolean isVehicleNumber(String vehicleNumber) {
		String regex = "";
		if(vehicleNumber.length() == 7){
			regex = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";
		}else if(vehicleNumber.length() == 8){
			//新能源
			regex = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{5}[A-Z0-9]{1}$";
		}else{
			return false;
		}
		//创建 Pattern 对象
		Pattern pattern = Pattern.compile(regex);
		//创建 matcher 对象
		Matcher matcher = pattern.matcher(vehicleNumber);

		return matcher.matches();
	}
	public static void main(String[] args) {
		System.out.println(isVehicleNumber("桂BA1234"));
	}
	
	 /**
	  * 将文件转成base64 字符串
	  * @param path文件路径
	  * @return  *String
	  * @throws Exception
	  */
	 public static String encodeBase64File(String path) throws Exception {
		 File file = new File(path);;
		 FileInputStream inputFile = new FileInputStream(file);
		 byte[] buffer = new byte[(int) file.length()];
		 inputFile.read(buffer);
		 inputFile.close();
		 return new BASE64Encoder().encode(buffer);
	 }
	 
	 
	 //验证车牌号码：支持6位车牌
	 public static boolean isLicensePlate(String licensePlate) {
			String regex = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[警京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{0,1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}[A-Z0-9]{1}$|^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[警京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{0,1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(licensePlate);
			return matcher.matches();
		}
	 /**
		 * 去掉特殊符号 例如表情符号
		 * @author li.quan
		 * @date 2017-5-11 下午04:47:07
		 * @param source
		 * @return    
		 * @return String
		 */
		public static String filterEmoji(String source) { 
	        if(source != null)
	        {
	            Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
	            Matcher emojiMatcher = emoji.matcher(source);
	            if ( emojiMatcher.find())
	            {
	                source = emojiMatcher.replaceAll("");
	                return source ;
	            }
	        return source;
	       }
	       return source; 
	    }
		
		public static String objToStr(Object o){
			if(o == null){
				return "";
			}
			return String.valueOf(o);
		}
		
		/**
		 * 手机号验证
		 * @Title: isMobile
		 * @Description: 
		 * @author weifa.Yang 
		 * @date 2016-3-24 上午11:45:29
		 * @param mobile
		 * @return boolean  验证通过返回true
		 */
		public static boolean isMobile(String mobile) { 
			Pattern p = null;
			Matcher m = null;
			boolean b = false; 
			p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
			m = p.matcher(mobile);
			b = m.matches(); 
			return b;
		}

	/**
	 * 姓名验证
	 * @Title: isMobile
	 * @Description:
	 * @author mengjianliang
	 * @date 2017-9-11 上午11:45:29
	 * @param mobile
	 * @return boolean  验证通过返回true
	 */
	public static boolean isName(String name) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[\\p{L} .'-]+$"); // 验证姓名
		m = p.matcher(name);
		b = m.matches();
		return b;
	}
}