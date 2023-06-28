package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.exception.NoIDException;
import ggc.app.exception.UnknownTransactionKeyException;

/* Receive payment for sale transaction */
public class DoReceivePayment extends Command<WarehouseManager> {

  public DoReceivePayment(WarehouseManager receiver) {
    /*Constructor*/
    super(Label.RECEIVE_PAYMENT, receiver);
    addIntegerField("id", Message.requestTransactionKey());
  }

  @Override
  public final void execute() throws CommandException, UnknownTransactionKeyException {
    /*executed when this option is selected*/
    try{
      _receiver.pay(integerField("id"));
    }catch(NoIDException nie){
      throw new UnknownTransactionKeyException(integerField("id"));
    }
  }

}
