package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "technicians")
public class Technician {

    @Id
    private String id;
    private String techCode; 
    private String name;
    private String phone;
    private Double salaryBase;
    private String position;
    private String userId;
    private Boolean active;

    public Technician() {}

    // Getter & Setter
    public String getId() { 
        return id; 
    }
    public void setId(String id) { 
        this.id = id; 
    }

    public String getTechCode() { 
        return techCode; 
    }
    public void setTechCode(String techCode) { 
        this.techCode = techCode; 
    }

    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }

    public String getPhone() { 
        return phone; 
    }
    public void setPhone(String phone) { 
        this.phone = phone; 
    }

    public Double getSalaryBase() { 
        return salaryBase; 
    }
    public void setSalaryBase(Double salaryBase) { 
        this.salaryBase = salaryBase; 
    }

    public String getPosition() { 
        return position; 
    }
    public void setPosition(String position) { 
        this.position = position; 
    }

    public String getUserId() { 
        return userId; 
    }
    public void setUserId(String userId) { 
        this.userId = userId; 
    }

    public Boolean getActive() { 
        return active; 
    }
    public void setActive(Boolean active) { 
        this.active = active; 
    }
}
