package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;

/* Show current date */
class DoDisplayDate extends Command<WarehouseManager> {

  DoDisplayDate(WarehouseManager receiver) {
    /*Constructor*/
    super(Label.SHOW_DATE, receiver);
  }

  @Override
  public final void execute() throws CommandException {
    /*executed when this option is selected*/
    _display.popup(Message.currentDate(_receiver.displayDate()));
  }

}
