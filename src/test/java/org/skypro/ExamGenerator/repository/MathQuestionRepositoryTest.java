package org.skypro.ExamGenerator.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skypro.ExamGenerator.model.Question;

import static org.junit.jupiter.api.Assertions.*;

class MathQuestionRepositoryTest {
    private MathQuestionRepository repository;

    @BeforeEach
    void setUp() {
        repository = new MathQuestionRepository();
    }

    @Test
    void add_ShouldAddQuestion() {
        Question q = new Question("2+2", "4");
        repository.add(q);
        assertEquals(1, repository.getAll().size());
    }

    @Test
    void add_ShouldNotDuplicate() {
        repository.add(new Question("2+2", "4"));
        repository.add(new Question("2+2", "4"));
        assertEquals(1, repository.getAll().size());
    }

    @Test
    void remove_ShouldRemoveQuestion() {
        repository.add(new Question("2+2", "4"));
        Question removed = repository.remove(new Question("2+2", "4"));
        assertNotNull(removed);
        assertTrue(repository.getAll().isEmpty());
    }
}