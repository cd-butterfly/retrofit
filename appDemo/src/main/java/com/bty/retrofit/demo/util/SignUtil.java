package com.bty.retrofit.demo.util;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 */
public class SignUtil {
    /**
     * md5签名
     *
     */
    public static String md5Signature(Map params, String secret) {
        String result = null;

        String orgin = getParam(params, null);
        if (orgin == null)
            return result;
        orgin = secret + orgin + secret;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = byte2hex(md.digest(orgin.getBytes("utf-8")));
        } catch (Exception e) {
            throw new RuntimeException("Isign error !");
        }
        return result;
    }

    /**
     */
    public static Map sign(Map params, String secret) {
        if (params.containsKey("Sign")) {
            params.remove("Sign");
        }
        if (params.containsKey("Isign")) {
            params.remove("Isign");
        }
        String signature = md5Signature(params, secret).toLowerCase();
        params.put("Sign", signature);
        return params;
    }

    /**
     * 获取签名
     *
     */
    public static String sign(String json, String secret) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            Map map = json2Map(jsonObject);
            sign(map, secret);
            return JSON.toJSONString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     */
    private static String byte2hex(byte[] b) {

        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
        }
        return hs.toString().toUpperCase();

    }

    /**
     */
    public static List getList(JSONArray array) {
        if (array == null || array.length() == 0) {
            return null;
        } else {
            try {
                String type = array.get(0).getClass().getName();
                if (type.startsWith("org.json.JSONObject")) {
                    List<Map> list2 = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        try {
                            JSONObject obj = (JSONObject) array.get(i);
                            list2.add(json2Map(obj));
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    return list2;
                } else {
                    List list = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        try {
                            Object obj = array.get(i);
                            list.add(obj);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                    return list;
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     */
    public static Map json2Map(JSONObject jsonObj) {
        if (jsonObj == null)
            return null;
        Map<String, Object> map = new TreeMap();
        Iterator it = jsonObj.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            // 过滤token
            if (!key.toLowerCase().equals("token")) {
                try {
                    Object value = jsonObj.get(key);
                    if (value.getClass().equals(JSONArray.class)) {
                        map.put(key, getList((JSONArray) value));
                    } else if (value.getClass().equals(JSONObject.class)) {
                        map.put(key, json2Map((JSONObject) value));
                    } else {
                        map.put(key, value);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return sortMapByKey(map);
    }

    /**
     *
     */
    public static String getParam(Map map, String parent) {
        if (map == null)
            return null;
        Iterator entries = map.entrySet().iterator();
        StringBuffer sb = new StringBuffer();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            if (value != null) {
                if (parent != null) {
                    key = parent + "[" + key + "]";
                }
                if (value instanceof Date) {
                    DateFormat df = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss");
                    sb.append(key);
                    sb.append(df.format(value));
                } else if (value.getClass().isEnum()) {
                    Enum<?> e = (Enum<?>) value;
                    sb.append(key);
                    sb.append(e.toString());
                } else if (value.getClass().isPrimitive()
                        || value.getClass().getName().startsWith("java.lang")) {
                    sb.append(key);
//                    sb.append(String.valueOf(value));
                    sb.append(StringUtils.subZeroAndDot(value));
                } else if (value instanceof List) {
                    sb.append(getParam((List) value, key));
                } else {
                    sb.append(getParam((Map) value, key));
                }
            }
        }
        return sb.toString();
    }

    /**
     *
     */
    public static String getParam(List list, String parent) {
        if (list == null || list.size() == 0)
            return null;
        StringBuffer sb = new StringBuffer();
        if (list.get(0) instanceof Date) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            parent = parent + "[]";
            for (Object obj : list) {
                sb.append(parent);
                sb.append(df.format(obj));
            }
        } else if (list.get(0).getClass().isEnum()) {
            parent = parent + "[]";
            for (Object obj : list) {
                Enum<?> e = (Enum<?>) list.get(0);
                sb.append(parent);
                sb.append(e.toString());
            }
        } else if (list.get(0).getClass().isPrimitive()
                || list.get(0).getClass().getName().startsWith("java.lang")) {
            parent = parent + "[]";
            for (Object obj : list) {
                sb.append(parent);
//                sb.append(String.valueOf(obj));
                sb.append(StringUtils.subZeroAndDot(obj));
            }
        } else if (list.get(0) instanceof List) {
            for (int i = 0; i < list.size(); i++) {
                sb.append(getParam((List) list.get(i), parent + "[" + i + "]"));
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                sb.append(getParam((Map) list.get(i), parent + "[" + i + "]"));
            }
        }
        return sb.toString();
    }

    /**
     */
    public static List getList(Object obj) {
        if (!(obj instanceof Collection || obj.getClass().isArray()))
            return null;
        List list = new ArrayList<>();
        if (obj instanceof Collection) {
            Object[] objs = ((Collection<?>) obj).toArray();
            for (Object item : objs) {
                list.add(getValue(item));
            }
        } else {
            for (int i = 0; i < Array.getLength(obj); ++i) {
                Object item = Array.get(obj, i);
                list.add(getValue(item));
            }
        }
        return list;
    }

    /**
     */
    public static Map getMap(Map map) {
        if (map == null)
            return null;
        Map<String, Object> map2 = new TreeMap();
        Iterator entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            if (entry.getValue() == null)
                continue;
            try {
                map2.put(String.valueOf(entry.getKey()),
                        getValue(entry.getValue()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map2;
    }

    /**
     */
    public static Object getValue(Object value) {
        if (value.getClass().isPrimitive()
                || value.getClass().getName().startsWith("java.lang")
                || value instanceof Date || value.getClass().isEnum()) {
            return value;
        } else if (value instanceof Collection || value.getClass().isArray()) {
            return getList(value);
        } else if (value instanceof Map) {
            return getMap((Map) value);
        } else {
            return obj2Map(value);
        }
    }

    /**
     */
    public static List<Field> getFields(Object obj) {
        if (obj == null)
            return null;
        List<Field> fieldList = new ArrayList<>();
        for (Class clazz = obj.getClass(); clazz != Object.class; clazz = clazz
                .getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            fieldList.addAll(Arrays.asList(fields));
        }
        return fieldList;
    }

    /**
     *
     */
    public static Map obj2Map(Object thisObj) {
        if (thisObj == null)
            return null;
        Map<String, Object> map = new TreeMap();
        try {
            List<Field> fields = getFields(thisObj);
            for (Field field : fields) {
                if (!field.getName().toLowerCase().equals("token") && !field.getName().toLowerCase().equals("serialversionuid")) {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(thisObj);
                        if (value != null) {
                            map.put(field.getName(), getValue(value));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String, Object> sortMapByKey(Map<String, Object> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, Object> sortedMap = new TreeMap<String, Object>(new Comparator<String>() {
            public int compare(String key1, String key2) {
                return key1.toLowerCase().compareTo(key2.toLowerCase());
            }
        });
        sortedMap.putAll(oriMap);
        return sortedMap;
    }
}

