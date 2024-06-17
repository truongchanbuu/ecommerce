package com.buutruong.ecommerce.core.util;

import com.ibm.icu.text.UnicodeSet;

import java.text.Normalizer;

public class StringUtils {
    public static String removeAccentsAndWhiteSpace(String str) {
        String normalized = Normalizer.normalize(str.toUpperCase(), Normalizer.Form.NFD);

        return normalized
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("\\p{Punct}", "")
                .replaceAll("\\s+", "");
    }
}
