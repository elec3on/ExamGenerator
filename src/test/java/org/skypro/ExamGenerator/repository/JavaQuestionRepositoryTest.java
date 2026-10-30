package org.skypro.ExamGenerator.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skypro.ExamGenerator.model.Question;

import static org.junit.jupiter.api.Assertions.*;

class JavaQuestionRepositoryTest {
    private JavaQuestionRepository repository;

    @BeforeEach
    void setUp() {
        repository = new JavaQuestionRepository();
    }

    @Test
    void add_ShouldAddQuestion() {
        Question q = new Question("Q1", "A1");
        repository.add(q);
        assertEquals(1, repository.getAll().size());
        assertTrue(repository.getAll().contains(q));
    }

    @Test
    void remove_ShouldRemoveExistingQuestion() {
        Question q = new Question("Q1", "A1");
        repository.add(q);
        Question removed = repository.remove(new Question("Q1", "A1"));
        assertNotNull(removed);
        assertTrue(repository.getAll().isEmpty());
    }

    @Test
    void remove_ShouldReturnNullForMissingQuestion() {
        assertNull(repository.remove(new Question("None", "None")));
    }

    @Test
    void getAll_ShouldReturnCopy() {
        repository.add(new Question("Q1", "A1"));
        var all = repository.getAll();
        all.clear();
        assertEquals(1, repository.getAll().size());
    }

    @Test
    void add_Duplicate_ShouldNotIncreaseSize() {
        Question q = new Question("Q1", "A1");
        repository.add(q);
        repository.add(new Question("Q1", "A1")); // эквивалентный объект

        assertEquals(1, repository.getAll().size());
    }
}
