package engine.controller;

import engine.domain.SolvedQuizInfo;
import engine.domain.Quiz;
import engine.domain.User;
import engine.dto.Response;
import engine.service.QuizService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@Validated
public class QuizController {
    QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/api/quizzes")
    public Page<Quiz> getAllQuizzes(@RequestParam(defaultValue = "0") Integer page) {
        return quizService.getAllQuizzes(page);
    }

    @GetMapping("/api/quizzes/{id}")
    public Quiz getQuizById(@PathVariable Long id) {
        return quizService.findQuizById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found"));
    }

    @GetMapping("/api/quizzes/completed")
    public Page<SolvedQuizInfo> getAllSolvedQuizzesByUser(@AuthenticationPrincipal User user,
                                                          @RequestParam(defaultValue = "0") Integer page) {
        return quizService.getAllSolvedQuizzesByUser(user, page);
    }

    @PostMapping("/api/quizzes")
    public Quiz addQuiz(@RequestBody @Valid Quiz quiz, @AuthenticationPrincipal User user) {
        return quizService.addQuiz(quiz, user);
    }

    @PostMapping("/api/quizzes/{id}/solve")
    public Response solveQuiz(@PathVariable Long id, @RequestBody Map<String, TreeSet<Integer>> answer,
                              @AuthenticationPrincipal User user) {
        Quiz quiz = quizService.findQuizById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return quizService.solveQuiz(quiz, answer, user);
    }

    @DeleteMapping("/api/quizzes/{id}")
    public void deleteQuiz(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Quiz quiz = quizService.findQuizById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(quiz.getUser().equals(user)) {
            quizService.deleteQuiz(quiz);
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permissions");
        }
    }
}
