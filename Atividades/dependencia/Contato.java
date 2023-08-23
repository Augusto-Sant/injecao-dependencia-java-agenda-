package Atividades.dependencia;

public class Contato {
    Integer id = null;
    String name;
    String birthDate;
    String phone;
    String email;

    public Contato(String name, String birthDate, String phone, String email) {
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
        this.email = email;
    }

    public Contato(int id, String name, String birthDate, String phone, String email) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
        this.email = email;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        if (this.id == null) {
            return "{" +
                    " name='" + getName() + "'" +
                    ", birthDate='" + getBirthDate() + "'" +
                    ", phone='" + getPhone() + "'" +
                    ", email='" + getEmail() + "'" +
                    "}";
        }
        return "{" +
                " id='" + getId() + "'" +
                " name='" + getName() + "'" +
                ", birthDate='" + getBirthDate() + "'" +
                ", phone='" + getPhone() + "'" +
                ", email='" + getEmail() + "'" +
                "}";
    }

}
