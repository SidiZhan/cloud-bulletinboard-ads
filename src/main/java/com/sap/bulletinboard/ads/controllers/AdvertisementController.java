package com.sap.bulletinboard.ads.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.sap.bulletinboard.ads.models.Advertisement;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(AdvertisementController.PATH)
//@RequestScope
public class AdvertisementController {
    public static final String PATH = "/api/v1/ads";
    private static final Map<Long, Advertisement> ads = new HashMap<>();
    
    @GetMapping
    public Iterable<Advertisement> advertisements(){
        return ads.values();
    }
    
    @GetMapping("/{id}")
    public Advertisement advertisementById(@PathVariable("id") Long id) {
        if(!ads.containsKey(id)) {
            throw new NotFoundException(id + " not found!");
        }
        return ads.get(id);
    }
    
    @PostMapping
    public ResponseEntity<Advertisement> add(@RequestBody Advertisement advertisement,
            UriComponentsBuilder uriComponentsBuilder) throws URISyntaxException {
        Long id = new Long(ads.size());
        ads.put(id, advertisement);
        UriComponents uriComponents = uriComponentsBuilder.path(PATH + "/{id}").buildAndExpand(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(uriComponents.getPath()));
        return new ResponseEntity<Advertisement>(advertisement, headers, HttpStatus.CREATED);
    }
}
