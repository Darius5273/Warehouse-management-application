package Business_logic;

import Business_logic.Validators.*;
import Data_access.ProductDAO;
import Models.Clients;
import Models.Products;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ProductBLL {

    private List<Validator<Products>> validators;
    private ProductDAO productDAO;

    public ProductBLL() {
        validators = new ArrayList<Validator<Products>>();
        validators.add(new PriceValidator());
        validators.add(new StockQuantityValidator());
        productDAO = new ProductDAO();
    }

    public Products findProductById(int id) {
        Products product = productDAO.findById("productId", id);
        if (product == null) {
            throw new NoSuchElementException("The product with id =" + id + " was not found!");
        }
        return product;
    }

    public Products insert(Products p) {
        for (Validator<Products> v : validators) {
            v.validate(p);
        }
        return productDAO.insert(p, "productId");
    }

    public void delete(Products c)
    {
        productDAO.delete(c,"productId");
    }

    public Products update(Products p) {
        return productDAO.update(p, "productId");
    }

    public List<Products> findAllProducts() {
        return productDAO.findAll();
    }
    public int getIdByName(String name) {
        return productDAO.getProductIdByName(name);
    }
    public int getStockQuantity(int id)
    {
        return productDAO.getStockQuantity(id);
    }
    public List<String> filterProducts(String name){return productDAO.getClientsByName(name);}

}
