package com.an.utils;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by karas on 3/29/15.
 */
public class TestUtil {

    public static void printMap(Map<String, Object> map){
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, Object>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Object> entry = iter.next();
            sb.append(entry.getKey());
            sb.append('=').append('"');
            sb.append(entry.getValue());
            sb.append('"');
            if (iter.hasNext()) {
                sb.append(',').append(' ');
            }
        }
        System.out.println(sb.toString());

    }
}
