package vn.com.skyone.app2sky;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.rtf.RTFEditorKit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class Lib {

    public Lib() {

    }

    private static final long ONE_DAY_MILLI_SEC = 86400000;
    private static final long ONE_HOUR_MILLI_SEC = 3600000;
    private static final long ONE_MINUTE_MILLI_SEC = 60000;
    private static final String EMPTY = "";

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
        // new StringReader(text)
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

    public static String rtfToHtmlPlus(Reader rtf) throws IOException {
        // new StringReader(text)
        JEditorPane p = new JEditorPane();
        p.setContentType("text/rtf");
        EditorKit kitRtf = p.getEditorKitForContentType("text/rtf");
        try {
            kitRtf.read(rtf, p.getDocument(), 0);
            kitRtf = null;
            EditorKit kitHtml = p.getEditorKitForContentType("text/html");
            Writer writer = new StringWriter();
            kitHtml.write(writer, p.getDocument(), 0, p.getDocument().getLength());
            return writer.toString().replace("Microsoft Sans Serif", "Arial");
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getBody(String html) throws IOException {
        org.jsoup.nodes.Document document = Jsoup.parse(html);
        Element boby = document.body();
        if (boby != null) {
            return boby.html();
        }
        return null;
    }

    public static String parseBodyToHtml(String body) throws IOException {
        org.jsoup.nodes.Document document = Jsoup.parseBodyFragment(body);
        if (document != null) {
            return document.html();
        }
        return null;
    }

    public static String toPlainText(Reader rtf) throws IOException {
        RTFEditorKit rtfParser = new RTFEditorKit();
        Document document = rtfParser.createDefaultDocument();
        try {
            rtfParser.read(rtf, document, 0);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        try {
            return document.getText(0, document.getLength());
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

    public static void writeFileUTF8(File file, String content) throws IOException {
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")));
        try {
            out.write(content);
        } finally {
            out.close();
        }
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

    public static Date str2Dt(String str) {
        Date ret = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            ret = sdf.parse(str);
        } catch (ParseException e) {
            sdf = new SimpleDateFormat("yyyy");
            try {
                ret = sdf.parse(str);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        return ret;
    }

    public static Long getMilliSecond(Date dt) {
        if (dt == null) {
            return 0L;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.getTimeInMillis();
    }

    public static Integer getSex(String str) {
        if ("NAM".equals(str.toUpperCase())) {
            return 1;
        } else if ("NỮ".equals(str.toUpperCase())) {
            return 2;
        }
        return 3;
    }

    public static String removeAccent(String str) {
        return removeAccent(str, Locale.getDefault());
    }

    public static String removeAccent(String str, Locale locale) {
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

    public static String searchText(String code, String barCode, String identityNumber, String birthYear, String name,
            String phoneMobile, String phoneHome, String email, String taxCode) {
        code = code == null ? "" : code.trim().replace("\\s+", " ");
        barCode = barCode == null ? "" : barCode.trim().replace("\\s+", " ");
        phoneMobile = phoneMobile == null ? "" : phoneMobile.trim().replace("\\s+", " ");
        phoneHome = phoneHome == null ? "" : phoneHome.trim().replace("\\s+", " ");
        email = email == null ? "" : email.trim().replace("\\s+", " ");
        taxCode = taxCode == null ? "" : taxCode.trim().replace("\\s+", " ");
        identityNumber = identityNumber == null ? "" : identityNumber.trim().replace("\\s+", " ");

        StringBuffer sb = new StringBuffer(code);
        sb.append(" ");
        sb.append(barCode);
        sb.append(" ");
        sb.append(taxCode);
        sb.append(" ");
        sb.append(identityNumber);
        sb.append(" ");
        sb.append(birthYear);
        sb.append(" ");
        sb.append(Lib.removeAccent(name));
        sb.append(" ");
        sb.append(birthYear);
        sb.append(" ");
        sb.append(phoneMobile);
        sb.append(" ");
        sb.append(phoneHome);
        sb.append(" ");
        sb.append(email);
        sb.append(" ");
        sb.append(code);
        sb.append(" ");
        sb.append(barCode);
        sb.append(" ");
        sb.append(taxCode);
        sb.append(" ");
        sb.append(identityNumber);
        return sb.toString().trim().replace("\\s+", " ").toUpperCase();
    }

}
