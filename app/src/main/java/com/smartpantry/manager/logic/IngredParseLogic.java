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

    // checks if two ingredient names match by whole string or individual word
    public static boolean isMatch(String stkWord, String recpWord) {
        if (stkWord == null || recpWord == null) {
            return false;
        }

        String nStk = normTerm(stkWord);
        String nRec = normTerm(recpWord);

        // handles exact match
        if (nStk.equals(nRec)) {
            return true;
        }

        // handles multi-word items (like red tomato or green tomatoes)
        String[] stkWords = stkWord.trim().toLowerCase().split("\\s+");
        for (String w : stkWords) {
            if (normTerm(w).equals(nRec)) {
                return true;
            }
        }

        // handles multi-word recipe requirements (like recipe needs blue cheese instead of cheese)
        String[] recpWords = recpWord.trim().toLowerCase().split("\\s+");
        for (String w : recpWords) {
            if (normTerm(w).equals(nStk)) {
                return true;
            }
        }

        return false;
    }
}
