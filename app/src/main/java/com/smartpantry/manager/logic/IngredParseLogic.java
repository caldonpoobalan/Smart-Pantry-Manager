package com.smartpantry.manager.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// normalizes ingredient names for singular and plural matching
public class IngredParseLogic {

    // irregular plurals lookup
    private static final Map<String, String> irrPlurMap = new HashMap<>();

    // words that should not be trimmed of trailing s
    private static final Set<String> whtListSet = new HashSet<>();

    static {
        // will add more here later once I think of more recipes and ingredients needed for seed data
        irrPlurMap.put("tomatoes", "tomato");
        irrPlurMap.put("potatoes", "potato");

        // words ending in s that are already singular
        whtListSet.add("cheese");
    }

    // cleans word and converts plural to singular
    public static String normTerm(String rawWord) {
        if (rawWord == null) return "";
        String clnWord = rawWord.trim().toLowerCase();

        // check irregular words first
        if (irrPlurMap.containsKey(clnWord)) {
            return irrPlurMap.get(clnWord);
        }

        // check words ending in s that are already singular
        if (whtListSet.contains(clnWord)) {
            return clnWord;
        }

        // words ending with es
        if (clnWord.endsWith("es")) {
            return clnWord.substring(0, clnWord.length() - 2);
        }

        // standard ingredient plurals ending in s, I will have to add more depending on the seed recipes
        if (clnWord.endsWith("s") && !clnWord.endsWith("ss")) {
            return clnWord.substring(0, clnWord.length() - 1);
        }

        return clnWord;
    }

    // checks if two ingredient names match
    public static boolean isMatch(String stkWord, String recpWord) {
        return normTerm(stkWord).equals(normTerm(recpWord));
    }
}
