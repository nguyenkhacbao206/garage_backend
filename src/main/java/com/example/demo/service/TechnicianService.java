package com.example.demo.service;

import com.example.demo.dto.TechnicianRequest;
import com.example.demo.dto.TechnicianResponse;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Technician;
import com.example.demo.repository.TechnicianRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
public class TechnicianService {

    private final TechnicianRepository technicianRepository;

    public TechnicianService(TechnicianRepository technicianRepository) {
        this.technicianRepository = technicianRepository;
    }

    // tạo mã tự động
    private String generateTechCode() {
        Technician last = technicianRepository.findTopByOrderByTechCodeDesc();

        if (last == null || last.getTechCode() == null) {
            return "TECH-001";
        }

        String lastCode = last.getTechCode();
        int number = Integer.parseInt(lastCode.replace("TECH-", ""));
        number += 1;

        return String.format("TECH-%0" + Math.max(3, String.valueOf(number).length()) + "d", number);
    }

    public List<TechnicianResponse> getAll() {
        return technicianRepository.findAll()
                .stream()
                .sorted((t1, t2) -> {
                    if (t1.getCreatedAt() == null) return 1;
                    if (t2.getCreatedAt() == null) return -1;
                    return t2.getCreatedAt().compareTo(t1.getCreatedAt());
                })
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // sort
    public List<TechnicianResponse> sortByCreatedAt(List<TechnicianResponse> tech, boolean asc) {

        Comparator<TechnicianResponse> comp = Comparator.comparing(
                TechnicianResponse::getCreatedAt,
                Comparator.nullsLast(Comparator.naturalOrder())
        );

        if (!asc) {
            comp = comp.reversed();
        }

        tech.sort(comp);
        return tech;
    }

    // Search technicians
    public List<TechnicianResponse> search(String input) {
        if (input == null || input.trim().isEmpty()) {
            return getAll();
        }

        String[] tokens = input.toLowerCase().split("[^a-z0-9À-ỹ]+");
        List<Technician> all = technicianRepository.findAll();
        List<Technician> matched = new ArrayList<>();

        for (Technician t : all) {
            String code = t.getTechCode() != null ? t.getTechCode().toLowerCase() : "";
            String name = t.getName() != null ? t.getName().toLowerCase() : "";
            String phone = t.getPhone() != null ? t.getPhone().toLowerCase() : "";

            for (String token : tokens) {
                if (token.isEmpty()) continue;
                if (code.contains(token) || name.contains(token) || phone.contains(token)) {
                    matched.add(t);
                    break;
                }
            }
        }

        return matched.stream().map(this::toResponse).collect(Collectors.toList());
    }


    // new create
    public TechnicianResponse create(TechnicianRequest req) {
        if (req.getPhone() != null && technicianRepository.existsByPhone(req.getPhone())) {
            throw new DuplicateKeyException("Phone number already exists!");
        }
        // check phone numbers
        if (req.getPhone() == null || !req.getPhone().matches("0\\d{9}")) {
            throw new RuntimeException("phone number must start with 0 and exactly 10 digits");
        }

        Technician t = new Technician();
        t.setTechCode(generateTechCode());
        t.setName(req.getName());
        t.setPhone(req.getPhone());
        t.setSalaryBase(req.getBaseSalary());
        t.setPosition(req.getPosition());
        t.setUserId(req.getUserId());
        t.setActive(true);
        t.setCreatedAt(LocalDateTime.now());
        t.setUpdatedAt(LocalDateTime.now());

        Technician saved = technicianRepository.save(t);
        return toResponse(saved);
    }

    // update
    public TechnicianResponse update(String id, TechnicianRequest req) {
        Technician t = technicianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kỹ thuật viên với id: " + id));

        if (!req.getPhone().equals(t.getPhone()) && technicianRepository.existsByPhone(req.getPhone())) {
            throw new DuplicateKeyException("Số điện thoại đã tồn tại");
        }

        if (req.getPhone() == null || !req.getPhone().matches("0\\d{9}")) {
            throw new RuntimeException("phone number must start with 0 and exactly 10 digits");
        }

        t.setName(req.getName());
        t.setPhone(req.getPhone());
        t.setSalaryBase(req.getBaseSalary());
        t.setPosition(req.getPosition());
        t.setUserId(req.getUserId());

        t.setUpdatedAt(LocalDateTime.now());

        Technician saved = technicianRepository.save(t);
        return toResponse(saved);
    }

    public void delete(String id) {
        if (!technicianRepository.existsById(id)) {
            throw new RuntimeException("Kỹ thuật viên không tồn tại");
        }
        technicianRepository.deleteById(id);
    }

    private TechnicianResponse toResponse(Technician t) {
        TechnicianResponse res = new TechnicianResponse();
        res.setId(t.getId());
        res.setTechCode(t.getTechCode());
        res.setName(t.getName());
        res.setPhone(t.getPhone());
        res.setBaseSalary(t.getSalaryBase());
        res.setPosition(t.getPosition());
        res.setUserId(t.getUserId());
        res.setActive(t.getActive());
        res.setCreatedAt(t.getCreatedAt());
        res.setUpdatedAt(t.getUpdatedAt());
        
        return res;
    }


}
