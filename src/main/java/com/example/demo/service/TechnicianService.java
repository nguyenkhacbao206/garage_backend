package com.example.demo.service;

import com.example.demo.dto.TechnicianRequest;
import com.example.demo.dto.TechnicianResponse;
import com.example.demo.entity.Technician;
import com.example.demo.repository.TechnicianRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TechnicianService {

    private final TechnicianRepository technicianRepository;

    public TechnicianService(TechnicianRepository technicianRepository) {
        this.technicianRepository = technicianRepository;
    }

    public List<TechnicianResponse> getAll() {
        return technicianRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TechnicianResponse create(TechnicianRequest req) {
        if (req.getPhone() != null && technicianRepository.existsByPhone(req.getPhone())) {
            throw new DuplicateKeyException("Số điện thoại đã tồn tại");
        }

        Technician t = new Technician();
        t.setName(req.getName());
        t.setPhone(req.getPhone());
        t.setSalaryBase(req.getBaseSalary());
        t.setPosition(req.getPosition());
        t.setUserId(req.getUserId());
        t.setActive(true);

        Technician saved = technicianRepository.save(t);
        return toResponse(saved);
    }

    public TechnicianResponse update(String id, TechnicianRequest req) {
        Technician t = technicianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kỹ thuật viên với id: " + id));

        if (req.getName() != null) t.setName(req.getName());
        if (req.getPhone() != null) {
            if (!req.getPhone().equals(t.getPhone()) && technicianRepository.existsByPhone(req.getPhone())) {
                throw new DuplicateKeyException("Số điện thoại đã tồn tại");
            }
            t.setPhone(req.getPhone());
        }
        if (req.getBaseSalary() != null) t.setSalaryBase(req.getBaseSalary());
        if (req.getPosition() != null) t.setPosition(req.getPosition());
        if (req.getUserId() != null) t.setUserId(req.getUserId());

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
        return new TechnicianResponse(
                t.getId(),
                t.getName(),
                t.getPhone(),
                t.getSalaryBase(),
                t.getPosition(),
                t.getUserId(),
                t.getActive()
        );
    }
}
