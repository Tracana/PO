package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.app.exception.DuplicatePartnerKeyException;
import ggc.core.exception.BadEntryException;

/* Register new partner */
class DoRegisterPartner extends Command<WarehouseManager> {

  DoRegisterPartner(WarehouseManager receiver) {
    /*Constructor*/
    super(Label.REGISTER_PARTNER, receiver);
    addStringField("id", Message.requestPartnerKey());
    addStringField("name", Message.requestPartnerName());
    addStringField("address", Message.requestPartnerAddress());
  }

  @Override
  public void execute() throws CommandException, DuplicatePartnerKeyException {
    /*executed when this option is selected*/
    try{
      _receiver.registerPartner(stringField("id"), stringField("name"),stringField("address"));
    } catch(BadEntryException bee){
      throw new DuplicatePartnerKeyException(stringField("id"));
    }
  }

}
