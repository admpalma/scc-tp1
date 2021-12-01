package scc.rest.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationModel {

    private String user;
    private String pwd;
}
