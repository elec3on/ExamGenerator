package org.skypro.ExamGenerator.repository;

import org.skypro.ExamGenerator.model.Question;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Repository
public class MathQuestionRepository implements QuestionRepository {
    private final Set<Question> questions = new HashSet<>();

    @Override
    public Question add(Question question) {
        questions.add(question);
        return question;
    }

    @Override
    public Question remove(Question question) {
        return questions.remove(question) ? question : null;
    }

    @Override
    public Collection<Question> getAll() {
        return new HashSet<>(questions);
    }
}
