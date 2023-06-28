package ggc.app.main;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.app.exception.FileOpenFailedException;
import ggc.core.exception.MissingFileAssociationException;
import java.io.IOException;

/* Save current state to file under current name (if unnamed, query for name) */
class DoSaveFile extends Command<WarehouseManager> {

  /** @param receiver */
  DoSaveFile(WarehouseManager receiver) {
    /* Constructor */
    super(Label.SAVE, receiver);
  }

  @Override
  public final void execute() throws CommandException, FileOpenFailedException{
    /*executed when this option is selected*/
    if(_receiver.getFilename().equals("")){
      try{ 
        String filename = Form.requestString(Message.newSaveAs());
        _receiver.saveAs(filename);
      }catch (IOException | MissingFileAssociationException ie){
        ie.printStackTrace();
      }

    }
    try{
      _receiver.save();
    }catch (IOException | MissingFileAssociationException e){
      e.printStackTrace();
    }
  }

}
