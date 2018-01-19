package bjsxt.zookeeper.watcher;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;
import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.mozilla.intl.chardet.nsPSMDetector;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 项目名称：RenRenERP <br />
 * 类名称：CharsetUtils <br />
 * 创建人：Administrator <br />
 * 备注： <br />
 * 
 * @version <br />
 */
public class CharsetUtils extends org.apache.http.util.CharsetUtils
{
	public static final String UTF_8 = "UTF-8";
	public static final String UTF_16 = "UTF-16BE";

	public static final String GBK = "GBK";
	public static final String GB2312 = "GB2312";

	public static final String ISO8859_1 = "ISO8859_1";

	public static final String WINDOWS_1252 = "windows-1252";
	public static final String UNICODE = "Unicode";

	private Charset charset = Charset.defaultCharset();
	private boolean found = false;

	private static boolean debug = false;

	/**
	 * 默认的输入流编码
	 */
	public static Charset defaultOutputCharset()
	{
		if (SystemUtils.IS_OS_WINDOWS)
			return debug ? Charset.forName(UTF_8) : Charset.forName(GB2312);
		else
			return Charset.forName(UTF_8);
	}

	/**
	 * 根据文件名获取文件的编码方式
	 */
	public Charset getCharset(String fileName)
	{
		InputStream inputStream = null;
		BufferedInputStream bufferedInputStream = null;

		try
		{
			inputStream = new FileInputStream(fileName);
			bufferedInputStream = new BufferedInputStream(inputStream);

			byte[] byteArray = new byte[1024];
			bufferedInputStream.read(byteArray);

			nsDetector detector = new nsDetector(nsPSMDetector.ALL); // 语言线索常量

			// Set an observer...
			// The Notify() will be called when a matching charset is found.
			detector.Init(new nsICharsetDetectionObserver()
			{
				public void Notify(String charsetName)
				{
					charset = Charset.forName(charsetName);
					found = true;
				}
			});

			detector.DoIt(byteArray, byteArray.length, false); // 测试
			detector.DataEnd(); // 测试结束！

			if (!found)
			{
				String probableCharsets[] = detector.getProbableCharsets();
				charset = Charset.forName(probableCharsets[0]);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			IOUtils.closeQuietly(bufferedInputStream);
			IOUtils.closeQuietly(inputStream);
		}

		return charset;
	}
}
