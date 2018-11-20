package com.techexchange.mobileapps.assignment2;


import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class QuestionListFactory {
    private static Random rand = new Random();

    private static Question createQuestion(List cities, List countries, int ccIndex) {
        String question = cities.get(ccIndex) + " is the capital of which country?";
        String correctAnswer = countries.get(ccIndex).toString();
        List<String> wrongAnswers = new ArrayList<String>();
        int numInputs = countries.size();
        int  n;
        String currCountry;

        for (int j = 0; j < 3; ++j) {
            do {
                n = rand.nextInt(numInputs) + 0; // numInputs is exclusive max and 0 is inclusive min.
                currCountry = countries.get(n).toString();
            } while (currCountry.equals(correctAnswer) || wrongAnswers.contains(currCountry));
            wrongAnswers.add(currCountry);
        }
        return new Question(question, correctAnswer, wrongAnswers.toArray(new String[3]), Question.State.WHITE);
    }

    public static List<Question> readCSV(InputStream inputStream) {
        List<Question> resultList = new ArrayList();
        CSVReader reader = null;

        try{
            reader = new CSVReader(new InputStreamReader(inputStream));
            String[] nextLine;
            List countries = new ArrayList<String>();
            List cities = new ArrayList<String>();

            reader.readNext(); // to get rid of the column names.
            while ((nextLine = reader.readNext()) != null) {
                countries.add(nextLine[0]);
                cities.add(nextLine[1]);
            }
            int numInputs = countries.size();

            for (int i = 0; i < numInputs; ++i) {
                resultList.add(createQuestion(cities, countries, i));
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(reader != null) {
                try {
                    reader.close();
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resultList;
    }
}
