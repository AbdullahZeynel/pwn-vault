package com.redteam.vulndb.controller;

import com.redteam.vulndb.entity.Vulnerability;
import com.redteam.vulndb.service.VulnerabilityService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controller for serving proof-of-concept images stored as BLOBs.
 */
@Controller
public class ImageController {

    private final VulnerabilityService vulnerabilityService;

    public ImageController(VulnerabilityService vulnerabilityService) {
        this.vulnerabilityService = vulnerabilityService;
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        return vulnerabilityService.findById(id)
                .filter(vuln -> vuln.getProofImage() != null)
                .map(vuln -> {
                    HttpHeaders headers = new HttpHeaders();
                    String contentType = vuln.getImageContentType() != null
                            ? vuln.getImageContentType()
                            : "image/png";
                    headers.setContentType(MediaType.parseMediaType(contentType));
                    return new ResponseEntity<>(vuln.getProofImage(), headers, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
