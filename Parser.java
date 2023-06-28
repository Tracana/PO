package ggc.core;

import java.io.Serializable;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import ggc.core.exception.BadEntryException;
import ggc.core.exception.NoIDException;

public class Parser implements Serializable{

  /* Serial number for serialization */
  private static final long serialVersionUID = 202109192006L;
  /*Parser's store*/
  private Warehouse _store;

  /**
   * @param w
   */
  public Parser(Warehouse w) {
    _store = w;
  }

  /**
   * @param filemname
   * @throws IOException
   * @throws BadEntryException
   */
  void parseFile(String filename) throws IOException, BadEntryException, NoIDException {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;

      while ((line = reader.readLine()) != null)
        parseLine(line);
      
    }
  }

  /**
   * @param line
   * @throws BadEntryException
   * @throws NoIDException
   */
  private void parseLine(String line) throws BadEntryException, NoIDException {
    String[] components = line.split("\\|");

    switch (components[0]) {
      case "PARTNER":
        parsePartner(components, line);
        break;
      case "BATCH_S":
        parseSimpleProduct(components, line);
        break;

      case "BATCH_M":
        parseAggregateProduct(components, line);
        break;
        
      default:
        throw new BadEntryException("Invalid type element: " + components[0]);
    }
  }

  /**
   * @param components
   * @param line
   * @throws BadEntryException
   */
  private void parsePartner(String[] components, String line) throws BadEntryException {
    if (components.length != 4)
      throw new BadEntryException("Invalid partner with wrong number of fields (4): " + line);
    
    String id = components[1];
    String name = components[2];
    String address = components[3];
    
    try{
        _store.registerPartner(id, name, address);
    }
    catch(BadEntryException bee){
        throw new BadEntryException("Id already exists");
    }
  }

  /**
   * @param components
   * @param line
   * @throws BadEntryException
   */
  private void parseSimpleProduct(String[] components, String line) throws NoIDException, BadEntryException {
    Product product = null;
    if (components.length != 5)
      throw new BadEntryException("Invalid number of fields (4) in simple batch description: " + line);
    
    String idProduct = components[1];
    String idPartner = components[2];

    Partner partner = _store.getPartnerByID(idPartner);
    double price = Double.parseDouble(components[3]);
    int stock = Integer.parseInt(components[4]);

    if (!(_store.getProducts().containsKey(idProduct))) { 
      _store.registerProductS(idProduct);
    } 
    product = _store.getProductByID(idProduct);
    _store.registerBatchS(product, partner, price, stock);

  }
 
  /**
   * @param components
   * @param line
   * @throws BadEntryException
   */
  private void parseAggregateProduct(String[] components, String line) throws NoIDException, BadEntryException{
    if (components.length != 7)
      throw new BadEntryException("Invalid number of fields (7) in aggregate batch description: " + line);

    String idProduct = components[1];
    String idPartner = components[2];
    double price = Double.parseDouble(components[3]);
    int stock = Integer.parseInt(components[4]);
    double alpha = Double.parseDouble(components[5]);

    Product product = null;
    Partner partner = _store.getPartnerByID(idPartner);

    List<Integer> quantities = new ArrayList<>();
    List<Product> products = new ArrayList<>();

    for (String component : components[6].split("#")) {
        String[] recipeComponent = component.split(":");
        products.add(_store.getProductByID(recipeComponent[0]));
        quantities.add(Integer.parseInt(recipeComponent[1]));
    }

    List<Component> comps = new ArrayList<>();

    for(int i = 0; i < quantities.size(); i++){
      comps.add(new Component(quantities.get(i), products.get(i)));
    }
    Recipe recipe = new Recipe(alpha, comps);


    if (!(_store.getIdProducts().contains(idProduct))) { 
      _store.getProducts().put(idProduct, new AggregateProduct(idProduct, recipe));
    } 
    
    product = _store.getProductByID(idProduct);
    _store.registerBatchC(product, partner, price, stock, recipe);
    
  }
}
