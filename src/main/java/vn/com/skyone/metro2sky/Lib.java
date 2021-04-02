package vn.com.skyone.metro2sky;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.rtf.RTFEditorKit;

public class Lib {

    public Lib() {

    }

    private static final long ONE_DAY_MILLI_SEC = 86400000;
    private static final long ONE_HOUR_MILLI_SEC = 3600000;
    private static final long ONE_MINUTE_MILLI_SEC = 60000;
    private static final String EMPTY = "";
    private static final String PATTERN_DATE = "dd/MM/yyyy";

    public final static String tagStart = "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)\\>";
    public final static String tagEnd = "\\</\\w+\\>";
    public final static String tagSelfClosing = "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)/\\>";
    public final static String htmlEntity = "&[a-zA-Z][a-zA-Z0-9]+;";
    public final static Pattern htmlPattern = Pattern.compile(
            "(" + tagStart + ".*" + tagEnd + ")|(" + tagSelfClosing + ")|(" + htmlEntity + ")", Pattern.DOTALL);

    /**
     * Convert to vietnam no sign
     * 
     * @param org
     * @return
     */
    public static String viLatin(String org) {
        if (org == null) {
            return "";
        }

        char arrChar[] = org.toCharArray();
        char result[] = new char[arrChar.length];
        for (int i = 0; i < arrChar.length; i++) {
            switch (arrChar[i]) {
            case '\u00E1':
            case '\u00E0':
            case '\u1EA3':
            case '\u00E3':
            case '\u1EA1':
            case '\u0103':
            case '\u1EAF':
            case '\u1EB1':
            case '\u1EB3':
            case '\u1EB5':
            case '\u1EB7':
            case '\u00E2':
            case '\u1EA5':
            case '\u1EA7':
            case '\u1EA9':
            case '\u1EAB':
            case '\u1EAD':
            case '\u0203':
            case '\u01CE': {
                result[i] = 'a';
                break;
            }
            case '\u00E9':
            case '\u00E8':
            case '\u1EBB':
            case '\u1EBD':
            case '\u1EB9':
            case '\u00EA':
            case '\u1EBF':
            case '\u1EC1':
            case '\u1EC3':
            case '\u1EC5':
            case '\u1EC7':
            case '\u0207': {
                result[i] = 'e';
                break;
            }
            case '\u00ED':
            case '\u00EC':
            case '\u1EC9':
            case '\u0129':
            case '\u1ECB': {
                result[i] = 'i';
                break;
            }
            case '\u00F3':
            case '\u00F2':
            case '\u1ECF':
            case '\u00F5':
            case '\u1ECD':
            case '\u00F4':
            case '\u1ED1':
            case '\u1ED3':
            case '\u1ED5':
            case '\u1ED7':
            case '\u1ED9':
            case '\u01A1':
            case '\u1EDB':
            case '\u1EDD':
            case '\u1EDF':
            case '\u1EE1':
            case '\u1EE3':
            case '\u020F': {
                result[i] = 'o';
                break;
            }
            case '\u00FA':
            case '\u00F9':
            case '\u1EE7':
            case '\u0169':
            case '\u1EE5':
            case '\u01B0':
            case '\u1EE9':
            case '\u1EEB':
            case '\u1EED':
            case '\u1EEF':
            case '\u1EF1': {
                result[i] = 'u';
                break;
            }
            case '\u00FD':
            case '\u1EF3':
            case '\u1EF7':
            case '\u1EF9':
            case '\u1EF5': {
                result[i] = 'y';
                break;
            }
            case '\u0111': {
                result[i] = 'd';
                break;
            }
            case '\u00C1':
            case '\u00C0':
            case '\u1EA2':
            case '\u00C3':
            case '\u1EA0':
            case '\u0102':
            case '\u1EAE':
            case '\u1EB0':
            case '\u1EB2':
            case '\u1EB4':
            case '\u1EB6':
            case '\u00C2':
            case '\u1EA4':
            case '\u1EA6':
            case '\u1EA8':
            case '\u1EAA':
            case '\u1EAC':
            case '\u0202':
            case '\u01CD': {
                result[i] = 'A';
                break;
            }
            case '\u00C9':
            case '\u00C8':
            case '\u1EBA':
            case '\u1EBC':
            case '\u1EB8':
            case '\u00CA':
            case '\u1EBE':
            case '\u1EC0':
            case '\u1EC2':
            case '\u1EC4':
            case '\u1EC6':
            case '\u0206': {
                result[i] = 'E';
                break;
            }
            case '\u00CD':
            case '\u00CC':
            case '\u1EC8':
            case '\u0128':
            case '\u1ECA': {
                result[i] = 'I';
                break;
            }
            case '\u00D3':
            case '\u00D2':
            case '\u1ECE':
            case '\u00D5':
            case '\u1ECC':
            case '\u00D4':
            case '\u1ED0':
            case '\u1ED2':
            case '\u1ED4':
            case '\u1ED6':
            case '\u1ED8':
            case '\u01A0':
            case '\u1EDA':
            case '\u1EDC':
            case '\u1EDE':
            case '\u1EE0':
            case '\u1EE2':
            case '\u020E': {
                result[i] = 'O';
                break;
            }
            case '\u00DA':
            case '\u00D9':
            case '\u1EE6':
            case '\u0168':
            case '\u1EE4':
            case '\u01AF':
            case '\u1EE8':
            case '\u1EEA':
            case '\u1EEC':
            case '\u1EEE':
            case '\u1EF0': {
                result[i] = 'U';
                break;
            }

            case '\u00DD':
            case '\u1EF2':
            case '\u1EF6':
            case '\u1EF8':
            case '\u1EF4': {
                result[i] = 'Y';
                break;
            }
            case '\u0110':
            case '\u00D0':
            case '\u0089': {
                result[i] = 'D';
                break;
            }
            default:
                result[i] = arrChar[i];
            }
        }

        return new String(result);
    }

    public static String removeAccent(String str) {
        if (str == null) {
            return "";
        }
        str = str.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        str = str.replaceAll("[èéẹẻẽêềếệểễ]", "e");
        str = str.replaceAll("[ìíịỉĩ]", "i");
        str = str.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        str = str.replaceAll("[ùúụủũưừứựửữ]", "u");
        str = str.replaceAll("[ỳýỵỷỹ]", "y");
        str = str.replaceAll("[đ]", "d");
        str = str.replaceAll("[ÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴ]", "A");
        str = str.replaceAll("[ÈÉẸẺẼÊỀẾỆỂỄ]", "E");
        str = str.replaceAll("[ÌÍỊỈĨ]", "I");
        str = str.replaceAll("[ÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠ]", "O");
        str = str.replaceAll("[ÙÚỤỦŨƯỪỨỰỬỮ]", "U");
        str = str.replaceAll("[ỲÝỴỶỸ]", "Y");
        str = str.replaceAll("[ĐÐ]", "D");
        // Combining Diacritical Marks
        // huyền, sắc, hỏi, ngã, nặng
        str = str.replaceAll("[\u0300\u0301\u0303\u0309\u0323]", "");
        // mũ â (ê), mũ ă, mũ ơ (ư)
        str = str.replaceAll("[\u02C6\u0302\u0306\u031B]", "");
        return str;
    }

    public static String getName(String value, Const.TYPE_NAME type) {
        if (value == null) {
            return "";
        }
        String[] arr = value.replace("\\s+", " ").split(" ");
        switch (type) {
        case FIRST_NAME:
            String first = "";
            if (arr.length > 1) {
                first = arr[arr.length - 1];
            }
            return first;
        case MIDDLE_NAME: {
            String middle = "";
            if (arr.length > 2) {
                for (int i = 1; i < arr.length - 1; i++) {
                    if (i == 1) {
                        middle += arr[i].trim();
                    } else {
                        middle += " " + arr[i].trim();
                    }
                }
            }
            return middle;
        }
        case LAST_NAME:
            return arr[0];
        case ACCOUNT:
            String last = "";
            for (int i = 0; i < arr.length - 1; i++) {
                if (arr[i].length() > 1) {
                    last += arr[i].substring(0, 1).trim();
                }
            }
            return viLatin(arr[arr.length - 1].trim() + last);
        case CODE:
            String code = "";
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].length() > 1) {
                    code += arr[i].substring(0, 1).trim();
                }
            }
            return code;
        default:
            return "";
        }

    }

    public static String elapsedTime(Long begin, Long end) {
        String result = "";
        long different = end - begin;

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;
        different = different % secondsInMilli;

        if (elapsedDays > 0) {
            result += elapsedDays + " d ";
        }

        if (elapsedHours > 0) {
            result += elapsedHours + " h ";
        }

        if (elapsedMinutes > 0) {
            result += elapsedMinutes + " m ";
        }

        if (elapsedSeconds > 0) {
            result += elapsedSeconds + " s ";
        }

        if (different > 0) {
            result += different + " ms ";
        }

        return result;
    }

    public static long diffDay(Long date1, Long date2) {
        if (null != date1 && null != date2) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTimeInMillis(date1);
            cal1.set(Calendar.HOUR_OF_DAY, 0);
            cal1.set(Calendar.MINUTE, 0);
            cal1.set(Calendar.SECOND, 0);
            cal1.set(Calendar.MILLISECOND, 0);

            Calendar cal2 = Calendar.getInstance();
            cal2.setTimeInMillis(date2);
            cal2.set(Calendar.HOUR_OF_DAY, 0);
            cal2.set(Calendar.MINUTE, 0);
            cal2.set(Calendar.SECOND, 0);
            cal2.set(Calendar.MILLISECOND, 0);

            return Math.abs((cal1.getTimeInMillis() - cal2.getTimeInMillis()) / ONE_DAY_MILLI_SEC);
        }
        return -1;
    }

    public static Long getStartDate(Long date) {
        if (date == null) {
            return date;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);

        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));

        return cal.getTimeInMillis();
    }

    public static Date getStartDate(Date date) {
        if (date == null) {
            return date;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));

        return cal.getTime();
    }

    public static Long getEndDate(Long date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTimeInMillis(date);
        }

        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));

        return cal.getTimeInMillis();
    }

    public static Date getEndDate(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }

        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));

        return cal.getTime();
    }

    public static boolean isEmpty(String src) {
        if (null == src || src.trim().isEmpty()) {
            return true;
        }
        return false;
    }

    public static byte[] createChecksum(byte[] data) throws Exception {
        MessageDigest complete = MessageDigest.getInstance("MD5");
        complete.update(data);
        return complete.digest();
    }

    // see this How-to for a faster way to convert
    // a byte array to a HEX string
    public static String getMD5Checksum(byte[] data) throws Exception {
        byte[] b = createChecksum(data);
        String result = "";

        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public static String bytesToSize(long bytes) {
        int k = 1024;
        String[] sizes = { "Bytes", "KB", "MB", "GB", "TB" };
        int i = 0;
        double b = bytes * 1.0;
        while (b >= k) {
            b /= k;
            i++;
        }
        DecimalFormat df = new DecimalFormat("##.00");
        return df.format(b) + " " + sizes[i];
    }

    public static String dt2Str(Long dt, String pattern) {
        if (dt != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(new Date(dt));
        } else {
            return EMPTY;
        }
    }

    public static String dt2Str(Date dt, String pattern) {
        if (dt != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(dt);
        } else {
            return EMPTY;
        }
    }

    public static String genCode(String code, Integer index) {
        if (!isEmpty(code) && index > 0) {
            DecimalFormat dfIndex = new DecimalFormat("00");
            return code + "-" + dfIndex.format(index);
        }
        return "";
    }

    public static String toString(Object value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value.toString();
    }

    public static Long toLong(Object value, long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        Long ret = defaultValue;
        try {
            if (value instanceof Number) {
                ret = ((Number) value).longValue();
            } else {
                ret = value.toString().trim().isEmpty() ? defaultValue : Long.valueOf(value.toString().trim());
            }
        } catch (Exception e) {
            ret = defaultValue;
        }
        return ret;
    }

    public static Long getMinDate() {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, cal.getActualMinimum(Calendar.YEAR));
        cal.set(Calendar.MONTH, cal.getActualMinimum(Calendar.MONTH));
        cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));

        return cal.getTimeInMillis();
    }

    public static Long getMaxDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 9999);
        cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));

        return cal.getTimeInMillis();
    }

    public static Long str2Dt(String ymd, String pattern) {
        if (ymd == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date ret = sdf.parse(ymd);
            return ret.getTime();
        } catch (ParseException e) {
            return null;
        }
    }

    public static Long toDate(Object value) {
        return str2Dt(value.toString(), "yyyyMMdd");
    }

    public static Date toDate(Long millis) {
        if (millis == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return cal.getTime();
    }

    public static Date getDate(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getTime();
    }

    public static Long dateToLong(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }

        return cal.getTimeInMillis();
    }

    public static String getPassword(String password) {
        try {
            password = "SkyEMR@062016" + password;
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String rtfToHtml(Reader rtf) throws IOException {
        JEditorPane p = new JEditorPane();
        p.setContentType("text/rtf");
        EditorKit kitRtf = p.getEditorKitForContentType("text/rtf");
        try {
            kitRtf.read(rtf, p.getDocument(), 0);
            kitRtf = null;
            EditorKit kitHtml = p.getEditorKitForContentType("text/html");
            Writer writer = new StringWriter();
            kitHtml.write(writer, p.getDocument(), 0, p.getDocument().getLength());
            return writer.toString();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertToRTF(String htmlStr) {
        OutputStream os = new ByteArrayOutputStream();
        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        RTFEditorKit rtfEditorKit = new RTFEditorKit();
        String rtfStr = null;
        htmlStr = htmlStr.replaceAll("<br.*?>", "#NEW_LINE#");
        htmlStr = htmlStr.replaceAll("</p>", "#NEW_LINE#");
        htmlStr = htmlStr.replaceAll("<p.*?>", "");
        InputStream is = new ByteArrayInputStream(htmlStr.getBytes());
        try {
            Document doc = htmlEditorKit.createDefaultDocument();
            htmlEditorKit.read(is, doc, 0);
            rtfEditorKit.write(os, doc, 0, doc.getLength());
            rtfStr = os.toString();
            rtfStr = rtfStr.replaceAll("#NEW_LINE#", "\\\\par ");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return rtfStr;
    }

    public static String fileToString(File file) {
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {
                sb.append(st);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        int strLen = str.length();
        int[] newCodePoints = new int[strLen];
        int outOffset = 0;

        boolean capitalizeNext = true;
        str = str.toLowerCase();
        for (int index = 0; index < strLen;) {
            final int codePoint = str.codePointAt(index);
            if (Character.isWhitespace(codePoint)) {
                capitalizeNext = true;
                newCodePoints[outOffset++] = codePoint;
                index += Character.charCount(codePoint);
            } else if (capitalizeNext) {
                int titleCaseCodePoint = Character.toTitleCase(codePoint);
                newCodePoints[outOffset++] = titleCaseCodePoint;
                index += Character.charCount(titleCaseCodePoint);
                capitalizeNext = false;
            } else {
                newCodePoints[outOffset++] = codePoint;
                index += Character.charCount(codePoint);
            }
        }
        return new String(newCodePoints, 0, outOffset);
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String num2Str(Number num, String pattern) {
        String str = EMPTY;
        if (num != null) {
            DecimalFormat df = new DecimalFormat(pattern);
            str = df.format(num);
        }
        return str;
    }

    public static String increaseCode(String code) {
        if (code.length() > 2) {
            String suffix = code.substring(3, code.length());
            if (Lib.isNumeric(suffix)) {
                return code.substring(0, 3) + Lib.num2Str(Integer.valueOf(suffix) + 1, "000");
            }
        }
        return "";
    }

    public static String dt2Str(Long dt, TimeZone zone, Locale locale) {
        if (dt != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATE);
            return sdf.format(new Date(dt));
        } else {
            return EMPTY;
        }
    }

    public static boolean checkAccent(String text) {
        return Pattern.matches("\\p{InCombiningDiacriticalMarks}+", Normalizer.normalize(text, Form.NFD));
    }

    public static String removeAccents(String text) {
        return text == null ? null
                : Normalizer.normalize(text, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public static boolean isHtml(String s) {
        boolean ret = false;
        if (s != null) {
            ret = htmlPattern.matcher(s).find();
        }
        return ret;
    }

    public static String markupHtml(String value) {
        if (value == null || EMPTY.equals(value)) {
            return value;
        }

        if (isHtml(value)) {
            value = value.replace("<strong>", "<strong><b>");
            value = value.replace("</strong>", "</b></strong>");
            value = value.replace("<em>", "<em><i>");
            value = value.replace("</em>", "</i></em>");
            return value;
        } else {
            value = value.replaceAll("  ", "&nbsp; ");
            StringBuilder result = new StringBuilder();
            String[] lines = value.split("[\\n\\r]+");

            if (lines != null && lines.length > 0) {
                result.append(lines[0]);

                for (int i = 1; i < lines.length; i++) {
                    result.append("<br/>");
                    result.append(lines[i]);
                }
            }
            return result.toString();
        }
    }

    public static String nIb(String value) {
        if (value == null) {
            return "";
        }
        return value;
    }

    public static InputStream cropImage(InputStream src, String type, double perT, double perL, double perR,
            double perB) {
        if (perT == 0 && perL == 0 && perR == 0 && perB == 0) {
            return src;
        }
        BufferedImage image = null;
        InputStream is = null;
        int t = 0, l = 0, r = 0, b = 0;
        int x = 0, y = 0, w = 0, h = 0;
        try {
            image = ImageIO.read(src);
            int iw = image.getWidth();
            int ih = image.getHeight();
            t = (int) Math.round(perT * ih / 100);
            l = (int) Math.round(perL * iw / 100);
            r = (int) Math.round(perR * iw / 100);
            b = (int) Math.round(perB * ih / 100);
            x = l > iw ? iw : l;
            y = t > ih ? ih : t;
            w = r > iw ? iw : iw - r - l;
            h = b > ih ? ih : ih - b - t;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image.getSubimage(x, y, w, h), type, os);
            is = new ByteArrayInputStream(os.toByteArray());
        } catch (Exception e) {
            is = null;
        }
        return is;
    }

    public static Integer toInteger(Object value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        Integer ret = defaultValue;
        try {
            if (value instanceof Number) {
                ret = ((Number) value).intValue();
            } else {
                ret = value.toString().trim().isEmpty() ? defaultValue : Integer.valueOf(value.toString().trim());
            }
        } catch (Exception e) {
            ret = defaultValue;
        }
        return ret;
    }

    public static byte[] readFile(String filePath) throws IOException {
        return readFile(new File(filePath));
    }

    public static byte[] readFile(File file) throws IOException {
        if (file == null || !file.exists()) {
            return null;
        }

        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if (ous != null) {
                    ous.close();
                }
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
            }
        }

        return ous.toByteArray();
    }

    public static void writeFile(File file, byte[] data) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data);
        fos.close();
    }

    /**
     * Copy file
     * 
     * @param src
     * @param dest
     */
    public static boolean copyFile(File source, File target) {
        boolean ret = true;
        try {
            Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            ret = false;
        }
        return ret;
    }

    /**
     * Copy file
     * 
     * @param src
     * @param dest
     */
    public static boolean copyFile(String source, String target) {
        return copyFile(new File(source), new File(target));
    }

    /**
     * Move file
     * 
     * @param src
     * @param dest
     */
    public static boolean moveFile(File source, File target) {
        try {
            File parent = target.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            Files.move(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Move file
     * 
     * @param src
     * @param dest
     */
    public static boolean moveFile(String source, String target) {
        return moveFile(new File(source), new File(target));
    }

    /**
     * Delete file
     * 
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        try {
            Files.deleteIfExists(file.toPath());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Delete file
     * 
     * @param file
     * @return
     */
    public static boolean deleteFile(String file) {
        return deleteFile(new File(file));
    }

    /**
     * Delete folder
     * 
     * @param folder
     * @return
     */
    public static boolean deleteFolder(File folder) {
        boolean deleted = false;
        if (folder.isDirectory()) {
            // directory is empty, then delete it
            if (folder.list().length == 0) {
                deleted = folder.delete();
            } else {
                // list all the directory contents
                String files[] = folder.list();
                for (String temp : files) {
                    // construct the file structure
                    File fileDelete = new File(folder, temp);
                    // recursive delete
                    deleted = deleteFolder(fileDelete);
                }

                // check the directory again, if empty then delete it
                if (folder.list().length == 0) {
                    deleted = folder.delete();
                }
            }
        } else {
            // if file, then delete it
            deleted = folder.delete();
        }

        return deleted;
    }

    /**
     * Delete folder
     * 
     * @param folder
     * @return
     */
    public static boolean deleteFolder(String folder) {
        return deleteFolder(new File(folder));
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024]; // Adjust if you want
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    /**
     * Get extension file
     * 
     * @param fileName
     * @return
     */
    public static String getExtFromName(String fileName) {
        if (Lib.isEmpty(fileName)) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf(Const.DOT));
    }

    public static String getExtWithoutDot(String fileName) {
        if (Lib.isEmpty(fileName)) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf(Const.DOT) + 1);
    }

    /**
     * Get image type from file name
     * 
     * @param fileName
     * @return
     */
    public static String getImageType(String fileName) {
        String type = fileName.substring(fileName.lastIndexOf(Const.DOT) + 1).toLowerCase();
        if ("jpeg".equals(type) || "jpg".equals(type) || "jpe".equals(type)) {
            return "jpeg";
        } else if ("gif".equals(type)) {
            return "gif";
        } else if ("svg".equals(type)) {
            return "svg";
        } else if ("svgz".equals(type)) {
            return "svgz";
        } else if ("png".equals(type)) {
            return "png";
        } else if ("tif".equals(type) || "tiff".equals(type)) {
            return "tiff";
        } else if ("bmp".equals(type)) {
            return "bmp";
        } else if ("pct".equals(type) || "pic".equals(type) || "pict".equals(type)) {
            return "pict";
        } else if ("cgm".equals(type)) {
            return "cgm";
        } else if ("rgb".equals(type)) {
            return "x-rgb";
        } else if ("ico".equals(type)) {
            return "x-icon";
        } else if ("ief".equals(type)) {
            return "ief";
        } else if ("jp2".equals(type)) {
            return "jp2";
        } else if ("djv".equals(type) || "djvu".equals(type)) {
            return "vnd.djvu";
        } else if ("mac".equals(type) || "pnt".equals(type) || "pntg".equals(type)) {
            return "x-macpaint";
        } else if ("pbm".equals(type)) {
            return "x-portable-bitmap";
        } else if ("pgm".equals(type)) {
            return "x-portable-graymap";
        } else if ("pnm".equals(type)) {
            return "x-portable-anymap";
        } else if ("ppm".equals(type)) {
            return "x-portable-pixmap";
        } else if ("qti".equals(type) || "qtif".equals(type)) {
            return "x-quicktime";
        } else if ("ras".equals(type)) {
            return "x-cmu-raster";
        } else if ("wbmp".equals(type)) {
            return "vnd.wap.wbmp";
        } else if ("xbm".equals(type) || "xpm".equals(type)) {
            return "x-xbitmap";
        } else if ("xwd".equals(type)) {
            return "x-xwindowdump";
        } else if ("pdf".equals(type)) {
            return "pdf";
        } else if (Const.FILE_TYPE_DOCX.equals(type)) {
            return Const.FILE_TYPE_DOCX;
        } else if (Const.FILE_TYPE_XLSX.equals(type)) {
            return Const.FILE_TYPE_XLSX;
        } else {
            return "jpeg";
        }
    }

}
