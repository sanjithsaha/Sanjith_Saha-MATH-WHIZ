package controller;

import dao.ScoreDAO;
import model.GameScore;
import model.Question;
import service.GameService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Handles the whole game:
 *   POST /start        -> check name, create 4 questions, show game page
 *   POST /submit       -> validate answers, calculate score, save to DB, redirect to result
 *   GET  /result       -> show the last result
 *   GET  /leaderboard  -> show top 10 scores
 */
@WebServlet(urlPatterns = {"/start", "/submit", "/result", "/leaderboard"})
public class GameServlet extends HttpServlet {

    private final ScoreDAO scoreDAO = new ScoreDAO();
    private final GameService gameService = new GameService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/leaderboard".equals(path)) {
            showLeaderboard(req, resp);
        } else if ("/result".equals(path)) {
            showResult(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();
        if ("/start".equals(path)) {
            startGame(req, resp);
        } else if ("/submit".equals(path)) {
            submitAnswers(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }
    }

    private void startGame(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("playerName");
        name = (name == null) ? "" : name.trim();

        if (name.isEmpty() || name.length() > 100) {
            req.setAttribute("error", "Please enter your name (1 to 100 characters).");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("playerName", name);
        session.setAttribute("questions", gameService.createQuestions());
        session.removeAttribute("result");
        resp.sendRedirect(req.getContextPath() + "/game.jsp");
    }

    @SuppressWarnings("unchecked")
    private void submitAnswers(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        List<Question> questions = (session == null) ? null
                : (List<Question>) session.getAttribute("questions");
        String name = (session == null) ? null : (String) session.getAttribute("playerName");
        if (questions == null || name == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        int n = questions.size();
        String[] given = new String[n];
        String[] errors = new String[n];
        boolean hasError = false;
        for (int i = 0; i < n; i++) {
            String raw = req.getParameter("answer" + i);
            given[i] = (raw == null) ? "" : raw.trim();
            errors[i] = GameService.validate(questions.get(i), given[i]);
            if (errors[i] != null) {
                hasError = true;
            }
        }

        // Invalid input: show the game page again with the messages
        if (hasError) {
            req.setAttribute("given", given);
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/game.jsp").forward(req, resp);
            return;
        }

        boolean[] correct = new boolean[n];
        String[] expected = new String[n];
        for (int i = 0; i < n; i++) {
            correct[i] = GameService.isCorrect(questions.get(i), given[i]);
            expected[i] = GameService.expectedAnswer(questions.get(i));
        }
        int totalScore = GameService.totalScore(questions, correct);

        try {
            scoreDAO.save(new GameScore(name, totalScore, GameService.DIFFICULTY));
        } catch (SQLException e) {
            throw new ServletException("Could not save the score", e);
        }

        // Keep the result in the session and redirect, so refreshing the page does not save twice
        session.setAttribute("result", new Object[]{totalScore, given, expected, correct, questions});
        session.removeAttribute("questions");
        resp.sendRedirect(req.getContextPath() + "/result");
    }

    private void showResult(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Object[] r = (session == null) ? null : (Object[]) session.getAttribute("result");
        if (r == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }
        req.setAttribute("playerName", session.getAttribute("playerName"));
        req.setAttribute("score", r[0]);
        req.setAttribute("given", r[1]);
        req.setAttribute("expected", r[2]);
        req.setAttribute("correct", r[3]);
        req.setAttribute("questions", r[4]);
        req.getRequestDispatcher("/result.jsp").forward(req, resp);
    }

    private void showLeaderboard(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("scores", scoreDAO.getTop10());
        } catch (SQLException e) {
            throw new ServletException("Could not load the leaderboard", e);
        }
        req.getRequestDispatcher("/leaderboard.jsp").forward(req, resp);
    }
}
