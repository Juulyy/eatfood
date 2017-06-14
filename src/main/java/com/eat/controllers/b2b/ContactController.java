package com.eat.controllers.b2b;

import com.eat.models.b2b.Contact;
import com.eat.services.b2b.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/api/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;


    /**
     * ///////////////////////////////////////////CRUD OPERATION////////////////////////////
     */

    /**
     * /findAll
     * @return http://localhost:8090/api/contacts/find-all/
     */
    @GetMapping(value = "/find-all", produces = "application/json")
    public Collection<Contact> findAll(){
        return contactService.findAll();
    }

    /**
     * /{id} as PathVariable
     * @param id
     * @return http://localhost:8090/api/contacts/2
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    public Contact getContact(@PathVariable("id") String id){
        return contactService.getById(id);
    }

    /**
     * POST /add @RequestBody Contact contact
     * @param contact
     * @return http://localhost:8090/api/contacts/add
     */
    @PostMapping(value = "/add", produces = "application/json")
    public Contact contact(@RequestBody Contact contact){
        return contactService.add(contact);
    }

    /**
     * PATCH(needeable to update all fields) @RequestBody Contact contact
     * @param contact
     * @return http://localhost:8090/api/contacts/5
     */
    @PatchMapping(value = "/{id}", produces = "application/json")
    public Contact updateContact(@RequestBody Contact contact){
        return contactService.update(contact);
    }

    /** DELETE
     * http://localhost:8090/api/contacts/2
     * @param id
     */
    @DeleteMapping(value = "/{id}")
    public void deleteContact(@PathVariable Long id){
        contactService.deleteById(id);
    }

    /**
     * ////////////////////////////ADDITIONAL SEARCHING OPERATION/////////////////////////////
     */

     /**
     * /findByIds @RequestParam(value = "ids")
     * @param ids
     * @return http://localhost:8090/api/contacts/find-by-ids?ids=1&ids=2
     */
    @GetMapping(value = "/find-by-ids", produces = "application/json")
    public List<Contact> getContactByIds(@RequestParam(value = "ids") List<Long> ids){
        return new ArrayList<>(contactService.findContactByIds(ids));
    }

}


