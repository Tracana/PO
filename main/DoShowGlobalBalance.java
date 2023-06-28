package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;

/* Show global balance */
class DoShowGlobalBalance extends Command<WarehouseManager> {

  DoShowGlobalBalance(WarehouseManager receiver) {
    /*Constructor*/
    super(Label.SHOW_BALANCE, receiver);
  }

  @Override
  public final void execute() throws CommandException {
    /*executed when this option is selected*/
    _display.popup(Message.currentBalance(_receiver.getAvailableBalance(), _receiver.getAccountingBalance()));
  }
  
}
