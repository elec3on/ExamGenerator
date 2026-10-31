package org.skypro.ExamGenerator.service;

import org.skypro.ExamGenerator.model.Question;

import java.util.Collection;

public interface ExaminerService {
    Collection<Question> getQuestions(int amount);
}
