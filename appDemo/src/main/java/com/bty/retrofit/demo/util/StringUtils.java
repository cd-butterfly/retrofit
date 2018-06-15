package com.bty.retrofit.demo.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * �ַ���������
 */
public class StringUtils {
    /**
     * ���ָ�����ַ����Ƿ�Ϊ�ա�
     * <ul>
     * <li>SysUtils.isEmpty(null) = true</li>
     * <li>SysUtils.isEmpty("") = true</li>
     * <li>SysUtils.isEmpty("   ") = true</li>
     * <li>SysUtils.isEmpty("abc") = false</li>
     * </ul>
     *
     * @param value �������ַ���
     * @return true/false
     */
    public static boolean isEmpty(String value) {
        int strLen;
        if (value == null || (strLen = value.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((!Character.isWhitespace(value.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * ���ָ�����ַ����б��Ƿ�Ϊ�ա�
     */
    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }


    /**
     * �������Ƿ�Ϊ�������ַ�����
     */
    public static boolean isNumeric(Object obj) {
        if (obj == null) {
            return false;
        }
        String str = obj.toString();
        int sz = str.length();
        if (sz == 0) {
            return false;
        }
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != '.') {
                return false;
            }
        }
        return true;
    }

    /**
     * ���ָ���ַ����Ƿ�Ϊ�ֻ���
     */
    public static boolean isMobileNO(String text) {
        return isMatch(text, "^(1[0-9])\\d{9}$");
    }

    /**
     * ��������ʽ�Ƿ���ȷ(ֻ������ĸ+����)
     */
    public static boolean isPassword(String text) {
        return isMatch(text, "^\\w{6,14}$");
    }

    /**
     * ����Ƿ���ʵ����
     *
     * @param text
     * @return
     */
    public static boolean isTrueName(String text) {
        return isMatch(text, "^\\w{1,6}$");
    }

    /**
     * ����Ƿ�QQ��
     * ��1-9��ͷ��5-12λ����
     *
     * @param text
     * @return
     */
    public static boolean isQQ(String text) {
        return isMatch(text, "^[1-9]\\d{4,11}$");
    }

    /**
     * ����Ƿ���Ϸ������ݸ�ʽ
     *
     * @param text
     * @return
     */
    public static boolean isFeedback(String text) {
        return isMatch(text, "^[\\w\\W]{1,500}$");
    }

    /**
     * ����Ƿ�ΪE-Mail
     *
     * @param text
     * @return
     */
    public static boolean isEmail(String text) {
        return isMatch(text, "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$");
    }

    /**
     * ����Ƿ�Ϊ��֤��
     *
     * @param text
     * @return
     */
    public static boolean isValidateCode(String text) {
        return isMatch(text, "^\\d{6}$");
    }

    /**
     * ����Ƿ��ǹ�˾��
     *
     * @param text
     * @return
     */
    public static boolean isCompany(String text) {
        return isMatch(text, "^\\w{1,20}$");
    }

    /**
     * ����Ƿ����ŵ���
     *
     * @param text
     * @return
     */
    public static boolean isStore(String text) {
        return isMatch(text, "^\\w{1,20}$");
    }

    /**
     * ����Ƿ�Ϊurl
     *
     * @param text
     * @return
     */
    public static boolean isUrl(String text) {
        return isMatch(text, "^(https|http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$");
    }

    public static boolean isMatch(String text, String pattern) {
        if (isEmpty(text)) return false;
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        return m.matches();
    }

    /**
     * �ַ���ת����
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int strToInt(String str, int defValue) {
        if (str != null && !str.equals("")) {
            try {
                return Integer.parseInt(str);
            } catch (Exception e) {
            }
        }
        return defValue;
    }

    /**
     * ����ַ�����true����false
     *
     * @param str
     * @return true | false
     */
    public static Boolean str2Bool(String str) {
        if (str != null) {
            return Boolean.parseBoolean(str);
        } else {
            return false;
        }
    }

    /**
     * listתstring
     *
     * @param list
     * @return
     */
    public static String listToString(ArrayList list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                sb.append(list.get(i));
            } else {
                sb.append("," + list.get(i));
            }
        }
        String str = sb.toString();
        return str;
    }

    public static String getFileName(String path) {
        int start = path.lastIndexOf("/");
        int end = path.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return path.substring(start + 1, end);
        } else {
            return null;
        }
    }


    /**
     * �鿴�ַ������м����ض��ַ�
     *
     * @param str �ַ���
     * @param des �ض��ַ�
     * @return
     */
    public static int getStringNum(String str, String des) {
        int cnt = 0;
        int offset = 0;
        while ((offset = str.indexOf(des, offset)) != -1) {
            offset = offset + des.length();
            cnt++;
        }
        return cnt;
    }

    /**
     * ��ȡ�ض��ַ������ֵ
     *
     * @param str
     * @return
     */
    public static String stringAfter(String str, String str2) {
        if (!str.equals(str2)) {
            String[] arr = str.split(str2);
            return arr[1];
        } else {
            return null;
        }

    }

    /**
     * ��ȡ�ض��ַ�ǰ����ֵ
     *
     * @param str
     * @return
     */
    public static String stringAgo(String str, String str2) {

        if (!str.equals(str2)) {
            String[] arr = str.split(str2);
            return arr[0];
        } else {
            return null;
        }

    }

    /**
     * ȥ���ַ���2ͷ�Ļ��з����������ַ�
     *
     * @param source
     * @return
     */
    public static String trimRN(String source) {
        if (source == null) return null;
        char[] array = source.toCharArray();
        int left = 0;
        for (char c : array) {
            if (c != 10 && c != 13 && c != 32 && (c < 128 || c > 160) && c != 12288) {
                break;
            }
            left++;
        }
        int right = array.length;
        for (int i = right - 1; i >= 0; i--) {
            if (array[i] != 10 && array[i] != 13 && array[i] != 32 && (array[i] < 128 || array[i] > 160) && array[i] != 12288) {
                break;
            }
            right--;
        }
        if (left >= right) {
            return "";
        } else {
            return source.substring(left, right);
        }
    }

    /**
     * ȥ������
     *
     * @param source
     * @return
     */
    public static String trimBracket(String source) {
        if (isEmpty(source)) return null;
        int index = source.indexOf("(");
        if (index > 0) {
            return source.substring(0, index);
        }
        return source;
    }

    /**
     * �ж��Ƿ�Ϊ������������double��float
     *
     * @param str ������ַ���
     * @return �Ǹ���������true, ���򷵻�false
     */
    public static boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * ��ȡ�ַ��� ����������Ϊdouble��float������ת����ȥ��0��.0
     * @param obj
     * @return
     */
    /*public static String getValueWithoutZero(Object obj){
        if (obj == null) {
            return "";
        }
        String str = obj.toString();
        int sz = str.length();
        if (sz == 0) {
            return "";
        }
        String returnStr = str;
        if(isDouble(str)){
            double fromStr = Double.parseDouble(str);
            long castStr = (long)fromStr;
            if (fromStr == castStr) {
                returnStr = castStr + "";
            } else {
                returnStr = fromStr + "";
            }
        }
        return returnStr;
    }*/

    public static String subZeroAndDot(Object obj){
        if (obj == null) {
            return "";
        }
        if(!getInstanceOf(obj)){
            return String.valueOf(obj);
        }
        String str = String.valueOf(obj);
        int sz = str.length();
        if (sz == 0) {
            return "";
        }
        if(str.indexOf(".") > 0){
            str = str.replaceAll("0+?$", "");//ȥ�������0
            str = str.replaceAll("[.]$", "");//�����һλ��.��ȥ��
        }
        return str;
    }

    public static boolean getInstanceOf(Object obj){
        if(obj instanceof Long ||obj instanceof Integer ||obj instanceof Double ||obj instanceof Float){
            return true;
        }
        return false;
    }
}
