package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.exception.NoIDException;
import ggc.app.exception.UnknownPartnerKeyException;
import java.util.ArrayList;
import java.util.List;
import ggc.core.Sale;

/* Show all transactions for a specific partner */
class DoShowPartnerSales extends Command<WarehouseManager> {


  DoShowPartnerSales(WarehouseManager receiver) {
    /*Constructor*/
    super(Label.SHOW_PARTNER_SALES, receiver);
    addStringField("idPartner", Message.requestPartnerKey());
  }

  @Override
  public void execute() throws CommandException, UnknownPartnerKeyException {
    /*executed when this option is selected*/
    List<Sale> _sales = new ArrayList<>();
    try{
      _sales = _receiver.showPartnerSalesBreakdownSales(stringField("idPartner"));
      for(Object s : _sales){
        _display.addLine(s.toString());
      }
      _display.display();
    }catch(NoIDException nie){
      throw new UnknownPartnerKeyException(stringField("idPartner"));
    }
  }

}
