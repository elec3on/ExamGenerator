package org.skypro.ExamGenerator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.ExamGenerator.model.Question;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExaminerServiceImplTest {

    @Mock
    private QuestionService javaQuestionService;

    @Mock
    private QuestionService mathQuestionService;

    private ExaminerServiceImpl examinerService;

    @BeforeEach
    void setUp() {
        examinerService = new ExaminerServiceImpl(javaQuestionService,mathQuestionService);
    }

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
        verify(javaQuestionService).getAll();
        verify(mathQuestionService).getAll();
    }

    @Test
    void getQuestions_ShouldReturnUniqueQuestions() {
        Question shared = new Question("Shared", "SharedAnswer");
        Question javaOnly = new Question("JQ", "JA");
        when(javaQuestionService.getAll()).thenReturn(List.of(shared, javaOnly));
        when(mathQuestionService.getAll()).thenReturn(List.of(shared));

        Collection<Question> result = examinerService.getQuestions(2);
        assertEquals(Set.of(shared, javaOnly), new HashSet<>(result));
    }

    @Test
    void getQuestions_ShouldThrowWhenAmountTooLarge() {
        when(javaQuestionService.getAll()).thenReturn(List.of(
                new Question("JQ", "JA")));
        when(mathQuestionService.getAll()).thenReturn(List.of(
                new Question("MQ", "MA")));

        assertThrows(ResponseStatusException.class, () -> examinerService.getQuestions(5));
    }

    @Test
    void getQuestions_ShouldReturnAllWhenAmountEqualsTotal() {
        Question jq = new Question("JQ", "JA");
        Question mq = new Question("MQ", "MA");
        when(javaQuestionService.getAll()).thenReturn(List.of(jq));
        when(mathQuestionService.getAll()).thenReturn(List.of(mq));

        Collection<Question> result = examinerService.getQuestions(2);

        assertEquals(Set.of(jq, mq), new HashSet<>(result));
    }

    @Test
    void getQuestions_ResultIsSetWithExpectedSize() {
        when(javaQuestionService.getAll()).thenReturn(List.of(
                new Question("JQ1", "JA1"),
                new Question("JQ2", "JA2")));
        when(mathQuestionService.getAll()).thenReturn(List.of(
                new Question("MQ1", "MA1")));

        Collection<Question> result = examinerService.getQuestions(2);
        assertInstanceOf(Set.class, result);
        assertEquals(2, result.size());
    }

    @Test
    void getQuestions_AmountZero_ShouldReturnEmptySet() {
        Collection<Question> result = examinerService.getQuestions(0);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getQuestions_NegativeAmount_ShouldThrow() {
        assertThrows(ResponseStatusException.class,
                () -> examinerService.getQuestions(-1));
    }
}