package ggc.app.transactions;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exception.UnknownPartnerKeyException;
import ggc.app.exception.UnknownProductKeyException;
import ggc.core.Recipe;
import ggc.core.WarehouseManager;
import ggc.core.exception.NoIDException;
import ggc.core.exception.NoProductIdException;

import java.util.ArrayList;
import java.util.List;

/* Register order */
public class DoRegisterAcquisitionTransaction extends Command<WarehouseManager> {

  public DoRegisterAcquisitionTransaction(WarehouseManager receiver) {
    /*Constructor*/
    super(Label.REGISTER_ACQUISITION_TRANSACTION, receiver);
    addStringField("idPartner", Message.requestPartnerKey());
    addStringField("idProduct", Message.requestProductKey());
    addRealField("price", Message.requestPrice());
    addIntegerField("amount", Message.requestAmount());
  }

  @Override
  public final void execute() throws CommandException {
    /*executed when this option is selected*/

    String idPartner = stringField("idPartner");
    String idProduct = stringField("idProduct");
    double price = realField("price");
    int amount = integerField("amount");
    boolean notification = true;

    try{
      _receiver.isTherePartner(idPartner);
      _receiver.isThereProduct(idProduct);
      _receiver.registerAcquisitionTransaction(idPartner, idProduct, price, amount, notification);
    } catch(NoProductIdException e) {
      notification = false;
      String bool = Form.requestString(Message.requestAddRecipe());

      if(bool.toLowerCase().equals("s")){
        int numberComponents = Form.requestInteger(Message.requestNumberOfComponents());
        double alpha = Form.requestReal(Message.requestAlpha());
        List<String> components = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();

        do{
          String component = Form.requestString(Message.requestProductKey());
          int quantityComp = Form.requestInteger(Message.requestAmount());
          components.add(component);
          quantities.add(quantityComp);
          numberComponents--;
        }while(numberComponents > 0);
        try {
          Recipe recipe = _receiver.registerRecipe(alpha, components, quantities);
          _receiver.registerProductC(idProduct, recipe);
          _receiver.registerAcquisitionTransaction(idPartner, idProduct, price, amount, notification);
        } catch (NoProductIdException e1) {
          throw new UnknownProductKeyException(e1.getNoProductIdException());
        }
      }else{
        _receiver.registerProductS(idProduct);
        try {
          _receiver.registerAcquisitionTransaction(idPartner, idProduct, price, amount, notification);
        } catch (NoProductIdException e1) {
          // Should never go here
          e1.printStackTrace();
        }
      }
    } catch (NoIDException e) {
      throw new UnknownPartnerKeyException(e.getNoIDException());
    }
  }
}
