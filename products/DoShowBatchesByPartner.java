package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.exception.NoIDException;
import ggc.app.exception.UnknownPartnerKeyException;
import java.util.ArrayList;
import java.util.List;

/* Show batches supplied by partner */
class DoShowBatchesByPartner extends Command<WarehouseManager> {

  List<String> _batches = new ArrayList<>();

  DoShowBatchesByPartner(WarehouseManager receiver) {
    /*Constructor*/
    super(Label.SHOW_BATCHES_SUPPLIED_BY_PARTNER, receiver);
    addStringField("idPartner", Message.requestPartnerKey());
  }

  @Override
  public final void execute() throws CommandException, UnknownPartnerKeyException{
    /*executed when this option is selected*/
    try{
      _batches = _receiver.showBatchesByPartner(stringField("idPartner"));
      for(String b : _batches){
        _display.addLine(b);
      }
      _display.display();
    }catch(NoIDException nie){
      throw new UnknownPartnerKeyException(stringField("idPartner"));
    }
  }
}
