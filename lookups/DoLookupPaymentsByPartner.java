package ggc.app.lookups;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.exception.NoIDException;
import ggc.app.exception.UnknownPartnerKeyException;

import java.util.ArrayList;
import java.util.List;

import ggc.core.Sale;
import ggc.core.WarehouseManager;

/* Lookup payments by given partner */
public class DoLookupPaymentsByPartner extends Command<WarehouseManager> {

  List<Sale> _transactions = new ArrayList<>();

  public DoLookupPaymentsByPartner(WarehouseManager receiver) {
    /*Constructor*/
    super(Label.PAID_BY_PARTNER, receiver);
    addStringField("idPartner", Message.requestPartnerKey());
  }

  @Override
  public void execute() throws CommandException, UnknownPartnerKeyException{
    /*executed when this option is selected*/
    try{
      _transactions = _receiver.getPaymentsByPartner(stringField("idPartner"));
      for(Sale s : _transactions){
        _display.addLine(s.toString());
      }
      _display.display();
    }catch(NoIDException nie){
      throw new UnknownPartnerKeyException(stringField("idPartner"));
    }
  }

}
