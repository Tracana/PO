package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.exception.NoIDException;
import ggc.core.exception.NoProductIdException;
import ggc.core.exception.TooMuchException;
import ggc.app.exception.UnavailableProductException;
import ggc.app.exception.UnknownPartnerKeyException;
import ggc.app.exception.UnknownProductKeyException;

/* Register order */
public class DoRegisterBreakdownTransaction extends Command<WarehouseManager> {

  public DoRegisterBreakdownTransaction(WarehouseManager receiver) {
    /*Constructor*/
    super(Label.REGISTER_BREAKDOWN_TRANSACTION, receiver);
    addStringField("idPartner", Message.requestPartnerKey());
    addStringField("idProduct", Message.requestProductKey());
    addIntegerField("amount", Message.requestAmount());
  }

  @Override
  public final void execute() throws CommandException, UnavailableProductException {
    /*executed when this option is selected*/
    String idPartner = stringField("idPartner");
    String idProduct =stringField("idProduct");
    int amount = integerField("amount");

    try {
      try {
        _receiver.registerBreakdownTransaction(idPartner, idProduct, amount);
      } catch (NoIDException nie) {
        throw new UnknownPartnerKeyException(idPartner);
      } catch (NoProductIdException npie) {
        throw new UnknownProductKeyException(idProduct);
      }
    }catch(TooMuchException tme){
      throw new UnavailableProductException(idProduct, amount, _receiver.availableAmount(idProduct));
    }
    
  }

}
