package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exception.UnknownPartnerKeyException;
import ggc.app.exception.UnknownProductKeyException;
import ggc.core.WarehouseManager;
import ggc.core.exception.NoIDException;
import ggc.core.exception.NoProductIdException;

/**
 * Toggle product-related notifications.
 */
class DoToggleProductNotifications extends Command<WarehouseManager> {

  DoToggleProductNotifications(WarehouseManager receiver) {
    /*Constructor*/
    super(Label.TOGGLE_PRODUCT_NOTIFICATIONS, receiver);
    addStringField("idPartner", Message.requestPartnerKey());
    addStringField("idProduct", Message.requestProductKey());
  }

  @Override
  public void execute() throws CommandException {
    /*executed when this option is selected*/
    String idPartner = stringField("idPartner");
    String idProduct = stringField("idProduct");

    try {
      _receiver.toggleProductNotifications(idProduct, idPartner);
    } catch (NoProductIdException e) {
      throw new UnknownProductKeyException(idProduct);
    }
    catch(NoIDException e1){
      throw new UnknownPartnerKeyException(idPartner);
    }
  }

}
