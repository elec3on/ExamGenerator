package org.skypro.ExamGenerator.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.ExamGenerator.model.Question;
import org.skypro.ExamGenerator.repository.JavaQuestionRepository;
import org.skypro.ExamGenerator.repository.QuestionRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MathQuestionServiceTest {

    @Mock
    private QuestionRepository repository;

    @InjectMocks
    private MathQuestionService service;

    @Test
    void add_ShouldDelegateToRepository() {
        Question q = new Question("2+2", "4");
        when(repository.add(any(Question.class))).thenReturn(q);

        Question result = service.add("2+2", "4");

        assertEquals(q, result);
        verify(repository).add(new Question("2+2", "4"));
    }

    @Test
    void remove_ShouldDelegateToRepository() {
        Question q = new Question("2+2", "4");
        when(repository.remove(q)).thenReturn(q);

        Question removed = service.remove(q);

        assertEquals(q, removed);
        verify(repository).remove(q);
    }

    @Test
    void getAll_ShouldReturnRepositoryData() {
        List<Question> questions = List.of(new Question("2+2", "4"));
        when(repository.getAll()).thenReturn(questions);

        assertEquals(questions, service.getAll());
    }

    @Test
    void getRandomQuestion_ShouldThrowWhenEmpty() {
        when(repository.getAll()).thenReturn(List.of());
        assertThrows(IllegalStateException.class, () -> service.getRandomQuestion());
    }

    @Test
    void getRandomQuestion_ShouldReturnOneFromRepository() {
        List<Question> questions = List.of(
                new Question("2+2", "4"),
                new Question("3*3", "9")
        );
        when(repository.getAll()).thenReturn(questions);

        assertTrue(questions.contains(service.getRandomQuestion()));
    }

    @Test
    void add_Duplicate_ShouldBeStoredOnce() {
        JavaQuestionRepository realRepo = new JavaQuestionRepository();
        JavaQuestionService s = new JavaQuestionService(realRepo);

        s.add("Q1", "A1");
        s.add("Q1", "A1");

        assertEquals(1, s.getAll().size());
    }
}
