/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kalsym.service.vertical.controller;

import com.kalsym.service.vertical.ServiceVerticalApplication;
import com.kalsym.service.vertical.model.Resource;
import com.kalsym.service.vertical.model.product.Product;
import com.kalsym.service.vertical.model.store.Store;
import com.kalsym.service.vertical.repository.ProductRepository;
import com.kalsym.service.vertical.repository.ResourceRepository;
import com.kalsym.service.vertical.repository.StoreRepository;
import com.kalsym.service.vertical.utility.HttpResponse;
import com.kalsym.service.vertical.utility.Logger;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hasan
 */
/**
 *
 * This controller is used for get,post,put and delete operations of a resource
 */
@RestController
@RequestMapping("/resources/")
@CrossOrigin("*")
public class ResourceController {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping(path = {""}, name = "resource-post", produces = "application/json")
    public ResponseEntity<HttpResponse> postResource(HttpServletRequest request,
            @RequestParam(name = "storeId", required = true) String storeId,
            @RequestParam(name = "productId", required = true) String productId,
            @RequestBody Resource bodyResource) {
        String logPrefix = request.getRequestURI();
        HttpResponse response = new HttpResponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, "", "");

        Optional<Store> optionalStore = storeRepository.findById(storeId);

        if (!optionalStore.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, " NOT_FOUND storeId: " + storeId);
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setError("store not found");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (!optionalProduct.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, " NOT_FOUND productId: " + productId);
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setError("product not found");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        List<Resource> resources = resourceRepository.findAll();

        //Check if a resource of product already exists, send conflict
        if (!resources.isEmpty()) {

            for (Resource resource : resources) {
                if (resource.getResourceProduct().getId().equals(productId)) {
                    Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, " Resource Already Exists ");
                    response.setStatus(HttpStatus.CONFLICT);
                    response.setError("Resource Already Exists");
                    return ResponseEntity.status(response.getStatus()).body(response);
                }
            }
        }

        bodyResource.setResourceProduct(optionalProduct.get());
        bodyResource.setName(bodyResource.getResourceProduct().getName());
        Resource savedResource = resourceRepository.save(bodyResource);

        response.setStatus(HttpStatus.CREATED);
        response.setData(savedResource);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping(path = {""}, name = "resource-put", produces = "application/json")
    public ResponseEntity<HttpResponse> putResource(HttpServletRequest request,
            @RequestParam(name = "storeId", required = true) String storeId,
            @RequestParam(name = "resourceId", required = true) String resourceId,
            @RequestBody Resource bodyResource) {
        String logPrefix = request.getRequestURI();
        HttpResponse response = new HttpResponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, "", "");

        Optional<Store> optionalStore = storeRepository.findById(storeId);

        if (!optionalStore.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, " NOT_FOUND storeId: " + storeId);
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setError("store not found");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        Optional<Resource> optionalResource = resourceRepository.findById(resourceId);

        if (!optionalResource.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, " NOT_FOUND resourceId: " + resourceId);
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setError("Resource not found");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        List<Resource> resources = resourceRepository.findAll();

        if (!resources.isEmpty()) {

            for (Resource resource : resources) {
                if (null != bodyResource.getResourceProduct()) {
                    if (resource.getResourceProduct().getId().equals(bodyResource.getResourceProduct().getId())) {
                        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, " Resource Already Exists ");
                        response.setStatus(HttpStatus.CONFLICT);
                        response.setError("Resource Already Exists");
                        return ResponseEntity.status(response.getStatus()).body(response);
                    }
                }
            }
        }

        Resource resource = optionalResource.get();
        resource.update(bodyResource);

        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, " Resource updated for Id: " + resourceId, "");
        response.setStatus(HttpStatus.ACCEPTED);
        response.setData(resourceRepository.save(resource));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
    
    @GetMapping(path = {""}, name = "resource-get-all", produces = "application/json")
    public ResponseEntity<HttpResponse> getResources(HttpServletRequest request,
            @RequestParam(name = "storeId", required = true) String storeId) {

        String logPrefix = request.getRequestURI();
        HttpResponse response = new HttpResponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, "", "");

        Optional<Store> optStore = storeRepository.findById(storeId);

        if (!optStore.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, " NOT_FOUND storeId: " + storeId);
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setError("store not found");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        List<Resource> resources = resourceRepository.findAll();

        response.setStatus(HttpStatus.OK);
        response.setData(resources);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping(path = {"/find/"}, name = "resource-get-by-id", produces = "application/json")
    public ResponseEntity<HttpResponse> getResourcesById(HttpServletRequest request,
            @RequestParam(name = "storeId", required = true) String storeId,
            @RequestParam(name = "resourceId", required = true) String resourceId) {

        String logPrefix = request.getRequestURI();
        HttpResponse response = new HttpResponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, "", "");

        Optional<Store> optStore = storeRepository.findById(storeId);

        if (!optStore.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, " NOT_FOUND storeId: " + storeId);
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setError("store not found");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        Optional<Resource> optionalResource = resourceRepository.findById(resourceId);

        if (!optionalResource.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logPrefix, " NOT_FOUND resourceId " + resourceId);
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setError("resource not found");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        response.setStatus(HttpStatus.OK);
        response.setData(optionalResource.get());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping(path = {""}, name = "resource-delete-by-id", produces = "application/json")
    public ResponseEntity<HttpResponse> deleteResourceById(HttpServletRequest request,
            @RequestParam(name = "storeId", required = true) String storeId,
            @RequestParam(name = "resourceId", required = true) String resourceId) {
        String logprefix = request.getRequestURI();
        HttpResponse response = new HttpResponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "", "");

        Optional<Store> optionalStore = storeRepository.findById(storeId);

        if (!optionalStore.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, " NOT_FOUND storeId: " + storeId);
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setError("store not found");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        Optional<Resource> optionalResource = resourceRepository.findById(resourceId);

        if (!optionalResource.isPresent()) {
            Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, " NOT_FOUND: " + resourceId, "");
            response.setStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "Reservation Resource found", "");
        resourceRepository.delete(optionalResource.get());
        Logger.application.info(Logger.pattern, ServiceVerticalApplication.VERSION, logprefix, "resource deleted, with id: {}", resourceId);
        response.setStatus(HttpStatus.OK);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
