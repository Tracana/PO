package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.app.exception.UnknownPartnerKeyException;
import ggc.core.exception.NoIDException;

/* Show partner */
class DoShowPartner extends Command<WarehouseManager> {

  DoShowPartner(WarehouseManager receiver) {
    /*Constructor*/
    super(Label.SHOW_PARTNER, receiver);
    addStringField("id", Message.requestPartnerKey());
  }

  @Override
  public void execute() throws CommandException, UnknownPartnerKeyException {
    /*executed when this option is selected*/
    try{
      _display.addLine(_receiver.showPartner(stringField("id")));
      for(String n : _receiver.showNotification(stringField("id"))){
        _display.addLine(n);
      }
      _display.display();
    } catch(NoIDException nie){
      throw new UnknownPartnerKeyException(stringField("id"));
    }
  }

}
