package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.exception.NoIDException;
import ggc.app.exception.UnknownProductKeyException;
import java.util.ArrayList;
import java.util.List;

/* Show all products */
class DoShowBatchesByProduct extends Command<WarehouseManager> {

  List<String> _batches = new ArrayList<>();

  DoShowBatchesByProduct(WarehouseManager receiver) {
    /*Constructor*/
    super(Label.SHOW_BATCHES_BY_PRODUCT, receiver);
    addStringField("idProduct", Message.requestProductKey());
  }

  @Override
  public final void execute() throws CommandException {
    /*executed when this option is selected*/
    try{
      _batches = _receiver.showBatchesByProduct(stringField("idProduct"));
      for(String b : _batches){
        _display.addLine(b);
      }
      _display.display();
    }catch(NoIDException nie){
      throw new UnknownProductKeyException(stringField("idProduct"));
    } 
  }
}
