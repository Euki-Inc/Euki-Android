package com.kollectivemobile.euki.manager;

import android.util.Pair;

import com.kollectivemobile.euki.model.Question;
import com.kollectivemobile.euki.model.Quiz;
import com.kollectivemobile.euki.model.QuizMethod;
import com.kollectivemobile.euki.model.QuizType;
import com.kollectivemobile.euki.networking.EukiCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizManagerImpl implements QuizManager {
    @Override
    public List<QuizMethod> getMethods(QuizType quizType) {
        List<QuizMethod> methods = new ArrayList<>();
        switch (quizType) {
            case CONTRACEPTION:
                for (int i = 1; i <= 12; i++) {
                    methods.add(new QuizMethod("contraception_" + i, "icon_contraception_" + i));
                }
                break;
            case MENSTRUATION:
                for (int i = 1; i <= 7; i++) {
                    methods.add(new QuizMethod("menstruation_" + i, "icon_menstruation_" + i));
                }
                break;
        }

        return methods;
    }

    @Override
    public void getContraceptionQuiz(EukiCallback<Quiz> callback) {
        Quiz quiz = new Quiz("contraception_quiz_instructions");

        Question question = new Question("how_want_start_method");
        question.getOptions().add(new Pair("pn_my_own", Arrays.asList(8, 9, 10, 12)));
        question.getOptions().add(new Pair("at_a_clinic", Arrays.asList(1, 2, 3, 4, 5, 6, 7)));
        question.getOptions().add(new Pair("want_permanent_method", Arrays.asList(11)));
        quiz.getQuestions().add(question);

        question = new Question("how_often_use_method");
        question.getOptions().add(new Pair("when_i_have_sex", Arrays.asList(8, 9, 10, 12)));
        question.getOptions().add(new Pair("daily", Arrays.asList(6)));
        question.getOptions().add(new Pair("weekly", Arrays.asList(5)));
        question.getOptions().add(new Pair("monthly", Arrays.asList(4, 7)));
        question.getOptions().add(new Pair("yearly_or_less", Arrays.asList(1, 2, 3, 11)));
        quiz.getQuestions().add(question);

        question = new Question("how_important_is_pregnancy");
        question.getOptions().add(new Pair("very_important", Arrays.asList(1, 2, 3, 11)));
        question.getOptions().add(new Pair("somewhat_important", Arrays.asList(4, 5, 6, 7)));
        question.getOptions().add(new Pair("not_important", Arrays.asList(8, 9, 10, 12)));
        quiz.getQuestions().add(question);

        question = new Question("what_benefic_interest");
        question.getOptions().add(new Pair("regular_period", Arrays.asList(2, 6)));
        question.getOptions().add(new Pair("lighter_bleeding", Arrays.asList(1, 4, 5, 6, 7)));
        question.getOptions().add(new Pair("less_cramping", Arrays.asList(3, 5, 6, 7)));
        question.getOptions().add(new Pair("less_acne", Arrays.asList(5, 6, 7)));
        quiz.getQuestions().add(question);

        question = new Question("what_side_effects_ok");
        question.getOptions().add(new Pair("lighter_period", Arrays.asList(1, 3, 5, 6, 7)));
        question.getOptions().add(new Pair("heavier_period", Arrays.asList(2)));
        question.getOptions().add(new Pair("weight_gain", Arrays.asList(4)));
        question.getOptions().add(new Pair("no_side_effects", Arrays.asList(8, 9, 10, 11, 12)));
        quiz.getQuestions().add(question);

        question = new Question("how_stop_using_method");
        question.getOptions().add(new Pair("on_my_own", Arrays.asList(5, 6, 7, 8, 9, 10, 12)));
        question.getOptions().add(new Pair("at_a_clinic", Arrays.asList(1, 2, 3, 4)));
        question.getOptions().add(new Pair("i_want_permanent_method", Arrays.asList(11)));
        quiz.getQuestions().add(question);

        callback.onSuccess(quiz);
    }


    @Override
    public Pair<String, List<Integer>> getresultContraception(Quiz quiz) {
        Boolean hasAnswer = false;
        Map<Integer, Integer> contraceptionCounts = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            contraceptionCounts.put(i, 0);
        }

        for (Question question : quiz.getQuestions()) {
            Integer answerIndex = question.getAnswerIndex();
            if (answerIndex == null) {
                continue;
            }

            for (Integer contraceptionIndex : question.getOptions().get(answerIndex).second) {
                Integer value = contraceptionCounts.get(contraceptionIndex);
                contraceptionCounts.put(contraceptionIndex, value + 1);
                hasAnswer = true;
            }
        }

        List<Integer> resultIndexes = new ArrayList<>();

        if (hasAnswer) {
            while (resultIndexes.size() < 3) {
                Integer currentMax = contraceptionCounts.get(1);
                Integer maxKey = 1;

                for (Integer key : contraceptionCounts.keySet()) {
                    Integer value = contraceptionCounts.get(key);
                    if (currentMax < value) {
                        currentMax = value;
                        maxKey = key;
                    }
                }

                if (currentMax > 3) {
                    for (Integer currentKey : contraceptionCounts.keySet()) {
                        Integer value = contraceptionCounts.get(currentKey);
                        if (value == currentMax) {
                            contraceptionCounts.put(currentKey, -1);
                            resultIndexes.add(currentKey);
                        }
                    }
                } else {
                    contraceptionCounts.put(maxKey, -1);
                    resultIndexes.add(maxKey);
                }
            }
        }

        String result = hasAnswer ? "recommended_methods" : "no_recommended_methods";
        return new Pair<>(result, resultIndexes);
    }

    @Override
    public void getMenstruationQuiz(EukiCallback<Quiz> callback) {
        Quiz quiz = new Quiz("menstruation_quiz_instructions");

        Question question = new Question("id_prefer_to_get_menstrual_products");
        question.getOptions().add(new Pair("at_the_pharmacy", Arrays.asList(2, 3, 4, 5, 7)));
        question.getOptions().add(new Pair("at_the_grocery_store", Arrays.asList(1, 2, 3, 7)));
        question.getOptions().add(new Pair("at_the_gas_station", Arrays.asList(2, 3, 7)));
        question.getOptions().add(new Pair("online", Arrays.asList(1, 2, 3, 4, 5, 6, 7)));
        question.getOptions().add(new Pair("free_at_a_clinic", Arrays.asList(2, 3, 7)));
        quiz.getQuestions().add(question);

        question = new Question("id_prefer_to_pay");
        question.getOptions().add(new Pair("about_5_10_per_month", Arrays.asList(2, 3, 7)));
        question.getOptions().add(new Pair("about_15_20_per_month", Arrays.asList(5)));
        question.getOptions().add(new Pair("about_20_50_one_time", Arrays.asList(1, 4, 6)));
        quiz.getQuestions().add(question);

        question = new Question("id_like_a_product_i_can_swim_in");
        question.getOptions().add(new Pair("yes", Arrays.asList(3, 4, 5)));
        question.getOptions().add(new Pair("no", Arrays.asList(1, 2, 6, 7)));
        quiz.getQuestions().add(question);

        question = new Question("i_want_a_product_that_i_only_have_to_change_every");
        question.getOptions().add(new Pair("two_four_hours", Arrays.asList(1, 2, 3, 7)));
        question.getOptions().add(new Pair("four_eight_hours", Arrays.asList(3, 6)));
        question.getOptions().add(new Pair("up_to_12_hours", Arrays.asList(4, 5, 6)));
        quiz.getQuestions().add(question);

        question = new Question("id_prefer_a_product_that_i_can");
        question.getOptions().add(new Pair("wear_outside_of_my_body", Arrays.asList(1, 2, 6, 7)));
        question.getOptions().add(new Pair("insert_into_my_body", Arrays.asList(3, 4, 5)));
        quiz.getQuestions().add(question);

        callback.onSuccess(quiz);
    }

    @Override
    public Pair<String, List<Integer>> getResultMenstruation(Quiz quiz) {
        Boolean hasAnswer = false;
        Map<Integer, Integer> menstruationCounts = new HashMap<>();
        for (int i = 1; i <= 7; i++) {
            menstruationCounts.put(i, 0);
        }

        for (Question question : quiz.getQuestions()) {
            Integer answerIndex = question.getAnswerIndex();
            if (answerIndex == null) {
                continue;
            }

            for (Integer menstruationIndex : question.getOptions().get(answerIndex).second) {
                Integer value = menstruationCounts.get(menstruationIndex);
                menstruationCounts.put(menstruationIndex, value + 1);
                hasAnswer = true;
            }
        }

        List<Integer> resultIndexes = new ArrayList<>();

        if (hasAnswer) {
            while (resultIndexes.size() < 3) {
                Integer currentMax = menstruationCounts.get(1);
                Integer maxKey = 1;

                for (Integer key : menstruationCounts.keySet()) {
                    Integer value = menstruationCounts.get(key);
                    if (currentMax < value) {
                        currentMax = value;
                        maxKey = key;
                    }
                }

                if (currentMax > 3) {
                    for (Integer currentKey : menstruationCounts.keySet()) {
                        Integer value = menstruationCounts.get(currentKey);
                        if (value == currentMax) {
                            menstruationCounts.put(currentKey, -1);
                            resultIndexes.add(currentKey);
                        }
                    }
                } else {
                    menstruationCounts.put(maxKey, -1);
                    resultIndexes.add(maxKey);
                }
            }
        }

        String result = hasAnswer ? "recommended_methods_menstruation" : "no_recommended_methods_menstruation";
        return new Pair<>(result, resultIndexes);
    }
}
