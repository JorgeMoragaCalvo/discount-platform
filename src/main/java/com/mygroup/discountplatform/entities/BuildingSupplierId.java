package com.mygroup.discountplatform.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class BuildingSupplierId implements Serializable {

    @Column(name = "id_building")
    private Long buildingId;

    @Column(name = "id_supplier")
    private Long supplierId;
}
