package com.eat.models.common;

import com.eat.models.common.enums.ImageType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "IMAGE")
public class Image implements Serializable {

    private static final long serialVersionUID = -1698024016029067852L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_ID")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "TYPE")
    private ImageType type;

    @NonNull
    @Column(name = "IMAGE_URL")
    private String imageUrl;

}