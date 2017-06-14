package com.eat.listeners;

//@Getter
//@Setter
//@Service
//public class GenericEntityListener {
//
//    @Autowired
//    private DetailTypeRepository repository;
//
//    @PrePersist
//    public void beforeSave(DetailType entity){
//        if(repository == null) {
//            AutowireHelper.autowire(this, repository);
//        }
//        DetailType detailType = repository.findByName(entity.getName());
//        if(detailType != null){
//            entity.setId(detailType.getId());
//        }
//    }
//}
