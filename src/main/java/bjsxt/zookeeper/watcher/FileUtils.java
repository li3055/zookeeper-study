package bjsxt.zookeeper.watcher;

import java.awt.Container;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.imageio.IIOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SystemUtils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.lang.time.DateUtils;

/**
 * @version <br />
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
    public static final String RIGHT_SPRIT = "\\";

    public static final String LEFT_SPRIT = "/";

    public static final String LEFT_DOUBLE_SPRIT = "//";

    public static final String SEPARATOR = LEFT_SPRIT;

    public static final int THUMBNAIL_HEIGHT = 200;


    /**
     */
    public static String suitName(String fileName) {
        if (StringUtils.isEmpty(fileName))
            return StringUtils.EMPTY;

        if (!StringUtils.contains(fileName, RIGHT_SPRIT) && !StringUtils.contains(fileName, LEFT_SPRIT))
            return fileName;

        return replace(fileName);
    }

    /**
     */
    public static String concatPath(String... dirOrfileName) {
        if (ArrayUtils.isEmpty(dirOrfileName))
            return StringUtils.EMPTY;

        StringBuffer stringBuffer = new StringBuffer();
        for (String s : dirOrfileName) {
            stringBuffer.append(s);
            stringBuffer.append(LEFT_SPRIT);
        }

        return replace(stringBuffer.toString());
    }

    /**
     */
    public static String getDir(String fileName) {
        if (!StringUtils.contains(fileName, RIGHT_SPRIT) && !StringUtils.contains(fileName, LEFT_SPRIT))
            return fileName;

        String result = fileName.replace(RIGHT_SPRIT, LEFT_SPRIT);
        int index = result.lastIndexOf(LEFT_SPRIT);

        return result.substring(0, index);
    }

    /**
     */
    public static String getSimpleFileName(String fileName) {
        return getSimpleFileName(fileName, true);
    }

    /**
     * @param fileName
     * @param includeSuffix
     */
    public static String getSimpleFileName(String fileName, boolean includeSuffix) {
        if (StringUtils.isEmpty(fileName))
            return StringUtils.EMPTY;

        String result = fileName.replace(RIGHT_SPRIT, LEFT_SPRIT);
        int index = result.lastIndexOf(LEFT_SPRIT);
        result = result.substring(index + 1);

        if (includeSuffix)
            return result;

        index = result.lastIndexOf(".");
        return (index == -1) ? result : result.substring(0, index);
    }

    /**
     * @param fileName
     */
    public static String getFileSuffix(String fileName) {
        if (StringUtils.isEmpty(fileName))
            return StringUtils.EMPTY;

        String result = fileName.replace(RIGHT_SPRIT, LEFT_SPRIT);
        int index = result.lastIndexOf(LEFT_SPRIT);
        result = result.substring(index + 1);

        index = result.lastIndexOf(".");
        return (index == -1) ? result : result.substring(index + 1);
    }

    /**
     */
    public static void mkdirs(String path) {
        if (StringUtils.isEmpty(path))
            return;

        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
    }


    /**
     */
    public static String cutFileName(String fileName, String... character) {
        if (StringUtils.isEmpty(fileName))
            return StringUtils.EMPTY;

        if (ArrayUtils.isEmpty(character))
            return fileName;

        for (String s : character) {
            if (StringUtils.isEmpty(s))
                continue;

            if (!StringUtils.contains(fileName, s))
                continue;

            int index = fileName.indexOf(s);
            return fileName.substring(0, index);
        }

        return getSimpleFileName(fileName, false);
    }

    /**
     */
    public static String randomFileName() {
        return String.format("%d_%s", System.currentTimeMillis(), UUID.randomUUID().toString());
    }

    /**
     */
    public static String addRandomToFileName(String fileName, String random, String split) {
        String simpleFileName = getSimpleFileName(fileName, false);
        String suffix = getFileSuffix(fileName);

        return String.format("%s%s%s.%s", simpleFileName, split, random, suffix);
    }


    /**
     */
    public static List<String> childDirList(String dir) {
        File file = new File(dir);
        if (!file.exists() || !file.isDirectory())
            return null;

        File[] fileArray = file.listFiles();
        if (ArrayUtils.isEmpty(fileArray))
            return null;

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < fileArray.length; i++) {
            if (fileArray[i].isDirectory())
                list.add(fileArray[i].getName());
        }

        return list;
    }

    /**
     */
    public static List<String> childFileList(String dir) {
        File file = new File(dir);
        if (!file.exists() || !file.isDirectory())
            return null;

        File[] fileArray = file.listFiles();
        if (ArrayUtils.isEmpty(fileArray))
            return null;

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < fileArray.length; i++) {
            if (fileArray[i].isFile())
                list.add(fileArray[i].getName());
        }

        return list;
    }

    /**
     */
    public static List<String> readToList(String fileName) {
        List<String> result = null;
        Charset charset = new CharsetUtils().getCharset(fileName);
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            return result;
        }

        return readToList(inputStream, charset);
    }

    /**
     */
    public static List<String> readToList(InputStream inputStream, Charset charset) {
        List<String> result = new ArrayList<String>();

        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        String line = null;

        try {
            if (StringUtils.equals(charset.toString(), CharsetUtils.WINDOWS_1252))
                inputStreamReader = new InputStreamReader(inputStream, CharsetUtils.UNICODE);
            else
                inputStreamReader = new InputStreamReader(inputStream, charset);

            bufferedReader = new BufferedReader(inputStreamReader);

            result = new ArrayList<String>();
            while ((line = bufferedReader.readLine()) != null)
                result.add(line);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(bufferedReader);
            IOUtils.closeQuietly(inputStreamReader);
        }

        if (StringUtils.equalsAny(charset.toString(), CharsetUtils.UTF_8, CharsetUtils.UTF_16))
            removeBom(result);

        return result;
    }

    private static void removeBom(List<String> list) {
        byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        String line = get(list, 0);
        if (StringUtils.isEmpty(line))
            return;

        int index = line.indexOf(new String(bom));
        if (index != -1) {
            list.remove(0);
            list.add(0, line.substring(index + 1, line.length()));
        }
    }

    public static <T> T get(List<T> list, int index) {
        if (list == null || list.size() == 0 || index >= list.size())
            return null;

        return list.get(index);
    }

    /**
     */
    public static boolean writeLine(String fileName, String line) {
        List<String> list = new ArrayList();
        list.add(line);
        return writeLine(fileName, list);
    }

    /**
     */
    public static boolean writeLine(String fileName, List<String> list) {
        boolean result = false;
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            fileOutputStream = new FileOutputStream(fileName);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, CharsetUtils.UTF_8);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            for (String s : list) {
                bufferedWriter.write(s);
                bufferedWriter.newLine();
            }

            bufferedWriter.flush();
        } catch (Exception e) {
            return result;
        } finally {
            IOUtils.closeQuietly(bufferedWriter);
            IOUtils.closeQuietly(outputStreamWriter);
            IOUtils.closeQuietly(fileOutputStream);
        }

        return true;
    }

    /**
     */
    private static String replace(String param) {
        String result = new String(param);

        if (StringUtils.contains(result, RIGHT_SPRIT))
            result = result.replace(RIGHT_SPRIT, LEFT_SPRIT);

        while (StringUtils.contains(result, LEFT_DOUBLE_SPRIT))
            result = result.replace(LEFT_DOUBLE_SPRIT, LEFT_SPRIT);

        if (result.endsWith(LEFT_SPRIT))
            result = result.substring(0, result.length() - 1);

        return result;
    }


    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }


    public static void main(String[] args) {


        File dir = new File("D:/ProfitTrailer - test/application.properties");

        List<String> lsit = readToList("D:/ProfitTrailer - test/trading/PAIRS.properties");

        for (String s : lsit) {
            System.out.println(s);
        }
    }
}