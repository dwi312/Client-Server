package com.app.server.controller;

import java.util.List;

import com.app.server.core.Response;
import com.app.server.model.Mahasiswa;
import com.app.server.service.MahasiswaService;

public class MahasiswaController {
    private final MahasiswaService service = new MahasiswaService();

    public Response create(Mahasiswa m) {
        boolean ok = service.createMahasiswa(m);

        if (ok) {
            return new Response(201, "Mahasiswa created", m);
        } else {
            return new Response(400, "Failed to create (maybe NIM exists or invalid)", null);
        }
    }

    public Response list() {
        List<Mahasiswa> list = service.listAll();
        return new Response(200, "ok", list);
    }

    public Response get(String nim) {
        Mahasiswa m = service.getByNim(nim);
        
        if (m != null) {
            return new Response(200, "ok", m);
        } else {
            return new Response(404, "Not Found", null);
        }
    }

    public Response update(Mahasiswa m) {
        boolean ok = service.updateMahasiswa(m);
        if (ok) {
            return new Response(200, "Updated", m);
        } else {
            return new Response(404, "Not found", null);
        }
    }

    public Response delete(String nim) {
        boolean ok = service.deleteByNim(nim);
        if (ok) {
           return new Response(200, "Deleted", null);
        } else {
           return new Response(404, "Not found", null);
        }
    }

}
