package org.agoncal.application.petstore.model;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import jakarta.validation.Validator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Antonio Goncalves
 */
public class CustomerIT {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Inject
    private Validator validator;

    // ======================================
    // =          Lifecycle Methods         =
    // ======================================

    @Deployment
    public static JavaArchive jar() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(Customer.class, Category.class, Address.class, Country.class, UserRole.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    // ======================================
    // =              Methods               =
    // ======================================

    @Test
    public void shouldCreateAValidCustomer() {

        // Creates an object
        Country country = new Country("DV", "Dummy value", "Dummy value", "DMV", "DMV");
        Address address = new Address("78 Gnu Rd", "Texas", "666", country);
        Customer customer = new Customer("Paul", "Mc Cartney", "pmac", "pmac", "paul@beales.com", address);

        // Checks the object is valid
        assertEquals(0, validator.validate(customer).size(), "Should have not constraint violation");
    }
}