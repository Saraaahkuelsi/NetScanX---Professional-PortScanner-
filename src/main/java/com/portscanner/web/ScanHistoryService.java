package com.portscanner.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.portscanner.model.ScanReport;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class ScanHistoryService {

    private static final String HISTORY_FILE = "history.json";
    private ObjectMapper mapper;

    public ScanHistoryService() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    public void save(ScanReport report) {
        try {
            List<ScanReport> history = loadAll();
            history.add(report);
            mapper.writeValue(new File(HISTORY_FILE), history);
        } catch (Exception e) {
            System.out.println("Erreur sauvegarde historique : " + e.getMessage());
        }
    }
    
    public ScanReport findById(String scanId) {
        List<ScanReport> history = loadAll();
        for (ScanReport r : history) {
            if (r.getScanId() != null && r.getScanId().equals(scanId)) {
                return r;
            }
        }
        return null;
    }

    public List<ScanReport> loadAll() {
        try {
            File file = new File(HISTORY_FILE);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            ScanReport[] reports = mapper.readValue(file, ScanReport[].class);
            return new ArrayList<>(Arrays.asList(reports));
        } catch (Exception e) {
            System.out.println("Erreur chargement historique : " + e.getMessage());
            return new ArrayList<>();
        }
    }
}