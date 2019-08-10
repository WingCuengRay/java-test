package com.h2rd.refactoring.usermanagement;

import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class User {
    private String name;
    private String email;
    private Set<Role> roles;

    public enum Role {
        MASTER, ADMIN
    }
}
