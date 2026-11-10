package dao;

import model.GameScore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** INSERT and SELECT operations for the game_scores table (PreparedStatement only). */
public class ScoreDAO {

    private static final String INSERT_SQL =
            "INSERT INTO game_scores (player_name, score, difficulty) VALUES (?, ?, ?)";

    private static final String TOP10_SQL =
            "SELECT id, player_name, score, difficulty, played_at "
          + "FROM game_scores ORDER BY score DESC, played_at ASC LIMIT 10";

    /** Saves one game. played_at is filled by the database (CURRENT_TIMESTAMP). */
    public void save(GameScore gs) throws SQLException {
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {
            ps.setString(1, gs.getPlayerName());
            ps.setInt(2, gs.getScore());
            ps.setString(3, gs.getDifficulty());
            ps.executeUpdate();
        }
    }

    /** Highest 10 scores; earlier time wins when scores are equal. */
    public List<GameScore> getTop10() throws SQLException {
        List<GameScore> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(TOP10_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                GameScore gs = new GameScore();
                gs.setId(rs.getInt("id"));
                gs.setPlayerName(rs.getString("player_name"));
                gs.setScore(rs.getInt("score"));
                gs.setDifficulty(rs.getString("difficulty"));
                gs.setPlayedAt(rs.getTimestamp("played_at"));
                list.add(gs);
            }
        }
        return list;
    }
}
