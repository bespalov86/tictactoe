package com.games.tictactoe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GameFieldTest {

  private GameField field = new GameField(5, 'a');

  @Test(expected = IncorrectStepException.class)
  public void stepIncorrect() throws IncorrectStepException {

    Step step = field.doStep(-1, -1, 'b');
  }

  @Test
  public void stepOk() throws IncorrectStepException {

    Step step = field.doStep(0, 0, 'b');
    assertNotNull(step);
    assertEquals(Step.Result.Ok, step.getResult());
  }

  @Test
  public void stepBusy() throws IncorrectStepException {

    Step step1 = field.doStep(1, 0, 'b');
    Step step2 = field.doStep(1, 0, 'c');
    assertEquals(Step.Result.Busy, step2.getResult());
  }

  @Test
  public void stepWinHorizontal() throws IncorrectStepException {

    GameField field = new GameField(3, 'a');

    Step step1 = field.doStep(0, 0, 'w');
    Step step2 = field.doStep(0, 1, 'w');
    Step step3 = field.doStep(0, 2, 'w');
    assertEquals(Step.Result.Win, step3.getResult());
  }

  @Test
  public void stepWinVertical() throws IncorrectStepException {

    GameField field = new GameField(3, 'a');
    Step step1 = field.doStep(0, 0, 'w');
    Step step2 = field.doStep(1, 0, 'w');
    Step step3 = field.doStep(2, 0, 'w');
    assertEquals(Step.Result.Win, step3.getResult());
  }

}
