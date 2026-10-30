package org.skypro.ExamGenerator.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.ExamGenerator.model.Question;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExaminerServiceImplTest {

    @Mock
    private QuestionService javaQuestionService;

    @Mock
    private QuestionService mathQuestionService;

    @InjectMocks
    private ExaminerServiceImpl examinerService;

    @Test
    void getQuestions_ShouldCombineBothSubjects() {
        when(javaQuestionService.getAll()).thenReturn(List.of(
                new Question("JQ1", "JA1"),
                new Question("JQ2", "JA2")
        ));
        when(mathQuestionService.getAll()).thenReturn(List.of(
                new Question("MQ1", "MA1")
        ));

        Collection<Question> result = examinerService.getQuestions(3);

        assertEquals(3, result.size());
    }

    @Test
    void getQuestions_ShouldReturnUniqueQuestions() {
        Question shared = new Question("Shared", "SharedAnswer");
        when(javaQuestionService.getAll()).thenReturn(List.of(shared, new Question("JQ", "JA")));
        when(mathQuestionService.getAll()).thenReturn(List.of(shared));

        Collection<Question> result = examinerService.getQuestions(2);

        assertEquals(2, result.size());
        assertEquals(2, result.stream().distinct().count());
    }

    @Test
    void getQuestions_ShouldThrowWhenAmountTooLarge() {
        when(javaQuestionService.getAll()).thenReturn(List.of(new Question("JQ", "JA")));
        when(mathQuestionService.getAll()).thenReturn(List.of(new Question("MQ", "MA")));

        assertThrows(ResponseStatusException.class, () -> examinerService.getQuestions(5));
    }

    @Test
    void getQuestions_ShouldReturnAllWhenAmountEqualsTotal() {
        when(javaQuestionService.getAll()).thenReturn(List.of(new Question("JQ", "JA")));
        when(mathQuestionService.getAll()).thenReturn(List.of(new Question("MQ", "MA")));

        Collection<Question> result = examinerService.getQuestions(2);
        assertEquals(2, result.size());
    }

    @Test
    void getQuestions_ResultIsSetWithExpectedSize() {
        when(javaQuestionService.getAll()).thenReturn(List.of(
                new Question("JQ1", "JA1"),
                new Question("JQ2", "JA2")));
        when(mathQuestionService.getAll()).thenReturn(List.of(
                new Question("MQ1", "MA1")));

        Collection<Question> result = examinerService.getQuestions(2);

        assertInstanceOf(Set.class, result);   // ✅ результат — Set
        assertEquals(2, result.size());        // ✅ проверка размера
    }

    @Test
    void getQuestions_AmountZero_ShouldReturnEmptySet() {
        when(javaQuestionService.getAll()).thenReturn(List.of(new Question("JQ", "JA")));
        when(mathQuestionService.getAll()).thenReturn(List.of());

        Collection<Question> result = examinerService.getQuestions(0);

        assertTrue(result.isEmpty());
    }

    @Test
    void getQuestions_AmountZero_ShouldReturnEmpty() {
        when(javaQuestionService.getAll()).thenReturn(List.of(new Question("JQ", "JA")));
        when(mathQuestionService.getAll()).thenReturn(List.of());

        Collection<Question> result = examinerService.getQuestions(0);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}