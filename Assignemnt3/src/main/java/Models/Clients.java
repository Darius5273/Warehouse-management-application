package Models;

public class Clients {
    private int clientId;
    private String name;
    private String email;
    private String phone;

    public Clients()
    {

    }
    public Clients(int clientId, String name, String email, String phone) {
        super();
        this.clientId=clientId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    public Clients(String name, String email, String phone) {
        super();
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}