package com.portscanner.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import com.portscanner.core.ScanEngine;
import com.portscanner.core.ScanPolicy;
import com.portscanner.model.ScanReport;
import com.portscanner.model.ScanTarget;
import com.portscanner.scanner.SubnetScanner;
import com.portscanner.security.Auditlogger;



@Controller
public class ScanController {

    @Autowired
    private ScanProgressService progressService;

    @Autowired
    private ScanHistoryService historyService;

    private boolean isOnline() {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("google.com", 80), 3000);
            socket.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @GetMapping("/")
    public String index(Model model) {
        return "home";
    }
    
    @GetMapping("/alerts")
    @ResponseBody
    public List<String> alerts() {
        return progressService.getAlerts();
    }

    @GetMapping("/scanner")
    public String scanner(Model model) {
        return "index";
    }



    @GetMapping("/progress")
    @ResponseBody
    public int progress() {
        return progressService.getProgress();
    }

    @GetMapping("/history")
    public String history(Model model) {
        model.addAttribute("history", historyService.loadAll());
        return "history";
    }

    @GetMapping("/history/{scanId}")
    public String detail(@PathVariable String scanId, Model model) {
        ScanReport report = historyService.findById(scanId);
        if (report == null) {
            return "redirect:/history";
        }
        model.addAttribute("report", report);
        model.addAttribute("mode", "host");
        model.addAttribute("online", false);
        return "results";
    }

    @GetMapping("/scan")
    public String scan(
            @RequestParam String mode,
            @RequestParam(required = false) String host,
            @RequestParam(required = false) String cidr,
            @RequestParam int startPort,
            @RequestParam int endPort,
            @RequestParam(defaultValue = "false") boolean stealth,
            @RequestParam(defaultValue = "false") boolean bannerGrabbing,
            @RequestParam(defaultValue = "false") boolean cveDetection,
            Model model) {

        progressService.reset();

        boolean online = isOnline();
        progressService.setProgress(20);

        ScanPolicy policy = new ScanPolicy();
        policy.setConnectionTimeout(2000);
        policy.setStealthMode(stealth);
        policy.setOnlineMode(online);
        policy.setBannerGrabbing(bannerGrabbing);
        Auditlogger logger = new Auditlogger("audit.log");
        progressService.setProgress(40);

        if (mode.equals("subnet")) {
            SubnetScanner subnetScanner = new SubnetScanner(policy, logger, startPort, endPort);
            progressService.setProgress(60);

            List<ScanReport> reports = subnetScanner.scan(cidr);
            progressService.setProgress(80);

            for (ScanReport r : reports) {
                historyService.save(r);
            }

            model.addAttribute("reports", reports);
            model.addAttribute("mode", "subnet");
            model.addAttribute("online", online);

        } else {
            ScanTarget target = new ScanTarget(host, startPort, endPort);
            ScanEngine engine = new ScanEngine(policy, logger, progressService);
            progressService.setProgress(60);

            ScanReport report = engine.scan(target);
            engine.shutdown();
            historyService.save(report);
            progressService.setProgress(80);

            model.addAttribute("report", report);
            model.addAttribute("mode", "host");
            model.addAttribute("online", online);
        }

        progressService.setProgress(100);
        return "results";
    }
}