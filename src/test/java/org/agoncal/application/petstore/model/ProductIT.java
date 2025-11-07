package org.agoncal.application.petstore.model;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import jakarta.validation.Validator;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Antonio Goncalves
 */
public class ProductIT {

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
                .addClasses(Category.class, Product.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    // ======================================
    // =              Methods               =
    // ======================================

    @Test
    public void shouldCreateAValidProduct() {

        // Creates an object
        Category category = new Category("Fish", "Any of numerous cold-blooded aquatic vertebrates characteristically having fins, gills, and a streamlined body");
        Product product = new Product("Bulldog", "Friendly dog from England", category);

        // Checks the object is valid
        assertEquals(0, validator.validate(product).size(), "Should have not constraint violation");
    }

    @Test
    public void shouldBeAbleToMarshallAndUnmarchallIntoXML() throws Exception {

        // Creates an object
        Category category = new Category("Fish", "Any of numerous cold-blooded aquatic vertebrates characteristically having fins, gills, and a streamlined body");
        Product product = new Product("Bulldog", "Friendly dog from England", category);

        // Marshalls it to XML
        StringWriter writer = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(Product.class);
        Marshaller m = context.createMarshaller();
        m.marshal(product, writer);
    }
}