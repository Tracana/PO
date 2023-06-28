package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.app.exception.UnknownTransactionKeyException;
import ggc.core.exception.NoIDException;

/* Show specific transaction */
public class DoShowTransaction extends Command<WarehouseManager> {

  public DoShowTransaction(WarehouseManager receiver) {
    /*Constructor*/
    super(Label.SHOW_TRANSACTION, receiver);
    addIntegerField("id", Message.requestTransactionKey());
  }

  @Override
  public final void execute() throws CommandException, UnknownTransactionKeyException {
    try{
      _display.popup(_receiver.showTransaction(integerField("id")));
    } catch(NoIDException nie){
      throw new UnknownTransactionKeyException(integerField("id"));
    }
  }

}
