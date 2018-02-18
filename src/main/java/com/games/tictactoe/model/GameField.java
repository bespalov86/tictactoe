package com.games.tictactoe.model;

import java.util.Arrays;

class GameField {

  private static final String NO_SUCH_POSITION_MESSAGE = "No such position";

  private final int size;
  private final char freeSymbol;
  private final char[][] field;

  public GameField(int size, char freeSymbol) {

    this.size = size;
    this.freeSymbol = freeSymbol;

    field = new char[size][size];
    for (char[] row : field) {
      Arrays.fill(row, freeSymbol);
    }
  }

  /**
   * @return two dimensional game field
   */
  public char[][] getField() {
    return field;
  }

  /**
   * Returns Step object containing data about result of step.
   * 
   * @param row
   *          row of step
   * @param col
   *          column of step
   * @param symbol
   *          symbol of step
   * @return {@link com.games.tictactoe.model.Step}
   * @throws IncorrectStepException
   *           if step position incorrect
   */
  public Step doStep(int row, int col, char symbol) throws IncorrectStepException {

    if (row < 0 || row >= size || col < 0 || col >= size) {
      throw new IncorrectStepException(NO_SUCH_POSITION_MESSAGE);
    }

    if (field[row][col] != freeSymbol) {
      return new Step(Step.Result.Busy);
    }

    Step result = null;
    field[row][col] = symbol;
    if (isStepWin(row, col, symbol)) {
      result = new Step(Step.Result.Win);
    } else if (isDraw()) {
      result = new Step(Step.Result.Draw);
    } else {
      result = new Step(Step.Result.Ok);
    }

    return result;
  }

  private boolean isStepWin(int row, int col, char symbol) {

    return isHorizontalWin(row, symbol) || isVerticalWin(col, symbol)
        || isLeftRightDiagonalWin(symbol) || isRightLeftDiagonalWin(symbol); // TODO add check for row and col
  }

  private boolean isHorizontalWin(int row, char symbol) {

    for (int col = 0; col < size; col++) {
      if (field[row][col] != symbol) {
        return false;
      }
    }

    return true;
  }

  private boolean isVerticalWin(int col, char symbol) {

    for (int row = 0; row < size; row++) {
      if (field[row][col] != symbol) {
        return false;
      }
    }

    return true;
  }

  private boolean isLeftRightDiagonalWin(char symbol) {

    for (int i = 0; i < size; i++) {
      if (field[i][i] != symbol) {
        return false;
      }
    }

    return true;
  }

  private boolean isRightLeftDiagonalWin(char symbol) {

    for (int i = 0; i < size; i++) {
      if (field[i][size - i - 1] != symbol) {
        return false;
      }
    }

    return true;
  }

  /**
   * search free symbol in other algorithm we can e.g. count steps
   */
  private boolean isDraw() {

    for (char[] row : field) {
      for (char c : row) {
        if (c == freeSymbol) {
          return false;
        }
      }
    }

    return true;
  }
}
