package com.mycompany.medsconnect;

public class Product{

    private String product_name;
    private String product_code;
    private String product_mrp;
    private String product_bbprice;
    private String product_qty;
    private String product_value;

    public void setProductName (String product_name)
    {
        this.product_name = product_name;
    }

    public String getProductName()
    {
        return product_name;
    }


    public void setProductMRP (String product_mrp)
    {
        this.product_mrp = product_mrp;
    }

    public String getProductMRP()
    {
        return product_mrp;
    }

    public void setProductBBPrice (String product_bbprice)
    {
        this.product_bbprice = product_bbprice;
    }

    public String getProductBBPrice()
    {
        return product_bbprice;
    }

    public void setProductQty (String product_qty)
    {
        this.product_qty = product_qty;
    }

    public String getProductQty()
    {
        return product_qty;
    }

    public void setProductValue (String product_value)
    {
        this.product_value = product_value;
    }

    public String getProductValue()
    {
        return product_value;
    }

    public void setProductCode(String product_code)
    {
        this.product_code = product_code;
    }

    public String getProductCode()
    {
        return product_code;
    }
}
