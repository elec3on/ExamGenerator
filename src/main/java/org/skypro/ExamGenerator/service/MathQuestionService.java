package org.skypro.ExamGenerator.service;

import org.skypro.ExamGenerator.model.Question;
import org.skypro.ExamGenerator.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@Service
public class MathQuestionService implements QuestionService {
    private final QuestionRepository repository;
    private final Random random = new Random();

    public MathQuestionService(@Qualifier("mathQuestionRepository") QuestionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Question add(String question, String answer) {
        return add(new Question(question, answer));
    }

    @Override
    public Question add(Question question) {
        return repository.add(question);
    }

    @Override
    public Question remove(Question question) {
        return repository.remove(question);
    }

    @Override
    public Collection<Question> getAll() {
        return repository.getAll();
    }

    @Override
    public Question getRandomQuestion() {
        List<Question> list = new ArrayList<>(repository.getAll());
        if (list.isEmpty()) {
            throw new IllegalStateException("Список вопросов пуст");
        }
        return list.get(random.nextInt(list.size()));
    }
}