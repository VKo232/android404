/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.afweb.model;

import java.util.regex.Pattern;

import static com.afweb.model.SymbolNameObj.strReplace;

/**
 *
 * @author eddy
 */
public class NameObj {

    private String NormalizeName;

    public NameObj(String strName) {
        strName = strName.trim().toUpperCase();
        strName = strReplace(strName, ".", "-");
        strName = strReplace(strName, "@", "-");
        strName = strReplace(strName, " ", "");
        setNormalizeName(strName);
    }

    /**
     * @return the NormalizeName
     */
    public String getNormalizeName() {
        return NormalizeName;
    }

    /**
     * @param NormalizeName the NormalizeName to set
     */
    public void setNormalizeName(String NormalizeName) {
        this.NormalizeName = NormalizeName;
    }

    public static boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."
                + "[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                + "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }
}
