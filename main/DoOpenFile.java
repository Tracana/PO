package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;


import ggc.app.exception.FileOpenFailedException;
import ggc.core.exception.UnavailableFileException;
import ggc.core.WarehouseManager;

/* Open existing saved state */
class DoOpenFile extends Command<WarehouseManager> {

  /** @param receiver */
  DoOpenFile(WarehouseManager receiver) {
    /* Constructor */
    super(Label.OPEN, receiver);
    addStringField("filename", Message.openFile());
  }

  @Override
  public final void execute() throws CommandException{
    /*executed when this option is selected*/
    try {
      String filename = stringField("filename");
      _receiver.load(filename);
    } catch (UnavailableFileException fnfe) {
      throw new FileOpenFailedException(fnfe.getFilename());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

}
