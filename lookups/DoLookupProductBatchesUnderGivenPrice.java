package ggc.app.lookups;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import java.util.ArrayList;
import java.util.List;


/* Lookup products cheaper than a given price */
public class DoLookupProductBatchesUnderGivenPrice extends Command<WarehouseManager> {

  List<String> _batches = new ArrayList<>();

  public DoLookupProductBatchesUnderGivenPrice(WarehouseManager receiver) {
    /*Constructor*/
    super(Label.PRODUCTS_UNDER_PRICE, receiver);
    addIntegerField("givenPrice", Message.requestPriceLimit());
  }

  @Override
  public void execute() throws CommandException {
    /*executed when this option is selected*/
    _batches = _receiver.showProductBatchesUnderGivenPrice(integerField("givenPrice"));
      for(String b : _batches){
        _display.addLine(b);
      }
      _display.display();
  }

}
