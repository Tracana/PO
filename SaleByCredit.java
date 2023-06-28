package ggc.core;


public class SaleByCredit extends Sale {

    /* Serial number for serialization */
    private static final long serialVersionUID = 202109192006L;
    /*SaleByCredit's deadline*/
    private Date _deadline;
    /*SaleByCredit's amount paid*/
    private double _amountPaid;
    /*SaleByCredit's period*/
    private int _paymentPeriod;
    /*SalebyCredit's payment situation*/
    private boolean _paymentSituation;
  
    
    /**
    * @param id
    * @param product
    * @param partner
    * @param quantity
    * @param deadline
    */
    protected SaleByCredit(int id, Product product, Partner partner, int quantity, int deadline) {
        super(id, product, partner, quantity);
        _deadline = new Date(deadline);
        _amountPaid = 0.0;
        _paymentPeriod = 1;
        _paymentSituation = false;

    }

    /**
    * @return payment Period
    */
    int getPaymentPeriod(){
        return _paymentPeriod;
    }
    /**
    * @return deadline
    */
    Date getDeadline(){
        return _deadline;
    }

    int getDeadlineToString(){
        return _deadline.now();
    }

    /**
    * @return amount paid
    */
    @Override
    double getAmountPaid(){
        return _amountPaid;
    }

    void setAmountPaid(Double baseValue){
        _amountPaid = baseValue;
    }

    /**
    * @return payment situation
    */
    @Override
    public boolean isPaid(){
        return _paymentSituation;
    }

    /**
    * @return payment situation
    */
    void setPaymentSituation(boolean situation){
        _paymentSituation = situation;
    }

    /**
    * @return true if the payment was made in time
    */
    @Override
    boolean paidInTime(Date date){
        return date.difference(_deadline) <= 0;
    }

    /**
    * @param date
    * @return amount paid
    */
    double doPayment(int date){
        Date currentDate = new Date(date);
        setPaymentPeriod(_deadline.now(), date);
        setPaymentDate(date);
        verifyPartnerStatus(currentDate);
        setFineAndDiscount();
        setPaymentSituation(true);
        return _amountPaid;
    }

    /**
    * @param currentdate
    */
    @Override
    double getCurrentAmountPaid(Date currentDate){
        setPaymentPeriod(_deadline.now(), currentDate.now());
        setPaymentDate(currentDate.now());
        setFineAndDiscount();
        return _amountPaid;
    }

    /**
    * @param deadline
    * @param paymentDate
    */
    void setPaymentPeriod(int deadline, int paymentDate){
        if(deadline - paymentDate >= getN()){
            _paymentPeriod = 1;
        }
        else if(0 <= deadline - paymentDate && deadline - paymentDate < getN()){
            _paymentPeriod = 2;
        }
        else if(0 < paymentDate - deadline && paymentDate - deadline <= getN()){
            _paymentPeriod = 3;
        }
        else{
            _paymentPeriod = 4;
        }
    }

    /**
    * @return the N days from each type of product
    */
    int getN(){
        if("AGGREGATEPRODUCT".equals(getProduct().getType())){
            return 3;
        }
        else{
            return 5;
        }
    }
    
    void verifyPartnerStatus(Date date){
        if("ELITE".equals(super.getPartner().getStatus()) && (date.difference(_deadline)) >= 15){
            getPartner().removePoints((int)Math.round(getPartner().getPoints() * 0.75));
            getPartner().setStatus(new SelectionState(getPartner()));
        }
        else if("SELECTION".equals(super.getPartner().getStatus()) && (date.difference(_deadline)) >= 2){
            getPartner().removePoints((int)Math.round(getPartner().getPoints() * 0.9));
            getPartner().setStatus(new NormalState(getPartner()));
        }
        else{
            getPartner().setPoints(0);
        }
    }

    void setFineAndDiscount() {
        if(getPaymentPeriod() == 1){
            _amountPaid = getBaseValue() * 0.9;
        }
        else if(getPaymentPeriod() == 2){
            if("NORMAL".equals(getPartner().getStatus())){
                _amountPaid = getBaseValue();
                //System.out.println(_amountPaid);
            }
            else if("SELECTION".equals(getPartner().getStatus())){
                if(super.getPaymentDate().difference(getDeadline()) <= 2){
                    _amountPaid = getBaseValue() * 0.95;
                }
                else{
                    _amountPaid = getBaseValue();
                }
            }
            else{
                _amountPaid = getBaseValue() * 0.9;
            }    
        }
        else if(getPaymentPeriod() == 3){
            if("NORMAL".equals(getPartner().getStatus())){
                _amountPaid = 1.05 * super.getPaymentDate().difference(_deadline) * getBaseValue();
            }
            else if("SELECTION".equals(getPartner().getStatus())){
                if(super.getPaymentDate().difference(_deadline) <= 1){
                    _amountPaid = getBaseValue();
                }
                else{
                    _amountPaid = 1.02 * super.getPaymentDate().difference(_deadline) * getBaseValue();
                }

            } 
            else{
                _amountPaid = getBaseValue() * 0.95;
            }
        }
        else{
            if("NORMAL".equals(getPartner().getStatus())){
                _amountPaid = 1.1 * super.getPaymentDate().difference(_deadline) * getBaseValue();
            }
            else if("SELECTION".equals(getPartner().getStatus())){
                _amountPaid = 1.05 * super.getPaymentDate().difference(_deadline) * getBaseValue();
            }
            else{
                _amountPaid = getBaseValue();
            }
        }
    }

    /**
    * @return the string that represents the Sale
    */
    @Override
    public String toString() {
        if(isPaid()){
            return "VENDA|" + super.getTransactionID() + "|" + super.getIdPartner() + "|" + super.getIdProduct() +
            "|" + super.getQuantity() + "|" + (int)super.getBaseValue() + "|" + (int)Math.round(this.getAmountPaid()) + "|"
             + this.getDeadlineToString() + "|" + super.getPaymentDate();
        }
        return "VENDA|" + super.getTransactionID() + "|" + super.getIdPartner() + "|" + super.getIdProduct() +
            "|" + super.getQuantity() + "|" + (int)super.getBaseValue() + "|" + (int)Math.round(this.getAmountPaid()) + "|"
             + this.getDeadlineToString();
    }
}
