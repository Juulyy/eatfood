package com.eat.models.mongo;

import com.eat.models.b2b.Place;
import com.eat.models.mongo.enums.ChangeRequestStatus;
import com.eat.models.mongo.enums.ChangeRequestType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel("Request from b2b user for place content changing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "place_change_requests")
public class PlaceChangeRequest implements Serializable, Convertible {

    private static final long serialVersionUID = 1770241498371459885L;

    @Id
    private String id;

    @NotNull
    @JsonProperty(value = "sql_object_id")
    @Indexed(name = "sql_object_id")
    private Long sqlObjectId;

    @NotNull
    @Field(value = "status")
    @JsonProperty(value = "status")
    private ChangeRequestStatus status;

    @CreatedDate
    @JsonProperty(value = "createdDate")
    @Field(value = "createdDate")
    private LocalDateTime createdDate;

    @JsonProperty(value = "viewedDate")
    @Field(value = "viewedDate")
    private LocalDateTime viewedDate;

    @Field(value = "object")
    @JsonProperty(value = "object")
    private AbstractPlaceChangeRequest object;

    @NotNull
    @Field(value = "requestType")
    @JsonProperty(value = "requestType")
    private ChangeRequestType requestType;

    @Builder(builderMethodName = "of", buildMethodName = "create")
    public PlaceChangeRequest(Place place, ChangeRequestType requestType) {
        this.sqlObjectId = place.getId();
        this.status = ChangeRequestStatus.PENDING;
        this.object = this.toRequestObject(place, requestType);
        this.requestType = requestType;
    }

    @Override
    public Place toPlace() {
        ChangeRequestType type = this.getRequestType();
        Place place = null;
        switch (type) {
            case MENUS:
                PlaceMenusChangeRequest placeMenusRequest = (PlaceMenusChangeRequest) this.getObject();
                place.setMenus(placeMenusRequest.getMenus());
                break;
            case OFFERS:
                PlaceOffersChangeRequest placeOffersRequest = (PlaceOffersChangeRequest) this.getObject();
                place.setOffers(placeOffersRequest.getOffers());
                break;
            case PLACE_PAGE:
                PlacePageChangeRequest pageChangeRequest = (PlacePageChangeRequest) this.getObject();
                place.setName(pageChangeRequest.getName());
                place.setLongtitude(pageChangeRequest.getLongtitude());
                place.setLatitude(pageChangeRequest.getLatitude());
                place.setContacts(pageChangeRequest.getContacts());
                place.setSchedule(pageChangeRequest.getSchedule());
                place.setPlaceDetails(pageChangeRequest.getPlaceDetails());
                place.setImages(pageChangeRequest.getImages());
                place.setPriceLevel(pageChangeRequest.getPriceLevel());
                place.setBusinessPlan(pageChangeRequest.getBusinessPlan());
                break;
        }
        return place;
    }

}