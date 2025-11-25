package com.example.demo.dto;

public class UpdateProfileRequest {

    private String username;
    private String email;
    private String phonenumber;

    private String birthday;
    private String gender;
    private String city;
    private String hometown;
    private String address;

    private String description;

    // GETTER & SETTER

    public String getUsername() { 
        return username;
     }
    public void setUsername(String username) { 
        this.username = username;
     }

    public String getEmail() { 
        return email;
     }
    public void setEmail(String email) { 
        this.email = email;
     }

    public String getPhonenumber() { 
        return phonenumber;
     }
    public void setPhonenumber(String phonenumber) { 
        this.phonenumber = phonenumber;
     }

    public String getBirthday() { 
        return birthday;
     }
    public void setBirthday(String birthday) { 
        this.birthday = birthday;
     }

    public String getGender() { 
        return gender;
     }
    public void setGender(String gender) { 
        this.gender = gender;
     }

    public String getCity() { 
        return city;
     }
    public void setCity(String city) { 
        this.city = city;
     }

    public String getHometown() { 
        return hometown;
     }
    public void setHometown(String hometown) { 
        this.hometown = hometown;
     }

    public String getAddress() { 
        return address;
     }
    public void setAddress(String address) { 
        this.address = address;
     }

    public String getDescription() { 
        return description;
     }
    public void setDescription(String description) { 
        this.description = description;
     }

}
