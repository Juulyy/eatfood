package com.eat.controllers.b2b;

import com.eat.models.b2b.ContactType;
import com.eat.services.b2b.ContactTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/api/contact-type")
public class ContactTypeController {

    @Autowired
    ContactTypeService contactTypeService;

    /**
     * ///////////////////////////////////////////CRUD OPERATION////////////////////////////
     */

    /**
     * /findAll
     * @return http://localhost:8090/api/contact/types/find-all/
     */
    @GetMapping(value = "/find-all", produces = "application/json")
    public Collection<ContactType> findAll(){
        return contactTypeService.findAll();
    }

    /**
     * /{id} as PathVariable
     * @param id
     * @return http://localhost:8090/api/contact/types/2
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    public ContactType getContactType(@PathVariable("id") Long id){
        return contactTypeService.findById(id);
    }

    /**
     * POST /save @RequestBody ContactType contactType
     * @param contactType
     * @return http://localhost:8090/api/contact/types/add
     */
    @PostMapping(value = "/add", produces = "application/json")
    public ContactType contactType(@RequestBody ContactType contactType){
        return contactTypeService.save(contactType);
    }

    /**
     * PATCH(needeable to update all fields) @RequestBody ContactType contactType
     * @param contactType
     * @return http://localhost:8090/api/contact/types/5
     */
    @PatchMapping(value = "/{id}", produces = "application/json")
    public ContactType updateContactType(@RequestBody ContactType contactType){
        return contactTypeService.update(contactType);
    }

    /** DELETE
     * http://localhost:8090/api/contact/types/2
     * @param id
     */
    @DeleteMapping(value = "/{id}")
    public void deleteContactType(@PathVariable Long id){
        contactTypeService.delete(id);
    }

    /**
     * ////////////////////////////ADDITIONAL SEARCHING OPERATION/////////////////////////////
     */

    /**
     * /findByName @RequestParam(value = "name")
     * @param name
     * @return http://localhost:8090/api/contact/types/find-by-name?name=email
     */
    @GetMapping(value = "/find-by-name", produces = "application/json")
    public List<ContactType> getContactTypeByName(@RequestParam(value = "name") String name){
        return new ArrayList<>(contactTypeService.findContactTypeByName(name));
    }

    /**
     * /findByNameContaining
     * @param name
     * @return http://localhost:8090/api/contact/types/find-by-name-containing?name=Delive
     */
    @GetMapping(value = "/find-by-name-containing", produces = "application/json")
    public List<ContactType> getAdditionalOptionByNameContaining(@RequestParam(value = "name") String name){
        return contactTypeService.findContactTypeByNameContaining(name);
    }

    /**
     * /findByIds @RequestParam(value = "ids")
     * @param ids
     * @return http://localhost:8090/api/contact/types/find-by-ids?ids=1&ids=2
     */
    @GetMapping(value = "/find-by-ids", produces = "application/json")
    public List<ContactType> getContactTypeByIds(@RequestParam(value = "ids") List<Long> ids){
        return contactTypeService.findContactTypeByIds(ids);
    }

    /**
     * /findByContactAspect
     * @param contactAspect
     * @return http://localhost:8090/api/contact/types/find-by-contact-aspect?contactAspect=PHONE
     */
    @GetMapping(value = "/find-by-contact-aspect", produces = "application/json")
    public List<ContactType> getContactTypeByContactAspect(@RequestParam(value = "contactAspect") String contactAspect){
        return contactTypeService.findContactTypeByContactAspect(contactAspect);
    }

}
