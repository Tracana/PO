package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.Acquisition;
import ggc.core.WarehouseManager;
import ggc.core.exception.NoIDException;
import ggc.app.exception.UnknownPartnerKeyException;
import java.util.ArrayList;
import java.util.List;

/* Show all transactions for a specific partner */
class DoShowPartnerAcquisitions extends Command<WarehouseManager> {

  DoShowPartnerAcquisitions(WarehouseManager receiver) {
    /*Constructor*/
    super(Label.SHOW_PARTNER_ACQUISITIONS, receiver);
    addStringField("idPartner", Message.requestPartnerKey());
  }

  @Override
  public void execute() throws CommandException, UnknownPartnerKeyException {
    /*executed when this option is selected*/
    List<Acquisition> _acquisitions = new ArrayList<>();
    try{
      _acquisitions = _receiver.showPartnerAcquisitions(stringField("idPartner"));
      for(Object a : _acquisitions){
        _display.addLine(a.toString());
      }
      _display.display();
    }catch(NoIDException nie){
      throw new UnknownPartnerKeyException(stringField("idPartner"));
    }
  }

}
