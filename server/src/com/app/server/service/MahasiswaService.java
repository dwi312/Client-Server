package com.app.server.service;

import java.util.List;

import com.app.server.model.Mahasiswa;
import com.app.server.repository.MahasiswaRepository;

public class MahasiswaService {
    private final MahasiswaRepository repo = new MahasiswaRepository();
    
    public boolean createMahasiswa(Mahasiswa m) {
        if (m.getNim() == null || m.getNim().trim().isEmpty()) {
            return false;
        }

        if (repo.findByNim(m.getNim()) != null) {
            return false;
        }
        return repo.save(m);
    }

    public List<Mahasiswa> listAll() {
        return repo.findAll();
    }

    public Mahasiswa getByNim(String nim) {
        return repo.findByNim(nim);
    }

    public boolean updateMahasiswa(Mahasiswa m) {
        if (repo.findByNim(m.getNim()) == null) return false;
        return repo.update(m);
    }

    public boolean deleteByNim(String nim) {
        return repo.deleteByNim(nim);
    }
}
