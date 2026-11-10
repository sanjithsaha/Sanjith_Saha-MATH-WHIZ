# SANJITH SAHA – MATH WHIZ

**Student:** Sanjith Saha  **ID:** IT24044

A web-based mathematical game built with **Java Servlet/JSP, JDBC and MySQL**. The player enters a name, answers four maths questions (Prime, Modulus and two Trigonometry), gets a score out of **10**, and the result is saved in a database and shown on a **Top 10 leaderboard**.

All correct answers are calculated by Java methods at run time. No answers are stored manually.

## Project Objectives

- Build reusable Java methods for mathematical calculations.
- Process requests and responses using Java Servlets.
- Connect to a MySQL database with JDBC and `PreparedStatement`.
- Store, retrieve, sort and display records (Top 10 leaderboard).
- Document and submit a complete project on GitHub.

## Technologies Used

| Area | Technology |
|------|------------|
| Language | Java 17 |
| Web | Jakarta Servlet 6, JSP, JSTL |
| Server | Apache Tomcat 10.1 |
| Database | MySQL 8 + JDBC (mysql-connector-j) |
| Build | Maven |

## Software Requirements

- JDK 17 or later
- Apache Tomcat 10.1 or later (Jakarta EE 10)
- MySQL 8 or later
- Maven 3.8 or later (or an IDE with Maven support)

## Project Structure

```
MathWhiz/
├── pom.xml
├── database/math_whiz_db.sql
├── screenshots/
└── src/
    ├── main/java/
    │   ├── controller/GameServlet.java    # /start, /submit, /result, /leaderboard
    │   ├── model/GameScore.java           # one row of game_scores
    │   ├── model/Question.java            # one game question
    │   ├── service/MathService.java       # isPrime, calculateMod, calculateSin/Cos/Tan
    │   ├── service/GameService.java       # creates questions, validates, scores
    │   └── dao/
    │       ├── DBUtil.java                # JDBC connection
    │       └── ScoreDAO.java              # INSERT / SELECT (PreparedStatement)
    ├── main/resources/db.properties
    ├── main/webapp/
    │   ├── index.jsp  game.jsp  result.jsp  leaderboard.jsp
    │   ├── css/style.css
    │   └── WEB-INF/web.xml
    └── test/java/service/MathServiceTest.java
```

## Database Configuration

Run `database/setup_all.sql` once. It creates the database, the table and a MySQL user `mathwhiz` (password `MathWhiz@123`, for local lab use only). `src/main/resources/db.properties` already uses this user. If you prefer your own user, edit the file:

```properties
db.url=jdbc:mysql://localhost:3306/math_whiz_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=mathwhiz
db.password=MathWhiz@123
```

## Database Creation SQL

File: `database/math_whiz_db.sql`

```sql
CREATE DATABASE IF NOT EXISTS math_whiz_db;
USE math_whiz_db;

CREATE TABLE IF NOT EXISTS game_scores (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    player_name VARCHAR(100) NOT NULL,
    score       INT          NOT NULL,
    difficulty  VARCHAR(20)  NOT NULL DEFAULT 'NORMAL',
    played_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

## Steps to Run the Application

1. Create the database and user: `sudo mysql < database/setup_all.sql` (or `mysql -u root -p < database/setup_all.sql`)
2. `src/main/resources/db.properties` is already configured for that user.
3. Build the project: `mvn clean package`
4. Copy `target/MathWhiz.war` into Tomcat's `webapps/` folder and start Tomcat.
5. Open `http://localhost:8080/MathWhiz/` in a browser.

Run the math tests (Maven not needed):

```
mkdir -p out
javac -d out src/main/java/model/Question.java src/main/java/service/*.java src/test/java/service/MathServiceTest.java
java -cp out service.MathServiceTest
```

## Math Methods (`MathService`)

```java
public static int isPrime(int number)          // 1 = prime, 0 = not prime
public static int calculateMod(int a, int b)   // a % b, divisor 0 -> IllegalArgumentException
public static double calculateSin(double degree)
public static double calculateCos(double degree)
public static double calculateTan(double degree) // NaN where tan is undefined (90, 270)
```

## Game Rules and Scoring System

| Question | Area | Marks |
|----------|------|-------|
| Q1 | Prime Number (1 = Prime, 0 = Not Prime) | 2 |
| Q2 | Modulus (integer operands only) | 2 |
| Q3 | Trigonometry (sin / cos / tan) | 3 |
| Q4 | Trigonometry (sin / cos / tan) | 3 |
| **Total** | | **10** |

- Prime: returns 1 if prime, otherwise 0. Numbers less than 2 are not prime.
- Modulus: integers only. The divisor must not be zero.
- Trigonometry: angle in degrees is converted to radians, then `Math.sin()`, `Math.cos()` or `Math.tan()` is used. Decimal answers are compared with a tolerance of ±0.01.
- Answers must be valid (1/0 for prime, whole number for modulus, number for trigonometry); otherwise the game page shows an error message.
- Difficulty: `NORMAL` (saved in the database with every score).

```java
int totalScore = 0;
if (primeCorrect) totalScore += 2;
if (modCorrect)   totalScore += 2;
if (trig1Correct) totalScore += 3;
if (trig2Correct) totalScore += 3;
```

## Top 10 Leaderboard

Shows player name, score, difficulty and date/time. Highest score first; for equal scores the earlier date/time ranks higher.

```sql
SELECT * FROM game_scores
ORDER BY score DESC, played_at ASC
LIMIT 10;
```

## Testing

`MathServiceTest` checks: `isPrime(7)=1`, `isPrime(8)=0`, `isPrime(1)=0`, `17 % 5 = 2`, `sin(30°)=0.5`, `cos(60°)=0.5`, `tan(45°)=1.0`, divisor `0` gives an error, all correct answers give 10 marks, wrong answers give no marks.

## Application Screenshots

### Welcome Page
![Welcome Page](screenshots/home.png)

### Game Page
![Game Page](screenshots/game.png)

### Result Page
![Result Page](screenshots/result.png)

### Leaderboard
![Leaderboard](screenshots/leaderboard.png)

## GitHub Project Directory Link

https://github.com/YOUR_USERNAME/YOUR_REPOSITORY/tree/main/MathWhiz

## Acknowledgement

The overall project structure was referenced from a classmate's public repository (SWACHHA-MATHWHIZ). The code in this repository was written separately for this submission.
