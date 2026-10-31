package org.skypro.ExamGenerator.service;

import org.skypro.ExamGenerator.model.Question;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExaminerServiceImpl implements ExaminerService {
    private final QuestionService javaQuestionService;
    private final QuestionService mathQuestionService;

    public ExaminerServiceImpl(
            @Qualifier("javaQuestionService") QuestionService javaQuestionService,
            @Qualifier("mathQuestionService") QuestionService mathQuestionService) {
        this.javaQuestionService = javaQuestionService;
        this.mathQuestionService = mathQuestionService;
    }

    @Override
    public Collection<Question> getQuestions(int amount) {
        List<Question> all = new ArrayList<>();
        all.addAll(javaQuestionService.getAll());
        all.addAll(mathQuestionService.getAll());

        Set<Question> unique = new HashSet<>(all);

        if (amount > unique.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Запрошено больше вопросов, чем доступно в хранилище");
        }

        Set<Question> result = new HashSet<>();
        List<Question> shuffled = new ArrayList<>(unique);
        Collections.shuffle(shuffled);

        for (Question q : shuffled) {
            if (result.size() == amount) break;
            result.add(q);
        }

        if (result.size() != amount) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Не удалось набрать нужное количество уникальных вопросов");
        }

        return result;
    }
}