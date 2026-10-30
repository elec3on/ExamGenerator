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
class JavaQuestionServiceTest {

    @Mock
    private QuestionRepository repository;

    @InjectMocks
    private JavaQuestionService service;

    // 1. add(String, String)
    @Test
    void add_StringString_ShouldDelegateToRepository() {
        Question q = new Question("Q1", "A1");
        when(repository.add(any(Question.class))).thenReturn(q);

        Question result = service.add("Q1", "A1");

        assertEquals(q, result);
        verify(repository).add(new Question("Q1", "A1"));
    }

    // 2. add(Question)
    @Test
    void add_Question_ShouldDelegateToRepository() {
        Question q = new Question("Q1", "A1");
        when(repository.add(q)).thenReturn(q);

        Question result = service.add(q);

        assertEquals(q, result);
        verify(repository).add(q);
    }

    // 3. remove
    @Test
    void remove_ShouldDelegateToRepository() {
        Question q = new Question("Q1", "A1");
        when(repository.remove(q)).thenReturn(q);

        assertEquals(q, service.remove(q));
        verify(repository).remove(q);
    }

    // 3b. remove отсутствующего — возвращает null
    @Test
    void remove_Missing_ShouldReturnNull() {
        Question q = new Question("Q1", "A1");
        when(repository.remove(q)).thenReturn(null);

        assertNull(service.remove(q));
    }

    // 4. getAll
    @Test
    void getAll_ShouldReturnRepositoryData() {
        List<Question> questions = List.of(
                new Question("Q1", "A1"),
                new Question("Q2", "A2"));
        when(repository.getAll()).thenReturn(questions);

        assertEquals(questions, service.getAll());
    }

    // 5. getRandomQuestion — непустая коллекция
    @Test
    void getRandomQuestion_ShouldReturnOneOfExisting() {
        List<Question> questions = List.of(
                new Question("Q1", "A1"),
                new Question("Q2", "A2"),
                new Question("Q3", "A3"));
        when(repository.getAll()).thenReturn(questions);

        assertTrue(questions.contains(service.getRandomQuestion()));
    }

    // 5b. getRandomQuestion — пустая коллекция
    @Test
    void getRandomQuestion_Empty_ShouldThrow() {
        when(repository.getAll()).thenReturn(List.of());
        assertThrows(IllegalStateException.class, () -> service.getRandomQuestion());
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