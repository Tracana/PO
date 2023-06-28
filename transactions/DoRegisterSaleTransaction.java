package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.exception.TooMuchException;
import ggc.app.exception.UnavailableProductException;
import ggc.app.exception.UnknownPartnerKeyException;
import ggc.app.exception.UnknownProductKeyException;
import ggc.core.exception.NoIDException;
import ggc.core.exception.NoProductIdException;

/* Register order */
public class DoRegisterSaleTransaction extends Command<WarehouseManager> {

  public DoRegisterSaleTransaction(WarehouseManager receiver) {
    /*Constructor*/
    super(Label.REGISTER_SALE_TRANSACTION, receiver);
    addStringField("idPartner", Message.requestPartnerKey());
    addIntegerField("deadline", Message.requestPaymentDeadline());
    addStringField("idProduct", Message.requestProductKey());
    addIntegerField("amount", Message.requestAmount());
  }

  @Override
  public final void execute() throws CommandException, UnavailableProductException { 
    /*executed when this option is selected*/

    String idPartner = stringField("idPartner");
    int deadline = integerField("deadline");
    String idProduct =stringField("idProduct");
    int amount = integerField("amount");

    try {
      _receiver.isTherePartner(idPartner);
      _receiver.isThereProduct(idProduct);
      _receiver.registerSaleTransaction(idPartner, deadline, idProduct, amount);
    }catch(TooMuchException tme){
      throw new UnavailableProductException(idProduct, amount, _receiver.availableAmount(idProduct));
    }catch(NoIDException nide){
      throw new UnknownPartnerKeyException(nide.getNoIDException());
    }catch(NoProductIdException npie){
      throw new UnknownProductKeyException(npie.getNoProductIdException());
    }
  }

}
