package com.example.demojsonb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="person")
public class PersonWithoutAddressJsonbObject {

    @Id
    private Integer pid;

    private String firstname;
    private String lastname;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name="address", columnDefinition = "jsonb")
    private String address;
}
