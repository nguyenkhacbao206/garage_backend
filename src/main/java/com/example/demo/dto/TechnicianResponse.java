package com.example.demo.dto;

public class TechnicianResponse {

    private String id;
    private String name;
    private String phone;
    private Double baseSalary;
    private String position;
    private String userId;
    private Boolean active;

    public TechnicianResponse() {}

    public TechnicianResponse(String id, String name, String phone, Double baseSalary,
                              String position, String userId, Boolean active) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.baseSalary = baseSalary;
        this.position = position;
        this.userId = userId;
        this.active = active;
    }

    // Getter/Setter
    public String getId() { 
        return id; 
    }
    public void setId(String id) { 
        this.id = id; 
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

    public Double getBaseSalary() { 
        return baseSalary; 
    }
    public void setBaseSalary(Double baseSalary) { 
        this.baseSalary = baseSalary; 
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
