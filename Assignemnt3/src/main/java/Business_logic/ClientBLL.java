package Business_logic;

import Business_logic.Validators.ClientNameValidator;
import Business_logic.Validators.EmailValidator;
import Business_logic.Validators.PhoneValidator;
import Business_logic.Validators.Validator;
import Data_access.ClientDAO;
import Models.Clients;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ClientBLL {

    private List<Validator<Clients>> validators;
    private ClientDAO clientDAO;

    public ClientBLL() {
        validators = new ArrayList<Validator<Clients>>();
        validators.add(new EmailValidator());
        validators.add(new ClientNameValidator());
        validators.add(new PhoneValidator());

        clientDAO = new ClientDAO();
    }

    public Clients findClientById(int id) {
        Clients c = clientDAO.findById("clientId", id);
        if (c == null) {
            throw new NoSuchElementException("The client with id =" + id + " was not found!");
        }
        return c;
    }

    public Clients insert(Clients c) {
        for (Validator<Clients> v : validators) {
            v.validate(c);
        }
        return clientDAO.insert(c, "clientId");
    }

    public Clients update(Clients c) {
        for (Validator<Clients> v : validators) {
            v.validate(c);
        }
        return clientDAO.update(c, "clientId");
    }
    public void delete(Clients c)
    {
        clientDAO.delete(c,"clientId");
    }

    public List<Clients> findAllClients()
    {
        return clientDAO.findAll();
    }
    public int getIdByName(String name)
    {
        return clientDAO.getClientIdByName(name);
    }
    public List<String> filterClients(String name){return clientDAO.getClientsByName(name);}
}
