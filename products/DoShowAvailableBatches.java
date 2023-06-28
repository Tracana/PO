package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.Batch;
/* Show available batches */
class DoShowAvailableBatches extends Command<WarehouseManager> {

  DoShowAvailableBatches(WarehouseManager receiver) {
    /*Constructor*/
    super(Label.SHOW_AVAILABLE_BATCHES, receiver);
  }

  @Override
  public final void execute() throws CommandException {
    /*executed when this option is selected*/
    for(Batch b : _receiver.showAllAvailableBatches())
      _display.addLine(b);
    _display.display();
    
  }
}
