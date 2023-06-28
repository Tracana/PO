package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.app.exception.InvalidDateException;
import ggc.core.exception.InvalidTimeException;

/*Advance current date*/
class DoAdvanceDate extends Command<WarehouseManager> {

  DoAdvanceDate(WarehouseManager receiver) {
    /*Constructor*/
    super(Label.ADVANCE_DATE, receiver);
    addIntegerField("timeAdd", Message.requestDaysToAdvance());
  }

  @Override
  public final void execute() throws CommandException, InvalidDateException {
    /*executed when this option is selected*/
    try {
      _receiver.advanceDays(integerField("timeAdd"));
    } catch (InvalidTimeException e) {
      throw new InvalidDateException(integerField("timeAdd"));
    }
  }
}