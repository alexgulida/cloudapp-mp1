/**
 * Created by Aliaksandr_Hulida on 8/27/2015.
 * Solution for Coursera Cloud Computing Coulrse. Week1
 * Programming Assignment 1:
 * Java Programming Essentials
 */

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];

        List<String> linesList = Files.readAllLines(Paths.get(inputFileName), StandardCharsets.ISO_8859_1);
        List<String> linesListIndexed = new ArrayList<>();

        for (int i : getIndexes()) {
            String line = linesList.get(i).toLowerCase();
            StringTokenizer st = new StringTokenizer(line, delimiters);
            while (st.hasMoreTokens()) {
                linesListIndexed.add(st.nextToken());
            }

        }
        Set<String> stopWordsSet = new HashSet<>(Arrays.asList(stopWordsArray));
        linesListIndexed.removeAll(stopWordsSet);

        Map<String, Integer> map = new HashMap<String, Integer>();

        for (String s : linesListIndexed) {
            if (map.containsKey(s)) {
                map.put(s, map.get(s) + 1);

            } else {
                map.put(s, 1);
            }
        }

        ValueComparator<String, Integer> comparator = new ValueComparator<String, Integer>(map);
        Map<String, Integer> sortedMap = new TreeMap<>(comparator);
        sortedMap.putAll(map);
        List<String> sortedList = new ArrayList<>(sortedMap.keySet());


        PrintWriter output = new PrintWriter(new FileWriter("./output.txt"));
        for (int i = 0; i < ret.length; i++) {
            ret[i] = sortedList.get(i);
            output.println(ret[i]);
        }
        output.close();
        return ret;
    }

    static class ValueComparator<K, V extends Comparable<V>> implements Comparator<K> {

        Map<K, V> map;

        public ValueComparator(Map<K, V> base) {
            this.map = base;
        }

        @Override
        public int compare(K o1, K o2) {
            return map.get(o2).compareTo(map.get(o1));
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("MP1 <User ID>");
        } else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item : topItems) {
                System.out.println(item);
            }
        }
    }
}