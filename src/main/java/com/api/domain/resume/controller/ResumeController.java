package com.api.domain.resume.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.resume.entity.Resume;
import com.api.domain.resume.service.ResumeService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeService resumeService;

    @GetMapping("/")
    public List<Resume> getAllResumes() {
        return resumeService.getAllResumes();
    }
    
    @GetMapping("/{resumeId}")
    public Resume getResume(@PathVariable Long resumeId) {
        return resumeService.getResumeById(resumeId);
    }
}
