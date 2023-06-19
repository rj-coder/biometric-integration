package in.westerncoal.biometric.model;

import java.sql.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Timestamps {

    @CreationTimestamp
    private Date createTimestamp;

    @UpdateTimestamp
    private Date updateTimestamp;


}
