package com.urbanaura.urbanaura.controller;

import com.urbanaura.urbanaura.model.Property;
import com.urbanaura.urbanaura.model.Locality;
import com.urbanaura.urbanaura.repository.PropertyRepository;
import com.urbanaura.urbanaura.repository.LocalityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PropertyRepository propertyRepository;
    
    @Autowired
    private LocalityRepository localityRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("properties", propertyRepository.findAll());
        model.addAttribute("localities", localityRepository.findAll());
        return "admin-dashboard";
    }

    @GetMapping("/property/new")
    public String newPropertyForm(Model model) {
        model.addAttribute("property", new Property());
        model.addAttribute("localities", localityRepository.findAll());
        return "admin-property-form";
    }
    
    @PostMapping("/property/save")
    public String saveProperty(@ModelAttribute Property property, @RequestParam("localityId") Long localityId) {
        if (property.getId() == null) {
            long maxId = propertyRepository.findAll().stream().mapToLong(Property::getId).max().orElse(0);
            property.setId(maxId + 1);
        }
        Locality locality = localityRepository.findById(localityId).orElse(null);
        if (locality != null) {
            property.setLocality(locality);
            propertyRepository.save(property);
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/property/delete/{id}")
    public String deleteProperty(@PathVariable Long id) {
        propertyRepository.deleteById(id);
        return "redirect:/admin/dashboard";
    }
}
