package engine.service;

import engine.domain.SolvedQuizInfo;
import engine.domain.Quiz;
import engine.domain.User;
import engine.dto.Response;
import engine.repo.QuizRepo;
import engine.repo.SolvedQuizRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Service
public class QuizService {
    final int PAGE_SIZE = 10;
    private final QuizRepo quizRepo;
    private final SolvedQuizRepo solvedQuizRepo;

    public QuizService(QuizRepo quizRepo, SolvedQuizRepo solvedQuizRepo) {
        this.quizRepo = quizRepo;
        this.solvedQuizRepo = solvedQuizRepo;
    }

    public Page<Quiz> getAllQuizzes(int page) {
        PageRequest paging = PageRequest.of(page, PAGE_SIZE, Sort.by("id"));

        return quizRepo.findAll(paging);
    }

    public Page<SolvedQuizInfo> getAllSolvedQuizzesByUser(User user, int page) {
        PageRequest paging = PageRequest.of(page, PAGE_SIZE, Sort.by("completedAt").descending());
        return solvedQuizRepo.findAllByUser(user, paging);
    }

    public Optional<Quiz> findQuizById(Long id) {
        return quizRepo.findById(id);
    }

    public Quiz addQuiz(Quiz quiz, User user) {
        quiz.setUser(user);
        return quizRepo.save(quiz);
    }

    public void deleteQuiz(Quiz quiz) {
        quizRepo.delete(quiz);
    }

    public Response solveQuiz(Quiz quiz, Map<String, TreeSet<Integer>> answer, User user) {
        Set<Integer> inAnswer = answer.get("answer");
        Set<Integer> quizAnswer = new TreeSet<>(quiz.getAnswer());
        if(inAnswer.equals(quizAnswer)) {
            solvedQuizRepo.save(new SolvedQuizInfo(user, quiz.getId(), LocalDateTime.now()));
            return new Response(true, "Congratulations, you're right!");
        }
        else {
            return new Response(false, "Wrong answer! Please, try again.");
        }
    }
}
