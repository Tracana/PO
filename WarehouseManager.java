package ggc.core;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import ggc.core.exception.BadEntryException;
import ggc.core.exception.ImportFileException;
import ggc.core.exception.InvalidTimeException;
import ggc.core.exception.MissingFileAssociationException;
import ggc.core.exception.NoIDException;
import ggc.core.exception.NoProductIdException;
import ggc.core.exception.TooMuchException;
import ggc.core.exception.UnavailableFileException;

/** Façade for access. */
public class WarehouseManager implements Serializable {

  /* Serial number for serialization */
  private static final long serialVersionUID = 202109192006L;
  /* Name of file storing current warehouse */
  private String _filename;
  /* The wharehouse itself */
  private Warehouse _warehouse;

  public WarehouseManager() {
    _filename = "";
    _warehouse = new Warehouse();
  }

  /**
   * @return _warehouse
   */
  public Warehouse getWarehouse(){
    return _warehouse;
  }

  /**
   * @param newName
   */
  public void setFilename(String newName){
    _filename = newName;
  }
  
  /**
   * @return _filename
   */
  public String getFilename(){
    return _filename;
  }

  /**
   * @throws IOException
   * @throws FileNotFoundException
   * @throws MissingFileAssociationException
   */
  public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {
    if(getFilename() == null){
      throw new FileNotFoundException(getFilename());
    }  
    else{
      auxiliarSave(getFilename());
    }
  }

  /**
   * @param filename
   * @throws MissingFileAssociationException
   * @throws IOException
   * @throws FileNotFoundException
   */
  public void saveAs(String filename) throws MissingFileAssociationException, FileNotFoundException, IOException {
    if(filename == null){
      throw new FileNotFoundException();
    } 
    else{
      auxiliarSave(filename);
    }  
  }

  /**
   * @param filename
   * @throws UnavailableFileException
   * @throws ClassNotFoundException
   */
  public void load(String filename) throws UnavailableFileException, ClassNotFoundException{ 
    try{
      ObjectInputStream inputFile = new ObjectInputStream(new FileInputStream(filename));
      _filename = filename;
      _warehouse = (Warehouse)inputFile.readObject();
      inputFile.close();
    }
    catch(IOException ie){
      throw new UnavailableFileException(filename);
    }
  }

  /**
   * @param filename
   * @throws MissingFileAssociationException
   * @throws IOException
   */
  public void auxiliarSave(String filename) throws MissingFileAssociationException, IOException {
    try(ObjectOutputStream outputFile = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))){
      outputFile.writeObject(_warehouse);
      setFilename(filename);
    }catch(FileNotFoundException fnfe){
      throw new MissingFileAssociationException();
    }catch(IOException ie){
      throw new IOException(ie);
    }
  }

  /**
   * @param textfile
   * @throws ImportFileException
   */
  public void importFile(String textfile) throws ImportFileException {
      try {
        _warehouse.importFile(textfile);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (BadEntryException e) {
        e.printStackTrace();
      } catch (NoIDException e) {
        e.printStackTrace();
      }

  }

  /**
   * @param name
   * @param address
   * @param id
   * @throws BadEntryException
   */
  public void registerPartner(String name, String address, String id) throws BadEntryException{
    _warehouse.registerPartner(name, address, id);
  }

  /**
   * @param idProduct
   */
  public void registerProductS(String idProduct){
    _warehouse.registerProductS(idProduct);
  }
  /**
   * @param idProduct
   * @param recipe
   */
  public void registerProductC(String idProduct, Recipe recipe){
    _warehouse.registerProductC(idProduct, recipe);
  }

  /**
   * @param idProduct
   * @throws NoProductIdException
   */
  public Recipe registerRecipe(double alpha, List<String> componets, List<Integer> quantities) throws NoProductIdException{
    return _warehouse.registerRecipe(alpha, componets, quantities);
  }

  /**
   * @return current date
   */
  public int displayDate(){
    return _warehouse.displayDate();
  }

  /**
   * @param days
   * @throws InvalidTimeException
   */
  public void advanceDays(int days) throws InvalidTimeException{
    _warehouse.advanceDays(days);
  }

  /**
   * @return All Partenrs
   */
  public List<Partner> showAllPartners(){
    return _warehouse.showAllPartners();
  }

  /**
   * @param id
   * @return Partner
   * @throws NoIDException
   */
  public String showPartner(String id) throws NoIDException{
    return _warehouse.showPartner(id);
  }

  /**
   * @param id
   * @return list of notifications
   */
  public List<String> showNotification(String id){
    return _warehouse.showNotification(id);
  }

  /**
   * @return All Products
   */
  public List<Product> showAllProducts(){
    return _warehouse.showAllProducts();
  }

  /**
   * @return All Available Batches
   */
  public List<Batch> showAllAvailableBatches(){
    return _warehouse.showAllAvailableBatches();
  }

  /**
   * @return Batches By Partner
   * @throws NoIDException
   */
  public List<String> showBatchesByPartner(String id) throws NoIDException{
    return _warehouse.showBatchesByPartner(id);
  }

  /**
   * @return Batches By Product
   * @throws NoIDException
   */
  public List<String> showBatchesByProduct(String id) throws NoIDException{
    return _warehouse.showBatchesByProduct(id);
  }

  /**
   * @return available Balance
   */
  public int getAvailableBalance(){
    return _warehouse.getAvailableBalance();
  }

  /**
   * @return accounting Balance
   */
  public int getAccountingBalance(){
    return _warehouse.getAccountingBalance();
  }

  /**
   * @return batches of product under given price
   */
  public List<String> showProductBatchesUnderGivenPrice(int givenPrice){
    return _warehouse.showProductBatchesUnderGivenPrice(givenPrice);
  }


  public void toggleProductNotifications(String idProduct, String idPartner) throws NoIDException, NoProductIdException{
    _warehouse.toggleProductNotifications(idProduct, idPartner);
  }

  /**
   * @return partner acquisitions
   * @throws NoIDException
   */
  public List<Acquisition> showPartnerAcquisitions(String id) throws NoIDException{
    return _warehouse.showPartnerAcquisitions(id);
  }

  /**
   * @return partner sales and/ or BreakdownSales
   * @throws NoIDException
   */
  public List<Sale> showPartnerSalesBreakdownSales(String id) throws NoIDException{
    return _warehouse.showPartnerSalesBreakdownSale(id);
  }

  /**
   * @return partner sales and/ or BreakdownSales
   * @throws NoIDException
   */
  public List<Sale> getPaymentsByPartner(String idPartner) throws NoIDException{
    return _warehouse.getPaymentsByPartner(idPartner);
  }

  /**
   * @param id
   * @return Transaction
   * @throws NoIDException
   */
  public String showTransaction(int id) throws NoIDException{
    return _warehouse.showTransaction(id);
  }

  /**
   * @param idPartner
   * @param idProduct
   * @param price
   * @param amount
   * @return true if the product exist
   * @throws NoProductIdException
   */
  public void registerAcquisitionTransaction(String idPartner, String idProduct, double price, int amount, boolean notification) throws NoProductIdException{
    _warehouse.registerAcquisitionTransaction(idPartner, idProduct, price, amount, notification);
  }

  public void isTherePartner(String idPartner) throws NoIDException{
    _warehouse.isTherePartner(idPartner);
  }

  /**
   * @param idPartner
   * @param deadline
   * @param idProduct
   * @throws TooMuchException
   */
  public void registerSaleTransaction(String idPartner, int deadline, String idProduct, int amount) throws TooMuchException{
    _warehouse.registerSaleTransaction(idPartner, deadline, idProduct, amount);
  }

  /**
   * @param idPartner
   * @param idProduct
   * @param amount
   * @throws TooMuchException
   * @throws NoProductIdException
   * @throws NoIDException
   */
  public void registerBreakdownTransaction(String idPartner, String idProduct, int amount) throws TooMuchException, NoIDException, NoProductIdException{
    _warehouse.registerBreakdownTransaction(idPartner, idProduct, amount);
  }

  /**
   * @param idProduct
   * @throws NoProductIdException
   */
  public void isThereProduct(String idProduct) throws NoProductIdException{
    _warehouse.isThereProduct(idProduct);
  }

  /**
   * @param idProduct
   * @return available Amount
   */
  public int availableAmount(String idProduct){
    return _warehouse.availableAmount(idProduct);
  }

  /**
   * @param id
   * @throws NoIDException
   */
  public void pay(int id) throws NoIDException{
    _warehouse.pay(id);
  }

}
