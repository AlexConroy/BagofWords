package com.alex.bagofwords;

import android.content.Context;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static java.lang.Math.*;

public class Sentences {

    static ArrayList<String> noviceSentences = new ArrayList<String>();
    static ArrayList<String> beginnerSentences = new ArrayList<String>();
    static ArrayList<String> intermediateSentences = new ArrayList<String>();
    static ArrayList<String> advancedSentences = new ArrayList<String>();
    static Random random;

    // ----- Methods for novice sentences -------
    public static void addNoviceSentence(String sentence) {
        noviceSentences.add(sentence);
    }

    public static int numberOfNoviceSentences() {
        return noviceSentences.size();
    }

    /* remove from all arraylists.
    public static String getNoviceSentence(int position) {
        return noviceSentences.get(position);
    } */

    public static String pickRandomNoviceSentence() {
        random = new Random();
        String randomSentence = noviceSentences.get(random.nextInt(numberOfNoviceSentences()));
        return randomSentence;
    }

    public static boolean noviceNotEmpty() {
        return numberOfNoviceSentences() != 0;
    }

    // ---------- END -----------------


    // ------ Beginner Methods --------

    public static void addBeginnerSentence(String sentence) {
        beginnerSentences.add(sentence);
    }

    public static int numberOfBeginnerSentences() {
        return beginnerSentences.size();
    }

    public static String getBeginnerSentence(int position) {
        return beginnerSentences.get(position);
    }

    public static String pickRandomBeginnerSentence() {
        random = new Random();
        String randomSentence = beginnerSentences.get(random.nextInt(numberOfBeginnerSentences()));
        return randomSentence;
    }

    public static boolean beinngerNotEmpty() {
        return numberOfBeginnerSentences() != 0;
    }

    // --------- END --------------

    // ---------- Intermediate Methods -----------
    public static void addIntermediateSentence(String sentence) {
        intermediateSentences.add(sentence);
    }

    public static int numberOfIntermediateSentences() {
        return intermediateSentences.size();
    }


    public static String pickRandomIntermediateSentence() {
        random = new Random();
        String randomSentence = intermediateSentences.get(random.nextInt(numberOfIntermediateSentences()));
        return randomSentence;
    }

    public static boolean intermediateNotEmpty() {
        return numberOfIntermediateSentences() != 0;
    }

    // ----------------------

    // ---- Advanced Methods -------

    public static void addAdvancedSentence(String sentence) {
        advancedSentences.add(sentence);
    }

    public static int numberOfAdvancedSentences() {
        return advancedSentences.size();
    }


    public static String pickRandomAdvancedSentence() {
        random = new Random();
        String randomSentence = advancedSentences.get(random.nextInt(numberOfAdvancedSentences()));
        return randomSentence;
    }

    public static boolean advancedNotEmpty() {
        return numberOfAdvancedSentences() != 0;
    }

    // ------ END ---------


    public static int evaluate(String correctSentence, String userInputSentence) {
        String splitCorrectSentence[] = correctSentence.split("\\s+|(?=\\W)");
        String splitInputedSentence[] = userInputSentence.split("\\s+|(?=\\W)");
        int size = splitCorrectSentence.length;
        int score = 0;
        for(int i = 0; i < size; i++) {
            if(splitCorrectSentence[i].equals(splitInputedSentence[i])) {
                score++;
            }
        }
        return score;
    }

    public static int gameScore(int matches, int timeRemaining) {
        return (int) (pow(matches, 2) * timeRemaining);
    }


    public static String[] shuffleSentence(String[] sentence) {
        Random random = new Random();
        int wordsLength = sentence.length - 1; // just the length of the words in the sentence
        for(int i = 0; i < wordsLength; i++){
            int randomPosition = random.nextInt(wordsLength);
            String temp = sentence[i];
            sentence[i] = sentence[randomPosition];
            sentence[randomPosition] = temp;
        }
        return sentence;
    }





}
