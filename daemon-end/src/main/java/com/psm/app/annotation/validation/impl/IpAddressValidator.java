package com.psm.app.annotation.validation.impl;

import com.psm.app.annotation.validation.ValidIpAddress;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.InetAddress;

public class IpAddressValidator implements ConstraintValidator<ValidIpAddress, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            InetAddress inetAddress = InetAddress.getByName(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
