# The-Game-of-Hex

An AI agent is created which plays the game of Hex using min-max and alpha-beta pruning to best his human counterparts.

The AI moves are evaluated using the evaluation function in which different heuristics are used to select the best move. The alpha-beta pruning helps in finding the best move faster.

# Directory Structure

<pre>
📦The-Game-of-Hex
┣ 📂Documents
┃ ┗ 📜The Game of Hex.pdf
┣ 📂out
┃ ┗ 📂production
┃ ┃ ┗ 📂AiHex
┃ ┃ ┃ ┗ 📂AiHex
┃ ┃ ┃ ┃ ┣ 📂gameMechanics
┃ ┃ ┃ ┃ ┃ ┣ 📜GameRunner.class
┃ ┃ ┃ ┃ ┃ ┣ 📜InvalidMoveException.class
┃ ┃ ┃ ┃ ┃ ┣ 📜Move.class
┃ ┃ ┃ ┃ ┃ ┣ 📜Runner.class
┃ ┃ ┃ ┃ ┃ ┗ 📜SeasonMechanics.class
┃ ┃ ┃ ┃ ┣ 📂graphical
┃ ┃ ┃ ┃ ┃ ┣ 📂boardPanels
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜HeatMap.class
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜HexDisplayOnlyPanel.class
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜HexGamePanel.class
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜HexGroupPanel.class
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜HexPanel.class
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜HexSelector.class
┃ ┃ ┃ ┃ ┃ ┣ 📜BoardSetupPanel.class
┃ ┃ ┃ ┃ ┃ ┣ 📜GUI.class
┃ ┃ ┃ ┃ ┃ ┗ 📜PlayerChoicePanel.class
┃ ┃ ┃ ┃ ┣ 📂hexBoards
┃ ┃ ┃ ┃ ┃ ┣ 📜AbstractBoard.class
┃ ┃ ┃ ┃ ┃ ┣ 📜AdjMatrix.class
┃ ┃ ┃ ┃ ┃ ┣ 📜Board.class
┃ ┃ ┃ ┃ ┃ ┣ 📜BoardData.class
┃ ┃ ┃ ┃ ┃ ┣ 📜GameBoard.class
┃ ┃ ┃ ┃ ┃ ┣ 📜HexLocation.class
┃ ┃ ┃ ┃ ┃ ┣ 📜OpenBoard.class
┃ ┃ ┃ ┃ ┃ ┗ 📜ScoreBoard.class
┃ ┃ ┃ ┃ ┣ 📂players
┃ ┃ ┃ ┃ ┃ ┣ 📜AbstractPlayer.class
┃ ┃ ┃ ┃ ┃ ┣ 📜AIPlayer.class
┃ ┃ ┃ ┃ ┃ ┣ 📜EvaluationFunction.class
┃ ┃ ┃ ┃ ┃ ┣ 📜Pair.class
┃ ┃ ┃ ┃ ┃ ┣ 📜Player.class
┃ ┃ ┃ ┃ ┃ ┗ 📜PointAndClickPlayer.class
┃ ┃ ┃ ┃ ┣ 📜Main$1.class
┃ ┃ ┃ ┃ ┗ 📜Main.class
┣ 📂src
┃ ┗ 📂AiHex
┃ ┃ ┣ 📂gameMechanics
┃ ┃ ┃ ┣ 📜GameRunner.java
┃ ┃ ┃ ┣ 📜InvalidMoveException.java
┃ ┃ ┃ ┣ 📜Move.java
┃ ┃ ┃ ┣ 📜Runner.java
┃ ┃ ┃ ┗ 📜SeasonMechanics.java
┃ ┃ ┣ 📂graphical
┃ ┃ ┃ ┣ 📂boardPanels
┃ ┃ ┃ ┃ ┣ 📜HeatMap.java
┃ ┃ ┃ ┃ ┣ 📜HexDisplayOnlyPanel.java
┃ ┃ ┃ ┃ ┣ 📜HexGamePanel.java
┃ ┃ ┃ ┃ ┣ 📜HexGroupPanel.java
┃ ┃ ┃ ┃ ┣ 📜HexPanel.java
┃ ┃ ┃ ┃ ┗ 📜HexSelector.java
┃ ┃ ┃ ┣ 📜BoardSetupPanel.java
┃ ┃ ┃ ┣ 📜GUI.java
┃ ┃ ┃ ┗ 📜PlayerChoicePanel.java
┃ ┃ ┣ 📂hexBoards
┃ ┃ ┃ ┣ 📜AbstractBoard.java
┃ ┃ ┃ ┣ 📜AdjMatrix.java
┃ ┃ ┃ ┣ 📜Board.java
┃ ┃ ┃ ┣ 📜BoardData.java
┃ ┃ ┃ ┣ 📜GameBoard.java
┃ ┃ ┃ ┣ 📜HexLocation.java
┃ ┃ ┃ ┣ 📜OpenBoard.java
┃ ┃ ┃ ┗ 📜ScoreBoard.java
┃ ┃ ┣ 📂players
┃ ┃ ┃ ┣ 📜AbstractPlayer.java
┃ ┃ ┃ ┣ 📜AIPlayer.java
┃ ┃ ┃ ┣ 📜EvaluationFunction.java
┃ ┃ ┃ ┣ 📜Pair.java
┃ ┃ ┃ ┣ 📜Player.java
┃ ┃ ┃ ┗ 📜PointAndClickPlayer.java
┃ ┃ ┗ 📜Main.java
┣ 📜.gitignore
┣ 📜AiHex.iml
┗ 📜README.md
</pre>
