package com.devsuperior.bds04.projections;

public interface UserDetailsProjection {

    String getUserName();
    String getPassword();
    Long getRoleId();
    String getAuthority();
}
