package ggc.core;

import java.io.Serializable;
import java.util.*;
import java.io.IOException;

import ggc.core.exception.*;

/* Class Warehouse implements a warehouse */
public class Warehouse implements Serializable {

  /* Serial number for serialization */
  private static final long serialVersionUID = 202109192006L;
  /* Warehouse's date */
  private Date _date;
  /* Warehouse's map of all partners */
  private Map<String,Partner> _partners;
  /* Warehouse's map of all products */
  private Map<String,Product> _products;
  /* Warehouse's map of all transactions */
  private Map<Integer,Transaction> _transactions;
  /* Warehouse's array of all batches */
  private List<Batch> _batches;
  /* Warehouse's available Balance */
  private double _availableBalance;
  /* Warehouse's accounting Balance */
  private double _accountingBalance;
  /* Warehouse's next Transaction ID */
  private int _nextTransactionID;

  public Warehouse(){
    _date = new Date();
    _partners = new HashMap<>();
    _products = new HashMap<>();
    _transactions = new HashMap<>();
    _batches = new ArrayList<>();
    _availableBalance = 0.0;
    _accountingBalance = 0.0; 
    _nextTransactionID = 0; 
  }
  
  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   */ 
  void importFile(String txtfile) throws IOException, BadEntryException, NoIDException{
    Parser parser = new Parser(this);
    parser.parseFile(txtfile);
  }

  /**
   * @param product
   * @param partner
   * @return true if notifications are active
   */
  boolean getProductNotificationSituation(Partner partner, Product product){
    return product.getObservers().contains(partner);
  }

  /**
   * @param idProduct
   * @param idPartner
   * @throws NoIDException
   * @throws NoProductIdException
   */
  void toggleProductNotifications(String idProduct, String idPartner) throws NoIDException, NoProductIdException{
    Partner partner = getPartnerByID(idPartner);
    Product product = getProductByID(idProduct);
    if(partner == null){
      throw new NoIDException(idPartner);
    }
    if(product == null){
      throw new NoProductIdException(idProduct);
    }
    if(getProductNotificationSituation(partner, product)){
      /*the partner wants to turn off notifications*/
      product.removeObserver(partner);
    }
    else{
      /*the partner wants to turn on notifications*/
      product.addObserver(partner);
    }
  }

  /**
   * @param id
   * @param name
   * @param address
   * @throws BadEntryException
   */
  void registerPartner(String id, String name, String address) throws BadEntryException{
    if(_partners.containsKey(id.toLowerCase())){
      throw new BadEntryException(id);  
    }
    Partner newPartner = new Partner(id, name, address);
    for(Product prod : _products.values()){
      prod.addObserver(newPartner);
    }
    _partners.put(id.toLowerCase(), newPartner);
  }

  /**
   * @param idProduct
   * @param idPartner
   * @param price
   * @param stock
   * @throws BadEntryException
   */
  void registerBatchS(Product product, Partner partner, double price, int stock) throws BadEntryException {
    Batch newBatch = new Batch(product, partner, price, stock);
    product.addBatch(newBatch);
    product.updateMaxPrice(price);
    product.updateStock(stock);
    _batches.add(newBatch);
  }

  /**
   * @param idProduct
   * @param idPartner
   * @param price
   * @param stock
   * @param recipe
   * @throws BadEntryException
   */
  void registerBatchC(Product product, Partner partner, double price, int stock, Recipe recipe) throws NoIDException {
    if(!(_partners.containsKey(partner.getID().toLowerCase()))){
      throw new NoIDException(partner.getID());
    }
    
    else if(!(_products.containsKey(product.getID()))){
      product = new AggregateProduct(product.getID(), recipe);
      _products.put(product.getID().toLowerCase(), product);
    }

    Batch newBatch = new Batch(product, partner, price, stock);
    _batches.add(newBatch);
    product.addBatch(newBatch);
    product.updateMaxPrice(price);
    product.updateStock(stock);

  }

  /**
   * @param idPartner
   * @param idProduct
   * @param price
   * @param amount
   * @throws NoProductIdException
   */
  void registerAcquisitionTransaction(String idPartner, String idProduct, double price, int amount, boolean notification) throws NoProductIdException{
    Product product = getProductByID(idProduct);
    Partner partner = getPartnerByID(idPartner); 
    isThereProduct(idProduct);
    Acquisition acquisition = new Acquisition(_nextTransactionID, product, partner, amount, price * amount, _date);
  
    _transactions.put(_nextTransactionID, acquisition);
    if(product.getStock() == 0 && notification){
      product.notifyAllObservers("NEW", product, price);
    }
    if(obtainBatchWithLowerPrice(product) > price){
      product.notifyAllObservers("BARGAIN", product, price);
    }
    product.updateStock(amount);
    product.updateMaxPrice(price);
    partner.addAcquisition(acquisition);
    Batch newBatch = new Batch(getProductByID(idProduct), getPartnerByID(idPartner), price, amount);
    _batches.add(newBatch);
    product.addBatch(newBatch);
    partner.addAcquisitionValue(price * amount);
    _nextTransactionID++;
    _availableBalance -= price * amount;
  }
 
  /**
   * @param idPartner
   * @param idProduct
   * @param price
   * @param deadline
   * @throws TooMuchException
   */
  void registerSaleTransaction(String idPartner, int deadline, String idProduct, int amount) throws TooMuchException{
    Product product = getProductByID(idProduct);
    Partner partner = getPartnerByID(idPartner);
    SaleByCredit sale = new SaleByCredit(_nextTransactionID, product, partner, amount, deadline);
    if(product.getStock() >= amount){
      enoughProductToSale(sale);
      partner.addSale(sale);
      _transactions.put(_nextTransactionID, sale);
      sale.setPaymentDate(_date.now());
      sale.setPaymentPeriod(deadline, _date.now());
      sale.setFineAndDiscount();
      _nextTransactionID++;
    }
    else{
      throw new TooMuchException(amount);
    }
  }

  /**
   * @param idPartner
   * @param idProduct
   * @param amount
   * @throws TooMuchException
   * @throws NoIDException
   * @throws NoProductIdException
   */
  void registerBreakdownTransaction(String idPartner, String idProduct, int amount) throws TooMuchException, NoIDException, NoProductIdException{
    Product product = getProductByID(idProduct);
    Partner partner = getPartnerByID(idPartner);
    if(partner == null){
      throw new NoIDException(idPartner);
    }
    if(product == null){
      throw new NoProductIdException(idProduct);
    }
    if(product.getType().equalsIgnoreCase("AGGREGATEPRODUCT")){

      if(product.getStock() < amount){
        throw new TooMuchException(amount);
      }
      AggregateProduct aProduct = (AggregateProduct)product;
      BreakdownSale breakdown = new BreakdownSale(_nextTransactionID, product, partner, amount, aProduct.getRecipe());

      if(enoughProductToBreakdown(breakdown)){
        _transactions.put(_nextTransactionID, breakdown);
        breakdown.setBatches(getBatchesOrderedByPrice());
        partner.addBreakdown(breakdown);
        breakdown.setPaymentDate(_date.now());
        partner.setPoints(10 * (int)Math.round(breakdown.getAmountPaid()));
        _nextTransactionID++;
      }
    }
  }

  /**
   * @param id
   * @throws NoIDException
   */
  void pay(int id) throws NoIDException{
    Transaction transaction = _transactions.get(id);
    if(transaction == null){
      throw new NoIDException(String.valueOf(id));
    }
    else{
      if(!transaction.isPaid()){
        transaction.setPaymentDate(displayDate());
        double amountPaid = paySale((SaleByCredit)transaction);
        transaction.getPartner().getAllSalesPaid().add((Sale)transaction);
        transaction.getPartner().addSalesPaid((int)Math.round(amountPaid));
        if(transaction.paidInTime(_date)){
          transaction.getPartner().setPoints(10*amountPaid);
          transaction.getPartner().updateStatus();
        }
        _availableBalance += amountPaid;
      }
    }
  }

  /**
   * @return current days.
   */
 int displayDate(){
    return _date.now();
  }

  /**
   * @param idProduct
   */
  void registerProductS(String idProduct){
    if(!_products.containsKey(idProduct)){
      Product newProduct = new SimpleProduct(idProduct);
      _products.put(idProduct, newProduct);
      for(Partner p : _partners.values()){
        newProduct.addObserver(p);
      }
    }
  }

  /**
   * @param idProduct
   * @param recipe
   */
  void registerProductC(String idProduct, Recipe recipe){
    if(!_products.containsKey(idProduct)){
      Product newProduct = new AggregateProduct(idProduct, recipe);
      _products.put(idProduct.toLowerCase(), newProduct); 
      for(Partner p : _partners.values()){
        newProduct.addObserver(p);
      }
    }  
  }

  /**
   * @param alpha
   * @param components
   * @param quantities
   * @throws NoProductIdException
   */
  Recipe registerRecipe(double alpha, List<String> components, List<Integer> quantities) throws NoProductIdException{
    int i = 0;
    List<Component> listComponents = new ArrayList<>();

    while(i < components.size()){
      String idProduct = components.get(i);
      int productQuantity = quantities.get(i);
      Product product = getProductByID(idProduct);
      if(product != null){
        Component component = new Component(productQuantity, product);
        listComponents.add(component);
      }
      else{
        throw new NoProductIdException(idProduct);
      }
      i++;
    }
    return new Recipe(alpha, listComponents);
  }

  /**
   * @param days
   * @throws InvalidTimeException
   */
  void advanceDays(int days) throws InvalidTimeException{
    if(days < 0){
      throw new InvalidTimeException(days);
    }
    else{
      _date.add(days);
      updateAccountingBalance();
    }
  }

  void updateAccountingBalance() {
    double newBalance = 0.0;
    for(Partner p : _partners.values()){
      newBalance += p.getPartnerAccountingBalance(_date);
    }
    _accountingBalance = newBalance + _availableBalance;
  }
  
  /**
   * @return string of all partners ordered
   */
  List<Partner> showAllPartners(){
    List<Partner> orderedPartners = new ArrayList<>(_partners.values());
    orderedPartners.sort(new Comparator<Partner>(){
          @Override
          public int compare(Partner p1, Partner p2){
            return p1.getID().toLowerCase().compareTo(p2.getID().toLowerCase());
          }
    });
    return orderedPartners;
  }

  /**
   * @param id
   * @return partner
   * @throws NoIDException
   */
  String showPartner(String id) throws NoIDException {
    if(_partners.containsKey(id.toLowerCase())){
      Partner partner = _partners.get(id.toLowerCase());
      return partner.toString();
    }
    else{
      throw new NoIDException(id);
    }
  }

  /**
   * @param idPartner
   * @return list of notifications
   */
  List<String> showNotification(String idPartner){
    Partner partner = _partners.get(idPartner.toLowerCase());
    List<String> notif = new ArrayList<>();
    for(Notification n : partner.getNotifications()){
      notif.add(n.toString());
    }
    partner.cleanNotification();
    return notif;
  }

  /**
   * @return string of all products ordered
   */
  List<Product> showAllProducts(){
    List<Product> orderedProducts = new ArrayList<>(_products.values());
    orderedProducts.sort(new Comparator<Product>(){
          @Override
          public int compare(Product p1, Product p2){
            return p1.getID().toLowerCase().compareTo(p2.getID().toLowerCase());
          }
    });
    return orderedProducts;
  }

  /**
   * @return string of Available Batches ordered
   */
  List<Batch> showAllAvailableBatches(){
    List<Batch> orderedBatches = new ArrayList<>(_batches);
    orderedBatches.sort(new Comparator<Batch>(){
          @Override
          public int compare(Batch b1, Batch b2){
            int comp = 0;
            comp = b1.getIDProduct().toLowerCase().compareTo(b2.getIDProduct().toLowerCase());
            if(comp == 0){
              comp = b1.getIDPartner().toLowerCase().compareTo(b2.getIDPartner().toLowerCase());
            }
            if(comp == 0){
              comp = Double.compare(b1.getPrice(), b2.getPrice());
            }
            if(comp == 0){
              comp = b1.getStock() - b2.getStock();
            }
            return comp;
          }
    });
    return orderedBatches;
  }

  /**
   * @param idProduct
   * @return string of Available Batches ordered by Partner
   * @throws NoIDException
   */
  List<String> showBatchesByPartner(String id) throws NoIDException{
    if (!(_partners.containsKey(id.toLowerCase()))){
      throw new NoIDException(id);
    }
    else{
      List<String> orderedBatches = getOrderedBatches();
      List<String> orderedBatchesPartner = new ArrayList<>();
      
      for(String ob : orderedBatches){
        String[] line = ob.split("\\|");
        if(line[1].equalsIgnoreCase(id)){
          orderedBatchesPartner.add(ob);
        }
      }
      return orderedBatchesPartner;
    }
  }

  /**
   * @param idProduct
   * @return string of Available Batches ordered by Product
   */
  List<String> showBatchesByProduct(String idProduct) throws NoIDException{
    if (!(_products.containsKey(idProduct))){
      throw new NoIDException(idProduct);
    }
    else{
      List<String> orderedBatches = getOrderedBatches();
      List<String> orderedBatchesProduct = new ArrayList<>();
      for(String ob : orderedBatches){
        String[] line = ob.split("\\|");
        if(line[0].equalsIgnoreCase(idProduct)){
          orderedBatchesProduct.add(ob);
        }
      }
      return orderedBatchesProduct;
    }
  }

  /**
   * @return available Balance
   */
  int getAvailableBalance(){
    return (int)Math.round(_availableBalance);
  }

  /**
   * @return accounting Balance
   */
  int getAccountingBalance(){
    updateAccountingBalance();
    return (int)Math.round(_accountingBalance);
  }

  /**
   * @param givenPrice
   * @return batches of product under given price
   */
  List<String> showProductBatchesUnderGivenPrice(int givenPrice){
    List<String> batches = new ArrayList<>();

    List<Batch> orderedBatches = _batches;
      orderedBatches.sort(new Comparator<Batch>(){
            @Override
            public int compare(Batch b1, Batch b2){
              int comp = 0;
            comp = b1.getIDProduct().toLowerCase().compareTo(b2.getIDProduct().toLowerCase());
            if(comp == 0){
              comp = b1.getIDPartner().toLowerCase().compareTo(b2.getIDPartner().toLowerCase());
            }
            if(comp == 0){
              comp = Double.compare(b1.getPrice(), b2.getPrice());
            }
            if(comp == 0){
              comp = b1.getStock() - b2.getStock();
            }
            return comp;
          }
    });
    for(Batch b : orderedBatches){
      if(b.getPrice() < Double.valueOf(givenPrice))
        batches.add(b.toString());
    }
    return batches;
  }

  /**
   * @param id
   * @return string of partner's acquisitions
   * @throws NoIDException
   */
  List<Acquisition> showPartnerAcquisitions(String id) throws NoIDException{
    if (getPartnerByID(id) == null){
      throw new NoIDException(id);
    }
    else{
      return getPartnerByID(id).getAcquisitions();
    }
  }

    /**
   * @param idPartner
   * @return string of partner's acquisitions
   * @throws NoIDException
   */
  List<Sale> getPaymentsByPartner(String idPartner) throws NoIDException{
    if(!(getIdPartners().contains(idPartner.toLowerCase()))){
      throw new NoIDException(idPartner);
    }
    Partner partner = getPartnerByID(idPartner);
    List<Sale> orderedSales = partner.getAllSalesPaid();
    orderedSales.sort(new Comparator<Sale>(){
      @Override
      public int compare(Sale s1, Sale s2){
        return s1.getTransactionID() - s2.getTransactionID();
      }
    }); 
    return orderedSales;
  }

  /**
   * @param id
   * @return string of partner's sales and/or breakdownSales
   * @throws NoIDException
   */
  List<Sale> showPartnerSalesBreakdownSale(String id) throws NoIDException{
    if (getPartnerByID(id) == null){
      throw new NoIDException(id);
    }
    else{
      return getPartnerByID(id).getSales();
    }
  }

  /**
   * @param id
   * @return Transaction
   * @throws NoIDException
   */
  String showTransaction(int id) throws NoIDException{
    if(_transactions.get(id) == null){
      throw new NoIDException(String.valueOf(id));
    }
    else{
      Transaction transaction = _transactions.get(id);
      return transaction.toString();
    }
  }
 
  /*---------------------------- Auxiliar Methods ----------------------------*/

  /**
   * @param idProduct
   * @throws NoProductIdException
   */
  void isThereProduct(String idProduct) throws NoProductIdException{
    Product product = getProductByID(idProduct);
    if(product == null){
      throw new NoProductIdException(idProduct);
    }
  }

  void isTherePartner(String idPartner) throws NoIDException{
    if(getPartnerByID(idPartner) == null){
      throw new NoIDException(idPartner);
    }
  }

  /**
   * @param batch
   */
  void removeBatch(Batch b){
    Iterator<Batch> iter = _batches.iterator();
    while(iter.hasNext()){
      Batch batchNext = iter.next();
      if(batchNext.equals(b)){
        iter.remove();
      }
    }
  }

  /**
   * @param sale
   */
  double paySale(SaleByCredit sale){
    return sale.doPayment(displayDate());
  }

  /**
   * @param partner
   * @param product
   * @return the lowest price of the batch
   */
  double obtainBatchWithLowerPrice(Product product){
    List<Batch> batches = new ArrayList<>();

    for(Batch b : _batches){
      if(b.getProduct().getID().equalsIgnoreCase(product.getID())){
        batches.add(b);
      }
    }
    if(batches.size() != 0){
      List<Batch> orderedBatchesByPrice = batches;
      orderedBatchesByPrice.sort(new Comparator<Batch>(){
            @Override
            public int compare(Batch b1, Batch b2){
              return Double.compare(b1.getPrice(), b2.getPrice());
            }
      });
      return orderedBatchesByPrice.get(0).getPrice();
    }
    return 0.0;
    
  }

  /**
   * @param idProduct
   * @return available Amount
   */
  int availableAmount(String idProduct){
    Product product = getProductByID(idProduct);
    return product.getStock();
  }

  /**
   * @param Sale
   * @return enough Product sold by partner
   */
  boolean enoughProductToBreakdown(BreakdownSale sale){
    int quantity = sale.getQuantity();
    Double saleValue = 0.0;
    for(Batch b : getBatchesOrderedByPrice()){
      if(b.getIDProduct().equals(sale.getIdProduct())){
        if(b.getStock() >= quantity){
          b.removeStock(quantity);
          b.getProduct().removeStock(quantity);
          saleValue += quantity * b.getPrice();
          double baseValue = createBatchesWithComponents((AggregateProduct)sale.getProduct(), sale.getIdPartner(), quantity, saleValue);
          if(baseValue > 0){
            sale.setBaseValue(baseValue);
            sale.setAmountPaid(baseValue);
            _availableBalance += baseValue;
          }
          if(b.getStock() == 0){
            removeBatch(b);
          }
          return true;
        }
        else{
          saleValue += b.getStock() * b.getPrice();
          quantity -= b.getStock();
          b.getProduct().removeStock(b.getStock());
          removeBatch(b);
        }
      }
    }
    return false;
  }
  
  boolean enoughProductToSale(Sale sale){
    int quantity = sale.getQuantity();
    Double baseValue = 0.0;
    List<Integer> quantitiesFromBatches = new ArrayList<>();
    List<Batch> batchesOrderedFrom = new ArrayList<>();
    for(Batch b : getBatchesOrderedByPrice()){
      if(b.getIDProduct().equals(sale.getIdProduct())){
        if(b.getStock() >= quantity){
          baseValue += quantity * b.getPrice();
          quantitiesFromBatches.add(quantity);
          batchesOrderedFrom.add(b);
          sale.setBaseValue(baseValue);
          b.getProduct().removeStock(sale.getQuantity());
          updateQuantitiesFromBatches(batchesOrderedFrom, quantitiesFromBatches);
          //_accountingBalance += baseValue;
          if(b.getStock() == 0){
            removeBatch(b);
          }
          return true;
        }
        else{
          baseValue += b.getStock() * b.getPrice();
          quantity -= b.getStock();
          quantitiesFromBatches.add(b.getStock());
          batchesOrderedFrom.add(b);
          removeBatch(b);
        }
      }
    }
    return false;
  }

   /**
   * @param product
   * @param idPartner
   * @param quantity
   */
  double createBatchesWithComponents(AggregateProduct product, String idPartner, int quantity, double saleValue){
    double baseValue = 0.0;
    Recipe recipe = product.getRecipe();
    int quantityToAdd = 0;
    for(Component c : recipe.getComponent()){
      Product newProduct = c.getProduct();
      double priceBatch = c.getProduct().getMinPrice();
      quantityToAdd = c.getQuantity() * quantity;
      baseValue += quantityToAdd * getProductPrice(c.getName());
      Batch newBatch = new Batch(getProductByID(c.getName()), getPartnerByID(idPartner), priceBatch,
      quantityToAdd);
      _batches.add(newBatch);
      newProduct.addBatch(newBatch);
      newProduct.updateStock(quantityToAdd);
    }
    return saleValue - baseValue;
  }

  /**
   * @param idProduct
   * @return price
   */
  double getProductPrice(String idProduct){
    double price = getProductByID(idProduct).getMaxPrice();
    if(_products.containsKey(idProduct)){
      for(Batch b : _batches)
        if(b.getIDProduct().equals(idProduct))
          price = b.getPrice();
    }
    return price;
  }

  /**
   * @return batches ordered by product and price
   */
  List<Batch> getBatchesOrderedByPrice(){
    List<Batch> orderedBatches = new ArrayList<>(_batches);
    orderedBatches.sort(new Comparator<Batch>(){
          @Override
          public int compare(Batch b1, Batch b2){
            int comp = 0;
            comp = Double.compare(b1.getPrice(), b2.getPrice());
            
            if(comp == 0){
              comp = b1.getIDPartner().toLowerCase().compareTo(b2.getIDPartner().toLowerCase());
            }
            return comp;
          }
    });
    return orderedBatches;
  }

  /**
   * @param batches
   * @param quantities
   */
  void updateQuantitiesFromBatches(List<Batch> batches, List<Integer> quantities){
    for(int i = 0; i < batches.size(); i++){
      batches.get(i).removeStock(quantities.get(i));
      if(batches.get(i).getStock() == 0){
        removeBatch(batches.get(i));
      }
    }
  }

  /**
   * @param id
   * @return foundProduct
   * @throws NoIDException
   */
  Product getProductByID(String id){
    return _products.get(id);
  }

  /**
   * @param id
   * @return foundPartner
   * @throws NoIDException
   */
  Partner getPartnerByID(String id){
    return _partners.get(id.toLowerCase());
  }

  /**
   * @return batches ordered
   */
  List<String> getOrderedBatches(){
    List<String> batches = new ArrayList<>();
    List<Batch> orderedBatches = new ArrayList<>(_batches);
    orderedBatches.sort(new Comparator<Batch>(){
          @Override
          public int compare(Batch b1, Batch b2){
            int comp = 0;
            comp = b1.getIDProduct().toLowerCase().compareTo(b2.getIDProduct().toLowerCase());
            if(comp == 0){
              comp = b1.getIDPartner().toLowerCase().compareTo(b2.getIDPartner().toLowerCase());
            }
            if(comp == 0){
              comp = Double.compare(b1.getPrice(), b2.getPrice());
            }
            if(comp == 0){
              comp = b1.getStock() - b2.getStock();
            }
            return comp;
          }
    });
    for(Batch batch : orderedBatches){
      batches.add(batch.toString());
    }
    return batches;
  }

  /**
   * @return _products
   */
  protected Map<String, Product> getProducts(){
   return _products;
  }

  /**
   * @return allIdProducts
   */
  List<String> getIdProducts(){
    List<String> allIdProducts = new ArrayList<String>(_products.keySet());
    return allIdProducts;
  }

  /**
   * @return allIdPartners
   */
  List<String> getIdPartners(){
    List<String> allIdPartners = new ArrayList<String>(_partners.keySet());
    return allIdPartners;
  }

  /**
   * @return _date
   */
  Date getDate(){
    return _date;
  }

}
